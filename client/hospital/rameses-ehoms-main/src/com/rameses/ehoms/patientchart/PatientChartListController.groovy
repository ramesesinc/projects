package com.rameses.ehoms.patientchart;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;

class PatientChartList extends DefaultListController {

    public String getSection() {
        return controller.workunit.workunit.properties.section;
    }


}