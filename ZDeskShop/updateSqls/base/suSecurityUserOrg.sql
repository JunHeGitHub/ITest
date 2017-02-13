/*
Navicat MySQL Data Transfer

Source Server         : 1
Source Server Version : 50157
Source Host           : 128.128.128.128:3306
Source Database       : securityDB_R

Target Server Type    : MYSQL
Target Server Version : 50157
File Encoding         : 65001

Date: 2014-09-08 10:19:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for suSecurityUserOrg
-- ----------------------------
DROP TABLE IF EXISTS `suSecurityUserOrg`;
CREATE TABLE `suSecurityUserOrg` (
  `id` varchar(36) NOT NULL,
  `loginName` varchar(36) DEFAULT NULL COMMENT '登录名',
  `orgCode` varchar(36) DEFAULT NULL COMMENT '组织id',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
