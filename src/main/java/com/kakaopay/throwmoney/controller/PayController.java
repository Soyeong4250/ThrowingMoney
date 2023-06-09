package com.kakaopay.throwmoney.controller;

import com.kakaopay.throwmoney.domain.dto.FindInfoRes;
import com.kakaopay.throwmoney.domain.dto.Response;
import com.kakaopay.throwmoney.domain.dto.ThrowReq;
import com.kakaopay.throwmoney.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/pay")
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    // 조회

    @GetMapping("/record")
    public Response<FindInfoRes> getRecord(@RequestHeader(value = "X-USER-ID") Integer userId,
                                           @RequestHeader(value = "X-ROOM-ID") Long roomId,
                                           @RequestParam String token) {
        log.info("조회");
        FindInfoRes record = payService.findByToken(userId, roomId, token);
        return Response.success(record);
    }

    // 뿌리기
    @PostMapping("/throw")
    public Response<String> throwMoney(@RequestHeader(value = "X-USER-ID") Integer userId,
                                 @RequestHeader(value = "X-ROOM-ID") Long roomId,
                                 @RequestBody ThrowReq throwReq) {
        log.info("뿌리기");
        String token = payService.throwMoney(userId, roomId, throwReq);
        return Response.success(String.format("생성된 token은 %s 입니다.", token));
    }

    // 받기
    @PostMapping("/receive")
    public Response<String> receiveMoney(@RequestHeader(value = "X-USER-ID") Integer userId,
                                         @RequestHeader(value ="X-ROOM-ID") Long roomId,
                                         @RequestParam String token) {
        log.info("받기");
        int receiveMoney = payService.receiveMoney(userId, roomId, token);
        return Response.success(receiveMoney + "원 받기가 완료되었습니다.");
    }
}
