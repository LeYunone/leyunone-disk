# Disk

乐云一私用云盘，自主研发中

![](https://img-blog.csdnimg.cn/img_convert/1af1244c07f65025bb48958b0e704b92.png)

## 部署教程：

### 1.数据库

```sql
CREATE TABLE `file_extend_content`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `file_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '文件id\'',
  `file_content` longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL COMMENT '文件文本内容，按比例缩小',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

CREATE TABLE `file_folder`  (
  `folder_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `folder_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '文件夹名',
  `is_folder` tinyint(1) NULL DEFAULT NULL COMMENT '是否为文件夹 0否 1是',
  `parent_id` int(0) NULL DEFAULT NULL COMMENT '父id',
  `file_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '文件id',
  `update_dt` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`folder_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

CREATE TABLE `file_history`  (
  `history_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '主键id',
  `file_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '文件id',
  `file_key` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '上传标识',
  `upload_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '上传id',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态 0请求上传 1上传阶段 2上传结束',
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '备注',
  `env` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '环境 local/oss',
  `md5` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '冗余字段',
  `create_dt` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`history_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

CREATE TABLE `file_info`  (
  `file_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '主键',
  `file_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '文件名',
  `file_size` int(0) NULL DEFAULT NULL COMMENT '文件大小',
  `file_type` tinyint(1) NULL DEFAULT NULL COMMENT '文件类型',
  `file_path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT '文件路径',
  `file_md5` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NULL DEFAULT NULL COMMENT 'md5码',
  `update_dt` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_dt` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`file_id`) USING BTREE,
  INDEX `con_idx`(`file_md5`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;
```

### 2.配置文件yaml

```yaml
#阿里云oss配置
oss:
	endpoint: XX
	accessKeyId: XX
	accessKeySecret: XX
	bucketName: XX
	bucketUrl: XX
#网盘配置
disk:
	env: oss  #oss或者local
	fileAddress: e:/test #当环境为local时的文件存储地址
	storage: 2	#未上传完成的任务保存的最大天数
	download:
		count: 4 #下载时的并发线程数
```

### 3.前端

见VUE项目地址：[https://github.com/LeYunone/leyunone-disk-vue](https://github.com/LeYunone/leyunone-disk-vue)

