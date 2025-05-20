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

import com.app.my_project.entity.ProductionLogEntity;
import com.app.my_project.repository.ProductionLogRepository;

@RestController
@RequestMapping("/api/production-logs")
public class ProductionLogApiController {
    @Autowired
    private ProductionLogRepository productionLogRepository;

    @GetMapping
    public List<ProductionLogEntity> getAllProductionLogs() {
        return productionLogRepository.findAll();
    }

    @PostMapping
    public ProductionLogEntity createProductionLog(@RequestBody ProductionLogEntity productionLog) {
        return productionLogRepository.save(productionLog);
    }
    
    @PutMapping("/{id}")
    public ProductionLogEntity updateProductionLog(
        @PathVariable Long id, 
        @RequestBody ProductionLogEntity productionLog) {
        return productionLogRepository.save(productionLog);
    }

    @DeleteMapping("/{id}")
    public void deleteProductionLog(@PathVariable Long id) {
        productionLogRepository.deleteById(id);
    }
}
