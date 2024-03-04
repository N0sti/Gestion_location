package com.epf.rentmanager.configuration;


import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@ComponentScan({ "com.epf.rentmanager.service", "com.epf.rentmanager.dao",
        "com.epf.rentmanager.persistence" }) // packages dans lesquels chercher les beans

public class AppConfiguration {
}
