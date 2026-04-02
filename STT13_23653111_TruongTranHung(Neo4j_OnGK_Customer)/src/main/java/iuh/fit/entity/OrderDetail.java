/*
* @ (#) OrderDetail.java          1.0        3/31/2026
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
public class OrderDetail {
    private Order order;
    private Product product;
    private int quantity;
    private double unitPrice;
    private double discount;
}
