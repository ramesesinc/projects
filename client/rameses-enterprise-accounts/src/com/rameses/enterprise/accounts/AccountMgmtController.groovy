/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rameses.enterprise.accounts

import com.rameses.osiris2.common.ExplorerViewController;

public abstract class AccountMgmtController extends ExplorerViewController {
    
    public abstract String getServiceName();
    public abstract Object getAccountService();
    public abstract String getTitle();
    
    public String getPrefixId() {
        return "ACCT";
    }
    
    public String getDefaultFileType() {
        return "account";
    }
    
    public String getContext() {
        return "account";
    }    

    void sync() {
        if(! MsgBox.confirm("This will update your current records. Continue? ")) return;
        getAccountService().syncFromCloud();
    }
    
}