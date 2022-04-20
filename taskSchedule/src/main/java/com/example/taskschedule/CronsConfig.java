package com.example.taskschedule;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

// 必须要@Data注解，才能自动完成crons赋值.
@Configuration
@ConfigurationProperties(prefix = "crontab")
@Data
public class CronsConfig {

    // 加载yaml配置文件中的数组配置
    private List<CronEntity> crons;

    /**
     * 奇怪的是，方法名为getAll()，就报错了。
     */
    public void get()
    {
        Iterator iterator  = crons.iterator();
        while (iterator.hasNext()) {
            CronEntity cron = (CronEntity) iterator.next();
            System.out.println(cron.getNextTime());
        }
    }
}
