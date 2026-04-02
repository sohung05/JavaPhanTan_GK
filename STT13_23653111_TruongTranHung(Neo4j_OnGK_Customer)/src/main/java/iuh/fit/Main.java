/*
 * @ (#) Main.java          1.0        3/31/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */

package iuh.fit;
import iuh.fit.dao.OrderDAO;
import iuh.fit.dao.ProductDAO;
import iuh.fit.dao.SupplierDAO;
import iuh.fit.entity.Product;
import iuh.fit.entity.Supplier;


import java.util.List;
/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 3/31/2026
 * @version:    1.0
 */
public class Main {
    public static void main(String[] args) {
        OrderDAO orderDAO = new OrderDAO();
        ProductDAO productDAO = new ProductDAO();
        SupplierDAO supplierDAO = new SupplierDAO();

        List<Product> products =  productDAO.listProductsBySupplier("Tokyo Traders",1,3);
        products.forEach(System.out::println);

        Supplier supplier = new Supplier("S006","Mayumi Ohno","VietNam","LeNguyenQuocHuy");
        System.out.println(SupplierDAO.updateSupplier(supplier));

        double total = orderDAO.calculateTotalOrder("O008");
        System.out.println(total);
    }
}
