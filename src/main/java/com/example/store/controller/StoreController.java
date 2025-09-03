package com.example.store.controller;

import com.example.store.dto.AllStoresResponseDto;
import com.example.store.dto.StoreResponseDto;
import com.example.store.request.StoreRequest;
import com.example.store.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponseDto> createStore(@Valid @RequestBody StoreRequest request) {

        StoreResponseDto storeResponse = storeService.createStore(request);

        return ResponseEntity.ok(storeResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable UUID id) {

        storeService.deleteStore(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponseDto> findStoreById(@PathVariable UUID id) {

        StoreResponseDto storeResponseDto = storeService.findById(id);

        return ResponseEntity.ok(storeResponseDto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable UUID id, @Valid @RequestBody StoreRequest request) {

        StoreResponseDto storeResponseDto = storeService.updateById(id, request);

        return ResponseEntity.ok(storeResponseDto);

    }

    @GetMapping
    public ResponseEntity<List<AllStoresResponseDto>> findAllStores() {

        List<AllStoresResponseDto> allStoresResponses = storeService.findAllStores();

        return ResponseEntity.ok(allStoresResponses);

    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<AllStoresResponseDto>> findStoreByLocation(@PathVariable String location) {

        List<AllStoresResponseDto> allStoresResponses = storeService.findByLocation(location);

        return ResponseEntity.ok(allStoresResponses);

    }

    @GetMapping("/sorted")
    public ResponseEntity<List<AllStoresResponseDto>> findAllStoresByName() {

        List<AllStoresResponseDto> allStoresResponses = storeService.findAllStoresByName();

        return ResponseEntity.ok(allStoresResponses);

    }

    @GetMapping("/{id}/copy")
    public ResponseEntity<StoreResponseDto> copyStore(@PathVariable UUID id) {

        StoreResponseDto copyStoreResponceDto = storeService.copy(id);

        return ResponseEntity.ok(copyStoreResponceDto);

    }

}
