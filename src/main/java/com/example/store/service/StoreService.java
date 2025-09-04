package com.example.store.service;

import com.example.store.dto.AllStoresResponseDto;
import com.example.store.dto.StoreResponseDto;
import com.example.store.entity.Store;
import com.example.store.mapper.StoreMapper;
import com.example.store.repository.StoreRepository;
import com.example.store.request.StoreRequest;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

    @Transactional(rollbackFor = Exception.class)
    public StoreResponseDto createStore(@Valid StoreRequest request){

        Store store = new Store(UUID.randomUUID(), request.getName(), request.getLocation(), request.getEmail(), null);

        storeRepository.saveAndFlush(store);

        return storeMapper.mapToStoreResponseDto(store);

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteStore(UUID storeId){

        storeRepository.deleteById(storeId);

    }

    public StoreResponseDto findById(UUID storeId){

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

        List<AllStoresResponseDto> list = stores.stream()
                .map(e -> storeMapper.mapToAllStoresResponseDto(e))
                .toList();

        return list;

    }

    public List<AllStoresResponseDto> findByLocation(String location) {

        List<Store> stores = storeRepository.findByLocation(location);

        List<AllStoresResponseDto> listLocation = stores.stream()
                .map(e -> storeMapper.mapToAllStoresResponseDto(e))
                .toList();

        return listLocation;

    }

    public List<AllStoresResponseDto> findAllStoresByName() {

        List<Store> stores = storeRepository.findAll(Sort.by(Sort.Order.asc("name")));

        return stores.stream()
                .map(e -> storeMapper.mapToAllStoresResponseDto(e))
                .toList();

    }

    @Transactional(rollbackFor = Exception.class)
    public StoreResponseDto copy(UUID storeId ) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow();

        Store copyStore = new Store(UUID.randomUUID(), store.getName(), store.getLocation(), store.getEmail(), store.getUpdatedAt());

        storeRepository.saveAndFlush(copyStore);

        return storeMapper.mapToStoreResponseDto(copyStore);

    }

}
