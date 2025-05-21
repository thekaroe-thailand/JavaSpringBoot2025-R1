package com.app.my_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.my_project.entity.ProductionLogEntity;

public interface ProductionLogRepository extends JpaRepository<ProductionLogEntity, Long> {
    List<ProductionLogEntity> findAllByProductionIdOrderByIdDesc(Long productionId);
}

