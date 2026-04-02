/*
 * @ (#) SupplierDAO.java          1.0        3/31/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */

package iuh.fit.dao;
import iuh.fit.entity.Supplier;
import iuh.fit.untils.AppUntils;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 3/31/2026
 * @version:    1.0
 */


import java.util.Map;

public class SupplierDAO {
    public static boolean updateSupplier (Supplier supplier) {
        if (supplier == null) {
            System.out.println("supplier: Không được null");
        }else if (supplier.getSupplierId() ==""||supplier.getSupplierId()==null){
            System.out.println("SupplierID: Không được null hoặc rỗng");
        }else if (supplier.getCompanyName() ==""||supplier.getCompanyName()==null){
            System.out.println("Các thuộc tính cập nhật: CompanyName: Không được null hoặc rỗng");
        }

        String query = """
                MATCH (s:Supplier {supplier_id: $supplierId})
                WHERE $companyName IS NOT NULL AND trim($companyName) <> ""
                SET s.company_name = $companyName
                RETURN COUNT(s) > 0 AS updated
                """;
        Map<String,Object> params = Map.of("supplierId",supplier.getSupplierId(),"companyName",supplier.getCompanyName());
        try(Session session = AppUntils.getSession()) {
            return session.executeWrite(tx -> {
                Result result = tx.run(query,params);
                return result.single().get("updated").asBoolean();
            });
        }
    }

    public static void main(String[] args) {
        Supplier supplier = new Supplier("S006","Mayumi Ohno","VietNam","TrungNguyen");
        System.out.println(updateSupplier(supplier));
    }
}
