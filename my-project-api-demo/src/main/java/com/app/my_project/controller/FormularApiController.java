package com.app.my_project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.my_project.entity.FormularEntity;
import com.app.my_project.repository.FormularRepository;

@RestController
@RequestMapping("/api/formulars")
public class FormularApiController {
    @Autowired
    private FormularRepository formularRepository;

    @GetMapping
    public List<FormularEntity> getAllFormulars() {
        return formularRepository.findAll();
    }

    @PostMapping
    public FormularEntity createFormular(@RequestBody FormularEntity formularEntity) {
        return formularRepository.save(formularEntity);
    }

    @PutMapping("/{id}")
    public FormularEntity updateFormular(
        @PathVariable Long id, 
        @RequestBody FormularEntity formularEntity
    ) {
        FormularEntity formular = formularRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Formular not found"));
        formular.setName(formularEntity.getName());
        formular.setQty(formularEntity.getQty());
        formular.setUnit(formularEntity.getUnit());
        formular.setMaterial(formularEntity.getMaterial());

        return formularRepository.save(formular);
    }

    @DeleteMapping("/{id}")
    public void deleteFormular(@PathVariable Long id) {
        formularRepository.deleteById(id);
    }
    
}
