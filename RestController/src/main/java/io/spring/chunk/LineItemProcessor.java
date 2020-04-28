package io.spring.chunk;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;

public class LineItemProcessor implements ItemProcessor<Line, Line>, StepExecutionListener {
    @Override
    public Line process(Line s) throws Exception {
        return s;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Line Processor initialized.");

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("Line Processor ended.");
        return ExitStatus.COMPLETED;
    }
}
