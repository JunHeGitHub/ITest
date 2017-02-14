DROP TABLE IF EXISTS `zkmCommonTree`;
CREATE TABLE `zkmCommonTree` (
	`id`DOUBLE NOT NULL PRIMARY KEY  AUTO_INCREMENT,
	`leftClick` VARCHAR (300),
	`rightClick` VARCHAR (300),
	`nodeId` VARCHAR (108),
	`nodeName` VARCHAR (150),
	`beanName` VARCHAR (90),
	`recordType` VARCHAR (60),
	`isSynchronous` VARCHAR (3),
	`requestUrl` VARCHAR (300),
	`type` VARCHAR (60),
	`normalNode` VARCHAR (3),
	`leftparam` VARCHAR (108),
	`rightparam` VARCHAR (108),
	`desc` VARCHAR (1500)
);
insert into `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) values(NULL,NULL,'634D7EA6-5022-F3A8-93D5-4D2B590B3E88','常见问题','','Uproblem','1','/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action',NULL,'0',NULL,NULL,NULL);
insert into `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) values(NULL,NULL,'634D7EA6-5022-F3A8-93D5-4D2B590B3E88','专题','','dv','1','/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action',NULL,'0',NULL,NULL,NULL);
insert into `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) values(NULL,NULL,'634D7EA6-5022-F3A8-93D5-4D2B590B3E88','征询问答','','dfaq','1','/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action',NULL,'0',NULL,NULL,NULL);
insert into `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) values('','','zkm_docTypeManage','文档类型','','td','1','/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action','noMenu','1',NULL,NULL,NULL);
insert into `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) values(NULL,NULL,'634D7EA6-5022-F3A8-93D5-4D2B590B3E88','知识库','','d','1','/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action',NULL,'0',NULL,NULL,NULL);
insert into `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) values(NULL,NULL,'634D7EA6-5022-F3A8-93D5-4D2B590B3E88','我的收藏','','userFavorites','1','/ZDesk/ZKM/ZKMTreeNodeClassificationAdjustments/userFavoritesNodes.action','separatePages','0',NULL,NULL,NULL);
insert into `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) values('pwin.zkm_common_left_click','CT_common_zTreeRightClick','zkm_docTypeManage','文档类型','','td','1','/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action','showMenu ','1','CT_left_click_param','CT_right_click_param',NULL);
insert into `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) values(NULL,NULL,NULL,'专题','zkmSimpleCommonTreeFilter','dv','0','/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action','zkmSecurity','1',NULL,NULL,NULL);
insert into `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) values(NULL,NULL,NULL,'征询问答','zkmSimpleCommonTreeFilter','dfaq','0','/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action','zkmSecurity','1',NULL,NULL,NULL);
insert into `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) values(NULL,NULL,NULL,'知识库','zkmSimpleCommonTreeFilter','d','0','/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action','zkmSecurity','1',NULL,NULL,NULL);
insert into `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) values(NULL,NULL,NULL,NULL,'','organization','1','/ZDesk/ZKM/ZKMCommonTree/getSuSecurityPermissionList.action','zkmYongHuXinXiChaXun','1',NULL,NULL,NULL);
insert into `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) values(NULL,'zuzhijigou_common_zTreeRightClick',NULL,NULL,'','organization','1','/ZDesk/ZKM/ZKMCommonTree/getSuSecurityPermissionList.action','zuzhijigoutreeupdate','1',NULL,'zhuzhijigou_right_click_param',NULL);
insert into `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) values(NULL,NULL,NULL,'常见问题','zkmSimpleCommonTreeFilter','Uproblem','0','/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action','zkmSecurity','1',NULL,NULL,NULL);

DELETE FROM `suSecurityPermission` WHERE `code`='99061';
INSERT INTO `suSecurityPermission` (`code`, `name`, `description`, `typeName`, `modleId`, `modleName`, `compontentId`, `compontentName`, `attrIndex`, `iconCls`, `parentIndex`, `subType`, `alias1`, `alias2`, `alias3`, `alias4`, `alias5`, `companyId`, `companyName`, `departmentId`, `departmentName`) VALUES('99061','树编辑器','','finallyMenu','','树编辑器','','','99061','','2103','rs_createTreeComponent_win','','','','','','01','客服中心',NULL,NULL);
