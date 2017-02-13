/*
Navicat MySQL Data Transfer

Source Server         : 1
Source Server Version : 50157
Source Host           : 128.128.128.128:3306
Source Database       : securityDB_R

Target Server Type    : MYSQL
Target Server Version : 50157
File Encoding         : 65001

Date: 2014-09-08 10:19:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for suSecurityUserRole
-- ----------------------------
DROP TABLE IF EXISTS `suSecurityUserRole`;
CREATE TABLE `suSecurityUserRole` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '业务无关主键',
  `loginName` varchar(100) NOT NULL COMMENT '用户登录名',
  `roleId` int(20) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=100020 DEFAULT CHARSET=gbk;
