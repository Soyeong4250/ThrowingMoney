package com.kakaopay.throwmoney.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Send extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token;  // 뿌리기 식별

    private int cnt;  // 받을 수 있는 인원

    private int money;  // 원래 뿌린 금액

    private int remainAmount;  // 남은 금액

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // 뿌린 사람 정보

    @Builder
    public Send(Long id, String token, int money, int remainAmount, User user) {
        this.id = id;
        this.token = token;
        this.money = money;
        this.remainAmount = remainAmount;
        this.user = user;
    }
}
