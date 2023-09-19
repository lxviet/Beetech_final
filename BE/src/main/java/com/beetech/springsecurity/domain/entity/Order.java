package com.beetech.springsecurity.domain.entity;

import com.beetech.springsecurity.domain.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Table(name = "order_table")
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String displayId;
    private OrderStatus status;
    private LocalDate orderDate;
    private String userNote;
    private long totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MstUser user;
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderShippingDetail orderShippingDetail;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}
