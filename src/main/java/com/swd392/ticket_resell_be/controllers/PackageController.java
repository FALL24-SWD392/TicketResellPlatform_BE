package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.PackageDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.Package;
import com.swd392.ticket_resell_be.services.PackageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/packages")
public class PackageController {
    PackageService packageService;

    @PostMapping
    public ResponseEntity<ApiItemResponse<Package>> createPackage(@RequestBody @Valid PackageDtoRequest packageDtoRequest) {
        ApiItemResponse<Package> response = packageService.createPackage(packageDtoRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiItemResponse<Optional<Package>>> getPackageById(@PathVariable("id") UUID packageId) {
        ApiItemResponse<Optional<Package>> response = packageService.getPackageById(packageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiItemResponse<List<Package>>> getAllPackages() {
        ApiItemResponse<List<Package>> response = packageService.getAllPackages();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiItemResponse<Package>> updatePackage(@PathVariable("id") UUID packageId, @RequestBody @Valid PackageDtoRequest packageDtoRequest) {
        ApiItemResponse<Package> response = packageService.updatePackage(packageId, packageDtoRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiItemResponse<Void>> deletePackage(@PathVariable("id") UUID packageId) {
        ApiItemResponse<Void> response = packageService.deletePackage(packageId);
        return ResponseEntity.ok(response);
    }
}
