
CREATE TABLE bldgrpu_land (
  objid varchar(50) NOT NULL,
  bldgrpuid varchar(50) NOT NULL,
  landfaas_objid varchar(50) NOT NULL 
) 
GO 

ALTER TABLE bldgrpu_land ADD PRIMARY KEY  (objid)
GO 
CREATE UNIQUE INDEX ux_bldgrpu_land_bldgrpu_landfaas ON bldgrpu_land (bldgrpuid,landfaas_objid)
GO 
CREATE INDEX ix_bldgrpu_land_bldgrpuid ON bldgrpu_land (bldgrpuid)
GO 
CREATE INDEX ix_bldgrpu_land_landfaasid ON bldgrpu_land (landfaas_objid)
GO 
ALTER TABLE bldgrpu_land 
    ADD CONSTRAINT FK_bldgrpu_land_bldgrpu 
    FOREIGN KEY (bldgrpuid) REFERENCES bldgrpu (objid)
GO 
ALTER TABLE bldgrpu_land 
    ADD CONSTRAINT FK_bldgrpu_land_landfaas 
    FOREIGN KEY (landfaas_objid) REFERENCES faas (objid) 
GO 
