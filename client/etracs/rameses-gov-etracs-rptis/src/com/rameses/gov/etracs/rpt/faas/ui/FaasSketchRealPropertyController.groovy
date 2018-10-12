package com.rameses.gov.etracs.rpt.faas.ui;


import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*


class FaasSketchRealPropertyController extends FaasSketchInfoController
{
    def convertToDrawing() {
        if (MsgBox.confirm('Are you sure you want to convert image sketch to drawing format?')){
            return Inv.lookupOpener('sketch:drawing:rp:open', [
                entity : entity,
                oncloseSketch: oncloseSketch,
            ])
        }
        return null;
    }
}
