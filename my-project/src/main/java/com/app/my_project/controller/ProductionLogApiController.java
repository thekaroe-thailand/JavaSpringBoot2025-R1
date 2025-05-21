package com.app.my_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.my_project.repository.ProductionLogRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import com.app.my_project.entity.ProductionLogEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/production-logs")
public class ProductionLogApiController {
    @Autowired
    private ProductionLogRepository productionLogRepository;

    @GetMapping("/{productionId}")
    public List<ProductionLogEntity> getAllProductionLog(@PathVariable Long productionId) {
        return productionLogRepository.findAllByProductionIdOrderByIdDesc(productionId);
    }

    @PostMapping
    public ProductionLogEntity createProductionLog(@RequestBody ProductionLogEntity productionLog) {
        return productionLogRepository.save(productionLog);
    }

    @PutMapping("/{id}")
    public ProductionLogEntity updateProductionLog(
            @PathVariable Long id,
            @RequestBody ProductionLogEntity productionLog) {
        ProductionLogEntity p = productionLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Production log not found"));
        p.setProduction(productionLog.getProduction());
        p.setRemark(productionLog.getRemark());
        p.setQty(productionLog.getQty());

        return productionLogRepository.save(p);
    }

    @DeleteMapping("/{id}")
    public void deleteProductionLog(@PathVariable Long id) {
        productionLogRepository.deleteById(id);
    }
}
