DROP TABLE IF EXISTS `Z_Management`;
CREATE TABLE `Z_Management` (
  `id` int(32) NOT NULL COMMENT '表名',
  `name` varchar(20) NOT NULL COMMENT '名称和文件内容对应',
  `color` varchar(10) DEFAULT NULL COMMENT '背景颜色',
  `imgurl` varchar(500) DEFAULT NULL COMMENT '图片保存位置',
  `url` varchar(300) DEFAULT NULL COMMENT '文本链接地址',
  `weight` int(8) DEFAULT NULL COMMENT '权重',
  `enable` int(5) DEFAULT NULL COMMENT '是否启用 1--启用，0--禁用',
  `type` int(10) DEFAULT NULL COMMENT '文件类型 1--广告词，2--温馨提示，3--小图，4--大图',
  `content` varchar(3000) DEFAULT NULL COMMENT '文件内容，对文件的详细说明',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=gbk;

DELETE FROM DataItemAllocation WHERE DataItemAllocation.productionId = 'fileUpload' and bItem='fileUpload' and peizhiItem='relativePath';
INSERT INTO `DataItemAllocation` (`peizhiItem`, `peizhiItemValue`, `desc`, `bItem`, `sItem`, `productionId`, `generateTime`) VALUES ('relativePath', 'ZKM/files', '文件上传相对路径', 'fileupload', '文件上传配置', 'fileUpload', '2015-01-07 11:21:21');
