package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PackageRepository extends JpaRepository<Package, UUID> {

}
