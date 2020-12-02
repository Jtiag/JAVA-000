CREATE TABLE `t_order_detail`
(
    `id`          bigint(20)     NOT NULL AUTO_INCREMENT COMMENT '订单详情id',
    `order_id`    bigint(20)     NOT NULL COMMENT '订单id',
    `user_id`     bigint(20)     NOT NULL COMMENT '用户id',
    `sku_id`      bigint(20)     NOT NULL COMMENT '商品id',
    `sku_name`    varchar(200)   NOT NULL COMMENT '商品名',
    `order_price` decimal(10, 2) NOT NULL COMMENT '商品价格',
    `sku_num`     int(10)        NOT NULL COMMENT '商品数量',
    `create_time` timestamp      NOT NULL COMMENT '创建时间',
    `update_time` timestamp      NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='订单表详情表';