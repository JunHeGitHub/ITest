-- MySQL dump 10.13  Distrib 5.1.57, for unknown-linux-gnu (x86_64)
--
-- Host: localhost    Database: securityDB
-- ------------------------------------------------------
-- Server version	5.1.57-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES gbk */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `DicIndex`
--

DROP TABLE IF EXISTS `DicIndex`;
CREATE TABLE `DicIndex` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `indexCode` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '索引码',
  `indexName` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '索引名称',
  `dicInternational` varchar(32) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '国际化类型',
  PRIMARY KEY (`id`),
  KEY `indexCode` (`indexName`)
) ENGINE=MyISAM AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of DicIndex
-- ----------------------------
INSERT INTO `DicIndex` VALUES ('42', 'system_default', '系统默认字典项', '');
INSERT INTO `DicIndex` VALUES ('43', 'test', '测试1', '');