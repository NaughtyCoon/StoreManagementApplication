package com.example.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Schema(description = "DTO с основной информацией для работы со списком всех поставщиков")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierResponseDto {

    @Schema(description = "Идентификатор поставщика")
    private UUID id;

    @Schema(description = "Наименование поставщика")
    private String name;

    @Schema(description = "E-mail поставщика")
    private String email;

    @Schema(description = "Телефон поставщика")
    private String phone;

    @Schema(description = "Адрес поставщика")
    private String address;

    @Schema(description = "Сайт поставщика")
    private String website;

}
