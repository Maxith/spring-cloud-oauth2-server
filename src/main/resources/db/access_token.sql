/*
Navicat MySQL Data Transfer

Source Server         : 1
Source Server Version : 50636
Source Host           : 192.168.1.80:3306
Source Database       : system_comprehensive_management

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2017-11-29 11:44:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for access_token
-- ----------------------------
DROP TABLE IF EXISTS `access_token`;
CREATE TABLE `access_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `token_id` varchar(32) DEFAULT NULL COMMENT '令牌token',
  `username` varchar(128) DEFAULT NULL COMMENT '用户名',
  `client_id` varchar(32) DEFAULT NULL COMMENT '应用id',
  `authentication_id` varchar(32) DEFAULT NULL COMMENT '授权id',
  `refresh_token` varchar(32) DEFAULT NULL COMMENT '刷新token',
  `token_type` varchar(32) DEFAULT 'Bearer' COMMENT '认证类型',
  `token_expired_seconds` bigint(20) DEFAULT NULL COMMENT '应用token过期时间,单位秒',
  `refresh_token_expired_seconds` bigint(20) DEFAULT NULL COMMENT '刷新token过期时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_token_id` (`token_id`),
  UNIQUE KEY `uidx_authentication_id` (`authentication_id`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8 COMMENT='认证令牌表';
