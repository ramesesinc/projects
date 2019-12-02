package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class ErrorsModel  
{
    String title = 'List of Errors';
    
    def errors;
    def selectedItem;
    
    def listHandler = [
        fetchList: {errors}
    ] as BasicListModel
}