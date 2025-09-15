package com.example.store.service;

import com.example.store.dto.AllStoresResponseDto;
import com.example.store.dto.ProductResponseDto;
import com.example.store.dto.StoreResponseDto;
import com.example.store.entity.Product;
import com.example.store.entity.Store;
import com.example.store.entity.StoreProduct;
import com.example.store.mapper.StoreMapper;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.StoreProductRepository;
import com.example.store.repository.StoreRepository;
import com.example.store.request.ProductRequest;
import com.example.store.request.StoreRequest;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Validated
@Transactional(readOnly = true)
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreProductRepository storeProductRepository;

    @Transactional(rollbackFor = Exception.class)
    public StoreResponseDto createStore(@Valid StoreRequest request) {

        Store store = new Store(UUID.randomUUID(), request.getName(), request.getLocation(), request.getEmail(), null);

        storeRepository.saveAndFlush(store);

        return storeMapper.mapToStoreResponseDto(store);

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteStore(UUID storeId) {

        storeRepository.findById(storeId).orElseThrow();
        storeRepository.deleteById(storeId);

    }

    public StoreResponseDto findById(UUID storeId) {

        Store store = storeRepository.findById(storeId).orElseThrow();

        return storeMapper.mapToStoreResponseDto(store);

    }

    @Transactional(rollbackFor = Exception.class)
    public StoreResponseDto updateById(UUID id, @Valid StoreRequest request) {

        Store store = storeRepository.findById(id).orElseThrow();

        store.setName(request.getName());
        store.setLocation(request.getLocation());
        store.setEmail(request.getEmail());

        storeRepository.saveAndFlush(store);

        return storeMapper.mapToStoreResponseDto(store);

    }

    public List<AllStoresResponseDto> findAllStores() {

        List<Store> stores = storeRepository.findAll();

        return stores.stream()
                .map(e -> storeMapper.mapToAllStoresResponseDto(e))
                .toList();

    }

    public List<AllStoresResponseDto> findByLocation(String location) {

        List<Store> stores = storeRepository.findByLocation(location);

        return stores.stream()
                .map(e -> storeMapper.mapToAllStoresResponseDto(e))
                .toList();

    }

    public List<AllStoresResponseDto> findAllStoresByName() {

        List<Store> stores = storeRepository.findAll(Sort.by(Sort.Order.asc("name")));

        return stores.stream()
                .map(e -> storeMapper.mapToAllStoresResponseDto(e))
                .toList();

    }

    @Transactional(rollbackFor = Exception.class)
    public StoreResponseDto copy(UUID storeId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow();

        Store copyStore = new Store(UUID.randomUUID(), store.getName(), store.getLocation(), store.getEmail(), store.getUpdatedAt());

        storeRepository.saveAndFlush(copyStore);

        return storeMapper.mapToStoreResponseDto(copyStore);

    }

    public List<ProductResponseDto> findAllProductByLocation(String street) {

        // Шаг 1. Получаем все магазины
        List<Store> allStores = storeRepository.findAll();

        return allStores.stream()
                .filter(e -> e.getLocation().contains(street)) // Шаг 2. Фильтруем магазины по указанной улице
                .map(Store::getId)
                .map(storeProductRepository::findByStoreId)
                .flatMap(List::stream)
                .map(StoreProduct::getProductId)
                .map(productId -> productRepository.findById(productId)
                        .orElseThrow())
                .map(storeMapper::mapToProductResponseDto)
                .distinct()
                .toList(); // Шаг 3., собираем все товары из найденных магазинов

    }

    public List<ProductResponseDto> findUniqueProducts() {

        List<Product> allProducts = productRepository.findAll();
        List<ProductResponseDto> result = new ArrayList<>();

        for (Product product : allProducts) {

            int countStore = storeRepository.countStoresByProductId(product.getId());

            if (countStore == 1) {
                result.add(storeMapper.mapToProductResponseDto(product));
            }

        }

        return result;

    }

    public ProductResponseDto createProduct(UUID storeId, ProductRequest request) {

        Product product = new Product(UUID.randomUUID(), request.getName(), request.getPrice(), "some");

        productRepository.saveAndFlush(product);

        new StoreProduct(UUID.randomUUID(), storeId, product.getId());

        return storeMapper.mapToProductResponseDto(product);
    }
}
