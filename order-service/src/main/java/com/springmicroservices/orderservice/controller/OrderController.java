package com.springmicroservices.orderservice.controller;

import com.springmicroservices.orderservice.dto.OrderRequest;
import com.springmicroservices.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory",fallbackMethod = "fallBackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        return CompletableFuture.supplyAsync(() ->orderService.placeOrder(orderRequest));
    }

    CompletableFuture<String> fallBackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"Oops! something went wrong, try again later!");
    }
}
