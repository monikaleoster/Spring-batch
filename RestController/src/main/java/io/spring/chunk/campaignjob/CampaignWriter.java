package io.spring.chunk.campaignjob;

import io.spring.chunk.ApplicationContextProvider;
import io.spring.chunk.Line;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;

public class CampaignWriter implements ItemWriter<Line>{
/*
    DataSource dataSource;

    JdbcTemplate jdbcTemplate;*/
    @Override
    public void write(List<? extends Line> list) throws Exception {/*

        int[] types = {Types.VARCHAR, Types.VARCHAR};

        String updateSql = "UPDATE data SET status = ? WHERE name = ?";
list.forEach(item -> {
    Object[] params = {item.getName(),"Processed"};
    jdbcTemplate.update(updateSql, params, types);

});


     */  System.out.println("Campaign Writer Initialized"); }

  /*  @Override
public void beforeStep(StepExecution stepExecution) {
    this.dataSource = ApplicationContextProvider.getContext().getBean(DataSource.class);
    jdbcTemplate = new JdbcTemplate(dataSource);

    System.out.println("Campaign Writer Initialized");
}

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }*/
}
