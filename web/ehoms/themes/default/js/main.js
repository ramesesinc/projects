Scroller.addHandler(function(){
	var scrollTop = $(window).scrollTop();
	var target = $('#theme-header-wall');
	if (scrollTop >= 140) {
		target.addClass('theme-header-background-strong'); 
	} else {
		target.removeClass('theme-header-background-strong'); 
	}
});