**项目说明** 

该版本要求JDK1.8

<br> 

**项目结构** 
```
lottery-security
├─doc  项目SQL语句
├─common  公共
├─config  配置文件
├─modules 模块
│  ├─job 定时任务
│  └─sys 系统管理(核心)
├─lotteryApplication.java 项目启动类
│ 
├─resources 
│  ├─mapper     SQL文件
│  ├─template   代码生成器模板（可增加或修改相应模板）
│  ├─application.yml        全局配置文件
│  ├─application-dev.yml    开发环境配置文件
│  └─application-test.yml   测试环境配置文件
│  └─application-pro.yml    正式环境配置文件
│  └─generator.properties   代码生成器配置文件
│ 
├─webapp 
│  ├─statics   静态资源
│  ├─swagger   swagger ui
│  └─WEB-INF/views   系统页面
│     ├─modules      模块页面
│     ├─index.html   AdminLTE主题风格（默认主题）
│     └─index1.html  Layui主题风格
│

<br>

 **分布式部署**
- 分布式部署，需要安装redis，并配置application.yml里的redis信息
- 需要配置【lottery.redis.open=true】，表示开启redis缓存
- 需要配置【lottery.shiro.redis=true】，表示把shiro session存到redis里