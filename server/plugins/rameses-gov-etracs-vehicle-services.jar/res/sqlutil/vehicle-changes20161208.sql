vehicle_application ( txnmode VARCHAR(50), billexpirydate DATE`vehicle_tricycle` )

vehicle_tricycle ( chassisno VARCHAR(50),  sidecarcolor VARCHAR(50) )

vehicle_franchise ( cluster VARCHAR(50), dtregistered DATE )

ALTER TABLE vehicle_tricycle ADD COLUMN crname VARCHAR(255) NULL;