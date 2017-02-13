package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.util.*;
import com.rameses.seti2.models.*;

public class BPExpiryDateModel extends CrudFormModel {

    def qtrs = (1..4);

}