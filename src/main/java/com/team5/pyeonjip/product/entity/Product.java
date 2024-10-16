package com.team5.pyeonjip.product.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.team5.pyeonjip.category.entity.Category;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"productDetails", "productImages"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<ProductDetail> productDetails;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ProductImage> productImages;


    // ID만을 받는 생성자 추가
    public Product(Long id) {
        this.id = id;
    }




}
