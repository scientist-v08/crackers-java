package com.crackers.vinayakatraders.repository;

import com.crackers.vinayakatraders.entity.Routes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutesRepository extends JpaRepository<Routes, Long> {
    List<Routes> findByRole(Long role);
}
