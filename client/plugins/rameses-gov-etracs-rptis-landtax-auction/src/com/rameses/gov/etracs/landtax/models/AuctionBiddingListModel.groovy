package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AuctionBiddingListModel extends CrudListModel
{
    public void initColumn( def col ) {
        if (col.name == 'bidder.entity.name') {
            col.caption = 'Winning Bidder';
        }
    }
}