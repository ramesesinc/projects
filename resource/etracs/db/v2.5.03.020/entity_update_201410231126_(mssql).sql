
ALTER TABLE entity ALTER COLUMN entityname varchar(255);
CREATE INDEX ix_entityname ON entity (entityname); 

ALTER TABLE entityindividual ALTER COLUMN lastname VARCHAR(50);
ALTER TABLE entityindividual ALTER COLUMN firstname VARCHAR(50); 
ALTER TABLE entityindividual ALTER COLUMN middlename VARCHAR(50);
CREATE INDEX ix_lfname ON entityindividual (lastname, firstname); 
