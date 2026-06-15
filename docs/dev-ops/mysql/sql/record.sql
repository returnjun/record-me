-- ==========================================================
-- 1. 创建并使用数据库 (推荐使用 utf8mb4 字符集以支持 Emoji)
-- ==========================================================
CREATE DATABASE IF NOT EXISTS `period_tracker` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `period_tracker`;

-- ==========================================================
-- 2. 清理旧表 (如果存在的话，方便重复执行脚本)
-- ==========================================================
DROP TABLE IF EXISTS `daily_behavior_log`;
DROP TABLE IF EXISTS `daily_symptom`;
DROP TABLE IF EXISTS `cycle_record`;
DROP TABLE IF EXISTS `user_info`;

-- ==========================================================
-- 包含“逻辑删除”的最终版数据表结构
-- ==========================================================

-- 1. 用户信息表 (user_info)
CREATE TABLE `user_info` (
                             `user_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户唯一主键',
                             `username` VARCHAR(50) NOT NULL COMMENT '用户名',
                             `password` VARCHAR(255) NOT NULL COMMENT '密码',
                             `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
                             `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
                             `avg_cycle_days` INT DEFAULT 28 COMMENT '平均周期长度(天)',
                             `avg_period_days` INT DEFAULT 5 COMMENT '平均经期长度(天)',
                             `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除',
                             `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                             `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',

    -- 【新增】唯一性约束（底层会通过建立唯一索引来实现）
                             UNIQUE KEY `uk_username` (`username`) COMMENT '用户名唯一索引',
                             UNIQUE KEY `uk_phone` (`phone`) COMMENT '手机号唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 2. 生理周期表 (cycle_record)
CREATE TABLE `cycle_record` (
                                `cycle_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '周期记录主键',
                                `user_id` BIGINT NOT NULL COMMENT '关联的用户ID(逻辑外键)',
                                `start_date` DATE NOT NULL COMMENT '经期开始日(来时)',
                                `end_date` DATE DEFAULT NULL COMMENT '经期结束日(走时)',
                                `is_active` TINYINT(1) DEFAULT 1 COMMENT '是否为当前最新周期：1是，0否',
                                `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除', -- 【新增】逻辑删除字段
                                `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                                `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                                INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生理周期记录表';

-- 3. 每日症状打卡表 (daily_symptom)
CREATE TABLE `daily_symptom` (
                                 `record_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '每日打卡主键',
                                 `cycle_id` BIGINT NOT NULL COMMENT '归属的周期ID(逻辑外键)',
                                 `user_id` BIGINT NOT NULL COMMENT '关联的用户ID(逻辑外键)',
                                 `record_date` DATE NOT NULL COMMENT '记录的具体日期',
                                 `flow_level` INT DEFAULT 0 COMMENT '流量大小：1=少, 2=中, 3=多',
                                 `pain_level` INT DEFAULT 0 COMMENT '痛经程度：0=无, 1=轻微, 2=剧烈',
                                 `mood` VARCHAR(50) DEFAULT NULL COMMENT '心情状态',
                                 `notes` VARCHAR(255) DEFAULT NULL COMMENT '额外备注',
                                 `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除', -- 【新增】逻辑删除字段
                                 `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                                 `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                                 INDEX `idx_cycle_id` (`cycle_id`),
                                 INDEX `idx_user_date` (`user_id`, `record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日症状打卡表';

-- 4. 每日行为与外部因素聚合表 (daily_behavior_log)
CREATE TABLE `daily_behavior_log` (
                                      `log_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志主键',
                                      `user_id` BIGINT NOT NULL COMMENT '关联的用户ID(逻辑外键)',
                                      `record_date` DATE NOT NULL COMMENT '记录的具体日期',
                                      `behaviors_data` JSON DEFAULT NULL COMMENT '当日所有行为详情(JSON数组)',
                                      `is_deleted` TINYINT(1) DEFAULT 0 COMMENT '逻辑删除：0未删除，1已删除', -- 【新增】逻辑删除字段
                                      `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                                      `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
    -- 因为加了逻辑删除，唯一索引也需要把 is_deleted 考虑进去，避免重新插入同一天数据时冲突
                                      UNIQUE KEY `uk_user_date_del` (`user_id`, `record_date`, `is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日行为与外部因素聚合表';

CREATE TABLE `user_login_log` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
                                  `user_id` bigint(20) NOT NULL COMMENT '关联的用户ID',
                                  `login_ip` varchar(50) DEFAULT NULL COMMENT '登录时的IP地址',
                                  `login_device` varchar(255) DEFAULT NULL COMMENT '登录设备/浏览器信息(User-Agent)',
                                  `login_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '登录状态：1成功，0失败（密码错误等）',
                                  `login_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录发生的时间',
                                  PRIMARY KEY (`id`),
                                  KEY `idx_user_id` (`user_id`) USING BTREE -- 加上索引，方便查某个用户的所有记录
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户登录痕迹记录表';


INSERT INTO `user_info` (`user_id`, `username`, `password`, `avatar`, `phone`, `avg_cycle_days`, `avg_period_days`) VALUES
                                                                                                                        (1, 'Alice', '123456', 'https://example.com/avatar1.png', '13800000001', 28, 5),
                                                                                                                        (2, 'Bella', '123456', 'https://example.com/avatar2.png', '13800000002', 30, 6);

INSERT INTO `cycle_record` (`cycle_id`, `user_id`, `start_date`, `end_date`, `is_active`) VALUES
-- Alice 的历史周期 (已结束，is_active = 0)
(1, 1, '2026-04-25', '2026-04-29', 0),
-- Alice 的当前周期 (进行中，没有 end_date，is_active = 1)
(2, 1, '2026-05-25', NULL, 1),
-- Bella 的当前周期 (进行中，没有 end_date，is_active = 1)
(3, 2, '2026-05-28', NULL, 1);

INSERT INTO `daily_symptom` (`cycle_id`, `user_id`, `record_date`, `flow_level`, `pain_level`, `mood`, `notes`) VALUES
-- Alice 历史周期 (cycle_id=1) 的打卡记录
(1, 1, '2026-04-25', 2, 1, '疲惫', '第一天有点累'),
(2, 1, '2026-04-26', 3, 2, '烦躁', '量多，肚子疼'),
(3, 1, '2026-04-27', 2, 0, '平静', '好多了'),

-- Alice 当前周期 (cycle_id=2) 的打卡记录
(4, 1, '2026-05-25', 1, 1, '低落', '又来了，不想动'),
(5, 1, '2026-05-26', 3, 2, '敏感', '喝了红糖水稍微缓解'),

-- Bella 当前周期 (cycle_id=3) 的打卡记录
(6, 2, '2026-05-28', 2, 0, '开心', '没什么感觉，正常上班');

INSERT INTO `daily_behavior_log` (`user_id`, `record_date`, `behaviors_data`) VALUES
-- Alice 的行为记录
(1, '2026-05-25', '[{"type": "diet", "detail": "喝冷饮"}, {"type": "sleep", "detail": "熬夜到2点"}]'),
(1, '2026-05-26', '[{"type": "exercise", "detail": "瑜伽30分钟"}, {"type": "medication", "detail": "吃布洛芬"}]'),

-- Bella 的行为记录
(2, '2026-05-28', '[{"type": "exercise", "detail": "慢跑3公里"}]');