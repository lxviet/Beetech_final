package com.beetech.springsecurity.domain.entity;

import com.google.common.collect.Lists;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    @OneToOne(mappedBy = "category", cascade = CascadeType.ALL)
    private CategoryImage categoryImages;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;


    @Override
    public String toString() {
        return String.format(
                "User[id=%d, fullName='%s']",
                id, name);
    }
}
