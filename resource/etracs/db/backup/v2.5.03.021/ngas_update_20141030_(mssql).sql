
DROP TABLE [dbo].[ngas_revenue_remittance]
GO
DROP TABLE [dbo].[ngas_revenue_deposit]
GO
DROP TABLE [dbo].[ngas_revenueitem]
GO
DROP TABLE [dbo].[ngas_revenue]
GO


CREATE TABLE [dbo].[ngasaccount] (
	[objid] varchar(50)  NOT NULL ,
	[parentid] varchar(50)  NULL ,
	[state] varchar(10)  NULL ,
	[chartid] varchar(50)  NULL ,
	[code] varchar(50)  NULL ,
	[title] varchar(255)  NULL ,
	[type] varchar(20)  NULL ,
	[acctgroup] varchar(50)  NULL ,
	[target] decimal(12,2) NULL ,
	CONSTRAINT [PK__ngasacco__530D6FE47FCBCD04] PRIMARY KEY ([objid])
) 
GO

CREATE TABLE [dbo].[ngas_revenue_mapping] (
	[objid] varchar(50) NOT NULL ,
	[version] varchar(10) NULL ,
	[revenueitemid] varchar(50) NULL ,
	[acctid] varchar(50) NOT NULL ,
	CONSTRAINT [PK__ngas_rev__530D6FE4039C5DE8] PRIMARY KEY ([objid])
) 
GO

DROP INDEX [uix_revenueitem_ngasacctid] ON [dbo].[ngas_revenue_mapping]
GO
CREATE UNIQUE INDEX [uix_version_revenueitemid] ON [dbo].[ngas_revenue_mapping] ([version], [revenueitemid]) 
GO
CREATE INDEX [fk_revenue_mapping_ngasaccount] ON [dbo].[ngas_revenue_mapping] ([acctid]) 
GO
CREATE INDEX [fk_revenue_mapping_revenueitemid] ON [dbo].[ngas_revenue_mapping] ([revenueitemid]) 
GO


CREATE TABLE [dbo].[sreaccount] (
	[objid] varchar(50) NOT NULL ,
	[parentid] varchar(50) NULL ,
	[state] varchar(10) NULL ,
	[chartid] varchar(50) NULL ,
	[code] varchar(50) NULL ,
	[title] varchar(255) NULL ,
	[type] varchar(20) NULL ,
	[acctgroup] varchar(50) NULL ,
	CONSTRAINT [PK__sreaccou__530D6FE40C31A3E9] PRIMARY KEY ([objid])
) 
GO

CREATE TABLE [dbo].[sreaccount_incometarget] (
	[objid] varchar(50) NOT NULL ,
	[year] int NOT NULL ,
	[target] decimal(18,2) NULL ,
	CONSTRAINT [PK__sreaccou__EB04CDDC0F0E1094] PRIMARY KEY ([objid], [year])
)
GO

CREATE TABLE [dbo].[sre_revenue_mapping] (
	[objid] varchar(50) NOT NULL ,
	[version] varchar(10) NULL ,
	[revenueitemid] varchar(50) NOT NULL ,
	[acctid] varchar(50) NOT NULL ,  
	CONSTRAINT [PK__sre_reve__530D6FE412DEA178] PRIMARY KEY ([objid]) 
) 
GO

CREATE UNIQUE INDEX [uix_version_revenueitemid] ON [dbo].[sre_revenue_mapping] ([version],[revenueitemid]) 
GO

ALTER TABLE [dbo].[ngas_revenue_mapping] 
	ADD CONSTRAINT [fk_revenue_mapping_ngasaccount] 
	FOREIGN KEY ([acctid]) REFERENCES [dbo].[ngasaccount] ([objid]) 
GO
ALTER TABLE [dbo].[ngas_revenue_mapping] 
	ADD CONSTRAINT [fk_revenue_mapping_revenueitemid] 
	FOREIGN KEY ([revenueitemid]) REFERENCES [dbo].[revenueitem] ([objid]) 
GO 


INSERT INTO [dbo].[ngasaccount] ( 
	[objid], [parentid], [state], [chartid], [code], 
	[title], [type], [acctgroup], [target] 
) 
SELECT 
	[objid], [parentid], [state], [chartid], [code], 
	[title], [type], [acctgroup], [target] 
FROM [dbo].[account] 
GO

INSERT INTO [dbo].[ngas_revenue_mapping] (
	[objid], [version], [revenueitemid], [acctid] 
) 
SELECT 
	[objid], 'standard', [revitemid], [account_objid]  
FROM 
	[dbo].[revenueitem_attribute]
WHERE 
	[attribute_objid] IN ( 'ngassubaccount')  
GO 

INSERT INTO [dbo].[ngas_revenue_mapping] (
	[objid], [version], [revenueitemid], [acctid] 
)
SELECT 
	[objid], 'standard', [revitemid], [account_objid] 
FROM 
	[dbo].[revenueitem_attribute] 
WHERE 
	[attribute_objid] IN ( 'ngasstandard') 
GO


INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-004', NULL, 'SYSTEM', 'REV2004-08', '004', 'LOCAL SOURCES', 'group', 'REVENUE');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-005', 'REV2004-08-004', 'SYSTEM', 'REV2004-08', '05', 'TAX REVENUES (6+22+39)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-006', 'REV2004-08-005', 'SYSTEM', 'REV2004-08', '06', 'REAL PROPERTY TAX (7+12+17)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-007', 'REV2004-08-006', 'SYSTEM', 'REV2004-08', '07', 'REAL PROPERTY TAX- BASIC (8+9+10+11)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-008', 'REV2004-08-007', 'SYSTEM', 'REV2004-08', '08', 'CURRENT YEAR', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-009', 'REV2004-08-007', 'SYSTEM', 'REV2004-08', '09', 'FINES AND PENALTIES- CURRENT YEAR', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-010', 'REV2004-08-007', 'SYSTEM', 'REV2004-08', '10', 'PRIOR YEAR/S', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-011', 'REV2004-08-007', 'SYSTEM', 'REV2004-08', '11', 'FINES AND PENALTIES- PRIOR YEAR/S', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-012', 'REV2004-08-006', 'SYSTEM', 'REV2004-08', '12', 'SPECIAL LEVY ON IDLE LANDS (13+14+15+16)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-013', 'REV2004-08-012', 'SYSTEM', 'REV2004-08', '13', 'CURRENT YEAR', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-014', 'REV2004-08-012', 'SYSTEM', 'REV2004-08', '14', 'FINES AND PENALTIES- CURRENT YEAR', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-015', 'REV2004-08-012', 'SYSTEM', 'REV2004-08', '15', 'PRIOR YEAR/S', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-016', 'REV2004-08-012', 'SYSTEM', 'REV2004-08', '16', 'FINES AND PENALTIES- PRIOR YEAR/S', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-017', 'REV2004-08-006', 'SYSTEM', 'REV2004-08', '17', 'SPECIAL LEVY ON LAND BENEFITED BY PUBLIC WORKS PROJECTS (18+19+20+21)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-018', 'REV2004-08-017', 'SYSTEM', 'REV2004-08', '18', 'CURRENT YEAR', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-019', 'REV2004-08-017', 'SYSTEM', 'REV2004-08', '19', 'FINES AND PENALTIES- CURRENT YEAR', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-020', 'REV2004-08-017', 'SYSTEM', 'REV2004-08', '20', 'PRIOR YEAR/S', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-021', 'REV2004-08-017', 'SYSTEM', 'REV2004-08', '21', 'FINES AND PENALTIES- PRIOR YEAR/S', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-022', 'REV2004-08-005', 'SYSTEM', 'REV2004-08', '22', 'TAX ON BUSINESS (TOTAL OF LINE 23+24+35 TO 38)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-023', 'REV2004-08-022', 'SYSTEM', 'REV2004-08', '23', 'AMUSEMENT TAX', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-024', 'REV2004-08-022', 'SYSTEM', 'REV2004-08', '24', 'BUSINESS TAX (25 TO 33)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-025', 'REV2004-08-024', 'SYSTEM', 'REV2004-08', '25', 'MANUFACTURERS, ASSEMBLERS, ETC.', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-026', 'REV2004-08-024', 'SYSTEM', 'REV2004-08', '26', 'WHOLESALERS, DISTRIBUTORS, ETC.', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-027', 'REV2004-08-024', 'SYSTEM', 'REV2004-08', '27', 'EXPORTERS, MANUFACTURERS, DEALERS, ETC.', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-028', 'REV2004-08-024', 'SYSTEM', 'REV2004-08', '28', 'RETAILERS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-029', 'REV2004-08-024', 'SYSTEM', 'REV2004-08', '29', 'CONTRACTORS AND OTHER INDEPENDENT CONTRACTORS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-030', 'REV2004-08-024', 'SYSTEM', 'REV2004-08', '30', 'BANKS & OTHER FINANCIAL INSTITUTIONS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-031', 'REV2004-08-024', 'SYSTEM', 'REV2004-08', '31', 'PEDDLERS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-032', 'REV2004-08-024', 'SYSTEM', 'REV2004-08', '32', 'PRINTING & PUBLICATION TAX', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-033', 'REV2004-08-024', 'SYSTEM', 'REV2004-08', '33', 'TAX ON AMUSEMENT PLACE', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-034', 'REV2004-08-024', 'SYSTEM', 'REV2004-08', '34', 'OTHER BUSINESS TAXES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-035', 'REV2004-08-022', 'SYSTEM', 'REV2004-08', '35', 'FRANCHISE TAX', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-036', 'REV2004-08-022', 'SYSTEM', 'REV2004-08', '36', 'TAX ON DELIVERY TRUCKS AND VANS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-037', 'REV2004-08-022', 'SYSTEM', 'REV2004-08', '37', 'TAX ON SAND,GRAVEL & OTHER QUARRY RESOURCES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-038', 'REV2004-08-022', 'SYSTEM', 'REV2004-08', '38', 'FINES AND PENALTIES-BUSINESS TAXES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-039', 'REV2004-08-005', 'SYSTEM', 'REV2004-08', '39', 'OTHER TAXES (TOTAL OF LINE 40 TO 45)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-040', 'REV2004-08-039', 'SYSTEM', 'REV2004-08', '40', 'COMMUNITY TAX- CORPORATION', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-041', 'REV2004-08-039', 'SYSTEM', 'REV2004-08', '41', 'COMMUNITY TAX- INDIVIDUAL', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-042', 'REV2004-08-039', 'SYSTEM', 'REV2004-08', '42', 'PROFESSIONAL TAX', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-043', 'REV2004-08-039', 'SYSTEM', 'REV2004-08', '43', 'REAL PROPERTY TRANSFER TAX', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-044', 'REV2004-08-039', 'SYSTEM', 'REV2004-08', '44', 'OTHER TAXES ', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-045', 'REV2004-08-039', 'SYSTEM', 'REV2004-08', '45', 'FINES AND PENALTIES- OTHER TAXES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-046', 'REV2004-08-004', 'SYSTEM', 'REV2004-08', '46', 'NON-TAX REVENUES (47+63+81+99)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-047', 'REV2004-08-046', 'SYSTEM', 'REV2004-08', '47', 'REGULATORY FEES (PERMIT AND LICENSES) (48+58+61+62)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-048', 'REV2004-08-047', 'SYSTEM', 'REV2004-08', '48', 'PERMIT AND LICENSES (49 TO 57) ', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-049', 'REV2004-08-048', 'SYSTEM', 'REV2004-08', '49', 'FEES ON WEIGHTS AND MEASURES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-050', 'REV2004-08-048', 'SYSTEM', 'REV2004-08', '50', 'FISHERY RENTAL FEES AND PRIVILEGE FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-051', 'REV2004-08-048', 'SYSTEM', 'REV2004-08', '51', 'FRANCHISING AND LICENSING FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-052', 'REV2004-08-048', 'SYSTEM', 'REV2004-08', '52', 'BUSINESS PERMIT FEES ', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-053', 'REV2004-08-048', 'SYSTEM', 'REV2004-08', '53', 'BUILDING PERMIT FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-054', 'REV2004-08-048', 'SYSTEM', 'REV2004-08', '54', 'ZONAL/LOCATION PERMIT FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-055', 'REV2004-08-048', 'SYSTEM', 'REV2004-08', '55', 'TRICYCLE OPERATORS PERMIT FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-056', 'REV2004-08-048', 'SYSTEM', 'REV2004-08', '56', 'OCCUPATIONAL FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-057', 'REV2004-08-048', 'SYSTEM', 'REV2004-08', '57', 'OTHER PERMIT & LICENSES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-058', 'REV2004-08-047', 'SYSTEM', 'REV2004-08', '58', 'REGISTRATION FEES (59+60)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-059', 'REV2004-08-058', 'SYSTEM', 'REV2004-08', '59', 'CATTLE/ANIMAL REGISTRATION FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-060', 'REV2004-08-058', 'SYSTEM', 'REV2004-08', '60', 'CIVIL REGISTRATION FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-061', 'REV2004-08-047', 'SYSTEM', 'REV2004-08', '61', 'INSPECTION FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-062', 'REV2004-08-047', 'SYSTEM', 'REV2004-08', '62', 'FINES AND PENALTIES-PERMITS AND LICENSES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-063', 'REV2004-08-046', 'SYSTEM', 'REV2004-08', '63', 'SERVICE/USER CHARGES (SERVICE INCOME) (64+69+74 TO 80)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-064', 'REV2004-08-063', 'SYSTEM', 'REV2004-08', '64', 'CLEARANCE AND CERTIFICATION FEES (65 TO 68)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-065', 'REV2004-08-064', 'SYSTEM', 'REV2004-08', '65', 'POLICE CLEARANCE', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-066', 'REV2004-08-064', 'SYSTEM', 'REV2004-08', '66', 'SECRETARY''S FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-067', 'REV2004-08-064', 'SYSTEM', 'REV2004-08', '67', 'HEALTH CERTIFICATE', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-068', 'REV2004-08-064', 'SYSTEM', 'REV2004-08', '68', 'OTHER CLEARANCE AND CERTIFICATION', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-069', 'REV2004-08-063', 'SYSTEM', 'REV2004-08', '69', 'OTHER FEES (70 TO 73)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-070', 'REV2004-08-069', 'SYSTEM', 'REV2004-08', '70', 'GARBAGE FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-071', 'REV2004-08-069', 'SYSTEM', 'REV2004-08', '71', 'WHARFAGE FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-072', 'REV2004-08-069', 'SYSTEM', 'REV2004-08', '72', 'TOLL FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-073', 'REV2004-08-069', 'SYSTEM', 'REV2004-08', '73', 'OTHER SERVICE INCOME', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-074', 'REV2004-08-063', 'SYSTEM', 'REV2004-08', '74', 'FINES AND PENALTIES- SERVICE INCOME', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-075', 'REV2004-08-063', 'SYSTEM', 'REV2004-08', '75', 'LANDING AND AERONAUTICAL FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-076', 'REV2004-08-063', 'SYSTEM', 'REV2004-08', '76', 'PARKING AND TERMINAL FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-077', 'REV2004-08-063', 'SYSTEM', 'REV2004-08', '77', 'HOSPITAL FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-078', 'REV2004-08-063', 'SYSTEM', 'REV2004-08', '78', 'MEDICAL, DENTAL AND LABORATORY FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-079', 'REV2004-08-063', 'SYSTEM', 'REV2004-08', '79', 'MARKET & SLAUGHTERHOUSE FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-080', 'REV2004-08-063', 'SYSTEM', 'REV2004-08', '80', 'PRINTING AND PUBLICATION FEES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-081', 'REV2004-08-046', 'SYSTEM', 'REV2004-08', '81', 'INCOME FROM ECONOMIC ENTERPRISE (BUSINESS INCOME) (82)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-082', 'REV2004-08-081', 'SYSTEM', 'REV2004-08', '82', 'INCOME FROM ECONOMIC ENTERPRISES (83 TO 98)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-083', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '83', 'SCHOOL OPERATIONS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-084', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '84', 'POWER GENERATION/ DISTRIBUTION', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-085', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '85', 'HOSPITAL OPERATIONS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-086', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '86', 'CANTEEN/RESTAURANT OPERATIONS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-087', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '87', 'CEMETERY OPERATIONS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-088', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '88', 'COMMUNICATION FACILITIES & EQUIPMENT OPERATIONS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-089', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '89', 'DORMITORY OPERATIONS ', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-090', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '90', 'MARKET OPERATIONS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-091', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '91', 'SLAUGHTERHOUSE OPERATIONS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-092', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '92', 'TRANSPORTATION SYSTEM OPERATIONS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-093', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '93', 'WATERWORKS SYSTEM OPERATIONS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-094', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '94', 'PRINTING & PUBLICATION OPERATIONS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-095', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '95', 'INCOME FROM LEASE/RENTAL OF FACILITIES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-096', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '96', 'INCOME FROM TRADING BUSINESS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-097', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '97', 'OTHER ECONOMIC ENTERPRISES ', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-098', 'REV2004-08-082', 'SYSTEM', 'REV2004-08', '98', 'FINES AND PENALTIES- ECONOMIC ENTERPRISES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-099', 'REV2004-08-046', 'SYSTEM', 'REV2004-08', '99', 'OTHER RECEIPTS (OTHER GENERAL INCOME) (100 TO 102)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-100', 'REV2004-08-099', 'SYSTEM', 'REV2004-08', '100', 'INTEREST INCOME', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-101', 'REV2004-08-099', 'SYSTEM', 'REV2004-08', '101', 'DIVIDEND INCOME', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-102', 'REV2004-08-099', 'SYSTEM', 'REV2004-08', '102', 'OTHER GENERAL INCOME (MISCELLANEOUS) (103 TO 105)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-103', 'REV2004-08-102', 'SYSTEM', 'REV2004-08', '103', 'REBATES ON MMDA CONTRIBUTION', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-104', 'REV2004-08-102', 'SYSTEM', 'REV2004-08', '104', 'SALES OF CONFISCATED/ABANDONED/SEIZED GOODS & PROPERTIES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-105', 'REV2004-08-102', 'SYSTEM', 'REV2004-08', '105', 'MISCELLANEOUS - OTHERS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-107', NULL, 'SYSTEM', 'REV2004-08', '107', 'EXTERNAL SOURCES', 'group', 'REVENUE');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-108', 'REV2004-08-107', 'SYSTEM', 'REV2004-08', '108', 'SHARE FROM NATIONAL TAX COLLECTION (109+112)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-109', 'REV2004-08-108', 'SYSTEM', 'REV2004-08', '109', 'INTERNAL REVENUE ALLOTMENT (110+111)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-110', 'REV2004-08-109', 'SYSTEM', 'REV2004-08', '110', 'CURRENT YEAR', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-111', 'REV2004-08-109', 'SYSTEM', 'REV2004-08', '111', 'PRIOR YEAR ', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-112', 'REV2004-08-107', 'SYSTEM', 'REV2004-08', '112', 'OTHER SHARES FROM NATIONAL TAX COLLECTION (113 TO 117)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-113', 'REV2004-08-112', 'SYSTEM', 'REV2004-08', '113', 'SHARE FROM ECONOMIC ZONE (RA 7227)', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-114', 'REV2004-08-112', 'SYSTEM', 'REV2004-08', '114', 'SHARE FROM EVAT', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-115', 'REV2004-08-112', 'SYSTEM', 'REV2004-08', '115', 'SHARE FROM NATIONAL WEALTH ', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-116', 'REV2004-08-112', 'SYSTEM', 'REV2004-08', '116', 'SHARE FROM PAGCOR/PCSO/LOTTO', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-117', 'REV2004-08-112', 'SYSTEM', 'REV2004-08', '117', 'SHARE FROM TOBACCO EXCISE TAX', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-118', 'REV2004-08-107', 'SYSTEM', 'REV2004-08', '118', 'EXTRAORDINARY RECEIPTS/GRANTS/DONATIONS/AIDS (119+122+125)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-119', 'REV2004-08-118', 'SYSTEM', 'REV2004-08', '119', 'GRANTS AND DONATIONS (120+121)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-120', 'REV2004-08-119', 'SYSTEM', 'REV2004-08', '120', 'DOMESTIC', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-121', 'REV2004-08-119', 'SYSTEM', 'REV2004-08', '121', 'FOREIGN', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-122', 'REV2004-08-118', 'SYSTEM', 'REV2004-08', '122', 'SUBSIDY INCOME (123+124)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-123', 'REV2004-08-122', 'SYSTEM', 'REV2004-08', '123', 'OTHER SUBSIDY INCOME', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-124', 'REV2004-08-122', 'SYSTEM', 'REV2004-08', '124', 'SUBSIDY FROM GOCCs', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-125', 'REV2004-08-118', 'SYSTEM', 'REV2004-08', '125', 'EXTRAORDINARY GAINS AND PREMIUMS (126 TO 129)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-126', 'REV2004-08-125', 'SYSTEM', 'REV2004-08', '126', 'GAIN ON FOREX', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-127', 'REV2004-08-125', 'SYSTEM', 'REV2004-08', '127', 'GAIN ON SALE OF ASSETS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-128', 'REV2004-08-125', 'SYSTEM', 'REV2004-08', '128', 'PREMIUM ON BONDS PAYABLE', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-129', 'REV2004-08-125', 'SYSTEM', 'REV2004-08', '129', 'GAIN ON SALE OF INVESTMENTS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-130', 'REV2004-08-107', 'SYSTEM', 'REV2004-08', '130', 'INTER-LOCAL TRANSFERS (131 +132)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-131', 'REV2004-08-130', 'SYSTEM', 'REV2004-08', '131', 'SUBSIDY FROM LGUs', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-132', 'REV2004-08-130', 'SYSTEM', 'REV2004-08', '132', 'SUBSIDY FROM OTHER FUNDS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-133', 'REV2004-08-107', 'SYSTEM', 'REV2004-08', '133', 'CAPITAL/ INVESTMENT RECEIPTS (134 TO 136)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-134', 'REV2004-08-133', 'SYSTEM', 'REV2004-08', '134', 'PROCEEDS FROM SALE OF ASSETS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-135', 'REV2004-08-133', 'SYSTEM', 'REV2004-08', '135', 'PROCEEDS FROM SALE OF DEBT SECURITIES OF OTHER ENTITIES', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-136', 'REV2004-08-133', 'SYSTEM', 'REV2004-08', '136', 'COLLECTION OF LOANS RECEIVABLES (PRINCIPAL)', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-137', 'REV2004-08-107', 'SYSTEM', 'REV2004-08', '137', 'RECEIPTS FROM LOANS AND BORROWINGS (PAYABLE) (138 TO140)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-138', 'REV2004-08-137', 'SYSTEM', 'REV2004-08', '138', 'LOANS- FOREIGN ', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-139', 'REV2004-08-137', 'SYSTEM', 'REV2004-08', '139', 'LOANS- DOMESTIC', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-140', 'REV2004-08-137', 'SYSTEM', 'REV2004-08', '140', 'BONDS FLOTATION', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-143', NULL, 'SYSTEM', 'REV2004-08', '143', 'SPECIAL EDUCATION FUND', 'group', 'REVENUE');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-144', 'REV2004-08-143', 'SYSTEM', 'REV2004-08', '144', 'SPECIAL EDUCATION TAX (145 TO 148)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-145', 'REV2004-08-144', 'SYSTEM', 'REV2004-08', '145', 'CURRENT YEAR', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-146', 'REV2004-08-144', 'SYSTEM', 'REV2004-08', '146', 'FINES AND PENALTIES-CURRENT YEAR', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-147', 'REV2004-08-144', 'SYSTEM', 'REV2004-08', '147', 'PRIOR YEAR/S', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-148', 'REV2004-08-144', 'SYSTEM', 'REV2004-08', '148', 'FINES AND PENALTIES-PRIOR YEAR/S', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-149', 'REV2004-08-143', 'SYSTEM', 'REV2004-08', '149', 'DONATION/GRANTS/AID(150)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-150', 'REV2004-08-149', 'SYSTEM', 'REV2004-08', '150', 'GRANTS AND DONATIONS (151+152)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-151', 'REV2004-08-150', 'SYSTEM', 'REV2004-08', '151', 'FOREIGN ', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-152', 'REV2004-08-150', 'SYSTEM', 'REV2004-08', '152', 'DOMESTIC', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-153', 'REV2004-08-143', 'SYSTEM', 'REV2004-08', '153', 'OTHER RECEIPTS (154+155)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-154', 'REV2004-08-153', 'SYSTEM', 'REV2004-08', '154', 'INTEREST INCOME', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-155', 'REV2004-08-153', 'SYSTEM', 'REV2004-08', '155', 'OTHER BUSINESS INCOME (MISCELLANEOUS)', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-156', 'REV2004-08-143', 'SYSTEM', 'REV2004-08', '156', 'OTHER SUBSIDY INCOME', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-157', NULL, 'SYSTEM', 'REV2004-08', '157', 'INTER-LOCAL TRANSFERS', 'group', NULL);
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-158', 'REV2004-08-157', 'SYSTEM', 'REV2004-08', '158', 'SUBSIDY FROM LGUs', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-159', 'REV2004-08-157', 'SYSTEM', 'REV2004-08', '159', 'SUBSIDY FROM OTHER FUNDS', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-160', NULL, 'SYSTEM', 'REV2004-08', '160', 'RECEIPTS FROM LOANS AND BORROWINGS (PAYABLE) (161+162)', 'group', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-161', 'REV2004-08-160', 'SYSTEM', 'REV2004-08', '161', 'LOANS-FOREIGN', 'detail', '');
INSERT INTO [dbo].[sreaccount] ([objid], [parentid], [state], [chartid], [code], [title], [type], [acctgroup]) VALUES ('REV2004-08-162', 'REV2004-08-160', 'SYSTEM', 'REV2004-08', '162', 'LAONS-DOMESTIC', 'detail', '');


INSERT INTO [dbo].[sre_revenue_mapping] (
	[objid], [version], [revenueitemid], [acctid]  
) 
SELECT 
	[objid], 'standard', [revitemid], [account_objid] 
FROM 
	[dbo].[revenueitem_attribute] 
WHERE 
	[attribute_objid] IN ('sresubaccount'); 


INSERT INTO [dbo].[sre_revenue_mapping] ( 
	[objid], [version], [revenueitemid], [acctid] 
)
SELECT 
	[objid], 'standard', [revitemid], [account_objid] 
FROM 
	[dbo].[revenueitem_attribute] 
WHERE 
	[attribute_objid] IN ( 'srestandard');

