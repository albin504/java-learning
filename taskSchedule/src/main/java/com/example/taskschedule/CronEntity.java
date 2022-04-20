package com.example.taskschedule;

import lombok.Data;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.util.Date;

@Data
public class CronEntity {
    String cronExpression;
    String cmd;
    String output;
    String singleton;
    int id;

    public Date getNextTime() {
        if (CronSequenceGenerator.isValidExpression(this.cronExpression)) {
            CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(this.cronExpression);
            Date currentTime = new Date();
            Date date = cronSequenceGenerator.next(currentTime);
            return date;
        }
        return null;
    }
}
