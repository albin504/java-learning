package com.example.taskschedule;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskScheduleApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(TaskScheduleApplication.class, args);
    }

    private Schedule schedule;

    // 通过构造函数注入java bean
    public TaskScheduleApplication( Schedule schedule)
    {
        this.schedule = schedule;
    }

    @Override
    public void run(String... args) throws Exception {
        schedule.run();
    }
}
