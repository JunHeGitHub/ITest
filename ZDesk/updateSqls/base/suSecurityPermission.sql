/*
Navicat MySQL Data Transfer

Source Server         : 11
Source Server Version : 50157
Source Host           : 128.128.128.128:3306
Source Database       : securityDB

Target Server Type    : MYSQL
Target Server Version : 50157
File Encoding         : 65001

Date: 2014-10-13 12:32:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for suSecurityPermission
-- ----------------------------
DROP TABLE IF EXISTS `suSecurityPermission`;
CREATE TABLE `suSecurityPermission` (
  `id` char(36) NOT NULL COMMENT '业务无关主键',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '权限名称',
  `typeName` varchar(20) NOT NULL DEFAULT '' COMMENT '权限类型',
  `modleId` varchar(50) NOT NULL DEFAULT '' COMMENT '模块id',
  `modleName` varchar(100) NOT NULL DEFAULT '' COMMENT '模块名称',
  `funId` varchar(50) NOT NULL DEFAULT '' COMMENT '功能id',
  `funName` varchar(50) NOT NULL DEFAULT '' COMMENT '功能名称',
  `alias1` varchar(100) NOT NULL DEFAULT '' COMMENT '预留字段',
  `alias2` varchar(100) NOT NULL DEFAULT '' COMMENT '预留字段',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk COMMENT='权限表';

-- ----------------------------
-- Records of suSecurityPermission
-- ----------------------------
INSERT INTO `suSecurityPermission` VALUES ('2DE8C1BB-EF6E-9240-9439-6E8D5607EBDA', '', 'model', '', '', '', '', '', '');
INSERT INTO `suSecurityPermission` VALUES ('B2A120DB-8837-D283-CA13-5A87C67A0782', '', '123', '', '', '', '', '', '');
INSERT INTO `suSecurityPermission` VALUES ('DD1DF081-9AAE-CC77-4751-40C939B40027', '', '123', '', '', '', '', '', '');
