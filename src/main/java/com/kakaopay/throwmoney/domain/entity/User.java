package com.kakaopay.throwmoney.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "X-USER-ID")
    private Integer id;

    private String name;

    private String email;

    private String kakaoId;

    private String password;

    private String bank;

    private String account;

    private Long money;

    @ManyToOne
    MoneyInfo moneyInfo;

    @Builder
    public User(Integer id, String name, String email, String kakaoId, String bank, String account, Long money) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.kakaoId = kakaoId;
        this.bank = bank;
        this.account = account;
        this.money = money;
    }
}
