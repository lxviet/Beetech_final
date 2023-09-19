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
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int version_no;
    private long total_price;
    private String token;
    private String userNote;
    public void setCartDetails(List<CartDetail> cartDetails) {
        this.cartDetails = cartDetails;
    }

    public void setUser(MstUser user) {
        this.user = user;
    }

    public Cart(int version_no, long total_price, MstUser user) {
        this.version_no = version_no;
        this.total_price = total_price;
        this.user = user;
    }

    public Cart(int version_no, long total_price, String token) {
        this.version_no = version_no;
        this.total_price = total_price;
        this.token = token;
    }

    @OneToOne
    @JoinColumn(name = "loginId")
    private MstUser user;

    @OneToMany(mappedBy = "cart")
    @JsonManagedReference
    private List<CartDetail> cartDetails;


}
