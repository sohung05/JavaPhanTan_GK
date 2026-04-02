/*
 * @ (#) ProductDAO.java          1.0        3/31/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */

package iuh.fit.dao;
import iuh.fit.entity.Product;
import iuh.fit.untils.AppUntils;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import java.util.List;
import java.util.Map;
/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 3/31/2026
 * @version:    1.0
 */


public class ProductDAO {

//    CREATE INDEX companyNameIdex FOR (s:Supplier) ON (s.company_name)

    public static List<Product> listProductsBySupplier(String companyName, int page, int size) {
        if (companyName.trim() == "" || companyName ==null) {
            System.out.println("companyName: Không được null hoặc rỗng");
            throw new RuntimeException();
        }else if (page <1) {
            System.out.println("page: Phải là số nguyên dương (page ≥ 1)");
            throw new RuntimeException();
        } else if (size <1) {
            System.out.println("size: Phải là số nguyên dương (size ≥ 1)");
            throw new RuntimeException();
        }
        int skip = (page -1) *size;

        String cypher = """
                MATCH (p:Product)-[r:SUPPLIES]-(s:Supplier) 
                WHERE s.company_name= $companyName 
                RETURN p SKIP $skip LIMIT $limit;
                """;

        Map<String,Object> params = Map.of("companyName",companyName,"skip",skip,"limit",size);
        try (Session session = AppUntils.getSession()) {
            return session.executeRead(tx -> {
                Result result = tx.run(cypher,params);
                List<Record> products = result.list();
                return products.stream()
                        .map(record -> {
                            Node node = record.get("p").asNode();
                            return Product.builder()
                                    .producId(node.get("product_id").asString())
                                    .productName(node.get("product_name").asString())
                                    .unit(node.get("unit").asString())
                                    .unitPrice(node.get("unit_price").asDouble())
                                    .unitsInStock(node.get("units_in_stock").asInt())
                                    .build();
                        }).toList();

            });
        }
    }

    public static void main(String[] args) {
        List<Product> res =  listProductsBySupplier("Tokyo Traders",1,3);
        res.forEach(System.out::println);
    }
}