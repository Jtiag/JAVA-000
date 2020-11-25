CREATE TABLE `t_user`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `name`        varchar(100) NOT NULL COMMENT '姓名',
    `birthday`    timestamp    NOT NULL COMMENT '生日',
    `gender`      tinyint      NOT NULL COMMENT '性别',
    `email`       varchar(50)  NOT NULL COMMENT '邮箱',
    `phone`       varchar(11)  NOT NULL COMMENT '电话',
    `user_level`  tinyint      NOT NULL COMMENT '用户等级',
    `create_time` timestamp    NOT NULL COMMENT '创建时间',
    `update_time` timestamp    NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='用户表';