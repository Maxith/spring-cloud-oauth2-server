/*
Navicat MySQL Data Transfer

Source Server         : 1
Source Server Version : 50636
Source Host           : 192.168.1.80:3306
Source Database       : system_comprehensive_management

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2017-11-29 11:44:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for oauth_client
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client`;
CREATE TABLE `oauth_client` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(128) DEFAULT NULL COMMENT '应用名称',
  `client_id` varchar(32) DEFAULT NULL COMMENT '应用id',
  `client_secret` varchar(64) DEFAULT NULL COMMENT '应用密钥',
  `redirect_uri` varchar(256) DEFAULT NULL COMMENT '应用重定向地址',
  `client_uri` varchar(256) DEFAULT NULL COMMENT '应用地址',
  `description` varchar(1024) DEFAULT NULL COMMENT '应用描述',
  `icon_uri` varchar(256) DEFAULT NULL COMMENT '应用icon地址',
  `scope` varchar(256) DEFAULT NULL COMMENT '应用范围',
  `grant_types` varchar(256) NOT NULL COMMENT '应用所支持的授权模式,至少一个,多个用逗号隔开',
  `access_token_validity` bigint(20) DEFAULT NULL COMMENT '认证令牌有效期COMMENT ,单位秒',
  `refresh_token_validity` bigint(20) DEFAULT NULL COMMENT '刷新令牌有效期COMMENT ,单位秒',
  `trusted` tinyint(4) DEFAULT '0' COMMENT '是否受信任的,0,不受信,1受信',
  `archived` tinyint(4) DEFAULT '0' COMMENT '逻辑删除标志,0未删除,1已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_client_id` (`client_id`),
  UNIQUE KEY `uidx_client_secret` (`client_secret`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='应用表';
