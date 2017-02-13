/***公告工作流相关数据***/
-- ----------------------------
-- 左侧权限树生成语句
-- ----------------------------
/***公告:增加左侧树型数据之前删除***/
DELETE FROM commonTree_detail WHERE ID="E9D4CD8A-FF6D-F429-A5F4-6F92193D2806";
DELETE FROM commonTree_detail WHERE ID="83A5163C-6260-82F0-58E6-16A5D0079362";
DELETE FROM commonTree_detail WHERE ID="43222752-2DBF-50DA-A1DC-50D2E5EC1030";
DELETE FROM commonTree_detail WHERE ID="DE66D104-1D5C-35C1-3B45-0DD023842EA1";

/***留言:增加左侧树型数据之前删除***/
DELETE FROM commonTree_detail WHERE ID="67889435-11CE-807A-3C89-78A6B40AF521";
DELETE FROM commonTree_detail WHERE ID="B7CA4290-7618-5D8A-BEAB-34DA247D5C8B";
DELETE FROM commonTree_detail WHERE ID="5C401DE4-3155-D1AE-DAC1-1F6CF678DBA5";
/****自定义模板侧树型数据之前删除***/
DELETE FROM commonTree_detail WHERE ID="5C401DE4-3155-D1AE-DAC1-1F6CF678DBD2";
DELETE FROM commonTree_detail WHERE ID="C86DC70C-0E41-BF64-1F5E-FC75456BCF95";


/***公告: 左侧权限树生成语句***/
INSERT INTO `commonTree_detail`(`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('E9D4CD8A-FF6D-F429-A5F4-6F92193D2806', '公告发布流程', '', 'true', 'menu', '', 'ZSKadmin', '2015-02-04 12:15:26', '', '', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail`(`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('83A5163C-6260-82F0-58E6-16A5D0079362', '公告发布流程', '', 'true', 'menu', 'E9D4CD8A-FF6D-F429-A5F4-6F92193D2806', 'ZSKadmin', '2015-02-04 12:15:56', 'activitiProcessManager/commonProcess.html?PROCESSDEFINTION_ID=announcementProcess&IFRAMEPAGEHEIGHT=600&A_TASKNAME=fuhe&', '', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail`(`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('43222752-2DBF-50DA-A1DC-50D2E5EC1030', '已发布公告查询', '', 'true', 'menu', 'E9D4CD8A-FF6D-F429-A5F4-6F92193D2806', 'ZSKadmin', '2015-02-04 12:16:07', 'BOC/announceSearch.html', '', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail`(`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('DE66D104-1D5C-35C1-3B45-0DD023842EA1', '公告查询', '', 'true', 'menu', 'E9D4CD8A-FF6D-F429-A5F4-6F92193D2806', 'ZSKadmin', '2015-03-16 15:49:10', 'BOC/searchAnnouncement.html', '', '', '', '', '', '', '', '', '', '', '', '');

/***留言:增加左侧树型数据***/
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('67889435-11CE-807A-3C89-78A6B40AF521', '留言', '', 'true', 'menu', '', 'ZSKadmin', '2015-01-26 10:51:33', '', '1', '', '', '', '', '', '', '', '', '', '', 'Message');

INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('B7CA4290-7618-5D8A-BEAB-34DA247D5C8B', '留言待办', '', 'true', 'menu', '67889435-11CE-807A-3C89-78A6B40AF521', 'ZSKadmin', '2015-01-19 10:30:50', 'BOC/liuyan.html?PROCESSDEFINTION_ID=Message&A_TASKNAME=review&A_FORMCLASS=MessageSelect&', '1', '', '', '', '', '', '', '', '', '', '', 'MessageWaiter');

/****自定义模板侧树型数据***/
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('5C401DE4-3155-D1AE-DAC1-1F6CF678DBD2', '留言自定义模板', '', 'true', 'menu', '', 'ZSKadmin', '2015-01-26 10:51:33', '', '1', '', '', '', '', '', '', '', '', '', '', 'Messagemoban');
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('C86DC70C-0E41-BF64-1F5E-FC75456BCF95', '留言自定义模板管理', '', 'true', 'menu', '5C401DE4-3155-D1AE-DAC1-1F6CF678DBD2', 'ZSKadmin', '2015-03-02 10:36:35', 'BOC/zidingyimobanguanli.html', '0', '', '', '', '', '', '', '', '', '', '', 'Messagemobanguanli');
																																																																											
-- ----------------------------
-- 工作流   插入相关数据
-- ----------------------------
/***公告工作流   插入相关数据，插入之前删除***/
DELETE FROM `activitiParamDefination` WHERE `processDefination`="announcementProcess";

/***留言工作流动态配置数据，插入之前删除***/
DELETE FROM `activitiParamDefination` WHERE `processDefination`="Message";
DELETE FROM `activitiParamDefination` WHERE `processDefination`="MessageOne";
DELETE FROM `activitiParamDefination` WHERE `processDefination`="MessageTwo";

/***公告工作流   插入相关数据***/
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('clumns', '[{\"title\":\"紧急程度\" ,data: \'basilicType\'},\r\n{\"title\":\"标题\" ,data: \'title\'},\r\n{\"title\":\"地区\" ,data: \'area\'},\r\n{\"title\":\"创建者\" ,data: \'createUser\'},\r\n{\"title\":\"创建时间\" ,data: \'createTime\'}, {\"title\":\"公告ID\" ,data: \'id\',\"bVisible\": false}]', 'announcementProcess01');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('path', '../BOC/newAnnounce.html', 'announcementProcess01');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('tableName', 'zkmAnnouncementDisponse', 'announcementProcess01');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('field', 'id,title,bDate,eDate,createUser,createTime,area,content,disponseOpinion,checkOneOpinion,basilicType,messageState,approveState,appointedCheckUserID,appointedCheckUser,disponseUser,disponseBTime,disponseETime,flowNode,flowType,securityUserId,securityUser,isUpdate,flowID,published', 'announcementProcess01');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('clumns', '[{\"title\":\"紧急程度\" ,data: \'basilicType\'},\r\n{\"title\":\"标题\" ,data: \'title\'},\r\n{\"title\":\"地区\" ,data: \'area\'},\r\n{\"title\":\"创建者\" ,data: \'createUser\'},\r\n{\"title\":\"创建时间\" ,data: \'createTime\'}, {\"title\":\"公告ID\" ,data: \'id\',\"bVisible\": false}]', 'announcementProcess01');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('path', '../BOC/newAnnounce.html', 'announcementProcess02');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('tableName', 'zkmAnnouncementDisponse', 'announcementProcess02');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('field', 'id,title,bDate,eDate,createUser,createTime,area,content,disponseOpinion,checkOneOpinion,basilicType,messageState,approveState,appointedCheckUserID,appointedCheckUser,disponseUser,disponseBTime,disponseETime,flowNode,flowType,securityUserId,securityUser,isUpdate,flowID,published', 'announcementProcess02');
/***留言工作流动态配置数据***/
INSERT INTO `activitiParamDefination` (`key`, `value`, `processDefination`) VALUES ('clumns', '[{\"title\":\"流程创建人\",data:\'createUser\'},\r\n{\"title\":\"流程创建时间\" ,data: \'createTime\'},\r\n{\"title\":\"节点名称\" ,data: \'nodeName\'},\r\n{\"title\":\"回复方式\" ,data: \'HFFS\'},\r\n{\"title\":\"留言类型\" ,data: \'LYLX\'}]', 'MessageOne');
INSERT INTO `activitiParamDefination` ( `key`, `value`, `processDefination`) VALUES ('path', '../BOC/liuyan/liuyan.html', 'MessageOne');
INSERT INTO `activitiParamDefination` (`key`, `value`, `processDefination`) VALUES ('tableName', 'messagePorcess', 'MessageOne');
INSERT INTO `activitiParamDefination` (`key`, `value`, `processDefination`) VALUES ('field', 'id,approveState,appointedCheckUserID,appointedCheckUser,disponseUser,disponseBTime,disponseETime,createTime,createUser,flowNode,flowId,flowType,published,dataId,companyId,companyName,departmentId,departmentName,nodeName,lastApproveState,handAdvice,reviewAdvice,endDealTime,XM,CW,SF,HFFS,GDDH,SJ,DZYJ,YWLX,NR,generateTime,inboundTime,replyTime,agentId,remark,HF,LYLX,JSZH,appointedCheckTime,MBLX', 'MessageOne');
INSERT INTO `activitiParamDefination` (`key`, `value`, `processDefination`) VALUES ('clumns', '[{\"title\":\"流程创建人\",data:\'createUser\'},\r\n{\"title\":\"流程创建时间\" ,data: \'createTime\'},\r\n{\"title\":\"节点名称\" ,data: \'nodeName\'},\r\n{\"title\":\"回复方式\" ,data: \'HFFS\'},\r\n{\"title\":\"留言类型\" ,data: \'LYLX\'}]', 'MessageTwo');
INSERT INTO `activitiParamDefination` ( `key`, `value`, `processDefination`) VALUES ('path', '../BOC/liuyan/liuyan.html', 'MessageTwo');
INSERT INTO `activitiParamDefination` (`key`, `value`, `processDefination`) VALUES ('tableName', 'messagePorcess', 'MessageTwo');
INSERT INTO `activitiParamDefination` (`key`, `value`, `processDefination`) VALUES ('field', 'id,approveState,appointedCheckUserID,appointedCheckUser,disponseUser,disponseBTime,disponseETime,createTime,createUser,flowNode,flowId,flowType,published,dataId,companyId,companyName,departmentId,departmentName,nodeName,lastApproveState,handAdvice,reviewAdvice,endDealTime,XM,CW,SF,HFFS,GDDH,SJ,DZYJ,YWLX,NR,generateTime,inboundTime,replyTime,agentId,remark,HF,LYLX,JSZH,appointedCheckTime,MBLX', 'MessageTwo');



-- ----------------------------
-- 权限管理
-- ----------------------------

/**留言：增加基本权限标识插入前删除****/
DELETE FROM `suSecurityPermission` WHERE `id` ="AD88EEF8-39C9-FB32-B0FD-AC31F029A66E";
DELETE FROM `suSecurityPermission` WHERE `id` ="559D6F5E-D7DF-99CA-C79A-76F858B8A836";

/***增加基本权限标识数据前删除***/
DELETE FROM `suSecurityPermission` WHERE `id` ="0938D876-1E03-E90D-6163-0582053425D2";
DELETE FROM `suSecurityPermission` WHERE `id` ="50500C7E-931E-1628-AF6A-5F8C54E1090F";

/**留言：增加基本权限标识****/
INSERT INTO `suSecurityPermission` (`id`, `name`, `typeName`, `modleId`, `modleName`, `funId`, `funName`, `alias1`, `alias2`) VALUES ('AD88EEF8-39C9-FB32-B0FD-AC31F029A66E', '经办', 'base', '', '', '', '', '', '');
INSERT INTO `suSecurityPermission` (`id`, `name`, `typeName`, `modleId`, `modleName`, `funId`, `funName`, `alias1`, `alias2`) VALUES ('559D6F5E-D7DF-99CA-C79A-76F858B8A836', '复核', 'base', '', '', '', '', '', '');

/***增加基本权限标识数据***/
INSERT INTO `suSecurityPermission` (`id`, `name`, `typeName`, `modleId`, `modleName`, `funId`, `funName`, `alias1`, `alias2`) VALUES ('0938D876-1E03-E90D-6163-0582053425D2', '留言经办标识', 'base', '', '', '', '', '', '');
INSERT INTO `suSecurityPermission` (`id`, `name`, `typeName`, `modleId`, `modleName`, `funId`, `funName`, `alias1`, `alias2`) VALUES ('50500C7E-931E-1628-AF6A-5F8C54E1090F', '留言复核标识', 'base', '', '', '', '', '', '');

-- ----------------------------
--  角色授权
-- ----------------------------

/***公告：角色授权数据插入前删除***/
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="67889435-11CE-807A-3C89-78A6B40AF521" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="B7CA4290-7618-5D8A-BEAB-34DA247D5C8B" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="67889435-11CE-807A-3C89-78A6B40AF521" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="B7CA4290-7618-5D8A-BEAB-34DA247D5C8B" AND roleCode="系统管理员" AND type="menu";

/***留言：角色授权数据插入前删除***/
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="67889435-11CE-807A-3C89-78A6B40AF521" AND roleCode="复核留言" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="B7CA4290-7618-5D8A-BEAB-34DA247D5C8B" AND roleCode="复核留言" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="67889435-11CE-807A-3C89-78A6B40AF521" AND roleCode="经办留言" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="B7CA4290-7618-5D8A-BEAB-34DA247D5C8B" AND roleCode="经办留言" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="5C401DE4-3155-D1AE-DAC1-1F6CF678DBA5" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE `roleCode` ="系统管理员" and `permissionCode`="559D6F5E-D7DF-99CA-C79A-76F858B8A836" AND type="base";
DELETE FROM `suSecurityRoleMapPermission` WHERE `roleCode` ="系统管理员" and `permissionCode`="AD88EEF8-39C9-FB32-B0FD-AC31F029A66E" AND type="base";

/***留言：自定义模板数据插入前删除***/
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="5C401DE4-3155-D1AE-DAC1-1F6CF678DBD2" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="C86DC70C-0E41-BF64-1F5E-FC75456BCF95" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="E5164DD3-5BA7-4C06-F306-541067A3B5D3" AND roleCode="系统管理员" AND type="menu";

/***留言：增加角色基本权限标识数据前删除***/
DELETE FROM `suSecurityRoleMapPermission` WHERE `roleCode` ="复核留言" and `permissionCode`="0938D876-1E03-E90D-6163-0582053425D2" AND type="base";
DELETE FROM `suSecurityRoleMapPermission` WHERE `roleCode` ="经办留言" and `permissionCode`="50500C7E-931E-1628-AF6A-5F8C54E1090F" AND type="base";

/***增加角色可选项卡数据前删除***/
DELETE FROM `suSecurityRoleMapPermission` WHERE `roleCode` ="复核留言" and `permissionCode`="7768698F-EE77-7DD8-D8DD-6B82B2192606" AND type="tab";
DELETE FROM `suSecurityRoleMapPermission` WHERE `roleCode` ="经办留言" and `permissionCode`="7768698F-EE77-7DD8-D8DD-6B82B2192606" AND type="tab";

/***公告：角色授权数据***/
INSERT INTO `suSecurityRoleMapPermission`(`roleCode`, `permissionCode`, `type`) VALUES ('系统管理员', '43222752-2DBF-50DA-A1DC-50D2E5EC1030', 'menu');
INSERT INTO `suSecurityRoleMapPermission`(`roleCode`, `permissionCode`, `type`) VALUES ('系统管理员', '83A5163C-6260-82F0-58E6-16A5D0079362', 'menu');
INSERT INTO `suSecurityRoleMapPermission`(`roleCode`, `permissionCode`, `type`) VALUES ('系统管理员', 'ADC58CB8-992D-2FF7-7F3F-DAAD6E4183F8', 'menu');
INSERT INTO `suSecurityRoleMapPermission`(`roleCode`, `permissionCode`, `type`) VALUES ('系统管理员', 'E9D4CD8A-FF6D-F429-A5F4-6F92193D2806', 'menu');

/***留言：增加角色基本权限标识数据***/
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`) VALUES ('经办留言', '0938D876-1E03-E90D-6163-0582053425D2', 'base');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`) VALUES ('复核留言', '50500C7E-931E-1628-AF6A-5F8C54E1090F', 'base');

/***留言：增加角色可选项卡数据***/
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`) VALUES ('复核留言', '7768698F-EE77-7DD8-D8DD-6B82B2192606', 'tab');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`) VALUES ('经办留言', '7768698F-EE77-7DD8-D8DD-6B82B2192605', 'tab');

/***留言：角色授权数据插入***/
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`,`permissionCode`,`type`) VALUES ('复核留言','67889435-11CE-807A-3C89-78A6B40AF521','menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`,`permissionCode`,`type`) VALUES ('复核留言','B7CA4290-7618-5D8A-BEAB-34DA247D5C8B','menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`,`permissionCode`,`type`) VALUES ('经办留言','67889435-11CE-807A-3C89-78A6B40AF521','menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`,`permissionCode`,`type`) VALUES ('经办留言','B7CA4290-7618-5D8A-BEAB-34DA247D5C8B','menu');
INSERT INTO `suSecurityRoleMapPermission`(`roleCode`, `permissionCode`, `type`) VALUES ('系统管理员', 'AD88EEF8-39C9-FB32-B0FD-AC31F029A66E', 'base');
INSERT INTO `suSecurityRoleMapPermission`(`roleCode`, `permissionCode`, `type`) VALUES ( '系统管理员', '559D6F5E-D7DF-99CA-C79A-76F858B8A836', 'base');

/***留言：自定义模板数据授权***/
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`,`permissionCode`,`type`) VALUES ('系统管理员','5C401DE4-3155-D1AE-DAC1-1F6CF678DBD2','menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`,`permissionCode`,`type`) VALUES ('系统管理员','C86DC70C-0E41-BF64-1F5E-FC75456BCF95','menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`,`permissionCode`,`type`) VALUES ('系统管理员','E5164DD3-5BA7-4C06-F306-541067A3B5D3','menu');

-- ----------------------------
--  字典项
-- ----------------------------



/***留言：字典索引数据插入前删除***/
DELETE FROM `DicIndex` WHERE `indexCode`='jbspState';
DELETE FROM `DicIndex` WHERE `indexCode`='fhspState';
DELETE FROM `DicIndex` WHERE `indexCode`='Message_jbspState';
DELETE FROM `DicIndex` WHERE `indexCode`='Message_fhspState';
DELETE FROM `DicIndex` WHERE `indexCode`='MessComboType';
DELETE FROM `DicIndex` WHERE `indexCode`='MessageTemType';

/***留言：字典索引数据插入***/
INSERT INTO `DicIndex` (`indexCode`, `indexName`, `dicInternational`) VALUES ('Message_jbspState', '经办审批状态', '');
INSERT INTO `DicIndex` (`indexCode`, `indexName`, `dicInternational`) VALUES ('Message_fhspState', '复核审批状态', '');
INSERT INTO `DicIndex` (`indexCode`, `indexName`, `dicInternational`) VALUES ('MessComboType', '留言业务类型', '');
INSERT INTO `DicIndex` (`indexCode`, `indexName`, `dicInternational`) VALUES ('MessageTemType', '留言模板类型', '');
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
delete from `DicData`  where `code` = 'approve';
delete from `DicData`  where `code` = 'pass';
delete from `DicData`  where `code` = 'refuse';
delete from `DicData`  where `code` = 'basilic';
delete from `DicData`  where `code` = 'high';
delete from `DicData`  where `code` = 'middle';
delete from `DicData`  where `code` = 'totalCenter';
delete from `DicData`  where `code` = 'Beijing';
delete from `DicData`  where `code` = 'haerbin';
delete from `DicData`  where `code` = 'new';
delete from `DicData`  where `code` = 'publish';
delete from `DicData`  where `code` = 'quanxia';
delete from `DicData`  where `code` = 'beijingCenter';
delete from `DicData`  where `code` = 'hefei';
delete from `DicData`  where `code` = 'xian';
delete from `DicData`  where `code` = 'beijingAndHefei';
delete from `DicData`  where `code` = 'beijingAndXian';
delete from `DicData`  where `code` = 'XiabAndHefei';
delete from `DicData`  where `code` = 'AllArea';


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







/***留言：字典数据插入***/

/***留言：字典数据插入前删除***/
DELETE FROM `DicData` WHERE `code` ='waitsp';
DELETE FROM `DicData` WHERE `code` ='over';
DELETE FROM `DicData` WHERE `code` ='send';
DELETE FROM `DicData` WHERE `code` ='rollback';
DELETE FROM `DicData` WHERE `code` ='selfFinance';
DELETE FROM `DicData` WHERE `code` ='EBank';
DELETE FROM `DicData` WHERE `code` ='comFinance';
DELETE FROM `DicData` WHERE `code` ='creditCard';
DELETE FROM `DicData` WHERE `code` ='MessageTemTypePhone';
DELETE FROM `DicData` WHERE `code` ='MessageTemTypeEmail';


/***留言：字典数据***/
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('waitsp', '待审批', 'Message_jbspState', '经办审批状态', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('over', '无需回复', 'Message_jbspState', '经办审批状态', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('send', '发送', 'Message_fhspState', '复核审批状态', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('rollback', '退回', 'Message_fhspState', '复核审批状态', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('selfFinance', '个人金融业务', 'MessComboType', '留言业务类型', '', '', '', '', '01', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('EBank', '电子银行业务', 'MessComboType', '留言业务类型', '', '', '', '', '01', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('comFinance', '公司金融业务', 'MessComboType', '留言业务类型', '', '', '', '', '01', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('creditCard', '信用卡业务', 'MessComboType', '留言业务类型', '', '', '', '', '02', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('MessageTemTypePhone', '短信', 'MessageTemType', '留言模板类型', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('MessageTemTypeEmail', '电子邮件', 'MessageTemType', '留言模板类型', '', '', '', '', '', '', '', '');



-- ----------------------------
--  用户管理
-- ----------------------------

/***公告用户***/
DELETE FROM `suSecurityUserOrg` WHERE `id` ="5BF7059B-94C5-48F2-3C89-9F58A087A451";
DELETE FROM `suSecurityUserOrg` WHERE `id` ="507D0F84-F739-7174-597A-E9A7CE54BE8E";
DELETE FROM `suSecurityUserOrg` WHERE `id` ="342A6ADF-63F7-8762-5B28-5B38F1B2392D";
INSERT INTO `suSecurityUserOrg` (`id`, `loginName`, `orgCode`) VALUES ('5BF7059B-94C5-48F2-3C89-9F58A087A451', 'admin', '33');
INSERT INTO `suSecurityUserOrg` (`id`, `loginName`, `orgCode`) VALUES ('507D0F84-F739-7174-597A-E9A7CE54BE8E', 'jb', '311');
INSERT INTO `suSecurityUserOrg` (`id`, `loginName`, `orgCode`) VALUES ('342A6ADF-63F7-8762-5B28-5B38F1B2392D', 'fh', '312');




DELETE FROM `suSecurityUserRole` WHERE `loginName` ="admin" and `roleId`="393";
DELETE FROM `suSecurityUserRole` WHERE `loginName` ="jb" and `roleId`="399";
DELETE FROM `suSecurityUserRole` WHERE `loginName` ="fh" and `roleId`="397";
INSERT INTO `suSecurityUserRole` (`loginName`, `roleId`)  VALUES ('admin', '393');
INSERT INTO `suSecurityUserRole` (`loginName`, `roleId`)  VALUES ( 'jb', '399');
INSERT INTO `suSecurityUserRole`  (`loginName`, `roleId`) VALUES ('fh', '397');



DELETE FROM `suSecurityUser` WHERE `loginName` ="jb";
DELETE FROM `suSecurityUser` WHERE `loginName` ="fh";
INSERT INTO `suSecurityUser` (`loginName`, `name`, `pwd`, `startus`, `viewIndex`, `phone_number`, `agent_level`, `agent_information`, `job_number`, `seat_number`, `createDate`, `lastLoginDate`, `lastModifyPassword`, `companyId`, `companyName`, `departmentId`, `departmentName`, `alias1`, `alias2`, `alias3`, `alias4`, `alias5`) VALUES ( 'jb', '经办', '111', '0', '999999', '123', '', '', '', '', '2015-02-04 15:40:15', '2015-02-04 15:40:15', '2015-05-20 15:41:15', '01', '', '', '', '', '', '', '', '');
INSERT INTO `suSecurityUser`  (`loginName`, `name`, `pwd`, `startus`, `viewIndex`, `phone_number`, `agent_level`, `agent_information`, `job_number`, `seat_number`, `createDate`, `lastLoginDate`, `lastModifyPassword`, `companyId`, `companyName`, `departmentId`, `departmentName`, `alias1`, `alias2`, `alias3`, `alias4`, `alias5`)VALUES ( 'fh', '复核', '111', '0', '999999', '345', '', '', '', '', '2015-02-04 15:40:56', '2015-02-04 15:40:56', '2015-05-13 11:02:52', '01', '', '', '', '', '', '', '', '');




DELETE FROM `suSecurityRole` WHERE `name` ="复核公告";
DELETE FROM `suSecurityRole` WHERE `name` ="经办公告";
DELETE FROM `suSecurityRole` WHERE `name` ="复核标识";

INSERT INTO `suSecurityRole` ( `name`, `description`, `viewIndex`, `alias1`, `alias2`, `alias3`, `alias4`, `alias5`, `scoreWeight`) VALUES ('复核公告', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` ( `name`, `description`, `viewIndex`, `alias1`, `alias2`, `alias3`, `alias4`, `alias5`, `scoreWeight`)  VALUES ('经办公告', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` ( `name`, `description`, `viewIndex`, `alias1`, `alias2`, `alias3`, `alias4`, `alias5`, `scoreWeight`) VALUES ('复核标识', '', '999999', '', '', '', '', '', '100');

/***增加角色数据前删除***/
DELETE FROM `suSecurityRole` WHERE `name` ="留言经办";
DELETE FROM `suSecurityRole` WHERE `name` ="留言复核";

/***增加角色数据***/
INSERT INTO `suSecurityRole` ( `name`, `description`, `viewIndex`, `alias1`, `alias2`, `alias3`, `alias4`, `alias5`, `scoreWeight`) VALUES ('留言经办', '', '999999', '', '', '', '', '', '100');
INSERT INTO `suSecurityRole` ( `name`, `description`, `viewIndex`, `alias1`, `alias2`, `alias3`, `alias4`, `alias5`, `scoreWeight`) VALUES ('留言复核', '', '999999', '', '', '', '', '', '100');


/***增加 留言国际化数据前删除***/
DELETE FROM `i18nPrompt` WHERE `id` ="B9671DA4-8F64-2DB8-3F4C-2773B00AB9DD";
DELETE FROM `i18nPrompt` WHERE `id` ="E4215EE8-4E23-FA52-ECD3-94E2013E4D5E";
DELETE FROM `i18nPrompt` WHERE `id` ="D8338259-18F6-C964-837E-F5CCB7C187B8";
DELETE FROM `i18nPrompt` WHERE `id` ="C7B2B470-A663-313A-E705-6F9AD2886FB1";
DELETE FROM `i18nPrompt` WHERE `id` ="75F04CD7-D6B9-EC57-CBA8-507F35453E98";
DELETE FROM `i18nPrompt` WHERE `id` ="787A18FF-E130-60C0-BB7B-DCA33ADCD8E4";
DELETE FROM `i18nPrompt` WHERE `id` ="30A88D13-D7B5-E8F3-7589-5B3511D6DA2E";
DELETE FROM `i18nPrompt` WHERE `id` ="73925DC1-F9FA-223D-4691-99757E4E3A07";
DELETE FROM `i18nPrompt` WHERE `id` ="9D71A71F-2A05-03EE-05A7-82BDC6F6101A";

/***增加 留言国际化数据***/
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`) VALUES ('B9671DA4-8F64-2DB8-3F4C-2773B00AB9DD', 'liuyanguanli_shujuhuoqushibai', '\\u6570\\u636e\\u83b7\\u53d6\\u5931\\u8d25', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`) VALUES ('E4215EE8-4E23-FA52-ECD3-94E2013E4D5E', 'liuyanguanli_meiyoudaichulideshuju', '\\u6ca1\\u6709\\u5f85\\u5904\\u7406\\u7684\\u6570\\u636e', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`) VALUES ('D8338259-18F6-C964-837E-F5CCB7C187B8', 'liuyanguanli_sorry,qingheshigongsixinxizaishiyong', '\\u5f88\\u62b1\\u6b49\\uff0c\\u8bf7\\u6838\\u5b9e\\u516c\\u53f8\\u4fe1\\u606f\\uff0c\\u518d\\u4f7f\\u7528\\u8be5\\u529f\\u80fd', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`) VALUES ('C7B2B470-A663-313A-E705-6F9AD2886FB1', 'liuyanguanli_chenggonglingquxuanzhongshujutiaoshu', '\\u9009\\u4e2d\\u6570\\u636e${0}\\u6761,\\u6210\\u529f\\u9886\\u53d6\\u6570\\u636e${1}\\u6761', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`) VALUES ('75F04CD7-D6B9-EC57-CBA8-507F35453E98', 'liuyanguanli_renwubianhaohuoqushibai', '\\u4efb\\u52a1id\\u83b7\\u53d6\\u5931\\u8d25\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`) VALUES ('787A18FF-E130-60C0-BB7B-DCA33ADCD8E4', 'liuyanguanli_huifufasongIsSuccess', '${0}\\u53d1\\u9001${1}', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`) VALUES ('30A88D13-D7B5-E8F3-7589-5B3511D6DA2E', 'liuyanguanli_tijiaoshibaijinxingbaocun', '\\u5f88\\u62b1\\u6b49,\\u63d0\\u4ea4${0}\\uff01${1}', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`) VALUES ('73925DC1-F9FA-223D-4691-99757E4E3A07', 'liuyanguanli_zidingyimobanshujjiazaishibai,qingshuaxinyemianchongshi', '\\u81ea\\u5b9a\\u4e49\\u6a21\\u677f\\u6570\\u636e\\u52a0\\u8f7d\\u5931\\u8d25,\\u8bf7\\u5237\\u65b0\\u9875\\u9762\\u91cd\\u8bd5', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`) VALUES ('9D71A71F-2A05-03EE-05A7-82BDC6F6101A', 'liuyanguanli_qingxuanzeyaolingqudeshuju', '\\u8bf7\\u9009\\u62e9\\u8981\\u9886\\u53d6\\u7684\\u6570\\u636e', '中文', 'zh_CN', '英立讯', 'YLX');





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

