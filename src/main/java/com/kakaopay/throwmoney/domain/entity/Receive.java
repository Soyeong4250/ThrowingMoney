package com.kakaopay.throwmoney.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Receive extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token;

    private int receiveMoney;  // 받을 금액

    private boolean confirm;  // 받았는지 확인

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // 받은 사람 정보

    @ManyToOne
    @JoinColumn(name = "history_id")
    private History history;  // 기록

    @Builder
    public Receive(Long id, String token, Integer receiveMoney, User user) {
        this.id = id;
        this.token = token;
        this.receiveMoney = receiveMoney;
        this.user = user;
        this.confirm = false;
    }
}
