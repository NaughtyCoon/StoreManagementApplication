package com.example.store.service;

import com.example.store.TestContainerInitialization;

import com.example.store.dto.SupplierResponseDto;
import com.example.store.entity.Supplier;
import com.example.store.repository.SupplierRepository;
import com.example.store.request.SupplierRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SupplierServiceTest extends TestContainerInitialization {

    @Autowired
    private SupplierRepository repository;

    @Autowired
    private SupplierService service;

    @AfterEach
    void clear() {
        repository.deleteAll();
    }

    @Test
    void createSupplier_whenNameIsBlank_thenThrow() {

        SupplierRequest supplierRequest = createSupplierRequest("", "plant@plant.example.com",
                "", "пр. Ленина", "www.plant.example.com");

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createSupplier(supplierRequest));

    }

    @Test
    void createSupplier_whenNameIsNull_thenThrow() {

        SupplierRequest supplierRequest = createSupplierRequest(null, "plant@plant.example.com",
                "", "пр. Ленина", "www.plant.example.com");

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createSupplier(supplierRequest));

    }

    @Test
    void createSupplier_whenEmailIsBlank_thenThrow() {

        SupplierRequest supplierRequest = createSupplierRequest("Русский стандарт", "",
                "", "пр. Ленина", "www.plant.example.com");

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createSupplier(supplierRequest));

    }

    @Test
    void createSupplier_whenEmailIsNull_thenThrow() {

        SupplierRequest supplierRequest = createSupplierRequest("Русский стандарт", null,
                "", "пр. Ленина", "www.plant.example.com");

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createSupplier(supplierRequest));

    }

    @Test
    void createSupplier_whenEmailIsNotUnique_thenThrow() {

        SupplierRequest supplierRequestPreliminary = createSupplierRequest("ПершинЪ",
                "plant@plant.example.com","", "пр. Чкалова", "www.sbiten.example.com");
        service.createSupplier(supplierRequestPreliminary);

        SupplierRequest supplierRequest = createSupplierRequest("Русский стандарт",
                "plant@plant.example.com","", "пр. Ленина", "www.plant.example.com" );

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createSupplier(supplierRequest));

    }

    @Test
    void createSupplier_whenAllArgsCorrect_thenCreate() {

        SupplierRequest supplierRequest = createSupplierRequest("ПершинЪ", "plant@plant.example.com",
                "", null, "www.sbiten.example.com");
        UUID id = Assertions.assertDoesNotThrow(() -> service.createSupplier(supplierRequest).getId());

        SupplierResponseDto supplierResponseDto = service.findById(id);

        assertEquals(supplierRequest.getName(), supplierResponseDto.getName());
        assertEquals(supplierRequest.getEmail(), supplierResponseDto.getEmail());
        assertEquals(supplierRequest.getPhone(), supplierResponseDto.getPhone());
        assertEquals(supplierRequest.getAddress(), supplierResponseDto.getAddress());
        assertEquals(supplierRequest.getWebsite(), supplierResponseDto.getWebsite());

    }

//    @ParameterizedTest
//    @MethodSource("invalidData")
//    void updateSupplier_whenRequestInvalid_thenThrow(String name, String email, String phone, String address,
//                                                     String website) {
//
//        Supplier supplier = createSupplier("Пятёрочка", "Ленина", "123@fgdgd.er");
//
//        SupplierRequest supplierRequest = createSupplierRequest(name, location, email);
//
//        Assertions.assertThrows(ConstraintViolationException.class, () -> service.updateById(supplier.getId(), supplierRequest));
//
//    }

    private Supplier createSupplier(String name, String email, String phone, String address, String website) {

        Supplier supplier = new Supplier(UUID.randomUUID(), name, email, phone, address, website, null);
        supplier = repository.saveAndFlush(supplier);

        return supplier;

    }

    private SupplierRequest createSupplierRequest(String name, String email, String phone, String address, String website) {
        return new SupplierRequest(name, email, phone, address, website);
    }

    private Stream<Arguments> invalidData() {
        return Stream.of(
                Arguments.of("", "plant@plant.example.com", "", "пр. Ленина", "www.plant.example.com"),
                Arguments.of(null, "plant@plant.example.com", "", "пр. Ленина", "www.plant.example.com"),
                Arguments.of("Русский стандарт", null, "+75559997788", "пр. Ленина", "www.plant.example.com"),
                Arguments.of("Русский стандарт", "", "", "пр. Ленина", "www.plant.example.com"),
                Arguments.of("Русский стандарт", null, "", "пр. Ленина", "www.plant.example.com")
        );

    }

}
