package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.Order;
import com.beetech.springsecurity.domain.entity.OrderDetail;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface OrderDetailRepository extends ListPagingAndSortingRepository<OrderDetail, Integer>, ListCrudRepository<OrderDetail, Integer> {
}
