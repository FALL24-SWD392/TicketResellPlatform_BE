package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PackageRepository extends JpaRepository<Package, UUID> {

    // Custom query to find a package by its ID (optional)
    @Query("SELECT p FROM Package p WHERE p.id = :packageId")
    Optional<Package> findById(UUID packageId);

    // Custom query to find all packages (optional)
    @Query("SELECT p FROM Package p")
    List<Package> findAll();
}
