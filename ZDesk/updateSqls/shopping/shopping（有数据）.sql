SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- 订单记录表
-- ----------------------------
DROP TABLE IF EXISTS `orderAccount`;
CREATE TABLE `orderAccount` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderNumber` varchar(64) NOT NULL COMMENT '订单id',
  `orderGroupNumber` varchar(64) DEFAULT NULL COMMENT '订单组id',
  `goodsId` varchar(64) DEFAULT NULL,
  `goodsNumber` varchar(70) DEFAULT NULL COMMENT '商品编号',
  `goodsName` varchar(255) DEFAULT NULL,
  `price` float(15,2) NOT NULL COMMENT '订单价格',
  `privilegePrice` float(15,2) NOT NULL COMMENT '优惠价格',
  `sellersName` varchar(255) NOT NULL COMMENT '操作人姓名/账号',
  `type` varchar(15) NOT NULL COMMENT '操作类型:创建订单/变更价格/',
  `processingTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `id` (`id`) USING BTREE,
  KEY `orderNumber` (`orderNumber`) USING BTREE,
  KEY `orderGroupNumber` (`orderGroupNumber`) USING BTREE,
  KEY `processingTime` (`processingTime`) USING BTREE,
  KEY `goodsId` (`goodsId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of orderAccount
-- ----------------------------
INSERT INTO `orderAccount` VALUES ('199', '45646151678948646', '8570A562-7AAB-7047-4846-FED72B73A7A5', 'C39E1C10-8557-4191-995B-A2B5DEF3E9C4', '4BD06BA6-EE7B-42DD-8C1E-F19A3F7A07A3', '小米系列', '-100.00', '-123.00', '1', '变更价格', '2015-06-26 15:15:28');
INSERT INTO `orderAccount` VALUES ('200', '45646151678948646', '8570A562-7AAB-7047-4846-FED72B73A7A5', 'D67DC4D8-BE20-4048-BD5D-B843B4575009', '40401565-635F-4E2C-8AFC-1AE656B26A17', '测试插入数据', '0.00', '-1998.00', '1', '变更价格', '2015-06-26 15:15:31');
INSERT INTO `orderAccount` VALUES ('201', '20150623193541234', '7B0D43B1-BF6D-A1BB-6479-C77D05E0C51E', '61071154-C32D-4088-915B-463D49A6325F', '8E9C6BC9-6301-44DD-A15D-F2D91F264D10', '苹果手机', '0.00', '-56.00', '1', '变更价格', '2015-06-26 15:07:29');
INSERT INTO `orderAccount` VALUES ('202', '20150623193541234', '7B0D43B1-BF6D-A1BB-6479-C77D05E0C51E', '61071154-C32D-4088-915B-463D49A6325F', '8E9C6BC9-6301-44DD-A15D-F2D91F264D10', '苹果手机', '1654.00', '1654.00', '1', '变更价格', '2015-06-26 15:07:33');
INSERT INTO `orderAccount` VALUES ('203', '87456465464878946', 'BDBEB94B-1F09-7FE8-3AFA-2C054D0BE382', '0', '', '', '1.00', '0.00', '1', '创建订单', '2015-06-26 15:16:44');
INSERT INTO `orderAccount` VALUES ('204', '141649216645497461', '51FA7879-9379-4DF0-0724-4649D92E9251', '0', '', '', '1.00', '0.00', '1', '创建订单', '2015-06-26 15:14:20');
INSERT INTO `orderAccount` VALUES ('205', '201546492165494654', '923EBB67-4A3F-04C9-B836-BC3998D95151', '0', '', '', '4329.00', '0.00', '1', '创建订单', '2015-06-26 15:12:18');
INSERT INTO `orderAccount` VALUES ('206', '201506151254946869', '316EECBB-E2ED-9D42-BBD9-5B3BAB6949BB', '0', '', '', '1.00', '0.00', '1', '创建订单', '2015-06-26 15:09:37');
INSERT INTO `orderAccount` VALUES ('207', '201506191437381234', '20150619143739fWu', '0', '', '', '111.00', '0.00', '1', '创建订单', '2015-06-26 15:06:09');
INSERT INTO `orderAccount` VALUES ('208', '201506191507161234', '20150619150716uqa', '0', '', '', '1.00', '0.00', '1', '创建订单', '2015-06-26 15:06:02');
INSERT INTO `orderAccount` VALUES ('209', '20150619151848123', '20150619151848EEu', '0', '', '', '12225.00', '0.00', '1', '创建订单', '2015-06-26 15:05:59');
INSERT INTO `orderAccount` VALUES ('210', '20150619151848123', '20150619151848Xmm', '0', '', '', '1.00', '0.00', '1', '创建订单', '2015-06-26 15:05:57');
INSERT INTO `orderAccount` VALUES ('211', '201506191555165740', '201506191555164303', '0', '', '', '2555.00', '0.00', '1', '创建订单', '2015-06-19 15:55:16');
INSERT INTO `orderAccount` VALUES ('212', '201506231631365275', '201506231631368325', '0', '', '', '11112.00', '0.00', '1', '创建订单', '2015-06-23 16:31:35');
INSERT INTO `orderAccount` VALUES ('213', '201506231935421382', '201506231935425832', '0', '', '', '22222.00', '0.00', '1', '创建订单', '2015-06-23 19:35:41');

-- ----------------------------
-- 订单商品表
-- ----------------------------
DROP TABLE IF EXISTS `orderGoods`;
CREATE TABLE `orderGoods` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `orderNumber` varchar(64) NOT NULL DEFAULT '' COMMENT '订单编号',
  `goodsId` varchar(64) NOT NULL DEFAULT '' COMMENT '商品id',
  `goodsNumber` varchar(255) NOT NULL DEFAULT '' COMMENT '商品编码',
  `goodsName` varchar(255) NOT NULL DEFAULT '' COMMENT '商品名称',
  `goodsTitle` varchar(255) NOT NULL DEFAULT '' COMMENT '商品标题',
  `GoodsAttributes` varchar(255) NOT NULL DEFAULT '' COMMENT '商品类别',
  `goodsCotegory` varchar(255) NOT NULL DEFAULT '' COMMENT '记录买家所选商品类别与属性集合',
  `numbers` int(11) NOT NULL COMMENT '数量',
  `unitPrice` double(11,0) NOT NULL COMMENT '单价',
  `price` double(11,0) NOT NULL COMMENT '总价',
  `createDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `companyID` int(11) NOT NULL COMMENT 'companyID',
  `orderGroupNumber` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `orderNumber` (`orderNumber`) COMMENT '订单编号',
  KEY `goodsNumber` (`goodsNumber`) COMMENT '商品编码',
  KEY `goodsId` (`goodsId`) COMMENT '商品id',
  KEY `goodsCotegory` (`goodsCotegory`) COMMENT '记录买家所选商品类别与属性集合',
  KEY `id` (`id`),
  KEY `numbers` (`numbers`) COMMENT '数量'
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orderGoods
-- ----------------------------
INSERT INTO `orderGoods` VALUES ('133', '20150623193541234', '61071154-C32D-4088-915B-463D49A6325F', '8E9C6BC9-6301-44DD-A15D-F2D91F264D10', '苹果手机', '苹果5s', '尺码:38-运费:顺丰', '', '1', '56', '1654', '2015-06-26 15:07:45', '0', '7B0D43B1-BF6D-A1BB-6479-C77D05E0C51E');
INSERT INTO `orderGoods` VALUES ('134', '45646151678948646', 'C39E1C10-8557-4191-995B-A2B5DEF3E9C4', '4BD06BA6-EE7B-42DD-8C1E-F19A3F7A07A3', '小米系列', '测试添加异常', '参数:CPU-品牌:小米-运费:EMS', '测试:123-', '1', '23', '-100', '2015-06-26 15:15:18', '0', '8570A562-7AAB-7047-4846-FED72B73A7A5');
INSERT INTO `orderGoods` VALUES ('135', '45646151678948646', 'D67DC4D8-BE20-4048-BD5D-B843B4575009', '40401565-635F-4E2C-8AFC-1AE656B26A17', '测试插入数据', '测试插入数据', '参数:CPU-品牌:锤子-运费:顺丰', '测试:123-', '2', '999', '0', '2015-06-26 15:15:17', '0', '8570A562-7AAB-7047-4846-FED72B73A7A5');
INSERT INTO `orderGoods` VALUES ('136', '87456465464878946', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', 'F39AB4E6-4D3D-4388-A117-27FB512F031E', '1', '1', '参数:CPU-品牌:三星-运费:顺丰', '测试:对丰富-', '1', '1', '1', '2015-06-26 15:16:37', '0', 'BDBEB94B-1F09-7FE8-3AFA-2C054D0BE382');
INSERT INTO `orderGoods` VALUES ('137', '141649216645497461', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', 'F39AB4E6-4D3D-4388-A117-27FB512F031E', '1', '1', '参数:CPU-品牌:三星-运费:顺丰', '测试:教科文部-', '1', '1', '1', '2015-06-26 15:14:09', '0', '51FA7879-9379-4DF0-0724-4649D92E9251');
INSERT INTO `orderGoods` VALUES ('138', '201546492165494654', '42D58289-95DC-40C3-ACE2-D073BB140175', '709AB39C-01EB-4745-89EA-B3652CD1CA99', '添加商品', '添加商品1', '金额:50', '电话号码:，11-测试一个:11- ', '1', '4253', '4253', '2015-06-26 15:11:54', '0', '923EBB67-4A3F-04C9-B836-BC3998D95151');
INSERT INTO `orderGoods` VALUES ('139', '201546492165494654', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', 'E5E49349-9DE6-4FC3-BF36-2B36335AFAF2', '话费充值', '657', '金额:100', ' 电话号码:11-测试一个:11- ', '1', '76', '76', '2015-06-26 15:12:07', '0', '923EBB67-4A3F-04C9-B836-BC3998D95151');
INSERT INTO `orderGoods` VALUES ('140', '201506151254946869', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', 'F39AB4E6-4D3D-4388-A117-27FB512F031E', '1', '1', '参数:CPU-品牌:三星-运费:顺丰', '测试:插件- ', '1', '1', '1', '2015-06-26 15:09:28', '0', '316EECBB-E2ED-9D42-BBD9-5B3BAB6949BB');
INSERT INTO `orderGoods` VALUES ('141', '201506191437381234', 'EF0C9216-9A7C-4709-86FA-86828532C6F8', '85EDBC11-D72A-4530-B93E-5459DA928FC1', '1', '追加商品', '参数:内存-品牌:锤子-运费:顺丰', '测试:哈哈- ', '1', '111', '111', '2015-06-26 15:03:24', '0', '20150619143739fWu');
INSERT INTO `orderGoods` VALUES ('142', '201506191507161234', '6D7EA884-BD4F-4D70-9E3E-6DB9E6AA5E2F', '0E391D92-A2D4-4414-BDDD-829097EAB749', 'p8', '1', '运费:EMS', ' ', '1', '1', '1', '2015-06-26 15:03:28', '0', '20150619150716uqa');
INSERT INTO `orderGoods` VALUES ('143', '20150619151848123', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', 'F39AB4E6-4D3D-4388-A117-27FB512F031E', '1', '1', '参数:CPU-品牌:三星-运费:顺丰', ' 测试:家保护- ', '2', '1', '2', '2015-06-26 15:02:50', '0', '20150619151848EEu');
INSERT INTO `orderGoods` VALUES ('144', '20150619151848123', '6D7EA884-BD4F-4D70-9E3E-6DB9E6AA5E2F', '0E391D92-A2D4-4414-BDDD-829097EAB749', 'p8', '1', '运费:EMS', '  ', '1', '1', '1', '2015-06-26 15:02:55', '0', '20150619151848EEu');
INSERT INTO `orderGoods` VALUES ('145', '20150619151848123', '1ED804F0-6F91-42DF-9685-DC7A161BCD37', 'F4961B96-A1B7-43B7-B065-6ED36B6671AC', '腾讯QB点卡', '200元点QB卡', '单价:200', '游戏帐号:加紧急警报- ', '1', '11111', '11111', '2015-06-26 15:02:58', '0', '20150619151848EEu');
INSERT INTO `orderGoods` VALUES ('146', '20150619151848123', '43A73297-37EF-4D8F-B276-11332706C8DC', '51A9A662-B16B-4BAD-ADC7-769565528634', '中国石化加油卡', '石化加油卡冲200送100', '单价:200', '  ', '1', '1111', '1111', '2015-06-26 15:03:02', '0', '20150619151848EEu');
INSERT INTO `orderGoods` VALUES ('147', '20150619151848123', '733B19D1-196E-42AE-880B-A02ECBB3EB05', 'A9D344EA-9D97-4366-981C-54922D26BDC5', 'p7', 'p7', '金额:50', '  ', '1', '1', '1', '2015-06-26 15:03:05', '0', '20150619151848Xmm');
INSERT INTO `orderGoods` VALUES ('148', '201506191555165740', '919581C5-40C2-46BD-905F-A64F45DF8211', 'F35D5EA5-F00C-411D-BDE1-BA6E53D0DDC0', '三星手机', 'S6', '尺码:42-运费:申通', '  ', '1', '111', '111', '2015-06-19 11:00:00', '0', '201506191555164303');
INSERT INTO `orderGoods` VALUES ('149', '201506191555165740', '9B4A3B9B-202A-48F4-B34E-C600B16C445C', 'C38F57E6-F9FD-4066-BC00-D48CABBB6974', '服装', '皇帝的新装', '颜色:红色-尺寸:XXL-运费:申通', ' 测试属性:1- ', '1', '111', '111', '2015-06-19 00:45:00', '0', '201506191555164303');
INSERT INTO `orderGoods` VALUES ('150', '201506191555165740', 'CB42C5A8-E878-4763-A91B-EB73DEACEFF8', '047703F6-9E72-4B21-B118-FA2323106B8D', '一加手机', '一加手机E1', '尺码:39-运费:申通', '  ', '1', '1111', '1111', '2015-06-19 10:23:00', '0', '201506191555164303');
INSERT INTO `orderGoods` VALUES ('151', '201506191555165740', 'EF0C9216-9A7C-4709-86FA-86828532C6F8', '85EDBC11-D72A-4530-B93E-5459DA928FC1', '1', '追加商品', '参数:内存-品牌:锤子-运费:顺丰', ' 测试:1- ', '1', '111', '111', '2015-06-19 09:25:00', '0', '201506191555164303');
INSERT INTO `orderGoods` VALUES ('152', '201506191555165740', '43A73297-37EF-4D8F-B276-11332706C8DC', '51A9A662-B16B-4BAD-ADC7-769565528634', '中国石化加油卡', '石化加油卡冲200送100', '单价:200', ' ', '1', '1111', '1111', '2015-06-19 00:00:00', '0', '201506191555164303');
INSERT INTO `orderGoods` VALUES ('153', '201506231631365275', '6D7EA884-BD4F-4D70-9E3E-6DB9E6AA5E2F', '0E391D92-A2D4-4414-BDDD-829097EAB749', 'p8', '1', '运费:EMS', '  ', '1', '1', '1', '2015-06-23 10:01:00', '0', '201506231631368325');
INSERT INTO `orderGoods` VALUES ('154', '201506231631365275', '1ED804F0-6F91-42DF-9685-DC7A161BCD37', 'F4961B96-A1B7-43B7-B065-6ED36B6671AC', '腾讯QB点卡', '200元点QB卡', '单价:200', '游戏帐号:海拔- ', '1', '11111', '11111', '2015-06-23 10:05:00', '0', '201506231631368325');
INSERT INTO `orderGoods` VALUES ('155', '201506231935421382', '1ED804F0-6F91-42DF-9685-DC7A161BCD37', 'F4961B96-A1B7-43B7-B065-6ED36B6671AC', '腾讯QB点卡', '200元点QB卡', '单价:200', '游戏帐号:54665564545645- ', '2', '11111', '22222', '2015-06-23 12:03:00', '0', '201506231935425832');

-- ----------------------------
-- 订单表
-- ----------------------------
DROP TABLE IF EXISTS `orderTable`;
CREATE TABLE `orderTable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderNumber` varchar(64) NOT NULL DEFAULT '' COMMENT '订单编号',
  `orderGroupNumber` varchar(64) NOT NULL COMMENT '订单组id',
  `orderTime` datetime NOT NULL COMMENT '订单日期',
  `orderState` varchar(11) NOT NULL COMMENT '订单状态',
  `orderFor` varchar(11) NOT NULL DEFAULT '' COMMENT '订单所属',
  `buyersUser` varchar(255) NOT NULL DEFAULT '' COMMENT '下单人账号',
  `buyersName` varchar(255) NOT NULL DEFAULT '' COMMENT '下单人姓名',
  `buyersPhone` varchar(255) NOT NULL DEFAULT '' COMMENT '下单人手机号码',
  `buyersAddress` varchar(255) NOT NULL DEFAULT '' COMMENT '下单人收货地址',
  `buyersMessage` varchar(255) NOT NULL DEFAULT '' COMMENT '下单人留言',
  `sellersUser` varchar(255) NOT NULL DEFAULT '' COMMENT '订单处理人账号',
  `sellersName` varchar(255) DEFAULT '' COMMENT '订单处理人姓名',
  `sellersDepartmentId` int(11) DEFAULT NULL COMMENT '订单处理部门id',
  `sellersDepartmentName` varchar(255) DEFAULT '' COMMENT '订单处理部门名称',
  `orderProcessingTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单处理时间',
  `companyID` int(11) DEFAULT NULL,
  `orderPrice` double(11,0) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `orderNumber` (`orderNumber`) USING BTREE COMMENT '订单编号',
  KEY `buyersUser` (`buyersUser`) USING BTREE COMMENT '下单人账号',
  KEY `sellersUser` (`sellersUser`) USING BTREE COMMENT '处理人账号',
  KEY `buyersMessage` (`buyersMessage`) USING BTREE COMMENT '下单人留言',
  KEY `buyersAddress` (`buyersAddress`) USING BTREE COMMENT '下单人收货地址',
  KEY `orderState` (`orderState`) USING BTREE COMMENT '订单状态',
  KEY `id` (`id`) USING BTREE COMMENT 'id'
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orderTable
-- ----------------------------
INSERT INTO `orderTable` VALUES ('72', '20150623193541234', '7B0D43B1-BF6D-A1BB-6479-C77D05E0C51E', '2015-06-26 14:07:01', '02', '1', '1', '杨超', '\n							13718200402 ', '北京市海淀区信息路二号国际科技创业园2号院二号楼', '', '1', '1', '1', '1', '2015-06-30 15:07:24', '1', '1654');
INSERT INTO `orderTable` VALUES ('73', '45646151678948646', '8570A562-7AAB-7047-4846-FED72B73A7A5', '2015-06-12 10:28:00', '01', '1', '1', '测试默认地址', '12356465448', '北京朝阳上地七街国际创业园', '', '1', '1', '1', '1', '2015-06-26 15:15:09', '1', '-100');
INSERT INTO `orderTable` VALUES ('74', '87456465464878946', 'BDBEB94B-1F09-7FE8-3AFA-2C054D0BE382', '2015-06-10 00:14:00', '01', '1', '1', '英立讯', '15888888888', '北京海淀上地七街这边', '都飞机和国国籍', '1', '1', '1', '1', '2015-06-26 15:22:16', '1', '1');
INSERT INTO `orderTable` VALUES ('75', '141649216645497461', '51FA7879-9379-4DF0-0724-4649D92E9251', '2015-06-16 17:00:00', '05', '1', '1', '英立讯', '15888888888', '北京海淀上地七街这边', '苯甲酸钠j ja', '1', '1', '1', '1', '2015-06-26 15:14:12', '1', '1');
INSERT INTO `orderTable` VALUES ('76', '201546492165494654', '923EBB67-4A3F-04C9-B836-BC3998D95151', '2015-06-16 17:14:00', '01', '0', '1', '英立讯', '15888888888', '北京海淀上地七街这边', '111', '1', '1', '1', '1', '2015-06-26 15:11:45', '1', '4329');
INSERT INTO `orderTable` VALUES ('77', '201506151254946869', '316EECBB-E2ED-9D42-BBD9-5B3BAB6949BB', '2015-06-25 14:37:59', '01', '1', '1', '英立讯', '15888888888', '北京海淀上地七街这边', '，，', '1', '1', '1', '1', '2015-06-26 15:10:26', '1', '1');
INSERT INTO `orderTable` VALUES ('78', '201506191437381234', '20150619143739fWu', '2015-06-19 12:00:00', '04', '1', '1', '英立讯', '15888888888', '北京海淀上地七街这边', '', '1', '1', '1', '1', '2015-06-30 15:08:01', '1', '111');
INSERT INTO `orderTable` VALUES ('79', '201506191507161234', '20150619150716uqa', '2015-06-19 13:00:00', '04', '1', '1', '英立讯', '15888888888', '北京海淀上地七街这边', '，，，', '1', '1', '1', '1', '2015-06-30 15:07:48', '1', '1');
INSERT INTO `orderTable` VALUES ('80', '20150619151848123', '20150619151848EEu', '2015-06-19 07:00:00', '05', '1', '1', '英立讯', '15888888888', '北京海淀上地七街这边', '', '1', '1', '1', '1', '2015-06-26 15:03:38', '1', '12225');
INSERT INTO `orderTable` VALUES ('81', '20150619151848123', '20150619151848Xmm', '2015-06-19 08:00:00', '01', '0', '1', '英立讯', '15888888888', '北京海淀上地七街这边', '', '1', '1', '1', '1', '2015-06-26 15:03:44', '1', '1');
INSERT INTO `orderTable` VALUES ('82', '201506191555165740', '201506191555164303', '2015-06-19 09:00:00', '01', '1', '1', '英立讯', '15888888888', '北京海淀上地七街这边', '啊啊啊', '1', '1', '1', '1', '2015-06-26 14:08:56', '1', '2555');
INSERT INTO `orderTable` VALUES ('83', '201506231631365275', '201506231631368325', '2015-06-23 11:00:00', '01', '1', '1', '虚拟物品', '虚拟物品', '虚拟物品', '', '1', '1', '1', '1', '2015-06-26 14:08:56', '1', '11112');
INSERT INTO `orderTable` VALUES ('84', '201506231935421382', '201506231935425832', '2015-06-23 12:00:00', '02', '1', '1', '虚拟物品', '虚拟物品', '虚拟物品', '', '1', '1', '1', '1', '2015-06-30 15:07:34', '1', '22222');

-- ----------------------------
-- 评价表
-- ----------------------------
DROP TABLE IF EXISTS `tb_assess`;
CREATE TABLE `tb_assess` (
  `id` bigint(60) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `goodsId` char(36) NOT NULL DEFAULT '' COMMENT '商品id',
  `groupId` char(36) NOT NULL DEFAULT '' COMMENT '商品组ID',
  `content` text NOT NULL COMMENT '评价内容',
  `goodLevel` int(11) DEFAULT NULL COMMENT '评价等级',
  `serviceLevel` int(11) DEFAULT NULL COMMENT '服务态度',
  `sendLevel` int(11) DEFAULT NULL COMMENT '发货速度',
  `expressLevel` int(11) DEFAULT NULL COMMENT '物流速度',
  `showOrNot` int(11) DEFAULT NULL COMMENT '是否匿名评价',
  `userId` varchar(100) DEFAULT '' COMMENT '用户id',
  `userName` varchar(100) DEFAULT '' COMMENT '用户名',
  `addDate` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '评价时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_assess
-- ----------------------------
INSERT INTO `tb_assess` VALUES ('1', '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', '', 'asd', '3', '4', '3', '5', '1', '1', '英立讯', '2015-06-18 14:45:19');
INSERT INTO `tb_assess` VALUES ('2', '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', '', 'asd', '4', '3', '3', '3', '1', '1', '英立讯', '2015-06-18 14:45:22');
INSERT INTO `tb_assess` VALUES ('3', '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', '', 'dsfsdfs', '4', '3', '3', '3', '1', '1', '英立讯', '2015-06-18 14:45:25');
INSERT INTO `tb_assess` VALUES ('5', '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', '', '111111', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-18 14:45:29');
INSERT INTO `tb_assess` VALUES ('6', '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', '', '2222222222222222222222', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-18 14:45:34');
INSERT INTO `tb_assess` VALUES ('7', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', 'sdasd', '4', '3', '2', '4', '1', '1', '英立讯', '2015-06-18 16:43:43');
INSERT INTO `tb_assess` VALUES ('8', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '', '3', '4', '4', '4', '1', '1', '英立讯', '2015-06-19 12:21:37');
INSERT INTO `tb_assess` VALUES ('9', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '没有故事', '1', '5', '4', '4', '1', '1', '英立讯', '2015-06-19 15:08:22');
INSERT INTO `tb_assess` VALUES ('10', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '没有故事', '1', '5', '4', '4', '1', '1', '英立讯', '2015-06-19 15:08:26');
INSERT INTO `tb_assess` VALUES ('11', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '没有故事', '1', '5', '4', '4', '1', '1', '英立讯', '2015-06-19 15:08:27');
INSERT INTO `tb_assess` VALUES ('12', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '没有故事', '1', '5', '4', '4', '1', '1', '英立讯', '2015-06-19 15:08:28');
INSERT INTO `tb_assess` VALUES ('13', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '哎呀', '4', '4', '5', '5', '1', '1', '英立讯', '2015-06-19 15:09:25');
INSERT INTO `tb_assess` VALUES ('14', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '哎呀', '4', '4', '5', '5', '1', '1', '英立讯', '2015-06-19 15:09:26');
INSERT INTO `tb_assess` VALUES ('15', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '哎呀', '4', '4', '5', '5', '1', '1', '英立讯', '2015-06-19 15:09:27');
INSERT INTO `tb_assess` VALUES ('16', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '哎呀', '5', '4', '5', '4', '1', '1', '英立讯', '2015-06-19 15:11:31');
INSERT INTO `tb_assess` VALUES ('17', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '哎呀', '5', '4', '5', '4', '1', '1', '英立讯', '2015-06-19 15:11:33');
INSERT INTO `tb_assess` VALUES ('18', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '哎呀', '5', '4', '5', '4', '1', '1', '英立讯', '2015-06-19 15:11:34');
INSERT INTO `tb_assess` VALUES ('19', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '哎呀', '5', '4', '5', '4', '1', '1', '英立讯', '2015-06-19 15:11:35');
INSERT INTO `tb_assess` VALUES ('20', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '大家非银行及丰富的', '4', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:20:51');
INSERT INTO `tb_assess` VALUES ('21', '4056B1E4-C304-4466-8FE6-FBF26A4AFF7B', '', '123456', '4', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:27:01');
INSERT INTO `tb_assess` VALUES ('22', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '但韩国将会得到的', '4', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:27:01');
INSERT INTO `tb_assess` VALUES ('23', 'B2BFB84D-F46D-4575-B61B-71630C8178EB', '', '搞好土地开工的', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:27:01');
INSERT INTO `tb_assess` VALUES ('24', '11C28E22-5A10-4E56-8210-1885372A953D', '', '个hy vt vy', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:27:01');
INSERT INTO `tb_assess` VALUES ('28', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '，，', '4', '5', '4', '4', '1', '1', '英立讯', '2015-06-19 15:48:23');
INSERT INTO `tb_assess` VALUES ('29', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '，，。。，，，', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:49:12');
INSERT INTO `tb_assess` VALUES ('30', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '，，。。，，，', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:49:14');
INSERT INTO `tb_assess` VALUES ('31', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '，，。。，，，', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:49:15');
INSERT INTO `tb_assess` VALUES ('32', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '，，。。，，，', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:49:17');
INSERT INTO `tb_assess` VALUES ('33', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '，，。。，，，', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:49:18');
INSERT INTO `tb_assess` VALUES ('34', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '，，。。，，，', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:49:20');
INSERT INTO `tb_assess` VALUES ('35', '4056B1E4-C304-4466-8FE6-FBF26A4AFF7B', '', '，', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:56:47');
INSERT INTO `tb_assess` VALUES ('36', 'B2BFB84D-F46D-4575-B61B-71630C8178EB', '', '', '4', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:56:47');
INSERT INTO `tb_assess` VALUES ('37', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:56:47');
INSERT INTO `tb_assess` VALUES ('38', '11C28E22-5A10-4E56-8210-1885372A953D', '', '', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 15:56:47');
INSERT INTO `tb_assess` VALUES ('42', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '', '13213', '5', '5', '5', '5', '1', '1', '英立讯', '2015-06-19 17:56:33');

-- ----------------------------
-- 分类表
-- ----------------------------
DROP TABLE IF EXISTS `tb_Category`;
CREATE TABLE `tb_Category` (
  `id` char(36) NOT NULL DEFAULT '' COMMENT '序列号',
  `parentId` char(36) DEFAULT '' COMMENT '父节点',
  `nodeName` varchar(50) DEFAULT '' COMMENT '节点名称',
  `categroyUseOrNot` int(11) DEFAULT '1' COMMENT '分类启用、禁用标识(1启用，0禁用）',
  `departmentId` int(11) DEFAULT NULL COMMENT '所属部门ID',
  `companyId` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of tb_Category
-- ----------------------------
INSERT INTO `tb_Category` VALUES ('00B3555D-9313-4CC8-038D-F9927C770014', 'AB1110A4-8290-38E9-A2D5-D28E19396404', '手机', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('04A5BFCF-D1EA-7D1C-2C98-60B07999CBF6', '0', '清华', '1', '1', '1');
INSERT INTO `tb_Category` VALUES ('09BD4AE2-AAC2-6826-738A-40827DD298CE', '0', '虚拟类', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('18F3CBF0-1F19-D858-7ABA-99A055F7D443', '0', '英立讯', '1', '1', '1');
INSERT INTO `tb_Category` VALUES ('1A0B499A-87FC-DFF4-77A4-E612EFFD5E52', '5A5A93BA-9A92-F381-6F10-A71F5C660230', '虚拟类', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('21733041-C538-3B69-FDC4-36170218E08C', '25C8E80C-02CD-AC69-8A38-BDF6693DC0A1', '实物', '1', '1', '1');
INSERT INTO `tb_Category` VALUES ('25C8E80C-02CD-AC69-8A38-BDF6693DC0A1', '0', '英立讯测试', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('38B2CDAB-22A4-5CD6-369C-79EF3A579F76', 'AB1110A4-8290-38E9-A2D5-D28E19396404', '服装', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('3E4D4DA6-10F9-625B-71E7-A299EFF3947A', '21733041-C538-3B69-FDC4-36170218E08C', '加油卡', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('4010DAC1-E38B-9F09-C120-FC59EA06A193', 'F0B97896-DBDF-3760-2104-0DEED22F737E', '中国石化加油卡', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('45EFB086-E9EF-5992-1C5A-D4EEE9B5D2AE', 'BDB29A94-EC87-4958-7E7D-FF9902718DB1', '电信', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('4E178F66-8D54-C726-09EF-A0CAA649EEF8', '25C8E80C-02CD-AC69-8A38-BDF6693DC0A1', '虚拟', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('5A5A93BA-9A92-F381-6F10-A71F5C660230', '0', '测试', '1', '1', '1');
INSERT INTO `tb_Category` VALUES ('5A8B6F5D-2A8E-88C7-E626-C349C870FAC1', 'D6F0D0E3-9693-CC34-60A8-53D5A54D51E5', '加油卡', '1', '1', '1');
INSERT INTO `tb_Category` VALUES ('7C2616B4-1E9A-0113-0317-F8717967A407', 'BDB29A94-EC87-4958-7E7D-FF9902718DB1', '联通', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('8F170B22-3B83-151C-93D0-34B1FC278732', 'AB1110A4-8290-38E9-A2D5-D28E19396404', '鞋子', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('9180DFEA-303F-23B8-6FFA-3B211B157E4A', 'D309F671-5A46-414C-4CEC-72B208503626', 'Q币卡', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('9910DBE3-C6FF-F802-43E8-806B84297550', '1A0B499A-87FC-DFF4-77A4-E612EFFD5E52', '腾讯QB点卡', '1', '1', '1');
INSERT INTO `tb_Category` VALUES ('9B43E1E8-9D0D-660A-9F9C-FD41757E9423', 'BDB29A94-EC87-4958-7E7D-FF9902718DB1', '移动', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('A3A50BD4-65AD-EF65-5BDE-1B20595566E4', 'ACE38BC1-E9E7-D809-CD30-CC089CA4C778', 'QB点卡', '1', '1', '1');
INSERT INTO `tb_Category` VALUES ('A82AC0AB-7DFD-5B3D-9E7E-D20EC474E18B', '4E178F66-8D54-C726-09EF-A0CAA649EEF8', 'QB点卡', '1', '1', '1');
INSERT INTO `tb_Category` VALUES ('AB1110A4-8290-38E9-A2D5-D28E19396404', '0', '实物类', '1', '1', '1');
INSERT INTO `tb_Category` VALUES ('ACE38BC1-E9E7-D809-CD30-CC089CA4C778', '18F3CBF0-1F19-D858-7ABA-99A055F7D443', '虚拟类', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('BDB29A94-EC87-4958-7E7D-FF9902718DB1', '09BD4AE2-AAC2-6826-738A-40827DD298CE', '电话卡', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('C9FA25FE-6195-E4F2-534B-0309FD22AF4C', '04A5BFCF-D1EA-7D1C-2C98-60B07999CBF6', '捐助', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('D309F671-5A46-414C-4CEC-72B208503626', '09BD4AE2-AAC2-6826-738A-40827DD298CE', '点卡', '1', '0', '1');
INSERT INTO `tb_Category` VALUES ('D6F0D0E3-9693-CC34-60A8-53D5A54D51E5', '18F3CBF0-1F19-D858-7ABA-99A055F7D443', '实物类', '1', '1', '1');
INSERT INTO `tb_Category` VALUES ('F0B97896-DBDF-3760-2104-0DEED22F737E', '5A5A93BA-9A92-F381-6F10-A71F5C660230', '实物类', '1', '1', '1');

-- ----------------------------
-- 收货地址表
-- ----------------------------
DROP TABLE IF EXISTS `tb_ConsigneeAddress`;
CREATE TABLE `tb_ConsigneeAddress` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
  `userId` varchar(20) NOT NULL DEFAULT '' COMMENT '用户id',
  `area` varchar(255) NOT NULL DEFAULT '' COMMENT '所在地区',
  `street` varchar(255) DEFAULT '' COMMENT '街道',
  `address` varchar(255) NOT NULL DEFAULT '' COMMENT '详细地址',
  `consigneeName` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人姓名',
  `consigneePhone` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人手机号码',
  `postCode` varchar(255) DEFAULT '' COMMENT '邮编',
  `isDefaultAddress` varchar(255) NOT NULL DEFAULT '' COMMENT '是否为默认收货地址',
  `createData` varchar(255) NOT NULL DEFAULT '' COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `address` (`address`),
  KEY `id` (`id`),
  KEY `consigneeName` (`consigneeName`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_ConsigneeAddress
-- ----------------------------
INSERT INTO `tb_ConsigneeAddress` VALUES ('9', '1', '北京市_北京市_西城区', '0', '，', '测试', '13718200402', '', '1', '2015-06-19 16:00:11');
INSERT INTO `tb_ConsigneeAddress` VALUES ('19', '1', '额外人田', '0', '11111', '其味无穷', '11111111111', '1123', '0', '2015-06-19 16:27:42');
INSERT INTO `tb_ConsigneeAddress` VALUES ('21', '1', '1234', '0', '123534', '测试', '11111333333', '', '0', '2015-06-19 16:30:57');

-- ----------------------------
-- 字典表
-- ----------------------------
DROP TABLE IF EXISTS `tb_dic_item`;
CREATE TABLE `tb_dic_item` (
  `id` int(11) NOT NULL,
  `keyCode` char(4) DEFAULT '' COMMENT 'code值',
  `keyValue` varchar(20) DEFAULT '' COMMENT '字典项的值',
  `keyDesc` varchar(100) DEFAULT '' COMMENT '字典项描述',
  `company` int(11) DEFAULT NULL COMMENT '区分项'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_dic_item
-- ----------------------------
INSERT INTO `tb_dic_item` VALUES ('1001', '01', '待付款', '待付款', '1');
INSERT INTO `tb_dic_item` VALUES ('1002', '02', '已付款', '已付款', '1');
INSERT INTO `tb_dic_item` VALUES ('1003', '03', '已审核', '已审核', '1');
INSERT INTO `tb_dic_item` VALUES ('1004', '04', '已发货', '已发货', '1');
INSERT INTO `tb_dic_item` VALUES ('1005', '05', '交易成功', '交易成功', '1');
INSERT INTO `tb_dic_item` VALUES ('1006', '06', '交易关闭', '交易关闭', '1');
INSERT INTO `tb_dic_item` VALUES ('1007', '07', '待发货', '待发货', '1');
INSERT INTO `tb_dic_item` VALUES ('1008', '08', '待收货', '待收货', '1');

-- ----------------------------
-- 商品信息表
-- ----------------------------
DROP TABLE IF EXISTS `tb_goodsInfo`;
CREATE TABLE `tb_goodsInfo` (
  `id` char(36) NOT NULL DEFAULT '' COMMENT '商品id',
  `groupId` char(36) DEFAULT '' COMMENT '组ID 用于显示一组商品集合',
  `goodsId` char(36) DEFAULT '' COMMENT '商品主Id',
  `goodsCode` char(36) DEFAULT '' COMMENT '商品编码',
  `goodsName` varchar(255) DEFAULT '' COMMENT '商品名称',
  `goodsTitle` varchar(100) DEFAULT '' COMMENT '商品标题',
  `goodsDesc` text COMMENT '商品详情',
  `goodsImgs` text COMMENT '商品图片',
  `goodsLabel` varchar(200) DEFAULT '' COMMENT '商品标签',
  `goodsCateGory` varchar(200) DEFAULT '' COMMENT '商品类别id',
  `goodsType` int(11) DEFAULT NULL COMMENT '标识商品属于实物类(0)还是虚拟类(1)',
  `goodsProp` varchar(1000) DEFAULT '' COMMENT '商品属性字符串',
  `goodsCount` int(11) DEFAULT NULL COMMENT '商品数量',
  `goodsCostPrice` double(11,2) DEFAULT NULL COMMENT '商品成本价',
  `goodsShowPrice` double(11,2) DEFAULT NULL COMMENT '商品显示价格',
  `goodsSalePrice` double(11,2) DEFAULT NULL COMMENT '商品销售价格',
  `goodsStatus` int(11) DEFAULT NULL COMMENT '商品状态（上架和下架）',
  `goodsMark` varchar(100) DEFAULT NULL COMMENT '商品备注',
  `dataStatus` int(5) DEFAULT NULL COMMENT '数据状态（0禁用，1正常）',
  `createDate` datetime DEFAULT NULL COMMENT '商品创建时间',
  `dataDate` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '数据日期',
  `companyId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_goodsInfo` (`goodsName`,`goodsTitle`,`goodsLabel`,`goodsProp`(255),`goodsSalePrice`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_goodsInfo
-- ----------------------------
INSERT INTO `tb_goodsInfo` VALUES ('0D241444-38F0-46F5-9742-F80F4E0F0E39', '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', '0D241444-38F0-46F5-9742-F80F4E0F0E39', 'D1501ECA-F2CD-469C-85BE-907BD6CE086C', '小米系列', '小米4i', '<p style=\"margin-top: 0px; margin-bottom: 2em; padding: 0px; border: 0px; font-family: Arial, Helvetica, sans-serif; font-stretch: inherit; line-height: 24px; font-size: 12px; vertical-align: baseline; color: rgb(51, 51, 51); white-space: normal;\">孙悟空拿到了金箍棒勾了自己的生死簿，那时候他看到自己的年龄是342岁，然后当弼马温半个月，天上一日地上一年，就是下界的15年。做了齐天大圣大闹蟠桃会后他回到花果山，跟猴子们说他上天半年，也就是下界的180年，然后在八卦炉被炼了49天，所以经过了49年。刘伯钦对唐三藏说五行山是王莽篡汉时候出现的，也就是说是在公元8-23年之间。这么算的话他出生应该在公元前579到564年之间，是中国的春秋时代。</p><p style=\"margin-top: 0px; margin-bottom: 2em; padding: 0px; border: 0px; font-family: Arial, Helvetica, sans-serif; font-stretch: inherit; line-height: 24px; font-size: 12px; vertical-align: baseline; color: rgb(51, 51, 51); white-space: normal;\">这些植物里，西瓜的传入时间有汉、南北朝、五代几个说法，均比春秋时代晚。石榴和葡萄是汉代张骞通西域带回中原。这三个我觉得孙悟空出世时候吃不到。</p><p><br/></p>', '/ZDesk/zh_cn/shoppingPlatform/images/0_032637.jpg,', '', '\'00B3555D-9313-4CC8-038D-F9927C770014\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '1', '参数:内存-品牌:三星-运费:顺丰', '5', '5555.00', '6999.00', '2499.00', '1', '', '1', '2015-05-18 11:44:28', '2015-06-16 18:06:01', '1');
INSERT INTO `tb_goodsInfo` VALUES ('1ED804F0-6F91-42DF-9685-DC7A161BCD37', '4056B1E4-C304-4466-8FE6-FBF26A4AFF7B', '257FF59D-974D-48A4-ABA7-39D992715628', 'F4961B96-A1B7-43B7-B065-6ED36B6671AC', '腾讯QB点卡', '200元点QB卡', '<p>eqwe</p>', '/ZDesk/zh_cn/shoppingPlatform/images/close_092358.jpg,', '', '\'9910DBE3-C6FF-F802-43E8-806B84297550\',\'1A0B499A-87FC-DFF4-77A4-E612EFFD5E52\',\'5A5A93BA-9A92-F381-6F10-A71F5C660230\'', '1', '单价:200', '11107', '11111.00', '11111.00', '11111.00', '1', '', '1', '2015-06-19 15:13:17', '2015-06-23 19:35:41', '1');
INSERT INTO `tb_goodsInfo` VALUES ('24CCC0AF-68DA-4A57-9DE7-E85F3B758AA7', 'AAC2B3FC-95FA-4B78-B030-3C1EFFED0A1E', '24CCC0AF-68DA-4A57-9DE7-E85F3B758AA7', 'D49D70D4-BA05-461B-BFF9-E891BCE7F9BE', '14号测试商品', '14号测试商品2', '<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"width: 551px;\"><tbody><tr class=\"firstRow\"><td style=\"margin: 0px; padding: 0px;\"><p style=\"margin-top: 10px;\">　　一年一度的戛纳<a href=\"http://www.pclady.com.cn/tlist/10734.html\" target=\"_blank\" class=\"cmsLink\" style=\"color: rgb(0, 0, 0); outline: none; text-decoration: none; border-bottom-width: 1px; border-bottom-style: dashed; border-bottom-color: rgb(204, 0, 102); line-height: 18px;\">国际电影节</a>马上要开幕了，每年的戛纳都能吸引不少的目光，今年华语片有贾樟柯执导的《山河故人》、侯孝贤执导的《聂隐娘》两部华语片争夺<a href=\"http://www.pclady.com.cn/tlist/9093.html\" target=\"_blank\" class=\"cmsLink\" style=\"color: rgb(0, 0, 0); outline: none; text-decoration: none; border-bottom-width: 1px; border-bottom-style: dashed; border-bottom-color: rgb(204, 0, 102); line-height: 18px;\">金棕榈</a>大奖，更是增加第68届<a href=\"http://www.pclady.com.cn/tlist/9552.html\" target=\"_blank\" class=\"cmsLink\" style=\"color: rgb(0, 0, 0); outline: none; text-decoration: none; border-bottom-width: 1px; border-bottom-style: dashed; border-bottom-color: rgb(204, 0, 102); line-height: 18px;\">戛纳电影节</a>大奖花落谁家的悬念，这两部电影的主演董子健、张震亦双双入围戛纳影帝的角逐。</p><p style=\"margin-top: 10px; text-align: center;\"><img alt=\"第68届戛纳电影节\" src=\"http://img0.pclady.com.cn/pclady/1505/13/1302610_6684168_14314174910831.jpg\" title=\"第68届戛纳电影节\" style=\"border: 1px solid black; vertical-align: top;\"/><br/>第68届<a href=\"http://www.pclady.com.cn/tlist/9553.html\" target=\"_blank\" class=\"cmsLink\" style=\"color: rgb(0, 0, 0); outline: none; text-decoration: none; border-bottom-width: 1px; border-bottom-style: dashed; border-bottom-color: rgb(204, 0, 102); line-height: 18px;\">戛纳电影</a>节</p><p style=\"margin-top: 10px;\">　　今年的戛纳电影节在中国内地的热度明显比去年高，缘由很简单，贾樟柯和侯孝贤这二位华语导演的新作都入选了戛纳的主竞赛单元，若从概率上来说，华语片获金棕榈的机会就是2/19(注：今年戛纳电影节主竞赛单元共有19部电影)。</p><p style=\"margin-top: 10px;\">　　从1946年的第一届戛纳电影节开始，红毯就占据了其中的一个部分，到如今，除了电影和颁奖礼，红毯已经成了重中之重。在红毯上，作为点睛之笔的妆发造型也得到了越来越多人的追捧。67年的戛纳红毯，67年的时代变迁，我们也能从妆发的角度来阅尽这半个多世纪以来时尚的变化。今晚第68届戛纳电影节更是见证女神的时刻!</p><p style=\"margin-top: 10px;\">　　今晚的<a href=\"http://www.pclady.com.cn/tlist/10284.html\" target=\"_blank\" class=\"cmsLink\" style=\"color: rgb(0, 0, 0); outline: none; text-decoration: none; border-bottom-width: 1px; border-bottom-style: dashed; border-bottom-color: rgb(204, 0, 102); line-height: 18px;\">红地毯</a>，贾樟柯将携主演赵涛(同时也是他老婆)、董子健等人亮相。由于有贾樟柯这么一个牛气丈夫，赵涛多次角逐戛纳影后，不知这一次是否能够成功。年纪轻轻的董子健则将凭借《山河故人》角逐影帝，悬念十足。</p></td></tr></tbody></table><p><br/></p>', '/ZDesk/zh_cn/shoppingPlatform/images/3_013061.jpg,', '14号标签2', '\'38B2CDAB-22A4-5CD6-369C-79EF3A579F76\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '0', '颜色:蓝色-尺寸:XXL', '200', '66.66', '999.00', '166.66', '1', '14号备注2', '0', '2015-05-14 03:49:43', '2015-06-16 18:06:14', '1');
INSERT INTO `tb_goodsInfo` VALUES ('257FF59D-974D-48A4-ABA7-39D992715628', '4056B1E4-C304-4466-8FE6-FBF26A4AFF7B', '257FF59D-974D-48A4-ABA7-39D992715628', 'F55CF0A9-6242-4D6F-82FF-5F792A7B5DD7', '腾讯QB点卡', '腾腾讯QB点卡100元面额腾讯QB点卡100元面额腾讯QB点卡100元面额讯QB点卡100元面额', '<p><img title=\"3_051869.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/3_051869.jpg\"/></p><p><img title=\"apple_077888.png\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/apple_077888.png\"/></p><p></p>', '/ZDesk/zh_cn/shoppingPlatform/images/3_057246.jpg,', '', '\'9910DBE3-C6FF-F802-43E8-806B84297550\',\'1A0B499A-87FC-DFF4-77A4-E612EFFD5E52\',\'5A5A93BA-9A92-F381-6F10-A71F5C660230\'', '1', '单价:100', '100', '80.00', '95.00', '92.00', '1', '', '1', '2015-06-19 15:06:06', '2015-06-26 18:24:43', '1');
INSERT INTO `tb_goodsInfo` VALUES ('2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', 'F39AB4E6-4D3D-4388-A117-27FB512F031E', '1', '1', '<p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/q6_051106.jpg\" style=\"\" title=\"q6_051106.jpg\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/q7_039640.jpg\" style=\"\" title=\"q7_039640.jpg\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/q8_094621.jpg\" style=\"\" title=\"q8_094621.jpg\"/></p><p>111111<br/></p>', '/ZDesk/zh_cn/shoppingPlatform/images/2_072841.jpg,', '1', '\'00B3555D-9313-4CC8-038D-F9927C770014\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '0', '参数:CPU-品牌:三星-运费:顺丰', '185', '1.00', '1.00', '1.00', '1', '1', '1', '2015-06-15 17:55:06', '2015-06-19 15:18:47', '1');
INSERT INTO `tb_goodsInfo` VALUES ('2BA8F4C4-BE99-4128-B59E-C22D341D71CA', '1CC6D748-E949-4076-944D-B6889317F702', 'FE6DE6DA-5EF5-4A3C-A7FC-48E6AFA6FAD6', '63B51C58-0F38-4799-93CF-820988ED5EAA', '话费充值', '话费', '<p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">韩国近日发生一起耸人听闻的案件，一男子五只脚趾失踪，警方追查后竟发现脚趾残骸在狗肚子里，难道是宠物狗袭击主人？6月1日，一男子五只脚趾失踪，家人慌忙报警。由于其患有糖尿病，足部常常失去知觉。当日他一觉醒来后，&nbsp;男子五只脚趾失踪，而警方在他家养的宠物狗体内发现了骨头碎片。据此推测，男子五只脚趾失踪，有可能是遭宠物狗啃食。也有网友推测，男子五只脚趾失踪有可能是先被切下，然后再喂了狗。目前具体案情仍在调查中。</p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\"><img src=\"http://i.guancha.cn/news/2015/06/04/20150604111631206.jpg\" style=\"margin: 0px; padding: 0px; border: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\"><span style=\"margin: 0px; padding: 0px; color: rgb(153, 153, 153);\">男子五只脚趾失踪&nbsp;在家养犬内发现骨头碎片（资料图）&nbsp;</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">据韩联社6月1日报道，一位身患严重糖尿病而卧床在家的韩国男子，上月30日一觉醒来发现自己一只脚的5只脚趾全都不见了，而凶手可能就是他的爱犬。&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">韩国庆尚南道昌原市消防总部和警察局6月1日表示，30日上午12点41分左右，他们收到一位主妇报案称，其丈夫左脚的5个脚趾头全部不见了。该主妇的丈夫金某是一位糖尿病患者，一直在家接受糖尿病及其并发症的相关治疗。&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">一位消防官到达现场后，发现屋里的被子沾有血迹，金某躺在床上，其左脚的5个脚趾已全部不见。当警察对此事件进行询问时，其家属都称“不知道”。于是警察开始对当事人家中的3只宠物犬产生了怀疑。经X光摄像确认，其中一只宠物犬的腹部内发现了很多骨头碎片。&nbsp;</p><p><br/></p>', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/q3_066347.jpg,', '', '\'45EFB086-E9EF-5992-1C5A-D4EEE9B5D2AE\',\'BDB29A94-EC87-4958-7E7D-FF9902718DB1\',\'09BD4AE2-AAC2-6826-738A-40827DD298CE\'', '1', '金额:300', '100', '0.00', '300.00', '298.00', '1', '', '1', '2015-06-04 14:02:05', '2015-06-04 14:02:05', '1');
INSERT INTO `tb_goodsInfo` VALUES ('32C9E1BA-3408-4D7E-BD06-4F7534399120', '67AE0DC0-83B6-45F8-8212-62DCEA8CC50C', '32C9E1BA-3408-4D7E-BD06-4F7534399120', '1DF26546-A941-48C3-88AB-22BFB72D645E', '石化加油卡', '新石化加油卡', '<p>石化加油卡</p><p>石化加油卡</p><p>石化加油卡</p>', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/0_017523.jpg,', '', '\'3E4D4DA6-10F9-625B-71E7-A299EFF3947A\',\'21733041-C538-3B69-FDC4-36170218E08C\',\'25C8E80C-02CD-AC69-8A38-BDF6693DC0A1\'', '0', '金额:100', '50', '0.00', '555.00', '5555.00', '1', '', '1', '2015-06-06 17:24:42', '2015-06-16 11:17:35', '1');
INSERT INTO `tb_goodsInfo` VALUES ('40634437-9579-4460-A8D9-869221708514', 'D8A0B175-4F12-4469-BFC2-1FF702BB48FC', '40634437-9579-4460-A8D9-869221708514', '1BBA4185-2EED-4800-904A-B4FB3760457E', 'QB点卡', '50元QB点卡', '<p><strong style=\"margin: 0px 0px 3px; padding: 0px; font-weight: 700; display: block;\">购买流程：</strong></p><div id=\"mall-banner\" style=\"font: 12px/18px tahoma, arial, 微软雅黑, sans-serif; margin: 0px; padding: 0px; color: rgb(64, 64, 64); text-transform: none; text-indent: 0px; letter-spacing: normal; word-spacing: 0px; white-space: normal; font-size-adjust: none; font-stretch: normal; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;\"></div><p style=\"margin: 1.12em 0px; padding: 0px; line-height: 1.4;\"><a style=\"margin: 0px; padding: 0px; outline: 0px; color: rgb(51, 85, 170); text-decoration: none;\" href=\"http://detail.tmall.com/item.htm?spm=a1z10.3-b.w4011-9422530975.33.MWxaEJ&id=4511020250&rn=daa05a4d5c43e8d8e119bd228e07445e&abbucket=12\" target=\"_blank\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img04.taobaocdn.com/imgextra/i4/284344007/TB2XCSBcVXXXXbHXXXXXXXXXXXX-284344007.jpg_.webp\"/></a><a style=\"margin: 0px; padding: 0px; outline: 0px; color: rgb(51, 85, 170); text-decoration: none;\" href=\"http://detail.tmall.com/item.htm?spm=a1z10.3-b.w4011-9422530975.34.M18njh&id=4511084756&rn=1431a923dd60aa51f54f2d7e5baa865f&abbucket=12\" target=\"_blank\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img04.taobaocdn.com/imgextra/i4/284344007/TB2hS5HcVXXXXXiXXXXXXXXXXXX-284344007.jpg_.webp\"/></a><a style=\"margin: 0px; padding: 0px; outline: 0px; color: rgb(51, 85, 170); text-decoration: none;\" href=\"http://detail.tmall.com/item.htm?spm=a1z10.3-b.w4011-9422530975.33.pBDrrp&id=4511866076&rn=928abb6891d0d712ac014da9fa19a871&abbucket=12\" target=\"_blank\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img04.taobaocdn.com/imgextra/i4/284344007/TB2J8emcVXXXXbzXpXXXXXXXXXX-284344007.jpg_.webp\"/></a><a style=\"margin: 0px; padding: 0px; outline: 0px; color: rgb(51, 85, 170); text-decoration: none;\" href=\"http://detail.tmall.com/item.htm?spm=a1z10.3-b.w4011-9422530975.75.G2LJnq&id=4512496554&rn=5150349554e4a3655d76ad7706f373d8&abbucket=12\" target=\"_blank\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img01.taobaocdn.com/imgextra/i1/284344007/TB2MBiwcVXXXXXqXpXXXXXXXXXX-284344007.jpg_.webp\"/></a></p><p style=\"margin: 1.12em 0px; padding: 0px; line-height: 1.4;\"><a style=\"margin: 0px; padding: 0px; outline: 0px; color: rgb(51, 85, 170); line-height: 1.5; text-decoration: none;\" href=\"http://favorite.taobao.com/popup/add_collection.htm?spm=a220o.1000855.1997427721.7.IhH6Gm&id=59034925&itemid=59034925&itemtype=0&ownerid=a2f55fa4014ff1c4426cc5039cca3282&scjjc=2\" target=\"_blank\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img04.taobaocdn.com/imgextra/i4/284344007/TB2hV72cFXXXXXSXXXXXXXXXXXX-284344007.jpg_.webp\"/></a><strong style=\"margin: 0px; padding: 0px; text-align: center; color: rgb(0, 0, 0); line-height: 18px; font-size: 12px; font-weight: 700;\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img03.taobaocdn.com/imgextra/i3/284344007/TB2FXkPcFXXXXbTXpXXXXXXXXXX-284344007.jpg_.webp\"/><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img02.taobaocdn.com/imgextra/i2/284344007/TB2chc_cFXXXXX2XXXXXXXXXXXX-284344007.jpg_.webp\"/><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img04.taobaocdn.com/imgextra/i4/284344007/TB2IMk5cFXXXXa5XXXXXXXXXXXX-284344007.jpg_.webp\"/></strong></p><table width=\"790\" align=\"right\" style=\"width: 551px;\"><tbody style=\"margin: 0px; padding: 0px;\"><tr class=\"firstRow\" style=\"margin: 0px; padding: 0px;\"><td style=\"border-color: rgb(0, 0, 0); margin: 0px; padding: 0px;\"><p style=\"margin: 1.12em 0px; padding: 0px; text-align: center; line-height: 1.4;\"><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">特别说明：本充值方式货源均为官方卡，故官方充值活动本卡充值一样享受</span></p><p style=\"margin: 1.12em 0px; padding: 0px; line-height: 1.4;\">&nbsp;</p><p style=\"margin: 1.12em 0px; padding: 0px; text-align: center; line-height: 1.4;\"><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">适用游戏</span><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">：</span><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">腾讯旗下所有游戏&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 游戏官网</span><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">：</span><a style=\"margin: 0px; padding: 0px; outline: 0px; text-align: left; color: rgb(51, 85, 170); line-height: 16.8px; font-size: 18px; font-weight: 700; text-decoration: none;\">http://pay.qq.com</a></p><p style=\"margin: 1.12em 0px; padding: 0px; text-align: center; line-height: 1.4;\">&nbsp;</p><p style=\"margin: 1.12em 0px; padding: 0px; text-align: center; line-height: 1.4;\"><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(255, 0, 0); line-height: 16.8px; font-size: 18px; font-weight: 700;\">计费方式<span style=\"margin: 0px; padding: 0px;\">：100</span>元/100Q币&nbsp;</span><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;充值方式</span><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">：</span><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">自动充值(一般3秒-1分钟)</span></p></td></tr></tbody></table><p><br style=\"margin: 0px; padding: 0px;\"/></p><p style=\"margin: 1.12em 0px; padding: 0px; line-height: 1.4;\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; line-height: 1.5; vertical-align: top; float: none;\" src=\"http://img01.taobaocdn.com/imgextra/i1/284344007/TB2.MQJcFXXXXbzXpXXXXXXXXXX-284344007.jpg_.webp\"/><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; line-height: 1.5; vertical-align: top; float: none;\" src=\"http://img02.taobaocdn.com/imgextra/i2/284344007/TB2ccEMcFXXXXaEXpXXXXXXXXXX-284344007.jpg_.webp\"/></p><p></p>', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/q3_031061.jpg,http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/3_014133.jpg,', '', '\'A3A50BD4-65AD-EF65-5BDE-1B20595566E4\',\'ACE38BC1-E9E7-D809-CD30-CC089CA4C778\',\'18F3CBF0-1F19-D858-7ABA-99A055F7D443\'', '1', '金额:50', '100', '0.00', '50.00', '49.00', '1', '', '1', '2015-06-04 17:58:59', '2015-06-05 11:16:53', '1');
INSERT INTO `tb_goodsInfo` VALUES ('42D58289-95DC-40C3-ACE2-D073BB140175', 'DF9C5DF2-E3CA-440C-A9DC-5B4BE215D311', '42D58289-95DC-40C3-ACE2-D073BB140175', '709AB39C-01EB-4745-89EA-B3652CD1CA99', '添加商品', '添加商品1', '<p>测试商品</p>', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/q8_031493.jpg,', '452', '\'45EFB086-E9EF-5992-1C5A-D4EEE9B5D2AE\',\'BDB29A94-EC87-4958-7E7D-FF9902718DB1\',\'09BD4AE2-AAC2-6826-738A-40827DD298CE\'', '0', '金额:50', '54', '1.52', '152.00', '4253.00', '1', '', '1', '2015-06-06 16:17:19', '2015-06-16 13:08:25', '1');
INSERT INTO `tb_goodsInfo` VALUES ('43A73297-37EF-4D8F-B276-11332706C8DC', 'B2BFB84D-F46D-4575-B61B-71630C8178EB', 'D69023FB-5966-4B26-84FA-2C94B81A8EAD', '51A9A662-B16B-4BAD-ADC7-769565528634', '中国石化加油卡', '石化加油卡冲200送100', '<p><img title=\"0_059037.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/0_059037.jpg\"/></p><p><img title=\"apple_048337.png\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/apple_048337.png\"/></p><p></p>', '/ZDesk/zh_cn/shoppingPlatform/images/q2_050518.jpg,', '', '\'4010DAC1-E38B-9F09-C120-FC59EA06A193\',\'F0B97896-DBDF-3760-2104-0DEED22F737E\',\'5A5A93BA-9A92-F381-6F10-A71F5C660230\'', '0', '单价:200', '109', '1111.00', '1111.00', '1111.00', '1', '', '1', '2015-06-19 14:58:52', '2015-06-19 15:55:15', '1');
INSERT INTO `tb_goodsInfo` VALUES ('45A81BE8-DA93-4B1A-A30A-BEBEF1821166', '25274197-F2DF-4AD3-9C5F-B60F16B86D58', '45A81BE8-DA93-4B1A-A30A-BEBEF1821166', 'C518742C-E208-47BA-ABC7-53BCA85DBB62', '锤子手机', '锤子手机T1', '<p><img title=\"apple_037706.png\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/apple_037706.png\"/></p><p><img title=\"close_069129.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/close_069129.jpg\"/></p><p></p>', '/ZDesk/zh_cn/shoppingPlatform/images/1_085795.jpg,', '', '\'00B3555D-9313-4CC8-038D-F9927C770014\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '0', '参数:CPU-品牌:三星-运费:顺丰', '1111', '111.00', '111.00', '111.00', '1', '', '1', '2015-06-17 10:18:50', '2015-06-17 10:18:50', '1');
INSERT INTO `tb_goodsInfo` VALUES ('61071154-C32D-4088-915B-463D49A6325F', '7CE2BBA0-2BBC-4382-A0CF-0D242803C567', '61071154-C32D-4088-915B-463D49A6325F', '8E9C6BC9-6301-44DD-A15D-F2D91F264D10', '苹果手机', '苹果5s', '<p>苹果5s</p>', '/ZDesk/zh_cn/shoppingPlatform/images/3_063023.jpg,/ZDesk/zh_cn/shoppingPlatform/images/3_075332.jpg,', '', '\'8F170B22-3B83-151C-93D0-34B1FC278732\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '1', '尺码:38-运费:顺丰', '9', '5.00', '43.00', '56.00', '1', '', '1', '2015-05-18 17:20:29', '2015-06-16 18:07:21', '1');
INSERT INTO `tb_goodsInfo` VALUES ('6173FE9C-2F11-4705-AD21-064B33EE2CE2', '1CC6D748-E949-4076-944D-B6889317F702', '993F7B09-8C52-4E12-9CF0-5A5B85126900', 'E5E49349-9DE6-4FC3-BF36-2B36335AFAF2', '话费充值', '657', '<p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">韩国近日发生一起耸人听闻的案件，一男子五只脚趾失踪，警方追查后竟发现脚趾残骸在狗肚子里，难道是宠物狗袭击主人？6月1日，一男子五只脚趾失踪，家人慌忙报警。由于其患有糖尿病，足部常常失去知觉。当日他一觉醒来后，&nbsp;男子五只脚趾失踪，而警方在他家养的宠物狗体内发现了骨头碎片。据此推测，男子五只脚趾失踪，有可能是遭宠物狗啃食。也有网友推测，男子五只脚趾失踪有可能是先被切下，然后再喂了狗。目前具体案情仍在调查中。</p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\"><img src=\"http://i.guancha.cn/news/2015/06/04/20150604111631206.jpg\" style=\"margin: 0px; padding: 0px; border: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\"><span style=\"margin: 0px; padding: 0px; color: rgb(153, 153, 153);\">男子五只脚趾失踪&nbsp;在家养犬内发现骨头碎片（资料图）&nbsp;</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">据韩联社6月1日报道，一位身患严重糖尿病而卧床在家的韩国男子，上月30日一觉醒来发现自己一只脚的5只脚趾全都不见了，而凶手可能就是他的爱犬。&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">韩国庆尚南道昌原市消防总部和警察局6月1日表示，30日上午12点41分左右，他们收到一位主妇报案称，其丈夫左脚的5个脚趾头全部不见了。该主妇的丈夫金某是一位糖尿病患者，一直在家接受糖尿病及其并发症的相关治疗。&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">一位消防官到达现场后，发现屋里的被子沾有血迹，金某躺在床上，其左脚的5个脚趾已全部不见。当警察对此事件进行询问时，其家属都称“不知道”。于是警察开始对当事人家中的3只宠物犬产生了怀疑。经X光摄像确认，其中一只宠物犬的腹部内发现了很多骨头碎片。&nbsp;</p><p><br/></p>', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/q3_003202.jpg,http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/QQ%E5%9B%BE%E7%89%8720150428114019_036553.jpg,', '', '\'45EFB086-E9EF-5992-1C5A-D4EEE9B5D2AE\',\'BDB29A94-EC87-4958-7E7D-FF9902718DB1\',\'09BD4AE2-AAC2-6826-738A-40827DD298CE\'', '1', '金额:100', '11', '345.00', '65.00', '76.00', '1', '', '1', '2015-06-04 14:05:37', '2015-06-16 13:08:25', '1');
INSERT INTO `tb_goodsInfo` VALUES ('66FF5E67-1957-4189-A31E-A1C912347B83', '25274197-F2DF-4AD3-9C5F-B60F16B86D58', '45A81BE8-DA93-4B1A-A30A-BEBEF1821166', '2329CCA0-7F2F-4FF6-84E8-02F130DB95A3', '锤子手机', '锤子T2', '<p>1213</p>', '/ZDesk/zh_cn/shoppingPlatform/images/1_024256.jpg,', '', '\'00B3555D-9313-4CC8-038D-F9927C770014\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '0', '参数:内存-品牌:三星-运费:顺丰', '12234', '34543.00', '23423.00', '2344.00', '1', '', '1', '2015-06-17 12:07:46', '2015-06-17 12:07:46', '1');
INSERT INTO `tb_goodsInfo` VALUES ('6D7EA884-BD4F-4D70-9E3E-6DB9E6AA5E2F', '11C28E22-5A10-4E56-8210-1885372A953D', '6D7EA884-BD4F-4D70-9E3E-6DB9E6AA5E2F', '0E391D92-A2D4-4414-BDDD-829097EAB749', 'p8', '1', '<p>1</p>', '/ZDesk/zh_cn/shoppingPlatform/images/3_057246.jpg,', 'p8', '\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '1', '运费:EMS', '11108', '1.00', '1.00', '1.00', '1', 'p8p8p8p8p8p8p8p8p8p8p8p8p8p8p8p8p8p8p8', '1', '2015-06-19 14:59:19', '2015-06-29 10:50:20', '1');
INSERT INTO `tb_goodsInfo` VALUES ('733B19D1-196E-42AE-880B-A02ECBB3EB05', '3A0B3CF1-ED12-4C86-A19B-E217D27AE2AE', '733B19D1-196E-42AE-880B-A02ECBB3EB05', 'A9D344EA-9D97-4366-981C-54922D26BDC5', 'p7', 'p7', '<p>12`1</p>', '/ZDesk/zh_cn/shoppingPlatform/images/3_057246.jpg,/ZDesk/zh_cn/shoppingPlatform/images/3_057246.jpg,', '1', '\'25C8E80C-02CD-AC69-8A38-BDF6693DC0A1\'', '0', '金额:50', '110', '1.00', '1.00', '1.00', '1', '1', '1', '2015-06-19 15:01:21', '2015-06-29 10:50:45', '1');
INSERT INTO `tb_goodsInfo` VALUES ('919581C5-40C2-46BD-905F-A64F45DF8211', 'B341A323-35C8-49B8-8AD6-C926064B2FF1', '919581C5-40C2-46BD-905F-A64F45DF8211', 'F35D5EA5-F00C-411D-BDE1-BA6E53D0DDC0', '三星手机', 'S6', '<p><img title=\"3_098741.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/3_098741.jpg\"/></p><p><img title=\"apple_091637.png\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/apple_091637.png\"/></p><p></p>', '/ZDesk/zh_cn/shoppingPlatform/images/0%20-%20副本_067800.jpg,', '', '\'8F170B22-3B83-151C-93D0-34B1FC278732\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '1', '尺码:42-运费:申通', '110', '111.00', '111.00', '111.00', '1', '', '1', '2015-06-16 18:40:39', '2015-06-19 15:55:15', '1');
INSERT INTO `tb_goodsInfo` VALUES ('993F7B09-8C52-4E12-9CF0-5A5B85126900', '1CC6D748-E949-4076-944D-B6889317F702', 'FE6DE6DA-5EF5-4A3C-A7FC-48E6AFA6FAD6', '5DA7F276-4821-4551-8DAF-EAC1B998D39E', '话费充值', '214', '<p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">韩国近日发生一起耸人听闻的案件，一男子五只脚趾失踪，警方追查后竟发现脚趾残骸在狗肚子里，难道是宠物狗袭击主人？6月1日，一男子五只脚趾失踪，家人慌忙报警。由于其患有糖尿病，足部常常失去知觉。当日他一觉醒来后，&nbsp;男子五只脚趾失踪，而警方在他家养的宠物狗体内发现了骨头碎片。据此推测，男子五只脚趾失踪，有可能是遭宠物狗啃食。也有网友推测，男子五只脚趾失踪有可能是先被切下，然后再喂了狗。目前具体案情仍在调查中。</p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\"><img src=\"http://i.guancha.cn/news/2015/06/04/20150604111631206.jpg\" style=\"margin: 0px; padding: 0px; border: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\"><span style=\"margin: 0px; padding: 0px; color: rgb(153, 153, 153);\">男子五只脚趾失踪&nbsp;在家养犬内发现骨头碎片（资料图）&nbsp;</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">据韩联社6月1日报道，一位身患严重糖尿病而卧床在家的韩国男子，上月30日一觉醒来发现自己一只脚的5只脚趾全都不见了，而凶手可能就是他的爱犬。&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">韩国庆尚南道昌原市消防总部和警察局6月1日表示，30日上午12点41分左右，他们收到一位主妇报案称，其丈夫左脚的5个脚趾头全部不见了。该主妇的丈夫金某是一位糖尿病患者，一直在家接受糖尿病及其并发症的相关治疗。&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 27.2000007629395px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">一位消防官到达现场后，发现屋里的被子沾有血迹，金某躺在床上，其左脚的5个脚趾已全部不见。当警察对此事件进行询问时，其家属都称“不知道”。于是警察开始对当事人家中的3只宠物犬产生了怀疑。经X光摄像确认，其中一只宠物犬的腹部内发现了很多骨头碎片。&nbsp;</p><p><br/></p>', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/q3_003202.jpg,', '', '\'45EFB086-E9EF-5992-1C5A-D4EEE9B5D2AE\',\'BDB29A94-EC87-4958-7E7D-FF9902718DB1\',\'09BD4AE2-AAC2-6826-738A-40827DD298CE\'', '1', '金额:200', '3', '5.00', '20.00', '121.00', '1', '', '1', '2015-06-04 14:05:09', '2015-06-04 14:05:09', '1');
INSERT INTO `tb_goodsInfo` VALUES ('9B4A3B9B-202A-48F4-B34E-C600B16C445C', 'ECAC90AC-A85D-4F49-B59E-6E3F033D53EE', '9B4A3B9B-202A-48F4-B34E-C600B16C445C', 'C38F57E6-F9FD-4066-BC00-D48CABBB6974', '服装', '皇帝的新装', '<p>112222222222222</p><p><img title=\"q1_043666.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/q1_043666.jpg\"/></p><p><img title=\"q2_002244.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/q2_002244.jpg\"/></p><p></p>', '/ZDesk/zh_cn/shoppingPlatform/images/1_080945.jpg,', '', '\'38B2CDAB-22A4-5CD6-369C-79EF3A579F76\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '1', '颜色:红色-尺寸:XXL-运费:申通', '110', '111.00', '111.00', '111.00', '1', '', '1', '2015-06-17 10:43:17', '2015-06-19 15:55:15', '1');
INSERT INTO `tb_goodsInfo` VALUES ('A06D8023-6827-49AA-A2F4-1658E44C24B7', '3A0B3CF1-ED12-4C86-A19B-E217D27AE2AE', '733B19D1-196E-42AE-880B-A02ECBB3EB05', 'C4D21345-BA12-4D97-AC4F-D461BACFE482', 'p7', 'asd', '<p>1111</p><br/>', '/zh_cn/shoppingPlatform/images/0%20-%20副本_067800.jpg,/ZDesk/zh_cn/shoppingPlatform/pc/category/,', '1', '\'25C8E80C-02CD-AC69-8A38-BDF6693DC0A1\'', '0', '金额:50', '111', '1.00', '1.00', '1.00', '0', '11', '1', '2015-06-19 15:01:46', '2015-06-19 15:11:09', '1');
INSERT INTO `tb_goodsInfo` VALUES ('B27A7245-7F18-4DF1-B70C-6E89B9D90B75', '55F3734A-A52A-4661-8986-B52E0F4C55C2', 'D197D9B8-AA34-4022-95F2-8FE4C805FB2F', 'BCC5903D-A7F8-4F0C-AE52-87C3471ED702', '测试图片路径', '追加一个商品', '<p><img title=\"0 - 副本_025749.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/0 - 副本_025749.jpg\"/></p><p><img title=\"0_018275.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/0_018275.jpg\"/></p><p><img title=\"1_006212.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/1_006212.jpg\"/></p><p>&nbsp;</p>', '/ZDesk/zh_cn/shoppingPlatform/images/1_071336.jpg,/ZDesk/zh_cn/shoppingPlatform/pc/category/,', '', '\'00B3555D-9313-4CC8-038D-F9927C770014\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '0', '参数:内存-品牌:苹果-运费:申通', '111', '111.00', '111.00', '111.00', '1', '', '1', '2015-06-16 18:30:43', '2015-06-16 18:34:49', '1');
INSERT INTO `tb_goodsInfo` VALUES ('C39E1C10-8557-4191-995B-A2B5DEF3E9C4', '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', 'E3E2D1D9-D08F-49CF-80AA-E8D31909799F', '4BD06BA6-EE7B-42DD-8C1E-F19A3F7A07A3', '小米系列', '测试添加异常', '<p>测试添加异常</p>', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/0_052275.jpg,', '', '\'00B3555D-9313-4CC8-038D-F9927C770014\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '0', '参数:CPU-品牌:小米-运费:EMS', '99', '22.16', '565.00', '23.00', '1', '', '1', '2015-06-06 16:10:51', '2015-06-12 17:50:39', '1');
INSERT INTO `tb_goodsInfo` VALUES ('CB42C5A8-E878-4763-A91B-EB73DEACEFF8', '098622D1-49E3-49A8-AFF2-836CC672A05E', 'CB42C5A8-E878-4763-A91B-EB73DEACEFF8', '047703F6-9E72-4B21-B118-FA2323106B8D', '一加手机', '一加手机E1', '<p><img title=\"apple_036255.png\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/apple_036255.png\"/></p><p><img title=\"q1_010361.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/q1_010361.jpg\"/></p><p></p>', '/ZDesk/zh_cn/shoppingPlatform/images/3_093088.jpg,', '', '\'8F170B22-3B83-151C-93D0-34B1FC278732\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '0', '尺码:39-运费:申通', '110', '111.00', '111.00', '1111.00', '1', '', '1', '2015-06-17 10:37:10', '2015-06-19 15:55:15', '1');
INSERT INTO `tb_goodsInfo` VALUES ('D0397B5B-8327-45D7-A20C-20E9936B8E6B', '1CC6D748-E949-4076-944D-B6889317F702', 'D0397B5B-8327-45D7-A20C-20E9936B8E6B', '1842E292-DB2A-4F6A-B123-6A92B53FAB65', '话费充值', '电信话费充值', '<p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 25px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">韩国近日发生一起耸人听闻的案件，一男子五只脚趾失踪，警方追查后竟发现脚趾残骸在狗肚子里，难道是宠物狗袭击主人？6月1日，一男子五只脚趾失踪，家人慌忙报警。由于其患有糖尿病，足部常常失去知觉。当日他一觉醒来后，&nbsp;男子五只脚趾失踪，而警方在他家养的宠物狗体内发现了骨头碎片。据此推测，男子五只脚趾失踪，有可能是遭宠物狗啃食。也有网友推测，男子五只脚趾失踪有可能是先被切下，然后再喂了狗。目前具体案情仍在调查中。</p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 25px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\"><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/remote.jpg\" style=\"margin: 0px; padding: 0px; border: 0px;\"/></p><p style=\"text-align:center;margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 25px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\"><span style=\"margin: 0px; padding: 0px; color: rgb(153, 153, 153);\">男子五只脚趾失踪&nbsp;在家养犬内发现骨头碎片（资料图）&nbsp;</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 25px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">据韩联社6月1日报道，一位身患严重糖尿病而卧床在家的韩国男子，上月30日一觉醒来发现自己一只脚的5只脚趾全都不见了，而凶手可能就是他的爱犬。&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 25px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">韩国庆尚南道昌原市消防总部和警察局6月1日表示，30日上午12点41分左右，他们收到一位主妇报案称，其丈夫左脚的5个脚趾头全部不见了。该主妇的丈夫金某是一位糖尿病患者，一直在家接受糖尿病及其并发症的相关治疗。&nbsp;</p><p style=\"margin-top: 0px; margin-bottom: 0px; padding: 0px 0px 20px; line-height: 25px; text-indent: 2em; font-family: 宋体; white-space: normal; background-color: rgb(252, 251, 240);\">一位消防官到达现场后，发现屋里的被子沾有血迹，金某躺在床上，其左脚的5个脚趾已全部不见。当警察对此事件进行询问时，其家属都称“不知道”。于是警察开始对当事人家中的3只宠物犬产生了怀疑。经X光摄像确认，其中一只宠物犬的腹部内发现了很多骨头碎片。&nbsp;</p><p><br/></p>', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/q9_063629.jpg,', '', '\'45EFB086-E9EF-5992-1C5A-D4EEE9B5D2AE\',\'BDB29A94-EC87-4958-7E7D-FF9902718DB1\',\'09BD4AE2-AAC2-6826-738A-40827DD298CE\'', '1', '金额:100', '999', '0.00', '100.00', '99.00', '1', '', '1', '2015-06-04 12:48:17', '2015-06-04 14:10:12', '1');
INSERT INTO `tb_goodsInfo` VALUES ('D197D9B8-AA34-4022-95F2-8FE4C805FB2F', '55F3734A-A52A-4661-8986-B52E0F4C55C2', 'D197D9B8-AA34-4022-95F2-8FE4C805FB2F', '1F4BE4DE-474D-44E5-A1C9-E1063CB173F8', '测试图片路径', '测试图片路径', '<p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/0_093503.jpg\" style=\"\" title=\"0_093503.jpg\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/1_037768.jpg\" style=\"\" title=\"1_037768.jpg\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/2_011346.jpg\" style=\"\" title=\"2_011346.jpg\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/3_035093.jpg\" style=\"\" title=\"3_035093.jpg\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/apple_053378.png\" style=\"\" title=\"apple_053378.png\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/close_060055.png\" style=\"\" title=\"close_060055.png\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/close_018137.jpg\" style=\"\" title=\"close_018137.jpg\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/left_040385.png\" style=\"\" title=\"left_040385.png\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/q1_019003.jpg\" style=\"\" title=\"q1_019003.jpg\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/q2_026800.jpg\" style=\"\" title=\"q2_026800.jpg\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/q3 - 副本_063310.jpg\" style=\"\" title=\"q3 - 副本_063310.jpg\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/q3_018970.jpg\" style=\"\" title=\"q3_018970.jpg\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/q4_031260.jpg\" style=\"\" title=\"q4_031260.jpg\"/></p><p><img src=\"/ZDesk/zh_cn/shoppingPlatform/images/q5_056071.jpg\" style=\"\" title=\"q5_056071.jpg\"/></p><p><br/></p>', '/ZDesk/zh_cn/shoppingPlatform/images/q3_079395.jpg,/ZDesk/zh_cn/shoppingPlatform/images/apple_012112.png,/ZDesk/zh_cn/shoppingPlatform/images/1_022310.jpg,', '', '\'00B3555D-9313-4CC8-038D-F9927C770014\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '1', '参数:CPU-品牌:三星-运费:EMS', '111', '111.00', '111.00', '111.00', '1', '', '1', '2015-06-16 18:09:29', '2015-06-16 18:09:29', '1');
INSERT INTO `tb_goodsInfo` VALUES ('D67DC4D8-BE20-4048-BD5D-B843B4575009', '7FC8B9F8-064E-493E-B352-4BACA83421F1', 'D67DC4D8-BE20-4048-BD5D-B843B4575009', '40401565-635F-4E2C-8AFC-1AE656B26A17', '测试插入数据', '测试插入数据', '<p>测试插入数据</p><p>测试插入数据</p><p>测试插入数据</p><p>测试插入数据</p>', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/3_075332.jpg,', '', '\'00B3555D-9313-4CC8-038D-F9927C770014\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '1', '参数:CPU-品牌:锤子-运费:顺丰', '143', '923.10', '0.00', '999.00', '1', '', '0', '2015-05-19 10:59:03', '2015-06-16 11:00:51', '1');
INSERT INTO `tb_goodsInfo` VALUES ('D69023FB-5966-4B26-84FA-2C94B81A8EAD', 'B2BFB84D-F46D-4575-B61B-71630C8178EB', 'D69023FB-5966-4B26-84FA-2C94B81A8EAD', '850AF17E-B650-4992-846E-1AD5E392E4E7', '中国石化加油卡', '中国石化加油卡100元储值卡', '<p><img title=\"1_082346.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/1_082346.jpg\"/></p><p><img title=\"2_001298.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/2_001298.jpg\"/></p><p style=\"text-align: left;\"></p>', '/ZDesk/zh_cn/shoppingPlatform/images/3_048660.jpg,', '', '\'4010DAC1-E38B-9F09-C120-FC59EA06A193\',\'F0B97896-DBDF-3760-2104-0DEED22F737E\',\'5A5A93BA-9A92-F381-6F10-A71F5C660230\'', '0', '单价:100', '1111', '111.00', '111.00', '1112.00', '1', '', '1', '2015-06-19 14:42:37', '2015-06-19 14:42:37', '1');
INSERT INTO `tb_goodsInfo` VALUES ('DA9803D9-BA2E-44FB-ABC7-C605E0301864', 'AA2FDF08-817D-40E5-A0DA-9176EA6BF159', 'DA9803D9-BA2E-44FB-ABC7-C605E0301864', '6136BD3F-3206-4F55-8430-0FA590AC4E68', '加油卡', '100元加油卡', '<p><strong style=\"margin: 0px 0px 3px; padding: 0px; font-weight: 700; display: block;\">购买流程：</strong></p><div id=\"mall-banner\" style=\"font: 12px/18px tahoma, arial, 微软雅黑, sans-serif; margin: 0px; padding: 0px; color: rgb(64, 64, 64); text-transform: none; text-indent: 0px; letter-spacing: normal; word-spacing: 0px; white-space: normal; font-size-adjust: none; font-stretch: normal; background-color: rgb(255, 255, 255); -webkit-text-stroke-width: 0px;\"></div><p style=\"margin: 1.12em 0px; padding: 0px; line-height: 1.4;\"><a style=\"margin: 0px; padding: 0px; outline: 0px; color: rgb(51, 85, 170); text-decoration: none;\" href=\"http://detail.tmall.com/item.htm?spm=a1z10.3-b.w4011-9422530975.33.MWxaEJ&id=4511020250&rn=daa05a4d5c43e8d8e119bd228e07445e&abbucket=12\" target=\"_blank\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img04.taobaocdn.com/imgextra/i4/284344007/TB2XCSBcVXXXXbHXXXXXXXXXXXX-284344007.jpg_.webp\"/></a><a style=\"margin: 0px; padding: 0px; outline: 0px; color: rgb(51, 85, 170); text-decoration: none;\" href=\"http://detail.tmall.com/item.htm?spm=a1z10.3-b.w4011-9422530975.34.M18njh&id=4511084756&rn=1431a923dd60aa51f54f2d7e5baa865f&abbucket=12\" target=\"_blank\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img04.taobaocdn.com/imgextra/i4/284344007/TB2hS5HcVXXXXXiXXXXXXXXXXXX-284344007.jpg_.webp\"/></a><a style=\"margin: 0px; padding: 0px; outline: 0px; color: rgb(51, 85, 170); text-decoration: none;\" href=\"http://detail.tmall.com/item.htm?spm=a1z10.3-b.w4011-9422530975.33.pBDrrp&id=4511866076&rn=928abb6891d0d712ac014da9fa19a871&abbucket=12\" target=\"_blank\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img04.taobaocdn.com/imgextra/i4/284344007/TB2J8emcVXXXXbzXpXXXXXXXXXX-284344007.jpg_.webp\"/></a><a style=\"margin: 0px; padding: 0px; outline: 0px; color: rgb(51, 85, 170); text-decoration: none;\" href=\"http://detail.tmall.com/item.htm?spm=a1z10.3-b.w4011-9422530975.75.G2LJnq&id=4512496554&rn=5150349554e4a3655d76ad7706f373d8&abbucket=12\" target=\"_blank\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img01.taobaocdn.com/imgextra/i1/284344007/TB2MBiwcVXXXXXqXpXXXXXXXXXX-284344007.jpg_.webp\"/></a></p><p style=\"margin: 1.12em 0px; padding: 0px; line-height: 1.4;\"><a style=\"margin: 0px; padding: 0px; outline: 0px; color: rgb(51, 85, 170); line-height: 1.5; text-decoration: none;\" href=\"http://favorite.taobao.com/popup/add_collection.htm?spm=a220o.1000855.1997427721.7.IhH6Gm&id=59034925&itemid=59034925&itemtype=0&ownerid=a2f55fa4014ff1c4426cc5039cca3282&scjjc=2\" target=\"_blank\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img04.taobaocdn.com/imgextra/i4/284344007/TB2hV72cFXXXXXSXXXXXXXXXXXX-284344007.jpg_.webp\"/></a><strong style=\"margin: 0px; padding: 0px; text-align: center; color: rgb(0, 0, 0); line-height: 18px; font-size: 12px; font-weight: 700;\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img03.taobaocdn.com/imgextra/i3/284344007/TB2FXkPcFXXXXbTXpXXXXXXXXXX-284344007.jpg_.webp\"/><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img02.taobaocdn.com/imgextra/i2/284344007/TB2chc_cFXXXXX2XXXXXXXXXXXX-284344007.jpg_.webp\"/><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; vertical-align: top; float: none;\" src=\"http://img04.taobaocdn.com/imgextra/i4/284344007/TB2IMk5cFXXXXa5XXXXXXXXXXXX-284344007.jpg_.webp\"/></strong></p><table width=\"790\" align=\"right\" style=\"width: 551px;\"><tbody style=\"margin: 0px; padding: 0px;\"><tr class=\"firstRow\" style=\"margin: 0px; padding: 0px;\"><td style=\"border-color: rgb(0, 0, 0); margin: 0px; padding: 0px;\"><p style=\"margin: 1.12em 0px; padding: 0px; text-align: center; line-height: 1.4;\"><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">特别说明：本充值方式货源均为官方卡，故官方充值活动本卡充值一样享受</span></p><p style=\"margin: 1.12em 0px; padding: 0px; line-height: 1.4;\">&nbsp;</p><p style=\"margin: 1.12em 0px; padding: 0px; text-align: center; line-height: 1.4;\"><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">适用游戏</span><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">：</span><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">腾讯旗下所有游戏&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 游戏官网</span><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">：</span><a style=\"margin: 0px; padding: 0px; outline: 0px; text-align: left; color: rgb(51, 85, 170); line-height: 16.8px; font-size: 18px; font-weight: 700; text-decoration: none;\">http://pay.qq.com</a></p><p style=\"margin: 1.12em 0px; padding: 0px; text-align: center; line-height: 1.4;\">&nbsp;</p><p style=\"margin: 1.12em 0px; padding: 0px; text-align: center; line-height: 1.4;\"><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(255, 0, 0); line-height: 16.8px; font-size: 18px; font-weight: 700;\">计费方式<span style=\"margin: 0px; padding: 0px;\">：100</span>元/100Q币&nbsp;</span><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;充值方式</span><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">：</span><span style=\"margin: 0px; padding: 0px; text-align: left; color: rgb(64, 64, 64); line-height: 16.8px; font-size: 18px; font-weight: 700;\">自动充值(一般3秒-1分钟)</span></p></td></tr></tbody></table><p><br style=\"margin: 0px; padding: 0px;\"/></p><p style=\"margin: 1.12em 0px; padding: 0px; line-height: 1.4;\"><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; line-height: 1.5; vertical-align: top; float: none;\" src=\"http://img01.taobaocdn.com/imgextra/i1/284344007/TB2.MQJcFXXXXbzXpXXXXXXXXXX-284344007.jpg_.webp\"/><img align=\"absmiddle\" style=\"margin: 0px; padding: 0px; border: 0px currentColor; border-image: none; line-height: 1.5; vertical-align: top; float: none;\" src=\"http://img02.taobaocdn.com/imgextra/i2/284344007/TB2ccEMcFXXXXaEXpXXXXXXXXXX-284344007.jpg_.webp\"/></p><p></p>', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/2_020154.jpg,', '', '\'5A8B6F5D-2A8E-88C7-E626-C349C870FAC1\',\'D6F0D0E3-9693-CC34-60A8-53D5A54D51E5\',\'18F3CBF0-1F19-D858-7ABA-99A055F7D443\'', '0', '金额:100', '96', '50.00', '100.00', '99.00', '1', '', '1', '2015-06-04 18:01:57', '2015-06-10 14:11:32', '1');
INSERT INTO `tb_goodsInfo` VALUES ('DE1B1DB7-D088-4E86-B286-4D645603755A', 'D8A0B175-4F12-4469-BFC2-1FF702BB48FC', '40634437-9579-4460-A8D9-869221708514', '66EDE978-511F-4CCD-A863-8BD9D4812551', 'QB点卡', '100元点卡', '<p>100块</p>', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/3_076834.jpg,', '', '\'A3A50BD4-65AD-EF65-5BDE-1B20595566E4\',\'ACE38BC1-E9E7-D809-CD30-CC089CA4C778\',\'18F3CBF0-1F19-D858-7ABA-99A055F7D443\'', '1', '金额:100', '96', '0.00', '100.00', '98.00', '1', '', '1', '2015-06-04 18:13:27', '2015-06-10 14:07:16', '1');
INSERT INTO `tb_goodsInfo` VALUES ('EF0C9216-9A7C-4709-86FA-86828532C6F8', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', '85EDBC11-D72A-4530-B93E-5459DA928FC1', '1', '追加商品', '<p><img title=\"3_073683.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/3_073683.jpg\"/></p><p><img title=\"apple_095702.png\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/apple_095702.png\"/></p><p></p>', '/ZDesk/zh_cn/shoppingPlatform/images/apple_023405.png,', '', '\'00B3555D-9313-4CC8-038D-F9927C770014\',\'AB1110A4-8290-38E9-A2D5-D28E19396404\'', '0', '参数:内存-品牌:锤子-运费:顺丰', '109', '111.00', '111.00', '111.00', '1', '', '1', '2015-06-16 18:36:57', '2015-06-19 15:55:15', '1');
INSERT INTO `tb_goodsInfo` VALUES ('F58388B0-ADC2-4FBB-9CED-7C5D6F27A1E6', 'B2BFB84D-F46D-4575-B61B-71630C8178EB', 'D69023FB-5966-4B26-84FA-2C94B81A8EAD', 'C4C45684-6332-4BD5-AB92-24396804A711', '中国石化加油卡', '石化加油卡200元储值卡', '<p><img title=\"q1_098179.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/q1_098179.jpg\"/></p><p><img title=\"q2_001886.jpg\" src=\"/ZDesk/zh_cn/shoppingPlatform/images/q2_001886.jpg\"/></p><p></p>', '/ZDesk/zh_cn/shoppingPlatform/images/3_049280.jpg,', '', '\'4010DAC1-E38B-9F09-C120-FC59EA06A193\',\'F0B97896-DBDF-3760-2104-0DEED22F737E\',\'5A5A93BA-9A92-F381-6F10-A71F5C660230\'', '0', '单价:200', '123', '1234.00', '1234.00', '12345.00', '1', '', '1', '2015-06-19 14:50:29', '2015-06-19 14:50:29', '1');

-- ----------------------------
-- 商品属性表
-- ----------------------------
DROP TABLE IF EXISTS `tb_goodsProp`;
CREATE TABLE `tb_goodsProp` (
  `id` char(36) NOT NULL DEFAULT '',
  `groupId` char(36) DEFAULT '',
  `goodsId` char(36) DEFAULT '' COMMENT '商品id',
  `goodsCode` char(36) DEFAULT '' COMMENT '商品编码',
  `goodsName` varchar(100) DEFAULT '' COMMENT '商品名称',
  `goodsImgs` text COMMENT '商品图片',
  `propKey` varchar(50) DEFAULT '' COMMENT '属性key值',
  `propValue` varchar(50) DEFAULT '' COMMENT '属性value值',
  `addDate` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  `companyId` int(11) NOT NULL COMMENT '部门或',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_goodsProp
-- ----------------------------
INSERT INTO `tb_goodsProp` VALUES ('08306e5e-fc38-406c-ba91-f780674ea5d3', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', 'EF0C9216-9A7C-4709-86FA-86828532C6F8', '85EDBC11-D72A-4530-B93E-5459DA928FC1', '1', '/ZDesk/zh_cn/shoppingPlatform/images/apple_023405.png,', '运费', '顺丰', '2015-06-16 18:36:57', '1');
INSERT INTO `tb_goodsProp` VALUES ('10877bb4-72fd-43e3-843d-cdbc40e7b4b6', '55F3734A-A52A-4661-8986-B52E0F4C55C2', 'B27A7245-7F18-4DF1-B70C-6E89B9D90B75', 'BCC5903D-A7F8-4F0C-AE52-87C3471ED702', '测试图片路径', '/ZDesk/zh_cn/shoppingPlatform/images/1_071336.jpg,', '运费', '申通', '2015-06-16 18:30:43', '1');
INSERT INTO `tb_goodsProp` VALUES ('1bb17b36-b95e-4a32-9548-e9827495783e', '11C28E22-5A10-4E56-8210-1885372A953D', '6D7EA884-BD4F-4D70-9E3E-6DB9E6AA5E2F', '0E391D92-A2D4-4414-BDDD-829097EAB749', 'p8', '/ZDesk/zh_cn/shoppingPlatform/images/QQ%E6%88%AA%E5%9B%BE20150615164331_053026.png,', '运费', 'EMS', '2015-06-19 14:59:19', '1');
INSERT INTO `tb_goodsProp` VALUES ('2081a65d-ebe9-4948-8fbc-5ec01586b627', 'B341A323-35C8-49B8-8AD6-C926064B2FF1', '919581C5-40C2-46BD-905F-A64F45DF8211', 'F35D5EA5-F00C-411D-BDE1-BA6E53D0DDC0', '三星手机', '/ZDesk/zh_cn/shoppingPlatform/images/0%20-%20副本_067800.jpg,', '运费', '申通', '2015-06-16 18:40:39', '1');
INSERT INTO `tb_goodsProp` VALUES ('2ee50521-c675-4cf9-b149-cb74e454e0ec', '1CC65D2B-8170-486A-939F-6455ACDBF917', 'F3DA0C19-BCF2-4FF4-AE99-E775A6725FD6', '088F7038-183F-4499-9D0E-6E59217542C2', '测试删除商品', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/1_094708.jpg,', '运费', '顺丰', '2015-05-18 17:05:28', '1');
INSERT INTO `tb_goodsProp` VALUES ('3266acf8-c99d-455c-8e09-02cf2f3088b1', '1CC65D2B-8170-486A-939F-6455ACDBF917', '466115a7-bc09-4ed5-8f7e-56f9d19b99ed', '088F7038-183F-4499-9D0E-6E59217542C2', '测试删除商品', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/1_094708.jpg,', '运费', '顺丰', '2015-05-18 17:07:11', '1');
INSERT INTO `tb_goodsProp` VALUES ('3521a29b-dd07-401d-ad2c-7f8c1dad693b', '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', '0D241444-38F0-46F5-9742-F80F4E0F0E39', 'D1501ECA-F2CD-469C-85BE-907BD6CE086C', '小米系列', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/0_032637.jpg,', '参数', '内存', '2015-05-18 11:44:28', '1');
INSERT INTO `tb_goodsProp` VALUES ('3bf7b317-3c2b-43b0-bcc4-7535dbee2726', '55F3734A-A52A-4661-8986-B52E0F4C55C2', 'B27A7245-7F18-4DF1-B70C-6E89B9D90B75', 'BCC5903D-A7F8-4F0C-AE52-87C3471ED702', '测试图片路径', '/ZDesk/zh_cn/shoppingPlatform/images/1_071336.jpg,', '品牌', '苹果', '2015-06-16 18:30:43', '1');
INSERT INTO `tb_goodsProp` VALUES ('45fa6e7b-1500-4396-bca0-088e13e4b17b', '55F3734A-A52A-4661-8986-B52E0F4C55C2', 'D197D9B8-AA34-4022-95F2-8FE4C805FB2F', '1F4BE4DE-474D-44E5-A1C9-E1063CB173F8', '测试图片路径', '/ZDesk/zh_cn/shoppingPlatform/images/q3_079395.jpg,/ZDesk/zh_cn/shoppingPlatform/images/apple_012112.png,/ZDesk/zh_cn/shoppingPlatform/images/1_022310.jpg,', '品牌', '三星', '2015-06-16 18:09:29', '1');
INSERT INTO `tb_goodsProp` VALUES ('470a4f25-90a0-44ec-8d3a-025cd4ac8ab3', '1CC65D2B-8170-486A-939F-6455ACDBF917', '1FFD5527-60A9-4B87-BE1D-59CED6659139', '61684211-70A1-4286-97DA-375929852706', '测试删除商品', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/x_025862.jpg,', '品牌', '小米', '2015-05-18 17:10:12', '1');
INSERT INTO `tb_goodsProp` VALUES ('4a4e6526-48d2-4e8c-ac33-e92cea8892be', '7CE2BBA0-2BBC-4382-A0CF-0D242803C567', '61071154-C32D-4088-915B-463D49A6325F', '8E9C6BC9-6301-44DD-A15D-F2D91F264D10', '苹果手机', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/apple_040254.png,', '运费', '顺丰', '2015-05-18 17:20:29', '1');
INSERT INTO `tb_goodsProp` VALUES ('53526040-7c2f-4ffa-b377-8bd2c4fdaeb8', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', 'EF0C9216-9A7C-4709-86FA-86828532C6F8', '85EDBC11-D72A-4530-B93E-5459DA928FC1', '1', '/ZDesk/zh_cn/shoppingPlatform/images/apple_023405.png,', '参数', '内存', '2015-06-16 18:36:57', '1');
INSERT INTO `tb_goodsProp` VALUES ('56f9ca1d-1f10-4976-b17d-ad5b55d916e2', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', 'F39AB4E6-4D3D-4388-A117-27FB512F031E', '1', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/2_072841.jpg,', '运费', '顺丰', '2015-06-15 17:55:06', '1');
INSERT INTO `tb_goodsProp` VALUES ('5a146fb7-de40-4917-81aa-f1cff0244591', '4056B1E4-C304-4466-8FE6-FBF26A4AFF7B', '257FF59D-974D-48A4-ABA7-39D992715628', 'F55CF0A9-6242-4D6F-82FF-5F792A7B5DD7', '腾讯QB点卡', '/ZDesk/zh_cn/shoppingPlatform/images/3_057246.jpg,', '单价', '100', '2015-06-19 15:06:06', '1');
INSERT INTO `tb_goodsProp` VALUES ('5fdb205b-7cb9-4d1c-a0a5-4bc3c86a4d46', '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', 'C39E1C10-8557-4191-995B-A2B5DEF3E9C4', '4BD06BA6-EE7B-42DD-8C1E-F19A3F7A07A3', '小米系列', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/0_052275.jpg,', '运费', 'EMS', '2015-06-06 16:10:52', '1');
INSERT INTO `tb_goodsProp` VALUES ('6211b6c3-a041-4b8d-a9d3-535cb017eb01', '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', 'E5E49349-9DE6-4FC3-BF36-2B36335AFAF2', '话费充值', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/q3_003202.jpg,http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/QQ%E5%9B%BE%E7%89%8720150428114019_036553.jpg,', '金额', '100', '2015-06-04 14:05:37', '1');
INSERT INTO `tb_goodsProp` VALUES ('645d95df-9abc-44fa-8e33-123cb0e360d3', '25274197-F2DF-4AD3-9C5F-B60F16B86D58', '66FF5E67-1957-4189-A31E-A1C912347B83', '2329CCA0-7F2F-4FF6-84E8-02F130DB95A3', '锤子手机', '/ZDesk/zh_cn/shoppingPlatform/images/1_024256.jpg,', '运费', '顺丰', '2015-06-17 12:07:46', '1');
INSERT INTO `tb_goodsProp` VALUES ('66c6d535-539e-46f5-a534-24e0ae8db6e5', 'B2BFB84D-F46D-4575-B61B-71630C8178EB', 'D69023FB-5966-4B26-84FA-2C94B81A8EAD', '850AF17E-B650-4992-846E-1AD5E392E4E7', '中国石化加油卡', '/ZDesk/zh_cn/shoppingPlatform/images/3_048660.jpg,', '单价', '100', '2015-06-19 14:42:37', '1');
INSERT INTO `tb_goodsProp` VALUES ('6d711ab0-c607-4d93-b06f-00b2922ea9c7', '25274197-F2DF-4AD3-9C5F-B60F16B86D58', '45A81BE8-DA93-4B1A-A30A-BEBEF1821166', 'C518742C-E208-47BA-ABC7-53BCA85DBB62', '锤子手机', '/ZDesk/zh_cn/shoppingPlatform/images/1_085795.jpg,', '运费', '顺丰', '2015-06-17 10:18:50', '1');
INSERT INTO `tb_goodsProp` VALUES ('70126f7f-670d-4191-a4df-27c283a1009c', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', 'F39AB4E6-4D3D-4388-A117-27FB512F031E', '1', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/2_072841.jpg,', '参数', 'CPU', '2015-06-15 17:55:06', '1');
INSERT INTO `tb_goodsProp` VALUES ('70bb8bc2-689c-4471-99ca-d24c6119f909', 'B2BFB84D-F46D-4575-B61B-71630C8178EB', 'F58388B0-ADC2-4FBB-9CED-7C5D6F27A1E6', 'C4C45684-6332-4BD5-AB92-24396804A711', '中国石化加油卡', '/ZDesk/zh_cn/shoppingPlatform/images/3_049280.jpg,', '单价', '200', '2015-06-19 14:50:29', '1');
INSERT INTO `tb_goodsProp` VALUES ('71ddcff1-4980-4a79-92a4-d18ab58a0ad3', 'AA2FDF08-817D-40E5-A0DA-9176EA6BF159', 'DA9803D9-BA2E-44FB-ABC7-C605E0301864', '6136BD3F-3206-4F55-8430-0FA590AC4E68', '加油卡', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/2_020154.jpg,', '金额', '100', '2015-06-04 18:01:57', '1');
INSERT INTO `tb_goodsProp` VALUES ('75eca683-79d5-4d50-8480-f790731e8adb', 'AAC2B3FC-95FA-4B78-B030-3C1EFFED0A1E', '24CCC0AF-68DA-4A57-9DE7-E85F3B758AA7', 'D49D70D4-BA05-461B-BFF9-E891BCE7F9BE', '14号测试商品', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/3_013061.jpg,', '颜色', '蓝色', '2015-05-14 03:49:43', '1');
INSERT INTO `tb_goodsProp` VALUES ('763e6cbb-ef6d-47f5-b5b7-ad0175a6fd45', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', 'EF0C9216-9A7C-4709-86FA-86828532C6F8', '85EDBC11-D72A-4530-B93E-5459DA928FC1', '1', '/ZDesk/zh_cn/shoppingPlatform/images/apple_023405.png,', '品牌', '锤子', '2015-06-16 18:36:57', '1');
INSERT INTO `tb_goodsProp` VALUES ('7933001c-d879-4e37-b723-9dec12e743b7', '3A0B3CF1-ED12-4C86-A19B-E217D27AE2AE', 'A06D8023-6827-49AA-A2F4-1658E44C24B7', 'C4D21345-BA12-4D97-AC4F-D461BACFE482', 'p7', '/zh_cn/shoppingPlatform/images/0%20-%20副本_067800.jpg,', '金额', '50', '2015-06-19 15:01:46', '1');
INSERT INTO `tb_goodsProp` VALUES ('8299734a-0647-4fe0-b907-150ca55418e3', '55F3734A-A52A-4661-8986-B52E0F4C55C2', 'B27A7245-7F18-4DF1-B70C-6E89B9D90B75', 'BCC5903D-A7F8-4F0C-AE52-87C3471ED702', '测试图片路径', '/ZDesk/zh_cn/shoppingPlatform/images/1_071336.jpg,', '参数', '内存', '2015-06-16 18:30:43', '1');
INSERT INTO `tb_goodsProp` VALUES ('87f4e3b7-c249-4d88-bc1b-7a48f653da34', '545ACC84-B6D5-48AA-9B48-566570EABADB', '3C79EF52-C007-48A8-8F03-06C9BABA5256', 'E8C15792-ACD3-4261-B090-0BCA47AF1CC1', '运动鞋', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/3_081670.jpg,', '运费', '顺丰', '2015-05-15 11:34:31', '1');
INSERT INTO `tb_goodsProp` VALUES ('888b3507-a8c6-4f9a-9624-2b6fb93a2a50', '1CC65D2B-8170-486A-939F-6455ACDBF917', 'F3DA0C19-BCF2-4FF4-AE99-E775A6725FD6', '088F7038-183F-4499-9D0E-6E59217542C2', '测试删除商品', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/1_094708.jpg,', '品牌', '三星', '2015-05-18 17:05:28', '1');
INSERT INTO `tb_goodsProp` VALUES ('8b5052dc-3d0c-4126-9140-5887808c90b1', '1CC65D2B-8170-486A-939F-6455ACDBF917', 'F3DA0C19-BCF2-4FF4-AE99-E775A6725FD6', '088F7038-183F-4499-9D0E-6E59217542C2', '测试删除商品', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/1_094708.jpg,', '参数', '内存', '2015-05-18 17:05:28', '1');
INSERT INTO `tb_goodsProp` VALUES ('8f6a62ed-a0a8-44f8-8d99-d4116190fda2', 'ECAC90AC-A85D-4F49-B59E-6E3F033D53EE', '9B4A3B9B-202A-48F4-B34E-C600B16C445C', 'C38F57E6-F9FD-4066-BC00-D48CABBB6974', '服装', '/ZDesk/zh_cn/shoppingPlatform/images/1_080945.jpg,', '尺寸', 'XXL', '2015-06-17 10:43:17', '1');
INSERT INTO `tb_goodsProp` VALUES ('9ed68cff-7f20-4541-b329-c7603dc318fd', '1CC65D2B-8170-486A-939F-6455ACDBF917', '466115a7-bc09-4ed5-8f7e-56f9d19b99ed', '088F7038-183F-4499-9D0E-6E59217542C2', '测试删除商品', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/1_094708.jpg,', '参数', '内存', '2015-05-18 17:07:11', '1');
INSERT INTO `tb_goodsProp` VALUES ('9f46b42c-0b4e-4af3-88f8-054031b07889', '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', 'C39E1C10-8557-4191-995B-A2B5DEF3E9C4', '4BD06BA6-EE7B-42DD-8C1E-F19A3F7A07A3', '小米系列', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/0_052275.jpg,', '参数', 'CPU', '2015-06-06 16:10:51', '1');
INSERT INTO `tb_goodsProp` VALUES ('a71a40ca-add7-4ca1-83ee-8121df1b4950', '1CC6D748-E949-4076-944D-B6889317F702', '993F7B09-8C52-4E12-9CF0-5A5B85126900', '5DA7F276-4821-4551-8DAF-EAC1B998D39E', '话费充值', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/q3_003202.jpg,', '金额', '200', '2015-06-04 14:05:09', '1');
INSERT INTO `tb_goodsProp` VALUES ('aa147cc4-6b7c-40f8-8fca-70339db79687', '3A0B3CF1-ED12-4C86-A19B-E217D27AE2AE', '733B19D1-196E-42AE-880B-A02ECBB3EB05', 'A9D344EA-9D97-4366-981C-54922D26BDC5', 'p7', '/zh_cn/shoppingPlatform/images/0%20-%20副本_067800.jpg,', '金额', '50', '2015-06-19 15:01:21', '1');
INSERT INTO `tb_goodsProp` VALUES ('aecd88ae-0d82-4c41-8a1f-4d8fb0c70559', 'D8A0B175-4F12-4469-BFC2-1FF702BB48FC', '40634437-9579-4460-A8D9-869221708514', '1BBA4185-2EED-4800-904A-B4FB3760457E', 'QB点卡', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/q3_031061.jpg,http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/3_014133.jpg,', '金额', '50', '2015-06-04 17:58:59', '1');
INSERT INTO `tb_goodsProp` VALUES ('b0c828b2-fb9a-4309-a8f7-05926f8e2541', '098622D1-49E3-49A8-AFF2-836CC672A05E', 'CB42C5A8-E878-4763-A91B-EB73DEACEFF8', '047703F6-9E72-4B21-B118-FA2323106B8D', '一加手机', '/ZDesk/zh_cn/shoppingPlatform/images/3_093088.jpg,', '尺码', '39', '2015-06-17 10:37:10', '1');
INSERT INTO `tb_goodsProp` VALUES ('b1388fd8-c232-41a7-b15c-7e189fa53104', '1CC65D2B-8170-486A-939F-6455ACDBF917', '1FFD5527-60A9-4B87-BE1D-59CED6659139', '61684211-70A1-4286-97DA-375929852706', '测试删除商品', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/x_025862.jpg,', '运费', '顺丰', '2015-05-18 17:10:12', '1');
INSERT INTO `tb_goodsProp` VALUES ('b56e369c-aa54-4c56-8191-43758f960efd', '1CC65D2B-8170-486A-939F-6455ACDBF917', '1FFD5527-60A9-4B87-BE1D-59CED6659139', '61684211-70A1-4286-97DA-375929852706', '测试删除商品', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/x_025862.jpg,', '参数', 'CPU', '2015-05-18 17:10:12', '1');
INSERT INTO `tb_goodsProp` VALUES ('bccc780a-501a-443a-b8d6-4ffbb08fd374', 'ECAC90AC-A85D-4F49-B59E-6E3F033D53EE', '9B4A3B9B-202A-48F4-B34E-C600B16C445C', 'C38F57E6-F9FD-4066-BC00-D48CABBB6974', '服装', '/ZDesk/zh_cn/shoppingPlatform/images/1_080945.jpg,', '颜色', '红色', '2015-06-17 10:43:17', '1');
INSERT INTO `tb_goodsProp` VALUES ('bd43e484-c8df-49bd-8316-5b7358383b2b', 'B341A323-35C8-49B8-8AD6-C926064B2FF1', '919581C5-40C2-46BD-905F-A64F45DF8211', 'F35D5EA5-F00C-411D-BDE1-BA6E53D0DDC0', '三星手机', '/ZDesk/zh_cn/shoppingPlatform/images/0%20-%20副本_067800.jpg,', '尺码', '42', '2015-06-16 18:40:39', '1');
INSERT INTO `tb_goodsProp` VALUES ('bdf08361-0079-4fa8-9a60-9651b75dc0e1', 'D8A0B175-4F12-4469-BFC2-1FF702BB48FC', 'DE1B1DB7-D088-4E86-B286-4D645603755A', '66EDE978-511F-4CCD-A863-8BD9D4812551', 'QB点卡', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/3_076834.jpg,', '金额', '100', '2015-06-04 18:13:27', '1');
INSERT INTO `tb_goodsProp` VALUES ('be17eb34-e2d7-402b-953c-8bfae493ff68', 'DF9C5DF2-E3CA-440C-A9DC-5B4BE215D311', '42D58289-95DC-40C3-ACE2-D073BB140175', '709AB39C-01EB-4745-89EA-B3652CD1CA99', '添加商品', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/q8_031493.jpg,', '金额', '50', '2015-06-06 16:17:20', '1');
INSERT INTO `tb_goodsProp` VALUES ('c2cbfb91-be17-4a8b-8514-e1f0699d7812', '25274197-F2DF-4AD3-9C5F-B60F16B86D58', '45A81BE8-DA93-4B1A-A30A-BEBEF1821166', 'C518742C-E208-47BA-ABC7-53BCA85DBB62', '锤子手机', '/ZDesk/zh_cn/shoppingPlatform/images/1_085795.jpg,', '品牌', '三星', '2015-06-17 10:18:50', '1');
INSERT INTO `tb_goodsProp` VALUES ('ca7bfe3b-1b0d-49a8-b645-e3fa9e0bf7e0', '098622D1-49E3-49A8-AFF2-836CC672A05E', 'CB42C5A8-E878-4763-A91B-EB73DEACEFF8', '047703F6-9E72-4B21-B118-FA2323106B8D', '一加手机', '/ZDesk/zh_cn/shoppingPlatform/images/3_093088.jpg,', '运费', '申通', '2015-06-17 10:37:10', '1');
INSERT INTO `tb_goodsProp` VALUES ('d3215ec6-7339-4f92-b8b0-1ee4894491e2', '7CE2BBA0-2BBC-4382-A0CF-0D242803C567', '61071154-C32D-4088-915B-463D49A6325F', '8E9C6BC9-6301-44DD-A15D-F2D91F264D10', '苹果手机', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/apple_040254.png,', '尺码', '38', '2015-05-18 17:20:29', '1');
INSERT INTO `tb_goodsProp` VALUES ('d4193c83-9fe4-4978-bb04-f4bbb244ea24', '1CC6D748-E949-4076-944D-B6889317F702', '2BA8F4C4-BE99-4128-B59E-C22D341D71CA', '63B51C58-0F38-4799-93CF-820988ED5EAA', '话费充值', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/q3_066347.jpg,', '金额', '300', '2015-06-04 14:02:05', '1');
INSERT INTO `tb_goodsProp` VALUES ('dc22fead-878d-4d33-957c-ec0e6486b301', '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', '0D241444-38F0-46F5-9742-F80F4E0F0E39', 'D1501ECA-F2CD-469C-85BE-907BD6CE086C', '小米系列', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/0_032637.jpg,', '运费', '顺丰', '2015-05-18 11:44:28', '1');
INSERT INTO `tb_goodsProp` VALUES ('dc341637-3098-4878-b0aa-add29c8b311e', '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', '0D241444-38F0-46F5-9742-F80F4E0F0E39', 'D1501ECA-F2CD-469C-85BE-907BD6CE086C', '小米系列', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/0_032637.jpg,', '品牌', '三星', '2015-05-18 11:44:28', '1');
INSERT INTO `tb_goodsProp` VALUES ('de151bce-e780-48e4-88b8-eace3fc8fcfb', '1CC6D748-E949-4076-944D-B6889317F702', 'D0397B5B-8327-45D7-A20C-20E9936B8E6B', '1842E292-DB2A-4F6A-B123-6A92B53FAB65', '话费充值', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/q9_063629.jpg,', '金额', '100', '2015-06-04 12:48:17', '1');
INSERT INTO `tb_goodsProp` VALUES ('e3de9e8c-9fbb-4cbc-89dc-e8ac7a1adb86', '4056B1E4-C304-4466-8FE6-FBF26A4AFF7B', '1ED804F0-6F91-42DF-9685-DC7A161BCD37', 'F4961B96-A1B7-43B7-B065-6ED36B6671AC', '腾讯QB点卡', '/ZDesk/zh_cn/shoppingPlatform/images/close_092358.jpg,', '单价', '200', '2015-06-19 15:13:17', '1');
INSERT INTO `tb_goodsProp` VALUES ('e86486f4-ea8a-43bc-9f8c-8920a933d266', 'B2BFB84D-F46D-4575-B61B-71630C8178EB', '43A73297-37EF-4D8F-B276-11332706C8DC', '51A9A662-B16B-4BAD-ADC7-769565528634', '中国石化加油卡', '/ZDesk/zh_cn/shoppingPlatform/images/q2_050518.jpg,', '单价', '200', '2015-06-19 14:58:52', '1');
INSERT INTO `tb_goodsProp` VALUES ('ec48470b-f855-4cd8-94c8-4ae9b57972d7', 'AAC2B3FC-95FA-4B78-B030-3C1EFFED0A1E', '24CCC0AF-68DA-4A57-9DE7-E85F3B758AA7', 'D49D70D4-BA05-461B-BFF9-E891BCE7F9BE', '14号测试商品', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/3_013061.jpg,', '尺寸', 'XXL', '2015-05-14 03:49:43', '1');
INSERT INTO `tb_goodsProp` VALUES ('ed09d624-cc2d-4e7d-9920-5e96ad62e916', 'ECAC90AC-A85D-4F49-B59E-6E3F033D53EE', '9B4A3B9B-202A-48F4-B34E-C600B16C445C', 'C38F57E6-F9FD-4066-BC00-D48CABBB6974', '服装', '/ZDesk/zh_cn/shoppingPlatform/images/1_080945.jpg,', '运费', '申通', '2015-06-17 10:43:17', '1');
INSERT INTO `tb_goodsProp` VALUES ('f171883a-4a75-4301-b76a-a55dae4bdcaf', '25274197-F2DF-4AD3-9C5F-B60F16B86D58', '66FF5E67-1957-4189-A31E-A1C912347B83', '2329CCA0-7F2F-4FF6-84E8-02F130DB95A3', '锤子手机', '/ZDesk/zh_cn/shoppingPlatform/images/1_024256.jpg,', '参数', '内存', '2015-06-17 12:07:46', '1');
INSERT INTO `tb_goodsProp` VALUES ('f6c3bca7-76f0-413e-9499-193a7b4b6471', '25274197-F2DF-4AD3-9C5F-B60F16B86D58', '45A81BE8-DA93-4B1A-A30A-BEBEF1821166', 'C518742C-E208-47BA-ABC7-53BCA85DBB62', '锤子手机', '/ZDesk/zh_cn/shoppingPlatform/images/1_085795.jpg,', '参数', 'CPU', '2015-06-17 10:18:50', '1');
INSERT INTO `tb_goodsProp` VALUES ('f7e8a2dc-dfd7-42e3-98b2-7e7d4b110539', '1CC65D2B-8170-486A-939F-6455ACDBF917', '466115a7-bc09-4ed5-8f7e-56f9d19b99ed', '088F7038-183F-4499-9D0E-6E59217542C2', '测试删除商品', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/1_094708.jpg,', '品牌', '三星', '2015-05-18 17:07:11', '1');
INSERT INTO `tb_goodsProp` VALUES ('f9f3344c-4c94-497d-9fd2-0fbe10f0067e', '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', 'F39AB4E6-4D3D-4388-A117-27FB512F031E', '1', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/2_072841.jpg,', '品牌', '三星', '2015-06-15 17:55:06', '1');
INSERT INTO `tb_goodsProp` VALUES ('fa3dcbae-eb1d-4331-9fa9-6fb08ccabd0f', '545ACC84-B6D5-48AA-9B48-566570EABADB', '3C79EF52-C007-48A8-8F03-06C9BABA5256', 'E8C15792-ACD3-4261-B090-0BCA47AF1CC1', '运动鞋', 'http://127.0.0.1:8888/ZDesk/zh_cn/shoppingPlatform/images/3_081670.jpg,', '尺码', '38', '2015-05-15 11:34:31', '1');
INSERT INTO `tb_goodsProp` VALUES ('fccc55f9-b92a-4614-ae2e-1302f8a51635', '55F3734A-A52A-4661-8986-B52E0F4C55C2', 'D197D9B8-AA34-4022-95F2-8FE4C805FB2F', '1F4BE4DE-474D-44E5-A1C9-E1063CB173F8', '测试图片路径', '/ZDesk/zh_cn/shoppingPlatform/images/q3_079395.jpg,/ZDesk/zh_cn/shoppingPlatform/images/apple_012112.png,/ZDesk/zh_cn/shoppingPlatform/images/1_022310.jpg,', '运费', 'EMS', '2015-06-16 18:09:29', '1');
INSERT INTO `tb_goodsProp` VALUES ('fdd2906f-0cc3-4299-a6e9-08780471c858', '25274197-F2DF-4AD3-9C5F-B60F16B86D58', '66FF5E67-1957-4189-A31E-A1C912347B83', '2329CCA0-7F2F-4FF6-84E8-02F130DB95A3', '锤子手机', '/ZDesk/zh_cn/shoppingPlatform/images/1_024256.jpg,', '品牌', '三星', '2015-06-17 12:07:46', '1');
INSERT INTO `tb_goodsProp` VALUES ('fdeb6391-dc30-4cfb-9aa9-21c5d4f7645d', '55F3734A-A52A-4661-8986-B52E0F4C55C2', 'D197D9B8-AA34-4022-95F2-8FE4C805FB2F', '1F4BE4DE-474D-44E5-A1C9-E1063CB173F8', '测试图片路径', '/ZDesk/zh_cn/shoppingPlatform/images/q3_079395.jpg,/ZDesk/zh_cn/shoppingPlatform/images/apple_012112.png,/ZDesk/zh_cn/shoppingPlatform/images/1_022310.jpg,', '参数', 'CPU', '2015-06-16 18:09:29', '1');
INSERT INTO `tb_goodsProp` VALUES ('fe9dc5c1-a8f9-41d0-afe6-a19c3ecefc11', '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', 'C39E1C10-8557-4191-995B-A2B5DEF3E9C4', '4BD06BA6-EE7B-42DD-8C1E-F19A3F7A07A3', '小米系列', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/0_052275.jpg,', '品牌', '小米', '2015-06-06 16:10:52', '1');
INSERT INTO `tb_goodsProp` VALUES ('fec2d3e4-c13b-4359-828c-3fd13d9a8a80', '67AE0DC0-83B6-45F8-8212-62DCEA8CC50C', '32C9E1BA-3408-4D7E-BD06-4F7534399120', '1DF26546-A941-48C3-88AB-22BFB72D645E', '石化加油卡', 'http://192.168.0.80:8888/ZDesk/zh_cn/shoppingPlatform/images/0_017523.jpg,', '金额', '100', '2015-06-06 17:24:42', '1');

-- ----------------------------
-- 商品记录表
-- ----------------------------
DROP TABLE IF EXISTS `tb_goodsRecord`;
CREATE TABLE `tb_goodsRecord` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderNumber` varchar(64) DEFAULT '' COMMENT '订单编号',
  `orderId` bigint(20) DEFAULT NULL COMMENT '订单Id',
  `groupId` char(36) NOT NULL DEFAULT '' COMMENT '商品组id',
  `goodsId` char(36) NOT NULL COMMENT '商品id',
  `goodsName` varchar(200) DEFAULT '' COMMENT '商品标题',
  `goodsCostPrice` double(20,2) DEFAULT NULL COMMENT '成本价',
  `goodsSalePrice` double(20,2) DEFAULT NULL COMMENT '销售价',
  `saleCount` int(11) DEFAULT NULL COMMENT '销售数量',
  `updateCount` int(11) DEFAULT NULL COMMENT '更改数量',
  `state` varchar(20) DEFAULT '' COMMENT '数据状态',
  `addDate` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_goodsRecord
-- ----------------------------
INSERT INTO `tb_goodsRecord` VALUES ('35', '', null, '7FC8B9F8-064E-493E-B352-4BACA83421F1', 'D67DC4D8-BE20-4048-BD5D-B843B4575009', '测试插入数据', '923.10', '999.00', '3', '-3', '', '2015-06-03 12:05:28');
INSERT INTO `tb_goodsRecord` VALUES ('36', '', null, '7FC8B9F8-064E-493E-B352-4BACA83421F1', 'D67DC4D8-BE20-4048-BD5D-B843B4575009', '测试插入数据', '923.10', '999.00', '3', '-3', '', '2015-06-03 12:05:28');
INSERT INTO `tb_goodsRecord` VALUES ('37', '', null, '7FC8B9F8-064E-493E-B352-4BACA83421F1', 'D67DC4D8-BE20-4048-BD5D-B843B4575009', '测试插入数据', '923.10', '999.00', '3', '-3', '', '2015-06-03 12:27:09');
INSERT INTO `tb_goodsRecord` VALUES ('38', '', null, '7FC8B9F8-064E-493E-B352-4BACA83421F1', 'D67DC4D8-BE20-4048-BD5D-B843B4575009', '测试插入数据', '923.10', '999.00', '3', '-3', '', '2015-06-03 12:27:09');
INSERT INTO `tb_goodsRecord` VALUES ('39', '', null, '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', 'E3E2D1D9-D08F-49CF-80AA-E8D31909799F', '小米系列', '2400.00', '2999.00', '3', '-3', '', '2015-06-03 12:27:09');
INSERT INTO `tb_goodsRecord` VALUES ('40', '', null, '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', 'E3E2D1D9-D08F-49CF-80AA-E8D31909799F', '小米系列', '2400.00', '2999.00', '3', '-3', '', '2015-06-03 12:27:09');
INSERT INTO `tb_goodsRecord` VALUES ('41', '', null, '7FC8B9F8-064E-493E-B352-4BACA83421F1', 'D67DC4D8-BE20-4048-BD5D-B843B4575009', '测试插入数据', '923.10', '999.00', '1', '-1', '', '2015-06-04 11:10:34');
INSERT INTO `tb_goodsRecord` VALUES ('42', '', null, '1CC6D748-E949-4076-944D-B6889317F702', '993F7B09-8C52-4E12-9CF0-5A5B85126900', '话费充值', '5.00', '121.00', '0', '3', '商品创建', '2015-06-04 14:05:09');
INSERT INTO `tb_goodsRecord` VALUES ('43', '', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '0', '34', '商品创建', '2015-06-04 14:05:37');
INSERT INTO `tb_goodsRecord` VALUES ('44', '', null, 'D8A0B175-4F12-4469-BFC2-1FF702BB48FC', '40634437-9579-4460-A8D9-869221708514', 'QB点卡', '0.00', '49.00', '0', '100', '商品创建', '2015-06-04 17:58:59');
INSERT INTO `tb_goodsRecord` VALUES ('45', '', null, 'AA2FDF08-817D-40E5-A0DA-9176EA6BF159', 'DA9803D9-BA2E-44FB-ABC7-C605E0301864', '加油卡', '50.00', '99.00', '0', '100', '商品创建', '2015-06-04 18:01:57');
INSERT INTO `tb_goodsRecord` VALUES ('46', '', null, 'D8A0B175-4F12-4469-BFC2-1FF702BB48FC', 'DE1B1DB7-D088-4E86-B286-4D645603755A', 'QB点卡', '0.00', '98.00', '0', '100', '商品创建', '2015-06-04 18:13:27');
INSERT INTO `tb_goodsRecord` VALUES ('47', '', null, '7FC8B9F8-064E-493E-B352-4BACA83421F1', 'D67DC4D8-BE20-4048-BD5D-B843B4575009', '测试插入数据', '923.10', '999.00', '1', '-1', '', '2015-06-04 14:03:56');
INSERT INTO `tb_goodsRecord` VALUES ('48', 'D2AFE8F2-16A5-B8EF-39E5-1D8AEA57D3FE', null, 'D8A0B175-4F12-4469-BFC2-1FF702BB48FC', 'DE1B1DB7-D088-4E86-B286-4D645603755A', 'QB点卡', '0.00', '98.00', '1', '-1', '创建订单', '2015-06-05 12:03:45');
INSERT INTO `tb_goodsRecord` VALUES ('49', '', null, '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', 'C39E1C10-8557-4191-995B-A2B5DEF3E9C4', '小米系列', '22.16', '23.00', '0', '100', '商品创建', '2015-06-06 16:10:52');
INSERT INTO `tb_goodsRecord` VALUES ('50', '', null, 'DF9C5DF2-E3CA-440C-A9DC-5B4BE215D311', '42D58289-95DC-40C3-ACE2-D073BB140175', '添加商品', '1.52', '4253.00', '0', '1', '商品创建', '2015-06-06 16:17:20');
INSERT INTO `tb_goodsRecord` VALUES ('51', '93F24DE0-08F8-79F1-273E-81187D678D2C', null, '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', 'E3E2D1D9-D08F-49CF-80AA-E8D31909799F', '小米系列', '2400.00', '2999.00', '1', '-1', '创建订单', '2015-06-06 16:28:36');
INSERT INTO `tb_goodsRecord` VALUES ('52', '7192A786-7457-00F5-3670-7F86B3A98727', null, 'AA2FDF08-817D-40E5-A0DA-9176EA6BF159', 'DA9803D9-BA2E-44FB-ABC7-C605E0301864', '加油卡', '50.00', '99.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('53', '58C32AC8-A6ED-ED37-9861-EB6CB1DD5636', null, 'AA2FDF08-817D-40E5-A0DA-9176EA6BF159', 'DA9803D9-BA2E-44FB-ABC7-C605E0301864', '加油卡', '50.00', '99.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('54', '29BBF506-B9DC-64F2-50A3-03595FB0F5C5', null, 'AA2FDF08-817D-40E5-A0DA-9176EA6BF159', 'DA9803D9-BA2E-44FB-ABC7-C605E0301864', '加油卡', '50.00', '99.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('55', '', null, '67AE0DC0-83B6-45F8-8212-62DCEA8CC50C', 'CF89E06B-CDBE-435C-B04A-40BE9CD991CC', '石化加油卡', '0.00', '2222.00', '0', '100', '商品创建', '2015-06-06 17:23:28');
INSERT INTO `tb_goodsRecord` VALUES ('56', '', null, '67AE0DC0-83B6-45F8-8212-62DCEA8CC50C', '32C9E1BA-3408-4D7E-BD06-4F7534399120', '石化加油卡', '0.00', '5555.00', '0', '50', '商品创建', '2015-06-06 17:24:42');
INSERT INTO `tb_goodsRecord` VALUES ('57', '2B8C2BCA-0AD2-820F-672D-641D9839AA52', null, '7CE2BBA0-2BBC-4382-A0CF-0D242803C567', '61071154-C32D-4088-915B-463D49A6325F', '苹果手机', '5.00', '56.00', '2', '-2', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('58', 'AE4F5753-4A2F-E5BE-2105-8E20BD8916FF', null, 'D8A0B175-4F12-4469-BFC2-1FF702BB48FC', 'DE1B1DB7-D088-4E86-B286-4D645603755A', 'QB点卡', '0.00', '98.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('59', 'C91962ED-4DDD-7431-1B6D-23E321EA14BC', null, 'D8A0B175-4F12-4469-BFC2-1FF702BB48FC', 'DE1B1DB7-D088-4E86-B286-4D645603755A', 'QB点卡', '0.00', '98.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('60', '7E2DEF02-D945-D7A1-B3B4-1775B5AA8242', null, 'DF9C5DF2-E3CA-440C-A9DC-5B4BE215D311', '42D58289-95DC-40C3-ACE2-D073BB140175', '添加商品', '1.52', '4253.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('61', 'F2C04F82-47BB-EC3C-3849-FDFA7936C35F', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('62', 'B4EFFE57-B4AA-7BAF-5A4B-BE934BECBDDC', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('63', '6D2E3660-C85F-95C0-D324-918A47A75052', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('64', '5F1E61C6-7B41-B5BE-6F61-DF56E9F85290', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('65', 'E75A3E08-9AC8-4624-11B2-9CB7B0098284', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('66', '7B887E9E-36CF-1818-6A8A-7CF66B686AF3', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('67', 'ED0771DE-D37A-FB18-5AA2-8F5C8E838F83', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('68', 'CDECA693-6A40-E5D0-6363-F59DE70AD0FD', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('69', 'BBDE5399-7A68-9E7C-A22D-59B06A6BAF24', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('70', 'D3DEFA6D-8896-C1CE-C42F-1C5376A0B172', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('71', '22A0ADFD-BDF8-A17A-570B-769A46A90408', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('72', '9D8D526D-9244-2D29-950F-48149F2C5A36', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('73', 'F1D2F732-12FC-78D8-C7A6-9AA9119149B6', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('74', 'DED2415F-749B-43FB-3DC5-53D5DBD62C71', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('75', 'DC399967-CA28-3A3C-68E6-843D3761F7D7', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('76', 'A0567180-7813-34CB-FF63-938E226F6C1D', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('77', '6E58D836-089F-CCC4-9CC2-EACC561067BD', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('78', '99E3AA0B-ACFC-0D7D-48B1-30F327F22CE2', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('79', '8899782C-6570-2186-79A1-380722DC0FAF', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('80', 'E8B70186-454E-D8A0-8965-D8B93012BB31', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('81', '05B626A4-919A-227D-3CD1-3EF2BB426722', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('82', '08EA7BA4-767A-6E6E-11E5-B85E3ADC1E55', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-06 16:39:45');
INSERT INTO `tb_goodsRecord` VALUES ('83', '', null, 'AA2FDF08-817D-40E5-A0DA-9176EA6BF159', 'DA9803D9-BA2E-44FB-ABC7-C605E0301864', '加油卡', '50.00', '99.00', '-1', '1', '取消订单', '2015-06-08 16:21:29');
INSERT INTO `tb_goodsRecord` VALUES ('84', 'ECFF8A80-D94E-8138-B762-C2F61949AAC9', null, 'D8A0B175-4F12-4469-BFC2-1FF702BB48FC', 'DE1B1DB7-D088-4E86-B286-4D645603755A', 'QB点卡', '0.00', '98.00', '1', '-1', '创建订单', '2015-06-10 14:07:16');
INSERT INTO `tb_goodsRecord` VALUES ('85', '52CC86DA-34DB-7ABE-3D9A-B70CFFC25A2B', null, 'AA2FDF08-817D-40E5-A0DA-9176EA6BF159', 'DA9803D9-BA2E-44FB-ABC7-C605E0301864', '加油卡', '50.00', '99.00', '1', '-1', '创建订单', '2015-06-10 14:11:32');
INSERT INTO `tb_goodsRecord` VALUES ('86', '20150623193541234', null, '7CE2BBA0-2BBC-4382-A0CF-0D242803C567', '61071154-C32D-4088-915B-463D49A6325F', '苹果手机', '5.00', '56.00', '1', '-1', '创建订单', '2015-06-26 15:08:02');
INSERT INTO `tb_goodsRecord` VALUES ('87', '45646151678948646', null, '00B811CF-AE66-4F6E-89EF-9F7DF7BD3785', 'C39E1C10-8557-4191-995B-A2B5DEF3E9C4', '小米系列', '22.16', '23.00', '1', '-1', '创建订单', '2015-06-26 15:15:39');
INSERT INTO `tb_goodsRecord` VALUES ('88', '45646151678948646', null, '7FC8B9F8-064E-493E-B352-4BACA83421F1', 'D67DC4D8-BE20-4048-BD5D-B843B4575009', '测试插入数据', '923.10', '999.00', '2', '-2', '创建订单', '2015-06-26 15:15:41');
INSERT INTO `tb_goodsRecord` VALUES ('89', '', null, '3B6EC5D6-E960-47E7-8F55-BE84E7D61031', 'B0059086-7C87-4F5F-9AC8-E6CA87C0621D', '虚拟加油卡充值', '52.50', '999.00', '0', '100', '商品创建', '2015-06-15 12:50:54');
INSERT INTO `tb_goodsRecord` VALUES ('90', '', null, '3B6EC5D6-E960-47E7-8F55-BE84E7D61031', '350A9E06-2E03-45F0-BA14-C518C57AB364', '虚拟加油卡充值', '112.00', '6567.00', '0', '11', '商品创建', '2015-06-15 12:52:37');
INSERT INTO `tb_goodsRecord` VALUES ('91', '', null, '3B6EC5D6-E960-47E7-8F55-BE84E7D61031', '280E3C2F-76EA-4845-86E7-C50062B0645F', '虚拟加油卡充值', '13.50', '59.00', '0', '52', '商品创建', '2015-06-15 12:54:12');
INSERT INTO `tb_goodsRecord` VALUES ('92', '', null, '3B6EC5D6-E960-47E7-8F55-BE84E7D61031', 'C364ECFC-9574-4D72-B366-DE4C1D280C74', '虚拟加油卡充值', '11.00', '111.00', '0', '11', '商品创建', '2015-06-15 13:01:18');
INSERT INTO `tb_goodsRecord` VALUES ('93', '', null, '3B6EC5D6-E960-47E7-8F55-BE84E7D61031', '48780963-7255-4073-9D31-91F08FD853C7', '虚拟加油卡充值', '345.00', '1123.00', '0', '1234', '商品创建', '2015-06-15 17:34:26');
INSERT INTO `tb_goodsRecord` VALUES ('94', '', null, '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', '1', '1.00', '1.00', '0', '1', '商品创建', '2015-06-15 17:55:06');
INSERT INTO `tb_goodsRecord` VALUES ('95', '87456465464878946', null, '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', '1', '1.00', '1.00', '1', '-1', '创建订单', '2015-06-26 15:16:58');
INSERT INTO `tb_goodsRecord` VALUES ('96', '', null, '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', '1', '1.00', '1.00', '0', '189', '商品变更', '2015-06-16 12:13:53');
INSERT INTO `tb_goodsRecord` VALUES ('97', '', null, 'DF9C5DF2-E3CA-440C-A9DC-5B4BE215D311', '42D58289-95DC-40C3-ACE2-D073BB140175', '添加商品', '1.52', '4253.00', '0', '55', '商品变更', '2015-06-16 12:16:04');
INSERT INTO `tb_goodsRecord` VALUES ('98', '141649216645497461', null, '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', '1', '1.00', '1.00', '1', '-1', '创建订单', '2015-06-26 15:14:26');
INSERT INTO `tb_goodsRecord` VALUES ('99', '201546492165494654', null, 'DF9C5DF2-E3CA-440C-A9DC-5B4BE215D311', '42D58289-95DC-40C3-ACE2-D073BB140175', '添加商品', '1.52', '4253.00', '1', '-1', '创建订单', '2015-06-26 15:13:17');
INSERT INTO `tb_goodsRecord` VALUES ('100', '201546492165494654', null, '1CC6D748-E949-4076-944D-B6889317F702', '6173FE9C-2F11-4705-AD21-064B33EE2CE2', '话费充值', '345.00', '76.00', '1', '-1', '创建订单', '2015-06-26 15:13:19');
INSERT INTO `tb_goodsRecord` VALUES ('101', '201506151254946869', null, '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', '1', '1.00', '1.00', '1', '-1', '创建订单', '2015-06-26 15:10:08');
INSERT INTO `tb_goodsRecord` VALUES ('102', '', null, '55F3734A-A52A-4661-8986-B52E0F4C55C2', 'D197D9B8-AA34-4022-95F2-8FE4C805FB2F', '测试图片路径', '111.00', '111.00', '0', '111', '商品创建', '2015-06-16 18:09:29');
INSERT INTO `tb_goodsRecord` VALUES ('103', '', null, '55F3734A-A52A-4661-8986-B52E0F4C55C2', 'B27A7245-7F18-4DF1-B70C-6E89B9D90B75', '测试图片路径', '111.00', '111.00', '0', '111', '商品创建', '2015-06-16 18:30:43');
INSERT INTO `tb_goodsRecord` VALUES ('104', '', null, '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', 'EF0C9216-9A7C-4709-86FA-86828532C6F8', '1', '111.00', '111.00', '0', '111', '商品创建', '2015-06-16 18:36:57');
INSERT INTO `tb_goodsRecord` VALUES ('105', '', null, 'B341A323-35C8-49B8-8AD6-C926064B2FF1', '919581C5-40C2-46BD-905F-A64F45DF8211', '三星手机', '111.00', '111.00', '0', '111', '商品创建', '2015-06-16 18:40:39');
INSERT INTO `tb_goodsRecord` VALUES ('106', '', null, '25274197-F2DF-4AD3-9C5F-B60F16B86D58', '45A81BE8-DA93-4B1A-A30A-BEBEF1821166', '锤子手机', '111.00', '111.00', '0', '1111', '商品创建', '2015-06-17 10:18:50');
INSERT INTO `tb_goodsRecord` VALUES ('107', '', null, '098622D1-49E3-49A8-AFF2-836CC672A05E', 'CB42C5A8-E878-4763-A91B-EB73DEACEFF8', '一加手机', '111.00', '1111.00', '0', '111', '商品创建', '2015-06-17 10:37:10');
INSERT INTO `tb_goodsRecord` VALUES ('108', '', null, 'ECAC90AC-A85D-4F49-B59E-6E3F033D53EE', '9B4A3B9B-202A-48F4-B34E-C600B16C445C', '服装', '111.00', '111.00', '0', '111', '商品创建', '2015-06-17 10:43:17');
INSERT INTO `tb_goodsRecord` VALUES ('109', '', null, '25274197-F2DF-4AD3-9C5F-B60F16B86D58', '66FF5E67-1957-4189-A31E-A1C912347B83', '锤子手机', '34543.00', '2344.00', '0', '12234', '商品创建', '2015-06-17 12:07:46');
INSERT INTO `tb_goodsRecord` VALUES ('110', '201506191437381234', null, '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', 'EF0C9216-9A7C-4709-86FA-86828532C6F8', '1', '111.00', '111.00', '1', '-1', '创建订单', '2015-06-26 14:53:31');
INSERT INTO `tb_goodsRecord` VALUES ('111', '', null, 'B2BFB84D-F46D-4575-B61B-71630C8178EB', 'D69023FB-5966-4B26-84FA-2C94B81A8EAD', '中国石化加油卡', '111.00', '1112.00', '0', '1111', '商品创建', '2015-06-19 14:42:37');
INSERT INTO `tb_goodsRecord` VALUES ('112', '', null, 'B2BFB84D-F46D-4575-B61B-71630C8178EB', 'F58388B0-ADC2-4FBB-9CED-7C5D6F27A1E6', '中国石化加油卡', '1234.00', '12345.00', '0', '123', '商品创建', '2015-06-19 14:50:29');
INSERT INTO `tb_goodsRecord` VALUES ('113', '', null, 'B2BFB84D-F46D-4575-B61B-71630C8178EB', '43A73297-37EF-4D8F-B276-11332706C8DC', '中国石化加油卡', '1111.00', '1111.00', '0', '111', '商品创建', '2015-06-19 14:58:52');
INSERT INTO `tb_goodsRecord` VALUES ('114', '', null, '11C28E22-5A10-4E56-8210-1885372A953D', '6D7EA884-BD4F-4D70-9E3E-6DB9E6AA5E2F', 'p8', '1.00', '1.00', '0', '11111', '商品创建', '2015-06-19 14:59:19');
INSERT INTO `tb_goodsRecord` VALUES ('115', '', null, '3A0B3CF1-ED12-4C86-A19B-E217D27AE2AE', '733B19D1-196E-42AE-880B-A02ECBB3EB05', 'p7', '1.00', '1.00', '0', '1', '商品创建', '2015-06-19 15:01:21');
INSERT INTO `tb_goodsRecord` VALUES ('116', '', null, '3A0B3CF1-ED12-4C86-A19B-E217D27AE2AE', 'A06D8023-6827-49AA-A2F4-1658E44C24B7', 'p7', '1.00', '1.00', '0', '1', '商品创建', '2015-06-19 15:01:46');
INSERT INTO `tb_goodsRecord` VALUES ('117', '', null, '4056B1E4-C304-4466-8FE6-FBF26A4AFF7B', '257FF59D-974D-48A4-ABA7-39D992715628', '腾讯QB点卡', '80.00', '92.00', '0', '100', '商品创建', '2015-06-19 15:06:06');
INSERT INTO `tb_goodsRecord` VALUES ('118', '201506191507161234', null, '11C28E22-5A10-4E56-8210-1885372A953D', '6D7EA884-BD4F-4D70-9E3E-6DB9E6AA5E2F', 'p8', '1.00', '1.00', '1', '-1', '创建订单', '2015-06-26 14:53:38');
INSERT INTO `tb_goodsRecord` VALUES ('119', '', null, '3A0B3CF1-ED12-4C86-A19B-E217D27AE2AE', '733B19D1-196E-42AE-880B-A02ECBB3EB05', 'p7', '1.00', '1.00', '0', '110', '商品变更', '2015-06-19 15:10:58');
INSERT INTO `tb_goodsRecord` VALUES ('120', '', null, '3A0B3CF1-ED12-4C86-A19B-E217D27AE2AE', 'A06D8023-6827-49AA-A2F4-1658E44C24B7', 'p7', '1.00', '1.00', '0', '110', '商品变更', '2015-06-19 15:11:09');
INSERT INTO `tb_goodsRecord` VALUES ('121', '', null, '4056B1E4-C304-4466-8FE6-FBF26A4AFF7B', '1ED804F0-6F91-42DF-9685-DC7A161BCD37', '腾讯QB点卡', '11111.00', '11111.00', '0', '11111', '商品创建', '2015-06-19 15:13:17');
INSERT INTO `tb_goodsRecord` VALUES ('122', '20150619151848123', null, '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', '2B7D47FC-55A1-48CB-AEAF-68B6D33D13C9', '1', '1.00', '1.00', '2', '-2', '创建订单', '2015-06-26 14:52:22');
INSERT INTO `tb_goodsRecord` VALUES ('123', '20150619151848123', null, '11C28E22-5A10-4E56-8210-1885372A953D', '6D7EA884-BD4F-4D70-9E3E-6DB9E6AA5E2F', 'p8', '1.00', '1.00', '1', '-1', '创建订单', '2015-06-26 14:52:16');
INSERT INTO `tb_goodsRecord` VALUES ('124', '20150619151848123', null, '4056B1E4-C304-4466-8FE6-FBF26A4AFF7B', '1ED804F0-6F91-42DF-9685-DC7A161BCD37', '腾讯QB点卡', '11111.00', '11111.00', '1', '-1', '创建订单', '2015-06-26 14:52:12');
INSERT INTO `tb_goodsRecord` VALUES ('125', '20150619151848123', null, 'B2BFB84D-F46D-4575-B61B-71630C8178EB', '43A73297-37EF-4D8F-B276-11332706C8DC', '中国石化加油卡', '1111.00', '1111.00', '1', '-1', '创建订单', '2015-06-26 14:52:08');
INSERT INTO `tb_goodsRecord` VALUES ('126', '20150619151848123', null, '3A0B3CF1-ED12-4C86-A19B-E217D27AE2AE', '733B19D1-196E-42AE-880B-A02ECBB3EB05', 'p7', '1.00', '1.00', '1', '-1', '创建订单', '2015-06-26 14:52:05');
INSERT INTO `tb_goodsRecord` VALUES ('127', '201506191555165740', null, 'B341A323-35C8-49B8-8AD6-C926064B2FF1', '919581C5-40C2-46BD-905F-A64F45DF8211', '三星手机', '111.00', '111.00', '1', '-1', '创建订单', '2015-06-19 15:55:15');
INSERT INTO `tb_goodsRecord` VALUES ('128', '201506191555165740', null, 'ECAC90AC-A85D-4F49-B59E-6E3F033D53EE', '9B4A3B9B-202A-48F4-B34E-C600B16C445C', '服装', '111.00', '111.00', '1', '-1', '创建订单', '2015-06-19 15:55:15');
INSERT INTO `tb_goodsRecord` VALUES ('129', '201506191555165740', null, '098622D1-49E3-49A8-AFF2-836CC672A05E', 'CB42C5A8-E878-4763-A91B-EB73DEACEFF8', '一加手机', '111.00', '1111.00', '1', '-1', '创建订单', '2015-06-19 15:55:15');
INSERT INTO `tb_goodsRecord` VALUES ('130', '201506191555165740', null, '6BD123BD-3CCB-44E3-9B7B-381F7C1F4E41', 'EF0C9216-9A7C-4709-86FA-86828532C6F8', '1', '111.00', '111.00', '1', '-1', '创建订单', '2015-06-19 15:55:15');
INSERT INTO `tb_goodsRecord` VALUES ('131', '201506191555165740', null, 'B2BFB84D-F46D-4575-B61B-71630C8178EB', '43A73297-37EF-4D8F-B276-11332706C8DC', '中国石化加油卡', '1111.00', '1111.00', '1', '-1', '创建订单', '2015-06-19 15:55:15');
INSERT INTO `tb_goodsRecord` VALUES ('132', '201506231631365275', null, '11C28E22-5A10-4E56-8210-1885372A953D', '6D7EA884-BD4F-4D70-9E3E-6DB9E6AA5E2F', 'p8', '1.00', '1.00', '1', '-1', '创建订单', '2015-06-23 16:31:35');
INSERT INTO `tb_goodsRecord` VALUES ('133', '201506231631365275', null, '4056B1E4-C304-4466-8FE6-FBF26A4AFF7B', '1ED804F0-6F91-42DF-9685-DC7A161BCD37', '腾讯QB点卡', '11111.00', '11111.00', '1', '-1', '创建订单', '2015-06-23 16:31:35');
INSERT INTO `tb_goodsRecord` VALUES ('134', '201506231935421382', null, '4056B1E4-C304-4466-8FE6-FBF26A4AFF7B', '1ED804F0-6F91-42DF-9685-DC7A161BCD37', '腾讯QB点卡', '11111.00', '11111.00', '2', '-2', '创建订单', '2015-06-23 19:35:41');

-- ----------------------------
-- 分类属性表
-- ----------------------------
DROP TABLE IF EXISTS `tb_Prop`;
CREATE TABLE `tb_Prop` (
  `id` char(36) NOT NULL DEFAULT '' COMMENT '序号',
  `categoryId` char(36) DEFAULT '' COMMENT '分类ID',
  `propName` varchar(50) DEFAULT '' COMMENT '属性名称',
  `showType` varchar(20) DEFAULT '' COMMENT '显示方式（文本，下拉，单、多选等）',
  `propDesc` varchar(300) DEFAULT '' COMMENT '属性描述。属性值不填',
  `requiredOrNot` int(11) DEFAULT '1' COMMENT '是否必填项',
  `propUseOrNot` int(11) DEFAULT '1' COMMENT '属性启用、禁用（1启用，0禁用）',
  `saleOrBuy` int(11) DEFAULT '1' COMMENT '判断是买家还是卖家（默认卖家1，买家0）',
  `createPerson` varchar(50) DEFAULT '' COMMENT '创建人',
  `createDate` datetime DEFAULT NULL COMMENT '创建日期',
  `companyId` int(11) DEFAULT '1' COMMENT '所属标识',
  PRIMARY KEY (`id`),
  KEY `FK_prop` (`categoryId`) USING BTREE,
  KEY `index_propName` (`propName`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of tb_Prop
-- ----------------------------
INSERT INTO `tb_Prop` VALUES ('01DD58C9-D994-54F7-AF83-37CB863340A8', '18F3CBF0-1F19-D858-7ABA-99A055F7D443', '金额', 'select', '金额', '1', '1', '1', '张三', '2015-06-04 17:38:09', '1');
INSERT INTO `tb_Prop` VALUES ('034666B5-1040-D61B-EE52-6BD56E808A0C', 'B67FEE7D-10C6-2547-E75E-A1247C467FE9', '200', '', '', '1', '1', '1', 'admin', '2015-06-19 14:39:28', '1');
INSERT INTO `tb_Prop` VALUES ('04B92841-38FC-1177-4F0F-2964DF5B9CAB', '98D086A2-C518-7EBD-F065-80BF4B6CFA82', '100万', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('075E29F8-DBC6-2C04-CD7E-835DB6617FBF', 'A686B24B-06F9-5633-5F5A-0FA421ADE865', 'XXL', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('080EB19D-994F-B441-F0AE-35FF31603724', 'DD5793C1-1696-C8BC-6BED-54ECC1FD427C', '小米', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('0AA74B10-3CB2-C2D5-C299-56073F32B592', 'AB8D79AD-5B82-82C5-3151-7ACE2F552D7B', '300', '', '', '1', '1', '1', '张三', '2015-06-04 11:52:25', '1');
INSERT INTO `tb_Prop` VALUES ('0B078C6D-917A-BB27-38F5-E25FF18E5BE2', '602103C4-663C-4D36-1246-EF3FD0CFDED1', '黑色', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('0C5F78FC-82C2-6002-A264-7805FFEF3DB8', '34B5279D-EAE8-534C-2017-7A35C8E82C30', 'EMS', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('0F77DE7C-FCF9-6E23-6A81-5B2C097C5BEC', 'A82AC0AB-7DFD-5B3D-9E7E-D20EC474E18B', 'QQ帐号', 'text', '', '1', '1', '0', 'admin', '2015-06-16 15:37:29', '1');
INSERT INTO `tb_Prop` VALUES ('1FA31C23-4A07-92F2-9806-9FD1A8DFE046', '38B2CDAB-22A4-5CD6-369C-79EF3A579F76', '测试属性', 'text', '请问', '1', '1', '0', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('20AEBB90-52CE-46F1-4CA9-A077C0446A14', 'A3A50BD4-65AD-EF65-5BDE-1B20595566E4', '手机号', 'text', '买家手机号', '1', '1', '0', '张三', '2015-06-04 17:32:27', '1');
INSERT INTO `tb_Prop` VALUES ('22AB03F0-A797-C7F1-A788-D2CDABD89EA8', '6F8433C4-6191-18D3-7A48-24CDB5B9A33F', '100', '', '', '1', '1', '1', '张三', '2015-06-04 17:31:01', '1');
INSERT INTO `tb_Prop` VALUES ('2441C6BE-811A-35E3-9794-0BFAD93271DD', '01DD58C9-D994-54F7-AF83-37CB863340A8', '50', '', '', '1', '1', '1', '张三', '2015-06-04 17:38:24', '1');
INSERT INTO `tb_Prop` VALUES ('2541EBDF-CF51-265D-2345-C698F52C1533', '45EFB086-E9EF-5992-1C5A-D4EEE9B5D2AE', '电话号码', 'text', '电信电话号码', '1', '1', '0', '张三', '2015-06-04 11:43:04', '1');
INSERT INTO `tb_Prop` VALUES ('28D8D4DD-01C0-B6BF-0C19-66E7C03A1B05', '2D5E919D-EFD9-8112-3ABE-885F7EA72253', '41', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('2B3E0E85-11F1-5A6E-7D31-2FDA16B2BE12', '00B3555D-9313-4CC8-038D-F9927C770014', '测试', 'text', '测试属性', '1', '1', '0', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('2D5E919D-EFD9-8112-3ABE-885F7EA72253', '8F170B22-3B83-151C-93D0-34B1FC278732', '尺码', 'select', '鞋子尺码', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('311B1812-AA95-D384-490A-B90D4F070E74', '2D5E919D-EFD9-8112-3ABE-885F7EA72253', '39', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('34B5279D-EAE8-534C-2017-7A35C8E82C30', 'AB1110A4-8290-38E9-A2D5-D28E19396404', '运费', 'select', '运费', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('3553AA88-2F6A-D6AA-5202-2455124AE306', '00B3555D-9313-4CC8-038D-F9927C770014', '参数', 'select', '参数', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('4814A1FF-E7A1-FD56-9ACC-5CA20199E2D1', 'AB8D79AD-5B82-82C5-3151-7ACE2F552D7B', '100', '', '', '1', '1', '1', '张三', '2015-06-04 11:52:14', '1');
INSERT INTO `tb_Prop` VALUES ('4AD40D0F-D111-00D7-7C24-A53542D86DFB', '602103C4-663C-4D36-1246-EF3FD0CFDED1', '蓝色', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('4B36C11A-F456-FC0A-A351-441337855876', '2D5E919D-EFD9-8112-3ABE-885F7EA72253', '42', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('517BF2D8-1408-9A9B-03B1-EFF1CE96B109', '9B43E1E8-9D0D-660A-9F9C-FD41757E9423', '移动电话号码', 'text', '移动电话号码', '1', '1', '0', '张三', '2015-06-04 11:45:53', '1');
INSERT INTO `tb_Prop` VALUES ('52964CD8-1E24-1E76-353A-34AFA836BA0B', '2B3E0E85-11F1-5A6E-7D31-2FDA16B2BE12', '测试属性值1', '', '', '1', '1', '0', '张三', '2015-06-02 11:14:06', '1');
INSERT INTO `tb_Prop` VALUES ('602103C4-663C-4D36-1246-EF3FD0CFDED1', '38B2CDAB-22A4-5CD6-369C-79EF3A579F76', '颜色', 'select', '红、黄、蓝、黑、白', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('6681A79F-4ED4-7B5C-777A-AD12A3B8A6AD', '34B5279D-EAE8-534C-2017-7A35C8E82C30', '顺丰', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('6E9062E2-6293-2671-0BFA-CDCA5E3B2EF9', '1FA31C23-4A07-92F2-9806-9FD1A8DFE046', '属性1', '', '', '1', '1', '0', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('6F8433C4-6191-18D3-7A48-24CDB5B9A33F', 'A3A50BD4-65AD-EF65-5BDE-1B20595566E4', '金额', 'select', '点卡金额', '1', '0', '1', '张三', '2015-06-04 17:30:41', '1');
INSERT INTO `tb_Prop` VALUES ('6FB5481D-702F-092F-D5D2-D0DC1735786D', '7C2616B4-1E9A-0113-0317-F8717967A407', '联通电话', 'text', '联通电话号码', '1', '1', '0', '张三', '2015-06-04 11:45:01', '1');
INSERT INTO `tb_Prop` VALUES ('71617B28-6C9C-CCF1-F2F4-A74D7416AD50', '602103C4-663C-4D36-1246-EF3FD0CFDED1', '绿色', '', '', '1', '0', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('7C9742D3-6CE2-93E8-83E5-108CA6AD864B', 'DD5793C1-1696-C8BC-6BED-54ECC1FD427C', '三星', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('7CC79716-D13F-C7B6-2D3D-97A2D5FC7A14', '6F8433C4-6191-18D3-7A48-24CDB5B9A33F', '50', '', '', '1', '1', '1', '张三', '2015-06-04 17:30:56', '1');
INSERT INTO `tb_Prop` VALUES ('8342F50A-B7D8-2BAD-70AA-7842CA771F8A', '3553AA88-2F6A-D6AA-5202-2455124AE306', 'CPU', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('8721D868-69B0-B60A-53B1-EF2FC05DC00B', 'D0442FDA-3494-21DA-303B-1A97AE72FE25', '1234567890', '', '', '1', '1', '0', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('8A6365F4-0E80-19EE-E278-16D082F94E83', '2D5E919D-EFD9-8112-3ABE-885F7EA72253', '38', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('91DA3B25-2582-6FEB-A0EC-67B8AB46E0D7', '3553AA88-2F6A-D6AA-5202-2455124AE306', '内存', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('92BD791F-C5DD-D4F8-4A3D-ECF1F6F467CF', '602103C4-663C-4D36-1246-EF3FD0CFDED1', '红色', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('94C1D3AE-EE34-03D6-4223-6D1D30228D2E', 'CBC6B48B-5CDC-75AE-5818-53A78F949CAC', '100', '', '', '1', '1', '1', '张三', '2015-06-06 17:14:29', '1');
INSERT INTO `tb_Prop` VALUES ('98D086A2-C518-7EBD-F065-80BF4B6CFA82', 'C9FA25FE-6195-E4F2-534B-0309FD22AF4C', '捐助条件', 'select', '捐助条件', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('99369ECD-E186-162E-D4F3-2095A9806A50', '98D086A2-C518-7EBD-F065-80BF4B6CFA82', '200万', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('9E5AE623-980D-138E-80DE-1DE19E515856', '34B5279D-EAE8-534C-2017-7A35C8E82C30', '申通', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('A4085470-3885-AEF9-5113-BFD5145F379A', 'AB8D79AD-5B82-82C5-3151-7ACE2F552D7B', '50', '', '', '1', '1', '1', '张三', '2015-06-04 11:52:10', '1');
INSERT INTO `tb_Prop` VALUES ('A63CD43A-D9E1-5FFA-6BDF-96C2FE7AEE9A', 'A686B24B-06F9-5633-5F5A-0FA421ADE865', 'XL', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('A686B24B-06F9-5633-5F5A-0FA421ADE865', '38B2CDAB-22A4-5CD6-369C-79EF3A579F76', '尺寸', 'select', 'x,m,xl,xxl,xxxL', '1', '1', '1', 'admin', '2015-06-18 10:18:38', '1');
INSERT INTO `tb_Prop` VALUES ('A8AD2796-808A-3F7E-D4E8-778FF19A4C22', '2D5E919D-EFD9-8112-3ABE-885F7EA72253', '40', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('AB8D79AD-5B82-82C5-3151-7ACE2F552D7B', '45EFB086-E9EF-5992-1C5A-D4EEE9B5D2AE', '金额', 'select', '充值金额', '1', '1', '1', '张三', '2015-06-04 11:51:46', '1');
INSERT INTO `tb_Prop` VALUES ('AD51B1F2-44C9-BE19-CADE-BC55D0E3070B', '01DD58C9-D994-54F7-AF83-37CB863340A8', '100', '', '', '1', '1', '1', '张三', '2015-06-04 17:38:29', '1');
INSERT INTO `tb_Prop` VALUES ('B13E3C65-4A41-2EDE-1876-9E5911A1247F', 'AB8D79AD-5B82-82C5-3151-7ACE2F552D7B', '200', '', '', '1', '1', '1', '张三', '2015-06-04 11:52:19', '1');
INSERT INTO `tb_Prop` VALUES ('B267F635-A51D-6239-E2E2-D42D21EE4B1D', '2D5E919D-EFD9-8112-3ABE-885F7EA72253', '37', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('B67FEE7D-10C6-2547-E75E-A1247C467FE9', '5A5A93BA-9A92-F381-6F10-A71F5C660230', '单价', 'select', '单价', '1', '1', '1', 'admin', '2015-06-19 14:39:05', '1');
INSERT INTO `tb_Prop` VALUES ('BCA4153D-466C-DE01-C92F-FDE3E69ECB30', 'A3A50BD4-65AD-EF65-5BDE-1B20595566E4', 'QQ号', 'text', 'QQ充值帐号', '1', '1', '0', '张三', '2015-06-04 17:31:58', '1');
INSERT INTO `tb_Prop` VALUES ('CA80AFC9-46EC-C515-09AC-1395F4F653B6', 'DD5793C1-1696-C8BC-6BED-54ECC1FD427C', '锤子', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('CBC6B48B-5CDC-75AE-5818-53A78F949CAC', '25C8E80C-02CD-AC69-8A38-BDF6693DC0A1', '金额', 'select', '金额', '1', '1', '1', '张三', '2015-06-06 17:14:06', '1');
INSERT INTO `tb_Prop` VALUES ('CC026545-A5EA-919F-D863-9B0CC756FD7B', 'B67FEE7D-10C6-2547-E75E-A1247C467FE9', '100', '', '', '1', '1', '1', 'admin', '2015-06-19 14:39:23', '1');
INSERT INTO `tb_Prop` VALUES ('CD7A763E-73ED-CDD3-1929-D5A02078EEA7', '602103C4-663C-4D36-1246-EF3FD0CFDED1', '黄色', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('D0442FDA-3494-21DA-303B-1A97AE72FE25', '9180DFEA-303F-23B8-6FFA-3B211B157E4A', '帐号', 'text', '玩家帐号', '1', '1', '0', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('DD5793C1-1696-C8BC-6BED-54ECC1FD427C', '00B3555D-9313-4CC8-038D-F9927C770014', '品牌', 'select', '手机品牌', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('DEDC36A7-E9D1-F2F4-A7B3-40635121C24E', '1FA31C23-4A07-92F2-9806-9FD1A8DFE046', '测试属性值', '', '', '1', '1', '0', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('E6BD42E4-BC18-E60C-9B7F-78477D79D92F', '5A8B6F5D-2A8E-88C7-E626-C349C870FAC1', '手机号', 'text', '手机号', '1', '1', '0', '张三', '2015-06-04 17:41:22', '1');
INSERT INTO `tb_Prop` VALUES ('F616D636-5264-1B80-063C-8FEF4677C6F7', 'CBC6B48B-5CDC-75AE-5818-53A78F949CAC', '50', '', '', '1', '1', '1', '张三', '2015-06-06 17:14:24', '1');
INSERT INTO `tb_Prop` VALUES ('F6C1F336-0FF6-254E-2645-F1F7BAC39578', 'FC6035F1-E874-74A1-03AC-85983AC9B8BE', '测试', '', '', '1', '1', '0', '张三', '2015-06-04 11:50:26', '1');
INSERT INTO `tb_Prop` VALUES ('F80EE84B-ABAA-5146-A986-B082A7753673', '9910DBE3-C6FF-F802-43E8-806B84297550', '游戏帐号', 'text', '游戏账号', '1', '1', '0', 'admin', '2015-06-19 14:39:57', '1');
INSERT INTO `tb_Prop` VALUES ('F9EE96F4-19A7-74E5-CBFD-2F9E3EB9B883', '602103C4-663C-4D36-1246-EF3FD0CFDED1', '白色', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('FA58FA67-0ACE-634D-0C96-AFEA647EABA8', 'DD5793C1-1696-C8BC-6BED-54ECC1FD427C', '苹果', '', '', '1', '1', '1', '张三', '2015-03-10 00:00:00', '1');
INSERT INTO `tb_Prop` VALUES ('FC6035F1-E874-74A1-03AC-85983AC9B8BE', '45EFB086-E9EF-5992-1C5A-D4EEE9B5D2AE', '测试一个', 'text', '测试', '1', '1', '0', '张三', '2015-06-04 11:50:14', '1');

-- ----------------------------
-- 购物车 
-- ----------------------------
DROP TABLE IF EXISTS `tb_shoppingCart`;
CREATE TABLE `tb_shoppingCart` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goodsSubId` varchar(36) DEFAULT '' COMMENT '商品id（主键）',
  `goodsName` varchar(200) DEFAULT '' COMMENT '商品名称',
  `goodsTitle` varchar(200) DEFAULT '' COMMENT '商品标题',
  `goodsProp` varchar(3000) DEFAULT '' COMMENT '商品分类/属性集合',
  `goodsCount` varchar(3000) DEFAULT '' COMMENT '商品数量',
  `userAccount` varchar(200) DEFAULT '' COMMENT '用户账号',
  `userName` varchar(200) DEFAULT '' COMMENT '用户姓名',
  `addTime` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_shoppingCart
-- ----------------------------
INSERT INTO `tb_shoppingCart` VALUES ('22', '1ED804F0-6F91-42DF-9685-DC7A161BCD37', '腾讯QB点卡', '200元点QB卡', '', '3', '1', '张三', '2015-06-23 19:35:20');
INSERT INTO `tb_shoppingCart` VALUES ('23', 'EF0C9216-9A7C-4709-86FA-86828532C6F8', '1', '追加商品', '', '1', '1', '张三', '2015-06-26 18:19:26');
