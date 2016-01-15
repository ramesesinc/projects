package com.rameses.clfc.treasury.encashment;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class EncashmentGeneralController
{
    @Binding
    def binding;

    @ChangeLog
    def changeLog;

    String title = "General Information";

    def entity, allowEdit = true, bank, passbook;

    def bankLookup = Inv.lookupOpener('bank:lookup', [
        onselect: { o->
            entity?.check?.bank = o;
            binding.refresh('bank');
        },
        state   : 'ACTIVE'
    ]);

    def passbookLookup = Inv.lookupOpener('passbook:lookup', [
        onselect: { o->
            entity?.check?.passbook = o;
            binding.refresh('passbook');
        },
        state   : 'ACTIVE'
    ]);

    void refresh() {
        binding?.refresh();
    }

}