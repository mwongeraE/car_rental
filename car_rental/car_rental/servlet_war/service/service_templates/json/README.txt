ABTO3BIT 2015-2017
fireArt json specification
-------------------------------------------------------------------------------------------
fireArt Database users:
fa_admin, administartor with all privileges to objects of fireart schema (top secret as root)
fa_client, user with select privileges to objects of fireart schema
virtual fireArt user's collects in user table
-------------------------------------------------------------------------------------------
Request param:
(exclusive parameters str0-name str3-database str4-type str5-driver)
str1 - username
str2 - password (all str param by position identical for <skyDrakkar> specification)
param0 - virtual username
param1 - virtual password(hash code)
param2-param6 reserved to sepecific values
(param2-language param3-map_provider param4-timezone_offset)
param6 - user_type reserved
language,map_provider and other param send as coookie values
param 20,21 for data or error message return in json

Field    Description
-------------------------------------------------------------------------------------------
<String> string data
<Number> number value
<Buffer> BASE64 string buffer data

return json:
{"results":[{<data>}],"status":"SUCCESS"} 
OR 
{"results":[{"message":"<String>","database_message":"<String>"}],"session_id":%sessionid%sessionid,"status":"ERROR"}

example:

{"results":[{"user_type":"<Number>"}],"session_id":"<Number>","status":"SUCCESS"}
OR
{"results":[{"message":"Invalid username or password","message_code":-1}],"session_id":%sessionid%sessionid,"status":"ERROR"}
-------------------------------------------------------------------------------------------
not use BASE64 converting in get pictures modules (want to save BASE64 images in database)->REPLACE(REPLACE(TO_BASE64(pictures),'\r',''),'\n','')
%set_param = @this.getBytesValueAsBase64String("SELECT picture FROM table WHERE id=?","")%set_param
use
%set_param = @this.getStringValue(...)

-------------------------------------------------------------------------------------------
GET queris(POST for ajax identical):
% is a all values(%25 for browser string get query)
language is a string value (ENGLISH, RUSSIAN, UKRAINIAN)

-------------------------------------------------------------------------------------------

- get_user [return user list or user by id]
for admin user return all user list as for id=% or user by id, for not admin return current user allways.
url = /service/start?name=/json/get_user&user=<Username>&pass=<Password>&user_id=<Number>

example:
http://localhost/service/start?name=/json/get_user&user=george&pass=1234567890&user_id=%25

- get_user_type [1-admin 2-worker 3-client or customer]
url = /service/start?name=/json/get_user_type&user=<Username>&pass=<Password>

- get_user_picture [get user picture]
url = /service/start?name=/json/get_user_picture&user=<Username>&pass=<Password>&user_id=<Number>

- add_user [add a new user]
for admin user type may be 1..3, for non admin user type=3 allways.
url = /service/start?name=/json/add_user&user=<Username>&pass=<Password>
      &first_name=<String>&last_name=<String>&email=<string with @>&phone=<string with phone>&type=<Number>
      &new_user=<String>&new_pass=<String>

- update_user [update user data]
for admin user update every user, for non admin user update onle itself
url = /service/start?name=/json/update_user&user=<Username>&pass=<Password>&user_id=<Number>
      &first_name=<String>&last_name=<String>&email=<String with @>&phone=<String>

- update_user_password [change password for new]
change password every user for itself
url = /service/start?name=/json/update_user_password&user=<Username>&pass=<Password>&password_for_update=<String>

- update_user_discount [set a new user discount]
change password every user for itself
url = /service/start?name=/json/update_user_discount&user=<Username>&pass=<Password>&user_id=<Number>
      &discount_id=<Number>

update_user_picture [update user picture]
change password every user for itself
url = /service/start?name=/json/update_user_discount&user=<Username>&pass=<Password>&user_id=<Number>
      &picture=<Buffer>

- remove_user [remove user by id]
for admin user remove every id user, for not admin remove only itself.
url = /service/start?name=/json/remove_user&user=<Username>&pass=<Password>&user_id=<Number>

-------------------------------------------------------------------------------------------

- get_discount
url = /service/start?name=/json/get_discount&user=<Username>&pass=<Password>&discount_id=<Number>
      &product_id=<Number>&discount_name=<String>&language=<String>

-------------------------------------------------------------------------------------------

- get_manufacture
url = /service/start?name=/json/get_manufacture&user=<Username>&pass=<Password>&manufacture_id=<Number>
      &manufacture_name=<String>&description=<String>

-------------------------------------------------------------------------------------------
                                            ...
-------------------------------------------------------------------------------------------

GET
---
url = /service/start?name=/json/get_<module_name>&user=<Username>&pass=<Password>&<module_name>_id=<Number>&<other parameters>
      &offset=<Number>&rows=<Number>

(get use SELECT sql query for fetching LIMIT offset,rows as LIMIT 0,10)

ORDER BY id DESC for: order_AB, purchase, payment, message, track

other_parameters, first param allways(* - as LIKE for pictures = '<num>', not accept '%')

(get for allways)
-- manufacture (manufacture_id*,manufacture_name,description), manufacture_picture (manufacture_id)
-- manufacture_part (manufacture_id*,language)
-- product,maxrate_product (product_id*,manufacture_id,discount_id,product_code,product_name,description,price_from,price_to)
-- product_part_type (product_type_id*,manufacture_id,discount_id,product_code,product_name,description,price_from,price_to)
-- product_review (user_id*,product_id*)
-- product_circle (product_id*)
-- product_param (param_id*,param_name,param_value,language), product_param_picture(param_id), product_param_part (product_id), product_param_part_picture(product_id,param_id)
-- product_type (type_id*,type_name,description,language), product_type_part (product_id)
-- currency (currency_id)
-- language
-- color
-- discount (discount_id,product_id,discount_name,language)
-- delivery_type (type_id*,language)
-- order_status (status_id*,language)
-- tax (tax_id*,tax_name,language)
-- transport (transport_id*,transport_name,license_plate), transport_picture (transport_id*)
-- store (store_id*), store_part(store_id*,language)
-- stock (product_id*,stock_id)

(get user for itself)
-- user (user_id), user_picture (user_id)
-- order_X (not used)
-- order_AB (order_id,transport_id,status_id,order_address,delivery_address,create_date), order_AB_part (order_id*)

-- sensor (sensor_id,sensor_name,device_name,phone,name,create_date)
-- sensor_circle (sensor_id*)
-- sensor_group (group_id*,sensor_id,group_name,create_date), sensor_group_part (group_id*)
-- sensor_place (place_id*,sensor_id,place_name,language), sensor_place_picture (place_id*)
-- sensor_area (not used)
-- sensor_group_track (?), sensor_circle_track (?)
-- track_type (track_id*)
-- track (sensor_id*,sensor_time,create_date), max_track (sensor_id), max_circle_track (), track_part (track_id*), track_part_picture(track_part_id*)

-- purchase (purchase_id,invoice_code,invoice_date,order_id,order_date,delivery_id,delivery_date,payment_date,create_date)
-- payment (purchase_id*)
-- delivery (delivery_id*,location_address,create_date)

-- message (message_type*,create_date)

(get for admin)
-- stock_invoice (stock_invoice_id,invoice_code,invoice_date,supplier,phone,delivery_id,delivery_date,create_date), stock_invoice_part (stock_invoice_id,product_id)

ADD
---
url = /service/start?name=/json/add_<module_name>&user=<Username>&pass=<Password>&<other parameters>

(all pictures set in update)

(add for admin)
-- manufacture
-- manufacture_part
-- product
-- product_circle, product_review
-- product_param, product_param_part
-- product_type, product_type_part
-- purchase, purchase_part
-- payment
-- currency
-- language
-- discount
-- delivery_type
-- order_status
-- tax
-- store, store_part
-- stock
-- stock_invoice, stock_invoice_part

-- track_type

(add admin & worker)
-- transport
-- delivery

(add for allways)
-- user
-- order_X (not used)
-- order_AB, order_AB_part

-- sensor
-- sensor_circle
-- sensor_group, sensor_group_part
-- sensor_place
-- sensor_area (not used)

-- track, track_part

-- message

UPDATE
------
url = /service/start?name=/json/update_<module_name>&user=<Username>&pass=<Password>&<module_name>_id=<Number>&<other parameters>

(update for admin)
-- manufacture, manufacture_picture
-- product, product_discount, product_review
-- product_param, product_param_part_picture
-- product_type
-- purchase
-- currency
-- language_activity
-- discount
-- tax
-- store, store_picture
-- stock
-- stock_invoice, stock_invoice_part

(update admin & worker)
-- transport, transport_picture
-- delivery

(update for allways)
-- [user, user_picture, user_discount, user_password]
-- order_X (not used)
-- order_AB, order_AB_part

-- sensor, sensor_activity
-- sensor_group
-- sensor_place, sensor_place_picture
-- sensor_area (not used)

-- track_part, track_part_picture

REMOVE (clients remove user,order_AB(with part),purchase(with part) for itself)[X]
------
url = /service/start?name=/json/remove_<module_name>&user=<Username>&pass=<Password>&<module_name>_id=<Number>

example: /service/start?name=/json/remove_discount&user=george&pass=1234567890&discount_id=1

("id" is a <module_name>_id)

-- manufacture, manufacture_part (remove by id, remove all parts)
-- product, product_review (remove by id, remove all params_parts,reviews)
-- product_param, product_param_part (remove by id, remove all parts)
-- product_type, product_type_part (remove by id, remove all parts)
-- delivery (remove by id)
-- delivery_type (remove by id)
-- transport (remove by id)
-- order_status (remove by id)
-- order_X (not used)
-- order_AB, order_part (remove by id, remove all parts)
-- store (remove by id)
-- stock (remove by id, remove all invoices)
-- stock_invoice, stock_invoice_part (remove by id, remove all parts)
-- currency (remove by id)
-- language (not remove)
-- color (not remove)
-- discount (remove by id)
-- tax (remove by id)
-- purchase,payment (remove by id, remove payment by purchase_id, transaction_id)
-- user (remove by id)
-- sensor (remove by id, remove all circle, all groups, all group_parts, all places, all areas)
-- sensor_circle (remove by sensorA_id and sensorB_id)
-- sensor_group, sensor_group_part (remove by id, remove all parts)
-- sensor_place (remove by id)
-- sensor_area (not used)
-- track_type (remove by id)
-- track, track_part (remove by id, remove all parts)



-------------------------------------------------------
get data limitation: (for future version may be changed)

%if "param6=1"|"param6=2" %if

getOrderAB.., getPurchase, getSensor

%if "param6!=1&param6!=2" %if

getOrderABPart

%if "param6!=1" %if

getTrack..(is_my_sensor_or_circle), getDelivery(is_my_delivery), getPayment(is_my_purchase)

-------------------------------------------------------
get order data

get_order                           [list of orders, for client if owner, for driver if status_id==1]
get_order_near (get_order_near2)    [two ways to calc distance from order pickup]
get_order_transport                 [orders of driver by transport_id, no additional params]
get_max_order                       [last order for user, no additional params]
