package com.kakaopay.throwmoney.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ThrowReq {

    private int sendMoney;
    private int cnt;

}
