UPDATE vehicle_franchise SET vehicleunitid = NULL;
DELETE FROM vehicle_application_unit;
DELETE FROM vehicle_application_fee;
UPDATE vehicle_application SET taskid = NULL,primaryappid=NULL;
DELETE FROm vehicle_application_task;
DELETE FROm vehicle_application_info;
DELETE FROM vehicle_application;