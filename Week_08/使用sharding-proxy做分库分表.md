# 需要维护一个ShardingSphere proxy客户端

## shardingsphere-proxy安装

- [下载](https://www.apache.org/dyn/closer.cgi/shardingsphere/5.0.0-alpha/apache-shardingsphere-5.0.0-alpha-shardingsphere-proxy-bin.tar.gz)

- 使用ShardingSphere proxy操作操作步骤参考 [ **shardingsphere-proxy-example**](https://github.com/apache/shardingsphere/tree/master/examples/shardingsphere-proxy-example)

### ${shardingsphere_home}/conf/config-sharding.yaml 

分片规则配置

```properties
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

######################################################################################################
#
# Here you can configure the rules for the proxy.
# This example is configuration of sharding rule.
#
######################################################################################################

schemaName: sharding_db # 逻辑数据源名称

dataSources: # 数据源配置，可配置多个 <data-source-name>
  ds_0: # 与 ShardingSphere-JDBC 配置不同，无需配置数据库连接池
    url: jdbc:mysql://127.0.0.1:3333/demo_ds_0?serverTimezone=UTC&useSSL=false  #数据库 URL 连接
    username: root
    password: 123456
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 10
    minPoolSize: 1
    maintenanceIntervalMilliseconds: 30000
  ds_1:
    url: jdbc:mysql://127.0.0.1:3334/demo_ds_1?serverTimezone=UTC&useSSL=false
    username: root
    password: 123456
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 10
    minPoolSize: 1
    maintenanceIntervalMilliseconds: 30000

rules: # 与 ShardingSphere-JDBC 配置一致
  - !SHARDING # 规则别名，`-` 表示可配置多个规则
    tables: # 数据分片规则配置
      t_order: # 逻辑表名称
        actualDataNodes: ds_${0..1}.t_order_info_${0..16} #由数据源名 + 表名组成，以小数点分隔。多个表以逗号分隔，支持行表达式。缺省表示使用已知数据源与逻辑表名称生成数据节点，用于广播表（即每个库中都需要一个同样的表用于关联查询，多为字典表）或只分库不分表且所有库的表结构完全一致的情况
        #        databaseStrategy: # 分库策略，缺省表示使用默认分库策略，以下的分片策略只能选其一
        #          standard: # 用于单分片键的标准分片场景
        #            shardingColumn: user_id # 分片列名称
        #            shardingAlgorithmName: database_inline # 分片算法名称
        #          complex: # 用于多分片键的复合分片场景
        #            shardingColumns: #分片列名称，多个列以逗号分隔
        #            shardingAlgorithmName: # 分片算法名称
        #          hint: # Hint 分片策略
        #            shardingAlgorithmName: # 分片算法名称
        #          none: # 不分片
        tableStrategy:
          standard:
            shardingColumn: id
            shardingAlgorithmName: t_order_info_inline
    #      keyGenerateStrategy: # 分布式序列策略
    #        column: id  # 自增列名称，缺省表示不使用自增主键生成器
    #        keyGeneratorName: snowflake # 分布式序列算法名称
    bindingTables: # 绑定表规则列表
      - t_order_info
    #  broadcastTables: # 广播表规则列表
    #    - t_address
    defaultDatabaseStrategy:
      standard:
        shardingColumn: user_id
        shardingAlgorithmName: database_inline
    defaultTableStrategy:
      none:
    # 分片算法配置
    shardingAlgorithms:
      database_inline:
        type: INLINE
        props:
          algorithm-expression: ds_${user_id % 2}
      t_order_info_inline:
        type: INLINE
        props:
          algorithm-expression: t_order_info${id % 2}
    #    t_order_item_inline:
    #      type: INLINE
    #      props:
    #        algorithm-expression: t_order_item_${order_id % 2}
    # 分布式序列算法配置
#    keyGenerators:
#      snowflake:
#        type: SNOWFLAKE
#        props:
#          worker-id: 123

```

### {shardingsphere_home}/conf/server.yaml	(服务相关配置)

```properties
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

######################################################################################################
#
# If you want to configure governance, authorization and proxy properties, please refer to this file.
#
######################################################################################################
#
#governance:
#  name: governance_ds
#  registryCenter:
#    type: ZooKeeper
#    serverLists: localhost:2181
#    props:
#      retryIntervalMilliseconds: 500
#      timeToLiveSeconds: 60
#      maxRetries: 3
#      operationTimeoutMilliseconds: 500
#  overwrite: false

authentication:
  users:
    root:
      password: root
    sharding:
      password: sharding
      authorizedSchemas: sharding_db # 该用户授权可访问的数据库，多个用逗号分隔。缺省将拥有 root 权限，可访问全部数据库。

props:
  max-connections-size-per-query: 1
  acceptor-size: 16  # The default value is available processors count * 2.
  executor-size: 16  # Infinite by default.
  proxy-frontend-flush-threshold: 128  # The default value is 128.
    # LOCAL: Proxy will run with LOCAL transaction.
    # XA: Proxy will run with XA transaction.
    # BASE: Proxy will run with B.A.S.E transaction.
  proxy-transaction-type: LOCAL
  proxy-opentracing-enabled: false
  proxy-hint-enabled: false
  query-with-cipher-column: true
  sql-show: false
  check-table-metadata-enabled: false


```

需要先创建好对应的数据库（实际拆分的库）**为什么不支持启动ShardingSphere-Proxy时启动创建实际连接的数据库，创建对应的表却可以?**

```sql
create database demo_ds_0;
create database demo_ds_1;

CREATE TABLE `t_order_info`
(
    `id`                   bigint(20)     NOT NULL AUTO_INCREMENT COMMENT '自增id',
  	`order_id`						 bigint(20)     NOT NULL COMMENT '订单编号',
    `total_amount`         decimal(10, 3) NOT NULL COMMENT '订单金额',
    `user_id`              bigint(20)     NOT NULL COMMENT '用户id',
    `order_status`         TINYINT        NOT NULL COMMENT '订单状态',
    `payment_way`          varchar(12)    NOT NULL COMMENT '支付方式',
    `out_trade_no`         varchar(20)    NOT NULL COMMENT '支付流水号',
    `create_time`          timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
    `pay_time`             timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '付款时间',
    `delivery_time`        timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '发货时间',
    `deal_completion_time` timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '交易完成时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='订单表';
```



## 启动

```shell
# bin/start.sh 3308
bin/start.sh
```

```properties
Starting the ShardingSphere-Proxy ...
The classpath is .:/Users/jasper/data/apache-shardingsphere-5.0.0-alpha-shardingsphere-proxy-bin/lib/*:/Users/jasper/data/apache-shardingsphere-5.0.0-alpha-shardingsphere-proxy-bin/ext-lib/*
Please check the STDOUT file: /Users/jasper/data/apache-shardingsphere-5.0.0-alpha-shardingsphere-proxy-bin/logs/stdout.log
	at org.apache.shardingsphere.proxy.Bootstrap.main(Bootstrap.java:47)
[INFO ] 21:54:51.090 [main] ShardingSphere-metadata - Loading 0 tables' meta data for unconfigured tables.
[INFO ] 21:54:51.098 [main] ShardingSphere-metadata - Loading 0 tables' meta data for unconfigured tables.
[INFO ] 21:54:51.106 [main] ShardingSphere-metadata - Loading 0 tables' meta data for unconfigured tables.
[INFO ] 21:54:51.108 [main] ShardingSphere-metadata - Loading 0 tables' meta data for unconfigured tables.
[INFO ] 21:54:51.116 [main] o.a.s.i.c.s.SchemaContextsBuilder - Load meta data for schema sharding_db finished, cost 86 milliseconds.
Thanks for using Atomikos! Evaluate http://www.atomikos.com/Main/ExtremeTransactions for advanced features and professional support
or register at http://www.atomikos.com/Main/RegisterYourDownload to disable this message and receive FREE tips & advice
[INFO ] 21:54:51.519 [main] o.a.s.p.i.i.AbstractBootstrapInitializer - Database name is `MySQL`, version is `5.7.32`
[INFO ] 21:54:51.832 [main] o.a.s.p.frontend.ShardingSphereProxy - ShardingSphere-Proxy start success.
```

连接shardingShpere

```
mysql -uroot -P3308 -h127.0.0.1 -p
```

以user_id分库，以id分表,查询时一定要带上user_id+id不然会扫描描所有库所有表

# 按照id分表插入不生效的原因

id 为自增主建，插入时并没有带上id，故在每张表中都会插入一条数据

可以正常插入数据

```properties
[INFO ] 23:19:00.629 [ShardingSphere-Command-13] ShardingSphere-SQL - Logic SQL: insert into t_order_info(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,delivery_time,    deal_completion_time) values(0,0,0,'支付宝','0','2020-12-13 23:19:00.08','2020-12-13 23:19:00.08','2020-12-13 23:19:00.08','2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.630 [ShardingSphere-Command-13] ShardingSphere-SQL - SQLStatement: MySQLInsertStatement(setAssignment=Optional.empty, onDuplicateKeyColumns=Optional.empty)
 [INFO ] 23:19:00.630 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_0(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,      delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.630 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_1(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,      delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.630 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_2(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,      delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.630 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_3(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,      delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.630 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_4(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,      delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.630 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_5(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,      delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.630 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_6(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,      delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.630 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_7(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,      delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.631 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_8(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,      delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.631 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_9(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,      delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.631 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_10(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,     delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.631 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_11(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,     delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.631 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_12(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,     delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.631 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_13(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,     delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.631 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_14(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,     delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.631 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_15(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,     delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
 [INFO ] 23:19:00.631 [ShardingSphere-Command-13] ShardingSphere-SQL - Actual SQL: ds_0 ::: insert into t_order_info_16(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,     delivery_time,deal_completion_time) values(0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
```

# 插入时增加id插入报错

```sql
insert into t_order_info(id,total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,     delivery_time,deal_completion_time) values(1,0, 0, 0, '支付宝', '0', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08', '2020-12-13 23:19:00.08')
```

报错：

```properties
mysql> insert into t_order_info(id,total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,delivery_time,deal_completion_time) values(1,100.1,1,0,'支付宝','112',now(),now(),now(),now());
ERROR 1064 (42000): You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near '.shardingsphere.sharding.rewrite.token.pojo.TableToken@3a141ea8(id,total_amount,' at line 1
```

![image-20201214155831153](https://gitee.com/Jtiag/Img/raw/master/img/image-20201214155831153.png)

以为是不支持主键插入修改了表结构

![image-20201214155904907](https://gitee.com/Jtiag/Img/raw/master/img/image-20201214155904907.png)

插入语句：

```sql
insert into t_order_info(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,delivery_time,deal_completion_time,order_id) values(15, 0, 0, '支付宝', '15', '2020-12-13 23:19:01.847', '2020-12-13 23:19:01.847', '2020-12-13 23:19:01.847', '2020-12-13 23:19:01.847',1);
```

仍报错：

```properties
mysql> insert into t_order_info(total_amount,user_id,order_status,payment_way,out_trade_no,create_time,pay_time,delivery_time,deal_completion_time,order_id) values(15, 0, 0, '支付宝', '15', '2020-12-13 23:19:01.847', '2020-12-13 23:19:01.847', '2020-12-13 23:19:01.847', '2020-12-13 23:19:01.847',1);
ERROR 1064 (42000): You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near '.shardingsphere.sharding.rewrite.token.pojo.TableToken@7fc1bef2(total_amount,use' at line 1
```

大概浏览了一下源码，应该是sharding-proxy中StatementProxy代理那里有问题导致执行失败

# 操作记录

```shell
# 连接shardingsphere-proxy的逻辑库表
apache-shardingsphere-5.0.0-alpha-shardingsphere-proxy-bin mysql -uroot -P3308 -h127.0.0.1 -Dsharding_db -p112358
Warning: Using a password on the command line interface can be insecure.
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 4
Server version: 5.7.32-ShardingSphere-Proxy 5.0.0-RC1 MySQL Community Server (GPL)

Copyright (c) 2000, 2014, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> show databases;
+-------------+
| Database    |
+-------------+
| sharding_db |
+-------------+
1 row in set (0.01 sec)

mysql> use sharding_db;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A

Database changed
mysql> show tables;
+---------------------+
| Tables_in_demo_ds_0 |
+---------------------+
| t_order_info        |
+---------------------+
1 row in set (0.02 sec)

mysql> truncate table t_order_info;
Query OK, 0 rows affected (0.33 sec)
```

truncate table t_order_info 逻辑表对应的真是sql

```properties
[INFO ] 12:06:35.083 [ShardingSphere-Command-1] ShardingSphere-SQL - Logic SQL: truncate table t_order_info
[INFO ] 12:06:35.083 [ShardingSphere-Command-1] ShardingSphere-SQL - SQLStatement: MySQLTruncateStatement()
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_0
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_1
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_2
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_3
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_4
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_5
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_6
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_7
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_8
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_9
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_10
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_11
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_12
[INFO ] 12:06:35.084 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_13
[INFO ] 12:06:35.085 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_14
[INFO ] 12:06:35.085 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_15
[INFO ] 12:06:35.085 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_0 ::: truncate table t_order_info_16
[INFO ] 12:06:35.086 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_0
[INFO ] 12:06:35.086 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_1
[INFO ] 12:06:35.086 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_2
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_3
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_4
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_5
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_6
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_7
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_8
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_9
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_10
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_11
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_12
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_13
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_14
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_15
[INFO ] 12:06:35.087 [ShardingSphere-Command-1] ShardingSphere-SQL - Actual SQL: ds_1 ::: truncate table t_order_info_16
```