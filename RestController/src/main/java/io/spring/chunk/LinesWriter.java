package io.spring.chunk;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

public class LinesWriter implements ItemWriter<Line>, StepExecutionListener {

    DataSource dataSource;

    JdbcTemplate jdbcTemplate;


    @Override
    public void write(List<? extends Line> list) throws Exception {
        System.out.println("Writing Lines");
        for (Line s : list) insert(s);
    }

    private void insert(Line line) {
        String sql = "INSERT INTO Data " +
                "(name, age, dob, status) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{line.getName(),
                line.getAge(), line.getDob(),line.getStatus()
        });
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.dataSource = ApplicationContextProvider.getContext().getBean(DataSource.class);
        jdbcTemplate = new JdbcTemplate(dataSource);

        System.out.println("Writer Initialized");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("Writer completed");
        return ExitStatus.COMPLETED;

    }
}