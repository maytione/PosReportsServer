package com.em.reportserver.pos;

import com.em.reportserver.order.OrderItem;
import com.em.reportserver.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pos", schema = "pos")
public class Pos {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pos_generator")
    @SequenceGenerator(name = "pos_generator", sequenceName = "pos_seq", allocationSize = 1)
    private Long id;
    private String name;
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "pos_id", referencedColumnName = "id")
    @JsonIgnore
    private List<OrderItem> orders;


}
