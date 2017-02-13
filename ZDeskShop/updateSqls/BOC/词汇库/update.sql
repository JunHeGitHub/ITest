
-- ----------------------------
-- 左侧权限树生成语句
-- ----------------------------

/***留言:增加左侧树型数据之前删除***/
DELETE FROM commonTree_detail WHERE ID="3305D978-0C1B-3309-F6DD-76762E7F4162";

/***留言:增加左侧树型数据***/
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('3305D978-0C1B-3309-F6DD-76762E7F4162', '词汇库', '', 'true', 'menu', '', 'ZSKadmin', '2015-07-07 11:56:09', 'BOC/cihuiku.html?PROCESSDEFINTION_ID=WorksBase&A_TASKNAME=fuhe&A_FORMCLASS&WorksBaseSelect&', '', '', '', '', '', '', '', '', '', '', '', 'cihuiku');
																																																																							
-- ----------------------------
-- 工作流   插入相关数据
-- ----------------------------

/***留言工作流动态配置数据，插入之前删除***/
DELETE FROM `activitiParamDefination` WHERE `processDefination`="WorksBase";

/***留言工作流动态配置数据***/
INSERT INTO `activitiParamDefination` (`key`, `value`, `processDefination`) VALUES ('path', '../BOC/cihuiku.html', 'WorksBase');
INSERT INTO `activitiParamDefination` (`key`, `value`, `processDefination`) VALUES ('tableName', 'WorksBaseMem', 'WorksBase');
INSERT INTO `activitiParamDefination` (`key`, `value`, `processDefination`) VALUES ('field', 'id,applyType,approvalState,appointedCheckUserID,appointedCheckUser,disponseBTime,disponseETime,flowNode,flowType,flowId,published,disponseUser,savePath,worksTitle,createTime,createUser,handAdvice,reviewAdvice,note,endDealTime,appointedCheckTime,fileName,nodeName', 'WorksBase');
INSERT INTO `activitiParamDefination` (`key`, `value`, `processDefination`) VALUES ('clumns', '[{\"title\":\"申请类型\",data:\'applyType\'},\r\n{\"title\":\"词汇标题\" ,data: \'worksTitle\'},\r\n{\"title\":\"节点名称\" ,data: \'nodeName\'},\r\n{\"title\":\"经办人\" ,data: \'createUser\'},\r\n{\"title\":\"创建日期\" ,data: \'createTime\'}]', 'WorksBase');


-- ----------------------------
-- 权限管理
-- ----------------------------

/**留言：增加基本权限标识插入前删除****/
DELETE FROM `suSecurityPermission` WHERE `id` ="B9291696-338B-F52A-CE66-085507C252C7";
DELETE FROM `suSecurityPermission` WHERE `id` ="FC47F492-42E1-86DB-2363-98FC61F67028";

/**留言：增加基本权限标识****/
INSERT INTO `suSecurityPermission` (`id`, `name`, `typeName`, `modleId`, `modleName`, `funId`, `funName`, `companyId`, `companyName`, `departmentId`, `departmentName`, `alias1`, `alias2`) VALUES ('B9291696-338B-F52A-CE66-085507C252C7', '词汇库经办标识', 'base', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `suSecurityPermission` (`id`, `name`, `typeName`, `modleId`, `modleName`, `funId`, `funName`, `companyId`, `companyName`, `departmentId`, `departmentName`, `alias1`, `alias2`) VALUES ('FC47F492-42E1-86DB-2363-98FC61F67028', '词汇库复核标识', 'base', '', '', '', '', '', '', '', '', '', '');

-- ----------------------------
--  字典项
-- ----------------------------



/***留言：字典索引数据插入前删除***/
DELETE FROM `DicIndex` WHERE `indexCode`='WorksBase_fhspState';
DELETE FROM `DicIndex` WHERE `indexCode`='WorksBase_fhAdvice';

/***留言：字典索引数据插入***/
INSERT INTO `DicIndex` (`indexCode`, `indexName`, `dicInternational`) VALUES ('WorksBase_fhspState', '词汇库复核审批状态', '');
INSERT INTO `DicIndex` (`indexCode`, `indexName`, `dicInternational`) VALUES ('WorksBase_fhAdvice', '词汇库复核意见', '');


/***留言：字典数据插入前删除***/
DELETE FROM `DicData` WHERE `indexCode` ='WorksBase_fhspState';
DELETE FROM `DicData` WHERE `indexCode` ='WorksBase_fhAdvice';

/***词汇库：字典数据插入***/
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('WorksBase_Other', '其他', 'WorksBase_fhAdvice', '词汇库复核意见', '', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('WorksBase_SynonymError', '同义词错误', 'WorksBase_fhAdvice', '词汇库复核意见', '', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('WorksBase_workError', '词汇错误', 'WorksBase_fhAdvice', '词汇库复核意见', '', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('WorksBase_Pass', '通过', 'WorksBase_fhspState', '词汇库复核审批状态', '', '', '', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('WorksBase_rollback', '退回', 'WorksBase_fhspState', '词汇库复核审批状态', '', '', '', NULL, NULL, NULL, NULL, NULL);


-- ----------------------------
-- 权限管理
-- ----------------------------
/***留言：左侧权限树的权限数据插入前删除***/
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="3305D978-0C1B-3309-F6DD-76762E7F4162" AND roleCode="系统管理员" AND type="menu";

/***留言：左侧权限树的权限数据插入***/
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`)VALUES ('系统管理员', '3305D978-0C1B-3309-F6DD-76762E7F4162', 'menu');


/***留言：角色授权-基础权限数据插入前删除***/
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="B9291696-338B-F52A-CE66-085507C252C7" AND roleCode="系统管理员" AND type="base";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="FC47F492-42E1-86DB-2363-98FC61F67028" AND roleCode="系统管理员" AND type="base";

/***留言：角色授权-基础权限插入数据***/
INSERT INTO `suSecurityRoleMapPermission`(`roleCode`, `permissionCode`, `type`) VALUES ('系统管理员', 'B9291696-338B-F52A-CE66-085507C252C7', 'base');
INSERT INTO `suSecurityRoleMapPermission`(`roleCode`, `permissionCode`, `type`) VALUES ('系统管理员', 'FC47F492-42E1-86DB-2363-98FC61F67028', 'base');


