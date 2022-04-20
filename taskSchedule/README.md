# 工程简介
利用java，实现类似crontab的定时任务调度功能.

配置文件格式：
```aidl
crontab:
  crons:
    - cmd: "date;sleep 20;"
      id: 1
      output: /tmp/1.log
      singleton: 1
      cronExpression: "* * * * * *"
    - cmd: "date;sleep 180;date;"
      id: 2
      output: /tmp/2.log
      singleton: 1
      cronExpression: "0 */2 * * * *"
```


说明：此项目纯属练手。
# 延伸阅读

