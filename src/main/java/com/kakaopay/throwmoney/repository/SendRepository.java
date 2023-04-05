package com.kakaopay.throwmoney.repository;

import com.kakaopay.throwmoney.domain.entity.Send;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SendRepository extends JpaRepository<Send, Long> {
    Optional<Send> findByToken(String token);
}
