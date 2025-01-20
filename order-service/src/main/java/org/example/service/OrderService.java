package org.example.service;

import org.example.dto.InventoryResponse;
import org.example.dto.OrderLineItemDTO;
import org.example.dto.OrderRequest;
import org.example.model.Order;
import org.example.model.OrderLineItems;
import org.example.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
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
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemDTOList()
                .stream()
                .map(this::mapToDTO).collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                        .map(orderLineItem -> orderLineItem.getSkuCode())
                                .collect(Collectors.toList());

        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                        .uri("http://inventory-service/api/inventory",
                                uribuilder -> uribuilder.queryParam("skuCode", skuCodes).build())
                                .retrieve()
                                        .bodyToMono(InventoryResponse[].class).block();
        boolean allProductIsInStock = Arrays.stream(inventoryResponses)
                .allMatch(inventoryResponse -> inventoryResponse.isInStock());
        if(allProductIsInStock){
            orderRepository.save(order);
        }else{
            throw new IllegalArgumentException("Product is not in stock");
        }

    }

    private OrderLineItems mapToDTO(OrderLineItemDTO orderLineItemDTO){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemDTO.getQuantity());
        orderLineItems.setSkuCode(orderLineItemDTO.getSkuCode());
        orderLineItems.setPrice(orderLineItemDTO.getPrice());
        return orderLineItems;
    }

}
