---------------------
-- 词汇库流程主历史表
-- ----------------------------
DROP TABLE IF EXISTS `WorksBaseMemHis`;
CREATE TABLE `WorksBaseMemHis` (
  `id` varchar(36) NOT NULL COMMENT 'uuid',
  `applyType` varchar(20) DEFAULT '新建' COMMENT '申请类型(当前功能为新建)',
  `approvalState` varchar(20) DEFAULT NULL COMMENT '审批状态',
  `appointedCheckUserID` varchar(20) DEFAULT '' COMMENT '指定复核人ID',
  `appointedCheckUser` varchar(20) DEFAULT '' COMMENT '指定复核人',
  `disponseBTime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '当前处理人变化时间',
  `disponseETime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '最后一次提交时间',
  `flowNode` varchar(20) DEFAULT '' COMMENT '处理的流程节点',
  `flowType` varchar(20) DEFAULT '' COMMENT '流程类型',
  `flowId` varchar(36) DEFAULT '' COMMENT '流程实例标识',
  `published` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '发布时间',
  `disponseUser` varchar(12) DEFAULT '' COMMENT '当前处理人',
  `savePath` varchar(255) DEFAULT '' COMMENT '存放文件绝对路径',
  `worksTitle` varchar(100) DEFAULT NULL COMMENT '词汇标题',
  `createTime` datetime DEFAULT NULL COMMENT '流程创建时间',
  `createUser` varchar(12) DEFAULT NULL COMMENT '流程建立人',
  `handAdvice` varchar(255) DEFAULT NULL COMMENT '办理意见',
  `reviewAdvice` varchar(15) DEFAULT NULL COMMENT '复核意见',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  `endDealTime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '最后一次操作时间',
  `appointedCheckTime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '复核处理结束时间',
  `fileName` varchar(150) DEFAULT NULL COMMENT '上传文件名',
  `nodeName` varchar(36) DEFAULT '' COMMENT '节点名称',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;
