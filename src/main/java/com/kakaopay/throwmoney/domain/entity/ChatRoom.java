package com.kakaopay.throwmoney.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "X-ROOM-ID")
    private Long id;

    boolean openChat;

    @OneToMany (mappedBy = "chatroom_id")
    private List<User> participants = new ArrayList<>();

    @Builder
    public ChatRoom(Long id, boolean openChat, List<User> participants) {
        this.id = id;
        this.openChat = openChat;
        this.participants = participants;
    }
}
