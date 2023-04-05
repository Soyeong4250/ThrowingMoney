package com.kakaopay.throwmoney.repository;

import com.kakaopay.throwmoney.domain.entity.Receive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiveRepository extends JpaRepository<Receive, Long> {
    List<Receive> findByToken(String token);
}
