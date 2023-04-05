package com.kakaopay.throwmoney.domain.dto;

import com.kakaopay.throwmoney.domain.entity.Receive;
import com.kakaopay.throwmoney.domain.entity.Send;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
public class FindInfoRes {

    private LocalDateTime time;
    private int sendMoney;
    private int receiveMoney;
    private List<ReceiveInfo> receiverList;

    public static FindInfoRes of(Send send, List<Receive> receives) {
        List<ReceiveInfo> receiveInfoList = new ArrayList<>();

        receives.forEach(r -> {
            receiveInfoList.add(new ReceiveInfo(r.getReceiveMoney(), r.getUser().getKakaoId()));
        });

        return FindInfoRes.builder()
                .time(send.getCreateAt())
                .sendMoney(send.getMoney())
                .receiveMoney(send.getMoney() - send.getRemainAmount())
                .receiverList(receiveInfoList)
                .build();
    }
}
