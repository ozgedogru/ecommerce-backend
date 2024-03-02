package com.workintech.ecommerce.service;

import com.workintech.ecommerce.entity.Store;

import java.util.List;

public interface StoreService {
    List<Store> getAllStores();
    Store getStoreById(Long id);
    Store saveStore(Store store);
    Store updateStore(Long id, Store store);
    Store deleteStore(Long id);
}
