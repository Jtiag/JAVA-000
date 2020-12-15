CREATE TABLE `t_order_info`
(
    `id`                   bigint(20)     NOT NULL AUTO_INCREMENT COMMENT '自增id',
    `order_id`             bigint(20)     NOT NULL COMMENT '订单编号',
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