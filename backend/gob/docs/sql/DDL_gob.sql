/******************************************/
/*   DatabaseName = gob  */
/*   TableName = users   */
/******************************************/
CREATE TABLE `users` (
                         `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID（自增主键）',
                         `gmt_create` datetime NOT NULL COMMENT '创建时间',
                         `gmt_modified` datetime NOT NULL COMMENT '最后更新时间',
                         `nick_name` varchar(255) DEFAULT NULL COMMENT '用户昵称',
                         `password_hash` varchar(255) DEFAULT NULL COMMENT '密码哈希',
                         `state` varchar(64) DEFAULT NULL COMMENT '用户状态（ACTIVE，FROZEN）',
                         `invite_code` varchar(255) DEFAULT NULL COMMENT '邀请码',
                         `telephone` varchar(20) DEFAULT NULL COMMENT '手机号码',
                         `inviter_id` varchar(255) DEFAULT NULL COMMENT '邀请人用户ID',
                         `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
                         `profile_photo_url` varchar(255) DEFAULT NULL COMMENT '用户头像URL',
                         `real_name` varchar(255) DEFAULT NULL COMMENT '真实姓名',
                         `id_card_no` varchar(255) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '身份证no',
                         `user_role` varchar(128) DEFAULT NULL COMMENT '用户角色',
                         `deleted` int DEFAULT NULL COMMENT '是否逻辑删除，0为未删除，非0为已删除',
                         `lock_version` int DEFAULT NULL COMMENT '乐观锁版本号',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4  COMMENT='用户信息表'
;

/******************************************/
/*   DatabaseName = gob                   */
/*   TableName = user_accounts            */
/******************************************/
CREATE TABLE `user_accounts` (
                                 `id`              bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '账户ID（自增主键）',
                                 `gmt_create`      datetime        NOT NULL COMMENT '创建时间',
                                 `gmt_modified`    datetime        NOT NULL COMMENT '最后更新时间',
                                 `user_id`         bigint unsigned NOT NULL COMMENT '对应 users 表主键',
                                 `account_no`      varchar(64)     NOT NULL COMMENT '账户号（全局唯一）',
                                 `account_type`    varchar(32)     NOT NULL DEFAULT 'MAIN' COMMENT '账户类型（MAIN-主账户，SUB-子账户，TEST-测试币账户…）',
                                 `currency`        varchar(8)      NOT NULL DEFAULT 'CNY' COMMENT '币种（CNY/USD/POINT…）',
                                 `balance`         decimal(20,2)   NOT NULL DEFAULT 0.00 COMMENT '可用余额（分/厘后两位）',
                                 `frozen_amount`   decimal(20,2)   NOT NULL DEFAULT 0.00 COMMENT '冻结金额',
                                 `total_in`        decimal(20,2)   NOT NULL DEFAULT 0.00 COMMENT '累计入账',
                                 `total_out`       decimal(20,2)   NOT NULL DEFAULT 0.00 COMMENT '累计出账',
                                 `state`           varchar(32)     NOT NULL DEFAULT 'ACTIVE' COMMENT '账户状态（ACTIVE-正常，FROZEN-冻结，CLOSED-销户）',
                                 `deleted`         int             NOT NULL DEFAULT 0 COMMENT '是否逻辑删除，0为未删除，非0为已删除',
                                 `lock_version`    int             NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `uk_account_no` (`account_no`),
                                 UNIQUE KEY `uk_user_type_currency` (`user_id`, `account_type`, `currency`),
                                 KEY `idx_user_id` (`user_id`),
                                 CONSTRAINT `fk_user_accounts_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户账户表（用于模拟支付）';

/******************************************/
/*   DatabaseName = gob                   */
/*   TableName = account_entries          */
/******************************************/
CREATE TABLE `account_entries` (
                                   `id`               bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '流水ID（自增主键）',
                                   `gmt_create`       datetime        NOT NULL COMMENT '创建时间',
                                   `gmt_modified`     datetime        NOT NULL COMMENT '最后更新时间',

                                   `account_id`       bigint unsigned NOT NULL COMMENT '所属账户ID（user_accounts.id）',
                                   `entry_no`         varchar(64)     NOT NULL COMMENT '流水号（全局唯一，幂等）',

                                   `biz_type`         varchar(32)     NOT NULL COMMENT '业务类型（RECHARGE-充值, WITHDRAW-提现, PAY-付款, REFUND-退款, TRANSFER-转账, FROZEN-冻结, UNFROZEN-解冻, ADJUST-调账…）',
                                   `biz_id`           varchar(64)     NOT NULL COMMENT '业务单据号（外部系统订单号，可重复）',

                                   `amount`           decimal(20,2)   NOT NULL COMMENT '变动金额（正为入，负为出）',
                                   `balance`          decimal(20,2)   NOT NULL COMMENT '变动后账户余额',
                                   `frozen_amount`    decimal(20,2)   NOT NULL COMMENT '变动后冻结金额',

                                   `opposite_account` varchar(64)     DEFAULT NULL COMMENT '对端账户号（可选，转账/收款时填）',
                                   `remark`           varchar(255)    DEFAULT NULL COMMENT '摘要/备注',

                                   `deleted`          int             NOT NULL DEFAULT 0 COMMENT '逻辑删除标记，0=未删除',
                                   `lock_version`     int             NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',

                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uk_entry_no` (`entry_no`),
                                   KEY `idx_account_id` (`account_id`),
                                   KEY `idx_biz` (`biz_type`, `biz_id`),
                                   KEY `idx_gmt_create` (`gmt_create`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户流水表';
