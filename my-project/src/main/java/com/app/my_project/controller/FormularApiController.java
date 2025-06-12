package com.app.my_project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.my_project.repository.FormularRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import com.app.my_project.entity.FormularEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;;

@RestController
@RequestMapping("/api/formulars")
public class FormularApiController {
    private final FormularRepository formularRepository;

    public FormularApiController(FormularRepository formularRepository) {
        this.formularRepository = formularRepository;
    }

    @GetMapping("/{productId}")
    public List<FormularEntity> getAllFormulars(@PathVariable Long productId) {
        return formularRepository.findAllByProductionIdOrderByIdDesc(productId);
    }

    @PostMapping
    public FormularEntity creaetFormular(@RequestBody FormularEntity formularEntity) {
        return formularRepository.save(formularEntity);
    }

    @PutMapping("/{id}")
    public FormularEntity updateFormular(@PathVariable Long id, @RequestBody FormularEntity formularEntity) {
        FormularEntity formular = formularRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formular not found"));

        formular.setQty(formularEntity.getQty());
        formular.setUnit(formularEntity.getUnit());
        formular.setMaterial(formularEntity.getMaterial());
        formular.setProduction(formularEntity.getProduction());

        return formularRepository.save(formular);
    }

    @DeleteMapping("/{id}")
    public void deleteFormular(@PathVariable Long id) {
        formularRepository.deleteById(id);
    }
}
