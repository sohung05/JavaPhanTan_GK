/*
 * @ (#) Product.java          1.0        3/31/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */

package iuh.fit.entity;

import lombok.*;

/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 3/31/2026
 * @version:    1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    //(:Product {unit: "10 boxes x 20 bags", units_in_stock: 39, product_id: "P001", unit_price: 18.0, product_name: "Chai"})
    private String producId;
    private String productName;
    private String unit;
    private double unitPrice;
    private int unitsInStock;

    private Supplier supplier;

    public Product(String producId, String productName, String unit, double unitPrice, int unitsInStock) {
        this.producId = producId;
        this.productName = productName;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
    }
}
