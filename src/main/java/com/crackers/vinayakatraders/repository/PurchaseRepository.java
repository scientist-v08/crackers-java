package com.crackers.vinayakatraders.repository;

import com.crackers.vinayakatraders.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
