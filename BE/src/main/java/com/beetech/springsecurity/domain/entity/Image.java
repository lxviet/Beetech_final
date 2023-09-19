package com.beetech.springsecurity.domain.entity;


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

public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String path;
    private String name;
    private int thumb_flag;

    @OneToOne(mappedBy = "image", cascade = CascadeType.ALL)
    private CategoryImage categoryImageMapping;


    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL)
    private List<ProductImage> productImages;

}

