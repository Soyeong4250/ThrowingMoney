package com.kakaopay.throwmoney.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chatroom")
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatRoom extends BaseEntity {

    @Id
    @Column(name = "chatroom_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chatroom")
    private List<User> participants = new ArrayList<>();


    @Builder
    public ChatRoom(Long id, List<User> participants) {
        this.id = id;
        this.participants = participants;
    }
}
