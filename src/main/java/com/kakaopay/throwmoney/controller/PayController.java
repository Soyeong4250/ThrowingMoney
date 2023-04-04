package com.kakaopay.throwmoney.controller;

import com.kakaopay.throwmoney.domain.dto.FindInfoRes;
import com.kakaopay.throwmoney.domain.dto.Response;
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
    @GetMapping()
    public Response<FindInfoRes> getRecord(@RequestHeader(value = "X-USER-ID") Integer userId,
                                           @RequestHeader(value = "X-ROOM-ID") Long roomId,
                                           @RequestParam String token) {
        log.info("조회");
        payService.findByToken(userId, roomId, token);
        return Response.success(null);
    }
}
