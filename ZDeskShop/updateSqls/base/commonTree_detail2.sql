/*
Navicat MySQL Data Transfer

Source Server         : 111
Source Server Version : 50157
Source Host           : 128.128.128.128:3306
Source Database       : securityDB

Target Server Type    : MYSQL
Target Server Version : 50157
File Encoding         : 65001

Date: 2014-09-10 11:26:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for commonTree_detail
-- ----------------------------
DROP TABLE IF EXISTS `commonTree_detail`;
CREATE TABLE `commonTree_detail` (
  `id` char(36) NOT NULL COMMENT '主键UUID',
  `text` varchar(1000) DEFAULT '' COMMENT '用于ext的节点名称',
  `iconCls` varchar(200) DEFAULT '' COMMENT '用于ext节点的CSS对象',
  `leaf` varchar(20) DEFAULT 'true' COMMENT '用于ext描述节点的leaf',
  `recordType` varchar(20) DEFAULT 'f' COMMENT '目录文件标识，f文件，d目录',
  `parentId` char(36) DEFAULT '' COMMENT '上级节点ID',
  `createUser` varchar(200) DEFAULT '' COMMENT '创建者',
  `createTime` datetime DEFAULT NULL COMMENT '创建日期',
  `nodeTypeUrl` varchar(1000) DEFAULT NULL,
  `sortField` varchar(100) DEFAULT NULL COMMENT '派序列',
  `isdel` char(1) DEFAULT NULL COMMENT '是否删除',
  `delUser` varchar(200) DEFAULT '' COMMENT '删除用户',
  `companyId` varchar(50) DEFAULT '' COMMENT '公司ID',
  `companyName` varchar(200) DEFAULT '' COMMENT '公司名称',
  `departmentId` varchar(50) DEFAULT '' COMMENT '部门ID',
  `departmentName` varchar(200) DEFAULT '' COMMENT '部门名称',
  `leftClick` varchar(200) DEFAULT '' COMMENT '左击事件',
  `leftParam` varchar(36) DEFAULT '' COMMENT '左击鼠标参数',
  `rightClick` varchar(200) DEFAULT '' COMMENT '右击事件',
  `rightParam` varchar(36) DEFAULT '' COMMENT '右击鼠标参数',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk ROW_FORMAT=FIXED;

-- ----------------------------
-- Records of commonTree_detail
-- ----------------------------
INSERT INTO `commonTree_detail` VALUES ('13D03CF8-FF20-EEAF-D369-593B2BC6D90F', '天津市中心支行', '', 'true', 'structure', 'A02185E9-1E4F-4F52-3DE0-1BF9D3AABF99', 'ZSKadmin', '2014-08-27 14:00:03', null, '0', null, '', '', '', '', '', 'leftTest', 'zhuzhijigou_left_click_param', 'rightTest', 'zhuzhijigou_right_click_param');
INSERT INTO `commonTree_detail` VALUES ('A02185E9-1E4F-4F52-3DE0-1BF9D3AABF99', '中国银行', '', 'true', 'structure', '', 'ZSKadmin', '2014-08-26 09:49:10', null, '0', null, '', '', '', '', '', '', '', null, null);
INSERT INTO `commonTree_detail` VALUES ('2999CDCD-010A-6DD2-733B-0C194DE9446A', '贵阳中心支行', '', 'true', 'structure', 'A02185E9-1E4F-4F52-3DE0-1BF9D3AABF99', 'ZSKadmin', '2014-08-26 09:50:41', null, '0', null, '', '', '', '', '', 'leftTest', 'zhuzhijigou_left_click_param', 'rightTest', 'zhuzhijigou_right_click_param');
INSERT INTO `commonTree_detail` VALUES ('13D03CF8-FF20-EEAF-D369-593B2BC6D90E', '青岛市中心支行', '', 'true', 'structure', 'A02185E9-1E4F-4F52-3DE0-1BF9D3AABF99', 'ZSKadmin', '2014-08-26 09:51:45', null, '0', null, '', '', '', '', '', 'leftTest', 'zhuzhijigou_left_click_param', 'rightTest', 'zhuzhijigou_right_click_param');
INSERT INTO `commonTree_detail` VALUES ('13D03CF8-FF20-EEAF-D369-593B2BC6D90G', '重庆中心支行', '', 'true', 'structure', '13D03CF8-FF20-EEAF-D369-593B2BC6D90K', 'ZSKadmin', '2014-08-27 14:01:44', null, null, null, '', '', '', '', '', 'leftTest', 'zhuzhijigou_left_click_param', 'rightTest', 'zhuzhijigou_right_click_param');
INSERT INTO `commonTree_detail` VALUES ('13D03CF8-FF20-EEAF-D369-593B2BC6D90K', '四川分行', '', 'true', 'structure', 'A02185E9-1E4F-4F52-3DE0-1BF9D3AABF99', 'ZSKadmin', '2014-08-27 14:03:19', null, null, null, '', '', '', '', '', 'leftTest', 'zhuzhijigou_left_click_param', 'rightTest', 'zhuzhijigou_right_click_param');
INSERT INTO `commonTree_detail` VALUES ('31C76E87-B27C-4703-A43F-61EEF591B3C1', '成都中心支行', '', 'true', 'structure', '13D03CF8-FF20-EEAF-D369-593B2BC6D90K', null, '2014-08-07 11:19:36', null, null, null, '', '', '', '', '', '', '', null, null);
INSERT INTO `commonTree_detail` VALUES ('33EA758C-1634-426C-1D1A-2E73405B0634', '绵阳中心支行', '', 'true', 'structure', '13D03CF8-FF20-EEAF-D369-593B2BC6D90K', 'ZSKadmin', '2014-08-07 11:25:39', null, null, null, '', '', '', '', '', '', '', null, null);
INSERT INTO `commonTree_detail` VALUES ('9436426C-16F5-3794-918F-1FCA8F542448', '乐山中心支行', '', 'true', 'structure', '13D03CF8-FF20-EEAF-D369-593B2BC6D90K', 'ZSKadmin', '2014-08-07 11:38:53', null, null, null, '', '', '', '', '', '', '', null, null);
INSERT INTO `commonTree_detail` VALUES ('807EF9B0-5B6A-0ED2-CFFA-04FD47D3B789', '南充中心支行', '', 'true', 'structure', '13D03CF8-FF20-EEAF-D369-593B2BC6D90K', 'ZSKadmin', '2014-08-07 11:43:02', null, null, null, '', '', '', '', '', '', '', null, null);
INSERT INTO `commonTree_detail` VALUES ('C9D4D424-569B-3EEA-63D4-68BAFC96ABC9', '自贡中心支行', '', 'true', 'structure', '13D03CF8-FF20-EEAF-D369-593B2BC6D90K', 'ZSKadmin', '2014-08-07 11:45:26', null, null, null, '', '', '', '', '', '', '', null, null);
INSERT INTO `commonTree_detail` VALUES ('830AED24-0E95-D981-D27F-F07F948A248A', '河北分行', '', 'true', 'structure', 'A02185E9-1E4F-4F52-3DE0-1BF9D3AABF99', 'ZSKadmin', '2014-08-07 11:46:39', null, null, null, '', '', '', '', '', '', '', null, null);
INSERT INTO `commonTree_detail` VALUES ('27A0496C-A626-25B5-CFA8-15AFE40A0010', '石家庄中心支行', '', 'true', 'structure', '830AED24-0E95-D981-D27F-F07F948A248A', 'ZSKadmin', '2014-08-07 11:47:30', null, null, null, '', '', '', '', '', '', '', null, null);
INSERT INTO `commonTree_detail` VALUES ('B7165FDC-A5A1-FD35-3DFD-48E9C2C487F2', '中国石油', '', 'true', 'structure', '', 'ZSKadmin', '2014-08-29 15:19:05', null, null, null, '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail` VALUES ('01378050-43EF-AF12-A3F1-5AE11208B104', '中国移动', '', 'true', 'structure', '', 'ZSKadmin', '2014-08-29 15:21:02', null, null, null, '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail` VALUES ('C7ABA781-CDEC-4492-A1E6-118A3B54E36E', '似懂非懂三', '', 'true', 'intellectual', '', 'ZSKadmin', '2014-09-04 00:17:57', null, null, null, '', '', '', '', '', '', '', '', '');
