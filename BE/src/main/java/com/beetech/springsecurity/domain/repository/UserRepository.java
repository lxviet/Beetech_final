package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.MstUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<MstUser, Integer> {
    MstUser findByEmail(String email);

    MstUser findById(int id);

    @Query("SELECT u FROM MstUser u " +
            "JOIN u.orders o " +
            "WHERE (:username IS NULL OR u.userName LIKE %:username%) " +
            "AND (:email IS NULL OR u.email LIKE %:email%) " +
            "AND (:startDate IS NULL OR u.birthDay >= :startDate) " +
            "AND (:endDate IS NULL OR u.birthDay <= :endDate) " +
            "AND (:totalPrice IS NULL OR o.totalPrice >= :totalPrice)")
    List<MstUser> searchUsers(@Param("username") String username,
                              @Param("email") String email,
                              @Param("startDate") LocalDate startDate,
                              @Param("endDate") LocalDate endDate,
                              @Param("totalPrice") Long totalPrice,
                              Pageable pageable);
}
