-- DROP USER fa_admin;
CREATE USER fa_admin IDENTIFIED BY 'fa_admin';
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE, SHOW VIEW, TRIGGER ON fireart.* TO 'fa_admin'@'localhost' IDENTIFIED BY 'fa_admin';
FLUSH PRIVILEGES;

-- DROP USER fa_client;
CREATE USER fa_client IDENTIFIED BY 'fa_client';
GRANT SELECT, EXECUTE, SHOW VIEW ON fireart.* TO 'fa_client'@'localhost' IDENTIFIED BY 'fa_client';
FLUSH PRIVILEGES;