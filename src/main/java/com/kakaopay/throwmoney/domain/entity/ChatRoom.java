package com.kakaopay.throwmoney.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chatroom")
@Setter @Getter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @Column(name = "chatroom_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private boolean openChat;  // 오픈 채팅 여부

    @OneToMany(mappedBy = "chatroom")
    private List<User> participants = new ArrayList<>();

    @Builder
    public ChatRoom(Long id, List<User> participants) {
        this.id = id;
        this.participants = participants;
    }
}
