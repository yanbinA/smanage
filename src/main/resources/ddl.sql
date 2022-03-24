DROP TABLE IF EXISTS `s_local_auth`;
CREATE TABLE `s_local_auth` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) unsigned NOT NULL,
    `username` varchar(50) COLLATE utf8mb4_bin NOT NULL,
    `phone_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
    `mailbox` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
    `password` varchar(255) COLLATE utf8mb4_bin NOT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `s_user`;
CREATE TABLE `s_user` (
    `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    `gender` int(10) unsigned NOT NULL DEFAULT '1',
    `avatar` varchar(255) COLLATE utf8mb4_bin NOT NULL,
    `status` int(10) unsigned NOT NULL DEFAULT '1',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `s_menu`;

CREATE TABLE `s_menu` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `url` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `parent_id` int(11) DEFAULT NULL,
    `enabled` tinyint(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    KEY `parentId` (`parent_id`),
    CONSTRAINT `ibfk_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `s_menu` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `s_role`;
CREATE TABLE `s_role` (
    `id` int(11) NOT NULL,
    `name` varchar(50) COLLATE utf8mb4_bin NOT NULL,
    `nickname` varchar(50) COLLATE utf8mb4_bin NOT NULL,
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

DROP TABLE IF EXISTS `s_menu_role`;
CREATE TABLE `s_menu_role` (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `mid` int(11) NOT NULL,
   `rid` int(11) NOT NULL,
   PRIMARY KEY (`id`),
   KEY `mid` (`mid`),
   KEY `rid` (`rid`),
   CONSTRAINT `ibfk_menu_role_mid` FOREIGN KEY (`mid`) REFERENCES `s_menu` (`id`),
   CONSTRAINT `ibfk_menu_role_rid` FOREIGN KEY (`rid`) REFERENCES `s_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;