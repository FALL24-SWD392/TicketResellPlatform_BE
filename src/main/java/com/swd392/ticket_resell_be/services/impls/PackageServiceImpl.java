package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.PackageDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.Package;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.PackageRepository;
import com.swd392.ticket_resell_be.services.PackageService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PackageServiceImpl implements PackageService {

    PackageRepository packageRepository;
    ApiResponseBuilder apiResponseBuilder;

    @Override
    public ApiItemResponse<Package> createPackage(PackageDtoRequest pkgDto) {
        Package pkg = new Package();
        pkg.setPackageName(pkgDto.packageName());
        pkg.setSaleLimit(pkgDto.saleLimit());
        pkg.setPrice(pkgDto.price());
        pkg.setImageUrls(pkgDto.imageUrls());

        Package savedPackage = packageRepository.save(pkg);
        return apiResponseBuilder.buildResponse(savedPackage, HttpStatus.CREATED, "Package created successfully");
    }

    @Override
    public ApiItemResponse<Optional<Package>> getPackageById(UUID packageId) {
        Optional<Package> pkg = packageRepository.findById(packageId);
        if (pkg.isPresent()) {
            return apiResponseBuilder.buildResponse(pkg, HttpStatus.OK, "Package found");
        } else {
            throw new AppException(ErrorCode.PACKAGE_NOT_FOUND);
        }
    }

    @Override
    public ApiItemResponse<List<Package>> getAllPackages() {
        List<Package> packages = packageRepository.findAll();
        return apiResponseBuilder.buildResponse(packages, HttpStatus.OK, "All packages retrieved");
    }

    @Override
    public ApiItemResponse<Package> updatePackage(UUID packageId, PackageDtoRequest pkgDto) {
        if (!packageRepository.existsById(packageId)) {
            throw new AppException(ErrorCode.PACKAGE_NOT_FOUND);
        }

        Package pkg = packageRepository.findById(packageId).orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_FOUND));
        pkg.setPackageName(pkgDto.packageName());
        pkg.setSaleLimit(pkgDto.saleLimit());
        pkg.setPrice(pkgDto.price());
        pkg.setImageUrls(pkgDto.imageUrls());

        Package updatedPackage = packageRepository.save(pkg);
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
}
