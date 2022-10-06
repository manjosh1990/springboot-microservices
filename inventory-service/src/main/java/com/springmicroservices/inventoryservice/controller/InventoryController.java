package com.springmicroservices.inventoryservice.controller;

import com.springmicroservices.inventoryservice.dto.InventoryResponse;
import com.springmicroservices.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCodes){
       return inventoryService.isInStock(skuCodes);
    }
}
