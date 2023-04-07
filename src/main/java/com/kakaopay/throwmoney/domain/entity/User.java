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
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String kakaoId;

//    private String bank;

//    private String account;

    private int money;  // 현재 보유 금액

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatroom;

    @Builder
    public User(Integer id, String name, String email, String kakaoId, int money) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.kakaoId = kakaoId;
        this.money = money;
    }

    public void setMoney(int sendMoney) {
        this.money = sendMoney;
    }
}
