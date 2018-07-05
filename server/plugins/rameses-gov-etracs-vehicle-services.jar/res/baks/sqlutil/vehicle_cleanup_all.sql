#RUN REPLACE ${vtype} before running this script
DELETE FROM vehicle_payment_item WHERE apprefid IN ( SELECT objid FROM vehicle_application WHERE vehicletype='${vtype}' );
DELETE FROM vehicle_payment WHERE appid IN (SELECT objid FROM vehicle_application WHERE vehicletype='${vtype}' );
DELETE FROM vehicle_application_fee WHERE appid IN ( SELECT objid FROM vehicle_application WHERE vehicletype='${vtype}' );
DELETE FROM vehicle_application_${vtype}_task;
DELETE FROM vehicle_application WHERE vehicletype='${vtype}' ;

UPDATE vehicle_franchise SET vehicleid=NULL WHERE vehicleid IN (SELECT objid FROM vehicle_tricycle );

DELETE FROM vehicle_franchise WHERE vehicleid IN (SELECT objid FROM vehicle_tricycle );

DELETE FROM vehicle_payment_item WHERE franchiserefid IN ( SELECT objid FROM vehicle_${vtype} );
DELETE FROM vehicle_payment WHERE vehicleid IN (SELECT objid FROM vehicle_${vtype} );

DELETE FROM vehicle_franchise_fee WHERE parentid IN ( SELECT objid FROM vehicle_franchise WHERE vehicletype='${vtype}' );

DELETE FROM vehicle_${vtype};
DELETE FROM vehicle_franchise WHERE vehicletype='${vtype}';
UPDATE vehicletype SET issued = 0 WHERE objid='${vtype}';



