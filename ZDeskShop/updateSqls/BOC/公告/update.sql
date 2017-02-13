/***公告表结构***/
-- ----------------------------
-- BOC_gonggao  流程主表
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

-- ----------------------------
-- Table structure for `BOC_gonggaoHis`  流程历史表
-- ----------------------------
DROP TABLE IF EXISTS `BOC_gonggaoHis`;
CREATE TABLE `BOC_gonggaoHis` (
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
  `flowNode` varchar(200) DEFAULT '' COMMENT '处理的流程节点',
  `securityUserId` mediumtext COMMENT '授权人ID',
  `securityUser` mediumtext COMMENT '发布时授权给定用户',
  `flowId` varchar(36) DEFAULT '' COMMENT '流程标识',
  `isUpdate` varchar(10) DEFAULT NULL COMMENT '判断是否修订',
  `published` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '发布时间',
  `applyTime` datetime DEFAULT NULL COMMENT '申请时间',
  `publishArea` varchar(50) DEFAULT NULL COMMENT '发布范围',
  `endDealTime` datetime DEFAULT NULL COMMENT '最后处理时间',
  `appointedCheckTime` datetime DEFAULT NULL COMMENT '复核处理结束时间',
  `relId` varchar(36) DEFAULT NULL COMMENT '关联标识',
  `companyId` varchar(50) DEFAULT '' COMMENT '公司id',
  `companyName` varchar(200) DEFAULT '' COMMENT '公司名称',
  `departmentId` varchar(50) DEFAULT '' COMMENT '部门id',
  `departmentName` varchar(200) DEFAULT '' COMMENT '部门名称',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- 发布表  zkmAnnouncementPublished
-- ----------------------------
DROP TABLE IF EXISTS `zkmAnnouncementPublished`;
CREATE TABLE `zkmAnnouncementPublished` (
  `id` char(36) NOT NULL DEFAULT '' COMMENT '公告ID',
  `title` varchar(900) DEFAULT '' COMMENT '消息标题',
  `bDate` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '开始日期',
  `eDate` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '结束日期',
  `createUser` varchar(200) DEFAULT '' COMMENT '创建者',
  `createTime` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建日期',
  `area` varchar(500) DEFAULT '' COMMENT '地区',
  `content` varchar(4500) DEFAULT '' COMMENT '消息内容',
  `disponseOpinion` varchar(4500) DEFAULT '' COMMENT '办理意见',
  `checkOneOpinion` varchar(4500) DEFAULT '' COMMENT '复核意见',
  `basilicType` varchar(200) DEFAULT '' COMMENT '紧急程度',
  `messageState` varchar(200) DEFAULT '' COMMENT '消息状态',
  `approveState` varchar(200) DEFAULT '' COMMENT '审批状态',
  `appointedCheckUserID` varchar(100) DEFAULT '' COMMENT '指定复核人ID',
  `appointedCheckUser` varchar(200) DEFAULT '' COMMENT '指定复核人',
  `disponseUser` varchar(200) DEFAULT '' COMMENT '当前处理人',
  `disponseBTime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '处理开始时间',
  `disponseETime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '处理时间结束时间',
  `flowNode` varchar(200) DEFAULT '' COMMENT '处理的流程节点',
  `securityUserId` mediumtext COMMENT '授权人ID',
  `securityUser` mediumtext COMMENT '发布时授权给定用户',
  `isUpdate` varchar(10) DEFAULT NULL COMMENT '判断是否修订',
  `flowId` varchar(36) DEFAULT '' COMMENT '流程标识',
  `published` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '发布时间',
  `applyTime` datetime DEFAULT NULL COMMENT '申请时间',
  `publishArea` varchar(50) DEFAULT NULL COMMENT '发布范围',
  `flowState` varchar(20) DEFAULT NULL,
  `isDisable` varchar(10) DEFAULT NULL COMMENT '判断是否停用',
  `companyId` varchar(50) DEFAULT '' COMMENT '公司id',
  `companyName` varchar(200) DEFAULT '' COMMENT '公司名称',
  `departmentId` varchar(50) DEFAULT '' COMMENT '部门id',
  `departmentName` varchar(200) DEFAULT '' COMMENT '部门名称',
  `appointedCheckTime` datetime DEFAULT NULL COMMENT '复核处理结束时间',
  `endDealTime` datetime DEFAULT NULL COMMENT '最后处理时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- zkmAnnouncementSecurityUser 接收者表
-- ----------------------------
DROP TABLE IF EXISTS `zkmAnnouncementSecurityUser`;
CREATE TABLE `zkmAnnouncementSecurityUser` (
  `id` char(36) NOT NULL COMMENT '业务无关主键："uuid"',
  `loginName` varchar(20) DEFAULT NULL,
  `aid` char(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;






/***公告工作流相关数据***/
-- ----------------------------
-- 左侧权限树生成语句
-- ----------------------------
/***公告:增加左侧树型数据之前删除***/
DELETE FROM commonTree_detail WHERE ID="E9D4CD8A-FF6D-F429-A5F4-6F92193D2806";
DELETE FROM commonTree_detail WHERE ID="83A5163C-6260-82F0-58E6-16A5D0079362";
DELETE FROM commonTree_detail WHERE ID="43222752-2DBF-50DA-A1DC-50D2E5EC1030";
DELETE FROM commonTree_detail WHERE ID="DE66D104-1D5C-35C1-3B45-0DD023842EA1";


/***公告: 左侧权限树生成语句***/
INSERT INTO `commonTree_detail`(`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('E9D4CD8A-FF6D-F429-A5F4-6F92193D2806', '公告发布流程', '', 'true', 'menu', '', 'ZSKadmin', '2015-02-04 12:15:26', '', '', '', '', '', '', '', '', '', '', '', '', 'announcementProcess');
INSERT INTO `commonTree_detail`(`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('83A5163C-6260-82F0-58E6-16A5D0079362', '公告发布流程', '', 'true', 'menu', 'E9D4CD8A-FF6D-F429-A5F4-6F92193D2806', 'ZSKadmin', '2015-02-04 12:15:56', 'activitiProcessManager/commonProcess.html?PROCESSDEFINTION_ID=announcementProcess&IFRAMEPAGEHEIGHT=480&A_TASKNAME=fuhe', '', '', '', '', '', '', '', '', '', '', '', 'announcementProcess1');
INSERT INTO `commonTree_detail`(`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('43222752-2DBF-50DA-A1DC-50D2E5EC1030', '已发布公告查询', '', 'true', 'menu', 'E9D4CD8A-FF6D-F429-A5F4-6F92193D2806', 'ZSKadmin', '2015-02-04 12:16:07', 'BOC/announceSearch.html', '', '', '', '', '', '', '', '', '', '', '', 'announcementProcess2');
INSERT INTO `commonTree_detail`(`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('DE66D104-1D5C-35C1-3B45-0DD023842EA1', '公告查询', '', 'true', 'menu', 'E9D4CD8A-FF6D-F429-A5F4-6F92193D2806', 'ZSKadmin', '2015-03-16 15:49:10', 'BOC/searchAnnouncement.html', '', '', '', '', '', '', '', '', '', '', '', 'announcementProcess3');

																																																																										
-- ----------------------------
-- 工作流   插入相关数据
-- ----------------------------
/***公告工作流   插入相关数据，插入之前删除***/
DELETE FROM `activitiParamDefination` WHERE `processDefination`="announcementProcess01";
DELETE FROM `activitiParamDefination` WHERE `processDefination`="announcementProcess02";


/***公告工作流   插入相关数据***/
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('clumns', '[{\"title\":\"紧急程度\" ,data: \'basilicType\',\"width\":\'10%\'},{\"title\":\"审批状态\" ,data: \'approveState\',\"width\":\'10%\'},{\"title\":\"地区\" ,data: \'area\',\"width\":\'9%\'},{\"title\":\"创建者\" ,data: \'createUser\',\"width\":\'9%\'},{\"title\":\"创建时间\" ,data: \'createTime\',\"width\":\'15%\'},{\"title\":\"标题\" ,data: \'title\'}, {\"title\":\"公告ID\" ,data: \'id\',\"bVisible\": false}]', 'announcementProcess01');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('path', '../BOC/newAnnounce.html', 'announcementProcess01');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('tableName', 'BOC_gonggao', 'announcementProcess01');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('field', 'id,title,bDate,eDate,createUser,createTime,area,content,disponseOpinion,checkOneOpinion,basilicType,messageState,approveState,appointedCheckUserID,appointedCheckUser,disponseUser,disponseBTime,disponseETime,flowNode,flowType,securityUserId,securityUser,isUpdate,flowID,published', 'announcementProcess01');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('clumns', '[{\"title\":\"紧急程度\" ,data: \'basilicType\',\"width\":\'10%\'},{\"title\":\"审批状态\" ,data: \'approveState\',\"width\":\'10%\'},{\"title\":\"地区\" ,data: \'area\',\"width\":\'9%\'},{\"title\":\"创建者\" ,data: \'createUser\',\"width\":\'9%\'},{\"title\":\"创建时间\" ,data: \'createTime\',\"width\":\'15%\'},{\"title\":\"标题\" ,data: \'title\'}, {\"title\":\"公告ID\" ,data: \'id\',\"bVisible\": false}]', 'announcementProcess01');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('path', '../BOC/newAnnounce.html', 'announcementProcess02');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('tableName', 'BOC_gonggao', 'announcementProcess02');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('field', 'id,title,bDate,eDate,createUser,createTime,area,content,disponseOpinion,checkOneOpinion,basilicType,messageState,approveState,appointedCheckUserID,appointedCheckUser,disponseUser,disponseBTime,disponseETime,flowNode,flowType,securityUserId,securityUser,isUpdate,flowID,published', 'announcementProcess02');
/**增加公告权限****/
DELETE FROM `suSecurityPermission` WHERE `id` ="0938D876-1E03-E50D-6563-0586853425D2";
DELETE FROM `suSecurityPermission` WHERE `id` ="50500C7E-931E-1628-AF6A-5F8C54E3390F";

INSERT INTO `suSecurityPermission` (`id`, `name`, `typeName`, `modleId`, `modleName`, `funId`, `funName`, `alias1`, `alias2`) VALUES ('0938D876-1E03-E50D-6563-0586853425D2', '公告经办标识', 'base', '', '', '', '', '', '');
INSERT INTO `suSecurityPermission` (`id`, `name`, `typeName`, `modleId`, `modleName`, `funId`, `funName`, `alias1`, `alias2`) VALUES ('50500C7E-931E-1628-AF6A-5F8C54E3390F', '公告复核标识', 'base', '', '', '', '', '', '');



-- ----------------------------
-- 权限管理 给系统管理员赋左侧权限树的权限
-- ----------------------------
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="E9D4CD8A-FF6D-F429-A5F4-6F92193D2806" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="DE66D104-1D5C-35C1-3B45-0DD023842EA1" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="83A5163C-6260-82F0-58E6-16A5D0079362" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="43222752-2DBF-50DA-A1DC-50D2E5EC1030" AND roleCode="系统管理员" AND type="menu";


INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`)VALUES ('系统管理员', 'E9D4CD8A-FF6D-F429-A5F4-6F92193D2806', 'menu');
INSERT INTO `suSecurityRoleMapPermission`(`roleCode`, `permissionCode`, `type`) VALUES ('系统管理员', 'DE66D104-1D5C-35C1-3B45-0DD023842EA1', 'menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`)VALUES ('系统管理员', '83A5163C-6260-82F0-58E6-16A5D0079362', 'menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`)VALUES ('系统管理员', '43222752-2DBF-50DA-A1DC-50D2E5EC1030', 'menu');
-- ----------------------------
--  角色授权--基础权限
-- ----------------------------

/***公告：角色授权数据插入前删除***/
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="0938D876-1E03-E50D-6563-0586853425D2" AND roleCode="系统管理员" AND type="base";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="50500C7E-931E-1628-AF6A-5F8C54E3390F" AND roleCode="系统管理员" AND type="base";

/***公告：角色授权数据***/
INSERT INTO `suSecurityRoleMapPermission`(`roleCode`, `permissionCode`, `type`) VALUES ('系统管理员', '0938D876-1E03-E50D-6563-0586853425D2', 'base');
INSERT INTO `suSecurityRoleMapPermission`(`roleCode`, `permissionCode`, `type`) VALUES ('系统管理员', '50500C7E-931E-1628-AF6A-5F8C54E3390F', 'base');


-- ----------------------------
--  字典项
-- ----------------------------

/***公告：字典索引数据插入前删除***/
delete from `DicIndex` where `indexCode` = 'area';
delete from `DicIndex` where `indexCode` = 'approveState';
delete from `DicIndex` where `indexCode` = 'basilicType';
delete from `DicIndex` where `indexCode` = 'messageState';
delete from `DicIndex` where `indexCode` = 'publishArea';
/***公告：字典索引数据插入***/
INSERT INTO `DicIndex`  (`indexCode`, `indexName`, `dicInternational`) VALUES ( 'area', '地区', '');
INSERT INTO `DicIndex`  (`indexCode`, `indexName`, `dicInternational`) VALUES ('approveState', '审批状态', '');
INSERT INTO `DicIndex`  (`indexCode`, `indexName`, `dicInternational`) VALUES ('basilicType', '紧急程度', '');
INSERT INTO `DicIndex`  (`indexCode`, `indexName`, `dicInternational`) VALUES ('messageState', '消息状态', '');
INSERT INTO `DicIndex` (`indexCode`, `indexName`, `dicInternational`)  VALUES ('publishArea', '发布范围', '');

/***公告：字典数据插入前删除***/
DELETE FROM `DicData` WHERE `indexCode` ='approveState';
DELETE FROM `DicData` WHERE `indexCode` ='approveState';
DELETE FROM `DicData` WHERE `indexCode` ='area';
DELETE FROM `DicData` WHERE `indexCode` ='messageState'; 
DELETE FROM `DicData` WHERE `indexCode` ='publishArea'; 

/***公告：字典数据插入***/
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('approve', '待审批', 'approveState', '审批状态', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('pass', '通过', 'approveState', '审批状态', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('refuse', '拒绝', 'approveState', '审批状态', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('basilic', '紧急', 'basilicType', '紧急程度', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('high', '高', 'basilicType', '紧急程度', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('middle', '中', 'basilicType', '紧急程度', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('totalCenter', '总中心', 'area', '地区', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('Beijing', '北京', 'area','地区', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('haerbin', '哈尔滨', 'area', '地区', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('new', '新建', 'messageState', '消息状态', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('publish', '发布', 'messageState', '消息状态', '', '', '', '', '', '', '', '');
INSERT INTO `DicData`  (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`)VALUES ('quanxia', '全辖', 'publishArea', '发布范围', '', '', '', null, null, null, null, null);
INSERT INTO `DicData`  (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('beijingCenter', '北京中心', 'publishArea', '发布范围', '', '', '', null, null, null, null, null);
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('hefei', '合肥中心', 'publishArea', '发布范围', '', '', '', null, null, null, null, null);
INSERT INTO `DicData`  (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`)VALUES ('xian', '西安中心', 'publishArea', '发布范围', '', '', '', null, null, null, null, null);
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('beijingAndHefei', '北京中心&合肥中心', 'publishArea', '发布范围', '', '', '', null, null, null, null, null);
INSERT INTO `DicData`  (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`)VALUES ('beijingAndXian', '北京中心&西安中心', 'publishArea', '发布范围', '', '', '', null, null, null, null, null);
INSERT INTO `DicData`  (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`)VALUES ('XiabAndHefei', '西安中心&合肥中心', 'publishArea', '发布范围', '', '', '', null, null, null, null, null);
INSERT INTO `DicData`  (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`)VALUES ('AllArea', '北京中心&西安中心&合肥中心', 'publishArea', '发布范围', '', '', '', null, null, null, null, null);


/****国际化****/

/****公告的国际化数据*****/
DELETE FROM `i18nPrompt` WHERE `id` ="06A99D56-3B31-0658-1B9E-6D3DFA7300CD";
DELETE FROM `i18nPrompt` WHERE `id` ="66BF15DA-C953-90EA-5AC5-A987E8AF3F77";
DELETE FROM `i18nPrompt` WHERE `id` ="417A8096-FD29-155E-8E27-DE8C0C9BF723";
DELETE FROM `i18nPrompt` WHERE `id` ="5DDAD032-0394-BE76-347A-FB41C7F6F473";
DELETE FROM `i18nPrompt` WHERE `id` ="04DD9544-6E0A-9C5C-82E5-C44559FEFB3A";
DELETE FROM `i18nPrompt` WHERE `id` ="BBB24E60-B522-80EB-6940-24F57FE2F207";
DELETE FROM `i18nPrompt` WHERE `id` ="4FBCD1F9-A034-9038-3BC9-43A435006F2E";
DELETE FROM `i18nPrompt` WHERE `id` ="2ECFB6D9-DC38-5502-1CC4-F5004AAB8FF1";



INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`)  VALUES ('06A99D56-3B31-0658-1B9E-6D3DFA7300CD', 'BOC_approveStateIsApprove', '\\u5ba1\\u6279\\u72b6\\u6001\\u53ea\\u80fd\\u4e3a\\u5f85\\u5ba1\\u6279', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`)  VALUES ('66BF15DA-C953-90EA-5AC5-A987E8AF3F77', 'BOC_approveStateIsRefuseOrPass', '\\u5ba1\\u6279\\u72b6\\u6001\\u53ea\\u80fd\\u4e3a\\u901a\\u8fc7\\u6216\\u62d2\\u7edd', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`)  VALUES ('417A8096-FD29-155E-8E27-DE8C0C9BF723', 'BOC_TaskIDRefuse', '\\u4efb\\u52a1id\\u83b7\\u53d6\\u5931\\u8d25', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`)  VALUES ('5DDAD032-0394-BE76-347A-FB41C7F6F473', 'BOC_endTimeBigBeginTime', '\\u5931\\u6548\\u65f6\\u95f4\\u5fc5\\u987b\\u5927\\u4e8e\\u751f\\u6548\\u65f6\\u95f4', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`)  VALUES ('04DD9544-6E0A-9C5C-82E5-C44559FEFB3A', 'BOC_deleteAnnounSuccess', '\\u5220\\u9664\\u516c\\u544a\\u6210\\u529f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`)  VALUES ('BBB24E60-B522-80EB-6940-24F57FE2F207', 'BOC_AnnounceEditSuccess', '\\u516c\\u544a\\u7f16\\u8f91\\u6210\\u529f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`)  VALUES ('4FBCD1F9-A034-9038-3BC9-43A435006F2E', 'BOC_AnnounceStop', '\\u516c\\u544a\\u5df2\\u505c\\u7528', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`)  VALUES ('2ECFB6D9-DC38-5502-1CC4-F5004AAB8FF1', 'BOC_sureDeleteAnnounce', '\\u786e\\u5b9a\\u5220\\u9664\\u6b64\\u6761\\u516c\\u544a\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');

