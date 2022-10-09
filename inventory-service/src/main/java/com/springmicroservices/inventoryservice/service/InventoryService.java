package com.springmicroservices.inventoryservice.service;

import com.springmicroservices.inventoryservice.dto.InventoryResponse;
import com.springmicroservices.inventoryservice.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes){
        log.info("wait started");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("wait ended");
        return inventoryRepository.findBySkuCodeIn(skuCodes)
                .stream()
                .map(inventory ->
                    InventoryResponse.builder().skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity()>0)
                            .build()
                ).collect(Collectors.toList());
    }
}
