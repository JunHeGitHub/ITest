/*
Navicat MySQL Data Transfer

Source Server         : 128.128.128.128
Source Server Version : 50535
Source Host           : 128.128.128.128:3306
Source Database       : securityDB

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2015-05-13 12:31:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for Z_excelTaskDefined
-- ----------------------------
DROP TABLE IF EXISTS `Z_excelTaskDefined`;
CREATE TABLE `Z_excelTaskDefined` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '业务无关主键',
  `sql` text COMMENT '报表程序要执行的查询数据的sql',
  `sqlParam` varchar(2000) DEFAULT NULL COMMENT '报表程序要执行的sql所需的参数(须与sql中参数一一对应，以“,”分开)',
  `exportFields` varchar(2000) DEFAULT NULL COMMENT '要导出为excel的字段(以“,”号分开)，须与sql中的字段对应，须与excelTitles名称对应',
  `excelTitles` varchar(2000) DEFAULT NULL COMMENT '导出excel的列头名称(以“,”号分开)，注意要与sql中的字段顺序对应',
  `runnerIp` varchar(15) DEFAULT NULL COMMENT '执行该excel统计服务器IP',
  `dbId` varchar(50) DEFAULT NULL COMMENT '执行sql的DBID',
  `type` varchar(200) DEFAULT NULL COMMENT '统计excel生成类别',
  `excelSaveRootPath` varchar(1000) DEFAULT NULL COMMENT '生成的excel存放位置根路径--通常规则服务器路径加type 注意最后一级目录不要加“/”',
  `reMarks` varchar(300) DEFAULT NULL COMMENT '说明',
  `runCycle` varchar(50) DEFAULT NULL COMMENT '运行周期（多长时间执行一次）',
  `lastRunTime` datetime DEFAULT NULL COMMENT '最后一次的执行时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of Z_excelTaskDefined
-- ----------------------------
