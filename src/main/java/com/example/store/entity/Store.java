package com.example.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name="stores")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Store {

    @Id                                 // Ключ в базе данных
    @Column                             // Название колонки
    private UUID id;

    @Column
    private String name;

    @Column
    private String location;

    @Column
    private String email;

    @LastModifiedDate                   // Автоматическая дата последнего обновления строки (записи) в БД
    @Column
    private LocalDateTime updatedAt;

}
