/*
 * @ (#) AppUntils.java          1.0        4/2/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */

package iuh.fit.util;

/*
 * @description:
 * @author: Truong Tran Hung
 * @date: 4/2/2026
 * @version:    1.0
 */
import org.neo4j.driver.*;
public class AppUntils {
    private static final String DB_NAME = "hung23653111";
    private static final String USERNAME = "neo4j";
    private static final String PASSWORD = "Hung787898";
    private static final String URI = "neo4j://127.0.0.1:7687";

    private static Driver driver;

    public static Driver getDriver() {
        if (driver==null) {
            driver = GraphDatabase.driver(URI, AuthTokens.basic(USERNAME,PASSWORD));
        }
        return driver;
    }
    public static Session getSession() {
        return getDriver().session(SessionConfig.forDatabase(DB_NAME));
    }
}
