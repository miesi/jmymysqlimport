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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author mieslingert
 */
public class DataBase {
    
    private static final String jdbcUrl;
    private static final String dbUser;
    private static final String dbPass;
    private static final String jdbcClass;
    private static final HikariDataSource ds;

    static {
        // Do your thing during webapp's startup.
        Properties zcProperties = new Properties();
        String[] placesToTry = {"/home/mieslingert/.fhvdns.properties"};
        for (String f : placesToTry) {
            try {
                BufferedInputStream stream = new BufferedInputStream(new FileInputStream(f));
                zcProperties.load(stream);
                stream.close();
                break;  // Abbrechen des ladens nach dem ersten erfolgreichen Dateiöffnens
            } catch (Exception ex) {
            }
        }

        jdbcUrl = zcProperties.getProperty("jdbcUrl", "jdbc:mysql://localhost:3306/pdns_test_db");
        dbUser = zcProperties.getProperty("dbUser", "root");
        dbPass = zcProperties.getProperty("dbPass", "");
        jdbcClass = zcProperties.getProperty("jdbcClass", "com.mysql.jdbc.Driver");

        // Setup HikariCP
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPass);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useUnicode", "yes");
        config.addDataSourceProperty("characterEncoding", "utf8");
        config.setDriverClassName(jdbcClass);

        ds = new HikariDataSource(config);

        ds.setMaximumPoolSize(500);
    }

    public static HikariDataSource getDs() {
        return ds;
    }

}
