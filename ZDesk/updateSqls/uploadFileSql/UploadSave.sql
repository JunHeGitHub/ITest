/*
Navicat MySQL Data Transfer

Source Server         : 2
Source Server Version : 50535
Source Host           : 128.128.128.100:3306
Source Database       : securityDB

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2015-02-03 11:45:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for UploadSave
-- ----------------------------
DROP TABLE IF EXISTS `UploadSave`;
CREATE TABLE `UploadSave` (
  `id` varchar(255) NOT NULL DEFAULT '',
  `fileName` varchar(255) NOT NULL DEFAULT '1',
  `newFileName` varchar(255) DEFAULT '',
  `fileType` varchar(255) DEFAULT '',
  `savePath` varchar(255) DEFAULT '',
  `fileSize` varchar(255) DEFAULT '',
  `fileText` varchar(1000)  DEFAULT '',
  `uploadTime` datetime DEFAULT NULL COMMENT '文件上传时间',
  `state` varchar(1) DEFAULT '1' COMMENT '文件当前状态，0已删除，1未删除',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;
