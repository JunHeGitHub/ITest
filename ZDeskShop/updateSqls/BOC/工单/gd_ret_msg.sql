DROP TABLE IF EXISTS `gd_ret_msg`;
CREATE TABLE `gd_ret_msg` (
  `idcode` varchar(20) NOT NULL DEFAULT '',
  `type` varchar(10) NOT NULL DEFAULT '',
  `serialNo` varchar(20) NOT NULL DEFAULT '',
  `HandleDept` varchar(20) NOT NULL DEFAULT '',
  `Handler` varchar(50) NOT NULL DEFAULT '',
  `HandleTime` varchar(50) NOT NULL DEFAULT '',
  `ChangeTime` varchar(50) NOT NULL DEFAULT '',
  `HandleResult` varchar(500) NOT NULL DEFAULT '',
  `ApproveLog` varchar(500) NOT NULL DEFAULT '',
  `Status` varchar(10) NOT NULL DEFAULT '',
  `UrgencyType` varchar(32) NOT NULL DEFAULT '',
  `generateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `state` int(1) DEFAULT NULL,
  `mgs` varchar(2000) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=gbk ;
delete from `DataItemAllocation` where bItem='conf' and productionId='mqsync' and peizhiItem='mq';
INSERT INTO `DataItemAllocation` (`peizhiItem`, `peizhiItemValue`, `desc`, `bItem`, `sItem`, `productionId`, `generateTime`) VALUES ('mq', '/usr/local/nginx/html/excel/', '', 'conf', '', 'mqsync', '2015-04-23 19:12:41');
