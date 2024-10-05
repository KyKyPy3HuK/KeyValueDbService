package org.application.keyvalueapplication.repositories;


import jakarta.transaction.Transactional;
import org.application.keyvalueapplication.model.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface DataRepository extends JpaRepository<Data, Long> {
    @Transactional
    @Query("select d.value from Data d where d.key=:key")
    String getDataByKey(@Param("key") String key);

    @Modifying
    @Transactional
    @Query("update Data d set d.value=:value, d.ttl = :ttl where d.key=:key")
    void updateDataByKey(@Param("key") String key, @Param("value") String value, @Param("ttl") Date ttl);

    @Modifying
    @Transactional
    @Query("delete from Data d where d.ttl < CURRENT_TIMESTAMP")
    void deleteOldData();

    @Modifying
    @Transactional
    @Query("delete from Data d where d.key = :key")
    void deleteValueByKey(@Param("key") String key);
}
