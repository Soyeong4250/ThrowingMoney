package com.kakaopay.throwmoney.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    private Integer fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user")
    private User toUser;

    private Long room_id;

    @Builder
    public Message(Long id, String contents, Integer fromUser, User toUser, Long room_id) {
        this.id = id;
        this.contents = contents;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.room_id = room_id;
    }
}
