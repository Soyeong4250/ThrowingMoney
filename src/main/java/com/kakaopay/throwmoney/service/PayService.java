package com.kakaopay.throwmoney.service;

import com.kakaopay.throwmoney.domain.dto.FindInfoRes;
import com.kakaopay.throwmoney.domain.dto.ThrowReq;
import com.kakaopay.throwmoney.domain.entity.ChatRoom;
import com.kakaopay.throwmoney.domain.entity.Receive;
import com.kakaopay.throwmoney.domain.entity.Send;
import com.kakaopay.throwmoney.domain.entity.User;
import com.kakaopay.throwmoney.exception.ApplicationException;
import com.kakaopay.throwmoney.exception.ErrorCode;
import com.kakaopay.throwmoney.repository.ChatRoomRepository;
import com.kakaopay.throwmoney.repository.ReceiveRepository;
import com.kakaopay.throwmoney.repository.SendRepository;
import com.kakaopay.throwmoney.repository.UserRepository;
import com.kakaopay.throwmoney.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
        invalidUser(userId);

        // 채팅방 확인
        invalidChatRoom(roomId);

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

    @Transactional
    public String throwMoney(Integer userId, Long roomId, ThrowReq throwReq) {
        // 현재 로그인한 회원 확인
        User user = invalidUser(userId);

        // 채팅방 확인
        ChatRoom chatRoom = invalidChatRoom(roomId);

        // 머니 뿌리기
        if(user.getMoney() < throwReq.getSendMoney()) {
            throw new ApplicationException(ErrorCode.LACK_OF_MONEY, "현재 보유하고 있는 금액이 부족합니다.");
        } else if(chatRoom.getParticipants().size() - 1 < throwReq.getCnt()) {  // 본인을 제외한 채팅방 참가자 수와 뿌릴 인원 비교
            throw new ApplicationException(ErrorCode.EXCESS_OF_PEOPLE, "현재 인원보다 뿌리고자 하는 인원이 많습니다.");
        }

        // 로그인한 회원의 잔액 변경
        setUserMoney(user, throwReq.getSendMoney());

        String token = "";
        // token 생성
        while(true) {
            token = RandomUtil.createToken();

            if(!sendRepository.findByToken(token).isPresent()) {
                log.info("생성된 token : {}", token);
                break;
            }
        }

        sendRepository.save(Send.builder()
//                .token(token)
                .cnt(throwReq.getCnt())
                .money(throwReq.getSendMoney())
                .remainAmount(throwReq.getSendMoney())
                .user(user)
                .build());

        // 뿌리기
        divideMoney(token, throwReq.getSendMoney(), throwReq.getCnt());

        return token;
    }

    private void setUserMoney(User user, int sendMoney) {
        user.setMoney(user.getMoney() - sendMoney);
    }

    private void divideMoney(String token, int total, int cnt) {
        while(total > 0 && cnt  > 0) {
            int random;
            if(cnt == 1) {
                random = total;
            } else {
                random = RandomUtil.randomMoney(total);
            }

            total -= random;
            cnt -= 1;
            Receive receive = Receive.builder()
                    .token(token)
                    .receiveMoney(random)
                    .build();
            receiveRepository.save(receive);
        }
    }
    private ChatRoom invalidChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CHATROOM_NOT_FOUND, String.format("%d번 채팅방은 존재하지 않는 채팅방입니다.", roomId)));
    }

    private User invalidUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, String.format("%d번 회원은 존재하지 않는 회원입니다.", userId)));
    }
}
