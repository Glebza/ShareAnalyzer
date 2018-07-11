package com.konganalitics.services.model;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class AnaliticsDataProviderTest {


    @Autowired
    private AnaliticsDataProvider analiticsDataProvider;

    @Autowired
    private DataSource dataSource;

    private EmbeddedDatabase db;

    @Before
    public void init(){

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db/createShareHistoryTable.sql")
                .addScript("db/createShareTable.sql")
                .build();

    }

    @Test
    public void getSharesWithMaxGrowth() {


    }
}