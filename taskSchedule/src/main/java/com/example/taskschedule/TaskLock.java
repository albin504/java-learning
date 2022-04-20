package com.example.taskschedule;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TaskLock {
    public HashMap<Integer, Boolean> taskLock = new HashMap<>();

    public synchronized boolean getLock(int taskId) {
        if (taskLock.containsKey(taskId)) {
//            Logger.info("task " + taskId + " get lock false");
            return false;
        } else {
            taskLock.put(taskId, true);
//            Logger.info("task " + taskId + " get lock true");
            return true;
        }
    }

    public synchronized void releaseLock(int taskId) {
//        Logger.info("release task " + taskId + " lock");
        taskLock.remove(taskId);
    }
}
