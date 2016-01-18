USE clfc2;

ALTER TABLE `loanapp_capture`
ADD COLUMN `dtfiled` DATETIME DEFAULT NULL;

ALTER TABLE `loanapp_capture`
ADD INDEX `ix_dtfiled`(`dtfiled`);

UPDATE loanapp_capture SET dtfiled = `dtreleased`
WHERE dtfiled IS NULL;

ALTER TABLE `field_collection_route`
ADD COLUMN `trackerid` VARCHAR(40);

ALTER TABLE `field_collection_route`
ADD INDEX `ix_trackerid`(`trackerid`);
