package com.app.my_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.my_project.entity.ProductionLogEntity;

public interface ProductionLogRepository extends JpaRepository<ProductionLogEntity, Long> {

}
