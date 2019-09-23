/*
 * The MIT License
 *
 * Copyright 2019 mieslingert.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.mieslinger.jmymysqlimport;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import org.newsclub.net.mysql.AFUNIXDatabaseSocketFactory;

/**
 *
 * @author mieslingert
 */
public class DataBase {

    private static final String jdbcClass;
    private static final String jdbcUrl;
    private static Connection conn = null;

    static {

        Properties zcProperties = new Properties();
        String[] placesToTry = {"/home/mieslingert/.pdnsdb.properties"};
        for (String f : placesToTry) {
            try {
                BufferedInputStream stream = new BufferedInputStream(new FileInputStream(f));
                zcProperties.load(stream);
                stream.close();
                break;
            } catch (Exception ex) {
            }
        }
        jdbcClass = zcProperties.getProperty("jdbcClass", "com.mysql.jdbc.Driver");
        jdbcUrl = zcProperties.getProperty("jdbcUrl", "jdbc:mysql://");

        zcProperties.setProperty("cachePrepStmts", "true");
        zcProperties.setProperty("prepStmtCacheSize", "250");
        zcProperties.setProperty("prepStmtCacheSqlLimit", "2048");
        zcProperties.setProperty("useServerPrepStmts", "true");
        zcProperties.setProperty("useUnicode", "yes");
        zcProperties.setProperty("characterEncoding", "utf8");

        try {
            Class.forName(jdbcClass).newInstance();
            conn = DriverManager.getConnection(jdbcUrl, zcProperties);
        } catch (Exception e) {
            System.err.println("initialization failed, exiting.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Connection getConnection() {
        return conn;
    }

}
