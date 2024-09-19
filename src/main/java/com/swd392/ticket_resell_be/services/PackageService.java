package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.dtos.requests.PackageDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.Package;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PackageService {
    ApiItemResponse<Package> createPackage(PackageDtoRequest pkgDto);
    ApiItemResponse<Optional<Package>> getPackageById(UUID packageId);
    ApiItemResponse<List<Package>> getAllPackages();
    ApiItemResponse<Package> updatePackage(UUID packageId, PackageDtoRequest pkgDto);
    ApiItemResponse<Void> deletePackage(UUID packageId);
}
