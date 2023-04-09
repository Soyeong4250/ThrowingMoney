package com.kakaopay.throwmoney.repository;

import com.kakaopay.throwmoney.domain.entity.Receive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiveRepository extends JpaRepository<Receive, Long> {

    List<Receive> findByTokenAndUserNotNull(@Param("token") String token);

    Optional<Receive> findByTokenAndUserId(String token, Integer userId);

    List<Receive> findByTokenAndUserIsNull(String token);
}
