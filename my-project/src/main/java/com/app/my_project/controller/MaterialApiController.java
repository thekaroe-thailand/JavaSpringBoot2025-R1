package com.app.my_project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.my_project.entity.MaterialEntity;
import com.app.my_project.repository.MaterialRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/materials")
public class MaterialApiController {
    private final MaterialRepository materialRepository;

    public MaterialApiController(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    @PostMapping
    public ResponseEntity<MaterialEntity> creaEntity(
            @RequestBody MaterialEntity materialEntity) {
        return ResponseEntity.ok(materialRepository.save(materialEntity));
    }

    @GetMapping
    public ResponseEntity<List<MaterialEntity>> getMethodName() {
        List<MaterialEntity> materials = materialRepository.findAllByOrderByIdDesc();
        return ResponseEntity.ok(materials);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialEntity> updateMaterial(
            @PathVariable Long id,
            @RequestBody MaterialEntity materialEntity) {
        MaterialEntity materialForUpdate = materialRepository.findById(id).orElse(null);

        if (materialForUpdate == null) {
            throw new IllegalArgumentException("Material not found");
        }

        materialForUpdate.setName(materialEntity.getName());
        materialForUpdate.setUnitName(materialEntity.getUnitName());
        materialForUpdate.setQty(materialEntity.getQty());

        return ResponseEntity.ok(materialRepository.save(materialForUpdate));
    }

    @DeleteMapping("/{id}")
    public void deleteMaterial(@PathVariable Long id) {
        materialRepository.deleteById(id);
    }

}
