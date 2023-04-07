package com.kakaopay.throwmoney.service;

import com.kakaopay.throwmoney.domain.dto.FindInfoRes;
import com.kakaopay.throwmoney.domain.dto.ReceiveInfo;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayService {

    private final UserRepository userRepository;
    private final SendRepository sendRepository;
    private final ReceiveRepository receiveRepository;
    private final ChatRoomRepository chatRoomRepository;


    @Transactional(readOnly = true)
    public FindInfoRes findByToken(Integer userId, Long roomId, String token) {
        // 현재 로그인한 회원 확인
        User user = invalidUser(userId);

        // 채팅방 확인
        ChatRoom chatRoom = invalidChatRoomAndUser(roomId, user);

        // token 유효성 확인
        Send send = invalidToken(token);

        if(send.getUser().getId() != userId) {
            throw new ApplicationException(ErrorCode.FORBIDDEN_ACCESS, "접근권한이 없습니다.");
        }

        List<Receive> receiveList = receiveRepository.findByTokenAndUserNotNull(token);
        log.info("receiveList.size = {}", receiveList.size());
        FindInfoRes findInfoRes = FindInfoRes.of(send);
        List<ReceiveInfo> receiveInfoList = new ArrayList<>();

        if(receiveList.size() != 0) {
            log.info("비어있니");
            receiveList.forEach(r -> {
                receiveInfoList.add(new ReceiveInfo(r.getReceiveMoney(), r.getUser().getKakaoId()));
            });
        }
        findInfoRes.setReceiverList(receiveInfoList);

        return findInfoRes;
    }

    @Transactional
    public String throwMoney(Integer userId, Long roomId, ThrowReq throwReq) {
        // 현재 로그인한 회원 확인
        User user = invalidUser(userId);

        // 채팅방 확인
        ChatRoom chatRoom = invalidChatRoomAndUser(roomId, user);

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

        Send sender = Send.builder()
                .token(token)
                .cnt(throwReq.getCnt())
                .money(throwReq.getSendMoney())
                .remainAmount(throwReq.getSendMoney())
                .user(user)
                .build();

        sendRepository.save(sender);

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

    @Transactional
    public int receiveMoney(Integer userId, Long roomId, String token) {
        // 로그인한 사용자 확인
        User user = invalidUser(userId);

        // token 유효성 확인
        Send send = invalidToken(token);

        // 뿌린 채팅방과 현재 조회하는 채팅방이 일치하지 않은 경우
       if(send.getUser().getChatroom().getId() != roomId) {
           throw new ApplicationException(ErrorCode.HISTORY_NOT_FOUND, "거래 정보를 찾을 수 없습니다.");
       }

        // 채팅방 확인
        ChatRoom room = invalidChatRoomAndUser(roomId, user);

        // 로그인한 사용자와 돈을 뿌린 사람이 일치하는 경우
        if(user.equals(send.getUser())) {
            throw new ApplicationException(ErrorCode.CORRESPOND_USER_AND_SENDER, "머니를 뿌린 사람은 돈을 받을 수 없습니다.");
        }

        // 머니를 뿌린지 10분이 지난 경우
        if(send.getCreateAt().plusMinutes(10).isBefore(LocalDateTime.now())) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN, "머니를 뿌린지 10분이 넘어 돈을 받을 수 없습니다.");
        }

        // 머니 받기
        int receiveMoney = 0;
        // 이미 머니를 받은 사람인 경우 예외 처리
        List<Receive> receiveList = receiveRepository.findByToken(token);
        for (Receive r:receiveList) {
            if(r.getUser() == null) {
                r.setUser(user);
                receiveMoney = r.getReceiveMoney();
                log.info("{}원을 받습니다.", receiveMoney);
                break;
            }else if(r.getUser().equals(user)) {
                throw new ApplicationException(ErrorCode.ALREADY_RECEIVE, "머니를 이미 받았습니다.");
            }
        }

        return receiveMoney;
    }

    private User invalidUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.USERNAME_NOT_FOUND, String.format("%d번 회원은 존재하지 않는 회원입니다.", userId)));
    }

    private ChatRoom invalidChatRoomAndUser(Long roomId, User user) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.CHATROOM_NOT_FOUND, String.format("%d번 채팅방은 존재하지 않는 채팅방입니다.", roomId)));

        if(!chatRoom.getParticipants().contains(user)) {
            throw new ApplicationException(ErrorCode.PARTICIPANT_NOT_FOUND, "채팅방에 참가하고 있지 않은 회원입니다.");
        }

        return chatRoom;
    }

    private Send invalidToken(String token) {
        if(token == null) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN, "유효하지 않은 토큰입니다.");
        }
        Send send = sendRepository.findByToken(token)
                .orElseThrow(() -> new ApplicationException(ErrorCode.INVALID_TOKEN, "유효하지 않은 토큰입니다."));

        if(send.getCreateAt().plusDays(7).isBefore(LocalDateTime.now())) {
            throw new ApplicationException(ErrorCode.INVALID_TOKEN, "토큰이 만료되었습니다.");
        }

        return send;
    }

}
