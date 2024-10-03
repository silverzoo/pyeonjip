package com.team5.pyeonjip.product.repository;

import com.team5.pyeonjip.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ProductRepository extends JpaRepository<Product, Long> {


}