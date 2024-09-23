package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.PackageDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.Package;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.enums.TransactionStatus;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.PackageRepository;
import com.swd392.ticket_resell_be.repositories.SubscriptionRepository;
import com.swd392.ticket_resell_be.repositories.TransactionRepository;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import com.swd392.ticket_resell_be.services.PackageService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PackageServiceImplement implements PackageService {

    PackageRepository packageRepository;
    ApiResponseBuilder apiResponseBuilder;
    UserRepository userRepository;
    SubscriptionRepository subscriptionRepository;
    TransactionRepository transactionRepository;
    PayOS payOS;

    @Override
    public ApiItemResponse<Package> createPackage(PackageDtoRequest pkgDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User createdByUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Package pkg = new Package();
        pkg.setPackageName(pkgDto.getPackageName());
        pkg.setSaleLimit(pkgDto.getSaleLimit());
        pkg.setPrice(pkgDto.getPrice());
        pkg.setImageUrls(pkgDto.getImageUrls());
        pkg.setCreatedBy(createdByUser);
        pkg.setDuration(pkgDto.getDuration());
        pkg.setStatus(pkgDto.isActive());
        Package savedPackage = packageRepository.save(pkg);
        return apiResponseBuilder.buildResponse(savedPackage, HttpStatus.CREATED, "Package created successfully");
    }

    @Override
    public ApiItemResponse<Package> getPackageById(UUID packageId) {
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_FOUND));
        return apiResponseBuilder.buildResponse(pkg, HttpStatus.OK, "Package found");
    }

    @Override
    public ApiItemResponse<List<Package>> getAllPackages() {
        List<Package> packages = packageRepository.findAll();
        return apiResponseBuilder.buildResponse(packages, HttpStatus.OK, "All packages retrieved");
    }

    @Override
    public ApiItemResponse<Package> updatePackage(UUID packageId, PackageDtoRequest pkgDto) {
        Package existingPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_FOUND));
        existingPackage.setPackageName(pkgDto.getPackageName());
        existingPackage.setSaleLimit(pkgDto.getSaleLimit());
        existingPackage.setPrice(pkgDto.getPrice());
        existingPackage.setImageUrls(pkgDto.getImageUrls());
        existingPackage.setDuration(pkgDto.getDuration());
        existingPackage.setStatus(pkgDto.isActive());
        Package updatedPackage = packageRepository.save(existingPackage);
        return apiResponseBuilder.buildResponse(updatedPackage, HttpStatus.OK, "Package updated successfully");
    }

    @Override
    public ApiItemResponse<Void> deletePackage(UUID packageId) {
        if (!packageRepository.existsById(packageId)) {
            throw new AppException(ErrorCode.PACKAGE_NOT_FOUND);
        }
        packageRepository.deleteById(packageId);
        return apiResponseBuilder.buildResponse(null, HttpStatus.OK, "Package deleted successfully");
    }

    @Override
    public String purchasePackage(UUID packageId, UUID userId) throws Exception {
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        ItemData item = ItemData.builder()
                .name(pkg.getPackageName())
                .price(pkg.getPrice().intValue())
                .quantity(1)
                .build();
        PaymentData paymentData = PaymentData.builder()
                .orderCode(generateOrderCode())
                .amount(pkg.getPrice().intValue())
                .description("Purchase Package")
                .item(item)
                .returnUrl("http://your_return_url")
                .cancelUrl("http://your_cancel_url")
                .build();
        CheckoutResponseData checkoutData = payOS.createPaymentLink(paymentData);
        String checkoutUrl = checkoutData.getCheckoutUrl();
        savePendingTransaction(pkg, user, paymentData);
        return checkoutUrl;
    }

    private void savePendingTransaction(Package pkg, User user, PaymentData paymentData) {
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setAPackage(pkg);
        transaction.setUser(user);
        transaction.setAmount(pkg.getPrice());
        transaction.setDescription(String.valueOf(paymentData.getOrderCode()));
        transaction.setStatus(TransactionStatus.PENDING);
        transactionRepository.save(transaction);
    }

    public void confirmPayment(long orderCode) {
        Transaction transaction = (Transaction) transactionRepository.findTransactionsByDescription(String.valueOf(orderCode))
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));
        if (!TransactionStatus.PENDING.equals(transaction.getStatus())) {
            throw new AppException(ErrorCode.TRANSACTION_ALREADY_CONFIRMED);
        }
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
        Subscription subscription = new Subscription();
        subscription.setId(UUID.randomUUID());
        subscription.setUser(transaction.getUser());
        subscription.setPackageField(transaction.getAPackage());
        subscription.setStartDate(LocalDate.now());
        subscriptionRepository.save(subscription);
    }

    private long generateOrderCode() {
        return System.currentTimeMillis() % 100000;
    }

}
