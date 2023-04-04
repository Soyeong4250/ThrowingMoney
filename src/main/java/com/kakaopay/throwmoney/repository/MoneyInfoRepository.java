package com.kakaopay.throwmoney.repository;

import com.kakaopay.throwmoney.domain.entity.MoneyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyInfoRepository extends JpaRepository<MoneyInfo, Long> {
}
