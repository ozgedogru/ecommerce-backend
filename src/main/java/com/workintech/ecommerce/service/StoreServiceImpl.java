package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Store;
import com.workintech.ecommerce.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService{
    private StoreRepository storeRepository;

    @Autowired
    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public Store getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + id));
    }

    @Override
    public Store saveStore(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public Store updateStore(Long id, Store store) {
        if (!storeRepository.existsById(id)) {
            throw new RuntimeException("Store not found with id: " + id);
        }
        store.setId(id);
        return storeRepository.save(store);
    }

    @Override
    public Store deleteStore(Long id) {
        Optional<Store> storeOptional = storeRepository.findById(id);
        if (storeOptional.isEmpty()) {
            throw new RuntimeException("Store not found with id: " + id);
        }
        Store store = storeOptional.get();
        storeRepository.deleteById(id);
        return store;
    }
}
