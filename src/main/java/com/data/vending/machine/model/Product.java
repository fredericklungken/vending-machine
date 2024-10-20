package com.data.vending.machine.model;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    @NotNull(message = "Product name is required")
    private String name;  // Name of the product

    @NotNull(message = "Product price is required")
    private Integer price;  // Price of the product in the vending machine
}
