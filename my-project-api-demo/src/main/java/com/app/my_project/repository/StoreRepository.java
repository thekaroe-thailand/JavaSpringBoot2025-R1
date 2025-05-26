package com.app.my_project.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.my_project.entity.StoreEntity;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {
    // หาผลรวมการผลิต, ของเสีย, ของคงเหลือ, ยอดที่รับเข้าแล้วของคลังสินค้า 
    String sql = """
            SELECT 
            SUM(p.qty) AS sumProductionLog
            FROM production_entiry
            """;
    @Query(value=sql, nativeQuery=true)
    public List<Object[]> findByStoreId(@Param("storeId") Long storeId);
}
