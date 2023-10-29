package com.em.reportserver.order;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long posId;
    private OrderType orderType;
    private LocalDateTime intervalFrom;
    private LocalDateTime intervalTo;
}
