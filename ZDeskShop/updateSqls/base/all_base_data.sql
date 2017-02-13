
--   国际化表
DROP TABLE IF EXISTS `i18nPrompt`;
CREATE TABLE `i18nPrompt` (
  `id` varchar(64) NOT NULL COMMENT '非业务主键',
  `promptKey` varchar(255) DEFAULT '' COMMENT '提示信息的key值，由此字段判断取什么提示信息！',
  `promptValue` varchar(1000) DEFAULT '' COMMENT '提示信息，存放需要提示的具体说明文字',
  `languge` varchar(32) DEFAULT '' COMMENT '提示信息的语言。',
  `langugeCode` varchar(32) DEFAULT '' COMMENT '语言代码（例：zh-cn 中国 en 英国）',
  `projectName` varchar(32) DEFAULT '' COMMENT '提示信息所对应的项目名称。',
  `projectCode` varchar(32) DEFAULT '' COMMENT '对应项目的代码（如：ZSH 中国石化）',
  PRIMARY KEY (`id`),
  KEY `promptKey` (`promptKey`) USING BTREE,
  KEY `langugeCode` (`langugeCode`) USING BTREE,
  KEY `projectType` (`projectName`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of i18nPrompt
-- ----------------------------
INSERT INTO `i18nPrompt` VALUES ('2C63F38B-E607-734A-55D9-28CFAF9EAD3C', 'system_selectDataForDelete', '\\u8bf7\\u9009\\u4e2d\\u8981\\u5220\\u9664\\u7684\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('30EC2B9C-2D42-446E-74BB-36FF5F7003EF', 'permission_success', '\\u6388\\u6743\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('33CE64ED-EE8B-678C-3F16-4E3DA310522A', 'system_updateSuccess', '\\u4fee\\u6539\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('6463C52F-2268-DFBF-D5C7-76C63BD3E810', 'system_deleteFailed', '\\u5220\\u9664\\u5931\\u8d25\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('6BC801AD-A605-6C29-19F9-DCB5AEDF0B62', 'system_saveFailed', '\\u4fdd\\u5b58\\u5931\\u8d25\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('839CDAA3-B8CF-E9CD-E6B3-0524609BDFA2', 'permission_role', '\\u8bf7\\u9009\\u62e9\\u89d2\\u8272\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('91135CC3-7B86-122C-D67E-C2A401053763', 'SystemGeneration_yuyanchuangjianchenggong', '\\u8bed\\u8a00\\u521b\\u5efa\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('96F7D0CB-040C-5F2F-EFFA-363ADF5D6DA9', 'system_updateFailed', '\\u4fee\\u6539\\u5931\\u8d25\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('C0FCAD8C-C4E4-E282-D974-E0B728D9197F', 'system_deleteSuccess', '\\u5220\\u9664\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('C5E792C6-6C3C-0DB7-5169-40DEA621417F', 'i18nPrompt_noLanguge', '\\u672a\\u68c0\\u7d22\\u5230\\u8bed\\u8a00\\u4fe1\\u606f\\uff0c\\u8bf7\\u5148\\u521b\\u5efa\\u8bed\\u8a00\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('1597D5EB-7C33-A4BE-BF5E-61601C55F83A', 'i18nPrompt_keyyicunzai', '\\u63d0\\u793akey\\u503c\\u5df2\\u5b58\\u5728\\uff0c\\u8bf7\\u4fee\\u6539\\u540e\\u6dfb\\u52a0\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('16433D72-8648-A9BD-FC3C-6A89285C0A68', 'notKey', '\\u8be5key\\u4e0e\\u63d0\\u793a\\u5185\\u5bb9\\u4e0d\\u5b58\\u5728\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('DDCC7A04-A67E-DCD9-111F-07438FC3FCDB', 'system_deleteConfirmation', '\\u786e\\u8ba4\\u8981\\u5220\\u9664\\u6b64\\u6570\\u636e\\u5417\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('EC119E39-6239-E2A3-27BF-BB558C0AE5A5', 'system_signOutConfirmation', '\\u786e\\u8ba4\\u8981\\u9000\\u51fa\\u7a0b\\u5e8f\\u5417\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('EF7D5E74-DFDD-FFAF-B62A-0CAD31B86B07', 'system_canNotBeEmpty', '${0} \\u4e0d\\u80fd\\u4e3a\\u7a7a\\u503c\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('F10083B9-0DFB-0046-3693-53F01F72AA28', 'system_saveSuccess', '\\u4fdd\\u5b58\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('1E39A52D-12F9-0B0F-EE36-6CFEF7FB0C0F', 'role_operationFailed', '\\u64cd\\u4f5c\\u5931\\u8d25', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('456CAFD0-9ECD-CDD4-0729-60995C8B1888', 'role_nowantedData', '\\u62b1\\u6b49\\uff0c\\u6ca1\\u6709\\u7b26\\u5408\\u60a8\\u9700\\u8981\\u7684\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('455C247A-0A98-CFA5-5BB6-B795CD21291A', 'role_noSameName', '\\u60a8\\u597d\\uff0c\\u89d2\\u8272\\u540d\\u79f0\\u4e0d\\u80fd\\u91cd\\u540d\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('D76567E4-8A50-B5EE-33EB-64BAB0D2B69E', 'role_nameIsNotNull', '\\u60a8\\u597d\\uff0c\\u89d2\\u8272\\u540d\\u79f0\\u4e0d\\u80fd\\u4e3a\\u7a7a\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('64402EFE-00B9-F33B-CDC5-28ED0E4A94EA', 'role_selectOneData', '\\u8bf7\\u60a8\\u9009\\u62e9\\u4e00\\u6761\\u6570\\u636e', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('92F25013-C253-961B-9E91-512D2B9C5745', 'role_permission', '\\u60a8\\u9700\\u8981\\u4e3a\\u672c\\u89d2\\u8272\\u6388\\u6743\\u5417\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('8DED6ADA-12B2-2B4E-6B4F-67D3E33D02CB', 'role_user', '\\u60a8\\u786e\\u5b9a\\u4e3a\\u89d2\\u8272\\u6dfb\\u52a0\\u6216\\u4fee\\u6539\\u7528\\u6237\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('BAEF9D7E-4620-8D78-64A4-7955FF55D899', 'role_updatePermission', '\\u60a8\\u786e\\u5b9a\\u4e3a\\u89d2\\u8272\\u6dfb\\u52a0\\u6216\\u4fee\\u6539\\u6743\\u9650\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('88DCAF41-6CE6-8C6F-6BE5-D2189A5FF0FC', 'role_addOrupdateForRole', '\\u606d\\u559c\\u60a8\\uff01\\u4e3a\\u89d2\\u8272\\u6dfb\\u52a0/\\u4fee\\u6539\\u7528\\u6237\\u64cd\\u4f5c\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('D4A8878B-CDB9-161C-32A8-BE0DC8899633', 'tree_nodata', '\\u6ca1\\u6709\\u6570\\u636e!', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('E74ED843-66C1-0214-1EB4-F4F121396BF3', 'tree_addNode', '\\u6dfb\\u52a0\\u8282\\u70b9', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('1447681E-F048-7F5F-8A3D-D1305051E68A', 'tree_yibushuxianyichang', '\\u5f02\\u6b65\\u83b7\\u53d6\\u6570\\u636e\\u51fa\\u73b0\\u5f02\\u5e38\\u3002', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('C834F292-6F62-A908-14D1-3A665568D10A', 'tree_fenleileixingbuweikong', '\\u5206\\u7c7b\\u7c7b\\u578b\\u4e0d\\u80fd\\u4e3a\\u7a7a', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('AF5F874C-976D-BD43-7030-A8EFC1B4349D', 'tree_recordTypeAndTypeSame', '\\u6dfb\\u52a0\\u5931\\u8d25\\uff01\\u60a8\\u7684recordType\\u548ctype\\u503c\\u5df2\\u5b58\\u5728', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('ED9AFF6E-7B7B-975B-2DBC-A47B423308AF', 'tree_chooseRootType', '\\u8bf7\\u9009\\u62e9\\u6839\\u8282\\u70b9\\u7684\\u7c7b\\u578b\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('B3F446D8-731F-18C6-9CE1-6DB0A823FE1A', 'tree_chooseNode', '\\u8bf7\\u9009\\u62e9\\u8282\\u70b9', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('68CBAE98-162B-6D6C-DFBA-4BEC33C127C7', 'tree_nochooseTree', '\\u60a8\\u8fd8\\u6ca1\\u6709\\u9009\\u62e9\\u6811', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('555C51CD-57A6-DFCD-7225-7F119B123385', 'tree_typeAndRecordTypemustWrite', '\\u5206\\u7c7b\\u7c7b\\u578b\\u548c\\u8282\\u70b9\\u540d\\u79f0\\u5fc5\\u987b\\u586b\\u5199\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('48432A5A-AE4D-506B-41DC-E24782140976', 'tree_chooseDelTree', '\\u8bf7\\u9009\\u62e9\\u5220\\u9664\\u8282\\u70b9\\u7684\\u6811', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('8A87AA5A-FB8D-B06B-4B2A-D61E67781CC5', 'tree_chooseUpdateNode', '\\u8bf7\\u9009\\u62e9\\u8981\\u4fee\\u6539\\u7684\\u8282\\u70b9', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('B8460438-B4E5-DEC5-A9CF-13E8F1E86B04', 'tree_chooseTreeNode', '\\u8bf7\\u9009\\u62e9\\u6811\\u8282\\u70b9', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('5DAE82D5-B853-F200-4CEC-CAD158AB155D', 'yonghuguanli_meiyouzhaodaodenglumingbunengxiugaijuese', '\\u6ca1\\u6709\\u627e\\u5230\\u767b\\u5f55\\u540d,\\u4e0d\\u80fd\\u4fee\\u6539\\u89d2\\u8272', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('6EEB367B-DF16-EF2C-B4E8-7E9412EFE87F', 'yonghuguanli_yonghusuoshuzuzhixiugaichenggong', '\\u7528\\u6237\\u6240\\u5c5e\\u7ec4\\u7ec7\\u4fee\\u6539\\u6210\\u529f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('F79DA6C2-5E2D-5B29-F3D2-AD62E6B6B0AD', 'yonghuguanli_meiyouzhaodaodenglumingbunengxiugaizuzhi', '\\u6ca1\\u6709\\u627e\\u5230\\u767b\\u5f55\\u540d,\\u4e0d\\u80fd\\u4fee\\u6539\\u7ec4\\u7ec7', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('BD9191FA-8AB9-FF7E-BB07-80157E4F7282', 'yonghuguanli_qingweiyonghuxuanzejuese', '\\u8bf7\\u4e3a\\u7528\\u6237\\u9009\\u62e9\\u89d2\\u8272', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('A6671536-5785-3910-1D1A-FBA1567C278D', 'yonghuguanli_caozuoshibai', '\\u64cd\\u4f5c\\u5931\\u8d25', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('C6893970-FF7D-36B9-E2C6-F7B3AF194B5C', 'yonghuguanli_yonghuyongyoujuesexiugaichenggong', '\\u7528\\u6237\\u62e5\\u6709\\u89d2\\u8272\\u4fee\\u6539\\u6210\\u529f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('EDA93312-9776-8966-307D-D19A27FF42A5', 'yonghuguanli_denglumingchengbunengweikong', '\\u767b\\u5f55\\u540d\\u79f0\\u4e0d\\u80fd\\u4e3a\\u7a7a', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('4E1677C4-494D-6C1B-893C-6E04DABBF38E', 'yonghuguanli_denglumingchengyicunzai', '\\u767b\\u5f55\\u540d\\u79f0\\u5df2\\u5b58\\u5728', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('497A8BA5-94E2-EA39-DF08-4B67B2217F5A', 'yonghuguanli_yonghuxingmingbunengweikong', '\\u7528\\u6237\\u59d3\\u540d\\u4e0d\\u80fd\\u4e3a\\u7a7a', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('E8F31151-F375-70F3-E2B6-5D7DC04C80DB', 'yonghuguanli_fenjihaobunengweikong', '\\u5206\\u673a\\u53f7\\u4e0d\\u80fd\\u4e3a\\u7a7a', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('D7FE928E-5A57-8451-D26D-14F055B07DC0', 'yonghuguanli_fenjihaoyicunzai', '\\u5206\\u673a\\u53f7\\u5df2\\u5b58\\u5728', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('08EB89BB-6255-F6A7-D7ED-582FEDBED092', 'yonghuguanli_mimabunegweikong', '\\u5bc6\\u7801\\u4e0d\\u80fd\\u4e3a\\u7a7a', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('0D1DD6A1-7390-BBE7-5081-AC5F797788A3', 'ZJGL_xuanzeshuju', '\\u8bf7\\u9009\\u62e9\\u6570\\u636e', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('174A087A-1BFC-9652-6ADA-CE32F53DF4F9', 'ZJGL_mingzibuweikong', '\\u60a8\\u597d\\uff0c\\u540d\\u5b57\\u4e0d\\u80fd\\u4e3a\\u7a7a', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('3F2CF167-C833-50FD-96AF-59428B1D5539', 'ZJGL_tianxieshuzi', '\\u8bf7\\u586b\\u5199\\u6570\\u5b57', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('B1A20B09-9DE0-E984-6A14-26B4D88EC6E7', 'ZJGL_tianxiebitianxiang', '\\u8bf7\\u586b\\u5199\\u5fc5\\u586b\\u9879', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('EDA2D89E-3450-BB1C-347B-83390BD4E9CD', 'ZJGL_wuid', '\\u65e0id\\u503c\\uff0c\\u64cd\\u4f5c\\u5931\\u8d25', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('0A52E931-38F9-728A-7733-0A51809F7245', 'ZQC_fujianfabushibai', '\\u590d\\u68c0\\u53d1\\u5e03\\u5931\\u8d25', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('2847AA69-83DD-8831-C80F-B51BC3EF770D', 'ZQC_jieshushijianbunengdayukaishihijian', '\\u7ed3\\u675f\\u65f6\\u95f4\\u4e0d\\u80fd\\u5927\\u4e8e\\u5f00\\u59cb\\u65f6\\u95f4\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('56377D35-CE9F-80D5-A25C-358E2835A744', 'ZQC_fabushibai', '\\u53d1\\u5e03\\u5931\\u8d25', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('5E2C33C9-6D2E-37DB-9AD3-360322EE293B', 'ZQC_fujianfabuchenggong', '\\u590d\\u68c0\\u53d1\\u5e03\\u6210\\u529f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('7AB3987C-75B7-026B-C036-9AA1ADD0F110', 'ZQC_zifuchaochang', '\\u60a8\\u597d\\uff0c\\u8f93\\u5165\\u8d85\\u957f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('837D6713-9411-6F0F-7BBA-0761031FBF10', 'ZQC_wufapingfen', '\\u62b1\\u6b49\\uff0c\\u65e0\\u6cd5\\u8bc4\\u5206', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('8D3DC7A3-141C-5078-2548-5E570CAA4FEE', 'ZQC_chaxunshijianbunengweikong', '\\u60a8\\u597d\\uff0c\\u67e5\\u8be2\\u8d77\\u59cb\\u65f6\\u95f4\\u4e0e\\u7ed3\\u675f\\u65f6\\u95f4\\u4e0d\\u80fd\\u4e3a\\u7a7a', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('8F269476-4BD8-BF6D-18BA-7F1CCF31CB11', 'ZQC_fabuchenggong', '\\u53d1\\u5e03\\u6210\\u529f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('A86AE543-0D81-0E97-5C39-F2FD185C9A69', 'ZQC_meiyoucijilufenshu', '\\u6570\\u636e\\u5f02\\u5e38\\uff0c\\u65e0\\u6b64\\u6570\\u636e\\u5bf9\\u5e94\\u7684\\u5206\\u6570', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('C1533129-95FC-BBC4-ECBB-09915F1960CF', 'ZQC_chaxunshijanbunengdayusanshitian', '\\u67e5\\u8be2\\u65f6\\u95f4\\u4e0d\\u80fd\\u5927\\u4e8e30\\u5929\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('C2E3812B-625E-809C-D839-631249CBFFDC', 'ZQC_shuzichaochufanwei', '\\u60a8\\u597d\\uff0c\\u6570\\u5b57\\u8d85\\u51fa\\u8303\\u56f4\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('C8BDF912-0ABF-2EAA-1AC5-1E73E4B723D3', 'ZQC_fenshubitian', '\\u5206\\u6570\\u90fd\\u9700\\u8981\\u586b\\u5199', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('3C6AB424-FD67-62C2-6D68-E60CB36FAF89', 'system_caozuochenggong', '\\u64cd\\u4f5c\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('3FB19DC2-B699-382D-3E4A-1D23C45AC68E', 'system_caozuoshibai', '\\u64cd\\u4f5c\\u5931\\u8d25\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('5C8155B8-3BBE-0CCB-9667-C73D5A448C9E', 'zqc_share_qxzylqdsj', '\\u8bf7\\u9009\\u4e2d\\u8981\\u9886\\u53d6\\u7684\\u6570\\u636e!', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('28E7F99C-AC74-13AA-154D-C5653F7F9D12', 'zqc_share_czyfpdsj', '\\u6240\\u8981\\u9886\\u53d6\\u7684\\u6570\\u636e\\u4e2d\\u5b58\\u5728\\u5df2\\u5206\\u914d\\u7684\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('D9694790-A834-8539-28C9-5A3AA41B8890', 'zqc_share_qxzpfzb', '\\u8bf7\\u9009\\u62e9\\u8bc4\\u5206\\u6307\\u6807\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('4D5060BD-97F7-31EA-2887-4727D0E5FA22', 'zqc_share_qdlqsxzsj', '\\u786e\\u5b9a\\u9886\\u53d6\\u6240\\u9009\\u4e2d\\u6570\\u636e\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('70E44FDE-0EB2-ED49-FBE5-1335635565A8', 'zqc_share_qxzyqxlqdsj', '\\u8bf7\\u9009\\u4e2d\\u8981\\u53d6\\u6d88\\u9886\\u53d6\\u7684\\u6570\\u636e!', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('C6758434-F6C6-61EC-C836-612FE7758CA4', 'zqc_share_czwqczdsj_zjy', '\\u6240\\u8981\\u53d6\\u6d88\\u9886\\u53d6\\u7684\\u6570\\u636e\\u4e2d\\u5b58\\u5728\\u65e0\\u6743\\u64cd\\u4f5c\\u7684\\u6570\\u636e\\uff0c\\u53ea\\u80fd\\u53d6\\u6d88\\u8d28\\u68c0\\u5458\\u4e3a\\u5f53\\u524d\\u767b\\u5f55\\u4eba\\u7684\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('6683D1CA-4EF2-3C2B-6F39-C46F7721D88C', 'zqc_share_qxlqdsjbhf', '\\u6240\\u8981\\u53d6\\u6d88\\u9886\\u53d6\\u7684\\u6570\\u636e\\u4e2d\\u5b58\\u5728\\u6587\\u672c\\u72b6\\u6001\\u521d\\u68c0\\u672a\\u5206\\u914d\\u548c0\\u7684\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('019228A3-F42C-59F1-B471-2ECE3F8F7559', 'zqc_share_qdqxlqsxzsj', '\\u786e\\u5b9a\\u53d6\\u6d88\\u9886\\u53d6\\u6240\\u9009\\u4e2d\\u6570\\u636e\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('5E66CDE5-A60D-AC8E-3209-9C9561AA6202', 'zqc_share_qxzyfpdsj', '\\u8bf7\\u9009\\u4e2d\\u8981\\u5206\\u914d\\u7684\\u6570\\u636e!', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('5F986928-B151-9E14-6E93-CDDB12731750', 'zqc_share_fpsjgzjy', '\\u8bf7\\u9009\\u4e2d\\u8981\\u5206\\u914d\\u6587\\u672c\\u6570\\u636e\\u7ed9\\u54ea\\u4e9b\\u8d28\\u68c0\\u5458!', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('053C8FA0-61C4-D86A-83F9-E84E274240BA', 'zqc_share_qdfpsxzsj', '\\u786e\\u5b9a\\u5206\\u914d\\u6240\\u9009\\u4e2d\\u6570\\u636e\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('AF063C7A-7BCD-F29B-FF44-69242AA4BAC2', 'zqc_share_qxzyqxfpdsj', '\\u8bf7\\u9009\\u4e2d\\u8981\\u53d6\\u6d88\\u5206\\u914d\\u7684\\u6570\\u636e!', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('3DF4C292-1F0C-6F86-BFB7-6F84F264D3B0', 'zqc_share_czwqczdsj_fprbs', '\\u6240\\u8981\\u53d6\\u6d88\\u5206\\u914d\\u7684\\u6570\\u636e\\u4e2d\\u5b58\\u5728\\u65e0\\u6743\\u64cd\\u4f5c\\u7684\\u6570\\u636e\\uff0c\\u53ea\\u80fd\\u53d6\\u6d88\\u5206\\u914d\\u4eba\\u6807\\u8bc6\\u4e3a\\u5f53\\u524d\\u767b\\u5f55\\u4eba\\u7684\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('DF194792-9E91-055D-D263-0AF4AFDD21BC', 'zqc_share_qdqxfpsxzsj', '\\u786e\\u5b9a\\u53d6\\u6d88\\u5206\\u914d\\u6240\\u9009\\u4e2d\\u6570\\u636e\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('21B6B885-EF38-9235-D046-5799BC8E9C08', 'zqc_share_qxzxyfpdzjyxx', '\\u8bf7\\u9009\\u4e2d\\u9700\\u8981\\u5206\\u914d\\u7684\\u8d28\\u68c0\\u5458\\u4fe1\\u606f!', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('83085906-E34F-D023-E8F1-E05EDE51DF2E', 'zqc_share_yczntjytsj', '\\u4e00\\u6b21\\u53ea\\u80fd\\u6dfb\\u52a0\\u4e00\\u6761\\u6570\\u636e!', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('B752AFA0-92FD-FE32-9554-BDE71D0B135E', 'zqc_share_gjcycz', '\\u8be5\\u6559\\u6750\\u5df2\\u5b58\\u5728\\uff0c\\u4e0d\\u53ef\\u91cd\\u590d\\u6dfb\\u52a0\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('CA8B6C45-DB8D-AD98-F84F-7D3801AEA569', 'zqc_share_qxzjcmlm', '\\u8bf7\\u9009\\u62e9\\u6559\\u6750\\u76ee\\u5f55\\u540d!', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('A2122C45-80BB-7FD3-C40F-BDA4AA454025', 'zqc_share_qxzyxzdsj', '\\u8bf7\\u9009\\u4e2d\\u8981\\u4e0b\\u8f7d\\u7684\\u6570\\u636e!', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('1E4A84A0-339E-A7AE-986A-C6C4F720351B', 'zqc_share_zzcdgsjxz', '\\u53ea\\u652f\\u6301\\u5355\\u4e2a\\u6570\\u636e\\u4e0b\\u8f7d\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('40E9A8D4-8746-D58D-DA3A-9575E51E0351', 'zqc_share_qxfpdsjbhf_fj', '\\u6240\\u8981\\u53d6\\u6d88\\u5206\\u914d\\u7684\\u6570\\u636e\\u4e2d\\u5b58\\u5728\\u6587\\u672c\\u72b6\\u6001\\u521d\\u68c0\\u5df2\\u53d1\\u5e03\\u3001\\u521d\\u68c0\\u5df2\\u8bc4\\u5206\\u6216\\u590d\\u68c0\\u672a\\u5206\\u914d\\u7684\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('9C6C1A7E-5CCE-6148-AE86-82E88FA1CB7C', 'zqc_share_czyfpdsj_plfp', '\\u6240\\u8981\\u5206\\u914d\\u7684\\u6570\\u636e\\u4e2d\\u5b58\\u5728\\u5df2\\u5206\\u914d\\u7684\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('154CB425-BAE1-56A9-9047-0A409D9A9944', 'zqc_share_qxfpdsjbhf_fp', '\\u6240\\u8981\\u53d6\\u6d88\\u9886\\u53d6\\u7684\\u6570\\u636e\\u4e2d\\u5b58\\u5728\\u6587\\u672c\\u72b6\\u6001\\u521d\\u68c0\\u672a\\u5206\\u914d\\u548c0\\u7684\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('500DCC7F-5C2A-5D3D-8D67-F507797D1B97', 'gexiangpingfen_meiyouduiyingdepingfenzhibiao', '\\u6ca1\\u6709\\u5bf9\\u5e94\\u7684\\u8bc4\\u5206\\u6307\\u6807\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('79659CCC-8E2F-CEB6-36F3-43E3F26B79D2', 'gexiangpingfen_meiyoupingfenshuju', '\\u6b64\\u9879\\u6ca1\\u6709\\u8bc4\\u5206\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('FD8B4C9C-93B5-924A-FA3A-A60A04FBF528', 'zuoxichaxun_qingxuanzeyaochakandeshuju', '\\u8bf7\\u9009\\u4e2d\\u8981\\u67e5\\u770b\\u7684\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('E3913648-C8B1-5943-E932-FEAAABFD7350', 'zuoxichaxun_shangsongneirongbunengweikong', '\\u4e0a\\u8bc9\\u5185\\u5bb9\\u4e0d\\u80fd\\u4e3a\\u7a7a\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('4B384C99-FC88-8B77-62E9-24AB9AA1CCAE', 'zuoxishangsu_shangsuchenggong', '\\u4e0a\\u8bc9\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('44C1605F-A369-1867-95CF-67EA5803B9E4', 'ZJGL_zongfenbunengchaoguolinghuoyibai', '\\u603b\\u5206\\u4e0d\\u80fd\\u4f4e\\u4e8e0\\uff0c\\u6216\\u8005\\u8d85\\u8fc7100\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('A79D1785-776D-EC4C-B5B3-1EE1CC9B4A25', 'ZJGL_quanzhongbunengchaoguolinghuoyibai', '\\u6743\\u91cd\\u4e0d\\u80fd\\u4f4e\\u4e8e0\\uff0c\\u6216\\u8005\\u8d85\\u8fc7100\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('804D5025-76F4-EEDC-6D97-7C8C72BB75D9', 'ZQC_qingxuanzhongyaodaochudeshuju', '\\u8bf7\\u9009\\u62e9\\u8981\\u5bfc\\u51fa\\u7684\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('69899F3C-7346-B1B2-4239-52234A736D15', 'ZQC_zhinengdiaotingyitiaoluyin', '\\u53ea\\u80fd\\u8c03\\u542c\\u4e00\\u6761\\u5f55\\u97f3\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('E7AA353A-B3E5-F32C-E5FB-8FD94D5798B9', 'ZQC_xuanzhongshujuzhonghanyouweipingfenshuju', '\\u9009\\u4e2d\\u6570\\u636e\\u4e2d\\u542b\\u6709\\u672a\\u8bc4\\u5206\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('D1F95162-1B9B-EAFD-801E-2F7BB052B947', 'ZQC_xuanzhongshujuzhonghanyoushangsuweiwanchengshuju', '\\u9009\\u62e9\\u6570\\u636e\\u4e2d\\u542b\\u6709\\u4e0a\\u8bc9\\u672a\\u5b8c\\u6210\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('D82C190F-DF18-BC56-69CE-D3D3B3F8F7E2', 'ZQC_jieshushijianbunengweikong', '\\u7ed3\\u675f\\u65f6\\u95f4\\u4e0d\\u80fd\\u4e3a\\u7a7a\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('C63135C7-5961-CC8E-1D2B-03216F4673BB', 'ZQC_qingshurubiaoyouxinghaodeshijian', '\\u8bf7\\u8f93\\u5165\\u67e5\\u8be2\\u65f6\\u95f4\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('212ED295-227E-5254-E0D0-CB5FABB143A5', 'ZJGL_zhongjianzhibunengdayuzongfen', '\\u4e2d\\u95f4\\u503c\\u4e0d\\u80fd\\u5927\\u4e8e\\u603b\\u5206\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('8D0A5ECE-1A69-9AC3-678E-C183A6CA6F47', 'ZQC_zijiedianzongfenbunengdayugenjiedianzongfen', '\\u5b50\\u8282\\u70b9\\u603b\\u5206\\u4e0d\\u80fd\\u5927\\u4e8e\\u6839\\u8282\\u70b9\\u603b\\u5206\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('A1CCCD46-1BCA-6E4F-E9B9-5AD75F742CA1', 'ZQC_ycznckytsj', '\\u53ea\\u80fd\\u67e5\\u770b\\u4e00\\u6761\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('254FD375-828A-93D1-0A1F-4B285B3215F0', 'ZQC_ZhongJianZhiHeZhongJianZhiMingChengShuLiangBuYiZhi', '\\u4e2d\\u95f4\\u503c\\u548c\\u4e2d\\u95f4\\u503c\\u540d\\u79f0\\u6570\\u91cf\\u4e0d\\u4e00\\u81f4\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');

-- ----------------------------
-- Table structure for DicIndex 索引表
-- ----------------------------
DROP TABLE IF EXISTS `DicIndex`;
CREATE TABLE `DicIndex` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `indexCode` varchar(50) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '索引码',
  `indexName` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '索引名称',
  `dicInternational` varchar(32) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '国际化类型',
  PRIMARY KEY (`id`),
  KEY `indexCode` (`indexName`)
) ENGINE=MyISAM AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of DicIndex
-- ----------------------------
DELETE FROM `DicIndex` WHERE `indexCode`="system_default";
DELETE FROM `DicIndex` WHERE `indexCode`="test";

INSERT INTO `DicIndex` (`indexCode`, `indexName`) VALUES ('system_default', '系统默认字典项');
INSERT INTO `DicIndex` (`indexCode`, `indexName`) VALUES ('test', '测试1');
--

-- ----------------------------
-- Table structure for DicData  字典数据
-- ----------------------------

DROP TABLE IF EXISTS `DicData`;
CREATE TABLE `DicData` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '字典码',
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '名称',
  `indexCode` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '索引类型',
  `indexName` varchar(32) COLLATE utf8_unicode_ci DEFAULT '',
  `parentIndexCode` varchar(32) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '级联索引',
  `parentIndexName` varchar(64) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '级联索引名称',
  `dicInternational` varchar(32) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '国际化',
  `sortColums` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  `companyId` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `companyName` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `departmentId` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `departmentName` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `indexType` (`indexCode`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=87 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of DicData
-- ----------------------------
DELETE FROM `DicData` WHERE `code` ="page";
DELETE FROM `DicData` WHERE `code` ="tab";
DELETE FROM `DicData` WHERE `code` ="base";

INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`) VALUES ('tab', '导航', 'system_default', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`) VALUES ('page', '页面权限', 'system_default', '', '', '', '');
INSERT INTO `DicData` (`code`, `name`, `indexCode`, `indexName`, `parentIndexCode`, `parentIndexName`, `dicInternational`) VALUES ('base', '基本权限', 'system_default', '', '', '', '');



-- ----------------------------
-- Table structure for zkmCommonTree  ---- 后期改掉type
-- ----------------------------
DROP TABLE IF EXISTS `zkmCommonTree`;
CREATE TABLE `zkmCommonTree` (
  `id` double NOT NULL AUTO_INCREMENT,
  `leftClick` varchar(100) DEFAULT '',
  `rightClick` varchar(100) DEFAULT '',
  `nodeId` varchar(36) DEFAULT '',
  `nodeName` varchar(150) DEFAULT '',
  `beanName` varchar(90) DEFAULT '',
  `recordType` varchar(60) DEFAULT '',
  `isSynchronous` varchar(3) DEFAULT '',
  `requestUrl` varchar(255) DEFAULT '',
  `type` varchar(60) DEFAULT '',
  `normalNode` varchar(4) DEFAULT '0' COMMENT '普通节点,0:是 1不是',
  `leftparam` varchar(108) DEFAULT '',
  `rightparam` varchar(108) DEFAULT '',
  `desc` varchar(1500) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='通用树配置表';

-- ----------------------------
-- Records of zkmCommonTree
-- ----------------------------
INSERT INTO `zkmCommonTree` VALUES ('15', '', ' ', '', '', 'treeNodeSelected', 'menu', '0', '/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action', 'searchMenu', '0', '', '', '');
INSERT INTO `zkmCommonTree` VALUES ('18', 'window.top.navigationLeftChickFn', ' ', '', '菜单目录', 'permissonFilter', 'menu', '0', '/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action', 'menu', '0', '', '', '');
INSERT INTO `zkmCommonTree` VALUES ('21', ' ', ' ', '', '组织机构树', ' ', 'org', '1', '/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action', 'test', '0', '', '', '');
INSERT INTO `zkmCommonTree` VALUES ('22', ' ', ' ', '', '组织机构树', 'zkmSimpleCommonOrgTreeFilter', 'org', '0', '/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action', 'test1', '0', '', '', '');
INSERT INTO `zkmCommonTree` VALUES ('24', 'parent.searchPermisstion', ' ', ' ', ' 菜单目录', ' ', 'menu', '0', '/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action', 'allNode', '0', ' ', ' ', ' ');



-- ----------------------------
-- Table structure for commonTree_detail
-- ----------------------------
DROP TABLE IF EXISTS `commonTree_detail`;
CREATE TABLE `commonTree_detail` (
  `id` char(36) NOT NULL COMMENT '主键UUID',
  `text` varchar(1000) DEFAULT '' COMMENT '用于ext的节点名称',
  `iconCls` varchar(200) DEFAULT '' COMMENT '用于ext节点的CSS对象',
  `leaf` varchar(20) DEFAULT 'true' COMMENT '用于ext描述节点的leaf',
  `recordType` varchar(20) DEFAULT 'f' COMMENT '目录文件标识，f文件，d目录',
  `parentId` char(36) DEFAULT '' COMMENT '上级节点ID',
  `createUser` varchar(200) DEFAULT '' COMMENT '创建者',
  `createTime` datetime DEFAULT NULL COMMENT '创建日期',
  `nodeTypeUrl` varchar(1000) DEFAULT '' COMMENT '打开页面URL',
  `sortField` varchar(100) DEFAULT '' COMMENT '派序列',
  `isdel` char(1) DEFAULT '' COMMENT '是否删除',
  `delUser` varchar(200) DEFAULT '' COMMENT '删除用户',
  `companyId` varchar(50) DEFAULT '' COMMENT '公司ID',
  `companyName` varchar(200) DEFAULT '' COMMENT '公司名称',
  `departmentId` varchar(50) DEFAULT '' COMMENT '部门ID',
  `departmentName` varchar(200) DEFAULT '' COMMENT '部门名称',
  `leftClick` varchar(200) DEFAULT '' COMMENT '左击事件',
  `leftParam` varchar(36) DEFAULT '' COMMENT '左击鼠标参数',
  `rightClick` varchar(200) DEFAULT '' COMMENT '右击事件',
  `rightParam` varchar(36) DEFAULT '' COMMENT '右击鼠标参数',
  `modleId` varchar(50) DEFAULT '' COMMENT '模块',
  PRIMARY KEY (`id`),
  KEY `commontree_parentId` (`parentId`) USING BTREE,
  KEY `commontree_recordTyoe` (`recordType`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED COMMENT='通用树数据表';


-- ----------------------------
-- Records of commonTree_detail  数据表  数据为测试数据可删
-- ---------------------------

INSERT INTO `commonTree_detail` VALUES ('C00AEBA9-F187-070C-7CD6-EF41F606A620', '树形目录管理', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2014-09-25 10:35:51', 'common/commonTree.html', '8', null, '', '', '', '', '', '', '', '', '', 'treeManager');
INSERT INTO `commonTree_detail` VALUES ('3E111524-36F5-98A5-8633-8D31F6864E63', '字典管理', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2014-10-09 15:32:04', 'dictDataManage/Dictmanager.html', '1', null, '', '', '', '', '', '', '', '', '', 'dicManger');
INSERT INTO `commonTree_detail` VALUES ('B9C4C2E2-B88F-D47A-FEEC-35ADFAD73ACA', '国际化管理', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2014-09-25 12:07:07', 'i18n/promptManager.html', '7', null, '', '', '', '', '', '', '', '', '', 'InternationalManger');
INSERT INTO `commonTree_detail` VALUES ('D007BE35-2940-AB51-E502-C9DFAD51B083', '组织机构管理', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2014-09-25 10:41:49', '', '2', null, '', '', '', '', '', '', '', '', '', 'orgManager');
INSERT INTO `commonTree_detail` VALUES ('9C561F4E-4333-ECC0-52A1-CD45C13C5703', '角色授权', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2014-09-25 10:41:16', 'securityPermission/permission/index.html', '3', null, '', '', '', '', '', '', '', '', '', 'role_authorization');
INSERT INTO `commonTree_detail` VALUES ('8212E8D5-5560-9AA4-E694-3C241BF1886F', '权限管理', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2014-09-25 10:40:55', 'securityPermission/permission/suSecurityPermission.html', '4', null, '', '', '', '', '', '', '', '', '', 'jurisdictionManager');
INSERT INTO `commonTree_detail` VALUES ('3010E9D5-2C65-6303-3D06-9F8A763A8288', '用户管理', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2014-09-25 10:40:18', 'securityPermission/userInfo/userInfo.html', '5', null, '', '', '', '', '', '', '', '', '', 'userManager');
INSERT INTO `commonTree_detail` VALUES ('FB05FC87-B036-DE3B-3D88-7DA838B3D356', '角色管理', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2014-09-25 10:40:05', 'securityPermission/userInfo/role.html', '6', null, '', '', '', '', '', '', '', '', '', 'roleMananer');
INSERT INTO `commonTree_detail` VALUES ('34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZDesk管理', '', 'true', 'menu', '', 'ZSKadmin', '2014-09-25 10:39:35', null, '1', null, '', '', '', '', '', '', '', '', '', 'ZDeskManager');
INSERT INTO `commonTree_detail` VALUES ('313', '研发三组', '', 'true', 'org', '31', '', null, null, '0', null, '', '', '', '', '', '', '', '', '', 'RD3');
INSERT INTO `commonTree_detail` VALUES ('322', '销售', '', 'true', 'org', '32', '', null, null, '0', null, '', '', '', '', '', '', '', '', '', 'market');
INSERT INTO `commonTree_detail` VALUES ('321', '售前', '', 'true', 'org', '32', '', null, null, '1', null, '', '', '', '', '', '', '', '', '', 'pre-sales');
INSERT INTO `commonTree_detail` VALUES ('312', '研发二组', '', 'true', 'org', '31', '', null, null, '1', null, '', '', '', '', '', '', '', '', '', 'RD2');
INSERT INTO `commonTree_detail` VALUES ('311', '研发一组', '', 'true', 'org', '31', '', null, null, '2', null, '', '', '', '', '', '', '', '', '', 'RD1');
INSERT INTO `commonTree_detail` VALUES ('33', '质保部', '', 'true', 'org', '32', '', null, null, '2', null, '', '', '', '', '', '', '', '', '', 'QA');
INSERT INTO `commonTree_detail` VALUES ('32', '市场部', '', 'true', 'org', '3', '', null, null, null, null, '', '', '', '', '', '', '', '', '', 'MKT');
INSERT INTO `commonTree_detail` VALUES ('31', '研发部', '', 'true', 'org', '3', '', null, null, null, null, '', '', '', '', '', '', '', '', '', 'R_D');
INSERT INTO `commonTree_detail` VALUES ('3', '英立讯', '', 'true', 'org', '', '', null, null, '0', null, '', '', '', '', '', '', '', '', '', 'zinglabs');


/**字典页面树节点*/
DELETE FROM commonTree_detail WHERE ID="3E111524-36F5-98A5-8633-8D31F6864E63";
DELETE FROM commonTree_detail WHERE ID="03B35536-6A6F-0912-53AD-6726284BF54F";
DELETE FROM commonTree_detail WHERE ID="9174F9F6-5762-BC7D-400D-08EE05F27F88";

INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES('3E111524-36F5-98A5-8633-8D31F6864E63', '字典管理', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2014-10-09 15:32:04', '', '1', null, '', '', '', '', '', '', '', '', '', 'dicManger');
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES('03B35536-6A6F-0912-53AD-6726284BF54F', '字典数据', '', 'true', 'menu', '3E111524-36F5-98A5-8633-8D31F6864E63', 'ZSKadmin', '2014-12-15 21:50:44', 'dictDataManage/Dictmanager.html', '', '', '', '', '', '', '', '', '', '', '', 'dicdata');
INSERT INTO `commonTree_detail` (`id`, `text`, `iconCls`, `leaf`, `recordType`, `parentId`, `createUser`, `createTime`, `nodeTypeUrl`, `sortField`, `isdel`, `delUser`, `companyId`, `companyName`, `departmentId`, `departmentName`, `leftClick`, `leftParam`, `rightClick`, `rightParam`, `modleId`) VALUES('9174F9F6-5762-BC7D-400D-08EE05F27F88', '字典索引', '', 'true', 'menu', '3E111524-36F5-98A5-8633-8D31F6864E63', 'ZSKadmin', '2014-12-15 21:51:39', 'dictDataManage/DictIndexManager.html', '', '', '', '', '', '', '', '', '', '', '', 'dicIndex');


DROP TABLE IF EXISTS `suSecurityUser`;
CREATE TABLE `suSecurityUser` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '业务无关主键',
  `loginName` varchar(32) CHARACTER SET gbk COLLATE gbk_bin NOT NULL DEFAULT '',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '用户姓名',
  `pwd` varchar(32) CHARACTER SET gbk COLLATE gbk_bin NOT NULL DEFAULT '',
  `startus` int(11) DEFAULT '0' COMMENT '用户当前状态：0 正常 1 锁定 2 删除',
  `viewIndex` int(11) DEFAULT '999999' COMMENT '排序字段',
  `phone_number` varchar(32) NOT NULL DEFAULT '' COMMENT '分机号',
  `agent_level` varchar(32) DEFAULT '' COMMENT '等级',
  `agent_information` varchar(400) DEFAULT '' COMMENT '附加信息',
  `job_number` varchar(32) DEFAULT '' COMMENT '工号',
  `seat_number` varchar(32) DEFAULT '' COMMENT '坐席号',
  `createDate` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '用户建立时间',
  `lastLoginDate` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '用户最后登录时间',
  `lastModifyPassword` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改密码的时间',
  `companyId` varchar(32) DEFAULT '' COMMENT '公司Id',
  `companyName` varchar(32) DEFAULT '' COMMENT '公司名称',
  `departmentId` varchar(32) DEFAULT '' COMMENT '部门Id',
  `departmentName` varchar(32) DEFAULT '' COMMENT '部门名称',
  `alias1` varchar(32) DEFAULT '' COMMENT '预留字段1',
  `alias2` varchar(32) DEFAULT '' COMMENT '预留字段2',
  `alias3` varchar(32) DEFAULT '' COMMENT '预留字段3',
  `alias4` varchar(32) DEFAULT '' COMMENT '预留字段4',
  `alias5` varchar(32) DEFAULT '' COMMENT '预留字段5',
  PRIMARY KEY (`id`),
  UNIQUE KEY `loginName` (`loginName`) USING BTREE,
  UNIQUE KEY `phone_number` (`phone_number`) USING BTREE,
  KEY `name` (`name`) USING BTREE
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 ROW_FORMAT=FIXED COMMENT='用户表';
-- ----------------------------
-- Records of suSecurityUser
-- ----------------------------
INSERT INTO `suSecurityUser` VALUES ('1', 'admin', '系统管理员', 'zinglabs', '0', '999999', '9999', '', '系统管理员', '1000', '', '2014-10-16 11:02:42', '2014-10-16 11:02:42', '2014-10-21 13:49:43', '', '', '', '', '','', '', '', '');

-- ----------------------------
-- Table structure for suSecurityRole  角色
-- ----------------------------
DROP TABLE IF EXISTS `suSecurityRole`;
CREATE TABLE `suSecurityRole` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '业务无关主键',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '角色名称',
  `description` varchar(512) DEFAULT '' COMMENT '角色说明',
  `viewIndex` int(11) NOT NULL DEFAULT '999999' COMMENT '角色排序',
  `alias1` varchar(32) DEFAULT '' COMMENT '预留字段1',
  `alias2` varchar(32) DEFAULT '' COMMENT '预留字段2',
  `alias3` varchar(32) DEFAULT '' COMMENT '预留字段3',
  `alias4` varchar(32) DEFAULT '' COMMENT '预留字段4',
  `alias5` varchar(32) DEFAULT '' COMMENT '预留字段5',
  `scoreWeight` varchar(64) DEFAULT '100',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM   DEFAULT CHARSET=utf8 COMMENT='角色表';

INSERT INTO `suSecurityRole` VALUES ('393', '系统管理员', '系统管理员', '999999', '', '', '', '', '', '100');

-- ----------------------------
-- Table structure for suSecurityRole  用户角色关联
-- ----------------------------

DROP TABLE IF EXISTS `suSecurityUserRole`;
CREATE TABLE `suSecurityUserRole` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '业务无关主键',
  `loginName` varchar(32) NOT NULL COMMENT '用户登录名',
  `roleId` int(20) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='用户角色关联表';

INSERT INTO `suSecurityUserRole` VALUES ('1', 'admin', '393');


-- ----------------------------
-- Table structure for suSecurityRole 用户组织关联
-- ----------------------------
DROP TABLE IF EXISTS `suSecurityUserOrg`;
CREATE TABLE `suSecurityUserOrg` (
  `id` varchar(36) NOT NULL,
  `loginName` varchar(32) DEFAULT '' COMMENT '登录名',
  `orgCode` varchar(36) DEFAULT '' COMMENT '组织id',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='用户组织关联表';

INSERT INTO `suSecurityUserOrg`  VALUES ('5BF7059B-94C5-48F2-3C89-9F58A087A451', 'admin', '33');
INSERT INTO `suSecurityUserOrg`  VALUES ('8BD62004-B79D-37AE-D186-11B6243A8F63', 'admin', '321');




-- ----------------------------
-- Table structure for suSecurityPermission  权限表
-- ----------------------------
DROP TABLE IF EXISTS `suSecurityPermission`;
CREATE TABLE `suSecurityPermission` (
  `id` varchar(36) NOT NULL COMMENT '业务无关主键',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '权限名称',
  `typeName` varchar(20) NOT NULL DEFAULT '' COMMENT '权限类型',
  `modleId` varchar(50) NOT NULL DEFAULT '' COMMENT '模块id',
  `modleName` varchar(100) NOT NULL DEFAULT '' COMMENT '模块名称',
  `funId` varchar(50) NOT NULL DEFAULT '' COMMENT '功能id',
  `funName` varchar(50) NOT NULL DEFAULT '' COMMENT '功能名称',
  `companyId` varchar(50) DEFAULT '' COMMENT '公司id',
  `companyName` varchar(200) DEFAULT '' COMMENT '公司名称',
  `departmentId` varchar(50) DEFAULT '' COMMENT '部门id',
  `departmentName` varchar(200) DEFAULT '' COMMENT '部门名称',
  `alias1` text COMMENT '预留字段',
  `alias2` varchar(100) NOT NULL DEFAULT '' COMMENT '预留字段',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='权限表';

-- ----------------------------
-- Records of suSecurityPermission
-- ----------------------------
INSERT INTO `suSecurityPermission` VALUES ('1', '语音标识', 'base', '', '', '', '', '', '', '','','','');
INSERT INTO `suSecurityPermission` VALUES ('2', '文本标识', 'base', '', '', '', '', '', '', '','','','');
INSERT INTO `suSecurityPermission` VALUES ('3', '视频标识', 'base', '', '', '', '', '', '', '','','','');
INSERT INTO `suSecurityPermission` VALUES ('4', '班长标识', 'base', '', '', '', '', '', '', '','','','');
INSERT INTO `suSecurityPermission` VALUES ('7768698F-EE77-7DD8-D8DD-6B82B2192605', '导航', 'tab', '', '', '', '', '','','','','common/zkmCommonTree.html?treeWidth=400&treeHeight=600&mode=one&recordType=menu&showButton=s&type=menu', '2');
INSERT INTO `suSecurityPermission` VALUES ('7768698F-EE77-7DD8-D8DD-6B82B2192606', '组织', 'tab', '', '', '', '', '', '','','','common/userMappingOrg.html?beanId=iconFileter', '1');


-- 授权表--

DROP TABLE IF EXISTS `suSecurityRoleMapPermission`;
CREATE TABLE `suSecurityRoleMapPermission` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `roleCode` varchar(36) NOT NULL DEFAULT '' COMMENT '角色code或id',
  `permissionCode` varchar(36) NOT NULL DEFAULT '' COMMENT '权限码',
  `type` varchar(20) NOT NULL DEFAULT '' COMMENT '权限类型',
  PRIMARY KEY (`id`),
  KEY `suMapPermission_pid` (`permissionCode`) USING BTREE,
  KEY `suMaoPermission_rid` (`roleCode`) USING BTREE,
  KEY `suMaoPermission_type` (`type`) USING BTREE
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='权限角色关联表通过type区分权限类型';


INSERT INTO `suSecurityRoleMapPermission` VALUES ('1', '系统管理员', '7768698F-EE77-7DD8-D8DD-6B82B2192605', 'tab');
INSERT INTO `suSecurityRoleMapPermission` VALUES ('315', '系统管理员', '3151D3E0-C684-6E76-CD9A-ED5B0E6232DF', 'menu');
INSERT INTO `suSecurityRoleMapPermission` VALUES ('314', '系统管理员', '3E111524-36F5-98A5-8633-8D31F6864E63', 'menu');
INSERT INTO `suSecurityRoleMapPermission` VALUES ('313', '系统管理员', 'D007BE35-2940-AB51-E502-C9DFAD51B083', 'menu');
INSERT INTO `suSecurityRoleMapPermission` VALUES ('312', '系统管理员', '9C561F4E-4333-ECC0-52A1-CD45C13C5703', 'menu');
INSERT INTO `suSecurityRoleMapPermission` VALUES ('311', '系统管理员', '8212E8D5-5560-9AA4-E694-3C241BF1886F', 'menu');
INSERT INTO `suSecurityRoleMapPermission` VALUES ('310', '系统管理员', '3010E9D5-2C65-6303-3D06-9F8A763A8288', 'menu');
INSERT INTO `suSecurityRoleMapPermission` VALUES ('309', '系统管理员', 'FB05FC87-B036-DE3B-3D88-7DA838B3D356', 'menu');
INSERT INTO `suSecurityRoleMapPermission` VALUES ('308', '系统管理员', 'B9C4C2E2-B88F-D47A-FEEC-35ADFAD73ACA', 'menu');
INSERT INTO `suSecurityRoleMapPermission` VALUES ('307', '系统管理员', 'C00AEBA9-F187-070C-7CD6-EF41F606A620', 'menu');
INSERT INTO `suSecurityRoleMapPermission` VALUES ('306', '系统管理员', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'menu');
INSERT INTO `suSecurityRoleMapPermission` VALUES ('317', '系统管理员', '7768698F-EE77-7DD8-D8DD-6B82B2192606', 'tab');


/**字典页面授权*/
DELETE FROM `suSecurityRoleMapPermission` WHERE `permissionCode`="3E111524-36F5-98A5-8633-8D31F6864E63";
DELETE FROM `suSecurityRoleMapPermission` WHERE `permissionCode`="03B35536-6A6F-0912-53AD-6726284BF54F";
DELETE FROM `suSecurityRoleMapPermission` WHERE `permissionCode`="9174F9F6-5762-BC7D-400D-08EE05F27F88";

INSERT INTO `suSecurityRoleMapPermission` (`roleCode`,`permissionCode`,`type`) VALUES ('系统管理员','3E111524-36F5-98A5-8633-8D31F6864E63','menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`,`permissionCode`,`type`) VALUES ('系统管理员','03B35536-6A6F-0912-53AD-6726284BF54F','menu');
INSERT INTO `suSecurityRoleMapPermission` (`roleCode`,`permissionCode`,`type`) VALUES ('系统管理员','9174F9F6-5762-BC7D-400D-08EE05F27F88','menu');	

-- 配置表

DROP TABLE IF EXISTS `DataItemAllocation`;
CREATE TABLE `DataItemAllocation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `peizhiItem` varchar(64) NOT NULL DEFAULT '',
  `peizhiItemValue` varchar(64) DEFAULT '',
  `desc` varchar(32) DEFAULT '',
  `bItem` varchar(32) DEFAULT '',
  `sItem` varchar(32) DEFAULT '',
  `productionId` varchar(32) DEFAULT '',
  `generateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `peizhiItem` (`peizhiItem`),
  KEY `bItem` (`peizhiItem`),
  KEY `Item` (`sItem`)
) ENGINE=MyISAM AUTO_INCREMENT=3909633 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of DataItemAllocation
-- ----------------------------
INSERT INTO `DataItemAllocation` VALUES ('1', 'procMode', 'common', '', 'conf', '其他', '', '2013-07-14 15:36:35');
INSERT INTO `DataItemAllocation` VALUES ('2', 'Z_WorkFlowDetail', '工作流程', '工作流程', 'dynamicField', '其他', 'logTable', '2013-07-14 15:36:36');
INSERT INTO `DataItemAllocation` VALUES ('3', 'logLevel', 'DEBUG', '控制日志级别顺序如下 DEBUG ZING_lt; INFO Z', 'conf', '其他', '', '2013-07-14 15:36:36');
INSERT INTO `DataItemAllocation` VALUES ('4', 'threadNumberW', '5', '', 'conf', '其他', '', '2013-07-14 15:36:37');
INSERT INTO `DataItemAllocation` VALUES ('5', 'threadNumberR', '25', '', 'conf', '其他', '', '2013-07-14 15:36:38');
INSERT INTO `DataItemAllocation` VALUES ('6', 'threadNumber', '6', '程序使用的计算线程数量  为-1时程序根据任务动态调控', 'conf', '其他', '', '2013-07-14 15:36:38');
INSERT INTO `DataItemAllocation` VALUES ('7', 'Z_MsgSend', '系统消息发送', '消息发送', 'dynamicField', '其他', 'logTable', '2013-07-14 15:36:39');
INSERT INTO `DataItemAllocation` VALUES ('8', 'minGranularity', '7', '程序最小时间粒度 8:1刻钟（目前不支持）7:半小时 6:小时', 'conf', '其他', '', '2013-07-14 15:36:40');
INSERT INTO `DataItemAllocation` VALUES ('9', 'forwardInterval', '20', '为减少跨时段数据更新而延迟的数据更新时间', 'conf', '其他', '', '2013-07-14 15:36:40');
INSERT INTO `DataItemAllocation` VALUES ('10', 'shortAbandTime', '10', '短时放弃时长 大于0秒值 电话在该时长内放弃视为短时放弃 为负值', 'conf', '其他', 'shortAband', '2013-07-14 15:36:43');
INSERT INTO `DataItemAllocation` VALUES ('11', 'maxLoginDay', '3', '同一座席最长连续登录时间，单位：天（不是小时制，是整点制）', 'conf', '坐席', '', '2013-07-14 15:36:44');
INSERT INTO `DataItemAllocation` VALUES ('12', 'Z_MsgReceive', '系统消息接收', '系统消息接收', 'dynamicField', '其他', 'logTable', '2013-07-14 15:36:45');
INSERT INTO `DataItemAllocation` VALUES ('13', 'virtualSkill', '$7801$7802$7803$7804$7805$7806$7', '不初始化该组座席 即最终数据统计不统计该组及该组座席', 'conf', '坐席', '', '2013-07-14 15:36:48');
INSERT INTO `DataItemAllocation` VALUES ('14', 'Z_financialproduct', '理财产品一览表', '理财产品一览表', 'dynamicField', '其他', 'logTable', '2013-07-14 15:36:50');
INSERT INTO `DataItemAllocation` VALUES ('15', 'oneceSolvSecond', '7200', '一次问题解决率 时间单位：秒 当前电话前指定时间内无同一号码视为', 'conf', '坐席', '', '2013-03-15 09:19:03');
INSERT INTO `DataItemAllocation` VALUES ('16', 'unAnsPeriod', '1', '控制未接来电明细表中 最小统计的未接来电振铃时长', 'conf', '坐席', '', '2013-03-15 09:19:11');
INSERT INTO `DataItemAllocation` VALUES ('17', '不包括', '20', '时间', 'dynamicField', '其他', 'ruleActionMap', '2013-03-15 10:38:16');
INSERT INTO `DataItemAllocation` VALUES ('18', '包括', '10', '时间', 'dynamicField', '其他', 'ruleActionMap', '2013-03-15 10:38:16');
INSERT INTO `DataItemAllocation` VALUES ('19', 'shiftsGenMode', 'physicsMode', '', 'conf', '其他', '', '2013-03-15 09:25:08');
INSERT INTO `DataItemAllocation` VALUES ('20', 'phySize', '12', '', 'conf', '其他', '', '2013-03-15 09:25:11');
INSERT INTO `DataItemAllocation` VALUES ('21', 'comboDBId', 'ZDesk', '', 'conf', '其他', '', '2013-03-15 09:25:22');
INSERT INTO `DataItemAllocation` VALUES ('22', '开始时间范围', '30', '时间', 'dynamicField', '其他', 'ruleActionMap', '2013-03-15 10:38:15');
INSERT INTO `DataItemAllocation` VALUES ('23', '结束时间范围', '50', '时间', 'dynamicField', '其他', 'ruleActionMap', '2013-03-15 10:38:15');
INSERT INTO `DataItemAllocation` VALUES ('24', 'SSCIP', '128.128.128.128', 'SSC服务器的IP地址', 'conf', 'SSC', '', '2014-12-19 19:02:11');
INSERT INTO `DataItemAllocation` VALUES ('25', '开始结束时间范围', '60', '时间', 'dynamicField', '其他', 'ruleActionMap', '2013-03-15 10:38:15');
INSERT INTO `DataItemAllocation` VALUES ('26', 'TTIP', '128.128.128.128', 'TT服务器的IP地址', 'conf', 'TT', '', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('27', 'SSCPort', '11101', 'SSC服务器使用的端口号', 'conf', 'SSC', '', '2013-03-15 09:25:34');
INSERT INTO `DataItemAllocation` VALUES ('28', '最大工作天数', '80', '时间', 'dynamicField', '其他', 'ruleActionMap', '2013-03-15 10:38:13');
INSERT INTO `DataItemAllocation` VALUES ('29', 'ATPort', '11102', 'AT使用的端口', 'conf', '其他', '', '2013-03-15 09:25:36');
INSERT INTO `DataItemAllocation` VALUES ('30', 'TTPort', '1978', 'TT使用的端口', 'conf', 'TT', '', '2013-03-15 09:25:37');
INSERT INTO `DataItemAllocation` VALUES ('31', 'ttMaxThread', '30', '', 'conf', 'TT', '', '2013-03-15 09:25:38');
INSERT INTO `DataItemAllocation` VALUES ('32', 'msgServiceIP', '128.128.128.128', '', 'conf', '其他', '', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('33', 'ttRThread', '5', '', 'conf', 'TT', '', '2013-03-15 09:25:41');
INSERT INTO `DataItemAllocation` VALUES ('34', 'txRThread', '10', '', 'conf', 'TT', '', '2013-08-12 12:39:12');
INSERT INTO `DataItemAllocation` VALUES ('35', 'ttWThread', '10', '', 'conf', 'TT', '', '2013-03-15 09:25:48');
INSERT INTO `DataItemAllocation` VALUES ('36', 'securityDBId', 'ZDesk', '', 'conf', 'ZDesk', '', '2013-03-15 09:25:50');
INSERT INTO `DataItemAllocation` VALUES ('37', 'smsIp', '128.128.128.128', '', 'conf', 'SMS', '', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('38', 'smsPort', '7891', '', 'conf', 'SMS', '', '2013-03-15 09:25:53');
INSERT INTO `DataItemAllocation` VALUES ('39', 'smsUser', '401588', '', 'conf', 'SMS', '', '2013-03-15 09:25:54');
INSERT INTO `DataItemAllocation` VALUES ('40', 'smsPassword', '401588', '', 'conf', 'SMS', '', '2013-03-15 09:25:55');
INSERT INTO `DataItemAllocation` VALUES ('41', '1003', '20071118133331506519242', '', 'dynamicField', 'SG', 'sgIdMap', '2013-03-15 10:37:58');
INSERT INTO `DataItemAllocation` VALUES ('42', '1004', '20071118133331506519242', '', 'dynamicField', 'SG', 'sgIdMap', '2013-03-15 10:37:57');
INSERT INTO `DataItemAllocation` VALUES ('43', '1005', '20071118133248416791445', '', 'dynamicField', 'SG', 'sgIdMap', '2013-03-15 10:37:56');
INSERT INTO `DataItemAllocation` VALUES ('44', '1006', '20071118133229008811450', '', 'dynamicField', 'SG', 'sgIdMap', '2013-03-15 10:37:56');
INSERT INTO `DataItemAllocation` VALUES ('45', '人员固定', '100', '人员固定', 'dynamicField', '其他', 'ruleTypeMap', '2013-03-15 10:38:46');
INSERT INTO `DataItemAllocation` VALUES ('46', '1007', '20071118133229008811450', '', 'dynamicField', 'SG', 'sgIdMap', '2013-03-15 10:37:55');
INSERT INTO `DataItemAllocation` VALUES ('47', '1008', '20071118133229008811450', '', 'dynamicField', 'SG', 'sgIdMap', '2013-03-15 10:37:55');
INSERT INTO `DataItemAllocation` VALUES ('48', '物理组一致', '200', '物理组一致', 'dynamicField', '其他', 'ruleTypeMap', '2013-03-15 10:38:44');
INSERT INTO `DataItemAllocation` VALUES ('49', '1009', '20071118133229008811450', '', 'dynamicField', 'SG', 'sgIdMap', '2013-03-15 10:37:56');
INSERT INTO `DataItemAllocation` VALUES ('50', '1010', '20071118133229008811450', '', 'dynamicField', 'SG', 'sgIdMap', '2013-03-15 10:37:51');
INSERT INTO `DataItemAllocation` VALUES ('51', '后续班次', '400', '后续班次', 'dynamicField', '其他', 'ruleTypeMap', '2013-03-15 10:38:43');
INSERT INTO `DataItemAllocation` VALUES ('52', '后续休息', '500', '后续休息', 'dynamicField', '其他', 'ruleTypeMap', '2013-03-15 10:38:43');
INSERT INTO `DataItemAllocation` VALUES ('53', '班次多样', '600', '班次多样', 'dynamicField', '其他', 'ruleTypeMap', '2013-03-15 10:38:42');
INSERT INTO `DataItemAllocation` VALUES ('54', '班次公平', '700', '班次公平', 'dynamicField', '其他', 'ruleTypeMap', '2013-03-15 10:38:42');
INSERT INTO `DataItemAllocation` VALUES ('55', '10', '10', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:00');
INSERT INTO `DataItemAllocation` VALUES ('56', '5', '5', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:03');
INSERT INTO `DataItemAllocation` VALUES ('57', '2', '2', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:11');
INSERT INTO `DataItemAllocation` VALUES ('58', '8', '8', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:03');
INSERT INTO `DataItemAllocation` VALUES ('59', '88', '88', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:12');
INSERT INTO `DataItemAllocation` VALUES ('60', '12', '12', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:11');
INSERT INTO `DataItemAllocation` VALUES ('61', '1001', '10', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:09');
INSERT INTO `DataItemAllocation` VALUES ('62', '1002', '12', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:09');
INSERT INTO `DataItemAllocation` VALUES ('63', '1001', '人工咨询', '', 'dynamicField', 'SG', 'sgNameMap', '2013-03-15 09:54:47');
INSERT INTO `DataItemAllocation` VALUES ('64', '1003', '8', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:08');
INSERT INTO `DataItemAllocation` VALUES ('65', '1004', '8', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:01');
INSERT INTO `DataItemAllocation` VALUES ('66', '1002', '英文服务', '', 'dynamicField', 'SG', 'sgNameMap', '2013-03-15 09:54:47');
INSERT INTO `DataItemAllocation` VALUES ('67', '1005', '5', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:07');
INSERT INTO `DataItemAllocation` VALUES ('68', '1003', '贵宾卡服务', '', 'dynamicField', 'SG', 'sgNameMap', '2013-03-15 09:54:46');
INSERT INTO `DataItemAllocation` VALUES ('69', '1006', '10', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:06');
INSERT INTO `DataItemAllocation` VALUES ('70', '1007', '10', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:05');
INSERT INTO `DataItemAllocation` VALUES ('71', '1004', '贵宾卡服务', '', 'dynamicField', 'SG', 'sgNameMap', '2013-03-15 09:54:45');
INSERT INTO `DataItemAllocation` VALUES ('72', '1008', '10', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:04');
INSERT INTO `DataItemAllocation` VALUES ('73', '1005', '人工投诉', '', 'dynamicField', 'SG', 'sgNameMap', '2013-03-15 09:54:45');
INSERT INTO `DataItemAllocation` VALUES ('74', '1009', '10', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:01');
INSERT INTO `DataItemAllocation` VALUES ('75', '1010', '10', '', 'dynamicField', 'SG', 'sgAggregateMap', '2013-03-15 09:44:21');
INSERT INTO `DataItemAllocation` VALUES ('76', '1006', '人工咨询', '', 'dynamicField', 'SG', 'sgNameMap', '2013-03-15 09:54:45');
INSERT INTO `DataItemAllocation` VALUES ('77', '1007', '人工咨询', '', 'dynamicField', 'SG', 'sgNameMap', '2013-03-15 09:54:43');
INSERT INTO `DataItemAllocation` VALUES ('78', '1008', '人工咨询', '', 'dynamicField', 'SG', 'sgNameMap', '2013-03-15 09:54:44');
INSERT INTO `DataItemAllocation` VALUES ('79', '1009', '人工咨询', '', 'dynamicField', 'SG', 'sgNameMap', '2013-03-15 09:54:44');
INSERT INTO `DataItemAllocation` VALUES ('80', '1010', '人工咨询', '', 'dynamicField', 'SG', 'sgNameMap', '2013-03-15 09:54:48');
INSERT INTO `DataItemAllocation` VALUES ('81', '10.3.3.2', 'true', '', 'dynamicField', '路由', 'routeIpMap', '2013-03-15 09:43:23');
INSERT INTO `DataItemAllocation` VALUES ('82', '1001', '2009072917287499096', '', 'dynamicField', 'SG', 'sgOldIdMap', '2013-03-15 09:54:51');
INSERT INTO `DataItemAllocation` VALUES ('83', '10.3.3.1', 'true', '', 'dynamicField', '路由', 'routeIpMap', '2013-03-15 09:43:23');
INSERT INTO `DataItemAllocation` VALUES ('84', '10.3.8.33', 'true', '', 'dynamicField', '路由', 'routeIpMap', '2013-03-15 09:43:23');
INSERT INTO `DataItemAllocation` VALUES ('85', '1002', '2009072917287499132', '', 'dynamicField', 'SG', 'sgOldIdMap', '2013-03-15 09:54:54');
INSERT INTO `DataItemAllocation` VALUES ('86', '10.3.8.34', 'true', '', 'dynamicField', '路由', 'routeIpMap', '2013-03-15 09:43:24');
INSERT INTO `DataItemAllocation` VALUES ('87', '1003', '2009072917287499116', '', 'dynamicField', 'SG', 'sgOldIdMap', '2013-03-15 09:55:00');
INSERT INTO `DataItemAllocation` VALUES ('88', '1004', '2009072917287499121', '', 'dynamicField', 'SG', 'sgOldIdMap', '2013-03-15 09:54:50');
INSERT INTO `DataItemAllocation` VALUES ('89', '1005', '2009072917287499110', '', 'dynamicField', 'SG', 'sgOldIdMap', '2013-03-15 09:55:00');
INSERT INTO `DataItemAllocation` VALUES ('90', '1006', '2009072917287499127', '', 'dynamicField', 'SG', 'sgOldIdMap', '2013-03-15 09:55:02');
INSERT INTO `DataItemAllocation` VALUES ('91', '1008', '2009072917287499142', '', 'dynamicField', 'SG', 'sgOldIdMap', '2013-03-15 09:54:55');
INSERT INTO `DataItemAllocation` VALUES ('92', '1009', '2009072917287499101', '', 'dynamicField', 'SG', 'sgOldIdMap', '2013-03-15 09:54:53');
INSERT INTO `DataItemAllocation` VALUES ('93', '1010', '2009072917287499137', '', 'dynamicField', 'SG', 'sgOldIdMap', '2013-03-15 09:54:50');
INSERT INTO `DataItemAllocation` VALUES ('94', '10.235.10.162', 'true', '', 'dynamicField', '路由', 'routeIpData', '2013-03-15 09:43:22');
INSERT INTO `DataItemAllocation` VALUES ('95', '10.235.10.161', 'true', '', 'dynamicField', '路由', 'routeIpData', '2013-03-15 09:43:21');
INSERT INTO `DataItemAllocation` VALUES ('96', '1001', '210000000', '', 'dynamicField', 'SG', 'virtualSgNameMap', '2013-03-15 09:42:53');
INSERT INTO `DataItemAllocation` VALUES ('97', '1002', '210000000', '', 'dynamicField', 'SG', 'virtualSgNameMap', '2013-03-15 09:42:42');
INSERT INTO `DataItemAllocation` VALUES ('98', '1001', '100', '', 'dynamicField', 'SG', 'sgkMap', '2013-03-15 09:42:25');
INSERT INTO `DataItemAllocation` VALUES ('99', '1002', '50', '', 'dynamicField', 'SG', 'sgkMap', '2013-03-15 09:42:22');
INSERT INTO `DataItemAllocation` VALUES ('100', '1003', '50', '', 'dynamicField', 'SG', 'sgkMap', '2013-03-15 09:42:20');
INSERT INTO `DataItemAllocation` VALUES ('101', '1004', '50', '', 'dynamicField', 'SG', 'sgkMap', '2013-03-15 09:42:16');
INSERT INTO `DataItemAllocation` VALUES ('102', '1005', '50', '', 'dynamicField', 'SG', 'sgkMap', '2013-03-15 09:42:17');
INSERT INTO `DataItemAllocation` VALUES ('103', '1006', '50', '', 'dynamicField', 'SG', 'sgkMap', '2013-03-15 09:42:18');
INSERT INTO `DataItemAllocation` VALUES ('104', '1007', '50', '', 'dynamicField', 'SG', 'sgkMap', '2013-03-15 09:42:19');
INSERT INTO `DataItemAllocation` VALUES ('105', '11', '11', '', 'dynamicField', '其他', 'swapSkillMap', '2013-03-15 09:45:29');
INSERT INTO `DataItemAllocation` VALUES ('106', '1008', '50', '', 'dynamicField', 'SG', 'sgkMap', '2013-03-15 09:42:21');
INSERT INTO `DataItemAllocation` VALUES ('107', '1009', '100', '', 'dynamicField', 'SG', 'sgkMap', '2013-03-15 09:42:25');
INSERT INTO `DataItemAllocation` VALUES ('108', '1010', '50', '', 'dynamicField', 'SG', 'sgkMap', '2013-03-15 09:42:14');
INSERT INTO `DataItemAllocation` VALUES ('109', '1001', '北京咨询', '', 'dynamicField', 'SG', 'sgOldNameMap', '2013-03-15 09:41:44');
INSERT INTO `DataItemAllocation` VALUES ('110', '1002', '北京英语咨询', '', 'dynamicField', 'SG', 'sgOldNameMap', '2013-03-15 09:41:47');
INSERT INTO `DataItemAllocation` VALUES ('111', '1003', '北京理财溢出', '', 'dynamicField', 'SG', 'sgOldNameMap', '2013-03-15 09:41:48');
INSERT INTO `DataItemAllocation` VALUES ('112', '1004', '北京理财不溢出', '', 'dynamicField', 'SG', 'sgOldNameMap', '2013-03-15 09:41:49');
INSERT INTO `DataItemAllocation` VALUES ('113', '1005', '北京投诉', '', 'dynamicField', 'SG', 'sgOldNameMap', '2013-03-15 09:41:33');
INSERT INTO `DataItemAllocation` VALUES ('114', '1006', '北京网点咨询组', '', 'dynamicField', 'SG', 'sgOldNameMap', '2013-03-15 09:41:50');
INSERT INTO `DataItemAllocation` VALUES ('115', '1007', '北京网点信息', '', 'dynamicField', 'SG', 'sgOldNameMap', '2013-03-15 09:41:52');
INSERT INTO `DataItemAllocation` VALUES ('116', '1008', '北京自助设备', '', 'dynamicField', 'SG', 'sgOldNameMap', '2013-03-15 09:41:46');
INSERT INTO `DataItemAllocation` VALUES ('117', '1009', '北京网银', '', 'dynamicField', 'SG', 'sgOldNameMap', '2013-03-15 09:41:52');
INSERT INTO `DataItemAllocation` VALUES ('118', '1010', '北京查询查复', '', 'dynamicField', 'SG', 'sgOldNameMap', '2013-03-15 09:41:51');
INSERT INTO `DataItemAllocation` VALUES ('119', '最小工作天数', '90', '时间', 'dynamicField', '其他', 'ruleActionMap', '2013-03-15 10:38:16');
INSERT INTO `DataItemAllocation` VALUES ('120', 'isMultiSkill', 'true', '座席是否对多技能组，true或false', 'conf', '其他', '', '2013-03-15 09:20:29');
INSERT INTO `DataItemAllocation` VALUES ('121', 'isFtp', 'false', '需要ftp上传文件是此项设置为true 并设置具体ftp信息', 'conf', '其他', '', '2013-03-15 09:20:38');
INSERT INTO `DataItemAllocation` VALUES ('122', 'isSaveLog', 'true', '', 'conf', '其他', '', '2013-03-15 09:20:52');
INSERT INTO `DataItemAllocation` VALUES ('123', 'posNum', '1', '', 'conf', '其他', '', '2013-03-15 09:20:04');
INSERT INTO `DataItemAllocation` VALUES ('124', 'posName', 'A', '', 'conf', '其他', '', '2013-03-15 09:21:10');
INSERT INTO `DataItemAllocation` VALUES ('125', 'isCalcFee', 'false', '是否计算话费', 'conf', '其他', '', '2013-03-15 09:22:38');
INSERT INTO `DataItemAllocation` VALUES ('126', 'isOptimizeMode', 'true', '部分数据模式 不载入会议 一次解决率等', 'conf', '其他', '', '2013-03-15 09:21:40');
INSERT INTO `DataItemAllocation` VALUES ('127', 'isNeedDir', 'false', '不会建立数据文件目录', 'conf', '其他', '', '2013-03-15 09:22:34');
INSERT INTO `DataItemAllocation` VALUES ('128', 'isLoadDay', 'false', '统计监听，临签，座席状态流水等', 'conf', '其他', '', '2013-03-15 09:23:01');
INSERT INTO `DataItemAllocation` VALUES ('129', 'isLazyMode', 'true', '被动载入座席信息', 'conf', '其他', '', '2013-03-15 09:23:29');
INSERT INTO `DataItemAllocation` VALUES ('130', 'isCluster', 'false', '是否为集群', 'conf', '其他', '', '2013-07-14 15:40:55');
INSERT INTO `DataItemAllocation` VALUES ('131', 'isSecArr', 'true', '', 'conf', '其他', '', '2013-03-15 09:23:47');
INSERT INTO `DataItemAllocation` VALUES ('132', 'isRouteIpMapContain', 'true', '', 'conf', '其他', '', '2013-03-15 09:23:49');
INSERT INTO `DataItemAllocation` VALUES ('133', 'isCallInDISTINCT', 'false', '', 'conf', '其他', '', '2013-03-15 09:23:51');
INSERT INTO `DataItemAllocation` VALUES ('134', 'isPhysicsGroupMode', 'true', '', 'conf', '其他', '', '2013-03-15 09:23:54');
INSERT INTO `DataItemAllocation` VALUES ('135', 'isPasswordCncrypt', 'false', '', 'conf', '其他', '', '2013-03-15 09:23:58');
INSERT INTO `DataItemAllocation` VALUES ('136', 'agentSoundCodec', '', '座席视频', '产品', '视频', '', '2013-03-08 08:34:56');
INSERT INTO `DataItemAllocation` VALUES ('137', 'agentSilenceLevel', '', '座席视频', '产品', '视频', '', '2013-03-08 08:35:34');
INSERT INTO `DataItemAllocation` VALUES ('138', 'agentGain', '', '座席视频', '产品', '视频', '', '2013-03-08 08:35:55');
INSERT INTO `DataItemAllocation` VALUES ('139', 'agentRate', '', '座席视频', '产品', '视频', '', '2013-03-08 08:36:19');
INSERT INTO `DataItemAllocation` VALUES ('140', 'agentEncodeQuality', '', '座席视频', '产品', '视频', '', '2013-03-08 08:36:41');
INSERT INTO `DataItemAllocation` VALUES ('141', 'agentIsUseEchoSuppression ', '', '座席视频', '产品', '视频', '', '2013-03-08 08:36:57');
INSERT INTO `DataItemAllocation` VALUES ('142', 'agentLoopBack', '', '座席视频', '产品', '视频', '', '2013-03-08 08:37:18');
INSERT INTO `DataItemAllocation` VALUES ('143', 'agentFramesPerPacket', '', '座席视频', '产品', '视频', '', '2013-03-08 08:39:36');
INSERT INTO `DataItemAllocation` VALUES ('144', 'agentMode', '', '座席视频', '产品', '视频', '', '2013-03-08 08:40:58');
INSERT INTO `DataItemAllocation` VALUES ('145', 'agentQuality', '', '座席视频', '产品', '视频', '', '2013-03-08 08:42:21');
INSERT INTO `DataItemAllocation` VALUES ('146', 'customerSoundCodec', '', '客户端视频', '产品', '视频', '', '2013-03-08 08:43:41');
INSERT INTO `DataItemAllocation` VALUES ('147', 'agentCamBufferTime', '', '座席视频', '产品', '视频', '', '2013-03-08 08:43:49');
INSERT INTO `DataItemAllocation` VALUES ('148', 'agentCamBufferTime', '', '座席视频', '产品', '视频', '', '2013-03-08 08:44:08');
INSERT INTO `DataItemAllocation` VALUES ('149', 'customerURL', '128.128.128.128', '客户端地址', '产品', '视频', '', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('150', 'agentPublish', '', '座席视频', '产品', '视频', '', '2013-03-08 08:44:34');
INSERT INTO `DataItemAllocation` VALUES ('151', 'customerSilenceLevel', '', '客户端视频', '产品', '视频', '', '2013-03-08 08:45:01');
INSERT INTO `DataItemAllocation` VALUES ('152', 'customerGain', '', '客户端视频', '产品', '视频', '', '2013-03-08 08:45:24');
INSERT INTO `DataItemAllocation` VALUES ('153', 'customerRate', '', '客户端视频', '产品', '视频', '', '2013-03-08 08:46:06');
INSERT INTO `DataItemAllocation` VALUES ('154', 'customerEncodeQuality', '', '客户端视频', '产品', '视频', '', '2013-03-08 08:46:31');
INSERT INTO `DataItemAllocation` VALUES ('155', 'customerIsUseEchoSuppression    ', '', '客户端视频', '产品', '视频', '', '2013-03-08 08:50:31');
INSERT INTO `DataItemAllocation` VALUES ('156', 'customerLoopBack', '', '客户端视频', '产品', '视频', '', '2013-03-08 08:47:32');
INSERT INTO `DataItemAllocation` VALUES ('157', 'customerFramesPerPacket', '', '客户端视频', '产品', '视频', '', '2013-03-08 08:48:14');
INSERT INTO `DataItemAllocation` VALUES ('158', 'customerKeyFrameInterval', '', '客户端视频', '产品', '视频', '', '2013-03-08 08:50:27');
INSERT INTO `DataItemAllocation` VALUES ('159', 'customerMode', '', '客户端视频', '产品', '视频', '', '2013-03-08 08:50:22');
INSERT INTO `DataItemAllocation` VALUES ('160', 'customerQuality', '', '客户端视频', '产品', '视频', '', '2013-03-08 08:50:14');
INSERT INTO `DataItemAllocation` VALUES ('161', 'customerCamBufferTime', '', '客户端视频', '产品', '视频', '', '2013-03-08 08:50:07');
INSERT INTO `DataItemAllocation` VALUES ('162', 'customerPublish', '', '客户端视频', '产品', '视频', '', '2013-03-08 08:55:02');
INSERT INTO `DataItemAllocation` VALUES ('163', 'agentURL', '128.128.128.128', '坐席端地址', '产品', '视频', '', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('164', 'dbip', '128.128.128.128', 'ssc z3000数据库', 'database', 'ssc数据库配置', 'z3000', '2014-12-19 23:28:01');
INSERT INTO `DataItemAllocation` VALUES ('165', 'dbo', '', 'ssc z3000数据库dbo', 'database', 'ssc数据库配置', 'z3000', '2013-03-15 07:13:17');
INSERT INTO `DataItemAllocation` VALUES ('166', 'id', 'z3000', 'ssc z3000数据库调用标识', 'database', 'ssc数据库配置', 'z3000', '2013-03-15 07:13:12');
INSERT INTO `DataItemAllocation` VALUES ('167', 'name', 'z3000', 'ssc z3000数据库名称', 'database', 'ssc数据库配置', 'z3000', '2013-03-15 07:13:06');
INSERT INTO `DataItemAllocation` VALUES ('168', 'password', '12', 'ssc z3000数据库密码', 'database', 'ssc数据库配置', 'z3000', '2013-03-15 07:12:57');
INSERT INTO `DataItemAllocation` VALUES ('169', 'port', '3306', 'ssc z3000数据库端口', 'database', 'ssc数据库配置', 'z3000', '2013-03-15 07:14:59');
INSERT INTO `DataItemAllocation` VALUES ('170', 'type', 'mysql', 'ssc z3000数据库类型', 'database', 'ssc数据库配置', 'z3000', '2013-03-15 07:15:51');
INSERT INTO `DataItemAllocation` VALUES ('171', 'userName', 'zinglabs', 'ssc z3000数据库用户名', 'database', 'ssc数据库配置', 'z3000', '2013-03-15 07:16:38');
INSERT INTO `DataItemAllocation` VALUES ('172', 'route', '', '', 'database', 'ssc数据库配置', 'z3000', '2013-03-15 07:40:05');
INSERT INTO `DataItemAllocation` VALUES ('173', 'charset', 'GBK', 'ssc z3000数据库编码', 'database', 'ssc数据库配置', 'z3000', '2013-03-15 07:40:42');
INSERT INTO `DataItemAllocation` VALUES ('174', 'dbip', '128.128.128.128', 'securityDB数据库IP', 'database', 'ZDesk数据库配置', 'ZDesk', '2014-12-19 23:27:58');
INSERT INTO `DataItemAllocation` VALUES ('175', 'dbo', '', 'securityDB数据库dbo', 'database', 'ZDesk数据库配置', 'ZDesk', '2013-03-15 08:10:24');
INSERT INTO `DataItemAllocation` VALUES ('176', 'id', 'ZDesk', 'securityDB数据库调用标识', 'database', 'ZDesk数据库配置', 'ZDesk', '2013-03-15 08:10:28');
INSERT INTO `DataItemAllocation` VALUES ('177', 'name', 'securityDB', 'securityDB数据库名称', 'database', 'ZDesk数据库配置', 'ZDesk', '2013-03-15 08:10:32');
INSERT INTO `DataItemAllocation` VALUES ('178', 'password', '12', 'securityDB数据库密码', 'database', 'ZDesk数据库配置', 'ZDesk', '2013-03-15 08:10:35');
INSERT INTO `DataItemAllocation` VALUES ('179', 'port', '3306', 'securityDB数据库端口', 'database', 'ZDesk数据库配置', 'ZDesk', '2013-03-15 08:10:39');
INSERT INTO `DataItemAllocation` VALUES ('180', 'type', 'mysql', 'securityDB数据库类型', 'database', 'ZDesk数据库配置', 'ZDesk', '2013-03-15 08:10:43');
INSERT INTO `DataItemAllocation` VALUES ('181', 'userName', 'zinglabs', 'securityDB数据库用户名', 'database', 'ZDesk数据库配置', 'ZDesk', '2013-03-15 08:10:47');
INSERT INTO `DataItemAllocation` VALUES ('182', 'route', '', '', 'database', 'ZDesk数据库配置', 'ZDesk', '2013-05-22 19:14:51');
INSERT INTO `DataItemAllocation` VALUES ('183', 'charset', '', 'securityDB数据库编码', 'database', 'ZDesk数据库配置', 'ZDesk', '2013-03-15 08:10:53');
INSERT INTO `DataItemAllocation` VALUES ('184', 'dbip', '128.128.128.128', 'securityDB数据库IP', 'database', 'ZKM数据库配置', 'ZKM', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('185', 'dbo', '', 'securityDB数据库dbo', 'database', 'ZKM数据库配置', 'ZKM', '2013-03-15 08:25:41');
INSERT INTO `DataItemAllocation` VALUES ('186', 'id', 'ZKM', 'securityDB数据库调用标识', 'database', 'ZKM数据库配置', 'ZKM', '2013-03-15 08:25:43');
INSERT INTO `DataItemAllocation` VALUES ('187', 'name', 'securityDB', 'securityDB数据库名称', 'database', 'ZKM数据库配置', 'ZKM', '2013-03-15 08:25:45');
INSERT INTO `DataItemAllocation` VALUES ('188', 'password', '12', 'securityDB数据库密码', 'database', 'ZKM数据库配置', 'ZKM', '2013-03-15 08:25:46');
INSERT INTO `DataItemAllocation` VALUES ('189', 'port', '3306', 'securityDB数据库端口', 'database', 'ZKM数据库配置', 'ZKM', '2013-03-15 08:25:48');
INSERT INTO `DataItemAllocation` VALUES ('190', 'type', 'mysql', 'securityDB数据库类型', 'database', 'ZKM数据库配置', 'ZKM', '2013-03-15 08:25:49');
INSERT INTO `DataItemAllocation` VALUES ('191', 'userName', 'zinglabs', 'securityDB数据库用户名', 'database', 'ZKM数据库配置', 'ZKM', '2013-03-15 08:25:56');
INSERT INTO `DataItemAllocation` VALUES ('192', 'route', '', '', 'database', 'ZKM数据库配置', 'ZKM', '2013-05-22 19:14:49');
INSERT INTO `DataItemAllocation` VALUES ('193', 'charset', '', 'securityDB数据库编码', 'database', 'ZKM数据库配置', 'ZKM', '2013-03-15 08:26:02');
INSERT INTO `DataItemAllocation` VALUES ('194', 'dbip', '128.128.128.128', 'securityDB数据库IP', 'database', 'Report数据库配置', 'Report', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('195', 'dbo', '', 'securityDB数据库dbo', 'database', 'Report数据库配置', 'Report', '2013-03-15 08:26:15');
INSERT INTO `DataItemAllocation` VALUES ('196', 'id', 'Report', 'securityDB数据库调用标识', 'database', 'Report数据库配置', 'Report', '2013-03-15 08:26:19');
INSERT INTO `DataItemAllocation` VALUES ('197', 'name', 'securityDB', 'securityDB数据库名称', 'database', 'Report数据库配置', 'Report', '2013-03-15 08:26:25');
INSERT INTO `DataItemAllocation` VALUES ('198', 'password', '12', 'securityDB数据库密码', 'database', 'Report数据库配置', 'Report', '2013-03-15 08:26:30');
INSERT INTO `DataItemAllocation` VALUES ('199', 'port', '3306', 'securityDB数据库端口', 'database', 'Report数据库配置', 'Report', '2013-03-15 08:26:33');
INSERT INTO `DataItemAllocation` VALUES ('200', 'type', 'mysql', 'securityDB数据库类型', 'database', 'Report数据库配置', 'Report', '2013-03-15 08:26:35');
INSERT INTO `DataItemAllocation` VALUES ('201', 'userName', 'zinglabs', 'securityDB数据库用户名', 'database', 'Report数据库配置', 'Report', '2013-03-15 08:26:38');
INSERT INTO `DataItemAllocation` VALUES ('202', 'route', '', '', 'database', 'Report数据库配置', 'Report', '2013-05-22 19:14:48');
INSERT INTO `DataItemAllocation` VALUES ('203', 'charset', '', 'securityDB数据库编码', 'database', 'Report数据库配置', 'Report', '2013-03-15 08:26:41');
INSERT INTO `DataItemAllocation` VALUES ('204', 'dbip', '128.128.128.128', 'ZShifts数据库IP', 'database', 'ZWM数据库配置', 'ZWM', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('205', 'dbo', '', 'ZShifts数据库dbo', 'database', 'ZWM数据库配置', 'ZWM', '2013-03-15 08:26:50');
INSERT INTO `DataItemAllocation` VALUES ('206', 'id', 'ZWM', 'ZShifts数据库调用标识', 'database', 'ZWM数据库配置', 'ZWM', '2013-03-15 08:26:51');
INSERT INTO `DataItemAllocation` VALUES ('207', 'name', 'ZShifts', 'ZShifts数据库名称', 'database', 'ZWM数据库配置', 'ZWM', '2013-03-15 08:26:52');
INSERT INTO `DataItemAllocation` VALUES ('208', 'password', '12', 'ZShifts数据库密码', 'database', 'ZWM数据库配置', 'ZWM', '2013-03-15 08:26:52');
INSERT INTO `DataItemAllocation` VALUES ('209', 'port', '3306', 'ZShifts数据库端口', 'database', 'ZWM数据库配置', 'ZWM', '2013-03-15 08:26:52');
INSERT INTO `DataItemAllocation` VALUES ('210', 'type', 'mysql', 'ZShifts据库类型', 'database', 'ZWM数据库配置', 'ZWM', '2013-03-15 08:26:53');
INSERT INTO `DataItemAllocation` VALUES ('211', 'userName', 'zinglabs', 'ZShifts数据库用户名', 'database', 'ZWM数据库配置', 'ZWM', '2013-03-15 08:26:53');
INSERT INTO `DataItemAllocation` VALUES ('212', 'route', '', '', 'database', 'ZWM数据库配置', 'ZWM', '2013-05-22 19:14:49');
INSERT INTO `DataItemAllocation` VALUES ('213', 'charset', '', 'ZShifts数据库编码', 'database', 'ZWM数据库配置', 'ZWM', '2013-03-15 08:26:54');
INSERT INTO `DataItemAllocation` VALUES ('214', 'dbip', '128.128.128.128', 'securityDB数据库IP', 'database', 'ZQC数据库配置', 'ZQC', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('215', 'dbo', '', 'securityDB数据库dbo', 'database', 'ZQC数据库配置', 'ZQC', '2013-03-15 08:27:02');
INSERT INTO `DataItemAllocation` VALUES ('216', 'id', 'ZQC', 'securityDB数据库调用标识', 'database', 'ZQC数据库配置', 'ZQC', '2013-03-15 08:27:03');
INSERT INTO `DataItemAllocation` VALUES ('217', 'name', 'securityDB', 'securityDB数据库名称', 'database', 'ZQC数据库配置', 'ZQC', '2013-03-15 08:27:03');
INSERT INTO `DataItemAllocation` VALUES ('218', 'password', '12', 'securityDB数据库密码', 'database', 'ZQC数据库配置', 'ZQC', '2013-03-15 08:27:03');
INSERT INTO `DataItemAllocation` VALUES ('219', 'port', '3306', 'securityDB数据库端口', 'database', 'ZQC数据库配置', 'ZQC', '2013-03-15 08:27:04');
INSERT INTO `DataItemAllocation` VALUES ('220', 'type', 'mysql', 'securityDB数据库类型', 'database', 'ZQC数据库配置', 'ZQC', '2013-03-15 08:27:04');
INSERT INTO `DataItemAllocation` VALUES ('221', 'userName', 'zinglabs', 'securityDB数据库用户名', 'database', 'ZQC数据库配置', 'ZQC', '2013-03-15 08:27:05');
INSERT INTO `DataItemAllocation` VALUES ('222', 'route', '', '', 'database', 'ZQC数据库配置', 'ZQC', '2013-05-22 19:14:50');
INSERT INTO `DataItemAllocation` VALUES ('223', 'charset', '', 'securityDB数据库编码', 'database', 'ZQC数据库配置', 'ZQC', '2013-03-15 08:27:08');
INSERT INTO `DataItemAllocation` VALUES ('224', 'dbip', '128.128.128.128', '数据库IP', 'database', 'ZMonitor数据库配置', 'ZMonitor', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('225', 'dbo', '', '数据库dbo', 'database', 'ZMonitor数据库配置', 'ZMonitor', '2013-03-15 08:27:20');
INSERT INTO `DataItemAllocation` VALUES ('226', 'id', 'ZMonitor', '数据库调用标识', 'database', 'ZMonitor数据库配置', 'ZMonitor', '2013-03-15 08:27:21');
INSERT INTO `DataItemAllocation` VALUES ('227', 'name', 'securityDB', '数据库名称', 'database', 'ZMonitor数据库配置', 'ZMonitor', '2013-08-12 11:31:53');
INSERT INTO `DataItemAllocation` VALUES ('228', 'password', '12', '数据库密码', 'database', 'ZMonitor数据库配置', 'ZMonitor', '2013-08-11 22:46:04');
INSERT INTO `DataItemAllocation` VALUES ('229', 'port', '3306', '数据库端口', 'database', 'ZMonitor数据库配置', 'ZMonitor', '2013-03-15 08:27:22');
INSERT INTO `DataItemAllocation` VALUES ('230', 'type', 'mysql', '数据库类型', 'database', 'ZMonitor数据库配置', 'ZMonitor', '2013-03-15 08:27:25');
INSERT INTO `DataItemAllocation` VALUES ('231', 'userName', 'zinglabs', '数据库用户名', 'database', 'ZMonitor数据库配置', 'ZMonitor', '2013-03-15 08:27:25');
INSERT INTO `DataItemAllocation` VALUES ('232', 'route', '', '', 'database', 'ZMonitor数据库配置', 'ZMonitor', '2013-05-22 19:14:52');
INSERT INTO `DataItemAllocation` VALUES ('233', 'charset', '', 'openfire数据库编码', 'database', 'ZMonitor数据库配置', 'ZMonitor', '2013-03-15 08:27:27');
INSERT INTO `DataItemAllocation` VALUES ('234', 'dbip', '128.128.128.128', 'openfire数据库IP', 'database', 'openfire数据库配置', 'openfire', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('235', 'dbo', '', 'openfire数据库dbo', 'database', 'openfire数据库配置', 'openfire', '2013-03-15 08:27:35');
INSERT INTO `DataItemAllocation` VALUES ('236', 'id', 'openfire', 'openfire数据库调用标识', 'database', 'openfire数据库配置', 'openfire', '2013-03-15 08:27:35');
INSERT INTO `DataItemAllocation` VALUES ('237', 'name', 'openfire', 'openfire数据库名称', 'database', 'openfire数据库配置', 'openfire', '2013-03-15 08:27:35');
INSERT INTO `DataItemAllocation` VALUES ('238', 'password', '12', 'openfire数据库密码', 'database', 'openfire数据库配置', 'openfire', '2013-03-15 08:27:36');
INSERT INTO `DataItemAllocation` VALUES ('239', 'port', '3306', 'openfire数据库端口', 'database', 'openfire数据库配置', 'openfire', '2013-03-15 08:27:36');
INSERT INTO `DataItemAllocation` VALUES ('240', 'type', 'mysql', 'openfire数据可以类型', 'database', 'openfire数据库配置', 'openfire', '2013-03-15 08:27:36');
INSERT INTO `DataItemAllocation` VALUES ('241', 'userName', 'zinglabs', 'openfire数据库用户名', 'database', 'openfire数据库配置', 'openfire', '2013-03-15 08:27:37');
INSERT INTO `DataItemAllocation` VALUES ('242', 'route', '', '', 'database', 'openfire数据库配置', 'openfire', '2014-03-10 12:32:07');
INSERT INTO `DataItemAllocation` VALUES ('243', 'charset', '', 'openfire数据库编码', 'database', 'openfire数据库配置', 'openfire', '2013-03-15 08:27:38');
INSERT INTO `DataItemAllocation` VALUES ('244', 'dbip', '128.128.128.128', 'z3000数据库IP', 'database', 'z3000IM数据库配置', 'z3000IM', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('245', 'dbo', '', 'z3000数据库dbo', 'database', 'z3000IM数据库配置', 'z3000IM', '2013-03-15 08:29:27');
INSERT INTO `DataItemAllocation` VALUES ('246', 'id', 'z3000IM', 'z3000数据库调用标识', 'database', 'z3000IM数据库配置', 'z3000IM', '2013-03-15 08:29:27');
INSERT INTO `DataItemAllocation` VALUES ('247', 'name', 'z3000', 'z3000数据库名称', 'database', 'z3000IM数据库配置', 'z3000IM', '2013-03-15 08:29:27');
INSERT INTO `DataItemAllocation` VALUES ('248', 'password', '12', 'z3000数据库密码', 'database', 'z3000IM数据库配置', 'z3000IM', '2013-03-15 08:29:28');
INSERT INTO `DataItemAllocation` VALUES ('249', 'port', '3306', 'z3000数据库端口', 'database', 'z3000IM数据库配置', 'z3000IM', '2013-03-15 08:29:28');
INSERT INTO `DataItemAllocation` VALUES ('250', 'type', 'mysql', 'z3000数据库类型', 'database', 'z3000IM数据库配置', 'z3000IM', '2013-03-15 08:29:32');
INSERT INTO `DataItemAllocation` VALUES ('251', 'userName', 'zinglabs', 'z3000数据库用户名', 'database', 'z3000IM数据库配置', 'z3000IM', '2013-03-15 08:29:32');
INSERT INTO `DataItemAllocation` VALUES ('252', 'route', '', '', 'database', 'z3000IM数据库配置', 'z3000IM', '2013-03-15 08:29:32');
INSERT INTO `DataItemAllocation` VALUES ('253', 'charset', '', 'z3000数据库编码', 'database', 'z3000IM数据库配置', 'z3000IM', '2013-03-15 08:29:33');
INSERT INTO `DataItemAllocation` VALUES ('254', 'dbip', '128.128.128.128', 'z3000_ext数据库IP', 'database', 'z3000_ext数据库配置', 'z3000_ext', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('255', 'dbo', '', 'z3000_ext数据库dbo', 'database', 'z3000_ext数据库配置', 'z3000_ext', '2013-03-15 08:29:41');
INSERT INTO `DataItemAllocation` VALUES ('256', 'id', 'z3000_ext', 'z3000_ext数据库调用标识', 'database', 'z3000_ext数据库配置', 'z3000_ext', '2013-03-15 08:29:41');
INSERT INTO `DataItemAllocation` VALUES ('257', 'name', 'z3000_ext', 'z3000_ext数据库名称', 'database', 'z3000_ext数据库配置', 'z3000_ext', '2013-03-15 08:29:41');
INSERT INTO `DataItemAllocation` VALUES ('258', 'password', '12', 'z3000_ext数据库密码', 'database', 'z3000_ext数据库配置', 'z3000_ext', '2013-03-15 08:29:43');
INSERT INTO `DataItemAllocation` VALUES ('259', 'port', '3306', 'z3000_ext数据库端口', 'database', 'z3000_ext数据库配置', 'z3000_ext', '2013-03-15 08:29:41');
INSERT INTO `DataItemAllocation` VALUES ('260', 'type', 'mysql', 'z3000_ext据库类型', 'database', 'z3000_ext数据库配置', 'z3000_ext', '2013-03-15 08:29:42');
INSERT INTO `DataItemAllocation` VALUES ('261', 'userName', 'zinglabs', 'z3000_ext数据库用户名', 'database', 'z3000_ext数据库配置', 'z3000_ext', '2013-03-15 08:29:46');
INSERT INTO `DataItemAllocation` VALUES ('262', 'route', '', '', 'database', 'z3000_ext数据库配置', 'z3000_ext', '2013-05-22 19:14:45');
INSERT INTO `DataItemAllocation` VALUES ('263', 'charset', '', 'z3000_ext数据库编码', 'database', 'z3000_ext数据库配置', 'z3000_ext', '2013-03-15 08:29:49');
INSERT INTO `DataItemAllocation` VALUES ('264', 'dbip', '128.128.128.128', 'securityDB数据库IP', 'database', 'endTimeDb数据库配置', 'endTimeDb', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('265', 'dbo', '', 'securityDB数据库dbo', 'database', 'endTimeDb数据库配置', 'endTimeDb', '2013-03-15 08:30:05');
INSERT INTO `DataItemAllocation` VALUES ('266', 'id', 'endTimeDb', 'securityDB数据库调用标识', 'database', 'endTimeDb数据库配置', 'endTimeDb', '2013-03-15 08:30:06');
INSERT INTO `DataItemAllocation` VALUES ('267', 'name', 'securityDB', 'securityDB数据库名称', 'database', 'endTimeDb数据库配置', 'endTimeDb', '2013-03-15 08:30:06');
INSERT INTO `DataItemAllocation` VALUES ('268', 'password', '12', 'securityDB数据库密码', 'database', 'endTimeDb数据库配置', 'endTimeDb', '2013-03-15 08:30:07');
INSERT INTO `DataItemAllocation` VALUES ('269', 'port', '3306', 'securityDB数据库端口', 'database', 'endTimeDb数据库配置', 'endTimeDb', '2013-03-15 08:30:07');
INSERT INTO `DataItemAllocation` VALUES ('270', 'type', 'mysql', 'securityDB数据库类型', 'database', 'endTimeDb数据库配置', 'endTimeDb', '2013-03-15 08:30:07');
INSERT INTO `DataItemAllocation` VALUES ('271', 'userName', 'zinglabs', 'securityDB数据库用户名', 'database', 'endTimeDb数据库配置', 'endTimeDb', '2013-03-15 08:30:08');
INSERT INTO `DataItemAllocation` VALUES ('272', 'route', '', '', 'database', 'endTimeDb数据库配置', 'endTimeDb', '2013-03-15 08:30:08');
INSERT INTO `DataItemAllocation` VALUES ('273', 'charset', '', 'securityDB数据库编码', 'database', 'endTimeDb数据库配置', 'endTimeDb', '2013-03-15 08:30:09');
INSERT INTO `DataItemAllocation` VALUES ('274', 'dbip', '128.128.128.128', 'clcrdb数据库IP', 'database', 'csrSyncDB数据库配置', 'csrSyncDB', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('275', 'dbo', '', 'clcrdb数据库dbo', 'database', 'csrSyncDB数据库配置', 'csrSyncDB', '2013-03-15 08:30:19');
INSERT INTO `DataItemAllocation` VALUES ('276', 'id', 'csrSyncDB', 'clcrdb数据库调用标识', 'database', 'csrSyncDB数据库配置', 'csrSyncDB', '2013-03-15 08:30:20');
INSERT INTO `DataItemAllocation` VALUES ('277', 'name', 'clcrdb', 'clcrdb数据库名称', 'database', 'csrSyncDB数据库配置', 'csrSyncDB', '2013-03-15 08:30:20');
INSERT INTO `DataItemAllocation` VALUES ('278', 'password', 'sa1234', 'clcrdb数据库密码', 'database', 'csrSyncDB数据库配置', 'csrSyncDB', '2013-03-15 08:30:20');
INSERT INTO `DataItemAllocation` VALUES ('279', 'port', '4200', 'clcrdb数据库端口', 'database', 'csrSyncDB数据库配置', 'csrSyncDB', '2013-03-15 08:30:21');
INSERT INTO `DataItemAllocation` VALUES ('280', 'type', 'sybase', 'clcrdb数据库类型', 'database', 'csrSyncDB数据库配置', 'csrSyncDB', '2013-03-15 08:30:21');
INSERT INTO `DataItemAllocation` VALUES ('281', 'userName', 'sa', 'clcrdb数据库用户名', 'database', 'csrSyncDB数据库配置', 'csrSyncDB', '2013-03-15 08:30:22');
INSERT INTO `DataItemAllocation` VALUES ('282', 'route', '', '', 'database', 'csrSyncDB数据库配置', 'csrSyncDB', '2013-03-15 08:30:22');
INSERT INTO `DataItemAllocation` VALUES ('283', 'charset', '', 'clcrdb数据库编码', 'database', 'csrSyncDB数据库配置', 'csrSyncDB', '2013-03-15 08:30:23');
INSERT INTO `DataItemAllocation` VALUES ('284', 'dbip', '128.128.128.128', 'ZShifts数据库IP', 'database', 'tarGetDB数据库配置', 'tarGetDB', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('285', 'dbo', '', 'ZShifts数据库dbo', 'database', 'tarGetDB数据库配置', 'tarGetDB', '2013-03-15 08:30:35');
INSERT INTO `DataItemAllocation` VALUES ('286', 'id', 'tarGetDB', 'ZShifts数据库调用标识', 'database', 'tarGetDB数据库配置', 'tarGetDB', '2013-03-15 08:30:36');
INSERT INTO `DataItemAllocation` VALUES ('287', 'name', 'ZShifts', 'ZShifts数据库名称', 'database', 'tarGetDB数据库配置', 'tarGetDB', '2013-03-15 08:30:36');
INSERT INTO `DataItemAllocation` VALUES ('288', 'password', '12', 'ZShifts数据库密码', 'database', 'tarGetDB数据库配置', 'tarGetDB', '2013-03-15 08:30:36');
INSERT INTO `DataItemAllocation` VALUES ('289', 'port', '3306', 'ZShifts数据库端口', 'database', 'tarGetDB数据库配置', 'tarGetDB', '2013-03-15 08:30:37');
INSERT INTO `DataItemAllocation` VALUES ('290', 'type', 'mysql', 'ZShifts据库类型', 'database', 'tarGetDB数据库配置', 'tarGetDB', '2013-03-15 08:30:37');
INSERT INTO `DataItemAllocation` VALUES ('291', 'userName', 'zinglabs', 'ZShifts数据库用户名', 'database', 'tarGetDB数据库配置', 'tarGetDB', '2013-03-15 08:30:38');
INSERT INTO `DataItemAllocation` VALUES ('292', 'route', '', '', 'database', 'tarGetDB数据库配置', 'tarGetDB', '2013-03-15 08:30:38');
INSERT INTO `DataItemAllocation` VALUES ('293', 'charset', '', 'ZShifts数据库编码', 'database', 'tarGetDB数据库配置', 'tarGetDB', '2013-03-15 08:30:39');
INSERT INTO `DataItemAllocation` VALUES ('294', 'openfireATPort', '5222', 'openfire 服务端口', 'conf', 'TT', 'TT', '2013-05-16 13:30:21');
INSERT INTO `DataItemAllocation` VALUES ('295', 'ZDeskDOMAIN', '128.128.128.128', '座席端访问地址 域名 ip格式', 'conf', 'ZDesk', '', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('296', 'ZKM_CLUSTER_OPEN', 'false', '标志知识库开启集群功能', 'conf', '其他', '', '2013-05-22 19:16:28');
INSERT INTO `DataItemAllocation` VALUES ('297', 'ZKM_SERVER_IP', 'http://128.128.128.128', '集群环境知识库ip一般指向负载均衡，需加http://', 'conf', '其他', '', '2014-10-30 10:59:39');
INSERT INTO `DataItemAllocation` VALUES ('298', 'isCalZQCReport', 'true', '是否计算质检报表', 'conf', '其他', '', '2013-05-29 03:35:04');
INSERT INTO `DataItemAllocation` VALUES ('299', 'isCheckZQCAgent', 'true', '质检同步程序是否检查座席信息', 'conf', '其他', '', '2013-05-29 03:46:04');
INSERT INTO `DataItemAllocation` VALUES ('300', 'isSyncZQCRecord', 'true', '是否同步录音数据', 'conf', '其他', '', '2013-05-29 03:46:36');
INSERT INTO `DataItemAllocation` VALUES ('301', 'PROJECT_TYPE', 'BOC', '', 'conf', '其他', '', '2013-07-14 15:43:02');
INSERT INTO `DataItemAllocation` VALUES ('302', 'QQ_CHECK_CSR_IP', '', '配成空，就不会验证qq客户有效性', 'conf', '其他', '', '2013-12-30 00:02:23');
INSERT INTO `DataItemAllocation` VALUES ('303', 'ZIMDOMAIN', '128.128.128.128', '', 'conf', 'ZDesk', '', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('226564', 'openfire2Down', '', '', '', '', '', '2014-03-10 12:44:39');
INSERT INTO `DataItemAllocation` VALUES ('226565', 'groupserv0', '128.128.128.128', '群聊服务器0地址', 'group', '', '', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('226566', 'recordIsRuning0', 'true', '第i个聊天记录服务是否运行', 'record', '', '', '2014-03-30 21:25:54');
INSERT INTO `DataItemAllocation` VALUES ('226567', 'recordTaskCount', '3', '报表聊天服务上运行的线程数量', 'record', '', '', '2014-03-30 21:26:10');
INSERT INTO `DataItemAllocation` VALUES ('226568', 'recordServCount', '1', '运行的聊天记录及报表计算服务数量', 'record', '', '', '2014-03-30 21:26:32');
INSERT INTO `DataItemAllocation` VALUES ('226569', 'recordip0', '128.128.128.128', '第i个报表聊天记录服务ip地址,i从0起', 'record', '', '', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('226570', 'gRecServCount', '1', '运行的群聊服务器数量', 'record', '', '', '2014-05-19 00:06:15');
INSERT INTO `DataItemAllocation` VALUES ('226571', 'gRecip0', '128.128.128.128', '群聊聊天记录保存地址0', 'record', '', '', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('226572', 'gRecTaskCount', '3', '群聊保存服务器每服务器线程数', 'record', '', '', '2014-04-02 16:21:41');
INSERT INTO `DataItemAllocation` VALUES ('226672', 'MemIP2', '128.128.128.128', 'ttserver服务器IP', 'conf', '', '', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('226670', 'MemIP1', '128.128.128.128', 'ttserver服务器IP', 'conf', '', '', '2014-10-30 10:59:50');
INSERT INTO `DataItemAllocation` VALUES ('226673', 'sscSubCount', '5', '接收座席消息线程', 'conf', '', '', '2014-05-18 23:12:23');
INSERT INTO `DataItemAllocation` VALUES ('228317', 'ZDESK_EXCEL_EXP_TASK_TIME', '03:00:00', 'excel导出任务执行时间', 'conf', '', '', '2014-03-27 00:00:00');
INSERT INTO `DataItemAllocation` VALUES ('228319', 'ZDESK_EXCEL_EXP_TASK_RUNCYCLE', '60000', 'excel导出任务检测时间', 'conf', '', '', '2014-03-27 00:00:00');
INSERT INTO `DataItemAllocation` VALUES ('228321', 'ZDESK_EXCEL_EXP_DOWNLOAD_PATH_REPORT', '/ZDesk/reportExcelExpFolder', 'excel导出任务文件存放目录', 'conf', '', '', '2014-03-27 00:00:00');
INSERT INTO `DataItemAllocation` VALUES ('228323', 'ATSwitch', '128.128.128.128', null, 'system', null, '', '2013-12-14 23:17:20');
INSERT INTO `DataItemAllocation` VALUES ('927060', 'application', 'VConference', '视频应用地址', '产品', '视频', '', '2014-09-10 03:22:14');
INSERT INTO `DataItemAllocation` (`peizhiItem`, `peizhiItemValue`, `desc`, `bItem`, `sItem`, `productionId`, `generateTime`) VALUES ('relativePath', 'files', '文件上传相对路径', 'fileupload', '文件上传配置', 'fileUpload', '2015-01-07 11:21:21');
INSERT INTO `DataItemAllocation` (`peizhiItem`, `peizhiItemValue`, `desc`, `bItem`, `sItem`, `productionId`, `generateTime`) VALUES ('0', 'http://127.0.0.1:8888/ZDesk/remoteCall', NULL, 'remoteServer', NULL, 'ZKM', '2014-12-12 03:09:39');
INSERT INTO `DataItemAllocation` (`peizhiItem`, `peizhiItemValue`, `desc`, `bItem`, `sItem`, `productionId`, `generateTime`) VALUES ('1', 'http://127.0.0.1:8888/ZDesk/remoteCall', NULL, 'remoteServer', NULL, 'ZKM', '2014-12-12 03:09:46');
INSERT INTO `DataItemAllocation` (`peizhiItem`, `peizhiItemValue`, `desc`, `bItem`, `sItem`, `productionId`, `generateTime`) VALUES ('0', 'http://127.0.0.1:8888/ZDesk/remoteCall', NULL, 'remoteServer', NULL, 'ZQC', '2014-12-22 12:58:06');
INSERT INTO `DataItemAllocation` (`peizhiItem`, `peizhiItemValue`, `desc`, `bItem`, `sItem`, `productionId`, `generateTime`) VALUES ('1', 'http://127.0.0.1:8888/ZDesk/remoteCall', NULL, 'remoteServer', NULL, 'ZQC', '2014-12-22 12:58:40');




-- ----------------------------------
-- 自定义信息模板所需sql
-- ---------------------------------
-- 分类树配置表
INSERT INTO `zkmCommonTree` VALUES ('26', 'parent.test', NULL, 'D37759F7-5D0A-EBD7-EB93-AEAB7E1B61B6', '模板分类树', '', 'info_template', '0', '/ZDesk/ZKM/ZKMCommonTree/getTreeNode.action', '', NULL, NULL, NULL, '');

-- 删除分类树数据
DELETE FROM `commonTree_detail`  WHERE id='4F8736B9-2EA3-0A15-C047-B48B196B944D';
DELETE FROM `commonTree_detail`  WHERE id='0476D525-E12F-B895-2A08-89C6DDC7FA38';
DELETE FROM `commonTree_detail`  WHERE id='183BDA3C-1E18-0AF8-5274-FE67DDB31645';
-- 模板分类树数据
INSERT INTO `commonTree_detail` VALUES ('2A59CCD8-DEF8-5659-E16A-7272336A94CC', '自定义模板', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2014-11-28 11:06:07', 'customText/CustomTextTemplate.html', '', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail` VALUES ('4F8736B9-2EA3-0A15-C047-B48B196B944D', '分类三', '', 'true', 'info_template', '', 'ZSKadmin', '2014-11-28 11:02:27', '', '2', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail` VALUES ('0476D525-E12F-B895-2A08-89C6DDC7FA38', '分类二', '', 'true', 'info_template', '', 'ZSKadmin', '2014-11-28 11:02:19', '', '1', '', '', '', '', '', '', '', '', '', '', '');
INSERT INTO `commonTree_detail` VALUES ('183BDA3C-1E18-0AF8-5274-FE67DDB31645', '分类一', '', 'true', 'info_template', '', 'ZSKadmin', '2014-11-28 11:02:09', '', '0', '', '', '', '', '', '', '', '', '', '', '');

-- 菜单授权
INSERT INTO `suSecurityRoleMapPermission`  VALUES ('327', '系统管理员', '2A59CCD8-DEF8-5659-E16A-7272336A94CC', 'menu');

DROP TABLE IF EXISTS `CustomTextTemplate`;
CREATE TABLE `CustomTextTemplate` (
  `id` char(36) NOT NULL COMMENT '模板id',
  `name` varchar(50) DEFAULT '' COMMENT '模板名称',
  `templateType` char(50) DEFAULT '' COMMENT '模板类型，动态dynamic,静态static',
  `maxSize` int(11) DEFAULT '0' COMMENT '总长度',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


-- 模板数据表sql
DROP TABLE IF EXISTS `CustomTextTemplateData`;
CREATE TABLE `CustomTextTemplateData` (
  `id` char(36) NOT NULL COMMENT '模板数据id',
  `content` varchar(50) DEFAULT '' COMMENT '模板数据内容',
  `type` varchar(20) DEFAULT '' COMMENT '模板数据类型constant常量variable变量',
  `viewIndex` int(11) DEFAULT '9999' COMMENT '模板数据显示顺序',
  `templateId` char(36) DEFAULT '' COMMENT '模板id',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 树与数据关联表
DROP TABLE IF EXISTS `commonTreeDataMapper`;
CREATE TABLE `commonTreeDataMapper` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `nodeId` char(36) DEFAULT '' COMMENT '树节点id',
  `dataId` char(36) DEFAULT '' COMMENT '树下数据id',
  PRIMARY KEY (`id`),
  KEY `nodeId` (`nodeId`),
  KEY `dataId` (`dataId`)
) ENGINE=MyISAM AUTO_INCREMENT=40998 DEFAULT CHARSET=utf8;



-- ----------------------------
-- 文件上传
-- ----------------------------
DROP TABLE IF EXISTS `UploadSave`;
CREATE TABLE `UploadSave` (
  `id` varchar(255) NOT NULL DEFAULT '',
  `fileName` varchar(255) NOT NULL DEFAULT '1',
  `newFileName` varchar(255) DEFAULT '',
  `fileType` varchar(255) DEFAULT '',
  `savePath` varchar(255) DEFAULT '',
  `fileSize` varchar(255) CHARACTER SET utf8 DEFAULT '',
  `uploadTime` datetime DEFAULT NULL COMMENT '文件上传时间',
  `state` varchar(1) DEFAULT '1' COMMENT '文件当前状态，0已删除，1未删除',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- 文件上传与数据关联
-- ----------------------------
DROP TABLE IF EXISTS `FileDataMapper`;
CREATE TABLE `FileDataMapper` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fileId` varchar(50) DEFAULT '',
  `dataId` varchar(50) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
-- 文件上传菜单
INSERT INTO `commonTree_detail` VALUES ('AC2B7DE9-E1C8-50B3-C385-969630AC5B46', '文件上传管理', '', 'true', 'menu', '34958053-6D4E-E1E8-05AB-E65F29CDFDDC', 'ZSKadmin', '2015-01-14 15:38:49', '../webUploader/FileUploadManagement.html', '', '', '', '', '', '', '', '', '', '', '', 'fileUpload');
-- 文件上传菜单授权
INSERT INTO `suSecurityRoleMapPermission` VALUES ('339', '系统管理员', 'AC2B7DE9-E1C8-50B3-C385-969630AC5B46', 'menu');




-- ----------------------------
-- 质检模块基础表
-- ----------------------------
DROP TABLE IF EXISTS `ZQC_STATE_MANAGER`;
CREATE TABLE `ZQC_STATE_MANAGER` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stateKey` varchar(255) DEFAULT '' COMMENT '状态的key',
  `stateValue` varchar(255) DEFAULT '' COMMENT '状态值',
  `langugeCode` varchar(255) DEFAULT '' COMMENT '语言代码',
  `projectCode` varchar(255) DEFAULT '' COMMENT '项目代码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- 状态表数据
-- ----------------------------
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('1', 'zero', '0', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('2', 'chujianweifenpei', '初检未分配', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('3', 'chujianyifenpei', '初检已分配', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('4', 'chujianyifabu', '初检已发布', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('5', 'fujianweifenpei', '复检未分配', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('6', 'fujianyifenpei', '复检已分配', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('7', 'chujianyipingfen', '初检已评分', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('8', 'chujianweipingfen', '初检未评分', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('9', 'fujianyipingfen', '复检已评分', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('10', 'zuoxishangsu', '座席上诉', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('11', 'zuoxishangsuchenggong', '座席上诉成功', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('12', 'zuoxishangsushibai', '座席上诉失败', 'zh_cn', 'ylx');
INSERT INTO `ZQC_STATE_MANAGER` VALUES ('13', 'fujianweipingfen', '复检未评分', 'zh_cn', 'ylx');
