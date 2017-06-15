function Blinker( elemid, duration ) {

       var _BLINKER_KEY_FLAG = 'rui-blinker-element';

	var _self = this; 
	var _elemid = elemid;  
	var _duration = duration || 5000; 

	var _DELAY = 390;
	var _cancelled; 
	var _period; 

	this._init = function() {
		_cancelled = 0; 
		_period = 0; 
	}
	this._hideElem = function() {
		var elemobj = $(_elemid);
		if (!elemobj.hasClass(_BLINKER_KEY_FLAG)) {
			this.stop(); 
			return; 
		} 

		elemobj.css({visibility: 'hidden'});
		elemobj.addClass('blinker-blink');
 
		if ( _period < _duration ) {
			setTimeout( _self._showElem, _DELAY ); 
			_period += _DELAY; 
		}
	}
	this._showElem = function() {
		var elemobj = $(_elemid);
		elemobj.css({visibility: 'visible'}); 
		
		if ( _cancelled == 0 ) { 
			if ( _period < _duration ) { 
				setTimeout( _self._hideElem, _DELAY ); 	
				_period += _DELAY; 
				return; 
			} 
		} 
		elemobj.removeClass('blinker-blink'); 
	} 
	this.start = function() { 
		var elemobj = $(_elemid);
		elemobj.addClass(_BLINKER_KEY_FLAG);

		this._init();
		this._hideElem(); 
		this._play(); 
	} 
	this.stop = function() { 
		_cancelled = 1; 

		var elemobj = $(_elemid);
		elemobj.removeClass(_BLINKER_KEY_FLAG);
		elemobj.removeClass('blinker-blink'); 
		elemobj.css({visibility: 'visible'}); 
	}
	this._play = function() {
		try {  
			var audio = document.createElement('audio');
			audio.setAttribute('src', '/res/queue/audio/ding.mp3');
			audio.play(); 
		} catch(e) {;} 
	} 
}; 


tday=new Array("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday");
tmonth=new Array("January","February","March","April","May","June","July","August","September","October","November","December");

function GetClock(){
var d=new Date();
var nday=d.getDay(),nmonth=d.getMonth(),ndate=d.getDate(),nyear=d.getYear();
if(nyear<1000) nyear+=1900;
var nhour=d.getHours(),nmin=d.getMinutes(),nsec=d.getSeconds(),ap;

if(nhour==0){ap=" AM";nhour=12;}
else if(nhour<12){ap=" AM";}
else if(nhour==12){ap=" PM";}
else if(nhour>12){ap=" PM";nhour-=12;}

if(nmin<=9) nmin="0"+nmin;
if(nsec<=9) nsec="0"+nsec;

$('#clockbox').html( ""+tday[nday]+", "+tmonth[nmonth]+" "+ndate+", "+nyear+" "+nhour+":"+nmin+":"+nsec+ap+"" );
}



$(function(){

	GetClock();
	setInterval(GetClock,1000);

    $(".dropdown").hover(            
            function() {
                $('.dropdown-menu', this).stop( true, true ).fadeIn("fast");
                $(this).toggleClass('open');               
            },
            function() {
                $('.dropdown-menu', this).stop( true, true ).fadeOut("fast");
                $(this).toggleClass('open');              
            });
    });
    
	

	