DROP TABLE IF EXISTS `Z_Feedback`;
CREATE TABLE `Z_Feedback` (
  `id` int(32) NOT NULL,
  `section` varchar(15) NOT NULL COMMENT '部门',
  `name` varchar(15) NOT NULL COMMENT '姓名',
  `tel` varchar(15) NOT NULL COMMENT '手机号',
  `festnetznummer` varchar(15) DEFAULT NULL COMMENT '座机号',
  `notes` varchar(20) DEFAULT NULL COMMENT 'NOTES地址',
  `feedbackType` varchar(20) DEFAULT NULL COMMENT '意见类型',
  `cont` varchar(500) NOT NULL COMMENT '反馈内容',
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '反馈时间',
  `userId` varchar(10) NOT NULL COMMENT '登录用户id',
  `userName` varchar(20) NOT NULL COMMENT '登录用户名',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;
