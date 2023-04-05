package com.kakaopay.throwmoney.service;

import com.kakaopay.throwmoney.domain.dto.FindInfoRes;
import com.kakaopay.throwmoney.domain.entity.Receive;
import com.kakaopay.throwmoney.domain.entity.Send;
import com.kakaopay.throwmoney.exception.ApplicationException;
import com.kakaopay.throwmoney.exception.ErrorCode;
import com.kakaopay.throwmoney.repository.ChatRoomRepository;
import com.kakaopay.throwmoney.repository.ReceiveRepository;
import com.kakaopay.throwmoney.repository.SendRepository;
import com.kakaopay.throwmoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayService {

    private final UserRepository userRepository;
    private final SendRepository sendRepository;
    private final ReceiveRepository receiveRepository;
    private final ChatRoomRepository chatRoomRepository;
//    private final HistoryRepository historyRepository;

    @Transactional(readOnly = true)
    public FindInfoRes findByToken(Integer userId, Long roomId, String token) {
        // 현재 로그인한 회원 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, String.format("%d번 회원은 존재하지 않는 회원입니다.", userId)));

        // 채팅방 확인
        chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CHATROOM_NOT_FOUND, String.format("%d번 채팅방은 존재하지 않는 채팅방입니다.", roomId)));

        // token 유효성 확인
        if(token == null) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN, "유효하지 않은 토큰입니다.");
        }
        Send send = sendRepository.findByToken(token)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_TOKEN, "유효하지 않은 토큰입니다."));

        if(send.getUser().getId() != userId) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_ACCESS, "접근권한이 없습니다.");
        } else if(send.getCreateAt().plusDays(7).isBefore(LocalDateTime.now())) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN, "토큰이 만료되었습니다.");
        }

        List<Receive> receiveList = receiveRepository.findByToken(token);

        return FindInfoRes.of(send, receiveList);
    }

}
