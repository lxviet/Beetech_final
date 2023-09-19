package com.beetech.springsecurity.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category_img")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CategoryImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;


    public CategoryImage(long id,Category category, Image image) {
        this.id = id;
        this.category = category;
        this.image = image;
    }

    public CategoryImage(Category category, Image image) {
        this.category = category;
        this.image = image;
    }

}
