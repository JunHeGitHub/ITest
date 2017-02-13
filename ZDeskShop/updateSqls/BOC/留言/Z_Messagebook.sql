
-- ----------------------------
-- Table structure for Z_Messagebook 留言数据表
-- ----------------------------

	/***增加字段***/
	ALTER TABLE Z_Messagebook  ADD   LYLX VARCHAR(32) NOT NULL ;
	/***填充数据***/
	update Z_Messagebook set LYLX="咨询"
	
	/***增加字段***/
	ALTER TABLE Z_Messagebook  ADD   commitment_reply_time datetime NOT NULL ;
	
	
	
