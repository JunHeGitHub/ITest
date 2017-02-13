-- ----------------------------
-- 问题树节点关联数据索引表
-- ----------------------------
DROP TABLE IF EXISTS `yuliaokuWTTree_detail`;
CREATE TABLE `yuliaokuWTTree_detail` (
  `id` varchar(36) NOT NULL DEFAULT '' COMMENT '主键',
  `nodeId` varchar(36) DEFAULT NULL COMMENT '树节点编号',
  `dataId` varchar(36) DEFAULT NULL COMMENT '绑定数据编号',
  `recordType` varchar(36) DEFAULT NULL COMMENT '树类型',
  `generateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;
