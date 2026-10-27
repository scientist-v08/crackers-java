package com.crackers.vinayakatraders.repository;

import com.crackers.vinayakatraders.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query(value = """
            SELECT i FROM Inventory i
            WHERE (:state IS NULL OR i.state = :state)
              AND (:title IS NULL OR :title = '' OR LOWER(COALESCE(i.item, '')) LIKE LOWER(CONCAT('%', :title, '%')))
            ORDER BY i.id DESC
            """,
            countQuery = """
            SELECT COUNT(i) FROM Inventory i
            WHERE (:state IS NULL OR i.state = :state)
              AND (:title IS NULL OR :title = '' OR LOWER(COALESCE(i.item, '')) LIKE LOWER(CONCAT('%', :title, '%')))
            """)
    Page<Inventory> findFiltered(@Param("state") Short state, @Param("title") String title, Pageable pageable);

    @Query("""
            SELECT COALESCE(SUM(i.subTotal), 0) FROM Inventory i
            WHERE (:state IS NULL OR i.state = :state)
              AND (:title IS NULL OR :title = '' OR LOWER(COALESCE(i.item, '')) LIKE LOWER(CONCAT('%', :title, '%')))
            """)
    Long sumSubtotalFiltered(@Param("state") Short state, @Param("title") String title);
}
