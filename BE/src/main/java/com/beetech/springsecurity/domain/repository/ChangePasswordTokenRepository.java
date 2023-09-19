package com.beetech.springsecurity.domain.repository;

import com.beetech.springsecurity.domain.entity.ChangePasswordToken;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface ChangePasswordTokenRepository extends ListPagingAndSortingRepository<ChangePasswordToken, Integer>, ListCrudRepository<ChangePasswordToken, Integer> {

}


