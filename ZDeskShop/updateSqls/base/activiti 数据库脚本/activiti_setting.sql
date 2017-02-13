DELETE FROM DataItemAllocation WHERE DataItemAllocation.productionId = 'activiti' ;
INSERT INTO DataItemAllocation (`peizhiItem`,`peizhiItemValue`,`desc`,`bItem`,`sItem`,`productionId`) VALUES('dbip','128.128.128.128','activiti数据库IP','database','activiti数据库配置','activiti') ;
INSERT INTO DataItemAllocation (`peizhiItem`,`peizhiItemValue`,`desc`,`bItem`,`sItem`,`productionId`) VALUES('dbo','','activiti数据库dbo','database','activiti数据库配置','activiti') ;
INSERT INTO DataItemAllocation (`peizhiItem`,`peizhiItemValue`,`desc`,`bItem`,`sItem`,`productionId`) VALUES('id','activiti','activiti数据库id','database','activiti数据库配置','activiti') ;
INSERT INTO DataItemAllocation (`peizhiItem`,`peizhiItemValue`,`desc`,`bItem`,`sItem`,`productionId`) VALUES('name','activiti','activiti数据库名称','database','activiti数据库配置','activiti') ;
INSERT INTO DataItemAllocation (`peizhiItem`,`peizhiItemValue`,`desc`,`bItem`,`sItem`,`productionId`) VALUES('password','12','activiti数据库密码','database','activiti数据库配置','activiti') ;
INSERT INTO DataItemAllocation (`peizhiItem`,`peizhiItemValue`,`desc`,`bItem`,`sItem`,`productionId`) VALUES('port','3306','activiti数据库端口','database','activiti数据库配置','activiti') ;
INSERT INTO DataItemAllocation (`peizhiItem`,`peizhiItemValue`,`desc`,`bItem`,`sItem`,`productionId`) VALUES('type','mysql','activiti数据库类型','database','activiti数据库配置','activiti') ;
INSERT INTO DataItemAllocation (`peizhiItem`,`peizhiItemValue`,`desc`,`bItem`,`sItem`,`productionId`) VALUES('userName','zinglabs','activiti数据库用户名','database','activiti数据库配置','activiti') ;
INSERT INTO DataItemAllocation (`peizhiItem`,`peizhiItemValue`,`desc`,`bItem`,`sItem`,`productionId`) VALUES('route','','','database','activiti数据库配置','activiti') ;
INSERT INTO DataItemAllocation (`peizhiItem`,`peizhiItemValue`,`desc`,`bItem`,`sItem`,`productionId`) VALUES('charset','','activiti数据库编码','database','activiti数据库配置','activiti') ;


-- ----------------------------
-- Table structure for activitiParamDefination   --activiti 通用配置表
-- ----------------------------
DROP TABLE IF EXISTS `activitiParamDefination`;
CREATE TABLE `activitiParamDefination` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `key` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '参数名',
  `value` text COLLATE utf8_unicode_ci COMMENT '参数值',
  `processDefination` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '流程定义标识',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


/*左侧导航栏树型新增节点数据*/
DELETE FROM commonTree_detail WHERE ID="4476F0B2-6107-FABB-2D24-559AA68D5837";
DELETE FROM commonTree_detail WHERE ID="23438B12-F107-3953-1AB8-66238BEAD90C";
DELETE FROM commonTree_detail WHERE ID="23876B1C-D8FC-29AA-46B8-6EDC74C21F4B";
DELETE FROM commonTree_detail WHERE ID="8C7D8A9E-C60C-CAD7-ECEA-53C878D7DBB7";
DELETE FROM commonTree_detail WHERE ID="3EF14F91-AAED-73A1-21CF-DBE4751E6450";

INSERT INTO `commonTree_detail` VALUES ('4476F0B2-6107-FABB-2D24-559AA68D5837', '工作流管理', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2014-11-26 13:10:49', '', '', '', '', '', '', '', '', '', '', '', '', 'activitiManaer');
INSERT INTO `commonTree_detail` VALUES ('23438B12-F107-3953-1AB8-66238BEAD90C', '流程模型管理', '', 'true', 'menu', '4476F0B2-6107-FABB-2D24-559AA68D5837', 'ZSKadmin', '2014-11-26 13:28:53', 'activitiModelsManager/modelManager.html', '', '', '', '', '', '', '', '', '', '', '', 'modelManager');


/*给管理员赋予该节点及子节点的查看权限*/

DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="4476F0B2-6107-FABB-2D24-559AA68D5837" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="23438B12-F107-3953-1AB8-66238BEAD90C" AND roleCode="系统管理员" AND type="menu";


INSERT INTO `suSecurityRoleMapPermission` (`roleCode`,`permissionCode`,`type`) VALUES ('系统管理员','4476F0B2-6107-FABB-2D24-559AA68D5837','menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`,`permissionCode`,`type`) VALUES ('系统管理员','23438B12-F107-3953-1AB8-66238BEAD90C','menu');