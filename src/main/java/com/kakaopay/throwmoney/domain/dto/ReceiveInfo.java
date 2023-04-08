package com.kakaopay.throwmoney.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveInfo {

    private int receiveMoney;  // 받은 금액
    private String kakaoId;  // 사용자 아이디

}
