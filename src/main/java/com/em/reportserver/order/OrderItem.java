package com.em.reportserver.order;


import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orderitem", schema = "pos")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderitem_generator")
    @SequenceGenerator(name = "orderitem_generator", sequenceName = "orderitem_seq", allocationSize = 1)
    private Long id;
    @Column(name = "order_type")
    private OrderType orderType;
    @Column(name = "user_Id")
    private Long userId;
    @Column(name = "pos_Id")
    private Long posId;
    @Column(name = "interval_from")
    private LocalDateTime intervalFrom;
    @Column(name = "interval_to")
    private LocalDateTime intervalTo;
    @Column(name = "report_data")
    private String reportData;


}
