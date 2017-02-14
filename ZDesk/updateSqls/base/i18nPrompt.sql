-- 表结构
DROP TABLE IF EXISTS `i18nPrompt`;
CREATE TABLE `i18nPrompt` (
  `id` varchar(64) NOT NULL COMMENT '非业务主键',
  `promptKey` varchar(255) DEFAULT '' COMMENT '提示信息的key值，由此字段判断取什么提示信息！',
  `promptValue` varchar(500) DEFAULT '' COMMENT '提示信息，存放需要提示的具体说明文字',
  `languge` varchar(32) DEFAULT '' COMMENT '提示信息的语言。',
  `langugeCode` varchar(32) DEFAULT '' COMMENT '语言代码（例：zh-cn 中国 en 英国）',
  `projectName` varchar(32) DEFAULT '' COMMENT '提示信息所对应的项目名称。',
  `projectCode` varchar(32) DEFAULT '' COMMENT '对应项目的代码（如：ZSH 中国石化）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `promptKey` (`promptKey`) USING BTREE,
  KEY `langugeCode` (`langugeCode`) USING BTREE,
  KEY `projectType` (`projectName`) USING BTREE
);
-- 系统初始化默认数据
INSERT INTO `i18nPrompt` VALUES ('2C63F38B-E607-734A-55D9-28CFAF9EAD3C', 'system_selectDataForDelete', '\\u8bf7\\u9009\\u4e2d\\u8981\\u5220\\u9664\\u7684\\u6570\\u636e\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('33CE64ED-EE8B-678C-3F16-4E3DA310522A', 'system_updateSuccess', '\\u4fee\\u6539\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('6463C52F-2268-DFBF-D5C7-76C63BD3E810', 'system_deleteFailed', '\\u5220\\u9664\\u5931\\u8d25\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('6BC801AD-A605-6C29-19F9-DCB5AEDF0B62', 'system_saveFailed', '\\u4fdd\\u5b58\\u5931\\u8d25\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('91135CC3-7B86-122C-D67E-C2A401053763', 'SystemGeneration_yuyanchuangjianchenggong', '\\u8bed\\u8a00\\u521b\\u5efa\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('96F7D0CB-040C-5F2F-EFFA-363ADF5D6DA9', 'system_updateFailed', '\\u4fee\\u6539\\u5931\\u8d25\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('C0FCAD8C-C4E4-E282-D974-E0B728D9197F', 'system_deleteSuccess', '\\u5220\\u9664\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('C5E792C6-6C3C-0DB7-5169-40DEA621417F', 'i18nPrompt_noLanguge', '\\u672a\\u68c0\\u7d22\\u5230\\u8bed\\u8a00\\u4fe1\\u606f\\uff0c\\u8bf7\\u5148\\u521b\\u5efa\\u8bed\\u8a00\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('DDCC7A04-A67E-DCD9-111F-07438FC3FCDB', 'system_deleteConfirmation', '\\u786e\\u8ba4\\u8981\\u5220\\u9664\\u6b64\\u6570\\u636e\\u5417\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('EC119E39-6239-E2A3-27BF-BB558C0AE5A5', 'system_signOutConfirmation', '\\u786e\\u8ba4\\u8981\\u9000\\u51fa\\u7a0b\\u5e8f\\u5417\\uff1f', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('EF7D5E74-DFDD-FFAF-B62A-0CAD31B86B07', 'system_canNotBeEmpty', '${0} \\u4e0d\\u80fd\\u4e3a\\u7a7a\\u503c\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` VALUES ('F10083B9-0DFB-0046-3693-53F01F72AA28', 'system_saveSuccess', '\\u4fdd\\u5b58\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`) VALUES ('30EC2B9C-2D42-446E-74BB-36FF5F7003EF', 'permission_success', '\\u6388\\u6743\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
INSERT INTO `i18nPrompt` (`id`, `promptKey`, `promptValue`, `languge`, `langugeCode`, `projectName`, `projectCode`) VALUES ('839CDAA3-B8CF-E9CD-E6B3-0524609BDFA2', 'permission_role', '\\u8bf7\\u9009\\u62e9\\u89d2\\u8272\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');

-- 模拟数据
-- INSERT INTO `i18nPrompt` VALUES ('30EBAF69-068A-3D75-D5F9-EFBBC32F8E79', 'deleteOK', '\\u6570\\u636e\\u5220\\u9664\\u6210\\u529f\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('375C808D-78A4-6B23-DB71-EE5156C73179', 'ceshi', '\\u6d4b\\u8bd5', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('523099FA-3356-9589-4C5F-4EE73FAB4C1B', 'a', '\\u062d\\u0641\\u0638\\u0628\\u0646\\u062c\\u0627\\u062d!阿拉伯语', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('68A30701-0269-0E06-FA56-CB07DD52E870', 'b', 'Erfolgreich gerettet!德语', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('6A62E601-F3DF-3317-FC7C-D61B1990A5B4', 'addOk', '\\ucd94\\uac00 \\uc131\\uacf5!', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('6C979CF9-ED66-03C5-82B0-70EC46D1AF61', 'c', '\\u0441\\u043e\\u0445\\u0440\\u0430\\u043d\\u0438\\u0442\\u044c\\u0443\\u0441\\u043f\\u0435\\u0445!俄语', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('6DBD709D-D5FE-EAA9-BC0E-D6FE2ECA96D3', 'yuyandaimayicunzai', '\\u8be5\\u8bed\\u8a00\\u4ee3\\u7801\\u5df2\\u5b58\\u5728\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('6DC40BE5-9907-B228-FE9E-842306480FE7', 'd', 'Sauver le succ\\u00e8s!Si vous voulez supprimer cet article de donn\\u00e9es?法语', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('762AD3DE-11A9-09CA-28C2-E73903E0C19C', 'deletelose', '\\u5220\\u9664\\u5931\\u8d25\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('7FFF3968-F291-9907-4B71-8523932648B4', 'e', '\\uc800\\uc7a5 \\uc131\\uacf5!\\uc800\\uc7a5 \\uc2e4\\ud328!\\ud639\\uc2dc韩语', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('813E012C-486F-0560-248E-AA13AB398FE8', 'baocunyichang', '\\u4fdd\\u5b58\\u51fa\\u73b0\\u5f02\\u5e38\\uff0c\\u8bf7\\u8054\\u7cfb\\u7cfb\\u7edf\\u7ba1\\u7406\\u5458\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('842331C3-CC86-6B40-07B3-D06B1759B4B6', 'f', 'Salvo com SUCESSO!Deseja apagar este artigo?葡萄牙语', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('B1A66CC1-EA0E-66DA-F14E-4E839BF707F2', 'h', '\\u0e1a\\u0e31\\u0e19\\u0e17\\u0e36\\u0e01\\u0e04\\u0e27\\u0e32\\u0e21\\u0e2a\\u0e33\\u0e40\\u0e23\\u0e47\\u0e08泰语', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('B1F0D462-46FB-88B1-BE90-3D560396DEB0', 'isnull', '\\u8868\\u5355\\u4e2d\\u4e0d\\u80fd\\u6709\\u7a7a\\u503c\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('B2A2D258-55F4-30D2-8FF5-3F737DB17D13', 'i', '\\u00a1Guardar el \\u00e9xito!西班牙语', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('C8EE17BF-FC63-D4A8-603E-0BE36F0A2F65', 'exitOK', '\\u6210\\u529f\\u9000\\u51fa\\u7cfb\\u7edf\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('CCAD994B-4732-C782-9790-608E3088F9FA', 'prompt', '\\u63d0\\u793a', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('DD9F85F1-AA12-43F3-F8D8-62CC463BA707', 'tishikeyzhiyicunzai', '\\u63d0\\u793akey\\u503c\\u5df2\\u5b58\\u5728\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('E0024854-F5DE-024D-6E71-3072A837D8F6', 'k', 'Successfully saved!英语', '中文', 'zh_CN', '英立讯', 'YLX');
-- INSERT INTO `i18nPrompt` VALUES ('E8F08094-E3E9-235D-BA82-A710D7EAF9B6', 'notKey', '\\u4eb2\\uff0c\\u672a\\u83b7\\u53d6\\u5230\\u63d0\\u793a\\u4fe1\\u606f\\uff01\\u8bf7\\u67e5\\u770bkey\\u662f\\u5426\\u5b58\\u5728\\uff01', '中文', 'zh_CN', '英立讯', 'YLX');
