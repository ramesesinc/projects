##################################################
#To transfer market info
##################################################
INSERT INTO LIGAO_MARKET.`market` 
( OBJID, STATE, CODE, NAME, ADDRESS_TEXT, CLASSIFICATION_OBJID, 
address_type, address_bldgname, address_barangay_objid, address_barangay_name, address_city, address_province )
SELECT 
mb.objid, 'DRAFT' AS state, REPLACE(mb.`name`,' ','_') AS CODE , mb.`name`, mb.address AS address_text, m.classification_objid, 
'local' AS address_type, mb.name AS address_bldgname, '', 'BAGUMBAYAN' AS address_barangay_name, 
'LIGAO CITY' AS address_city, 'ALBAY' AS address_province   
 FROM etracs254_ligao_market.MARKET m
 INNER JOIN etracs254_ligao_market.`market_building` mb ON m.objid=mb.marketid


##################################################
# Transfer sections
##################################################
INSERT INTO ligao_market.`market_section`
(objid, state, `name`, description, system)
SELECT  
objid, state, `name`, `name` AS description, system
FROM etracs254_ligao_market.`marketsection`

