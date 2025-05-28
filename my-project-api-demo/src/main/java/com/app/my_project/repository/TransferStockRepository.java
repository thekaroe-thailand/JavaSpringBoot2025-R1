package com.app.my_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.my_project.entity.TransferStockEntity;

public interface TransferStockRepository extends JpaRepository<TransferStockEntity, Long> {
    List<TransferStockEntity> findByFromStoreId(Long fromStoreId);
    List<TransferStockEntity> findByToStoreId(Long toStoreId);
}
