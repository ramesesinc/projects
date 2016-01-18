package rameses.gov.etracs.signatory.template;
        
import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
        
public class SignatoryTemplateController
{
    @Binding
    def binding;
    
    @Service("SignatoryService")
    def svc;
    
    @Invoker
    def inv;
    
    String getTitle(){
        return inv.caption + ' Signatories';
    }
    
    def currentIndex = 0;
    def listSize = 0;
    
    def signatories = [];
    def selectedItem;

    String getDocType(){
        return inv.properties.docType;
    }
    
    public void init() {
        signatories = svc.getList( [doctype:docType] );
        currentIndex = signatories.indexOf( selectedItem );
        listSize = signatories.size();
    }
    
    public def addSignatory() {
        def params = [
            doctype : getDocType(),
            indexno : signatories.size() + 1,
            oncreate : { signatory ->
                svc.create(signatory);
                signatories.add(signatory);
                signatoryListHandler.load();
            }
        ];

        return InvokerUtil.lookupOpener( 'signatory:create', params );
    }
    
    public void removeSignatory() {
        if( MsgBox.confirm( 'Remove signatory?' ) ) {
            signatories = svc.delete([signatory:selectedItem, signatories:signatories]);
            signatoryListHandler.load();
        }
    }
    
    public void moveUp() {
        def prevItem = signatories.get( selectedItem.indexno - 2 );
        signatories = svc.changeIndexNo( [selectedItem, prevItem] );
        signatoryListHandler.load();
    }
    
    public void moveDown() {
        def nextItem = signatories.get( selectedItem.indexno );
        signatories = svc.changeIndexNo( [selectedItem, nextItem] );
        signatoryListHandler.load();
    }

    def signatoryListHandler = [
        getColumns  : {
            return [
                new Column(name:'lastname', caption:'Last Name'),
                new Column(name:'firstname', caption:'First name'),
                new Column(name:'middlename', caption:'Middle Name'),
                new Column(name:'title', caption:'Title'), 
                new Column(name:'department', caption:'Department')
            ];
        },
        fetchList    : { return signatories; }
    ] as BasicListModel
            
    void setSelectedItem(selectedItem){
        this.selectedItem = selectedItem;
        currentIndex = signatories.indexOf( selectedItem );
        listSize = signatories.size();
    }
}