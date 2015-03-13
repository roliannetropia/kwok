---------------
-- Tape Domain Upgrade
-- BDO Unibank
-- ITO-DCO-PRC-Tools and Automation
---------------
-- create asset_tape table
CREATE TABLE asset_tape
(
	tape_id 					integer NOT NULL primary key,
	tape_name					character varying(100) not null,
	serial_number				character varying(50) unique not null,
	barcode_number				character varying(50) unique not null,
	manufacturer_company_id 	integer,
	vendor_company_id 			integer,
	media_type					integer,
	manufactured_date			timestamp(1) without time zone,
	location					integer,
	retention					integer,
	system						integer,
	status						integer,
	transaction_date			timestamp(1) without time zone,
	transaction_time			timestamp(1) without time zone,
	date_move					timestamp(1) without time zone,
	date_expire					timestamp(1) without time zone
);
---------------
-- create asset_tape_view
CREATE OR REPLACE VIEW asset_tape_view AS 
 SELECT at.tape_id, at.tape_name, at.serial_number, at.barcode_number, at.manufacturer_company_id, at.vendor_company_id, at.media_type, at.manufactured_date, at.location, at.retention, at.system, at.status, at.transaction_date, at.transaction_time, at.date_move, at.date_expire
   FROM asset_tape at;

ALTER TABLE asset_tape_view
  OWNER TO postgres;
---------------
-- create sp_tape_add function
CREATE OR REPLACE FUNCTION sp_tape_add(OUT o_tape_id integer, IN p_tape_name character varying, IN p_serial_number character varying, IN p_barcode_number character varying, IN p_manufacturer_company_id integer, IN p_vendor_company_id integer, IN p_media_type integer, IN p_tape_location integer, IN p_tape_retention integer, IN p_tape_system integer, IN p_tape_status integer, IN p_manufactured_date character varying, IN p_transaction_date character varying, IN p_date_move character varying, IN p_date_expire character varying, IN p_transaction_time character varying)
  RETURNS integer AS
$BODY$begin

select nextval('seq_asset_tape_id') into o_tape_id;

insert into asset_tape(tape_id, tape_name, serial_number, barcode_number, manufacturer_company_id, vendor_company_id, media_type, location, retention, system, status, manufactured_date, transaction_date, date_move, date_expire, transaction_time)
values (o_tape_id, p_tape_name, p_serial_number, p_barcode_number, p_manufacturer_company_id, p_vendor_company_id, p_media_type, p_tape_location, p_tape_retention, p_tape_system, p_tape_status, to_timestamp(p_manufactured_date, 'YYYY-MM-DD HH24:MI:SS'), to_timestamp(p_transaction_date, 'YYYY-MM-DD HH24:MI:SS'), to_timestamp(p_date_move, 'YYYY-MM-DD HH24:MI:SS'), to_timestamp(p_date_expire, 'YYYY-MM-DD HH24:MI:SS'), to_timestamp(p_transaction_time, 'YYYY-MM-DD HH24:MI:SS'));

end;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION sp_tape_add(character varying, character varying, character varying, integer, integer, integer, integer, integer, integer, integer, character varying, character varying, character varying, character varying, character varying)
  OWNER TO postgres;
----------------------
-- create seq_asset_tape_id sequence
CREATE SEQUENCE seq_asset_tape_id
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 31
  CACHE 1;
ALTER TABLE seq_asset_tape_id
  OWNER TO postgres;
----------------------
-- access_page
-- add corresponding page into access_page
insert into access_page (page_id, page_name, module_id) values (327, '/tape/index', 16);
insert into access_page (page_id, page_name, module_id) values (328, '/tape/add', 16);
insert into access_page (page_id, page_name, module_id) values (329, '/tape/add-2', 16);
insert into access_page (page_id, page_name, module_id) values (330, '/tape/detail', 16);
insert into access_page (page_id, page_name, module_id) values (331, '/tape/list', 16);
----------------------
-- access_perm_page_map
-- categorize page_id (327-331) to 33 -> tape_read, 34 -> tape_write
insert into access_perm_page_map (perm_id, page_id) values (33, 327);
insert into access_perm_page_map (perm_id, page_id) values (34, 328);
insert into access_perm_page_map (perm_id, page_id) values (34, 329);
insert into access_perm_page_map (perm_id, page_id) values (33, 330);
insert into access_perm_page_map (perm_id, page_id) values (33, 331);
----------------------
-- access_user_perm_map
-- make tape_read and tape_wite accessible to system admin
insert into access_user_perm_map (user_id, perm_id) values (1, 33);
insert into access_user_perm_map (user_id, perm_id) values (1, 34);
----------------------
-- access_permission 
-- add category tape read and tape write
insert into access_permission (perm_id, perm_name, perm_is_enabled, order_num) values (33, 'tape read', 1, 153);
insert into access_permission (perm_id, perm_name, perm_is_enabled, order_num) values (34, 'tape write', 1, 154);
----------------------
-- attribute_field
insert into attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_name) values (-14, -18, 'company_type_tape_vendor','company_type_tape_vendor');
insert into attribute_field (attribute_field_id, attribute_id, field_key, attribute_field_name) values (-13, -18, 'company_type_tape_manufacturer','company_type_tape_manufacturer');
----------------------
-- attribute
insert into attribute (attribute_id, object_type_id, attribute_key, is_editable, is_custom_attr, attribute_type, attribute_convert_url, is_required) values (-22, 16, 'media_type', 1, 0, 1, 0, 0);
insert into attribute (attribute_id, object_type_id, attribute_key, is_editable, is_custom_attr, attribute_type, attribute_convert_url, is_required) values (-23, 16, 'tape_location', 1, 0, 1, 0, 0);
insert into attribute (attribute_id, object_type_id, attribute_key, is_editable, is_custom_attr, attribute_type, attribute_convert_url, is_required) values (-24, 16, 'tape_retention', 1, 0, 1, 0, 0);
insert into attribute (attribute_id, object_type_id, attribute_key, is_editable, is_custom_attr, attribute_type, attribute_convert_url, is_required) values (-25, 16, 'system', 1, 0, 1, 0, 0);
insert into attribute (attribute_id, object_type_id, attribute_key, is_editable, is_custom_attr, attribute_type, attribute_convert_url, is_required) values (-26, 16, 'status', 1, 0, 1, 0, 0);
----------------------
-- system_config
insert into system_config (config_key, config_value) values ('Tape.CheckUniqueTapeName', 'false');
insert into system_config (config_key, config_value) values ('Tape.CheckUniqueTapeSerialNumber', 'false');
insert into system_config (config_key, config_value) values ('tape.columnList','tape_name,tape_serial_number,tape_barcode_number');
insert into system_config (config_key, config_value) values ('tape.numberOfRowsToShow', 10);
update system_config set config_value = '13,1,2,4,14,5,3,6,8,7,15,16' where config_key = 'template.moduleTabs';
----------------------
-- object_type
insert into object_type (object_type_id, object_key) values (16, 'tape');
----------------------

