/***工单插入动态配置数据**/
DELETE FROM `activitiParamDefination` WHERE `processDefination`="BOC_gongdan01";
DELETE FROM `activitiParamDefination` WHERE `processDefination`="BOC_gongdan02";

/***工单插入动态配置数据**/
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('clumns', '[{\"title\":\"服务请求编号\",data:\'gongdanId\'},\r\n{\"title\":\"客户来电时间\" ,data: \'customerCallTime\'},\r\n{\"title\":\"呼入号码\" ,data: \'callInNum\'},\r\n{\"title\":\"服务请求级别\",data: \'gongdanLevel\'},\r\n{\"title\":\"工单状态\" ,data: \'gongdanState\'},\r\n{\"title\":\"创建人\" ,data: \'chuangjianrenId\'}]', 'BOC_gongdan01');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('path', '../BOC/gongdan.html', 'BOC_gongdan01');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('tableName', 'BOC_gongdan', 'BOC_gongdan01');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('clumns', '[{\"title\":\"服务请求编号\",data:\'gongdanId\'},\r\n{\"title\":\"客户来电时间\" ,data: \'customerCallTime\'},\r\n{\"title\":\"呼入号码\" ,data: \'callInNum\'},\r\n{\"title\":\"服务请求级别\",data: \'gongdanLevel\'},\r\n{\"title\":\"工单状态\" ,data: \'gongdanState\'},\r\n{\"title\":\"创建人\" ,data: \'chuangjianrenId\'}]', 'BOC_gongdan02');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('path', '../BOC/gongdan.html', 'BOC_gongdan02');
INSERT INTO `activitiParamDefination`(`key`, `value`, `processDefination`)  VALUES ('tableName', 'BOC_gongdan', 'BOC_gongdan02');

/***工单：增加基本权限标识数据前删除***/
DELETE FROM `suSecurityPermission` WHERE `id` ="CC724F42-96F6-4C48-35EC-952CA8902E31" or `name`="工单回复坐席标识";
DELETE FROM `suSecurityPermission` WHERE `id` ="9699989F-E99C-21EA-8B2B-3C3CC7F42F63" or `name`="工单复核标识";
DELETE FROM `suSecurityPermission` WHERE `id` ="E585C676-0859-2376-31E3-39370BAF04AB" or `name`="工单经办标识";

/**工单：增加基本权限标识****/
INSERT INTO `suSecurityPermission` (`id`, `name`, `typeName`, `modleId`, `modleName`,`companyId`,`companyName`) VALUES ('CC724F42-96F6-4C48-35EC-952CA8902E31', '工单回复坐席标识', 'base', 'gongdan', '工单', '01', '客服中心');
INSERT INTO `suSecurityPermission` (`id`, `name`, `typeName`, `modleId`, `modleName`,`companyId`,`companyName`) VALUES ('9699989F-E99C-21EA-8B2B-3C3CC7F42F63', '工单复核标识', 'base', 'gongdan', '工单', '01', '客服中心');
INSERT INTO `suSecurityPermission` (`id`, `name`, `typeName`, `modleId`, `modleName`,`companyId`,`companyName`) VALUES ('E585C676-0859-2376-31E3-39370BAF04AB', '工单经办标识', 'base', 'gongdan', '工单', '01', '客服中心');


/**工单导航树节点***/

DELETE FROM commonTree_detail WHERE ID="BBB0D82C-FFC1-EEB5-D10D-BC616CC9229B";
DELETE FROM commonTree_detail WHERE ID="A8CCD5F3-5E92-2EEB-754E-9BA942FD1516";
DELETE FROM commonTree_detail WHERE ID="0BFDF75F-8F8F-5031-AC5E-2AC7560DD669";

INSERT INTO `commonTree_detail`(`id`, `text`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `companyId`) VALUES ('BBB0D82C-FFC1-EEB5-D10D-BC616CC9229B', '工单流程', 'true', 'menu', '', 'ZSKadmin', '2015-02-04 12:15:26','01');
INSERT INTO `commonTree_detail`(`id`, `text`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`,`companyId`,`modleId`) VALUES ('A8CCD5F3-5E92-2EEB-754E-9BA942FD1516', '新建工单', 'true', 'menu', 'BBB0D82C-FFC1-EEB5-D10D-BC616CC9229B', 'ZSKadmin', '2015-02-04 12:15:26','BOC/gongdan.html?TYPE=xinjian','01','gongdanxinjian');
INSERT INTO `commonTree_detail`(`id`, `text`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`,`companyId`,`modleId`) VALUES ('0BFDF75F-8F8F-5031-AC5E-2AC7560DD669', '待办服务请求', 'true', 'menu', 'BBB0D82C-FFC1-EEB5-D10D-BC616CC9229B', 'ZSKadmin', '2015-02-04 12:15:26','activitiProcessManager/commonProcess.html?PROCESSDEFINTION_ID=BOC_gongdan&IFRAMEPAGEHEIGHT=655&','01','gongdandaiban');

/***附件****/

DELETE FROM   `DataItemAllocation`  WHERE  peizhiItem= 'BOCgongdan' and productionId='fileUpload'  
INSERT INTO `DataItemAllocation` (`peizhiItem`, `peizhiItemValue`, `desc`, `bItem`, `sItem`, `productionId`, `generateTime`) VALUES ('BOCgongdan', 'BOCgongdan', '文件上传相对路径', 'fileupload', '文件上传配置', 'fileUpload', '2015-01-07 11:21:21');
/***加字段**/
/**alter table UploadSave add fileText varchar(1000)  DEFAULT ''  **/

/*******************************************************************以下为字典**********************************************************************************/
/**证件类型*/
DELETE FROM `DicIndex` WHERE `indexCode`='paperType';
INSERT INTO `DicIndex` (`indexCode`, `indexName`) VALUES ('paperType', '证件类型');

delete from `DicData`  where `code` = '居民身份证';
delete from `DicData`  where `code` = '护照';
delete from `DicData`  where `code` = '户口薄';
delete from `DicData`  where `code` = '军人身份证';
delete from `DicData`  where `code` = '士兵证/军官证';
delete from `DicData`  where `code` = '港澳居民往来内地通行证';
delete from `DicData`  where `code` = '台湾居民往来大陆通行证';
delete from `DicData`  where `code` = '边民出入境通行证';

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('居民身份证', '居民身份证', 'paperType', '证件类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('护照', '护照', 'paperType', '证件类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('户口薄', '户口薄', 'paperType', '证件类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('军人身份证', '军人身份证', 'paperType', '证件类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('士兵证/军官证', '士兵证/军官证', 'paperType', '证件类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('港澳居民往来内地通行证', '港澳居民往来内地通行证', 'paperType', '证件类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('台湾居民往来大陆通行证', '台湾居民往来大陆通行证', 'paperType', '证件类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('边民出入境通行证', '边民出入境通行证', 'paperType', '证件类型', '0', '01','客服中心');


/**来源*/

DELETE FROM `DicIndex` WHERE `indexCode`='gongdanSource';
INSERT INTO `DicIndex` (`indexCode`, `indexName`) VALUES ('gongdanSource', '来源');


delete from `DicData`  where `code` = '文本客户';
delete from `DicData`  where `code` = '网银客户';
delete from `DicData`  where `code` = '微信客户';
delete from `DicData`  where `code` = '留言客户';
delete from `DicData`  where `code` = '语音客户';

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('文本客户', '文本客户','gongdanSource', '来源', '0', '01','客服中心');

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('网银客户', '网银客户', 'gongdanSource', '来源', '0', '01','客服中心');

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('微信客户', '微信客户', 'gongdanSource', '来源', '0', '01','客服中心');

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('留言客户', '留言客户', 'gongdanSource', '来源', '0', '01','客服中心');

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('语音客户', '语音客户', 'gongdanSource', '来源', '0', '01','客服中心');

/**工单级别*/

DELETE FROM `DicIndex` WHERE `indexCode`='gongdanLevel';
INSERT INTO `DicIndex` (`indexCode`, `indexName`) VALUES ('gongdanLevel', '服务请求级别');


delete from `DicData`  where `code` = '紧急';
delete from `DicData`  where `code` = '高';
delete from `DicData`  where `code` = '中';
delete from `DicData`  where `code` = '低';

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('紧急', '紧急','gongdanLevel', '服务请求级别', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('高', '高','gongdanLevel', '服务请求级别', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('中', '中','gongdanLevel', '服务请求级别', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('低', '低','gongdanLevel', '服务请求级别', '0', '01','客服中心');

/**所属分行*/
DELETE FROM `DicIndex` WHERE `indexCode`='belongBank';
INSERT INTO `DicIndex` (`indexCode`, `indexName`) VALUES ('belongBank', '所属分行');

delete from `DicData`  where `code` = '总行';
delete from `DicData`  where `code` = '北京分行';
delete from `DicData`  where `code` = '天津分行';
delete from `DicData`  where `code` = '河北分行';
delete from `DicData`  where `code` = '山西分行';
delete from `DicData`  where `code` = '内蒙古自治区分行';
delete from `DicData`  where `code` = '辽宁分行';
delete from `DicData`  where `code` = '吉林分行';
delete from `DicData`  where `code` = '黑龙江分行';
delete from `DicData`  where `code` = '上海市分行';
delete from `DicData`  where `code` = '江苏省分行';
delete from `DicData`  where `code` = '浙江省分行';
delete from `DicData`  where `code` = '安徽省分行';
delete from `DicData`  where `code` = '福建省分行';
delete from `DicData`  where `code` = '江西省分行';
delete from `DicData`  where `code` = '山东省分行';
delete from `DicData`  where `code` = '河南省分行';
delete from `DicData`  where `code` = '湖北省分行';
delete from `DicData`  where `code` = '湖南省分行';
delete from `DicData`  where `code` = '广东省分行';
delete from `DicData`  where `code` = '广西壮族自治区分行';
delete from `DicData`  where `code` = '海南省分行';
delete from `DicData`  where `code` = '四川省分行';
delete from `DicData`  where `code` = '贵州省分行';
delete from `DicData`  where `code` = '云南省分行';
delete from `DicData`  where `code` = '西藏自治区分行';
delete from `DicData`  where `code` = '陕西省分行';
delete from `DicData`  where `code` = '甘肃省分行';
delete from `DicData`  where `code` = '青海省分行';
delete from `DicData`  where `code` = '宁夏回族自治区分行';
delete from `DicData`  where `code` = '新疆维吾尔自治区分行';
delete from `DicData`  where `code` = '重庆市分行';
delete from `DicData`  where `code` = '深圳市分行';
delete from `DicData`  where `code` = '苏州分行';
delete from `DicData`  where `code` = '宁波市分行';
delete from `DicData`  where `code` = '济南分行';
delete from `DicData`  where `code` = '厦门分行';
delete from `DicData`  where `code` = '沈阳分行';

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('总行', '总行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('北京分行', '北京分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('天津分行', '天津分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('河北分行', '河北分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('山西分行', '山西分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('内蒙古自治区分行', '内蒙古自治区分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('辽宁分行', '辽宁分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('吉林分行', '吉林分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('黑龙江分行', '黑龙江分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('上海市分行', '上海市分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('江苏省分行', '江苏省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('浙江省分行', '浙江省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('安徽省分行', '安徽省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('福建省分行', '福建省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('江西省分行', '江西省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('山东省分行', '山东省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('河南省分行', '河南省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('湖北省分行', '湖北省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('湖南省分行', '湖南省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('广东省分行', '广东省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('广西壮族自治区分行', '广西壮族自治区分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('海南省分行', '海南省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('四川省分行', '四川省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('贵州省分行', '贵州省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('云南省分行', '云南省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('西藏自治区分行', '西藏自治区分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('陕西省分行', '陕西省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('甘肃省分行', '甘肃省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('青海省分行', '青海省分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('宁夏回族自治区分行', '宁夏回族自治区分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('新疆维吾尔自治区分行', '新疆维吾尔自治区分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('重庆市分行', '重庆市分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('深圳市分行', '深圳市分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('苏州分行', '苏州分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('宁波市分行', '宁波市分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('济南分行', '济南分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('厦门分行', '厦门分行','belongBank', '所属分行', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('沈阳分行', '沈阳分行','belongBank', '所属分行', '0', '01','客服中心');


/**渠道名称*/

DELETE FROM `DicIndex` WHERE `indexCode`='channelName';
INSERT INTO `DicIndex` (`indexCode`, `indexName`) VALUES ('channelName', '渠道名称');

delete from `DicData`  where `code` = '在线-文本';
delete from `DicData`  where `code` = '在线-留言';
delete from `DicData`  where `code` = '在线-语音';

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('在线-文本', '在线-文本','channelName', '渠道名称', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('在线-留言', '在线-留言','channelName', '渠道名称', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('在线-语音', '在线-语音','channelName', '渠道名称', '0', '01','客服中心');

/**投诉类型*/

DELETE FROM `DicIndex` WHERE `indexCode`='complainType';
INSERT INTO `DicIndex` (`indexCode`, `indexName`) VALUES ('complainType', '投诉类型');

delete from `DicData`  where `code` = '系统类';
delete from `DicData`  where `code` = '业务类';
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('系统类', '系统类','complainType', '投诉类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('业务类', '业务类','complainType', '投诉类型', '0', '01','客服中心');

/**业务类型*/

DELETE FROM `DicIndex` WHERE `indexCode`='businessType';
INSERT INTO `DicIndex` (`indexCode`, `indexName`) VALUES ('businessType', '业务类型');

delete from `DicData`  where `code` = '建议';
delete from `DicData`  where `code` = '表扬';

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('建议', '建议','businessType', '投诉类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('表扬', '表扬','businessType', '投诉类型', '0', '01','客服中心');

/**预约类型*/
DELETE FROM `DicIndex` WHERE `indexCode`='subscribeType';
INSERT INTO `DicIndex` (`indexCode`, `indexName`) VALUES ('subscribeType', '预约类型');

delete from `DicData`  where `code` = '大额取款';
delete from `DicData`  where `code` = '申购产品预约';
delete from `DicData`  where `code` = '其他预约';

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('大额取款', '大额取款','subscribeType', '预约类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('申购产品预约', '申购产品预约','subscribeType', '预约类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('其他预约', '其他预约','subscribeType', '预约类型', '0', '01','客服中心');

/**问题类型*/
DELETE FROM `DicIndex` WHERE `indexCode`='problemType';
INSERT INTO `DicIndex` (`indexCode`, `indexName`) VALUES ('problemType', '问题类型');

delete from `DicData`  where `code` = '贷记卡问题';
delete from `DicData`  where `code` = '准贷记卡问题';
delete from `DicData`  where `code` = '网银业务';
delete from `DicData`  where `code` = '电话银行业务';
delete from `DicData`  where `code` = '理财业务';
delete from `DicData`  where `code` = '资产业务';
delete from `DicData`  where `code` = '负债业务';
delete from `DicData`  where `code` = '中间业务';
delete from `DicData`  where `code` = '公司业务';

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('贷记卡问题', '贷记卡问题','problemType', '问题类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('准贷记卡问题', '准贷记卡问题','problemType', '问题类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('网银业务', '网银业务','problemType', '问题类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('电话银行业务', '电话银行业务','problemType', '问题类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('理财业务', '理财业务','problemType', '问题类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('资产业务', '资产业务','problemType', '问题类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('负债业务', '负债业务','problemType', '问题类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('中间业务', '中间业务','problemType', '问题类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('公司业务', '公司业务','problemType', '问题类型', '0', '01','客服中心');



/**业务活动类型*/
DELETE FROM `DicIndex` WHERE `indexCode`='BocBusinessAgent1';
INSERT INTO `DicIndex` (`indexCode`, `indexName`) VALUES ('BocBusinessAgent1', '业务活动类型');

DELETE FROM `DicIndex` WHERE `indexCode`='BocBusinessAgent2';
INSERT INTO `DicIndex` (`indexCode`, `indexName`) VALUES ('BocBusinessAgent2', '业务活动子类型');

delete from `DicData`  where `code` = '信用卡业务' and `indexCode`='BocBusinessAgent1';
delete from `DicData`  where `code` = '个人金融业务' and `indexCode`='BocBusinessAgent1';
delete from `DicData`  where `code` = '公司金融业务' and `indexCode`='BocBusinessAgent1';
delete from `DicData`  where `code` = '电子银行业务' and `indexCode`='BocBusinessAgent1';
delete from `DicData`  where `code` = '信用卡业务' and `indexCode`='BocBusinessAgent2';

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('信用卡业务', '信用卡业务','BocBusinessAgent1', '业务活动类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('个人金融业务', '个人金融业务','BocBusinessAgent1', '业务活动类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('公司金融业务', '公司金融业务','BocBusinessAgent1', '业务活动类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `sortColums`, `companyId`, `companyName`) 
VALUES ('电子银行业务', '电子银行业务','BocBusinessAgent1', '业务活动类型', '0', '01','客服中心');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`,`parentIndexCode`, `sortColums`, `companyId`, `companyName`) 
VALUES ('信用卡业务', '信用卡业务','problemType', '问题类型','信用卡业务', '0', '01','客服中心');


/*******************************************************************以上为字典**********************************************************************************/