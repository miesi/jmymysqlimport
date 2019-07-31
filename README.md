# jmymysqlimport
My Java mysqlimport

Standard MySQL tooling seems to assume everybody has always local access
rights to the DB Server.

This is not true in my case and I need something to move subsets of
tables around.

I guess this tool has been written many times before, unfortunately I'm unable
to find those.

Edit your .pdnsdb.properties file to contain something like this
```
dbUser:user
dbPass:changeMe
jdbcUrl:jdbc:mysql://db-server.example.com:3306/db?useServerPrepStmts=true
jdbcClass:com.mysql.jdbc.Driver

```

java -jar jmymysqlimport-1.0-SNAPSHOT-jar-with-dependencies.jar "insert into records(domain_id,name,type,content,ttl,prio,change_date,disabled,ordername,auth,rev_name) values (?,?,?,?,?,?,?,?,?,?,?)" /home/johndoe/file-with data