package com.rameses.gov.etracs.rptis.models;

interface AttachmentAdapter {
    public void loadFolders(files);
    public void loadFolders(files, folderName);
    public void loadItems( info );
}
