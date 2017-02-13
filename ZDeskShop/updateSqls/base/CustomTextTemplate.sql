/*
Navicat MySQL Data Transfer

Source Server         : 2
Source Server Version : 50535
Source Host           : 128.128.128.100:3306
Source Database       : securityDB

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2015-02-03 11:44:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for CustomTextTemplate
-- ----------------------------
DROP TABLE IF EXISTS `CustomTextTemplate`;
CREATE TABLE `CustomTextTemplate` (
  `id` char(36) NOT NULL COMMENT '模板id',
  `name` varchar(50) DEFAULT '' COMMENT '模板名称',
  `templateType` char(50) DEFAULT '' COMMENT '模板类型，动态dynamic,静态static',
  `maxSize` int(11) DEFAULT '0' COMMENT '总长度',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
