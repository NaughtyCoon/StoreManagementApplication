package com.example.store.service;

import com.example.store.TestContainerInitialization;
import com.example.store.dto.StoreResponseDto;
import com.example.store.entity.Store;
import com.example.store.repository.StoreRepository;
import com.example.store.request.StoreRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreServiceTest extends TestContainerInitialization {

    @Autowired
    private StoreRepository repository;

    @Autowired
    private StoreService service;

    @AfterEach
    void clear() {
        repository.deleteAll();
    }

    @Test
    void createStore_whenNameIsBlank_thenThrow() {

        StoreRequest storeRequest = createStoreRequest("", "ул. Урванцева", "mail@mailer.ml");

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createStore(storeRequest));

    }

    @Test
    void createStore_whenLocationIsBlank_thenThrow() {

        StoreRequest storeRequest = createStoreRequest("Красный Яр", "", "mail@mailer.ml" );

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createStore(storeRequest));

    }

    @Test
    void createStore_whenEmailIsBlank_thenThrow() {

        StoreRequest storeRequest = createStoreRequest("Красный Яр", "ул. Урванцева", "" );

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createStore(storeRequest));

    }

    @Test
    void createStore_whenNameAndLocationAndEmailNotBlank_thenCreate() {

        StoreRequest storeRequest = createStoreRequest("Пятёрочка", "ул. Урванцева", "mail@mailer.ml");

        StoreResponseDto storeResponseDto = Assertions.assertDoesNotThrow(() -> service.createStore(storeRequest));

        assertEquals(storeRequest.getName(), storeResponseDto.getName());

    }

    @ParameterizedTest
    @MethodSource("invalidData")
    void updateStore_whenRequestInvalid_thenThrow(String name, String location, String email) {

        Store store = createStore("Пятёрочка", "Ленина", "123@fgdgd.er");

        StoreRequest storeRequest = createStoreRequest(name, location, email);

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.updateById(store.getId(), storeRequest));

    }

    @Test
    void updateStore_whenStoreNotFoundById_thenThrow() {

        createStore("Пятёрочка", "Ленина", "mail@somemail.ml");

        StoreRequest storeRequest = createStoreRequest("Яр", "Урванцева", "mail@mailer.ml");

        Assertions.assertThrows(NoSuchElementException.class, () -> service.updateById(UUID.randomUUID(), storeRequest));

    }

    @Test
    void updateStore_whenStoreExists_thenUpdate() {

        UUID id = createStore("Пятёрочка", "Ленина", "mail@somemail.ml").getId();

        StoreRequest storeRequest = createStoreRequest("Fix-price", "Урванцева", "mail@mailer.ml");

        assertEquals("Fix-price", service.updateById(id, storeRequest).getName());

    }

    @Test
    void deleteStore_whenStoreNotFoundById_thenThrow() {

        UUID id = UUID.randomUUID();

        Assertions.assertThrows(NoSuchElementException.class, () -> service.deleteStore(id));

    }

    @Test
    void deleteStore_whenStoreExists_thenDelete() {

        UUID id = createStore("Пятёрочка", "Ленина", "mail@somemail.ml").getId();

        service.deleteStore(id);

        Assertions.assertThrows(NoSuchElementException.class, () -> service.findById(id));

    }

    @Test
    void findById_whenStoreNotFoundById_thenThrow() {

        UUID id = UUID.randomUUID();

        Assertions.assertThrows(NoSuchElementException.class, () -> service.findById(id));

    }

    @Test
    void findById_whenStoreIsFoundById_thenReturnAdequateResponseDto() {

        UUID id = createStore("Пятёрочка", "Ленина", "mail@somemail.ml").getId();

        assertEquals("StoreResponseDto", service.findById(id).getClass().getSimpleName());
        assertEquals(id, service.findById(id).getId());

    }

    private Store createStore(String name, String location, String email) {

        Store store = new Store(UUID.randomUUID(), name, location, email, null);
        store = repository.saveAndFlush(store);

        return store;

    }

    private StoreRequest createStoreRequest(String name, String location, String email) {
        return new StoreRequest(name, location, email);
    }

    private Stream<Arguments> invalidData() {
        return Stream.of(
                Arguments.of("", "ул.Урванцева", "mail@mailer.ml"),
                Arguments.of("Красный Яр", "", "mail@mailer.ml"),
                Arguments.of("", "", "mail@mailer.ml"),
                Arguments.of("Красный Яр", "ул.Урванцева", ""),
                Arguments.of("", "ул.Урванцева", ""),
                Arguments.of("Красный Яр", "", ""),
                Arguments.of("", "", "")
        );
    }

}
