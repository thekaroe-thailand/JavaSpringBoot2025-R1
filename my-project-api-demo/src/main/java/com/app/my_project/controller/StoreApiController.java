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

import com.app.my_project.entity.StoreEntity;
import com.app.my_project.repository.StoreRepository;

@RestController
@RequestMapping("/api/store")
public class StoreApiController {
    @Autowired
    private StoreRepository storeRepository;

    @GetMapping
    public List<StoreEntity> getStore() {
        return storeRepository.findAll();
    }

    @PostMapping
    public StoreEntity postStore(@RequestBody StoreEntity storeEntity) {
        return storeRepository.save(storeEntity);
    }   
    
    @PutMapping("/{id}")
    public StoreEntity putStore(
        @RequestBody StoreEntity storeEntity, 
        @PathVariable Long id ) {
        StoreEntity store = storeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Store not found"));
        store.setName(storeEntity.getName());
        store.setAddress(storeEntity.getAddress());
        store.setRemark(storeEntity.getRemark());
        
        return storeRepository.save(store);
    }
    
    @DeleteMapping("/{id}")
    public void deleteStore(@PathVariable Long id) {
        storeRepository.deleteById(id);
    }
    
}


