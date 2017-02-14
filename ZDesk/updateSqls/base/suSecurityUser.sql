/*
Navicat MySQL Data Transfer

Source Server         : 2
Source Server Version : 50535
Source Host           : 128.128.128.100:3306
Source Database       : securityDB

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2015-02-03 11:18:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for suSecurityUser
-- ----------------------------
DROP TABLE IF EXISTS `suSecurityUser`;
CREATE TABLE `suSecurityUser` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '业务无关主键',
  `loginName` varchar(32) CHARACTER SET gbk COLLATE gbk_bin NOT NULL DEFAULT '',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '用户姓名',
  `pwd` varchar(32) CHARACTER SET gbk COLLATE gbk_bin NOT NULL DEFAULT '',
  `startus` int(11) DEFAULT '0' COMMENT '用户当前状态：0 正常 1 锁定 2 删除',
  `viewIndex` int(11) DEFAULT '999999' COMMENT '排序字段',
  `phone_number` varchar(32) NOT NULL DEFAULT '' COMMENT '分机号',
  `agent_level` varchar(32) DEFAULT '' COMMENT '等级',
  `agent_information` varchar(400) DEFAULT '' COMMENT '附加信息',
  `job_number` varchar(32) DEFAULT '' COMMENT '工号',
  `seat_number` varchar(32) DEFAULT '' COMMENT '坐席号',
  `createDate` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '用户建立时间',
  `lastLoginDate` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '用户最后登录时间',
  `lastModifyPassword` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改密码的时间',
  `companyId` varchar(32) DEFAULT '' COMMENT '公司Id',
  `companyName` varchar(32) DEFAULT '' COMMENT '公司名称',
  `departmentId` varchar(32) DEFAULT '' COMMENT '部门Id',
  `departmentName` varchar(32) DEFAULT '' COMMENT '部门名称',
  `alias1` varchar(32) DEFAULT '' COMMENT '预留字段1',
  `alias2` varchar(32) DEFAULT '' COMMENT '预留字段2',
  `alias3` varchar(32) DEFAULT '' COMMENT '预留字段3',
  `alias4` varchar(32) DEFAULT '' COMMENT '预留字段4',
  `alias5` varchar(32) NOT NULL DEFAULT '' COMMENT '预留字段5',
  PRIMARY KEY (`id`,`alias5`),
  UNIQUE KEY `loginName` (`loginName`) USING BTREE,
  UNIQUE KEY `phone_number` (`phone_number`) USING BTREE,
  KEY `name` (`name`) USING BTREE
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED COMMENT='用户表';
