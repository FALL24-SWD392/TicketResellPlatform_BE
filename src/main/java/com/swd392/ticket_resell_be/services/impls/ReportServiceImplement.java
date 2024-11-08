package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.ReportDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.ReportDtoResponse;
import com.swd392.ticket_resell_be.entities.Report;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.ReportRepository;
import com.swd392.ticket_resell_be.repositories.TicketRepository;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import com.swd392.ticket_resell_be.services.OrderService;
import com.swd392.ticket_resell_be.services.ReportService;
import com.swd392.ticket_resell_be.services.UserService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.PagingUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReportServiceImplement implements ReportService {
    ReportRepository reportRepository;
    OrderService orderService;
    ApiResponseBuilder apiResponseBuilder;
    UserRepository userRepository;
    UserService userService;
    PagingUtil pagingUtil;
    private final TicketRepository ticketRepository;

    @Override
    public ApiItemResponse<ReportDtoResponse> createReport(ReportDtoRequest reportDtoRequest) {
        Report report = new Report();
        mapperHandmade(report, reportDtoRequest);
        report.setStatus(Categorize.PENDING);
        reportRepository.save(report);
        return apiResponseBuilder.buildResponse(
                parseToReportDtoResponse(report),
                HttpStatus.CREATED
        );
    }

    @Override
    public ApiItemResponse<ReportDtoResponse> processReport(UUID id, Categorize status) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_FOUND));
        if (!report.getStatus().equals(Categorize.PENDING))
            throw new AppException(ErrorCode.REPORT_NOT_IN_PENDING_STATE);
        if (status.equals(Categorize.APPROVED)) {
            report.setStatus(Categorize.APPROVED);

            minusReputation(report.getReported());

            report.setUpdatedBy(userService.getCurrentUser().data().username());
            report.setUpdatedAt(new Date());

            reportRepository.save(report);
            return apiResponseBuilder.buildResponse(
                    parseToReportDtoResponse(report),
                    HttpStatus.OK,
                    "Approved"
            );
        } else if (status.equals(Categorize.REJECTED)) {
            report.setStatus(Categorize.REJECTED);

            minusReputation(report.getReporter());

            report.setUpdatedBy(userService.getCurrentUser().data().username());
            report.setUpdatedAt(new Date());

            reportRepository.save(report);
            return apiResponseBuilder.buildResponse(
                    parseToReportDtoResponse(report),
                    HttpStatus.OK,
                    "Rejected"
            );
        } else {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
    }

    private void minusReputation(User user) {
        if (user != null) {
            int newReputationPoint = user.getReputation() - 5;
            user.setReputation(newReputationPoint);
            if (user.getReputation() < 80) {
                user.setStatus(Categorize.BANNED);
                userRepository.save(user);
            }
        } else
            throw new AppException(ErrorCode.USER_NOT_FOUND);
    }

    @Override
    public ApiItemResponse<ReportDtoResponse> getById(UUID id) {
        return apiResponseBuilder.buildResponse(
                parseToReportDtoResponse(reportRepository.findReportByIdIs(id)),
                HttpStatus.OK,
                null
        );
    }

    @Override
    public ApiListResponse<ReportDtoResponse> getReportByUserId(UUID id, int page, int size, Sort.Direction direction, String... properties) {
        User user = userService.findById(id);
        Page<Report> reports = reportRepository.findByReportedOrReporter(user, user, pagingUtil
                .getPageable(Report.class, page, size, direction, properties));
        if (reports.getTotalElements() == 0) {
            throw new AppException(ErrorCode.REPORT_NOT_FOUND);
        }
        return apiResponseBuilder.buildResponse(
                parseToReportDtoResponses(reports),
                reports.getSize(),
                reports.getNumber() + 1,
                reports.getTotalElements(),
                reports.getTotalPages(),
                HttpStatus.OK,
                null
        );
    }

    @Override
    public ApiListResponse<ReportDtoResponse> getAllReportsByStatus(Categorize status, int page, int size, Sort.Direction direction, String... properties) {
        Page<Report> reports;
        if (status.equals(Categorize.ALL)) {
            reports = reportRepository.findAll(pagingUtil
                    .getPageable(Report.class, page, size, direction, properties));
        } else {
            reports = reportRepository.findAllByStatus(status, pagingUtil
                    .getPageable(Report.class, page, size, direction, properties));
        }
        if (reports.getTotalElements() == 0) {
            throw new AppException(ErrorCode.REPORT_NOT_FOUND);
        }
        return apiResponseBuilder.buildResponse(
                parseToReportDtoResponses(reports),
                reports.getSize(),
                reports.getNumber() + 1,
                reports.getTotalElements(),
                reports.getTotalPages(),
                HttpStatus.OK,
                null
        );
    }

    private void mapperHandmade(Report report, ReportDtoRequest reportDtoRequest) {
        boolean check_1 = orderService.findById(reportDtoRequest.order_id()).getChatBox().getSender() == userService.findById(reportDtoRequest.reporter_id());
        boolean check_2 = orderService.findById(reportDtoRequest.order_id()).getChatBox().getRecipient() == userService.findById(reportDtoRequest.reported_id());
        if (check_1 || check_2) {
            report.setReporter(userService.findById(reportDtoRequest.reporter_id()));
            report.setReported(userService.findById(reportDtoRequest.reported_id()));
            report.setOrder(orderService.findById(reportDtoRequest.order_id()));
            report.setDescription(reportDtoRequest.description());
            report.setAttachment(reportDtoRequest.attachment());
            report.setStatus(reportDtoRequest.status());
        }
    }

    private ReportDtoResponse parseToReportDtoResponse(Report report) {
        ReportDtoResponse reportDtoResponse = new ReportDtoResponse();
        reportDtoResponse.setId(report.getId());
        reportDtoResponse.setReporterName(report.getReporter().getUsername());
        reportDtoResponse.setReportedName(report.getReported().getUsername());
        reportDtoResponse.setTicketId(report.getOrder().getChatBox().getTicket().getId());
        reportDtoResponse.setTicketName(report.getOrder().getChatBox().getTicket().getTitle());
        reportDtoResponse.setDescription(report.getDescription());
        reportDtoResponse.setAttachment(report.getAttachment());
        reportDtoResponse.setStatus(report.getStatus());
        reportDtoResponse.setCreatedBy(report.getCreatedBy());
        reportDtoResponse.setCreatedAt(report.getCreatedAt());
        reportDtoResponse.setUpdatedBy(report.getUpdatedBy());
        reportDtoResponse.setUpdatedAt(report.getUpdatedAt());

        return reportDtoResponse;
    }

    private List<ReportDtoResponse> parseToReportDtoResponses(Page<Report> reports) {
        return reports.getContent().stream()
                .map(report -> new ReportDtoResponse(
                        report.getId(),
                        report.getReporter().getUsername(),
                        report.getReported().getUsername(),
                        report.getOrder().getChatBox().getTicket().getId(),
                        report.getOrder().getChatBox().getTicket().getTitle(),
                        report.getDescription(),
                        report.getAttachment(),
                        report.getStatus(),
                        report.getCreatedBy(),
                        report.getCreatedAt(),
                        report.getUpdatedBy(),
                        report.getUpdatedAt()))
                .toList();
    }
}
