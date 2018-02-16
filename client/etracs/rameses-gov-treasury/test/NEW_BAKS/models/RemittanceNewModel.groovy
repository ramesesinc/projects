package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import java.rmi.server.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;
import javax.swing.*;
import com.rameses.io.*;
import com.rameses.seti2.models.*;

class RemittanceNewModel extends CrudPageFlowModel { 

    public void afterCreate() {
        MsgBox.alert( 'after create');
    }

} 