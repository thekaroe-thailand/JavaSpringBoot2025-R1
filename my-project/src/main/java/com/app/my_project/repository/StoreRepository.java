package com.app.my_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.my_project.entity.StoreEntity;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

}
