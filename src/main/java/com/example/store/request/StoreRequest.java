package com.example.store.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO запроса создания магазина")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreRequest {

    @Schema
    @NotBlank
    private String name;

    @Schema
    @NotBlank
    private String location;
}
