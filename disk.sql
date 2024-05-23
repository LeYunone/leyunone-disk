/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : 127.0.0.1:3306
 Source Schema         : disk

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 24/05/2024 00:22:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_folder
-- ----------------------------
DROP TABLE IF EXISTS `file_folder`;
CREATE TABLE `file_folder`  (
  `folder_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `folder_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '文件夹名',
  `is_folder` tinyint(1) NULL DEFAULT NULL COMMENT '是否为文件夹 0否 1是',
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父id',
  `file_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '文件id',
  `update_dt` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`folder_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 170 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for file_info
-- ----------------------------
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info`  (
  `file_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '文件名',
  `file_size` int(11) NULL DEFAULT NULL COMMENT '文件大小',
  `file_type` tinyint(1) NULL DEFAULT NULL COMMENT '文件类型',
  `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '文件路径',
  `file_md5` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'md5码',
  `update_dt` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_dt` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
