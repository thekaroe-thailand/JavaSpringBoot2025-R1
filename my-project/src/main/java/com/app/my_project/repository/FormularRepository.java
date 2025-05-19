package com.app.my_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.my_project.entity.FormularEntity;

public interface FormularRepository extends JpaRepository<FormularEntity, Long> {

}
