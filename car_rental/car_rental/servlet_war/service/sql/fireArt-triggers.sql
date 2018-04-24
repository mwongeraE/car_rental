-- fireArt Database Data
-- Version 2.0

-- use schema
USE fireart;

-- settings
/*
arrived_message	Driver Arrived Message	Your Driver has arrived.
cancelled_message	Booking Cancelled Message	Booking cancellation has been Confirmed.
cognalys_access_token	Cognalys Access Token	put_access_token_here
cognalys_app_id	Cognalys Application Id	put_app_id_number_here
google_maps_key	Google Map Key	put_key_here
otp_message	OTP Message	You code is %s
password_recovery_from	Password Recovery From	put_email_address_here
password_recovery_message	Password Recovery Message	Do not reply to this email, Your password is: %s
pasword_recovery_subject	Password Recovey Subject	Password Recovery
payment_message	Payment Confirmation Message	Payment has been made successfully.
paypal_client_id	Pay Pal Client Id	put_client_id_number_here
paypal_key	Pay Pal Key	put_key_here
paypal_secret	Pay Pal Secret Key	put_secret_key_here
paypal_type_of_payment	Pay Pal Type Of Payment : Sandbox, CreditCard	put_type_of_payment_here
paypal_webhook_id	Pay Pal WebHook Id	put_webhook_id_number_here
smtp_hostname	SMTP server hostname	http://mailgun.com
smtp_password	SMTP server password	pass
smtp_username	SMTP server username	user
stripe_key	Stripe Key	put_key_here
stripe_secret	Stripe Secret Key	put_secret_key_here
stripe_token_id	Stripe Token Id	put_token_id_number_here
*/

INSERT INTO settings (code,name,value) VALUES('arrived_message','Driver Arrived Message','Your Driver has arrived.');
INSERT INTO settings (code,name,value) VALUES('cancelled_message','Booking Cancelled Message','Booking cancellation has been Confirmed.');
INSERT INTO settings (code,name,value) VALUES('cognalys_access_token','Cognalys Access Token','put_access_token_here');
INSERT INTO settings (code,name,value) VALUES('cognalys_app_id','Cognalys Application Id','put_app_id_number_here');
INSERT INTO settings (code,name,value) VALUES('google_maps_key','Google Map Key','put_key_here');
INSERT INTO settings (code,name,value) VALUES('otp_message','OTP Message','You code is %s');
INSERT INTO settings (code,name,value) VALUES('password_recovery_from','Password Recovery From','put_email_address_here');
INSERT INTO settings (code,name,value) VALUES('password_recovery_message','Password Recovery Message','Do not reply to this email, Your password is: %s');
INSERT INTO settings (code,name,value) VALUES('pasword_recovery_subject','Password Recovey Subject','Password Recovery');
INSERT INTO settings (code,name,value) VALUES('payment_message','Payment Confirmation Message','Payment has been completed.');
INSERT INTO settings (code,name,value) VALUES('paypal_client_id','Pay Pal Client Id','put_client_id_number_here');
INSERT INTO settings (code,name,value) VALUES('paypal_key','Pay Pal Key','put_key_here');
INSERT INTO settings (code,name,value) VALUES('paypal_secret','Pay Pal Secret Key','put_secret_key_here');
INSERT INTO settings (code,name,value) VALUES('paypal_type_of_payment','Pay Pal Type Of Payment : Sandbox, CreditCard','put_type_of_payment_here');
INSERT INTO settings (code,name,value) VALUES('paypal_webhook_id','Pay Pal WebHook Id','put_webhook_id_number_here');
INSERT INTO settings (code,name,value) VALUES('smtp_hostname','SMTP server hostname','http://mailgun.com');
INSERT INTO settings (code,name,value) VALUES('smtp_password','SMTP server password','pass');
INSERT INTO settings (code,name,value) VALUES('smtp_username','SMTP server username','user');
INSERT INTO settings (code,name,value) VALUES('stripe_key','Stripe Key','put_key_here');
INSERT INTO settings (code,name,value) VALUES('stripe_secret','Stripe Secret Key','put_secret_key_here');
INSERT INTO settings (code,name,value) VALUES('stripe_token_id','Stripe Token Id','put_token_id_number_here');

-- tax

INSERT INTO tax (code,name,value) VALUES('driver_fee_value','Fee of driver in value',10.00);
INSERT INTO tax (code,name,value) VALUES('driver_fee_percent','Fee of driver in percent',5);
INSERT INTO tax (code,name,value) VALUES('client_fee_value','Fee of client in value',10.00);
INSERT INTO tax (code,name,value) VALUES('client_fee_percent','Fee of driver in percent',5);

-- triggers

-- Trigger on INSERT (now not using `<trigger_name>`)

DROP TRIGGER IF EXISTS product_circle_before_insert;
CREATE TRIGGER product_circle_before_insert BEFORE INSERT ON product_circle
FOR EACH ROW SET NEW.create_date = Now();
                
DROP TRIGGER IF EXISTS order_AB_before_insert;
CREATE TRIGGER order_AB_before_insert BEFORE INSERT ON order_AB
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS order_job_before_insert;
CREATE TRIGGER order_job_before_insert BEFORE INSERT ON order_job
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS delivery_before_insert;
CREATE TRIGGER delivery_before_insert BEFORE INSERT ON delivery
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS stock_invoice_before_insert;
CREATE TRIGGER stock_invoice_before_insert BEFORE INSERT ON stock_invoice
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS purchase_before_insert;
CREATE TRIGGER purchase_before_insert BEFORE INSERT ON purchase
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS discount_before_insert;
CREATE TRIGGER discount_before_insert BEFORE INSERT ON discount
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS payment_before_insert;
CREATE TRIGGER payment_before_insert BEFORE INSERT ON payment
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS user_before_insert;
CREATE TRIGGER user_before_insert BEFORE INSERT ON user
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS sensor_before_insert;
CREATE TRIGGER sensor_before_insert BEFORE INSERT ON sensor
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS sensor_circle_before_insert;
CREATE TRIGGER sensor_circle_before_insert BEFORE INSERT ON sensor_circle
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS sensor_group_before_insert;
CREATE TRIGGER sensor_group_before_insert BEFORE INSERT ON sensor_group
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS track_before_insert;
CREATE TRIGGER track_before_insert BEFORE INSERT ON track
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS message_before_insert;
CREATE TRIGGER message_before_insert BEFORE INSERT ON message
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS prepaid_card_before_insert;
CREATE TRIGGER prepaid_card_before_insert BEFORE INSERT ON prepaid_card
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS audit_before_insert;
CREATE TRIGGER audit_before_insert BEFORE INSERT ON audit
FOR EACH ROW SET NEW.create_date = Now();

DROP TRIGGER IF EXISTS project_before_insert;
CREATE TRIGGER project_before_insert BEFORE INSERT ON project
FOR EACH ROW SET NEW.create_date = Now();

--
