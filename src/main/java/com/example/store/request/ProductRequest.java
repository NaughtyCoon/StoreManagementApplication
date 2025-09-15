package com.example.store.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "DTO запроса создания магазина")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class ProductRequest {

        @Schema
        @JsonProperty("Наименование товара")
        @NotBlank
        private String name;

        @Schema
        @JsonProperty("Цена товара")
        @NotBlank
        private BigDecimal price;

}
