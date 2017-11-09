package com.rameses.gov.etracs.rptis.master.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;


class AttributeModel extends CrudFormModel
{
    boolean allowApprove = false;
    boolean editAllowed = false;
}