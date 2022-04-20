# 工程简介
说明：此项目纯属练手。

利用java，实现类似crontab的定时任务调度功能.

配置文件格式：
```yaml
crontab:
  crons:
    - cmd: "date;sleep 20;" # 要执行的命令
      id: 1 # 任务id
      output: /tmp/1.log # 指定任务的标准输出文件
      singleton: 1 # 是否禁止crontab并发执行。当一个任务还未执行完时，下一次调度时无需再运行
      cronExpression: "* * * * * *" # crontab 表达式
    - cmd: "date;sleep 180;date;"
      id: 2
      output: /tmp/2.log
      singleton: 1
      cronExpression: "0 */2 * * * *"
```
相比linux的crontab，这个项目能提供的额外收益：
1. 实现crontab在线管理，定时任务集中管理。
2. 可以选择禁止同一个定时任务并发执行

局限性：
1. 定时任务调度的最小时间单位是分钟。

# 项目用到的技术
1. 使用spring scheduling库，解析crontab表达式，计算任务的下一次执行时间。
2. 利用Timer定时器，每分钟做一次任务调度。每次调度时，需要做以下工作：

    （1）获取下一分钟即将执行的所有定时任务。
      
    （2）使用ScheduledExecutorService（定时任务线程池），提交一个一分钟后运行的任务。
   即每个任务会单独起一个线程。
   
    （3）在每个任务线程中，利用的是java Process类起一个单独进程，运行任务（即执行shell脚本）
# 延伸阅读

