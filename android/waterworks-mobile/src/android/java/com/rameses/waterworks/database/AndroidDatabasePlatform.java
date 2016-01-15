package com.rameses.waterworks.database;

import android.content.Context;
import javafxports.android.FXActivity;

public class AndroidDatabasePlatform extends DatabasePlatform{

    @Override
    public Database getDatabase() {
        Context ctx = FXActivity.getInstance().getApplicationContext();
        return new AndroidDatabase(ctx);
    }
    
}
