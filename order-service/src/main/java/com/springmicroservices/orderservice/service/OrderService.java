package com.springmicroservices.orderservice.service;

import com.springmicroservices.orderservice.dto.InventoryResponse;
import com.springmicroservices.orderservice.dto.OrderLineItemsDto;
import com.springmicroservices.orderservice.dto.OrderRequest;
import com.springmicroservices.orderservice.model.Order;
import com.springmicroservices.orderservice.model.OrderLineItems;
import com.springmicroservices.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        order.setOrderLineItemsList(orderLineItems);

        //get all skuCodes
        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).collect(Collectors.toList());


        // call inventory service and place order if product is in stock
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get().uri("http://inventory-service/api/inventory"
                        , uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        assert inventoryResponses != null;
        boolean allProducts = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        if (allProducts) {
            orderRepository.save(order);
            return "Order placed successfully";
        }
        throw new ServiceException("Product is not in stock, please try again");
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
