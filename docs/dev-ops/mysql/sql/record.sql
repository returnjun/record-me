-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: period_tracker
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cycle_record`
--

DROP TABLE IF EXISTS `cycle_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `cycle_record` (
  `cycle_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '周期记录主键',
  `user_id` bigint(20) NOT NULL COMMENT '关联的用户ID(逻辑外键)',
  `start_date` date NOT NULL COMMENT '经期开始日(来时)',
  `end_date` date DEFAULT NULL COMMENT '经期结束日(走时)',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否为当前最新周期：1是，0否',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`cycle_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='生理周期记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cycle_record`
--

LOCK TABLES `cycle_record` WRITE;
/*!40000 ALTER TABLE `cycle_record` DISABLE KEYS */;
INSERT INTO `cycle_record` VALUES (1,1,'2026-06-12','2026-06-15',0,0,'2026-06-01 22:48:09','2026-06-15 19:34:57'),(2,1,'2026-05-25','2026-06-02',0,0,'2026-06-01 22:48:09','2026-06-02 17:26:34'),(3,2,'2026-05-28','2026-06-03',1,0,'2026-06-01 22:48:09','2026-06-03 11:36:01'),(7,1,'2026-06-03','2026-06-15',1,0,'2026-06-03 14:05:32','2026-06-15 19:41:34'),(8,5,'2026-06-07',NULL,1,0,'2026-06-07 10:58:40','2026-06-07 10:58:40'),(10,6,'2021-09-30','2021-10-07',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(11,6,'2021-11-27','2021-12-03',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(12,6,'2022-01-03','2022-01-10',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(13,6,'2022-02-05','2022-02-10',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(14,6,'2022-03-09','2022-03-13',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(15,6,'2022-03-28','2022-03-31',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(16,6,'2022-04-23','2022-04-28',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(17,6,'2022-05-23','2022-05-29',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(18,6,'2022-07-04','2022-07-12',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(19,6,'2022-08-04','2022-08-10',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(20,6,'2022-09-03','2022-09-10',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(21,6,'2022-10-04','2022-10-15',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(22,6,'2022-11-28','2022-12-03',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(23,6,'2022-12-26','2023-01-01',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(24,6,'2023-02-06','2023-02-11',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(25,6,'2023-03-12','2023-03-13',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(26,6,'2023-04-22','2023-04-27',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(27,6,'2023-05-27','2023-06-04',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(28,6,'2023-07-02','2023-07-06',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(29,6,'2023-08-12','2023-08-18',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(30,6,'2023-09-17','2023-09-23',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(31,6,'2023-10-02','2023-10-06',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(32,6,'2023-10-21','2023-10-28',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(33,6,'2023-11-25','2023-11-30',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(34,6,'2023-12-30','2024-01-06',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(35,6,'2024-02-01','2024-02-08',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(36,6,'2024-02-27','2024-03-05',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(37,6,'2024-03-31','2024-04-06',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(38,6,'2024-05-04','2024-05-10',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(39,6,'2024-06-08','2024-06-15',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(40,6,'2024-07-26','2024-08-02',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(41,6,'2024-09-03','2024-09-08',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(42,6,'2024-10-15','2024-10-20',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(43,6,'2024-11-07','2024-11-11',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(44,6,'2024-12-12','2024-12-17',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(45,6,'2025-01-06','2025-01-14',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(46,6,'2025-02-06','2025-02-11',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(47,6,'2025-03-21','2025-04-08',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(48,6,'2025-04-30','2025-05-06',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(49,6,'2025-06-10','2025-06-16',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(50,6,'2025-07-05','2025-07-17',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(51,6,'2025-08-05','2025-08-10',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(52,6,'2025-08-21','2025-08-26',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(53,6,'2025-09-14','2025-09-19',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(54,6,'2025-10-11','2025-10-23',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(55,6,'2025-11-12','2025-11-17',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(56,6,'2025-12-10','2025-12-15',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(57,6,'2025-12-29','2026-01-03',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(58,6,'2026-02-09','2026-02-15',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(59,6,'2026-03-21','2026-03-26',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(60,6,'2026-04-30','2026-05-04',0,0,'2026-06-15 22:35:55','2026-06-15 22:35:55'),(61,6,'2026-06-01','2026-06-07',1,0,'2026-06-15 22:35:55','2026-06-15 22:35:55');
/*!40000 ALTER TABLE `cycle_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `daily_behavior_log`
--

DROP TABLE IF EXISTS `daily_behavior_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `daily_behavior_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `user_id` bigint(20) NOT NULL COMMENT '关联的用户ID(逻辑外键)',
  `record_date` date NOT NULL COMMENT '记录的具体日期',
  `behaviors_data` json DEFAULT NULL COMMENT '当日所有行为详情(JSON数组)',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`log_id`),
  UNIQUE KEY `uk_user_date_del` (`user_id`,`record_date`,`is_deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='每日行为与外部因素聚合表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daily_behavior_log`
--

LOCK TABLES `daily_behavior_log` WRITE;
/*!40000 ALTER TABLE `daily_behavior_log` DISABLE KEYS */;
INSERT INTO `daily_behavior_log` VALUES (1,1,'2026-05-25','[{\"type\": \"diet\", \"detail\": \"喝冷饮\"}, {\"type\": \"sleep\", \"detail\": \"熬夜到2点\"}]',0,'2026-06-01 22:48:09','2026-06-01 22:48:09'),(2,1,'2026-05-26','[{\"type\": \"exercise\", \"detail\": \"瑜伽30分钟\"}, {\"type\": \"medication\", \"detail\": \"吃布洛芬\"}]',0,'2026-06-01 22:48:09','2026-06-01 22:48:09'),(3,2,'2026-05-28','[{\"type\": \"exercise\", \"detail\": \"慢跑3公里\"}]',0,'2026-06-01 22:48:09','2026-06-01 22:48:09');
/*!40000 ALTER TABLE `daily_behavior_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `daily_symptom`
--

DROP TABLE IF EXISTS `daily_symptom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `daily_symptom` (
  `record_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '每日打卡主键',
  `cycle_id` bigint(20) NOT NULL COMMENT '归属的周期ID(逻辑外键)',
  `user_id` bigint(20) NOT NULL COMMENT '关联的用户ID(逻辑外键)',
  `record_date` date NOT NULL COMMENT '记录的具体日期',
  `flow_level` int(11) DEFAULT '0' COMMENT '流量大小：0=少, 1=中, 2=多',
  `pain_level` int(11) DEFAULT '0' COMMENT '痛经程度：0=轻微, 1=正常, 2=剧烈',
  `mood` varchar(50) DEFAULT NULL COMMENT '心情状态',
  `notes` varchar(255) DEFAULT NULL COMMENT '额外备注',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`record_id`),
  KEY `idx_cycle_id` (`cycle_id`),
  KEY `idx_user_date` (`user_id`,`record_date`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='每日症状打卡表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `daily_symptom`
--

LOCK TABLES `daily_symptom` WRITE;
/*!40000 ALTER TABLE `daily_symptom` DISABLE KEYS */;
INSERT INTO `daily_symptom` VALUES (1,1,1,'2026-04-25',2,1,'疲惫','第一天有点累',0,'2026-06-01 22:48:09','2026-06-01 22:48:09'),(2,2,1,'2026-04-26',3,2,'烦躁','量多，肚子疼',0,'2026-06-01 22:48:09','2026-06-01 22:48:09'),(3,3,1,'2026-04-27',2,0,'平静','好多了',0,'2026-06-01 22:48:09','2026-06-01 22:48:09'),(4,4,1,'2026-05-25',1,1,'低落','又来了，不想动',0,'2026-06-01 22:48:09','2026-06-01 22:48:09'),(5,5,1,'2026-05-26',3,2,'敏感','喝了红糖水稍微缓解',0,'2026-06-01 22:48:09','2026-06-01 22:48:09'),(6,6,2,'2026-05-28',2,0,'开心','没什么感觉，正常上班',0,'2026-06-01 22:48:09','2026-06-01 22:48:09'),(7,8,5,'2026-06-07',1,0,'幸福','',0,'2026-06-07 16:11:57','2026-06-07 21:57:21'),(8,8,5,'2026-06-08',0,0,NULL,NULL,0,'2026-06-08 10:12:48','2026-06-08 10:12:48'),(9,9,6,'2026-06-14',0,2,'幸福','hhh',0,'2026-06-14 23:57:30','2026-06-14 23:57:42');
/*!40000 ALTER TABLE `daily_symptom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_info`
--

DROP TABLE IF EXISTS `user_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_info` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户唯一主键',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `birthday` date DEFAULT NULL COMMENT '出生日期(用于精准计算年龄段)',
  `height` decimal(5,1) DEFAULT NULL COMMENT '身高(cm)',
  `weight` decimal(5,1) DEFAULT NULL COMMENT '体重(kg)',
  `avg_cycle_days` int(11) DEFAULT '28' COMMENT '平均周期长度(天)',
  `avg_period_days` int(11) DEFAULT '5' COMMENT '平均经期长度(天)',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除：0未删除，1已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_info`
--

LOCK TABLES `user_info` WRITE;
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` VALUES (1,'Alice','$2a$10$LF482YeJ05bq9.fKX1DdiOF.4juSalSBKBdHpOd5ZKmnKdkguPiA6','https://api.dicebear.com/7.x/adventurer/svg?seed=ypa78i','13800000001','2026-06-18',NULL,NULL,9,7,0,'2026-06-01 22:48:09','2026-06-15 22:04:37'),(2,'Bella','123456','https://example.com/avatar2.png','13800000002',NULL,NULL,NULL,30,6,0,'2026-06-01 22:48:09','2026-06-01 22:48:09'),(5,'daohaha','$2a$10$LF482YeJ05bq9.fKX1DdiOF.4juSalSBKBdHpOd5ZKmnKdkguPiA6',NULL,'18615425095',NULL,NULL,NULL,28,5,0,'2026-06-03 14:23:49','2026-06-03 14:23:49'),(6,'zhuo','$2a$10$mOiiBQpaW3WwM4k/4BDPZOs4.6F5DBI7YHcwsl2.2IkRETw/uoBpO','','15589994049','2026-06-15',1.0,1.0,28,5,0,'2026-06-14 23:56:55','2026-06-15 22:30:41');
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_login_log`
--

DROP TABLE IF EXISTS `user_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `user_id` bigint(20) NOT NULL COMMENT '关联的用户ID',
  `login_ip` varchar(50) DEFAULT NULL COMMENT '登录时的IP地址',
  `login_device` varchar(255) DEFAULT NULL COMMENT '登录设备/浏览器信息(User-Agent)',
  `login_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '登录状态：1成功，0失败（密码错误等）',
  `login_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录发生的时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户登录痕迹记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_login_log`
--

LOCK TABLES `user_login_log` WRITE;
/*!40000 ALTER TABLE `user_login_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_login_log` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-15 22:40:33
