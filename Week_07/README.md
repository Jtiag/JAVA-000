# 第十三课作业

环境：msyql 8.0.15 macos 16GB 4C

**按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率。**

- 普通jdbc statement插入(太慢了数据量减少到10万)

  

- 普通jdbc  prepareStatement批量插入（太慢了数据量减少到10万）

  每1000条记录提交一次

  

  每2000条记录提交一次

  

- 简单实现的jdbc pool statement插入

  

- 简单实现的jdbc pool prepareStatement插入

  

- 使用hikari pool statement插入

  ![image-20201202175057572](https://gitee.com/Jtiag/Img/raw/master/img/image-20201202175057572.png)

  大约需要14min

- 使用hikari pool prepareStatement插入

  ![image-20201202182037977](https://gitee.com/Jtiag/Img/raw/master/img/image-20201202182037977.png)

  快多了，大约：2.6min