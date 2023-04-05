package com.kakaopay.throwmoney.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter @Getter
@NoArgsConstructor
public class Sender {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token;  // 뿌리기 식별

    private LocalDateTime date;

    private Long money;  // 원래 뿌린 금액

    private Long remainAmount;  // 남은 금액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 뿌린 사람 정보

    @Builder
    public Sender(Long id, String token, LocalDateTime date, Long money, Long remainAmount, User user) {
        this.id = id;
        this.token = token;
        this.date = date;
        this.money = money;
        this.remainAmount = remainAmount;
        this.user = user;
    }
}
