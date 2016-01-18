package com.rameses.gov.treasury.tool.statementofreceipts
import javax.swing.filechooser.FileFilter;
/**
 *
 * @author Dino Quimson
 */
class XlsxFileFilter extends FileFilter{
    
    @Override
    public boolean accept(File f) {
        if(f.isDirectory()){
            return false;
        }
        return f.getName().endsWith(".xls") || f.getName().endsWith(".XLS");
    }

    @Override
    public String getDescription() {
        return "Excel Workbook (.xls)";
    }
	
}

