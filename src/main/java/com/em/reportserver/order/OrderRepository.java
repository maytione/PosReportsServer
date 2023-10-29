package com.em.reportserver.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderItem, Long> {

      Optional<List<OrderItem>> findAllByUserId(Long userId);
      Optional<List<OrderItem>> findAllByUserIdAndPosIdAndReportData(Long userId, Long posId, String reportData);
}
