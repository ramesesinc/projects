package com.rameses.geolocation.controller;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.client.ui.fx.*;

public class Geolocation {

    def mapModel = [
        getProperty: {
            return [
                latitude  : 10.30349809,
                longitude : 123.90987039,
                zoom      : 15
            ]
        },
        onLocationSelected: { o->
            println o;
        }
    ] as GoogleMapModel;

}