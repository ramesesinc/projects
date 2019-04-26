drop view if exists vw_property_bidder
;

create view vw_property_bidder
as 
select 
	b.*,
	p.property_objid, 
	e.name as bidder_name,
	e.address_text as bidder_address
from propertyauction_bidder b 
inner join entity e on b.entity_objid = e.objid 
inner join propertyauction_bidder_property p on b.objid = p.parent_objid
;