package com.start.config;

import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Named;
import javax.sql.DataSource;

@DataSourceDefinition(
        transactional = true,
        url = "jdbc:mariadb://127.0.0.1:3306/tevis_crm",
        className = "org.mariadb.jdbc.MySQLDataSource",
        user = "root",
        password = "password",
        name = "java:app/jdbc/tevis_crm",
        initialPoolSize = 1,
        maxPoolSize = 3
)
@Named
@Singleton
@Startup
public class DatabaseSetup {

    @Resource(lookup = "java:app/jdbc/tevis_crm")
    private DataSource ds;
}