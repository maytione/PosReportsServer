package com.em.reportserver.order;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class OrderResponse {

    private Long id;
    private OrderType orderType;
    private Long posId;
    private LocalDateTime intervalFrom;
    private LocalDateTime intervalTo;
    private String reportData;
}
