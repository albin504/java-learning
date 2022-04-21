package com.example.taskschedule;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class Schedule {
    CronsConfig crons;
    TaskLock taskLock;

    public Schedule(CronsConfig crons, TaskLock taskLock) {
        this.crons = crons;
        this.taskLock = taskLock;
    }

    public List<CronEntity> findDueCrons() {
        List<CronEntity> dueList = new ArrayList<>();
        Iterator iterator = crons.getCrons().iterator();
        while (iterator.hasNext()) {
            CronEntity cron = (CronEntity) iterator.next();
            Date date = cron.getNextTime();
            long time = date.getTime();
            if (time - System.currentTimeMillis() < 60000) {
                dueList.add(cron);
            }
        }
        return dueList;
    }

    public void run() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

        // 从当前时间的下一分钟0秒开始调度
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), 0);
        calendar.add(Calendar.MINUTE, 1);
        Date startTime = calendar.getTime();

        // 从下一分钟0秒开始，以固定频率，每分钟调度一次
        new Timer("schedule").scheduleAtFixedRate(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                Logger.info("schedule:" + timestamp);
                List<CronEntity> dueCrons = findDueCrons();
                Logger.info("due jobs:" + Arrays.toString(dueCrons.stream().map(b -> b.id).toArray()));
                for (CronEntity cron :
                        dueCrons) {
                    TaskRunner runner = new TaskRunner(cron, taskLock);
                    executorService.schedule(runner, 60, TimeUnit.SECONDS);
                }

            }
        }, startTime, 60000);
    }
}
