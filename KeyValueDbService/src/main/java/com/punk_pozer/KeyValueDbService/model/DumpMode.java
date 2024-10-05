package com.punk_pozer.KeyValueDbService.model;

public enum DumpMode {
    // Сохранение объектов БД с текущим остатком TTL
    SOFT,
    // Простое сохранение объектов БД, реальный TTL может быть утерян
    DEFAULT
}
