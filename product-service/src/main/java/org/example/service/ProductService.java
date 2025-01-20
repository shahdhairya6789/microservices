package org.example.service;

import org.example.dto.ProductRequest;
import org.example.dto.ProductResponse;
import org.example.model.Product;
import org.example.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product " + product.getId() + " is saved successfully");
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> productList = productRepository.findAll();
        List<ProductResponse> productResponses = productList
                .stream()
                .map(this::mapToProductResponse).collect(Collectors.toList());
        return productResponses;
    }

    private ProductResponse mapToProductResponse(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .description(product.getDescription())
                .name(product.getName())
                .price(product.getPrice())
                .build();
        return productResponse;
    }


}
