var socket;
var width;
var height;
var categorysize;

var codeSvc;
var notificationSvc;

$(document).ready(function(){
	width = $(document).width();
	height = $(document).height();

	codeSvc = Service.lookup("QueueCodeService","main");
	notificationSvc = Service.lookup("QueueNotificationService","main");
	var service = Service.lookup("QueueCategoryService","main");
	var categories = service.getList({});
	categorysize = categories.length;

	socket = Socket.create('queuepoll');
	socket.start();
	socket.handlers.myhandler = function(o){
		var data = o[0];
		if(data.code == "CLIENT"){
			location.reload();
		}
	}
});

function generate(sectionid,categoryid,id){
	var PARAMS = {"sectionid":sectionid, "categoryid":categoryid};
	var current = codeSvc.getNextNumber(PARAMS);
	if(Object.keys(current).length > 0){
		var id = "#" + id;
		var buttontext = $(id);

		var PARAMS2 = {"objid":current.objid,"state":"PENDING"};
		codeSvc.update(PARAMS2);

		var next = codeSvc.getNextNumber(PARAMS);
		var nextnumber = "?";
		if(Object.keys(next).length > 0) nextnumber = next.name;
		buttontext.text(nextnumber);

		notificationSvc.updateClientPage(next);
	}
}



