package com.example.store.service;

import com.example.store.dto.StoreResponseDto;
import com.example.store.entity.Store;
import com.example.store.mapper.StoreMapper;
import com.example.store.repository.StoreRepository;
import com.example.store.request.StoreRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional(rollbackOn = Exception.class)
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreMapper storeMapper;

    public StoreResponseDto createStore(StoreRequest request){

        Store store = new Store(UUID.randomUUID(), request.getName(), request.getLocation(), null);

        storeRepository.saveAndFlush(store);

        return storeMapper.mapToStoreResponseDto(store);

    }

    public void deleteStore(UUID storeId){

        storeRepository.deleteById(storeId);

    }

    public StoreResponseDto findById(UUID storeId){

        Store store = storeRepository.findById(storeId).orElseThrow();

        return storeMapper.mapToStoreResponseDto(store);

    }

    public StoreResponseDto updateById(UUID id, @Valid StoreRequest request) {

        Store store = storeRepository.findById(id).orElseThrow();

        store.setName(request.getName());
        store.setLocation(request.getLocation());

        storeRepository.saveAndFlush(store);

        return storeMapper.mapToStoreResponseDto(store);

    }

}
