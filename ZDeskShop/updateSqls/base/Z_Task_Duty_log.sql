/*
Navicat MySQL Data Transfer

Source Server         : 128.128.128.128
Source Server Version : 50535
Source Host           : 128.128.128.128:3306
Source Database       : securityDB

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2015-05-13 12:31:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for Z_Task_Duty_log
-- ----------------------------
DROP TABLE IF EXISTS `Z_Task_Duty_log`;
CREATE TABLE `Z_Task_Duty_log` (
  `id` varchar(36) NOT NULL DEFAULT '' COMMENT '主键',
  `type` varchar(50) DEFAULT '' COMMENT '任务类型',
  `runTime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '执行时间',
  `mgs` varchar(1000) DEFAULT '' COMMENT '消息',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;
