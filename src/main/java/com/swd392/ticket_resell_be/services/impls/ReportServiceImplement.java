package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.ReportDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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

            minusReputation(report.getReported().getId());

            report.setUpdatedBy(userService.getCurrentUser().data().username());
            report.setUpdatedAt(new Date());

            return apiResponseBuilder.buildResponse(
                    reportRepository.save(report),
                    HttpStatus.OK,
                    "Approved"
            );
        } else if (status.equals(Categorize.REJECTED)) {
            report.setStatus(Categorize.REJECTED);

            minusReputation(report.getReporter().getId());

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

    private void minusReputation(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            int newReputationPoint = user.get().getReputation() - 5;
            user.get().setReputation(newReputationPoint);
            if (user.get().getReputation() < 80) {
                int a = 1 + 1;
                //doi Hoang Dat lam updateUser
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
    public ApiListResponse<Report> getReportByUserId(UUID id, Categorize status) {
        List<UUID> uuidList = reportRepository.findReportByIdAndStatus(id, status)
                .stream()
                .map(Report::getId)
                .toList();
        List<Report> reportList = reportRepository.findAllById(uuidList);
        return apiResponseBuilder.buildResponse(
                reportList,
                0,
                0,
                0,
                0,
                HttpStatus.OK,
                null
        );
    }

    @Override
    public ApiListResponse<Report> getAllReportsByStatus(Categorize status) {
        return apiResponseBuilder.buildResponse(
                reportRepository.findAllByStatus(status),
                0,
                0,
                0,
                0,
                HttpStatus.OK,
                null
        );
    }


}
