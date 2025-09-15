package com.example.store.service;

import com.example.store.TestContainerInitialization;
import com.example.store.dto.ProductResponseDto;
import com.example.store.dto.StoreResponseDto;
import com.example.store.entity.Product;
import com.example.store.entity.Store;
import com.example.store.entity.StoreProduct;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.StoreProductRepository;
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

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreServiceTest extends TestContainerInitialization {

    public static final String DEFAULT_STORE_LOCATION = "ул. Ленина";
    public static final String DEFAULT_STORE_NAME = "Пятёрочка";
    public static final String DEFAULT_STORE_EMAIL = "mail@ya.ru";

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreProductRepository storeProductRepository;

    @Autowired
    private StoreService service;

    @AfterEach
    void clear() {
        storeRepository.deleteAll();
    }

    @Test
    void createStore_whenNameIsBlank_thenThrow() {

        StoreRequest storeRequest = createStoreRequest("", "ул. Урванцева", "mail@mailer.ml");

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createStore(storeRequest));

    }

    @Test
    void createStore_whenLocationIsBlank_thenThrow() {

        StoreRequest storeRequest = createStoreRequest("Красный Яр", "", "mail@mailer.ml");

        Assertions.assertThrows(ConstraintViolationException.class, () -> service.createStore(storeRequest));

    }

    @Test
    void createStore_whenEmailIsBlank_thenThrow() {

        StoreRequest storeRequest = createStoreRequest("Красный Яр", "ул. Урванцева", "");

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

    @Test
    void findAllStores_whenNoStoreExists_thenReturnEmptyList() {

        assertEquals(Collections.emptyList(), service.findAllStores());

    }

    @Test
    void findAllProductByLocation_whenStoreDoesNotExist_thenReturnEmptyList() {

        createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        createProduct("Кола", BigDecimal.valueOf(73.67), "Напитки");
        createProduct("Квас", BigDecimal.valueOf(55.00), "Напитки");

        List<ProductResponseDto> result = assertDoesNotThrow(() ->
                service.findAllProductByLocation("ул. Вязов"));

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    void findAllProductByLocation_whenStoreExistsButStreetIsNull_thenThrow() {

        Store store = createStore("Пятёрочка", "ул. Вязов", "123@example.com");

        Product product1 = createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        Product product2 = createProduct("Кола", BigDecimal.valueOf(73.67), "Напитки");
        Product product3 = createProduct("Квас", BigDecimal.valueOf(55.00), "Напитки");

        createStoreProduct(store.getId(), product1.getId());
        createStoreProduct(store.getId(), product2.getId());
        createStoreProduct(store.getId(), product3.getId());

        Assertions.assertThrows(NullPointerException.class, () -> service.findAllProductByLocation(null));

    }

    @Test
    void findAllProductByLocation_whenStoreNotFoundByStreet_thenReturnEmptyList() {

        Store store = createStore("Пятёрочка", "ул. Вязов", "123@example.com");

        Product product1 = createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        Product product2 = createProduct("Кола", BigDecimal.valueOf(73.67), "Напитки");
        Product product3 = createProduct("Квас", BigDecimal.valueOf(55.00), "Напитки");

        createStoreProduct(store.getId(), product1.getId());
        createStoreProduct(store.getId(), product2.getId());
        createStoreProduct(store.getId(), product3.getId());

        List<ProductResponseDto> result = assertDoesNotThrow(() ->
                service.findAllProductByLocation("ул. Ленина"));

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    void findAllProductByLocation_whenStoreProductIsEmpty_thenReturnEmptyList() {

        Store store = createStore("Пятёрочка", "ул. Вязов", "123@example.com");

        Product product1 = createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        Product product2 = createProduct("Кола", BigDecimal.valueOf(73.67), "Напитки");
        Product product3 = createProduct("Квас", BigDecimal.valueOf(55.00), "Напитки");

        List<ProductResponseDto> result =
                assertDoesNotThrow(() -> service.findAllProductByLocation("ул. Вязов"));

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    void findAllProductByLocation_whenStoreExistButNoProduct_thenReturnEmptyList() {

        Store store = createStore("Пятёрочка", "ул. Вязов", "123@example.com");
        Store store2 = createStore("Перекрёсток", "ул. Ленина", "1234@example.com");

        Product product1 = createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        Product product2 = createProduct("Кола", BigDecimal.valueOf(73.67), "Напитки");
        Product product3 = createProduct("Квас", BigDecimal.valueOf(55.00), "Напитки");

        createStoreProduct(store.getId(), product1.getId());
        createStoreProduct(store.getId(), product2.getId());
        createStoreProduct(store.getId(), product3.getId());

        List<ProductResponseDto> result = assertDoesNotThrow(() ->
                service.findAllProductByLocation("ул. Ленина"));

        Assertions.assertTrue(result.isEmpty());

    }

    @Test
    void findAllProductByLocation_whenStoreExistAndProductExist_thenReturnProductList() {

        Store store = createStore("Пятёрочка", "ул. Ленина", "123@example.com");
        Store store2 = createStore("Перекрёсток", "ул. Ленина", "1234@example.com");

        Product product1 = createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        Product product2 = createProduct("Кола", BigDecimal.valueOf(73.67), "Напитки");
        Product product3 = createProduct("Квас", BigDecimal.valueOf(55.00), "Напитки");
        Product product4 = createProduct("Коньяк", BigDecimal.valueOf(255.00), "Напитки");

        createStoreProduct(store.getId(), product1.getId());
        createStoreProduct(store.getId(), product2.getId());
        createStoreProduct(store.getId(), product3.getId());
        createStoreProduct(store2.getId(), product3.getId());
        createStoreProduct(store2.getId(), product4.getId());

        List<ProductResponseDto> result = assertDoesNotThrow(() ->
                service.findAllProductByLocation("ул. Ленина"));

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals(product1.getId(), result.get(0).getId());

    }

    @Test
    void findUniqueProducts_whenUniqueProductsExist_thenReturnUniqueProductList() {

        Store firstStore = createStore(DEFAULT_STORE_NAME, DEFAULT_STORE_LOCATION, DEFAULT_STORE_EMAIL);
        Store secondStore = createStore("Красный яр", "SomeStreet", DEFAULT_STORE_EMAIL);

        Product firstProduct = createProduct("Лимонад", BigDecimal.valueOf(23.12), "Напитки");
        Product secondProduct = createProduct("Кола", BigDecimal.valueOf(73.67), "Напитки");
        Product ThirdProduct = createProduct("Квас", BigDecimal.valueOf(55.00), "Напитки");

        createStoreProduct(firstStore.getId(), firstProduct.getId());
        createStoreProduct(firstStore.getId(), secondProduct.getId());
        createStoreProduct(firstStore.getId(), ThirdProduct.getId());
        createStoreProduct(secondStore.getId(), ThirdProduct.getId());
    }

//    @Test
//    void findAllStores_whenAnyStoreExists_thenFindAll() {
//
//        UUID[] id = new UUID[3];
//
//        id[0] = createStore("Пятёрочка", "Ленина", "mail@somemail.ml").getId();
//        id[1] = createStore("Магнит", "Клюквина", "mail@mailer.ml").getId();
//        id[2] = createStore("Винлаб", "Ватутина", "mailus@mail.ml").getId();
//
//        List<AllStoresResponseDto> allStoresResponseDtos = service.findAllStores();
//
//        for (int i = 0; i < allStoresResponseDtos.size(); i++) {
//            assertEquals(3, allStoresResponseDtos.size());
//            assertTrue(allStoresResponseDtos.stream()
//                    .anyMatch(e -> e.getId() == Arrays.stream(id)
//                            .filter(a -> a = e.getId())
//                                    .findAny()));
//        }
//    }

    private Store createStore(String name, String location, String email) {

        Store store = new Store(UUID.randomUUID(), name, location, email, null);
        store = storeRepository.saveAndFlush(store);

        return store;

    }

    private StoreProduct createStoreProduct(UUID storeId, UUID productId) {

        StoreProduct storeProduct = new StoreProduct(UUID.randomUUID(), storeId, productId);
        storeProduct = storeProductRepository.saveAndFlush(storeProduct);

        return storeProduct;

    }

    private Product createProduct(String name, BigDecimal price, String category) {

        Product product = new Product(UUID.randomUUID(), name, price, category);
        product = productRepository.saveAndFlush(product);

        return product;

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
