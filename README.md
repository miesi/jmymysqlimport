# jmymysqlimport
My Java mysqlimport

Standard MySQL tooling seems to assume everybody has always local access
rights to the DB Server.

This is not true in my case and I need something to move subsets of
tables around.

I guess this tool has been written many times before, unfortunately I'm unable
to find those.

Edit your .pdnsdb.properties file to contain something like this

UnixDomain socket
```
user:john
password:doe
jdbcUrl:jdbc:mysql:///databasename
socketFactory:org.newsclub.net.mysql.AFUNIXDatabaseSocketFactory
junixsocket.file:/srv/mysql/databasename/run/mysql-database.socket
```

TCP/IP Version
```
user:john
password:doe
jdbcUrl:jdbc:mysql://server.example.com:3306/example_database
```

Create a tab separated data file
```
mysql -h my-db-server.example.com -p -BNrqe "select domain_id,name,type,content,ttl,prio,change_date,disabled,ordername,auth,rev_name from records where domain_id in (1,2,3,4)" my_pdns_database >/home/johndoe/file-with-data
```

Insert the data into another place
```
java -jar jmymysqlimport-1.0-SNAPSHOT-jar-with-dependencies.jar "insert into records(domain_id,name,type,content,ttl,prio,change_date,disabled,ordername,auth,rev_name) values (?,?,?,?,?,?,?,?,?,?,?)" /home/johndoe/file-with-data
```