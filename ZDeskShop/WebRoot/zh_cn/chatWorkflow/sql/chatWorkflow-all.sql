
-- -----------------------------
-- 分类表
-- -----------------------------
DROP TABLE IF EXISTS `workflow_category`;
CREATE TABLE `workflow_category` (
  `id` bigint(36) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `categoryName` varchar(200) DEFAULT NULL COMMENT '分类名称',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `createPeople` varchar(200) DEFAULT NULL COMMENT '创建人',
  `isDelete` varchar(50) DEFAULT NULL COMMENT '是否启用',
  `companyId` varchar(255) DEFAULT NULL COMMENT 'companyId',
  PRIMARY KEY (`id`),
  KEY `createDate` (`createDate`),
  KEY `categoryName` (`categoryName`),
  KEY `createPeople` (`createPeople`),
  KEY `isDelete` (`isDelete`)
) DEFAULT CHARSET=utf8;

-- ----------------------------
-- 模板表
-- ----------------------------
DROP TABLE IF EXISTS `wx_category`;
CREATE TABLE `wx_category` (
  `id` bigint(36) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `categoryName` varchar(200) DEFAULT NULL COMMENT '模板名称',
  `categoryDesc` varchar(3000) DEFAULT NULL COMMENT '模板描述',
  `skillType` varchar(50) DEFAULT '' COMMENT '技能组类型',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `createPeople` varchar(200) DEFAULT NULL COMMENT '创建人',
  `isDelete` varchar(50) DEFAULT NULL COMMENT '是否启用',
  `partId` varchar(255) DEFAULT NULL COMMENT '分类id',
  `companyId` varchar(255) DEFAULT NULL COMMENT 'companyId',
  PRIMARY KEY (`id`),
  KEY `createDate` (`createDate`),
  KEY `categoryName` (`categoryName`),
  KEY `createPeople` (`createPeople`),
  KEY `isDelete` (`isDelete`)
) DEFAULT CHARSET=utf8;


-- ---------------------------
-- 分类模板授权
-- ---------------------------
DROP TABLE IF EXISTS `wx_categoryPrivilege`;
CREATE TABLE `wx_categoryPrivilege` (
  `id` bigint(36) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `categoryId` bigint(36) DEFAULT NULL COMMENT '分类id',
  `roleCode` varchar(200) DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;
-- ---------------------------
-- 工作流表
-- ---------------------------
DROP TABLE IF EXISTS `Workflow_Table`;
CREATE TABLE `Workflow_Table` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '工作流数据表主键，也是工作流编号',
  `workflow_name` varchar(100) NOT NULL DEFAULT '' COMMENT '工作流名称',
  `woow_chat_id` varchar(64) NOT NULL DEFAULT '' COMMENT '聊天窗口标识',
  `workflow_describe` varchar(1000) DEFAULT '' COMMENT '工作流描述',
  `workflow_sort` varchar(100) DEFAULT '' COMMENT '工作流分类',
  `workflow_sort_describe` varchar(1000) DEFAULT '' COMMENT '工作流分类描述',
  `check_invite` varchar(10) DEFAULT '' COMMENT '是否可邀请(是/否)',
  `workflow_state` varchar(20) DEFAULT '' COMMENT '工作流状态',
  `workflow_finish_time` datetime DEFAULT NULL COMMENT '工作流完成时间',
  `skId` int(20) DEFAULT NULL COMMENT '技能组id',
  `skName` varchar(50) DEFAULT '' COMMENT '技能组名',
  `skcode` varchar(50) DEFAULT '' COMMENT '技能组code唯一标示',
  `skType` varchar(32) DEFAULT '' COMMENT '技能组类型',
  `workflow_creater` varchar(64) DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `generate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '生成数据时间',
  PRIMARY KEY (`id`),
  KEY `workflow_name` (`workflow_name`),
  KEY `workflow_sort` (`workflow_sort`),
  KEY `workflow_state` (`workflow_state`),
  KEY `create_time` (`create_time`)
) DEFAULT CHARSET=utf8;

-- ----------------------------------
-- 工作流与用户人员之间的关联表
-- ----------------------------------
DROP TABLE IF EXISTS `Workflow_Relation_Table`;
CREATE TABLE `Workflow_Relation_Table` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `workflow_id` bigint(20) NOT NULL COMMENT '工作流编号',
  `stepId` varchar(255) DEFAULT '' COMMENT '节点id',
  `stepName` varchar(50) DEFAULT '' COMMENT '节点名称',
  `description` text COMMENT '节点描述',
  `stepLogo` varchar(20) DEFAULT '' COMMENT '节点标识',
  `cardUrl` varchar(50) NOT NULL COMMENT '门户用户id(账号)',
  `user_name` varchar(50) NOT NULL COMMENT '用户名称',
  `role` varchar(50) NOT NULL COMMENT '所属角色（参与/执行）',
  `staff_state` varchar(50) DEFAULT NULL COMMENT '人员所处状态',
  `staff_state_describe` varchar(200) DEFAULT NULL COMMENT '人员所处状态描述',
  `staff_finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `generate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '生成数据时间',
  PRIMARY KEY (`id`),
  KEY `user_name` (`user_name`),
  KEY `cardUrl` (`cardUrl`),
  KEY `stepId` (`stepId`),
  KEY `stepLogo` (`stepLogo`)
) DEFAULT CHARSET=utf8;

-- --------------------
-- ZDesk用户与微信号绑定记录表
-- --------------------
DROP TABLE IF EXISTS `chatWorkflow_binding`;
CREATE TABLE `chatWorkflow_binding` (
  `id` bigint(36) NOT NULL COMMENT 'id' AUTO_INCREMENT,
  `zdeskUserId` varchar(50) DEFAULT NULL COMMENT 'ZDesk用户id',
  `zdeskUserLoginName` varchar(100) DEFAULT NULL COMMENT 'ZDesk用户账号',
  `zdeskUserName` varchar(100) DEFAULT NULL COMMENT 'ZDesk用户名称',
  `cardUrl` varchar(64) DEFAULT NULL COMMENT '微信号',
  `gender` varchar(10) DEFAULT '' COMMENT '标识男或者女'
  `currentState` varchar(100) DEFAULT NULL COMMENT '当前状态',
  `bindingTime` datetime DEFAULT NULL COMMENT '绑定时间',
  `unbundlingTime` datetime DEFAULT NULL COMMENT '解绑时间',
  `RealName` varchar(64) DEFAULT NULL COMMENT '微信用户名',
  PRIMARY KEY (`id`)
  KEY `zdeskUserLoginName` (`zdeskUserLoginName`),
  KEY `cardUrl` (`cardUrl`)
) DEFAULT CHARSET=utf8;

-- ----------------------------
-- 节点表
-- ----------------------------
DROP TABLE IF EXISTS `stepRecording`;
CREATE TABLE `stepRecording` (
  `id` varchar(64) NOT NULL,
  `templateId` bigint(20) NOT NULL COMMENT '模板id',
  `stepName` varchar(50) DEFAULT '' COMMENT '节点名称',
  `executor_name` varchar(200) DEFAULT '' COMMENT '执行人名称',
  `executor` varchar(200) DEFAULT '' COMMENT '执行人',
  `description` text COMMENT '节点描述',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `serial` int(11) DEFAULT NULL COMMENT '顺序号',
  `companyId` varchar(255) DEFAULT '' COMMENT 'companyId',
  PRIMARY KEY (`id`),
  KEY `templateId` (`templateId`),
  KEY `executor` (`executor`)
) DEFAULT CHARSET=utf8;

-- ----------------------------
-- 节点管理表
-- ----------------------------

DROP TABLE IF EXISTS `stepManage`;
CREATE TABLE `stepManage` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `stepId` varchar(255)  DEFAULT NULL COMMENT '节点id',
  `stepScope` varchar(255)  DEFAULT '' COMMENT '节点范围',
  `scopeType` varchar(255)  DEFAULT '' COMMENT '范围类型',
  `companyId` varchar(255)  DEFAULT '' COMMENT 'companyId',
  PRIMARY KEY (`id`),
  KEY `stepId` (`stepId`),
  KEY `stepScope` (`stepScope`),
  KEY `scopeType` (`scopeType`)
) DEFAULT CHARSET=utf8;

-- ------------------
-- 门户工作流的树的基础数据
-- ------------------
INSERT INTO `commonTree_detail` VALUES ('BE8BAC41-D77F-D46E-27E7-3BBE7866BB4F', '门户工作流', '', 'true', 'menu', '', 'ZSKadmin', '2015-03-12 11:23:10', '', '', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail` VALUES ('EB0E8526-7693-1B3A-148A-F2F4000B3E33', '模板授权', '', 'true', 'menu', 'BE8BAC41-D77F-D46E-27E7-3BBE7866BB4F', 'ZSKadmin', '2015-03-12 11:41:23', 'chatWorkflow/special.html', '', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail` VALUES ('74FAA940-D308-74BB-9FD9-C258D416FC4B', '分类管理', '', 'true', 'menu', 'BE8BAC41-D77F-D46E-27E7-3BBE7866BB4F', 'ZSKadmin', '2015-03-12 11:44:57', 'chatWorkflow/workflow_category.html', '', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail` VALUES ('54A5F8B3-0BBB-7A99-EAB0-B57A253BB087', '节点管理', '', 'true', 'menu', 'BE8BAC41-D77F-D46E-27E7-3BBE7866BB4F', 'ZSKadmin', '2015-03-31 02:27:27', 'chatWorkflow/nodeManage.html', '', '', '', '', '', '', '', '', '', '', '', 'node');
