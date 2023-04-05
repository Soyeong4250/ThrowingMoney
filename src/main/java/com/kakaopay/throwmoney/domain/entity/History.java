package com.kakaopay.throwmoney.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @OneToOne
    private Sender sender;

    @OneToMany(mappedBy = "history", cascade = CascadeType.REMOVE)
    private List<Receiver> receivers = new ArrayList<>();

    @Builder
    public History(String id, Sender sender, List<Receiver> receivers) {
        this.id = id;
        this.sender = sender;
        this.receivers = receivers;
    }
}
