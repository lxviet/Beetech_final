package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.Cart;
import com.beetech.springsecurity.domain.entity.Order;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends ListPagingAndSortingRepository<Order, Integer>, ListCrudRepository<Order, Integer> {
    Optional<Order> findByUser_id(int userId);
    Optional<Order> findByIdAndDisplayId(int id, String displayId);

    Optional<Order> findByUser_idAndDisplayId(int ownerId, String displayId);
}



