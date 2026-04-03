/*
 * @ (#) SupplierDAO.java          1.0        3/31/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */

package iuh.fit.dao;
import iuh.fit.entity.Product;
import iuh.fit.entity.Supplier;
import iuh.fit.untils.AppUntils;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.summary.ResultSummary;
import org.neo4j.driver.types.Node;
/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 3/31/2026
 * @version:    1.0
 */


import java.util.List;
import java.util.Map;

public class SupplierDAO {
    public List<Product> listProductsBySupplier (String companyName, int page, int size ){
        if( companyName== null || companyName.isBlank()) {
            throw new IllegalArgumentException("companyName không được null hoặc rỗng");
        } if(page <1){
            throw new IllegalArgumentException("page: Phải là số nguyên dương (page ≥ 1)  ");
        } if (size  <1 ) {
            throw new IllegalArgumentException("size: Phải là số nguyên dương (size ≥ 1) ");
        }
        int skip = (page -1 )* size;
        String query = """
                MATCH (s:Supplier)-[r:SUPPLIES]->(p:Product)
                WHERE s.company_name = $companyName
                RETURN p SKIP $skip LIMIT $limit
                """;
        Map<String ,Object> pagrams = Map.of(
                "companyName", companyName,
                "skip",skip,
                "limit", size
        );
        try(Session session = AppUntils.getSession()){
            return session.executeRead(tx->{
                Result res = tx.run(query, pagrams);
                return res
                        .stream()
                        .map(r->{
                            Node node = r.get("p").asNode();
                            return new Product(
                                    node.get("product_id").asString(),
                                    node.get("product_name").asString(),
                                    node.get("unit").asString(),
                                    node.get("unit_price").asDouble(),
                                    node.get("units_in_stock").asInt()

                            );
                        })
                        .toList();
            });

        }
        }
    public boolean updateSupplier  (Supplier supplier ){
        if(supplier == null) {
            throw  new IllegalArgumentException("supplier is  null");}
        if(supplier.getSupplierId()== null || supplier.getSupplierId().isBlank()) {
            throw new IllegalArgumentException("Supplier không được null");
        }if(supplier.getCompanyName() == null || supplier.getCompanyName().isBlank()) {
            throw new IllegalArgumentException("CompanyName không được null hoặc rỗng");
        }
        String query = """
                MATCH (s:Supplier)
                WHERE s.supplier_id = $supplierId
                SET s.company_name = $companyName
                RETURN s
                """;
        Map<String ,Object> pagrams = Map.of(
                "supplierId",supplier.getSupplierId(),
                "companyName",supplier.getCompanyName()
        );
        try(Session session = AppUntils.getSession()){
            return session.executeWrite(tx->{
                ResultSummary resultSummary = tx.run(query,pagrams).consume();
                return  resultSummary.counters().propertiesSet()>0;
            });

        }
    }

        public double calculateTotalOrder (String orderID ){
        if(orderID == null || orderID.isBlank()) {
            return 0.0;
        }
        String querry = """
                MATCH (o:Order)-[r:ORDERS]-(p:Product)
                WHERE o.order_id = $orderId
                RETURN sum(r.quantity*r.unit_price*(1-r.discount)) as total
                """;
        Map<String ,Object> pagrams = Map.of(
                "orderId",orderID
        );
        try(Session session = AppUntils.getSession()){
            return session.executeWrite(tx->{
                Result res = tx.run(querry,pagrams);
                return res.single().get("total").asDouble();
            });

        }
    }


    public static void main(String[] args) {
    SupplierDAO dao = new SupplierDAO();
//    dao.listProductsBySupplier("Genen Shouyu", 1, 5).forEach(System.out::println);

//        Supplier supplier = new Supplier();
//        supplier.setSupplierId("S006");      // 👈 bắt buộc
//        supplier.setCompanyName("Hehe");
//        System.out.println(dao.updateSupplier(supplier));

        System.out.println(dao.calculateTotalOrder("S006"));


    }








}






