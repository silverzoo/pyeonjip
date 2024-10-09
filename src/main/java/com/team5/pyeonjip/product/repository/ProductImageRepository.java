package com.team5.pyeonjip.product.repository;

import com.team5.pyeonjip.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.id = :product_Id")
    List<ProductImage> findByProductId(@Param("product_Id") Long productId);

    // 특정 Product ID에 연결된 모든 ProductImage 삭제
    //todo: cascade 옵션으로 삭제
//    @Modifying
//    @Query("DELETE FROM ProductImage pi WHERE pi.product.id = :product_Id")
//    void deleteByProductId(@Param("product_Id") Long productId);
}