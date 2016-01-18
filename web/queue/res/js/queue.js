var width;
var height;

var group;
var categories;
var sections;

var stage;
var plumb;
var dialog

var groupBtn;
var categoryBtn;
var sectionBtn;

var selectedGroupBtn;
var selectedCategoryBtn;
var currentCategoryContainer;

var currentNumber;

$(document).ready(function(){
	width = $(document).width();
	height = $(document).height();

	stage = $("#container");
	groupBtn = $(".group");
	categoryBtn = $(".category");
	sectionBtn = $(".section");
	
	loadGroups();

	$("#blue").click(function(){
		$('link[href="res/css/default.css"]').attr('href','res/css/default.css');
		$('link[href="res/css/red.css"]').attr('href','res/css/default.css');
		$('link[href="res/css/yellow.css"]').attr('href','res/css/default.css');
	});

	$("#red").click(function(){
		$('link[href="res/css/default.css"]').attr('href','res/css/red.css');
		$('link[href="res/css/red.css"]').attr('href','res/css/red.css');
		$('link[href="res/css/yellow.css"]').attr('href','res/css/red.css');
	});

	$("#yellow").click(function(){
		$('link[href="res/css/default.css"]').attr('href','res/css/yellow.css');
		$('link[href="res/css/red.css"]').attr('href','res/css/yellow.css');
		$('link[href="res/css/yellow.css"]').attr('href','res/css/yellow.css');
	});
});


jsPlumb.ready(function(){
	plumb = jsPlumb.getInstance();
});


function loadGroups(){
	var service = Service.lookup("QueueGroupService","main");
	var list = service.getAllGroups();
	var buttons = [];
	$.each(list,function(index,value){
		//creating a group-button
		var name = value.name;
		var button = groupBtn.clone();
		button.text(name);
		button.css( "display","block" );
		button.click(function(){
			groupid = value.objid;
			stage.animate({
				width : width,
				height : height - 30,
				top : '0px',
				left : '0px'
			},function(){
				//this phase will execute after the stage animation was completed
				button.unbind("click");
				button.click(function(){
					location.reload();
				});
				selectedGroupBtn = button;
				group = value;
				categories = value.categories;
				sections = value.sections;
				loadCategories(value,categories);
			});
			$.each(buttons,function(i,button){
				if(i != index){
					button.css("display","none");
					button.animate({
						opacity : '0'
					});
				}
			});
		});

		stage.append(button);
		buttons.push(button);
	});

	stage.css("position","absolute");
	stage.css("left", (width/2) - (stage.width()/2) );
	stage.css("top", (height/2) - (stage.height()/2) );
}


function loadCategories(group,categories){
	$.each(categories,function(index,category){
		//creating category button
		var button = categoryBtn.clone();
		button.css("display","block");
		button.text(category.name);
		selectedCategoryBtn = button;

		//creating container for each button
		var container = $("<div><div>");
		container.width(width/categories.length);
		container.css("text-align","center");
		container.css("float","left");
		container.append(button);
		stage.append(container);
		currentCategoryContainer = container;

		//creating line connectors
		plumb.connect({
            source: selectedGroupBtn,
            target: button,
            anchor: ["Continuous", {faces:["top","bottom"]}],
            endpoint: ["Dot",{radius: 4}],
            connector:["Flowchart",{cssClass:"connector"}],
        });

        //loading the sections
        loadSections(group,category);
	});
}


function loadSections(group,category){
	var previouscontainer = selectedCategoryBtn;
	$.each(sections,function(index,section){
		//creating section buttons
		var button = $("<button class='button section'></button>");
		button.text(section.name);
		button.css("display","block");
		button.addClass(category.objid);
		button.click(function(){
			//get the current number
			var PARAMS = {"groupid":group.objid, "categoryid":category.objid, "sectionid":section.objid};
			console.log(PARAMS);
			var service = Service.lookup("QueueCodeService","main");
			//setting the current number
			currentNumber = service.getNextNumber(PARAMS);
			var number = currentNumber.name ? currentNumber.name : "?";
			$("#number").text(number);

			//show the dialog
			dialog = $("#dialog");
			dialog.css("display","block");
			dialog.dialog({
				title: section.name + " CURRENT NUMBER"
			});
			dialog.prev(".ui-dialog-titlebar").css("background","#373737");
		});
		currentCategoryContainer.append(button);

		//creating line connectors
		jsPlumb.connect({
            source: previouscontainer,
            target: button,
            anchor: ["Continuous", {faces:["top","bottom"]}],
            endpoint: ["Dot",{radius: 4}],
            connector:["Flowchart",{cssClass:"connector"}],
        });

        previouscontainer = button;
	});
}


function generateCode(){
	var service = Service.lookup("QueueCodeService","main");
	var number = "?";
	if(currentNumber.objid){
		console.log(currentNumber.objid);
		number = currentNumber.name;
		var PARAMS = {'objid':currentNumber.objid, 'state':'PENDING'};
		service.update(PARAMS);
		dialog.dialog('close');
	}
}




