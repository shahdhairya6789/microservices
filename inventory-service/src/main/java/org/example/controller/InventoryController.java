package org.example.controller;

import org.example.dto.InventoryRequest;
import org.example.dto.InventoryResponse;
import org.example.model.Inventory;
import org.example.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createInventory(@RequestBody InventoryRequest inventoryRequest){
        inventoryService.createInventory(inventoryRequest);
        return "Inventory saved";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam("skuCode") List<String> skuCode){
        List<InventoryResponse> inventoryResponses = inventoryService.isInStock(skuCode);
        return inventoryResponses;
    }

}
