package com.app.my_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.my_project.entity.MaterialEntity;

public interface MaterialRepository extends JpaRepository<MaterialEntity, Long> {
    List<MaterialEntity> findAllByOrderByIdDesc();
}
