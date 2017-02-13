
-- ----------------------------
-- 左侧权限树生成语句
-- ----------------------------

/***留言:增加左侧树型数据之前删除***/
DELETE FROM commonTree_detail WHERE ID="67889435-11CE-807A-3C89-78A6B40AF521";
DELETE FROM commonTree_detail WHERE ID="B7CA4290-7618-5D8A-BEAB-34DA247D5C8B";

/****留言:自定义模板侧树型数据之前删除***/
DELETE FROM commonTree_detail WHERE ID="5C401DE4-3155-D1AE-DAC1-1F6CF678DBD2";
DELETE FROM commonTree_detail WHERE ID="C86DC70C-0E41-BF64-1F5E-FC75456BCF95";


/***留言:增加左侧树型数据***/
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('67889435-11CE-807A-3C89-78A6B40AF521', '留言', '', 'true', 'menu', '', 'ZSKadmin', '2015-01-26 10:51:33', '', '1', '', '', '', '', '', '', '', '', '', '', 'Message');
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('B7CA4290-7618-5D8A-BEAB-34DA247D5C8B', '留言待办', '', 'true', 'menu', '67889435-11CE-807A-3C89-78A6B40AF521', 'ZSKadmin', '2015-01-19 10:30:50', 'BOC/liuyan.html?PROCESSDEFINTION_ID=Message&A_TASKNAME=review&A_FORMCLASS=MessageSelect&', '1', '', '', '', '', '', '', '', '', '', '', 'MessageWaiter');

/****自定义模板侧树型数据***/
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('5C401DE4-3155-D1AE-DAC1-1F6CF678DBD2', '留言自定义模板', '', 'true', 'menu', '', 'ZSKadmin', '2015-01-26 10:51:33', '', '1', '', '', '', '', '', '', '', '', '', '', 'Messagemoban');
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('C86DC70C-0E41-BF64-1F5E-FC75456BCF95', '留言自定义模板管理', '', 'true', 'menu', '5C401DE4-3155-D1AE-DAC1-1F6CF678DBD2', 'ZSKadmin', '2015-03-02 10:36:35', 'BOC/zidingyimobanguanli.html', '0', '', '', '', '', '', '', '', '', '', '', 'Messagemobanguanli');
																																																																											
-- ----------------------------
-- 工作流   插入相关数据
-- ----------------------------

/***留言工作流动态配置数据，插入之前删除***/
DELETE FROM `activitiParamDefination` WHERE `processDefination`="MessageOne";
DELETE FROM `activitiParamDefination` WHERE `processDefination`="MessageTwo";

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
DELETE FROM `suSecurityPermission` WHERE `id` ="0938D876-1E03-E90D-6163-0582053425D2";
DELETE FROM `suSecurityPermission` WHERE `id` ="50500C7E-931E-1628-AF6A-5F8C54E1090F";

/**留言：增加基本权限标识****/
INSERT INTO `suSecurityPermission` (`id`, `name`, `typeName`, `modleId`, `modleName`, `funId`, `funName`, `alias1`, `alias2`) VALUES ('0938D876-1E03-E90D-6163-0582053425D2', '留言经办标识', 'base', '', '', '', '', '', '');
INSERT INTO `suSecurityPermission` (`id`, `name`, `typeName`, `modleId`, `modleName`, `funId`, `funName`, `alias1`, `alias2`) VALUES ('50500C7E-931E-1628-AF6A-5F8C54E1090F', '留言复核标识', 'base', '', '', '', '', '', '');

-- ----------------------------
--  字典项
-- ----------------------------



/***留言：字典索引数据插入前删除***/
DELETE FROM `DicIndex` WHERE `indexCode`='Message_jbspState';
DELETE FROM `DicIndex` WHERE `indexCode`='Message_fhspState';
DELETE FROM `DicIndex` WHERE `indexCode`='MessComboType';
DELETE FROM `DicIndex` WHERE `indexCode`='MessageTemType';

/***留言：字典索引数据插入***/
INSERT INTO `DicIndex` (`indexCode`, `indexName`, `dicInternational`) VALUES ('Message_jbspState', '经办审批状态', '');
INSERT INTO `DicIndex` (`indexCode`, `indexName`, `dicInternational`) VALUES ('Message_fhspState', '复核审批状态', '');
INSERT INTO `DicIndex` (`indexCode`, `indexName`, `dicInternational`) VALUES ('MessComboType', '留言业务类型', '');
INSERT INTO `DicIndex` (`indexCode`, `indexName`, `dicInternational`) VALUES ('MessageTemType', '留言模板类型', '');


/***留言：字典数据插入前删除***/
DELETE FROM `DicData` WHERE `indexCode` ='MessageTemType';
DELETE FROM `DicData` WHERE `indexCode` ='Message_jbspState';
DELETE FROM `DicData` WHERE `indexCode` ='Message_fhspState';
DELETE FROM `DicData` WHERE `indexCode` ='MessComboType'; 

/***留言：字典数据插入***/
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('Message_waitsp', '待审批', 'Message_jbspState', '经办审批状态', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('Message_over', '无需回复', 'Message_jbspState', '经办审批状态', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('Message_send', '发送', 'Message_fhspState', '复核审批状态', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('Message_rollback', '退回', 'Message_fhspState', '复核审批状态', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('Message_selfFinance', '个人金融业务', 'MessComboType', '留言业务类型', '', '', '', '', '01', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('Message_EBank', '电子银行业务', 'MessComboType', '留言业务类型', '', '', '', '', '01', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('Message_comFinance', '公司金融业务', 'MessComboType', '留言业务类型', '', '', '', '', '01', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('Message_creditCard', '信用卡业务', 'MessComboType', '留言业务类型', '', '', '', '', '02', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('MessageTemTypePhone', '短信', 'MessageTemType', '留言模板类型', '', '', '', '', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`, `sortColums`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES ('MessageTemTypeEmail', '电子邮件', 'MessageTemType', '留言模板类型', '', '', '', '', '', '', '', '');


-- ----------------------------
--  留言国际化
-- ----------------------------


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


-- ----------------------------
-- 权限管理
-- ----------------------------
/***留言：左侧权限树的权限数据插入前删除***/
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="67889435-11CE-807A-3C89-78A6B40AF521" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="B7CA4290-7618-5D8A-BEAB-34DA247D5C8B" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="5C401DE4-3155-D1AE-DAC1-1F6CF678DBD2" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="C86DC70C-0E41-BF64-1F5E-FC75456BCF95" AND roleCode="系统管理员" AND type="menu";

/***留言：左侧权限树的权限数据插入***/
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`)VALUES ('系统管理员', '67889435-11CE-807A-3C89-78A6B40AF521', 'menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`)VALUES ('系统管理员', 'B7CA4290-7618-5D8A-BEAB-34DA247D5C8B', 'menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`)VALUES ('系统管理员', '5C401DE4-3155-D1AE-DAC1-1F6CF678DBD2', 'menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`)VALUES ('系统管理员', 'C86DC70C-0E41-BF64-1F5E-FC75456BCF95', 'menu');


/***留言：角色授权-基础权限数据插入前删除***/
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="50500C7E-931E-1628-AF6A-5F8C54E1090F" AND roleCode="系统管理员" AND type="base";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="0938D876-1E03-E90D-6163-0582053425D2" AND roleCode="系统管理员" AND type="base";

/***留言：角色授权-基础权限插入数据***/
INSERT INTO `suSecurityRoleMapPermission`(`roleCode`, `permissionCode`, `type`) VALUES ('系统管理员', '50500C7E-931E-1628-AF6A-5F8C54E1090F', 'base');
INSERT INTO `suSecurityRoleMapPermission`(`roleCode`, `permissionCode`, `type`) VALUES ('系统管理员', '0938D876-1E03-E90D-6163-0582053425D2', 'base');


