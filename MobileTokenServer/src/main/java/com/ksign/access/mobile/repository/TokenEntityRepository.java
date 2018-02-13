package com.ksign.access.mobile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ksign.access.mobile.entity.TokenEntity;

public interface TokenEntityRepository extends JpaRepository<TokenEntity, String> {

}
