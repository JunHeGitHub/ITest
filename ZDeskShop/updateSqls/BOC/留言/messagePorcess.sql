-- ----------------------------
-- Table structure for zkmCommonProcesss   留言流程表
-- ----------------------------

DROP TABLE IF EXISTS `messagePorcess`;
CREATE TABLE `messagePorcess` (
  `id` char(36) CHARACTER SET utf8 NOT NULL COMMENT 'ID',
  `approveState` varchar(200) DEFAULT '' COMMENT '审批状态',
  `appointedCheckUserID` varchar(100) DEFAULT '' COMMENT '指定复核人ID',
  `appointedCheckUser` varchar(200) DEFAULT '' COMMENT '指定复核人',
  `disponseUser` varchar(200) DEFAULT '' COMMENT '当前处理人',
  `disponseBTime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '当前处理人变化时间',
  `disponseETime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '最后一次提交时间',
  `createTime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '流程创建时间',
  `createUser` varchar(200) DEFAULT '' COMMENT '流程建立人',
  `flowNode` varchar(200) DEFAULT '' COMMENT '处理的流程节点',
  `flowType` varchar(20) DEFAULT '' COMMENT '流程类型',
  `flowId` varchar(36) DEFAULT '' COMMENT '流程实例标识',
  `published` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '发布时间',
  `jsonData` text COMMENT '业务数据（json格式）',
  `dataId` varchar(36) DEFAULT '' COMMENT '关联需要加复核流程的数据标识',
  `companyId` varchar(50) DEFAULT '' COMMENT '公司id',
  `companyName` varchar(200) DEFAULT '' COMMENT '公司名称',
  `departmentId` varchar(200) DEFAULT '' COMMENT '部门id',
  `departmentName` varchar(200) DEFAULT '' COMMENT '部门名称',
  `nodeName` varchar(36) DEFAULT '' COMMENT '节点名称',
  `lastApproveState` varchar(36) DEFAULT '' COMMENT '上一次操作状态',
  `reviewAdvice` varchar(200) DEFAULT '' COMMENT '复核处理意见',
  `handAdvice` varchar(200) DEFAULT '' COMMENT '经办处理意见',
  `endDealTime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '最后一次操作时间',
  `XM` varchar(32) DEFAULT '' COMMENT '姓名',
  `CW` varchar(32) DEFAULT '' COMMENT '称谓',
  `SF` varchar(32) DEFAULT '' COMMENT '地区',
  `HFFS` varchar(32) DEFAULT '' COMMENT '回复方式',
  `GDDH` varchar(32) DEFAULT '',
  `SJ` varchar(32) DEFAULT '' COMMENT '手机号码',
  `DZYJ` varchar(32) DEFAULT '' COMMENT '电子邮件',
  `YWLX` varchar(32) DEFAULT '' COMMENT '业务类型',
  `NR` varchar(1000) DEFAULT '' COMMENT '内容',
  `generateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '生成时间',
  `inboundTime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '到达时间',
  `replyTime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '回复时间',
  `agentId` varchar(32) DEFAULT '' COMMENT '代理ID',
  `remark` varchar(32) DEFAULT '' COMMENT '备注',
  `HF` text COMMENT '回复内容',
  `LYLX` varchar(32) DEFAULT NULL COMMENT '留言类型',
  `JSZH` varchar(32) DEFAULT '' COMMENT '接收账号',
  `appointedCheckTime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '复核处理结束时间',
  `MBLX` varchar(32) DEFAULT '' COMMENT '模板类型',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;


