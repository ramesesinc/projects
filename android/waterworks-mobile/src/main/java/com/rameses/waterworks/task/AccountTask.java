package com.rameses.waterworks.task;

import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Stubout;
import com.rameses.waterworks.dialog.Dialog;
import com.rameses.waterworks.page.AccountDetail;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class AccountTask extends Task<Void> {
    
    private Account account;
    private Stubout stubout;
    private int position;
        
    public AccountTask(Account account, Stubout stubout, int position){
        this.account = account;
        this.stubout = stubout;
        this.position = position;
    }

    @Override
    protected Void call() throws Exception {
        try{ Thread.sleep(200); }catch(Throwable t){ System.err.println(t); }
        if(account != null){
            Platform.runLater(new Runnable(){
                @Override
                public void run() {
                    if(Dialog.TITLE == null) Dialog.TITLE = new Label();
                    Node child = new AccountDetail(account,stubout,position).getLayout();
                    Dialog.show("Account Information", child);
                    if(stubout != null) Dialog.TITLE.setText(stubout.getCode() + " ( " + account.getSortOrder() + " )");
                }
            });
        }
        return null;
    }
    
}
