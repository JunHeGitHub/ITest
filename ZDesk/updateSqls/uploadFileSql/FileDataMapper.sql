/*
Navicat MySQL Data Transfer

Source Server         : 2
Source Server Version : 50535
Source Host           : 128.128.128.100:3306
Source Database       : securityDB

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2015-02-03 11:45:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for FileDataMapper
-- ----------------------------
DROP TABLE IF EXISTS `FileDataMapper`;
CREATE TABLE `FileDataMapper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fileId` varchar(50) DEFAULT '',
  `dataId` varchar(50) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
