package com.app.my_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.my_project.entity.SaleTempEntity;

public interface SaleTempRepository extends JpaRepository<SaleTempEntity, Long> {
    List<SaleTempEntity> findAllByUserIdOrderByIdDesc(Long userId);

    SaleTempEntity findByProductionIdAndUserId(Long productionId, Long userId);
}
