package com.em.reportserver.order;

import com.em.reportserver.pos.PosRepository;
import com.em.reportserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final UserRepository userRepository;
    private final PosRepository posRepository;
    private final OrderRepository orderRepository;

    public List<OrderItem> getOrders(String username) {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        var orders = orderRepository.findAllByUserId(user.get().getId());
        return orders.orElse(null);
    }

    public OrderItem createOrder(String username, OrderItemDto orderRequest) {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        var pos = posRepository.findById(orderRequest.getPosId());
        if (pos.isEmpty()) {
            throw new RuntimeException("POS not found");
        }

        if (pos.get().getUser() == null || !Objects.equals(pos.get().getUser().getId(), user.get().getId())) {
            throw new RuntimeException("POS not assigned");
        }

        OrderItem order = OrderItem.builder()
                .orderType(orderRequest.getOrderType())
                .intervalFrom(orderRequest.getIntervalFrom())
                .intervalTo(orderRequest.getIntervalTo())
                .posId(pos.get().getId())
                .userId(user.get().getId())
                .build();

        return orderRepository.save(order);

    }

    public List<OrderItem> getOrderJobs(String username, String code) {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        var pos = posRepository.findByUserIdAndCode(user.get().getId(), code);
        if (pos.isEmpty()) {
            throw new RuntimeException("POS not found or not assigned to user");
        }

        var orders = orderRepository.findAllByUserIdAndPosIdAndReportData(user.get().getId(), pos.get().getId(), null);
        return orders.orElse(null);
    }


}
