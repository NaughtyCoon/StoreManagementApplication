package com.example.store.controller;

import com.example.store.dto.SupplierResponseDto;
import com.example.store.request.SupplierRequest;
import com.example.store.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierResponseDto> createSupplier(@RequestBody SupplierRequest request) {

        SupplierResponseDto supplierResponse = supplierService.createSupplier(request);

        return ResponseEntity.ok(supplierResponse);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable UUID id) {

        supplierService.deleteSupplier(id);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/find/{id}")
    public ResponseEntity<SupplierResponseDto> findSupplierById(@PathVariable UUID id) {

        SupplierResponseDto supplierResponseDto = supplierService.findById(id);

        return ResponseEntity.ok(supplierResponseDto);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SupplierResponseDto> updateSupplier(@PathVariable("id") UUID id, @RequestBody SupplierRequest request) {

        SupplierResponseDto supplierResponseDto = supplierService.updateById(id, request);

        return ResponseEntity.ok(supplierResponseDto);

    }

}
