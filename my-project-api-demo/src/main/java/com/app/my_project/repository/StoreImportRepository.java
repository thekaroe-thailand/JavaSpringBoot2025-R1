package com.app.my_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.my_project.entity.StoreImportEntity;

public interface StoreImportRepository extends JpaRepository<StoreImportEntity, Long> {
    List<StoreImportEntity> findByStoreId(Long storeId);
}
