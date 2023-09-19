package com.beetech.springsecurity.domain.entity;

import com.beetech.springsecurity.domain.enums.DeleteFlag;
import com.beetech.springsecurity.domain.enums.Role;
import com.beetech.springsecurity.domain.enums.UserStatus;
import com.google.common.collect.Lists;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@Data
public class MstUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "login_id", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "username", length = 255, nullable = false)
    private String userName;

    @Column(name = "role", length = 255, nullable = false)
    private Role role;

    @Column(name = "birthDay", length = 255, nullable = false)
    private LocalDate birthDay;

    @Column(name = "status", length = 255, nullable = false)
    private UserStatus status;

    @Column(name = "delete_flag", length = 255, nullable = false)
    private DeleteFlag deleteflag;

    public MstUser(int id, String email
            , String password, String userName
            , Role role, LocalDate birthDay
            , UserStatus status, DeleteFlag deleteflag) {
        this.id = id;
        this.password = password;
        this.userName = userName;
        this.role = role;
        this.birthDay = birthDay;
        this.email = email;
        this.status = status;
        this.deleteflag = deleteflag;

    }

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ChangePasswordToken changePasswordToken;


    /**
     * Return role list for the user
     *
     * @return role list
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Lists.newArrayList(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getFullName() {
        return userName;
    }

    public List<Order> getOrder() {
        return this.orders;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !status.isLocked();

    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !deleteflag.isDeleteFlag();
    }

    @Override
    public String toString() {
        return "MstUser[" +
                "user_name='" + userName + '\'' +
                ", role='" + role + '\'' +
                ", birthday=" + birthDay +
                ", email='" + email + '\'' +
                ']';
    }
}
