
-- 菜单
INSERT INTO `commonTree_detail` VALUES ('9A325C31-246A-CA04-7D99-E462414BC181', '图文信息管理', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2015-01-13 18:34:17', '', '', '', '', '', '', '', '', '', '', '', '', 'tuwenxinxiguanli');
INSERT INTO `commonTree_detail` VALUES ('7F7DA2A2-B2D2-40D0-507E-BBD46D9D24D4', '素材管理', '', 'true', 'menu', '9A325C31-246A-CA04-7D99-E462414BC181', 'ZSKadmin', '2015-01-14 12:39:54', 'tuWenGuanLi/sucaiguanliCRUD.html', '', '', '', '', '', '', '', '', '', '', '', 'sucaiguanli');
INSERT INTO `commonTree_detail` VALUES ('1D24BC9B-D13E-C26A-0A2B-B77CFEDD3279', '菜单管理', '', 'true', 'menu', '9A325C31-246A-CA04-7D99-E462414BC181', 'ZSKadmin', '2015-01-14 12:41:09', 'tuWenGuanLi/weixincaidanguanli.html', '', '', '', '', '', '', '', '', '', '', '', 'caidanguanli');
INSERT INTO `commonTree_detail` VALUES ('3CF17D21-1BD4-8719-3329-D7D6D6259234', '图片库', '', 'true', 'menu', '9A325C31-246A-CA04-7D99-E462414BC181', 'ZSKadmin', '2015-01-14 12:42:12', 'tuWenGuanLi/pictureLibrary.html', '', '', '', '', '', '', '', '', '', '', '', 'tupianku');
INSERT INTO `commonTree_detail` VALUES ('C5EBFBF4-0D3F-8C41-B030-9A7AA5A48285', '导航菜单示例', '', 'true', 'menu', '9A325C31-246A-CA04-7D99-E462414BC181', 'ZSKadmin', '2015-01-14 14:20:16', 'tuWenGuanLi/menuControl.html', '', '', '', '', '', '', '', '', '', '', '', 'daohangcaidanshili');
INSERT INTO `commonTree_detail` VALUES ('BB0993F3-8BBB-28BC-B6C3-F14192D7849A', '图文展示', '', 'true', 'menu', '9A325C31-246A-CA04-7D99-E462414BC181', 'ZSKadmin', '2015-01-15 10:50:39', 'tuWenGuanLi/graphicList.html', '', '', '', '', '', '', '', '', '', '', '', 'tuwenzhanshi');

-- ----------------------------
-- Table structure for graphicInformationManager
-- ----------------------------
DROP TABLE IF EXISTS `graphicInformationManager`;
CREATE TABLE `graphicInformationManager` (
  `id` varchar(200) NOT NULL COMMENT '主键',
  `parentId` varchar(200) DEFAULT '' COMMENT '父级ID',
  `groupId` varchar(200) DEFAULT '' COMMENT '组ID',
  `sortId` int(11) DEFAULT '-1' COMMENT '排序ID',
  `title` varchar(200) DEFAULT '' COMMENT '标题',
  `autor` varchar(200) DEFAULT '' COMMENT '作者',
  `coverID` varchar(200) DEFAULT '' COMMENT '封面id',
  `coverUrl` varchar(200) DEFAULT '' COMMENT '封面url',
  `coverPhysicalPath` varchar(200) DEFAULT '' COMMENT '封面物理路径',
  `coverToTextContent` int(3) DEFAULT '0' COMMENT '封面是否显示到正文',
  `abstract` varchar(500) DEFAULT '' COMMENT '摘要',
  `textContent` text COMMENT '正文',
  `originalLink` varchar(200) DEFAULT '' COMMENT '原文链接',
  `Category` varchar(100) DEFAULT '' COMMENT '所属类别',
  `chainType` varchar(100) DEFAULT '' COMMENT '图文外链类型',
  `releasePeople` varchar(100) DEFAULT '' COMMENT '发布人',
  `isRelease` varchar(50) DEFAULT '已发布' COMMENT '是否发布',
  `releaseTime` varchar(100) DEFAULT '0000-00-00 00:00:00' COMMENT '发布时间',
  `dataTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据时间',
  `singleOrDouble` varchar(10) NOT NULL DEFAULT '' COMMENT '单图文或多图文',
  `contentClass` varchar(100) DEFAULT '' COMMENT '内容分类',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pictureGroup
-- ----------------------------
DROP TABLE IF EXISTS `pictureGroup`;
CREATE TABLE `pictureGroup` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `groupKey` varchar(255) DEFAULT '' COMMENT '组key',
  `groupValue` varchar(255) DEFAULT '' COMMENT '组名',
  `createPeople` varchar(255) DEFAULT '' COMMENT '创建人',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pictureGroup
-- ----------------------------
INSERT INTO `pictureGroup` VALUES ('1011', '', '分组二', 'admin', '2015-01-16 10:51:50');
INSERT INTO `pictureGroup` VALUES ('1021', '', '分组三', 'admin', '2015-01-16 10:51:57');
INSERT INTO `pictureGroup` VALUES ('1031', '', '分组四', 'admin', '2015-01-16 10:52:07');

-- ----------------------------
-- Table structure for pictureLibrary
-- ----------------------------
DROP TABLE IF EXISTS `pictureLibrary`;
CREATE TABLE `pictureLibrary` (
  `id` varchar(200) NOT NULL COMMENT '主键',
  `imageName` varchar(200) DEFAULT '' COMMENT '图片名称',
  `uploadImageName` varchar(200) DEFAULT '' COMMENT '图片上传后名称',
  `imageUrl` varchar(200) DEFAULT '' COMMENT '图片url',
  `imagePhysicalPath` varchar(200) DEFAULT '' COMMENT '图片物理路径',
  `genusGroup` varchar(100) DEFAULT '' COMMENT '所在组',
  `uploadPeople` varchar(100) DEFAULT '' COMMENT '上传人',
  `uploadTime` varchar(100) DEFAULT '0000-00-00 00:00:00' COMMENT '上传时间',
  `isDelete` int(10) DEFAULT '0' COMMENT '判断此数据是否被删除，0为未删除，1为已删除',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for tree_weixincaidan
-- ----------------------------
DROP TABLE IF EXISTS `tree_weixincaidan`;
CREATE TABLE `tree_weixincaidan` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parentId` int(11) NOT NULL DEFAULT '0' COMMENT '父节点',
  `name` varchar(100) DEFAULT '' COMMENT '节点名称',
  `url` varchar(200) DEFAULT '' COMMENT '节点链接地址',
  `isStart` int(5) DEFAULT '0' COMMENT '是否启动',
  `sort_` int(11) DEFAULT '0' COMMENT '排序',
  `type` varchar(100) DEFAULT '' COMMENT '节点类型',
  PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tree_weixincaidan
-- ----------------------------
INSERT INTO `tree_weixincaidan` VALUES ('1', '0', '', '', '0', '0', '娱乐');
INSERT INTO `tree_weixincaidan` VALUES ('2', '1', '娱乐一', 'http://baidu.com', '1', '0', '娱乐');
INSERT INTO `tree_weixincaidan` VALUES ('3', '2', '娱乐二', 'http://baidu.com', '1', '0', '娱乐');
INSERT INTO `tree_weixincaidan` VALUES ('4', '1', '娱乐三', '阿打发斯蒂芬', '1', '0', '娱乐');
INSERT INTO `tree_weixincaidan` VALUES ('5', '1', 'dddd', 'ddddd', '1', '0', '娱乐');
INSERT INTO `tree_weixincaidan` VALUES ('6', '1', 'qqqq', 'qqqqqq', '1', '0', '娱乐');
INSERT INTO `tree_weixincaidan` VALUES ('10', '0', '', '', '0', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('11', '10', '111', '111', '1', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('12', '10', '222', '333', '1', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('13', '10', '333', '333', '1', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('14', '10', '444', '444', '1', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('15', '10', '555', '555', '0', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('16', '11', '最新动态', '', '1', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('17', '11', '要闻摘要', '111-222', '1', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('18', '13', '333-111', '333-111', '1', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('19', '12', '222-111', '222-111', '0', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('20', '14', '444-111', '444-111', '1', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('21', '12', '222-222', '222-222', '0', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('22', '0', '', '', '0', '0', '生活');
INSERT INTO `tree_weixincaidan` VALUES ('23', '22', 'qqqq', 'qqqqq', '1', '0', '生活');
INSERT INTO `tree_weixincaidan` VALUES ('24', '22', 'wwww', 'wwwww', '1', '0', '生活');
INSERT INTO `tree_weixincaidan` VALUES ('25', '22', 'eeeee', 'eeeeee', '1', '0', '生活');
INSERT INTO `tree_weixincaidan` VALUES ('26', '13', '333-222', '333-222', '1', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('28', '13', '333-333', '333-333', '1', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('30', '14', '444-222', '444-222', '1', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('32', '14', '444-333', '444-333', '1', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('34', '14', '444-444', '444-444', '1', '0', '新闻');
INSERT INTO `tree_weixincaidan` VALUES ('36', '14', '444-555', '444-555', '1', '0', '新闻');


DROP TABLE IF EXISTS `categoryGroup`;
CREATE TABLE `categoryGroup` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `categoryKey` varchar(255) DEFAULT '' COMMENT '分类key',
  `categoryValue` varchar(255) DEFAULT '' COMMENT '分类名',
  `createPeople` varchar(255) DEFAULT '' COMMENT '创建人',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1371 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of categoryGroup
-- ----------------------------
INSERT INTO `categoryGroup` VALUES ('1350', '', '首页', 'admin', '2015-01-30 12:06:09');
INSERT INTO `categoryGroup` VALUES ('1360', '', '广播', 'admin', '2015-01-30 12:06:18');
INSERT INTO `categoryGroup` VALUES ('1370', '', '发现', 'admin', '2015-01-30 12:06:26');