/*
Navicat MySQL Data Transfer

Source Server         : 128
Source Server Version : 50157
Source Host           : 128.128.128.128:3306
Source Database       : securityDB

Target Server Type    : MYSQL
Target Server Version : 50157
File Encoding         : 65001

Date: 2014-08-29 10:22:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for suSecurityRole
-- ----------------------------
DROP TABLE IF EXISTS `suSecurityRole`;
CREATE TABLE `suSecurityRole` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '业务无关主键',
  `name` varchar(400) NOT NULL DEFAULT '' COMMENT '角色名称',
  `description` varchar(4000) DEFAULT '' COMMENT '角色说明',
  `viewIndex` int(11) NOT NULL DEFAULT '999999' COMMENT '角色排序',
  `alias1` varchar(100) DEFAULT '' COMMENT '预留字段1',
  `alias2` varchar(100) DEFAULT '' COMMENT '预留字段2',
  `alias3` varchar(100) DEFAULT '' COMMENT '预留字段3',
  `alias4` varchar(100) DEFAULT '' COMMENT '预留字段4',
  `alias5` varchar(100) DEFAULT '' COMMENT '预留字段5',
  `scoreWeight` varchar(64) DEFAULT '100',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM AUTO_INCREMENT=568 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of suSecurityRole
-- ----------------------------
INSERT INTO `suSecurityRole` VALUES ('393', '系统管理员', '系统管理员', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('395', '语音座席', '语音座席', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('397', '语音班长', '语音班长', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('401', '质检', '质检', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('407', '知识库经办', '知识库经办', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('411', '知识库管理员', '知识库管理员', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('413', '公告发布员', '公告发布员', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('415', '公告管理员', '公告管理员', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('417', '大屏监控', '大屏监控', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('560', '门那边v你', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('561', '0吧vv', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('452', '的发达', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('451', '打发', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('450', '第三方', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('459', '79000', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('460', 'rwerere', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('461', 'erag', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('462', 'gdgjgj', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('463', 'sdsf', 'sdf', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('464', 'llll', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('465', 'sdfdsaf', 'asdf', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('466', 'nhtbgrfv', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('467', 'nhtbgrfvk', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('468', 'gbbbb', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('469', 'kmjhfngbfv', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('470', '0000', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('471', ',mn bv', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('472', 'qqqqqq', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('473', '987yth', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('474', '0099', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('475', '234ty', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('476', 'iiiii', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('480', '56yu', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('481', '5hte', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('482', '333', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('483', '5y5', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('484', '98990', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('485', 'kjnhb', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('486', '0ouij', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('487', 'dd', 'dd', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('488', 'dfasd', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('489', 'asdf', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('490', 'gg', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('503', 'hnijmko', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('496', '000', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('497', '888', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('504', '发vf', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('562', '', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('507', '人方法', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('508', '放大放大', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('509', '仍然会', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('510', '好工夫', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('511', '纪念馆和办法v', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('512', '发生过的股份', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('513', 'ffd', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('514', 'gfgfgfgfgfgfg', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('515', '999', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('516', 'fdfadfdfd', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('517', 'jjjj', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('518', '98765', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('519', 'trffgg', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('520', 'gfffgngfngngng', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('521', 'ffgsfdsfb', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('522', 'junhgbfvdcsx', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('523', 'rrwefefearreg', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('524', 'ryegtrfd', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('525', '厄尔发二分法', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('526', '发打发打发', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('529', 'fdfdfdafaf3223232', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('530', '111222', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('531', 'gretegtgtrgtg', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('532', '22222222222222', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('533', 'scssddsds', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('534', '2vevevv', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('535', 'wwwwwwwwwwww', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('536', '111111111111', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('567', '公司该公司', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('566', 'gfgfgfg', '', '0', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('565', 'baba22', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('563', 'adsf', '', '123123', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` VALUES ('564', 'baba', '', '0', '', '', '', '', '', '100');
