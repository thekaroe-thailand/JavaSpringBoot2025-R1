package com.app.my_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.my_project.entity.BillSaleEntity;

public interface BillSaleRepository extends JpaRepository<BillSaleEntity, Long> {
    
}
