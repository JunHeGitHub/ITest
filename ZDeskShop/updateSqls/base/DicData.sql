-- MySQL dump 10.13  Distrib 5.1.57, for unknown-linux-gnu (x86_64)
--
-- Host: localhost    Database: securityDB
-- ------------------------------------------------------
-- Server version	5.1.57-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `DicData`
--

DROP TABLE IF EXISTS `DicData`;
CREATE TABLE `DicData` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '字典码',
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '名称',
  `indexCode` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '索引类型',
  `indexName` varchar(32) COLLATE utf8_unicode_ci DEFAULT '',
  `parentIndexCode` varchar(32) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '级联索引',
  `parentIndexName` varchar(64) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '级联索引名称',
  `dicInternational` varchar(32) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '国际化',
  `sortColums` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  `companyId` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `companyName` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `departmentId` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `departmentName` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexType` (`indexCode`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=87 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

