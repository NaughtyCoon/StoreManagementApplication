package com.example.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name="suppliers")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@EnableJpaAuditing
public class Supplier {

    @Id                                 // Ключ в базе данных
    @Column                             // Название колонки
    private UUID id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String address;

    @Column
    private String website;

    @LastModifiedDate                   // Автоматическая дата последнего обновления строки (записи) в БД
    @Column
    private LocalDateTime updatedAt;

}
