# 第十三课作业一

环境：msyql 8.0.15 macOs 16GB 4C

**按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。**

- 普通jdbc statement插入(太慢了数据量减少到10万)

  ![image-20201203104329621](https://gitee.com/Jtiag/Img/raw/master/img/image-20201203104329621.png)

  大约：1.72min

- 普通jdbc  prepareStatement批量插入（太慢了数据量减少到10万）

  **每1000条记录提交一次**

  ![image-20201203104508656](https://gitee.com/Jtiag/Img/raw/master/img/image-20201203104508656.png)

  大约：15.2s

  **每2000条记录提交一次**

  ![image-20201203104722838](https://gitee.com/Jtiag/Img/raw/master/img/image-20201203104722838.png)

  大约：14.7s

  每**10000条记录提交一次**

  ![image-20201203104847599](https://gitee.com/Jtiag/Img/raw/master/img/image-20201203104847599.png)

  大约：13.7s

  每**50000条记录提交一次**

  ![image-20201203105023670](https://gitee.com/Jtiag/Img/raw/master/img/image-20201203105023670.png)

  大约：13.7s

- 简单实现的jdbc pool statement插入

  ![image-20201203111344550](https://gitee.com/Jtiag/Img/raw/master/img/image-20201203111344550.png)

  大约：15.22min

- 简单实现的jdbc pool prepareStatement插入

  **batchSize=1000**

  ![image-20201203111936837](https://gitee.com/Jtiag/Img/raw/master/img/image-20201203111936837.png)

  大约：2.60min

  **batchSize=5000**

  ![image-20201203114843059](https://gitee.com/Jtiag/Img/raw/master/img/image-20201203114843059.png)

  大约：2.45min

  **batchSize=10000**

  ![image-20201203115210556](https://gitee.com/Jtiag/Img/raw/master/img/image-20201203115210556.png)

  大约：2.38min

  **batchSize=50000**

  大约：2.45min

- 使用hikari pool statement插入

  ![image-20201202175057572](https://gitee.com/Jtiag/Img/raw/master/img/image-20201202175057572.png)

  大约：14min

- 使用hikari pool prepareStatement插入

  **batchSize=1000**

  ![image-20201202182037977](https://gitee.com/Jtiag/Img/raw/master/img/image-20201202182037977.png)

  大约：2.6min

  **batchSize=5000**

  ![image-20201203102747968](https://gitee.com/Jtiag/Img/raw/master/img/image-20201203102747968.png)

  大约2.56min

  **batchSize=10000**

  ![image-20201203102232008](https://gitee.com/Jtiag/Img/raw/master/img/image-20201203102232008.png)

  大约：2.4min

  **batchSize=50000**

  ![image-20201203103310931](https://gitee.com/Jtiag/Img/raw/master/img/image-20201203103310931.png)

  大约：2.46min

  **总结**

  使用statement插入会涉及到每次都会去启动事物然后提交事物，和数据库的交涉比较多，每次提交都会去编译一次sql,性能会损失很多

  preStatement是预编译的，不会每次都去编译一次sql，并且在一定程度上有效的预防sql注入攻击，preStatement的性能和批量的大小有一定的关系，如批量在一定的范围增加时性能会随着增加一些

  

  # 第十四课作业

  1、（选做）配置一遍异步复制，半同步复制、组复制。 

  2、（必做）读写分离-动态切换数据源版本1.0 

  3、（必做）读写分离-数据库框架版本2.0 

  4、（选做）读写分离-数据库中间件版本3.0 

  5、（选做）配置MHA，模拟master宕机 

  6、（选做）配置MGR，模拟master宕机 

  7、（选做）配置Orchestrator，模拟master宕机，演练UI调整拓扑结构

  