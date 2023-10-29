package com.em.reportserver.order;

import java.util.List;

public interface OrderService {


    List<OrderItem> getOrders(String username);

    OrderItem createOrder(String username, OrderItemDto orderItemDto);

    List<OrderItem> getOrderJobs(String username, String code);



}
