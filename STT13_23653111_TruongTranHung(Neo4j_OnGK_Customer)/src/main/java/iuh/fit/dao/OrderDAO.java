/*
 * @ (#) OrderDAO.java          1.0        3/31/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */

package iuh.fit.dao;
import iuh.fit.untils.AppUntils;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import java.util.Map;
/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 3/31/2026
 * @version:    1.0
 */


public class OrderDAO {
    public static double calculateTotalOrder(String orderId) {
        String query = """
                MATCH (o:Order)-[r:ORDERS]-(p:Product)\s
                WHERE o.order_id = $orderId
                RETURN sum(r.quantity*r.unit_price*(1-r.discount)) as total
                """;
        Map<String, Object> params = Map.of("orderId",orderId);
        try(Session session = AppUntils.getSession()) {
            return session.executeRead(tx -> {
                Result result = tx.run(query,params);
                return result.single().get("total").asDouble();
            });
        }
    }

    public static void main(String[] args) {
        double res = calculateTotalOrder("O008");
        System.out.println(res);
    }
}