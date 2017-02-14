/*
Navicat MySQL Data Transfer

Source Server         : 2
Source Server Version : 50535
Source Host           : 128.128.128.100:3306
Source Database       : securityDB

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2015-02-03 11:43:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for commonTreeDataMapper
-- ----------------------------
DROP TABLE IF EXISTS `commonTreeDataMapper`;
CREATE TABLE `commonTreeDataMapper` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `nodeId` char(36) DEFAULT '' COMMENT '树节点id',
  `dataId` char(36) DEFAULT '' COMMENT '树下数据id',
  PRIMARY KEY (`id`),
  KEY `nodeId` (`nodeId`),
  KEY `dataId` (`dataId`)
) ENGINE=MyISAM AUTO_INCREMENT=40998 DEFAULT CHARSET=utf8;
