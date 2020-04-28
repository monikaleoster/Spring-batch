package io.spring.chunk.campaignjob;

import io.spring.chunk.ApplicationContextProvider;
import io.spring.chunk.Line;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class CampaignJDBCReader {



    public static ItemReader<Line> getCampaignReader(DataSource dataSource){
        return  createCampaignJDBCProvider( dataSource);
    }

    public static ItemReader<Line>  createCampaignJDBCProvider(DataSource dataSource){

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("Campaign JDBC Reader initialized");

        JdbcPagingItemReader<Line> databaseReader = new JdbcPagingItemReader<>();

        databaseReader.setDataSource(dataSource);
        databaseReader.setPageSize(1);

            Map<String, Order> sortConfiguration = new HashMap<>();
            sortConfiguration.put("name", Order.ASCENDING);


              MySqlPagingQueryProvider mysqlQueryProvider=   new MySqlPagingQueryProvider();
        mysqlQueryProvider.setSelectClause("SELECT *");
        mysqlQueryProvider.setFromClause("FROM data");
        mysqlQueryProvider.setSortKeys(sortConfiguration);
        databaseReader.setQueryProvider(mysqlQueryProvider);

        databaseReader.setRowMapper(new BeanPropertyRowMapper<>(Line.class));

        return databaseReader;
    }

}
