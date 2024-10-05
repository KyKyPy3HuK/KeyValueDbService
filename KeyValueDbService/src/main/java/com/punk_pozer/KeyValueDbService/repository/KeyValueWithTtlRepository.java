package com.punk_pozer.KeyValueDbService.repository;

import com.punk_pozer.KeyValueDbService.model.KeyValueWithTtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


public interface KeyValueWithTtlRepository extends JpaRepository<KeyValueWithTtl, String> {

    @Transactional
    @Query("select k.value from KeyValueWithTtl k where k.key = :key")
    String getValueByKey(@Param("key") String key);


    @Transactional
    @Query("select k from KeyValueWithTtl k where k.key = :key")
    KeyValueWithTtl findByKey(@Param("key") String key);


    @Modifying
    @Transactional
    @Query("update KeyValueWithTtl k set k.value =:value, k.ttl =:ttl  where k.key =:key")
    int updateByKey(@Param("key") String key,
                    @Param("value") String value,
                    @Param("ttl") Date ttl);


    @Modifying
    @Transactional
    @Query("delete from KeyValueWithTtl k where k.key =:key")
    int removeByKey(@Param("key") String key);


    @Modifying
    @Transactional
    @Query("DELETE FROM KeyValueWithTtl k WHERE (k.ttl IS NOT NULL) AND (k.ttl < CURRENT_TIMESTAMP)")
    int deleteAllExpired();
}