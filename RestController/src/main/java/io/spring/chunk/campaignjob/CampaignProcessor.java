package io.spring.chunk.campaignjob;

import io.spring.chunk.Line;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;

public class CampaignProcessor implements ItemProcessor<Line, Line>, StepExecutionListener {
    @Override
    public Line process(Line line) throws Exception {
        System.out.println(line.getName()+"processed");
        return line;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Campaign Processor initialized");

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("Campaign Processor ended.");
        return ExitStatus.COMPLETED;
    }
}
