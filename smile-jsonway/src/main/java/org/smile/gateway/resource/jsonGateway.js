var CallerResponder = new Class({
	debug:		false,
	context: 	new Object(),	
	success: 	function(jsonObj) {	
					var str = JWay.toJSON(jsonObj);			
					var msg = "ajax success: " + str;
					if (this.debug) {
						console.debug(msg);
					}
				},
	error:		function(request,settings,e) {
					if (request.status == 500) {
						var jsonError = JWay.parseJSON(request.responseText);
						alert(jsonError.code+"\r\n"+jsonError.message+"\r\n"+jsonError.details);
					}
					if (this.debug) {
						console.debug("ajax error: " + request.responseText);
						alert(e);
					}
				},
	complete:	function(res, status) {
					if (this.debug) {
						console.debug("ajax complete");
					}
				},
	beforeSend:	function(xml) {
					if (this.debug) {
						console.debug("ajax beforeSend:" + xml);
					}		
				}								
});	

var RemoteJsonService = new Class({
	jsonGateway: "/gateway/",
	async:		 true,
	
	ajaxCall: function(data,callerResponder) {
		jQuery.ajax({				
				type: 		"POST",
				url:  		this.jsonGateway,
				data: 		data,
				dataType: 	"json",
				async: 		this.async,
				debug:		callerResponder.debug,
				context:	callerResponder.context,
				success: 	callerResponder.success,							
				error:   	callerResponder.error,
				complete: 	callerResponder.complete,
				beforeSend: callerResponder.beforeSend			
			});			
		},
	
	 preprocess: function() {
		var callerResponder;
		if (arguments.length > 0){
			var callbackIdx=arguments.length-1;
			if(arguments[callbackIdx] instanceof CallerResponder) {
				callerResponder = arguments[callbackIdx];
				Array.prototype.splice.apply(arguments,[callbackIdx,1]);
			}else if(arguments[callbackIdx] instanceof Function ){
				callerResponder = new CallerResponder();
				callerResponder.success=arguments[callbackIdx];
				Array.prototype.splice.apply(arguments,[callbackIdx,1]);
			}
		}
		var newArgs = new Array();
		for (var i=0; i<arguments.length; i++) {
			newArgs[i] = arguments[i];
		}
		if (!callerResponder) {
			callerResponder = new CallerResponder();
		}	 
		var data = new Object();
		data.service = this.serviceName;		
		data.arguments = JWay.toJSON(newArgs);		
		var preprocessResult = new Object();
		preprocessResult.callerResponder = callerResponder;
		preprocessResult.data = data;
		return 	preprocessResult;
	 }		
});