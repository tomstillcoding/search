/*
 Navicat MySQL Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : search_engine

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 09/07/2022 21:15:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for segment
-- ----------------------------
DROP TABLE IF EXISTS `segment`;
CREATE TABLE `segment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `word` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1121935 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
