/*
Navicat MySQL Data Transfer

Source Server         : 1
Source Server Version : 50636
Source Host           : 192.168.1.80:3306
Source Database       : system_comprehensive_management

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2017-11-29 11:44:46
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(32) DEFAULT NULL COMMENT '验证码',
  `username` varchar(128) DEFAULT NULL COMMENT '用户名',
  `client_id` varchar(32) DEFAULT NULL COMMENT '应用id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='认证code表';
