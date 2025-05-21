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

import com.app.my_project.entity.ProductionLossEntity;
import com.app.my_project.repository.ProductionLossRepository;

@RestController
@RequestMapping("/production-loss")
public class ProductionLossController {
    @Autowired
    private ProductionLossRepository productionLossRepository;

    @GetMapping("/{productionId}")
    public List<ProductionLossEntity> getAllProductionLosses(@PathVariable Long productionId) {
        return productionLossRepository.findAllByProductionIdOrderByIdDesc(productionId);
    }

    @PostMapping
    public ProductionLossEntity createProductionLoss(@RequestBody ProductionLossEntity productionLoss) {
        return productionLossRepository.save(productionLoss);
    }

    @PutMapping("/{id}")
    public ProductionLossEntity updateProductionLoss(@PathVariable Long id, @RequestBody ProductionLossEntity productionLoss) {
        ProductionLossEntity existingProductionLoss = productionLossRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Production loss not found"));
        existingProductionLoss.setProduction(productionLoss.getProduction());
        existingProductionLoss.setRemark(productionLoss.getRemark());   
        existingProductionLoss.setQty(productionLoss.getQty());
        return productionLossRepository.save(existingProductionLoss);
    }

    @DeleteMapping("/{id}")
    public void deleteProductionLoss(@PathVariable Long id) {
        productionLossRepository.deleteById(id);
    }

}
