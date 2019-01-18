CREATE TABLE IF NOT EXISTS acl_sid (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  principal tinyint(1) NOT NULL,
  sid varchar(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY unique_uk_1 (sid,principal)
);

CREATE TABLE IF NOT EXISTS acl_class (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  class varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY unique_uk_2 (class)
);

CREATE TABLE IF NOT EXISTS acl_entry (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  acl_object_identity bigint(20) NOT NULL,
  ace_order int(11) NOT NULL,
  sid bigint(20) NOT NULL,
  mask int(11) NOT NULL,
  granting tinyint(1) NOT NULL,
  audit_success tinyint(1) NOT NULL,
  audit_failure tinyint(1) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY unique_uk_4 (acl_object_identity,ace_order)
);

CREATE TABLE IF NOT EXISTS acl_object_identity (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  object_id_class bigint(20) NOT NULL,
  object_id_identity bigint(20) NOT NULL,
  parent_object bigint(20) DEFAULT NULL,
  owner_sid bigint(20) DEFAULT NULL,
  entries_inheriting tinyint(1) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY unique_uk_3 (object_id_class,object_id_identity)
);

ALTER TABLE acl_entry
ADD FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);

ALTER TABLE acl_entry
ADD FOREIGN KEY (sid) REFERENCES acl_sid(id);

--
-- Constraints for table acl_object_identity
--
ALTER TABLE acl_object_identity
ADD FOREIGN KEY (parent_object) REFERENCES acl_object_identity (id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (object_id_class) REFERENCES acl_class (id);

ALTER TABLE acl_object_identity
ADD FOREIGN KEY (owner_sid) REFERENCES acl_sid (id);

insert into users(username, password, enabled) values
('admin'  , '$2a$12$PD9BYaK1yxzPwDww60UG.OIWbc1mp/uV9tNPpdgrRt5vnxz1OhpCq', true), -- pass
('steven' , '$2a$12$PD9BYaK1yxzPwDww60UG.OIWbc1mp/uV9tNPpdgrRt5vnxz1OhpCq', true), -- pass
('darrin' , '$2a$12$PD9BYaK1yxzPwDww60UG.OIWbc1mp/uV9tNPpdgrRt5vnxz1OhpCq', true), -- pass
('jason'  , '$2a$12$PD9BYaK1yxzPwDww60UG.OIWbc1mp/uV9tNPpdgrRt5vnxz1OhpCq', true), -- pass
('jon'    , '$2a$12$PD9BYaK1yxzPwDww60UG.OIWbc1mp/uV9tNPpdgrRt5vnxz1OhpCq', true); -- pass

insert into authorities (username, authority) values
('admin'  , 'ROLE_ADMIN'),
('steven' , 'ROLE_USER'),
('darrin' , 'ROLE_USER'),
('jason'  , 'ROLE_USER'),
('jon'    , 'ROLE_USER');

insert into groups(id, group_name) values
(1, 'Admins'),
(2, 'Users');

insert into group_authorities(group_id, authority) values
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER');

insert into group_members(id, username, group_id) values
-- Admins
(1, 'steven', 1),
-- Users
(2, 'steven', 2),
(3, 'darrin', 2),
(4, 'jason' , 2),
(5, 'jon'   , 2);

-- insert into banks(id, name, created_by, created_datetime, last_modified_by, last_modified_datetime, version) values
-- (1, 'JP Morgan Chase', 'admin', CURRENT_TIMESTAMP(), 'admin', CURRENT_TIMESTAMP(), 0),
-- (2, 'Bank of America', 'admin', CURRENT_TIMESTAMP(), 'admin', CURRENT_TIMESTAMP(), 0),
-- (3, 'Wells Fargo', 'admin', CURRENT_TIMESTAMP(), 'admin', CURRENT_TIMESTAMP(), 0);