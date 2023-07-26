package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {
}