package com.kakaopay.throwmoney.domain.dto;

import com.kakaopay.throwmoney.domain.entity.Send;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter @Getter
@AllArgsConstructor
public class FindInfoRes {

    private LocalDateTime time;
    private int sendMoney;
    private int receiveMoney;
    private List<ReceiveInfo> receiverList;

    public static FindInfoRes of(Send send) {
        return FindInfoRes.builder()
                .time(send.getCreateAt())
                .sendMoney(send.getMoney())
                .receiveMoney(send.getMoney() - send.getRemainAmount())
                .build();
    }
}
