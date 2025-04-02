package com.utility.creation.utilitycreation.model.entity;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

@Data
@RedisHash("entity")
public abstract class CommonEntity{
    private LocalDate createdDate;
    private LocalTime createdTime;
    private LocalDate modifiedDate;
    private LocalTime modifiedTime;
}