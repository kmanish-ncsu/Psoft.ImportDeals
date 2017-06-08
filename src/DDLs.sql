CREATE TABLE `deal` (
  `id` int(11) NOT NULL,
  `deal_id` varchar(45) DEFAULT NULL,
  `from_currency` char(3) DEFAULT NULL,
  `to_currency` char(3) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `source_file` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `source_file_idx` (`source_file`),
  CONSTRAINT `source_file` FOREIGN KEY (`source_file`) REFERENCES `deal_source_file` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1

CREATE TABLE `deal_count` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `currency` char(3) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=latin1

CREATE TABLE `deal_source_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `source_file` varchar(45) DEFAULT NULL,
  `valid_rows` int(11) DEFAULT NULL,
  `invalid_rows` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=latin1

CREATE TABLE `invalid_deal` (
  `id` int(11) NOT NULL,
  `deal_id` varchar(45) DEFAULT NULL,
  `from_currency` varchar(45) DEFAULT NULL,
  `to_currency` varchar(45) DEFAULT NULL,
  `timestamp` timestamp NULL DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `source_file` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1

#TRUNCATE
truncate deal;commit;
truncate invalid_deal;commit;
truncate deal_count;commit;
delete from deal_source_file;commit;
