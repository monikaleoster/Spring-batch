package io.spring.chunk.campaignjob;

import io.spring.chunk.ApplicationContextProvider;
import io.spring.chunk.Line;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class CampaignReader implements ItemReader<Line>, StepExecutionListener {
    DataSource dataSource;

    JdbcTemplate jdbcTemplate;
    @Override
    public Line read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        String sql = "SELECT * FROM data where status= ? limit 1";


       return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                new Line(rs.getString("name"), rs.getString("age"),rs.getString("dob"),rs.getString("status")), "NEW");

    }


    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.dataSource = ApplicationContextProvider.getContext().getBean(DataSource.class);
        jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("Campaign Reader initialized");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("Campaign Reader completed");

        return ExitStatus.COMPLETED;
    }
}
