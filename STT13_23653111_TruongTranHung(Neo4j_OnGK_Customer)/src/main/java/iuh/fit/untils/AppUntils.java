/*
 * @ (#) AppUntils.java          1.0        3/30/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */

package iuh.fit.untils;

import org.neo4j.driver.*;

/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 3/30/2026
 * @version:    1.0
 */
public class AppUntils {
    private static final String DB_NAME ="hung23653111";
    private static final String USER_NAME ="neo4j";
    private static final String PASSWORD ="Hung787898";
    private static final String URI ="neo4j://127.0.0.1:7687";
    //Cần biến lưu kết nối
    private static Driver driver;
    //Nếu CHƯA có → tạo
    //Nếu CÓ rồi → dùng lại
    private static Driver getDriver(){
        if(driver == null)
            driver = GraphDatabase.driver(URI, AuthTokens.basic(USER_NAME, PASSWORD));
        return driver;
    }
    //Lấy driver trước
    //Tạo session từ driver
    //Chỉ định database
    public static Session getSession (){
        return getDriver().session(SessionConfig.forDatabase(DB_NAME));
    }

}
