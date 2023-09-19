package com.beetech.springsecurity.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String sku;

    @Column(unique = true)
    private String name;

    private String detailInfo;

    private long price;

    public void setCategory(Category category) {
        this.category = category;
    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Integer delete_flag;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CartDetail> cartDetails;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> productImages;


    public Product(String sku, String name, String detail_info, long price, Integer delete_flag) {

        this.sku = sku;
        this.name = name;
        this.detailInfo = detail_info;
        this.price = price;
        this.delete_flag = delete_flag;
    }

    public Product(int id, String sku, String name, String detail_info, long price, Integer delete_flag) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.detailInfo = detail_info;
        this.price = price;
        this.delete_flag = delete_flag;
    }

    @Override
    public String toString() {
        return String.format("Product[id = %s, sku = %s, productName = %s, detail_info = %s, price = %s, delete_flag = %s",
                id, sku, name, detailInfo, price, delete_flag);
    }

}
