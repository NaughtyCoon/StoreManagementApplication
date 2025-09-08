package com.example.store.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO запроса создания поставщика")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierRequest {

    @Schema
    @JsonProperty("name")
    @NotBlank
    private String name;

    @Schema
    @JsonProperty("email")
    @NotBlank
    private String email;

    @Schema
    @JsonProperty("phone")
    private String phone;

    @Schema
    @JsonProperty("address")
    private String address;

    @Schema
    @JsonProperty("website")
    private String website;
}