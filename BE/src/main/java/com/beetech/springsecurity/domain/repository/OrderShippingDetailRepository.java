package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.OrderShippingDetail;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface OrderShippingDetailRepository extends ListPagingAndSortingRepository<OrderShippingDetail, Integer>, ListCrudRepository<OrderShippingDetail, Integer> {

}
