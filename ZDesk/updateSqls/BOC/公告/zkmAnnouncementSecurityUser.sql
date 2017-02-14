/*
Navicat MySQL Data Transfer

Source Server         : 1111
Source Server Version : 50535
Source Host           : 128.128.128.128:3306
Source Database       : securityDB

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2015-05-15 11:33:38
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `zkmAnnouncementSecurityUser`
-- ----------------------------
DROP TABLE IF EXISTS `zkmAnnouncementSecurityUser`;
CREATE TABLE `zkmAnnouncementSecurityUser` (
  `id` int(255) NOT NULL COMMENT '业务无关主键："uuid"',
  `msgId` varchar(36) DEFAULT NULL,
  `Receiver` mediumtext DEFAULT NULL,  
  `Title` varchar(1000) DEFAULT NULL,
  `bDate` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '开始日期',
  `eDate` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '结束日期',
  `readFlag` char(36) DEFAULT NULL ,
  `status` char(36) DEFAULT NULL ,
  PRIMARY KEY (`id`) 
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of zkmAnnouncementSecurityUser
-- ----------------------------
