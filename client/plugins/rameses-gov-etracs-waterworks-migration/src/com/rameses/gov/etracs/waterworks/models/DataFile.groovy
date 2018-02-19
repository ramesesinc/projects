package com.rameses.gov.etracs.waterworks.models; 

import java.io.File;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class DataFile {
    
    private File file;
    private Sheet sheet;
    private Workbook workbook;

    private String filepath;
    private int totalrows; 
    
    private def coldefs; 
    private def colfields;
    
    public DataFile( File file ) {
        this.filepath = file.canonicalPath; 
        this.file = new File( this.filepath ); 
        init( null );
    }
    
    public DataFile( Map info ) {
        if ( !info.filepath ) throw new Exception('infopath property is required'); 
        
        this.file = new File( info.filepath ); 
        this.filepath = file.canonicalPath; 
        init( info ); 
    }
       
    private init( Map info ) {
        workbook = Workbook.getWorkbook( file );
        sheet = workbook.getSheet(0); 
        totalrows = sheet.getRows(); 
        
        coldefs = (info?.coldefs ? info.coldefs : []);
        colfields = (info?.colfields ? info.colfields : []);
        
        if ( coldefs.isEmpty() ) {
            int colsize = sheet.getColumns();
            for (int i=0; i<colsize; i++) { 
                def cell = sheet.getCell(i, 0); 
                def cmap = [ index: cell.column, caption: cell.contents ]; 
                coldefs << cmap; 
            } 
        }
        if ( colfields.isEmpty() ) {
            colfields << [ name: 'acctno', caption:'Account Number', type:'string', required:true ];
            colfields << [ name: 'acctname', caption:'Account Name', type:'string', required:true ];
            colfields << [ name: 'accttype', caption:'Account Type', type:'string', required:true ];
            colfields << [ name: 'dtstarted', caption:'Date Started', type: 'date' ];
            colfields << [ name: 'address_text', caption:'Address', type: 'date' ];
            colfields << [ name: 'prevreading', caption:'Previous Reading', type: 'integer', required:true ];
            colfields << [ name: 'currentreading', caption:'Current Reading', type: 'integer', required:true ];
            colfields << [ name: 'dtreading', caption:'Reading Date', type: 'date' ];
            colfields << [ name: 'amount', caption:'Amount', type: 'decimal', required:true, defaultvalue:0.0 ];
            colfields << [ name: 'meterno', caption:'Meter Number', type:'string' ];
            colfields << [ name: 'metersize', caption:'Meter Size', type:'string' ];
            colfields << [ name: 'sectorcode', caption:'Sector', type:'string', required:true ];
            colfields << [ name: 'zonecode', caption:'Zone', type:'string', required:true ];
            colfields << [ name: 'stuboutcode', caption:'Stubout', type:'string' ];
        }
    } 
    
    public def getColDefs() { return coldefs; } 
    public def getColFields() { return colfields; } 
    
    public boolean isValidDefaultValue( colfield, value ) { 
        if ( colfield == null || value == null ) return true; 

        try {
            if ( colfield.type == null || colfield.type == 'string') {
                return true; 
            } else if ( colfield.type == 'integer' ) { 
                Integer.parseInt( value.toString()); 
                return true; 
            } else if ( colfield.type == 'decimal' ) { 
                new BigDecimal( value.toString()); 
                return true; 
            } else if ( colfield.type == 'date' ) {
                java.sql.Date.valueOf( value.toString()); 
                return true; 
            } else {
                return true; 
            }
        } catch(Throwable t) {
            return false; 
        }
    }
    
    public Map buildDef() {
        Map map = new HashMap();
        map.filepath = filepath;
        map.totalrows = totalrows; 
        map.coldefs = new ArrayList();
        map.colfields = new ArrayList();
        coldefs.each{ 
            Map dest = new HashMap(); 
            copyMap( it, dest );  
            map.coldefs << dest; 
        }
        colfields.each{ 
            Map dest = new HashMap(); 
            copyMap( it, dest ); 
            map.colfields << dest; 
            dest.remove('uploaded'); 
        }
        return map;
    }
    private void copyMap( Map source, Map dest ) {
        source.each{ k,v-> 
            if (v instanceof Map) {
                Map m = new HashMap();
                dest.put(k, m);
                copyMap( v, m ); 
            } else {
                dest.put(k, v); 
            }
        }
    }
    
    public void read( options ) { 
        for (int r=1; r<totalrows; r++) { 
            def item = new LinkedHashMap();
            try { 
                item.indexno = r; 
                colfields.each{ f-> 
                    buildItem(r, f, item); 
                } 
            } catch(Throwable t) { 
                item.haserror = true; 
                item.errormessage = t.message; 
            }
            
            if ( options.onbuildItem ) {
                options.onbuildItem( item ); 
            } 
        } 
    }
    private void buildItem( rowindex, colfield, item ) {
        def value = null; 
        if ( colfield.cell?.index ) { 
            def cell = sheet.getCell(colfield.cell.index, rowindex);
            value = cell.contents; 
        } 
        if ( !value ) value = colfield.defaultvalue; 
        if ( colfield.required && !value ) { 
            throw new Exception(''+ colfield.caption +' is required in line number '+ rowindex); 
        } 
        
        item.put( colfield.name, value ); 
    }
}