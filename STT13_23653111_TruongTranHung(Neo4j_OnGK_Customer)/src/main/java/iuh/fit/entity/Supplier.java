/*
 * @ (#) Supplier.java          1.0        3/31/2026
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
public class Supplier {
    //(:Supplier {country: "UK", contact_name: "Charlotte Cooper", company_name: "Exotic Liquids", supplier_id: "S001"})
    private String supplierId;
    private String companyName;
    private String contactName;
    private String country;

}
