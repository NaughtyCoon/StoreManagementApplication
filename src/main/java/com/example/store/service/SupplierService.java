package com.example.store.service;

import com.example.store.dto.SupplierResponseDto;
import com.example.store.entity.Supplier;
import com.example.store.mapper.SupplierMapper;
import com.example.store.repository.SupplierRepository;
import com.example.store.request.SupplierRequest;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
@Transactional(readOnly = true)
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierMapper supplierMapper;

    @Transactional(rollbackFor = Exception.class)
    public SupplierResponseDto createSupplier(@Valid SupplierRequest request) {

        Supplier supplier = new Supplier(UUID.randomUUID(), request.getName(), request.getEmail(), request.getPhone(), request.getAddress(), request.getWebsite(), null);

        supplierRepository.saveAndFlush(supplier);

        return supplierMapper.mapToSupplierResponseDto(supplier);

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteSupplier(UUID supplierId) {

        supplierRepository.deleteById(supplierId);

    }

    public SupplierResponseDto findById(UUID supplierId) {

        Supplier supplier = supplierRepository.findById(supplierId).orElseThrow();

        return supplierMapper.mapToSupplierResponseDto(supplier);

    }

    @Transactional(rollbackFor = Exception.class)
    public SupplierResponseDto updateById(UUID id, @Valid SupplierRequest request) {

        Supplier supplier = supplierRepository.findById(id).orElseThrow();

        supplier.setName(request.getName());
        supplier.setEmail(request.getEmail());
        supplier.setPhone(request.getPhone());
        supplier.setAddress(request.getAddress());
        supplier.setWebsite(request.getWebsite());

        supplierRepository.saveAndFlush(supplier);

        return supplierMapper.mapToSupplierResponseDto(supplier);

    }

}
