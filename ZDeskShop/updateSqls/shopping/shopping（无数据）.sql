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

