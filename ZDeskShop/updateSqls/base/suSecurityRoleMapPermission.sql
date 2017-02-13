-- 权限角色关联表--
DROP TABLE IF EXISTS `suSecurityRoleMapPermission`;
CREATE TABLE `suSecurityRoleMapPermission` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `roleCode` varchar(36) NOT NULL DEFAULT '' COMMENT '角色code或id',
  `permissionCode` varchar(36) NOT NULL DEFAULT '' COMMENT '权限码',
  `type` varchar(20) NOT NULL DEFAULT '' COMMENT '权限类型',
  PRIMARY KEY (`id`),
  KEY `suMapPermission_pid` (`permissionCode`) USING BTREE,
  KEY `suMaoPermission_rid` (`roleCode`) USING BTREE,
  KEY `suMaoPermission_type` (`type`) USING BTREE
) ENGINE=MyISAM  DEFAULT CHARSET=gbk COMMENT='权限角色关联表通过type区分权限类型';
