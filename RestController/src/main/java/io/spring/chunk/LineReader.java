package io.spring.chunk;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.Random;

public class LineReader implements ItemReader<Line>, StepExecutionListener {

    private FileUtils fu;



    @Override
    public void beforeStep(StepExecution stepExecution) {
        fu = new FileUtils("test2.csv");
        System.out.println("Line Reader initialized");
    }

    @Override
    public Line read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Line line = fu.readLine();
        if (line != null) System.out.println("Read line: " + line.toString());
        return line;
    }
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        fu.closeReader();
        System.out.println("Line Reader ended.");
        return ExitStatus.COMPLETED;
    }
}
