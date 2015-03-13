---------------
-- asset_tape
alter table asset_tape drop column tape_name cascade;

---------------
-- asset_tape_view
DROP VIEW asset_tape_view;

CREATE OR REPLACE VIEW asset_tape_view AS 
 SELECT at.tape_id, at.serial_number, at.barcode_number, at.manufacturer_company_id, at.vendor_company_id, at.media_type, at.manufactured_date, at.location, at.retention, at.system, at.status, at.transaction_date, at.transaction_time, at.date_move, at.date_expire
   FROM asset_tape at;

ALTER TABLE asset_tape_view
  OWNER TO postgres;
  
---------------
-- sp_tape_add
-- Function: sp_tape_add(character varying, character varying, character varying, integer, integer, integer, integer, integer, integer, integer, character varying, character varying, character varying, character varying, character varying)

DROP FUNCTION sp_tape_add(character varying, character varying, character varying, integer, integer, integer, integer, integer, integer, integer, character varying, character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION sp_tape_add(OUT o_tape_id integer, IN p_serial_number character varying, IN p_barcode_number character varying, IN p_manufacturer_company_id integer, IN p_vendor_company_id integer, IN p_media_type integer, IN p_tape_location integer, IN p_tape_retention integer, IN p_tape_system integer, IN p_tape_status integer, IN p_manufactured_date character varying, IN p_transaction_date character varying, IN p_date_move character varying, IN p_date_expire character varying, IN p_transaction_time character varying)
  RETURNS integer AS
$BODY$begin

select nextval('seq_asset_tape_id') into o_tape_id;

insert into asset_tape(tape_id, serial_number, barcode_number, manufacturer_company_id, vendor_company_id, media_type, location, retention, system, status, manufactured_date, transaction_date, date_move, date_expire, transaction_time)
values (o_tape_id, p_serial_number, p_barcode_number, p_manufacturer_company_id, p_vendor_company_id, p_media_type, p_tape_location, p_tape_retention, p_tape_system, p_tape_status, to_timestamp(p_manufactured_date, 'YYYY-MM-DD HH24:MI:SS'), to_timestamp(p_transaction_date, 'YYYY-MM-DD HH24:MI:SS'), to_timestamp(p_date_move, 'YYYY-MM-DD HH24:MI:SS'), to_timestamp(p_date_expire, 'YYYY-MM-DD HH24:MI:SS'), to_timestamp(p_transaction_time, 'YYYY-MM-DD HH24:MI:SS'));

end;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION sp_tape_add(character varying, character varying, integer, integer, integer, integer, integer, integer, integer, character varying, character varying, character varying, character varying, character varying)
  OWNER TO postgres;


---------------
-- access_page
insert into access_page (page_id, page_name, module_id) values (332,'/tape/search',16);

---------------
-- access_perm_page_map
insert into access_perm_page_map (perm_id, page_id) values (33,332);

---------------
-- system_config
insert into system_config (config_key, config_value) values ('Tape.CheckUniqueTapeBarcodeNumber','false');