package com.em.reportserver.order;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
@PreAuthorize("hasRole('USER')")
public class OrderController {

    private final OrderServiceImpl orderService;

    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<?> getOrders(Principal principal) {
        var orders = orderService.getOrders(principal.getName());
        List<OrderResponse> response = orders
                .stream()
                .map(item -> modelMapper.map(item, OrderResponse.class))
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<?> createOrder(Principal principal, @Validated @RequestBody OrderItemDto orderItemDto) {
        var result = orderService.createOrder(principal.getName(), orderItemDto);
        OrderItemDto response = new OrderItemDto();
        modelMapper.map(result, response);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{code}")
    public ResponseEntity<?> getOrderJobs(Principal principal, @PathVariable String code) {
        var orders = orderService.getOrderJobs(principal.getName(), code);
        List<OrderResponse> response = orders
                .stream()
                .map(item -> modelMapper.map(item, OrderResponse.class))
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
