-- ----------------------------
-- Table structure for TemplateMem  自定义信息模板
-- ----------------------------

DROP TABLE IF EXISTS `TemplateMem`;
CREATE TABLE `TemplateMem` (
  `id` varchar(36) NOT NULL COMMENT 'uuid',
  `code` varchar(18) DEFAULT NULL COMMENT '下拉值',
  `text` varchar(36) DEFAULT NULL COMMENT '下拉文本',
  `parentCode` varchar(36) DEFAULT NULL COMMENT '级联索引父级下拉值',
  `TemplateContent` text COMMENT '模板内容',
  `isParent` varchar(18) DEFAULT '1' COMMENT '是否为父节点',
  `parentText` varchar(36) DEFAULT NULL COMMENT '级联索引父级下拉文本',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;