[deleteAllHeaders]
DELETE FROM image_header WHERE refid = $P{refid}

[deleteAllItems]
DELETE FROM image_chunk 
WHERE parentid IN (
	SELECT objid FROM image_header WHERE refid = $P{refid} 
)

[getImages]	
SELECT * FROM image_header WHERE refid = $P{refid} ORDER BY title 


[getItems]	
SELECT * FROM image_chunk WHERE parentid = $P{objid} ORDER BY fileno

[deleteItems]
DELETE FROM image_chunk WHERE parentid = $P{objid}

