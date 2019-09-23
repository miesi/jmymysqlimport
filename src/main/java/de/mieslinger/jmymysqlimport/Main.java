package de.mieslinger.jmymysqlimport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import org.apache.commons.lang.StringUtils;

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
/**
 *
 * @author mieslingert
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // DB Connectivity -> from .config

        // statement to prepare -> from cmdline #1
        // file to read -> from cmdline #2
        // fields in file -> tab separated
        // file can be generated with
        // mysql -h xy -u user -p -BNrq -e "select fields from table where condition=x" database
        // TODO: db connectivity on cmdline
        // TODO: other field separators as \t
        // TODO: support enclosed fields
        // TODO: add debug support
        try {
            Connection cn = DataBase.getConnection();
            cn.setAutoCommit(false);

            PreparedStatement insZone = cn.prepareStatement(args[0]);

            // count ? in arg0
            int numFields = StringUtils.countMatches(args[0], "?");
            if (!(numFields > 0)) {
                System.out.println("No ? in insert statement. Exiting");
                System.exit(1);
            }

            BufferedReader br = new BufferedReader(new FileReader(args[1]));
            int r = 0;
            for (String line; (line = br.readLine()) != null;) {
                // split at TAB 
                String[] fields = line.split("\\t");

                for (int i = 1; i <= numFields; i++) {
                    if (fields[i - 1].equals("NULL")) {
                        // hope for the best
                        insZone.setNull(i, Types.VARCHAR);
                    } else {
                        insZone.setString(i, fields[i - 1]);
                    }
                }
                insZone.execute();

                r++;
                if (r > 1000) {
                    cn.commit();
                    r = 0;
                }
            }

            // clean up resources
            cn.commit();
            try {
                insZone.close();
            } catch (Exception e) {
            }
            try {
                cn.close();
            } catch (Exception e) {
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
