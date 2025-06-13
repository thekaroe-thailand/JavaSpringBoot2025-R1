package com.app.my_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.my_project.entity.BillSaleDetailEntity;

public interface BillSaleDetailRepository extends JpaRepository<BillSaleDetailEntity, Long> {
    public List<BillSaleDetailEntity> findAllByBillSaleId(Long billSaleId);
}
