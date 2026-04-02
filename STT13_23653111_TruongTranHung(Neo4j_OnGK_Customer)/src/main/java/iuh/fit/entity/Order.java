/*
 * @ (#) Order.java          1.0        3/31/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */

package iuh.fit.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

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
public class Order {
    private String orderId;
    private String customerName;
    private String employeeName;
    private LocalDate orderDate;
    private Status status;
    private List<OrderDetail> orderDetails;

}
