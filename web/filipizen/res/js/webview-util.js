

var WebViewUtil = new function() {

	this.activateBarcodeScanner = function( activate ) {
		//window.console.log("activate " + activate);
		if (window.handler) {
			window.handler.setAllowBarcodeScanning(activate);
		}
	}

	this.onPageLoad = function( params ) {
		window.console.log(params);
		if (window.handler) {
			window.console.log("activatebarcode "+params.activatebarcode)
			if (params.activatebarcode) {
				window.handler.setAllowBarcodeScanning(params.activatebarcode);
			}
			if (params.pagename) {
				window.handler.onpageload(params.pagename);
			}
		}
	}

	this.print = function( params, reporthandler ) {
		if (window.handler) {
			window.handler.print(params, reporthandler);
		}
	}
}