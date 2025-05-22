package com.app.my_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.my_project.entity.ProductionLossEntity;

public interface ProductionLossRepository extends JpaRepository<ProductionLossEntity, Long> {
    List<ProductionLossEntity> findAllByProductionIdOrderByIdDesc(Long productionId);
}
