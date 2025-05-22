package com.app.my_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.my_project.entity.ProductionLossEntity;
import com.app.my_project.repository.ProductionLossRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/production-loss")
public class ProductionLossApiController {
    @Autowired
    private ProductionLossRepository productionLossRepository;

    @GetMapping("/{productionId}")
    public List<ProductionLossEntity> getAllProductionLosses(
            @PathVariable Long productionId) {
        return productionLossRepository.findAllByProductionIdOrderByIdDesc(productionId);
    }

    @PostMapping
    public ProductionLossEntity createProductionLoss(@RequestBody ProductionLossEntity productionLoss) {
        return productionLossRepository.save(productionLoss);
    }

    @PutMapping("/{id}")
    public ProductionLossEntity updateProductionLoss(
            @PathVariable Long id,
            @RequestBody ProductionLossEntity productionLoss) {
        ProductionLossEntity p = productionLossRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Production loss not found"));
        p.setProduction(productionLoss.getProduction());
        p.setRemark(productionLoss.getRemark());
        p.setQty(productionLoss.getQty());
        p.setCreatedAt(productionLoss.getCreatedAt());

        return productionLossRepository.save(p);
    }

    @DeleteMapping("/{id}")
    public void deleteProductionLoss(@PathVariable Long id) {
        productionLossRepository.deleteById(id);
    }
}
