package com.team5.pyeonjip.product.repository;

import com.team5.pyeonjip.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Product 엔티티에 대한 JPA 레포지토리.
 * - Product와 관련된 CRUD 및 데이터베이스 연산을 처리합니다.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Product 엔티티와 관련된 커스텀 쿼리나 메서드를 필요에 따라 추가할 수 있습니다.
    @Query("SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.productDetails d " +
            "LEFT JOIN FETCH p.productImages i")
    List<Product> findAllWithDetailsAndImages();
}