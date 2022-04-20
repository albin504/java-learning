package com.example.taskschedule;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import lombok.extern.java.Log;

public class TaskRunner implements Runnable {
    private CronEntity cronEntity;
    TaskLock taskLock;

    public TaskRunner(CronEntity cronEntity, TaskLock taskLock) {
        this.cronEntity = cronEntity;
        this.taskLock = taskLock;
    }

    @SneakyThrows
    @Override
    public void run() {
        boolean singleton = Integer.parseInt(cronEntity.singleton) == 1 ? true : false;

        Map<String, Object> taskInfo = new HashMap();
        taskInfo.put("id", cronEntity.id);
        taskInfo.put("threadId", Thread.currentThread().getId());
        taskInfo.put("cmd", cronEntity.cmd);

        if ((singleton && taskLock.getLock(cronEntity.id)) || (!singleton)) {
            Logger.warn("----begin run: " + cronEntity.cmd + "   " + taskInfo.toString());
            Process p = runScript(cronEntity.cmd);
            if (p.waitFor(3600, TimeUnit.SECONDS)) {
                int returnVal = p.exitValue();
                if (returnVal != 0) {
                    Logger.error(cronEntity.cmd + " error" + "   " + taskInfo.toString());
                }
                InputStream inputStream = p.getInputStream();
                BufferedReader inputStream1 = new BufferedReader(new InputStreamReader(inputStream));
                String s;
                while ((s = inputStream1.readLine()) != null) {
                    Logger.warn("output:" + s + "   " + taskInfo.toString());
                }
                taskLock.releaseLock(cronEntity.id);
                Logger.warn("----end run:" + cronEntity.cmd + "   " + taskInfo.toString());
            }
        } else {
            Logger.error("该任务正在运行中，无需重复运行" + "   " + taskInfo.toString());
        }
    }

    public Process runScript(String command) throws IOException {
//        Process p = Runtime.getRuntime().exec(new String[]{"bash","-c", command});
//        return p;
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
        pb.redirectOutput(new File(cronEntity.output));
        pb.redirectError(new File(cronEntity.output));
        Process p = pb.start(); // may throw IOException
        return p;
    }
}
