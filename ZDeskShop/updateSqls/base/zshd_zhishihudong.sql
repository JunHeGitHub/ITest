-- ----------------------------
-- 问题表
-- ----------------------------
DROP TABLE IF EXISTS `zshd_question`;
CREATE TABLE `zshd_question` (
  `id` char(36) NOT NULL COMMENT '非业务主键',
  `questionId` varchar(20) DEFAULT NULL COMMENT '问题序号',
  `title` varchar(300) DEFAULT NULL COMMENT '标题',
  `loginName` varchar(20) DEFAULT NULL COMMENT '登录名',
  `userName` varchar(20) DEFAULT NULL COMMENT '建议者姓名',
  `suggestTime` datetime DEFAULT NULL COMMENT '建议时间',
  `suggestChannel` varchar(20) DEFAULT NULL COMMENT '建议渠道',
  `placeGroup` varchar(20) DEFAULT NULL COMMENT '所在专组',
  `classification` varchar(50) DEFAULT NULL COMMENT '业务分类',
  `suggestContent` varchar(3000) DEFAULT NULL COMMENT '建议/工单内容',
  `orderNumber` varchar(60) DEFAULT NULL COMMENT '工单编号',
  `principal` varchar(20) DEFAULT NULL COMMENT '负责人',
  `transpond` varchar(10) DEFAULT NULL COMMENT '是否转发',
  `dealSchedule` varchar(20) DEFAULT NULL COMMENT '处理进度',
  `answerContent` varchar(3000) DEFAULT NULL COMMENT '答复内容',
  `answerTime` datetime DEFAULT NULL COMMENT '答复日期',
  `dept` varchar(20) DEFAULT NULL COMMENT '分行/总行部门',
  `postTime` datetime DEFAULT NULL COMMENT '发送时间',
  `postContent` varchar(3000) DEFAULT NULL COMMENT '发送内容',
  `questionNum` varchar(300) DEFAULT NULL COMMENT '问题数量',
  `cuiBanTime` datetime DEFAULT NULL COMMENT '催办日期',
  `cuiBanWay` varchar(20) DEFAULT NULL COMMENT '催办方式',
  `replayTime` datetime DEFAULT NULL COMMENT '回复时间',
  `replayContent` varchar(3000) DEFAULT NULL COMMENT '回复内容及处理结果',
  `timelyReplay` varchar(10) DEFAULT NULL COMMENT '是否及时回复',
  `other` varchar(3000) DEFAULT NULL COMMENT '其他说明',
  `cilckNum` int(30) DEFAULT NULL COMMENT '点击数',
  `attentionNum` int(30) DEFAULT NULL COMMENT '关注数',
  `companyId` varchar(50) DEFAULT NULL COMMENT '公司ID',
  `companyName` varchar(200) DEFAULT NULL COMMENT '公司名称',
  `departmentId` varchar(50) DEFAULT NULL COMMENT '部门ID',
  `departmentName` varchar(200) DEFAULT NULL COMMENT '部门名称',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


-- ----------------------------
-- 关注问题表
-- ----------------------------
DROP TABLE IF EXISTS `zshd_attentionQuestion`;
CREATE TABLE `zshd_attentionQuestion` (
  `id` char(36) NOT NULL COMMENT '非业务主键',
  `questionId` varchar(20) DEFAULT NULL COMMENT '问题序号',
  `attentionName` varchar(20) DEFAULT NULL COMMENT '关注人',
  `title` varchar(300) DEFAULT NULL COMMENT '问题标题',
  `classification` varchar(50) DEFAULT NULL COMMENT '问题分类',
  `userName` varchar(20) DEFAULT NULL COMMENT '建议者姓名',
  `suggestTime` datetime DEFAULT NULL COMMENT '建议时间',
  `replayContent` varchar(3000) DEFAULT NULL COMMENT '问题的回复内容',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
-- ----------------------------
-- Records of attentionQuestion
-- ----------------------------

-- ----------------------------
-- 积分表
-- ----------------------------
DROP TABLE IF EXISTS `zshd_integral`;
CREATE TABLE `zshd_integral` (
  `id` char(36) NOT NULL COMMENT '非业务主键',
  `loginName` varchar(20) DEFAULT NULL COMMENT '登录名',
  `userName` varchar(20) DEFAULT NULL COMMENT '建议者姓名',
  `questionId` varchar(20) DEFAULT NULL,
  `integralNum` int(30) DEFAULT NULL COMMENT '积分数',
  `integralTime` datetime DEFAULT NULL COMMENT '积分时间',
  `companyId` varchar(50) DEFAULT NULL COMMENT '公司ID',
  `companyName` varchar(200) DEFAULT NULL COMMENT '公司名称',
  `departmentId` varchar(50) DEFAULT NULL COMMENT '部门ID',
  `departmentName` varchar(200) DEFAULT NULL COMMENT '部门名称',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of integral
-- ----------------------------



-- ----------------------------
-- 积分统计表
-- ----------------------------
DROP TABLE IF EXISTS `zshd_integralStatistics`;
CREATE TABLE `zshd_integralStatistics` (
  `id` char(36) NOT NULL COMMENT '主键',
  `loginName` varchar(20) NOT NULL COMMENT '录登名',
  `userName` varchar(20) DEFAULT NULL COMMENT '户用真实姓名',
  `january` int(30) DEFAULT NULL COMMENT '一月',
  `february` int(30) DEFAULT NULL COMMENT '二月',
  `march` int(30) DEFAULT NULL COMMENT '三月',
  `april` int(30) DEFAULT NULL COMMENT '四月',
  `may` int(30) DEFAULT NULL COMMENT '五月',
  `june` int(30) DEFAULT NULL COMMENT '六月',
  `july` int(30) DEFAULT NULL COMMENT '七月',
  `august` int(30) DEFAULT NULL COMMENT '八月',
  `september` int(30) DEFAULT NULL COMMENT '九月',
  `october` int(30) DEFAULT NULL COMMENT '十月',
  `november` int(30) DEFAULT NULL COMMENT '十一月',
  `december` int(30) DEFAULT NULL COMMENT '十二月',
  `companyId` varchar(50) DEFAULT NULL COMMENT '公司ID',
  `companyName` varchar(200) DEFAULT NULL COMMENT '公司名称',
  `departmentId` varchar(50) DEFAULT NULL COMMENT '门部ID',
  `departmentName` varchar(200) DEFAULT NULL COMMENT '门部名称',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of integralStatistics
-- ----------------------------


-- ----------------------------
-- 互动回复统计表
-- ----------------------------
DROP TABLE IF EXISTS `zshd_interactReplay`;
CREATE TABLE `zshd_interactReplay` (
  `id` char(36) CHARACTER SET gbk NOT NULL COMMENT 'ID',
  `loginName` varchar(20) CHARACTER SET gbk NOT NULL COMMENT '建议者录登名',
  `userName` varchar(20) CHARACTER SET gbk DEFAULT NULL COMMENT '建议者真实姓名',
  `january` int(30) DEFAULT NULL COMMENT '一月',
  `february` int(30) DEFAULT NULL COMMENT '二月',
  `march` int(30) DEFAULT NULL COMMENT '三月',
  `april` int(30) DEFAULT NULL COMMENT '四月',
  `may` int(30) DEFAULT NULL COMMENT '五月',
  `june` int(30) DEFAULT NULL COMMENT '六月',
  `july` int(30) DEFAULT NULL COMMENT '七月',
  `august` int(30) DEFAULT NULL COMMENT '八月',
  `september` int(30) DEFAULT NULL COMMENT '九月',
  `october` int(30) DEFAULT NULL COMMENT '十月',
  `november` int(30) DEFAULT NULL COMMENT '十一月',
  `december` int(30) DEFAULT NULL COMMENT '十二月',
  `companyId` varchar(50) CHARACTER SET gbk DEFAULT NULL COMMENT '司公ID',
  `companyName` varchar(200) CHARACTER SET gbk DEFAULT NULL COMMENT '公司名称',
  `departmentId` varchar(50) CHARACTER SET gbk DEFAULT NULL COMMENT '部门ID',
  `departmentName` varchar(200) CHARACTER SET gbk DEFAULT NULL COMMENT '部门名称',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of interactReplay
-- ----------------------------

-- ----------------------------
-- 知识库  问题分类树  插入知识库分类根节点
-- ----------------------------

INSERT INTO `securityDB`.`zkmCommonTree` (`id`, `leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, 
`normalNode`, `leftparam`, `rightparam`, `desc`) VALUES ('25', NULL, NULL, '021213ED-5A8A-6298-C184-A6C5D26F1BA6', '知识库分类', '', 'd', '0', 
'/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action', 'search', NULL, NULL, NULL, '');

-- ----------------------------
-- 插入左侧菜单权限树的数据
-- ----------------------------
DELETE FROM commonTree_detail WHERE ID="F4D8FCD3-38B5-443F-74B6-CF2BC1FAC24D";
DELETE FROM commonTree_detail WHERE ID="087A43DC-8B01-2985-5BE3-995772A99591";
DELETE FROM commonTree_detail WHERE ID="11DFD79C-A393-4245-6C78-A2FBF9AAA631";
DELETE FROM commonTree_detail WHERE ID="53B6B949-6039-3DB7-24F5-65F0997B42EA";
DELETE FROM commonTree_detail WHERE ID="429BE009-0BD7-53D9-FDF6-5A97C788E825";
DELETE FROM commonTree_detail WHERE ID="9DC8E6F4-D006-4B49-F8B2-5D35FD525CA8";
DELETE FROM commonTree_detail WHERE ID="F79BD46D-C3F3-7E37-5C30-EE59A4A2D94A";
DELETE FROM commonTree_detail WHERE ID="99301AD1-DD35-6D83-7D8E-4319A50D90AE";
DELETE FROM commonTree_detail WHERE ID="6473E8A9-D561-08B3-AD89-38313241EC7C";

INSERT INTO `commonTree_detail` VALUES ('F4D8FCD3-38B5-443F-74B6-CF2BC1FAC24D', '知识互动', '', 'true', 'menu', '', 'ZSKadmin', '2014-12-03 16:05:27', '', '', '', '', '', '', '', '', '', '', '', '', 'knowledgeInteract');
INSERT INTO `commonTree_detail` VALUES ('087A43DC-8B01-2985-5BE3-995772A99591', '我的问题', '', 'true', 'menu', 'F4D8FCD3-38B5-443F-74B6-CF2BC1FAC24D', 'ZSKadmin', '2014-12-03 16:06:55', 'zshd_zhishihudong/wodewenti.html', '', '', '', '', '', '', '', '', '', '', '', 'myQuestion');
INSERT INTO `commonTree_detail` VALUES ('11DFD79C-A393-4245-6C78-A2FBF9AAA631', '我处理的问题', '', 'true', 'menu', 'F4D8FCD3-38B5-443F-74B6-CF2BC1FAC24D', 'ZSKadmin', '2014-12-03 16:07:43', 'zshd_zhishihudong/myDealQuestion.html', '', '', '', '', '', '', '', '', '', '', '', 'myDisposeQuestion');
INSERT INTO `commonTree_detail` VALUES ('53B6B949-6039-3DB7-24F5-65F0997B42EA', '我关注的问题', '', 'true', 'menu', 'F4D8FCD3-38B5-443F-74B6-CF2BC1FAC24D', 'ZSKadmin', '2014-12-03 16:08:19', 'zshd_zhishihudong/myAttentionQuestion.html', '', '', '', '', '', '', '', '', '', '', '', 'myAttentionQuestion');
INSERT INTO `commonTree_detail` VALUES ('429BE009-0BD7-53D9-FDF6-5A97C788E825', '互动问题', '', 'true', 'menu', 'F4D8FCD3-38B5-443F-74B6-CF2BC1FAC24D', 'ZSKadmin', '2014-12-03 16:08:45', 'zshd_zhishihudong/hudongwenti.html', '', '', '', '', '', '', '', '', '', '', '', 'interactQuestion');
INSERT INTO `commonTree_detail` VALUES ('9DC8E6F4-D006-4B49-F8B2-5D35FD525CA8', '高级查询', '', 'true', 'menu', 'F4D8FCD3-38B5-443F-74B6-CF2BC1FAC24D', 'ZSKadmin', '2014-12-03 16:09:35', 'zshd_zhishihudong/gaojichaxun.html', '', '', '', '', '', '', '', '', '', '', '', 'advancedQuery');
INSERT INTO `commonTree_detail` VALUES ('F79BD46D-C3F3-7E37-5C30-EE59A4A2D94A', '积分统计表', '', 'true', 'menu', 'F4D8FCD3-38B5-443F-74B6-CF2BC1FAC24D', 'ZSKadmin', '2014-12-03 16:10:06', 'zshd_zhishihudong/jifentongji.html', '', '', '', '', '', '', '', '', '', '', '', 'integralTable');
INSERT INTO `commonTree_detail` VALUES ('99301AD1-DD35-6D83-7D8E-4319A50D90AE', '互动回复统计表', '', 'true', 'menu', 'F4D8FCD3-38B5-443F-74B6-CF2BC1FAC24D', 'ZSKadmin', '2014-12-03 16:10:52', 'zshd_zhishihudong/hudonghuifutongji.html', '', '', '', '', '', '', '', '', '', '', '', 'interactReplyTable');
INSERT INTO `commonTree_detail` VALUES ('6473E8A9-D561-08B3-AD89-38313241EC7C', '积分流水查询', '', 'true', 'menu', 'F4D8FCD3-38B5-443F-74B6-CF2BC1FAC24D', 'ZSKadmin', '2014-12-03 16:11:27', 'zshd_zhishihudong/Jifenliushui.html', '', '', '', '', '', '', '', '', '', '', '', 'integralQuery');



-- ----------------------------
-- 插入国际化管理  提示内容
-- ----------------------------
DELETE FROM `i18nPrompt` WHERE `id` ="66DE8547-CB27-BD70-32C6-D76908372695";
DELETE FROM `i18nPrompt` WHERE `id` ="763902EF-3CE0-B5AD-9FC9-D401C6A64F0B";
DELETE FROM `i18nPrompt` WHERE `id` ="57C3BD92-8B88-E307-AE54-0E12BEB44587";
DELETE FROM `i18nPrompt` WHERE `id` ="4FF1714E-A5BA-C599-EA5E-5D1E3B1DA607";
DELETE FROM `i18nPrompt` WHERE `id` ="E2882A23-35D9-2CE8-5BD7-6E4C224484C2";
DELETE FROM `i18nPrompt` WHERE `id` ="995BC6AE-8EE3-29AE-A81F-76F77CDF0243";
DELETE FROM `i18nPrompt` WHERE `id` ="3CF43EB9-83C1-C679-5CC6-47BBCAC6BFEC";
DELETE FROM `i18nPrompt` WHERE `id` ="4839DD1A-9973-6A19-F303-CCAE1AA64531";
DELETE FROM `i18nPrompt` WHERE `id` ="29E05A16-446C-3C7C-E354-FF5A79697F49";
DELETE FROM `i18nPrompt` WHERE `id` ="47F12637-D443-8954-F69F-20A52E1E8B24";
DELETE FROM `i18nPrompt` WHERE `id` ="7A5965D9-640D-DB6B-A5B4-CF99953FFA92";
DELETE FROM `i18nPrompt` WHERE `id` ="EDF12D23-64D9-00AC-170D-4DB819C908E2";
DELETE FROM `i18nPrompt` WHERE `id` ="91F76042-FC21-6F2B-CFAD-A6F8CDD903F0";
DELETE FROM `i18nPrompt` WHERE `id` ="DF91D6FD-C058-3CB2-53FF-2FE1B4F4BA61";
DELETE FROM `i18nPrompt` WHERE `id` ="35ACBE19-ED3A-1153-2D0A-3B8C0485C67B";

INSERT INTO `i18nPrompt` VALUES ('66DE8547-CB27-BD70-32C6-D76908372695', 'zshd_attention', '\\u5df2\\u6dfb\\u52a0\\u5230\\u5173\\u6ce8\\u5217\\u8868', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('763902EF-3CE0-B5AD-9FC9-D401C6A64F0B', 'zshd_replyFailed', '\\u56de\\u590d\\u5931\\u8d25\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('57C3BD92-8B88-E307-AE54-0E12BEB44587', 'zshd_queryTimeInThreeMonth', '\\u67e5\\u8be2\\u65f6\\u95f4\\u6bb5\\u9700\\u5728\\u4e09\\u4e2a\\u6708\\u5185', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('4FF1714E-A5BA-C599-EA5E-5D1E3B1DA607', 'zshd_cancelAttentionFailure', '\\u53d6\\u6d88\\u5173\\u6ce8\\u5931\\u8d25', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('E2882A23-35D9-2CE8-5BD7-6E4C224484C2', 'zshd_cancelAttentionSuccess', '\\u53d6\\u6d88\\u5173\\u6ce8\\u6210\\u529f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('995BC6AE-8EE3-29AE-A81F-76F77CDF0243', 'zshd_attentionYN', '\\u60a8\\u6240\\u9009\\u7684\\u95ee\\u9898\\u4e2d\\u5b58\\u5728\\u5df2\\u5173\\u6ce8\\u8fc7\\u7684\\u95ee\\u9898', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('3CF43EB9-83C1-C679-5CC6-47BBCAC6BFEC', 'zshd_attentionY', '\\u8bf7\\u9009\\u62e9\\u60f3\\u8981\\u5173\\u6ce8\\u7684\\u95ee\\u9898\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('4839DD1A-9973-6A19-F303-CCAE1AA64531', 'zshd_CancelAttention', '\\u786e\\u5b9a\\u8981\\u6dfb\\u52a0\\u5173\\u6ce8\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('29E05A16-446C-3C7C-E354-FF5A79697F49', 'zshd_sureCancelAttention', '\\u786e\\u5b9a\\u8981\\u53d6\\u6d88\\u5173\\u6ce8\\u6b64\\u95ee\\u9898\\u5417\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('47F12637-D443-8954-F69F-20A52E1E8B24', 'zshd_inputQueryCondition', '\\u8bf7\\u8f93\\u5165\\u81f3\\u5c11\\u4e00\\u9879\\u67e5\\u8be2\\u6761\\u4ef6', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('7A5965D9-640D-DB6B-A5B4-CF99953FFA92', 'zshd_chooseQuestion', '\\u8bf7\\u9009\\u62e9\\u8981\\u64cd\\u4f5c\\u7684\\u95ee\\u9898\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('EDF12D23-64D9-00AC-170D-4DB819C908E2', 'zshd_replySuccess', '\\u56de\\u590d\\u6210\\u529f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('91F76042-FC21-6F2B-CFAD-A6F8CDD903F0', 'zshd_NoRepeatAdd', '\\u8bf7\\u4e0d\\u8981\\u91cd\\u590d\\u6dfb\\u52a0', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('DF91D6FD-C058-3CB2-53FF-2FE1B4F4BA61', 'zshd_timeInterval', '\\u5fc5\\u987b\\u586b\\u5199\\u65f6\\u95f4\\u533a\\u95f4', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('35ACBE19-ED3A-1153-2D0A-3B8C0485C67B', 'zshd_SnobigE', '\\u8d77\\u59cb\\u65f6\\u95f4\\u4e0d\\u5f97\\u5927\\u4e8e\\u7ed3\\u675f\\u65f6\\u95f4', '中文', 'zh_CN', '英立讯', 'YLX');

-- ----------------------------
-- 索引码
-- ----------------------------
delete from `DicIndex` where `indexCode` = 'bumen';
delete from `DicIndex` where `indexCode` = 'SsuggestChannel';
delete from `DicIndex` where `indexCode` = 'chulijindu';
delete from `DicIndex` where `indexCode` = 'SplaceGroup';
delete from `DicIndex` where `indexCode` = 'zhuanfa';
delete from `DicIndex` where `indexCode` = 'jishihuifu';
delete from `DicIndex` where `indexCode` = 'cuibanfangshi';

INSERT INTO `DicIndex` (`indexCode`,  `indexName`) VALUES ( 'bumen', '部门');
INSERT INTO `DicIndex` (`indexCode`,  `indexName`) VALUES ( 'SsuggestChannel', '建议渠道');
INSERT INTO `DicIndex` (`indexCode`,  `indexName`) VALUES ( 'chulijindu', '处理进度');
INSERT INTO `DicIndex` (`indexCode`,  `indexName`) VALUES ( 'SplaceGroup', '所在专组');
INSERT INTO `DicIndex` (`indexCode`,  `indexName`) VALUES ( 'zhuanfa', '是否转发');
INSERT INTO `DicIndex` (`indexCode`,  `indexName`) VALUES ( 'jishihuifu', '及时回复');
INSERT INTO `DicIndex` (`indexCode`,  `indexName`) VALUES ( 'cuibanfangshi', '催办方式');

-- ----------------------------
-- 字典项
-- ----------------------------

delete from `DicData`  where `code` = 'fenhang';
delete from `DicData`  where `code` = 'zonghang';
delete from `DicData`  where `code` = 'yijianxiang';
delete from `DicData`  where `code` = 'xitongfankui';
delete from `DicData`  where `code` = 'yiwancheng';
delete from `DicData`  where `code` = 'neibuchulizhong';
delete from `DicData`  where `code` = 'wuxuchuli';
delete from `DicData`  where `code` = 'xiaoshou';
delete from `DicData`  where `code` = 'yes';
delete from `DicData`  where `code` = 'no';
delete from `DicData`  where `code` = 'Email';
delete from `DicData`  where `code` = 'phone';
delete from `DicData`  where `code` = 'shi';
delete from `DicData`  where `code` = 'fou';

INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'fenhang', '分行', 'bumen', '部门');
INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'zonghang', '总行', 'bumen', '部门');
INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'yijianxiang', '意见箱', 'SsuggestChannel', '建议渠道');
INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'xitongfankui', '系统反馈', 'SsuggestChannel', '建议渠道');
INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'yiwancheng', '已完成', 'chulijindu', '已完成');
INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'neibuchulizhong', '内部处理中', 'chulijindu', '内部处理中');
INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'wuxuchuli', '无需处理', 'chulijindu', '无需处理');
INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'xiaoshou', '销售', 'SplaceGroup', '所在专组');
INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'yes', '是', 'zhuanfa', '是否转发');
INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'no', '否', 'zhuanfa', '是否转发');
INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'Email', '邮件', 'cuibanfangshi', '催办方式');
INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'phone', '电话', 'cuibanfangshi', '催办方式');
INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'shi', '是', 'jishihuifu', '及时回复');
INSERT INTO `DicData`  (`code`,  `name`,  `indexCode` , `indexName`)VALUES ( 'fou', '否', 'jishihuifu', '及时回复');
-- ----------------------------
-- 权限标识
-- ----------------------------

INSERT INTO `suSecurityPermission` VALUES ('342', '知识经办标识', 'base', 'textSymbol', 'ZKMDocDisponseJB', '', '', '知识经办标识', '');
INSERT INTO `suSecurityPermission` VALUES ('207', '知识库管理员标识', 'base', 'textSymbol', 'ZKMAdminSymbol', '', '', '知识库管理员标识', '');
INSERT INTO `suSecurityPermission` VALUES ('344', '知识一级复核标识', 'base', 'textSymbol', 'ZKMDocDisponseFHOne', '', '', '知识一级复核标识', '');
INSERT INTO `suSecurityPermission` VALUES ('346', '知识二级复核标识', 'base', 'textSymbol', 'ZKMDocDisponseFHTwo', '', '', '知识二级复核标识', '');