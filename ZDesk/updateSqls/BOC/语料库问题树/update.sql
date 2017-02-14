
-- ----------------------------
-- 左侧权限树生成语句
-- ----------------------------

/***留言:增加左侧树型数据之前删除***/
DELETE FROM commonTree_detail WHERE ID="1CAD8548-71BA-9985-3F21-13027EA284D7";
DELETE FROM commonTree_detail WHERE ID="88B4E87D-E752-5B66-E99D-C490985AE8E8";
DELETE FROM commonTree_detail WHERE ID="5B7FC75F-491B-6F77-AE1A-C1AF3C35190B";
DELETE FROM commonTree_detail WHERE ID="4826E28B-8100-2DEE-D14A-8E532DC68C83";


/***留言:增加左侧树型数据***/
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('1CAD8548-71BA-9985-3F21-13027EA284D7', '语料库数据展示(坐席端)', '', 'true', 'menu', '', 'ZSKadmin', '2015-07-20 12:57:34', 'BOC/yuliaokuWT-DS.html?recordType=yuliaokuWTTree-ZXD&', '', '', '', '', '', '', '', '', '', '', '', 'yuliaokuWTShow-ZXD');
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('88B4E87D-E752-5B66-E99D-C490985AE8E8', '语料库数据展示(客户端)', '', 'true', 'menu', '', 'ZSKadmin', '2015-07-16 17:49:25', 'BOC/yuliaokuWT-DS.html?recordType=yuliaokuWTTree-KHD&', '', '', '', '', '', '', '', '', '', '', '', 'yuliaokuWTShow-KHD');
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('5B7FC75F-491B-6F77-AE1A-C1AF3C35190B', '语料库问题树(坐席端)', '', 'true', 'menu', '', 'ZSKadmin', '2015-07-14 16:10:07', 'BOC/yuliaokuWT-LB.html?recordType=yuliaokuWTTree-ZXD&', '', '', '', '', '', '', '', '', '', '', '', 'yuliaokuWTTree-ZXD');
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES ('4826E28B-8100-2DEE-D14A-8E532DC68C83', '语料库问题树(客户端)', '', 'true', 'menu', '', 'ZSKadmin', '2015-07-20 12:56:29', 'BOC/yuliaokuWT-LB.html?recordType=yuliaokuWTTree-KHD&', '', '', '', '', '', '', '', '', '', '', '', 'yuliaokuWTTree-KHD');
																																																																							


-- ----------------------------
-- 权限管理
-- ----------------------------

/**问题树：增加数插入前删除****/
DELETE FROM `zkmCommonTree` WHERE `recordType` ="yuliaokuWTTree-ZXD";
DELETE FROM `zkmCommonTree` WHERE `recordType` ="yuliaokuWTTree-KHD";

/**问题树：增加数插****/
INSERT INTO `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) VALUES ('null', 'null', 'D3E34791-A76C-7B5A-9BCA-C5E751F780F9', '语料库常见问题(坐席端)', '', 'yuliaokuWTTree-ZXD', '0', '', '', 'null', 'null', 'null', '');
INSERT INTO `zkmCommonTree` (`leftClick`, `rightClick`, `nodeId`, `nodeName`, `beanName`, `recordType`, `isSynchronous`, `requestUrl`, `type`, `normalNode`, `leftparam`, `rightparam`, `desc`) VALUES ('null', 'null', 'D3E34791-A76C-7B5A-9BCA-132751F780F9', '语料库常见问题(客户端)', '', 'yuliaokuWTTree-KHD', '0', '', '', 'null', 'null', 'null', '');


-- ----------------------------
-- 权限管理
-- ----------------------------
/***问题树：左侧权限树的权限数据插入前删除***/
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="4826E28B-8100-2DEE-D14A-8E532DC68C83" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="5B7FC75F-491B-6F77-AE1A-C1AF3C35190B" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="88B4E87D-E752-5B66-E99D-C490985AE8E8" AND roleCode="系统管理员" AND type="menu";
DELETE FROM `suSecurityRoleMapPermission` WHERE permissionCode="1CAD8548-71BA-9985-3F21-13027EA284D7" AND roleCode="系统管理员" AND type="menu";

/***问题数：左侧权限树的权限数据插入***/
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`)VALUES ('系统管理员', '4826E28B-8100-2DEE-D14A-8E532DC68C83', 'menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`)VALUES ('系统管理员', '5B7FC75F-491B-6F77-AE1A-C1AF3C35190B', 'menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`)VALUES ('系统管理员', '88B4E87D-E752-5B66-E99D-C490985AE8E8', 'menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`, `permissionCode`, `type`)VALUES ('系统管理员', '1CAD8548-71BA-9985-3F21-13027EA284D7', 'menu');

