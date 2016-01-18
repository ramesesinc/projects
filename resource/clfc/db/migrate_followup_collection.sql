USE clfc2;

INSERT INTO `clfc2`.`followupcollection`
            (`objid`)
SELECT objid FROM followup_collection;