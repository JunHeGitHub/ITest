-- ----------------------------
-- 增加初见质检员字段
-- ----------------------------
alter table T_GRADE_DETAIL_NEW add first_owner  varchar(20);
 
-- ----------------------------
-- 增加权重字段
-- ----------------------------
alter table dictinfo add weight  int(10) not NULL;
-- ----------------------------
-- 增加 是否涉及此项 字段
-- ----------------------------
alter table dictinfo add is_involve  int(5) not NULL;


-- ----------------------------
-- 创建初检表
-- ----------------------------
drop table chujian_remark
CREATE TABLE `chujian_remark` (
  `id` int(200) NOT NULL AUTO_INCREMENT,
  `grade_id` varchar(200) NOT NULL,
  `grade_index_id` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `grade_score` decimal(10,1) DEFAULT NULL,
  `remark_describe` text,
  PRIMARY KEY (`id`),
  KEY `grade_id` (`grade_id`),
  KEY `id` (`id`),
  KEY `grade_index_id` (`grade_index_id`)
) ENGINE=InnoDB AUTO_INCREMENT=233 DEFAULT CHARSET=utf8;

-- ----------------------------
-- 创建复检表
-- ----------------------------
drop table fujian_remark
CREATE TABLE `fujian_remark` (
  `id` int(200) NOT NULL AUTO_INCREMENT,
  `grade_id` varchar(200) NOT NULL,
  `grade_index_id` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `grade_score` decimal(10,1) DEFAULT NULL,
  `remark_describe` text,
  PRIMARY KEY (`id`),
  KEY `grade_id` (`grade_id`),
  KEY `id` (`id`),
  KEY `grade_index_id` (`grade_index_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;


-- ----------------------------
-- 创建复审表
-- ----------------------------
drop table fushen_remark
CREATE TABLE `fushen_remark` (
  `id` int(200) NOT NULL AUTO_INCREMENT,
  `grade_id` varchar(200) NOT NULL,
  `grade_index_id` varchar(50) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `grade_score` decimal(10,1) DEFAULT NULL,
  `remark_describe` text,
  PRIMARY KEY (`id`),
  KEY `grade_id` (`grade_id`),
  KEY `id` (`id`),
  KEY `grade_index_id` (`grade_index_id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8;


-- 创建教材表
CREATE TABLE `AGENT_TEACH_QUERY_ZU_GONGHAO` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `grade_id` varchar(200) NOT NULL DEFAULT '',
  `wulizu` varchar(400) DEFAULT '',
  `zuoxigonghao` varchar(400) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;


-- ----------------------------
-- 创建状态
-- ----------------------------
DROP TABLE IF EXISTS `ZQC_STATE_MANAGER`;
CREATE TABLE `ZQC_STATE_MANAGER` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stateKey` varchar(255) DEFAULT '' COMMENT '状态的key',
  `stateValue` varchar(255) DEFAULT '' COMMENT '状态值',
  `langugeCode` varchar(255) DEFAULT '' COMMENT '语言代码',
  `projectCode` varchar(255) DEFAULT '' COMMENT '项目代码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- 状态表数据
-- ----------------------------
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('1', 'zero', '0', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('2', 'chujianweifenpei', '初检未分配', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('3', 'chujianyifenpei', '初检已分配', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('4', 'chujianyifabu', '初检已发布', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('5', 'fujianweifenpei', '复检未分配', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('6', 'fujianyifenpei', '复检已分配', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('7', 'chujianyipingfen', '初检已评分', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('8', 'chujianweipingfen', '初检未评分', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('9', 'fujianyipingfen', '复检已评分', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('10', 'zuoxishangsu', '座席上诉', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('11', 'zuoxishangsuchenggong', '座席上诉成功', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('12', 'zuoxishangsushibai', '座席上诉失败', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('13', 'fujianweipingfen', '复检未评分', 'zh_cn', 'ylx');

-- 树菜单数据
delete from commonTree_detail where id='44A45BFC-8F4E-09E0-F4C9-664AE3BF9B81' ;
delete from commonTree_detail where id='93AF321E-F603-882B-5FD2-C4C8E7B2DD31' ;
delete from commonTree_detail where id='39E2D3B6-0339-C82A-6BF7-313183D5C8CD' ;
delete from commonTree_detail where id='B6389348-7A1C-2340-6882-BB30E677BDDA' ;
delete from commonTree_detail where id='EC9877AF-B36C-D20A-7A03-7533F4DD3C92' ;
delete from commonTree_detail where id='351A71C7-ABFE-9B8A-802A-4FCE31C9D60E' ;
delete from commonTree_detail where id='104E35DA-FCE6-5DFC-80F3-4347A49CDA45' ;
delete from commonTree_detail where id='F2A5DE26-FC81-C7E9-9CC2-A48409EF6FB0' ;
delete from commonTree_detail where id='88ED7441-00F8-1E8E-DC05-4E56BC19AAFC' ;
delete from commonTree_detail where id='96D107DE-3055-F8DB-F402-61210B8C53CC' ;
delete from commonTree_detail where id='8FDE8150-AD79-23DE-CB25-7EC1E7E96E01' ;
delete from commonTree_detail where id='F5219A4F-2C30-F0F7-3FCB-63ABAADAA5C9' ;
delete from commonTree_detail where id='9CEE7697-9BBF-DC46-36E5-45025C2CA1CB' ;
delete from commonTree_detail where id='23D97F5C-3A91-2FB1-0622-4A36BC7E302B' ;
delete from commonTree_detail where id='9D714E13-54BE-E732-9147-6B1C4089B36E' ;
delete from commonTree_detail where id='6E8BA776-700D-D75D-84F7-B8BEE7C193FC' ;
delete from commonTree_detail where id='1DDEDFCF-870A-F290-0416-B7E2C6883F3B' ;
delete from commonTree_detail where id='9A14942C-8058-49E6-2059-905E3F028DE8' ;
delete from commonTree_detail where id='35432152-F1A7-5C9C-0EE7-A5AA2BE233DD' ;
delete from commonTree_detail where id='C31BE179-7B5A-1C4D-C7A3-9A73AD031C1B' ;
delete from commonTree_detail where id='62DFA2CD-BCA4-A12A-18CA-CA4F3578F7A2' ;
delete from commonTree_detail where id='1D58FC5A-2CEF-2383-70E5-74C818EB7F1C' ;
delete from commonTree_detail where id='A164253C-1E9C-19BA-59B5-617BFB666E2C' ;
delete from commonTree_detail where id='FB81AF0F-D0EC-EEE3-B27D-BAC6A5119D82' ;
delete from commonTree_detail where id='D9FF1604-4A0E-C4F3-E1C2-5971AD02AFB7' ;
delete from commonTree_detail where id='F0578409-A755-44C5-03E4-C3D8E1F783DB' ;
delete from commonTree_detail where id='7C025E52-0745-E924-9A09-3AD5E0568068' ;
delete from commonTree_detail where id='39017B78-EA2A-6C04-A2EB-E182D7536DE1' ;
delete from commonTree_detail where id='A65D718F-9F85-9D07-EA93-4967570E560A' ;
delete from commonTree_detail where id='DFC6D6B4-B4D6-D060-DE27-446EEA7AF98C' ;
delete from commonTree_detail where id='BAA8488B-E784-3D3D-8FE8-490196E5FF72' ;
INSERT INTO `commonTree_detail`  VALUES ('44A45BFC-8F4E-09E0-F4C9-664AE3BF9B81', '质检', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2014-11-13 18:51:49', '', '', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail`  VALUES ('93AF321E-F603-882B-5FD2-C4C8E7B2DD31', '作业分配', '', 'true', 'menu', '44A45BFC-8F4E-09E0-F4C9-664AE3BF9B81', 'ZSKadmin', '2014-11-13 18:53:54', '', '1', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail`  VALUES ('39E2D3B6-0339-C82A-6BF7-313183D5C8CD', '文本作业分配', '', 'true', 'menu', '93AF321E-F603-882B-5FD2-C4C8E7B2DD31', 'ZSKadmin', '2014-11-13 18:54:17', 'ZQC/wenbenzuoyefenpei.html', '0', '', '', '', '', '', '', '', '', '', '', 'wenbenzuoyefenpei');
INSERT INTO `commonTree_detail`  VALUES ('B6389348-7A1C-2340-6882-BB30E677BDDA', '作业评分', '', 'true', 'menu', '44A45BFC-8F4E-09E0-F4C9-664AE3BF9B81', 'ZSKadmin', '2014-11-17 18:36:40', '', '2', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail`  VALUES ('EC9877AF-B36C-D20A-7A03-7533F4DD3C92', '文本质检员初检评分', '', 'true', 'menu', 'B6389348-7A1C-2340-6882-BB30E677BDDA', 'ZSKadmin', '2014-11-17 18:37:33', 'ZQC/wenbenchujianpingfen.html', '0', '', '', '', '', '', '', '', '', '', '', 'wenbenchujianpingfen');
INSERT INTO `commonTree_detail`  VALUES ('351A71C7-ABFE-9B8A-802A-4FCE31C9D60E', '文本质检员复检评分与分配', '', 'true', 'menu', '93AF321E-F603-882B-5FD2-C4C8E7B2DD31', 'ZSKadmin', '2014-11-18 17:27:14', 'ZQC/wenbenfujianpingfen.html', '2', '', '', '', '', '', '', '', '', '', '', 'wenbenfujianpingfen');
INSERT INTO `commonTree_detail`  VALUES ('104E35DA-FCE6-5DFC-80F3-4347A49CDA45', '文本质检员复审评分', '', 'true', 'menu', 'B6389348-7A1C-2340-6882-BB30E677BDDA', 'ZSKadmin', '2014-11-19 01:34:07', 'ZQC/wenbenfushenpingfen.html', '1', '', '', '', '', '', '', '', '', '', '', 'wenbenfushenpingfen');
INSERT INTO `commonTree_detail`  VALUES ('F2A5DE26-FC81-C7E9-9CC2-A48409EF6FB0', '状态管理', '', 'true', 'menu', '44A45BFC-8F4E-09E0-F4C9-664AE3BF9B81', 'ZSKadmin', '2014-11-19 02:15:25', 'ZQC/stateManager.html', '3', '', '', '', '', '', '', '', '', '', '', 'stateManager');
INSERT INTO `commonTree_detail`  VALUES ('88ED7441-00F8-1E8E-DC05-4E56BC19AAFC', '文本质检员复检评分', '', 'true', 'menu', 'B6389348-7A1C-2340-6882-BB30E677BDDA', 'ZSKadmin', '2014-11-19 16:08:16', 'ZQC/wenbenzhijianyuanfujianpingfen.html', '2', '', '', '', '', '', '', '', '', '', '', 'wenbenzhijianyuanfujianpingfen');
INSERT INTO `commonTree_detail`  VALUES ('96D107DE-3055-F8DB-F402-61210B8C53CC', '座席查询', '', 'true', 'menu', '44A45BFC-8F4E-09E0-F4C9-664AE3BF9B81', 'ZSKadmin', '2014-11-22 20:06:51', '', '4', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail`  VALUES ('8FDE8150-AD79-23DE-CB25-7EC1E7E96E01', '文本座席评分查询', '', 'true', 'menu', '96D107DE-3055-F8DB-F402-61210B8C53CC', 'ZSKadmin', '2014-11-22 20:07:36', 'ZQC/wenbenpingfenchaxun.html', '', '', '', '', '', '', '', '', '', '', '', 'wenbenpingfenchaxun');
INSERT INTO `commonTree_detail`  VALUES ('F5219A4F-2C30-F0F7-3FCB-63ABAADAA5C9', '数据查询', '', 'true', 'menu', '44A45BFC-8F4E-09E0-F4C9-664AE3BF9B81', 'ZSKadmin', '2014-11-22 20:07:56', '', '5', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail`  VALUES ('9CEE7697-9BBF-DC46-36E5-45025C2CA1CB', '文本明细查询', '', 'true', 'menu', 'F5219A4F-2C30-F0F7-3FCB-63ABAADAA5C9', 'ZSKadmin', '2014-11-22 20:08:55', 'ZQC/wenbenmingxichaxun.html', '', '', '', '', '', '', '', '', '', '', '', 'wenbenmingxichaxun');
INSERT INTO `commonTree_detail`  VALUES ('23D97F5C-3A91-2FB1-0622-4A36BC7E302B', '文本质检员评分查询', '', 'true', 'menu', 'F5219A4F-2C30-F0F7-3FCB-63ABAADAA5C9', 'ZSKadmin', '2014-11-22 20:09:47', 'ZQC/shujuchaxunwenbenpingfenchaxun.html', '', '', '', '', '', '', '', '', '', '', '', 'shujuchaxunwenbenpingfenchaxun');
INSERT INTO `commonTree_detail`  VALUES ('9D714E13-54BE-E732-9147-6B1C4089B36E', '语音作业分配', '', 'true', 'menu', '93AF321E-F603-882B-5FD2-C4C8E7B2DD31', 'ZSKadmin', '2014-11-23 19:33:48', 'ZQC/yuyinzuoyefenpei.html', '1', '', '', '', '', '', '', '', '', '', '', 'yuyinzuoyefenpei');
INSERT INTO `commonTree_detail`  VALUES ('6E8BA776-700D-D75D-84F7-B8BEE7C193FC', '语音质检员初检评分', '', 'true', 'menu', 'B6389348-7A1C-2340-6882-BB30E677BDDA', 'ZSKadmin', '2014-11-23 19:34:23', 'ZQC/yuyinchujianpingfen.html', '3', '', '', '', '', '', '', '', '', '', '', 'yuyinchujianpingfen');
INSERT INTO `commonTree_detail`  VALUES ('1DDEDFCF-870A-F290-0416-B7E2C6883F3B', '语音质检院复审评分', '', 'true', 'menu', 'B6389348-7A1C-2340-6882-BB30E677BDDA', 'ZSKadmin', '2014-11-23 19:35:41', 'ZQC/yuyinfushenpingfen.html', '5', '', '', '', '', '', '', '', '', '', '', 'yuyinfushenpingfen');
INSERT INTO `commonTree_detail`  VALUES ('9A14942C-8058-49E6-2059-905E3F028DE8', '语音质检员复检评分', '', 'true', 'menu', 'B6389348-7A1C-2340-6882-BB30E677BDDA', 'ZSKadmin', '2014-11-23 19:36:24', 'ZQC/yuyinzhijianyuanfujianpingfen.html', '6', '', '', '', '', '', '', '', '', '', '', 'yuyinzhijianyuanfujianpingfen');
INSERT INTO `commonTree_detail`  VALUES ('35432152-F1A7-5C9C-0EE7-A5AA2BE233DD', '质检管理', '', 'true', 'menu', '44A45BFC-8F4E-09E0-F4C9-664AE3BF9B81', 'ZSKadmin', '2014-11-19 09:35:06', '', '6', '', '', '', '', '', '', '', '', '', '', 'zj');
INSERT INTO `commonTree_detail`  VALUES ('C31BE179-7B5A-1C4D-C7A3-9A73AD031C1B', '教材目录管理', '', 'true', 'menu', '35432152-F1A7-5C9C-0EE7-A5AA2BE233DD', 'ZSKadmin', '2014-11-19 09:36:12', 'ZQC/jiaocaimuluguanli.html', '', '', '', '', '', '', '', '', '', '', '', 'zj');
INSERT INTO `commonTree_detail`  VALUES ('62DFA2CD-BCA4-A12A-18CA-CA4F3578F7A2', '教材文件查询', '', 'true', 'menu', '35432152-F1A7-5C9C-0EE7-A5AA2BE233DD', 'ZSKadmin', '2014-11-19 09:36:53', 'ZQC/jiaocaiwenjianchaxun.html', '', '', '', '', '', '', '', '', '', '', '', 'zj');
INSERT INTO `commonTree_detail`  VALUES ('1D58FC5A-2CEF-2383-70E5-74C818EB7F1C', '坐席培训查询', '', 'true', 'menu', '35432152-F1A7-5C9C-0EE7-A5AA2BE233DD', 'ZSKadmin', '2014-11-19 09:37:46', 'ZQC/zuoxipeixunchaxun.html', '', '', '', '', '', '', '', '', '', '', '', 'zj');
INSERT INTO `commonTree_detail`  VALUES ('A164253C-1E9C-19BA-59B5-617BFB666E2C', '评分指标管理', '', 'true', 'menu', '35432152-F1A7-5C9C-0EE7-A5AA2BE233DD', 'ZSKadmin', '2014-11-19 15:46:24', 'ZQC/pingfenzhibiaoliulan.html', '', '', '', '', '', '', '', '', '', '', '', 'zj');
INSERT INTO `commonTree_detail`  VALUES ('FB81AF0F-D0EC-EEE3-B27D-BAC6A5119D82', '文本座席复审查询', '', 'true', 'menu', '96D107DE-3055-F8DB-F402-61210B8C53CC', 'ZSKadmin', '2014-11-25 19:53:43', 'ZQC/wenbenfushenchaxun.html', '', '', '', '', '', '', '', '', '', '', '', 'wenbenfushenchaxun');
INSERT INTO `commonTree_detail`  VALUES ('D9FF1604-4A0E-C4F3-E1C2-5971AD02AFB7', '语音座席评分查询', '', 'true', 'menu', '96D107DE-3055-F8DB-F402-61210B8C53CC', 'ZSKadmin', '2014-11-25 19:54:15', 'ZQC/yuyinpingfenchaxun.html', '', '', '', '', '', '', '', '', '', '', '', 'yuyinpingfenchaxun');
INSERT INTO `commonTree_detail`  VALUES ('F0578409-A755-44C5-03E4-C3D8E1F783DB', '语音座席复审查询', '', 'true', 'menu', '96D107DE-3055-F8DB-F402-61210B8C53CC', 'ZSKadmin', '2014-11-25 19:54:44', 'ZQC/yuyinfushenchaxun.html', '', '', '', '', '', '', '', '', '', '', '', 'yuyinfushenchaxun');
INSERT INTO `commonTree_detail`  VALUES ('7C025E52-0745-E924-9A09-3AD5E0568068', '语音明细查询', '', 'true', 'menu', 'F5219A4F-2C30-F0F7-3FCB-63ABAADAA5C9', 'ZSKadmin', '2014-11-25 19:55:35', 'ZQC/yuyinmingxichaxun.html', '', '', '', '', '', '', '', '', '', '', '', 'yuyinmingxichaxun');
INSERT INTO `commonTree_detail`  VALUES ('39017B78-EA2A-6C04-A2EB-E182D7536DE1', '语音评分查询', '', 'true', 'menu', 'F5219A4F-2C30-F0F7-3FCB-63ABAADAA5C9', 'ZSKadmin', '2014-11-25 19:56:18', 'ZQC/shujuchaxunyuyinpingfenchaxun.html', '', '', '', '', '', '', '', '', '', '', '', 'yuyinpingfenchaxun');
INSERT INTO `commonTree_detail`  VALUES ('A65D718F-9F85-9D07-EA93-4967570E560A', '文本组内评分查询', '', 'true', 'menu', '96D107DE-3055-F8DB-F402-61210B8C53CC', 'ZSKadmin', '2014-12-02 18:11:40', 'ZQC/wulizuwenbenpingfenchaxun.html', '', '', '', '', '', '', '', '', '', '', '', 'wulizuwenbenpingfenchaxun');
INSERT INTO `commonTree_detail`  VALUES ('DFC6D6B4-B4D6-D060-DE27-446EEA7AF98C', '语音组内评分查询', '', 'true', 'menu', '96D107DE-3055-F8DB-F402-61210B8C53CC', 'ZSKadmin', '2014-12-02 18:12:19', 'ZQC/wulizuyuyinpingfenchaxun.html', '', '', '', '', '', '', '', '', '', '', '', 'wulizuyuyinpingfenchaxun');
INSERT INTO `commonTree_detail`  VALUES ('BAA8488B-E784-3D3D-8FE8-490196E5FF72', '语音质检员复检评分与分配', '', 'true', 'menu', '93AF321E-F603-882B-5FD2-C4C8E7B2DD31', 'ZSKadmin', '2015-04-02 12:33:28', 'ZQC/yuyinfujianpingfen.html', '', '', '', '', '', '', '', '', '', '', '', 'yuyinfujianpingfen');

