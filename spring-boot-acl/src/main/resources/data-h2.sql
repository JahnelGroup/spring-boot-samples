/*
 * This table serves a dual purpose to store information about either principals or
 * role/authority. If the this entry is for a user then the principal column would be
 * set to true.
 *
 * principal = true if the entry is for a user, false if for a role or authority
 * sid = Security Identifier (i.e, username if principal is true)
 */
CREATE TABLE IF NOT EXISTS acl_sid (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  principal tinyint(1) NOT NULL,
  sid varchar(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY unique_uk_1 (sid,principal)
);

/*
 * This table stores a list of objects that are protected by an ACL.

 * class = fully qualified classname of an entity being protected
 */
CREATE TABLE IF NOT EXISTS acl_class (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  class varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY unique_uk_2 (class)
);

/*
 * This table store all the Access Control Entries (ACE's). It details the role
 * needed to access a particular object.
 *
 * acl_object_identity = primary key of the object instance being protected
 * ace_order = precesdnece order
 * sid = user or role/authority owning the entity
 * mask = admin/write/read
 * granting = always set to true for auditing. TBD.
 * audit_success = always set to true for auditing. TBD.
 * audit_failure = always set to true for auditing. TBD.
 */
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

/*
 * Similar to acl_class but stores information related to a specific instance
 * of a class that is being protected.
 *
 * object_id_class = references id of the class from acl_class
 * object_id_identity = primary key of the object instance being protected
 * parent_object = links to the parent of this object (if it exists) .. TBD..
 * owner_sid = user or role/authority owning the entity
 * entries_inheriting = TBD...
 */
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
('Steven' , '$2y$12$8kcuL.1Jg.6/Ai3YtSSjUeMHUNI5bqt/bJXZFtiElPdhtt3S/r/X6', true), -- pass
('Darrin' , '$2y$12$8kcuL.1Jg.6/Ai3YtSSjUeMHUNI5bqt/bJXZFtiElPdhtt3S/r/X6', true), -- pass
('Jason'  , '$2y$12$8kcuL.1Jg.6/Ai3YtSSjUeMHUNI5bqt/bJXZFtiElPdhtt3S/r/X6', true), -- pass
('Jon'    , '$2y$12$8kcuL.1Jg.6/Ai3YtSSjUeMHUNI5bqt/bJXZFtiElPdhtt3S/r/X6', true); -- pass

insert into authorities (username, authority) values
('Steven' , 'ROLE_USER'),
('Darrin' , 'ROLE_USER'),
('Jason'  , 'ROLE_USER'),
('Jon'    , 'ROLE_USER');