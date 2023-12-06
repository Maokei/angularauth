CREATE TABLE `users` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `username` varchar(50) NOT NULL,
    `password` varchar(100) NOT NULL,
    `enabled` tinyint(4) NOT NULL DEFAULT 1,
    PRIMARY KEY (`id`)
);

CREATE TABLE `events` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    `description` varchar(1000) NOT NULL,
    `date` date NOT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO `events` (`id`, `name`, `description`, `date`) VALUES (NULL, 'Fix computer', 'Repair old asus', Date('2023-12-12'));
INSERT INTO `events` (`id`, `name`, `description`, `date`) VALUES (NULL, 'Fix microwave', 'Install new microwave parts', '2023-12-13');