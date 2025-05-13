package com.app.my_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.my_project.entity.ProductionEntity;

public interface ProductionRepository extends JpaRepository<ProductionEntity, Long> {

}
