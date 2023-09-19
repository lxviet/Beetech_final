package com.beetech.springsecurity.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "change_password_token")
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;
    private LocalDate expiredDate;

    public ChangePasswordToken(int id, String token, LocalDate expireDate) {
        this.token = token;
        this.id = id;
        this.expiredDate = expireDate;
    }

    public void setUser(MstUser user) {
        this.user = user;
    }

    @OneToOne
    @JoinColumn(name = "login_id")
    private MstUser user;

}
