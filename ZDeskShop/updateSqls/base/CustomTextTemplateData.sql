/*
Navicat MySQL Data Transfer

Source Server         : 2
Source Server Version : 50535
Source Host           : 128.128.128.100:3306
Source Database       : securityDB

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2015-02-03 11:45:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for CustomTextTemplateData
-- ----------------------------
DROP TABLE IF EXISTS `CustomTextTemplateData`;
CREATE TABLE `CustomTextTemplateData` (
  `id` char(36) NOT NULL COMMENT '模板数据id',
  `content` varchar(50) DEFAULT '' COMMENT '模板数据内容',
  `type` varchar(20) DEFAULT '' COMMENT '模板数据类型constant常量variable变量',
  `viewIndex` int(11) DEFAULT '9999' COMMENT '模板数据显示顺序',
  `templateId` char(36) DEFAULT '' COMMENT '模板id',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
