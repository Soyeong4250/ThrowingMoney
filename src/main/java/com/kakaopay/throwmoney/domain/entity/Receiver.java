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
public class Receiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(unique = true)
    private String token;

    private LocalDateTime date;

    private Integer receiveMoney;  // 받은 금액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 받은 사람 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private History history;  // 기록

    @Builder
    public Receiver(String id, String token, LocalDateTime date, Integer receiveMoney, User user) {
        this.id = id;
        this.token = token;
        this.date = date;
        this.receiveMoney = receiveMoney;
        this.user = user;
    }
}
