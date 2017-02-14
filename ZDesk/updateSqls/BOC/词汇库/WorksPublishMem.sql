---------------------
-- 词汇库流程发布虚拟表
-- ----------------------------
DROP TABLE IF EXISTS `WorksPublishMem`;
CREATE TABLE `WorksPublishMem` (
  `id` varchar(36) NOT NULL COMMENT 'uuid',
  `publishTime` datetime NOT NULL COMMENT '发布时间',
  `professionKey` varchar(6000) NOT NULL COMMENT '专业关键字',
  `revisionNote` varchar(6000) NOT NULL COMMENT '修订备注',
  `batchMark` varchar(36) NOT NULL COMMENT '批次标识',
  `isPublish` varchar(10) NOT NULL DEFAULT '0' COMMENT '是否发布',
  PRIMARY KEY (`id`),
  KEY `id` (`id`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=gbk;
