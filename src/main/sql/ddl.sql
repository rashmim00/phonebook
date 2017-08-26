create database phonebook default character set utf8;

grant all on phonebook.* to phonebook@'localhost' identified by 'phonebook';
grant all on phonebook.* to phonebook@'127.0.0.1' identified by 'phonebook';
flush privileges;

CREATE TABLE if not exists phonebook.contact (
     id MEDIUMINT NOT NULL AUTO_INCREMENT,
     address VARCHAR(255),
     birthdate bigint,
     company VARCHAR(255),
     email  VARCHAR(255),
     favorite int,
     firstName  VARCHAR(255),
     lastName  VARCHAR(255),
     PRIMARY KEY (id)
);

INSERT INTO `phonebook`.`contact` (`id`, `address`, `email`, `favorite`, `firstName`, `lastName`) VALUES ('1', '636 Chelsea Crossing, San Jose, CA', 'rrrr@gmail.com', '1', 'Rashmi', 'Goyal');

CREATE TABLE if not exists phonebook.groups (
     id MEDIUMINT NOT NULL AUTO_INCREMENT,
	 groupName  VARCHAR(255),
     groupIcon  VARCHAR(255),
     PRIMARY KEY (id)
);

INSERT INTO `phonebook`.`groups` (`id`, `groupName`) VALUES ('1', 'Grammy Lovers');

CREATE TABLE if not exists phonebook.group_participants (
  groupId int NOT NULL default '0',
  contactId int NOT NULL default '0',
   PRIMARY KEY (groupId, contactId)
 );
 
 INSERT INTO `phonebook`.`group_participants` (`groupId`, `contactId`) VALUES ('1', '1');
 
 CREATE TABLE if not exists phonebook.phoneNumber (
     contact_id MEDIUMINT NOT NULL,
     phone_type  VARCHAR(255),
     phone_number  VARCHAR(255),
     PRIMARY KEY (contact_id, phone_type)
);

INSERT INTO `phonebook`.`phonenumber` (`contact_id`, `phone_type`, `phone_number`) VALUES ('1', 'home', '408-444-2134');
INSERT INTO `phonebook`.`phonenumber` (`contact_id`, `phone_type`, `phone_number`) VALUES ('2', 'Mobile', '408-111-2222');

