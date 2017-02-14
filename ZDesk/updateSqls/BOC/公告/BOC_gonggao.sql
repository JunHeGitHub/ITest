/*
Navicat MySQL Data Transfer

Source Server         : 1111
Source Server Version : 50535
Source Host           : 128.128.128.128:3306
Source Database       : securityDB

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2015-05-20 16:15:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `BOC_gonggao`
-- ----------------------------
DROP TABLE IF EXISTS `BOC_gonggao`;
CREATE TABLE `BOC_gonggao` (
  `id` char(36) NOT NULL COMMENT '业务无关主键："uuid"',
  `title` varchar(1000) DEFAULT '' COMMENT '消息标题',
  `bDate` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '开始日期',
  `eDate` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '结束日期',
  `createUser` varchar(200) DEFAULT '' COMMENT '创建者',
  `createTime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '创建日期',
  `area` varchar(500) DEFAULT '' COMMENT '地区',
  `content` varchar(4000) DEFAULT '' COMMENT '消息内容',
  `disponseOpinion` varchar(4000) DEFAULT '' COMMENT '办理意见',
  `checkOneOpinion` varchar(4000) DEFAULT '' COMMENT '复核意见',
  `basilicType` varchar(200) DEFAULT '' COMMENT '紧急程度',
  `messageState` varchar(200) DEFAULT '' COMMENT '消息状态',
  `approveState` varchar(200) DEFAULT '' COMMENT '审批状态',
  `appointedCheckUserID` varchar(100) DEFAULT '' COMMENT '指定复核人ID',
  `appointedCheckUser` varchar(200) DEFAULT '' COMMENT '指定复核人',
  `disponseUser` varchar(200) DEFAULT '' COMMENT '当前处理人',
  `disponseBTime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '处理开始时间',
  `disponseETime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '处理时间结束时间',
  `securityUserId` mediumtext COMMENT '授权人ID',
  `securityUser` mediumtext COMMENT '发布时授权给定用户',
  `publishArea` varchar(50) DEFAULT NULL COMMENT '发布范围',
  `isUpdate` varchar(10) DEFAULT NULL COMMENT '判断是否修订',
  `published` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '发布时间',
  `applyTime` datetime DEFAULT NULL COMMENT '申请时间',
  `flowState` varchar(20) DEFAULT NULL,
  `flowId` varchar(36) DEFAULT '' COMMENT '流程标识',
  `flowNode` varchar(200) DEFAULT '' COMMENT '处理的流程节点',
  `companyId` varchar(50) DEFAULT '' COMMENT '公司id',
  `companyName` varchar(200) DEFAULT '' COMMENT '公司名称',
  `departmentId` varchar(50) DEFAULT '' COMMENT '部门id',
  `departmentName` varchar(200) DEFAULT '' COMMENT '部门名称',
  `appointedCheckTime` datetime DEFAULT NULL COMMENT '复核处理结束时间',
  `endDealTime` datetime DEFAULT NULL COMMENT '最后处理时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

