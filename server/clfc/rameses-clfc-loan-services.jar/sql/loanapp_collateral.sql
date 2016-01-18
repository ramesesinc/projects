[getAppliances]
SELECT * FROM loanapp_collateral_appliance WHERE parentid=$P{parentid} 

[removeAppliances]
DELETE FROM loanapp_collateral_appliance WHERE parentid=$P{parentid} 

[getVehicles]
SELECT * FROM loanapp_collateral_vehicle WHERE parentid=$P{parentid} 

[removeVehicles]
DELETE FROM loanapp_collateral_vehicle WHERE parentid=$P{parentid} 

[getProperties]
SELECT * FROM loanapp_collateral_property WHERE parentid=$P{parentid} 

[removeProperties]
DELETE FROM loanapp_collateral_property WHERE parentid=$P{parentid} 

[findOtherCollateral]
SELECT * FROM loanapp_collateral_other WHERE objid=$P{objid} 

[removeOtherCollateral]
DELETE FROM loanapp_collateral_other WHERE objid=$P{objid} 
