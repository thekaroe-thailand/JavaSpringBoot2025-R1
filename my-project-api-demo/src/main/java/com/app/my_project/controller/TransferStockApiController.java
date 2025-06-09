package com.app.my_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.my_project.repository.TransferStockRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import com.app.my_project.entity.TransferStockEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/transfer-stock")
public class TransferStockApiController {
    @Autowired
    private TransferStockRepository transferStockRepository;

    @GetMapping
    public List<TransferStockEntity> list() {
        return transferStockRepository.findAll();
    }

    @PostMapping
    public TransferStockEntity create(@RequestBody TransferStockEntity transferStock) {
        return transferStockRepository.save(transferStock);
    }

    @PutMapping("/{id}")
    public TransferStockEntity update(
            @PathVariable Long id,
            @RequestBody TransferStockEntity transferStock) {
        TransferStockEntity transferStockEntity = transferStockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TransferStock not found"));
        transferStockEntity.setFromStore(transferStock.getFromStore());
        transferStockEntity.setToStore(transferStock.getToStore());
        transferStockEntity.setQuantity(transferStock.getQuantity());
        transferStockEntity.setRemark(transferStock.getRemark());
        transferStockEntity.setCreatedAt(transferStock.getCreatedAt());
        transferStockEntity.setProduction(transferStock.getProduction());

        return transferStockRepository.save(transferStockEntity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        transferStockRepository.deleteById(id);
    }
}
