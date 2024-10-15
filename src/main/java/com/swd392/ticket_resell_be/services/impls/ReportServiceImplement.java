package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.PageDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.ReportDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.ReportDtoResponse;
import com.swd392.ticket_resell_be.entities.Report;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.OrderRepository;
import com.swd392.ticket_resell_be.repositories.ReportRepository;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import com.swd392.ticket_resell_be.services.ReportService;
import com.swd392.ticket_resell_be.services.UserService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.PagingUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
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
    OrderRepository orderRepository;
    ModelMapper modelMapper;
    ApiResponseBuilder apiResponseBuilder;
    UserRepository userRepository;
    UserService userService;
    PagingUtil pagingUtil;

    @Override
    public ApiItemResponse<Report> createReport(ReportDtoRequest reportDtoRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);

        Report report = modelMapper.map(reportDtoRequest, Report.class);
        boolean check = orderRepository.findById(report.getOrder().getId());
        if (!check) {
            throw new AppException(ErrorCode.USER_HAVE_NOT_YET_TRANSACTED);
        }
        report.setStatus(Categorize.PENDING);
        return apiResponseBuilder.buildResponse(
                reportRepository.save(report),
                HttpStatus.CREATED,
                null
        );
    }

    @Override
    public ApiItemResponse<Report> processReport(UUID id, Categorize status) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_FOUND));
        if (!report.getStatus().equals(Categorize.PENDING))
            throw new AppException(ErrorCode.REPORT_NOT_IN_PENDING_STATE);
        if (status.equals(Categorize.APPROVED)) {
            report.setStatus(Categorize.APPROVED);

            minusReputation(report.getReported());

            report.setUpdatedBy(userService.getCurrentUser().data().username());
            report.setUpdatedAt(new Date());

            return apiResponseBuilder.buildResponse(
                    reportRepository.save(report),
                    HttpStatus.OK,
                    "Approved"
            );
        } else if (status.equals(Categorize.REJECTED)) {
            report.setStatus(Categorize.REJECTED);

            minusReputation(report.getReporter());

            report.setUpdatedBy(userService.getCurrentUser().data().username());
            report.setUpdatedAt(new Date());

            return apiResponseBuilder.buildResponse(
                    reportRepository.save(report),
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
    public ApiItemResponse<Report> getById(UUID id) {
        return apiResponseBuilder.buildResponse(
                reportRepository.findReportByIdIs(id),
                HttpStatus.OK,
                null
        );
    }

    @Override
    public ApiListResponse<ReportDtoResponse> getReportByUserId(UUID id, Categorize status, PageDtoRequest pageDtoRequest) {
        Page<Report> reports = reportRepository.findReportByIdAndStatus(id, status, pagingUtil.getPageable(pageDtoRequest));
        if (reports.getTotalElements() > 0) {
            throw new AppException(ErrorCode.REPORT_NOT_FOUND);
        }
        return apiResponseBuilder.buildResponse(
                parseToReport(reports),
                reports.getSize(),
                reports.getNumber(),
                reports.getTotalElements(),
                reports.getTotalPages(),
                HttpStatus.OK,
                null
        );
    }

    @Override
    public ApiListResponse<ReportDtoResponse> getAllReportsByStatus(Categorize status, PageDtoRequest pageDtoRequest) {
        Page<Report> reports = reportRepository.findAllByStatus(status, pagingUtil.getPageable(pageDtoRequest));
        if (reports.getTotalElements() > 0) {
            throw new AppException(ErrorCode.REPORT_NOT_FOUND);
        }
        return apiResponseBuilder.buildResponse(
                parseToReport(reports),
                reports.getSize(),
                reports.getNumber(),
                reports.getTotalElements(),
                reports.getTotalPages(),
                HttpStatus.OK,
                null
        );
    }

    private List<ReportDtoResponse> parseToReport(Page<Report> reports) {
        return reports.getContent().stream()
                .map(report -> new ReportDtoResponse(
                        report.getId(),
                        report.getReporter().getId(),
                        report.getReported().getId(),
                        report.getOrder().getId(),
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
