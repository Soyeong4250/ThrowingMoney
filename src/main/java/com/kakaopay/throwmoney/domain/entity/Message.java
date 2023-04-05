package com.kakaopay.throwmoney.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Message extends BaseEntity {

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
