CREATE TABLE `t_goods`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '商品编号sku_id',
    `spu_id`      bigint(20)   NOT NULL COMMENT '商品聚合信息id',
    `price`       double       NOT NULL COMMENT '商品价格',
    `sku_name`    varchar(300) NOT NULL COMMENT '商品名称',
    `sku_desc`    text         NOT NULL COMMENT '商品描述',
    `weight`      double       NOT NULL COMMENT '商品重量',
    `brand_id`    int(10)      NOT NULL COMMENT '品牌id',
    `category_id` int(10)      NOT NULL COMMENT '品类id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT ='商品详情表';