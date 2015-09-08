
var clipStoryBoardStore = Ext.create('Ext.data.Store',{
				model : 'StoryBoard'
			   ,proxy : {
				   			type : 'ajax'
				   			// ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 
				   			//,url : '/clip/getClipSearchStroyboardInfoForExtJs.ssc'
				   			 
				   			,reader : {
				   			type : 'json'
				   			,root: 'storyboard' 
				   			 
				   			}
			   }
				,autoLoad : true
				  ,listeners : {
					  load : function(view,records){ 
						  
						 if(records[0] != undefined){
						  var corner = clipCornerStore.load({
	                        	 url :  context + '/clip/getClipSearchStroyboardCornerInfo.ssc',

			        			  action :'read'
			        				  ,params : {'ctId' :  records[0].data.ctId

			        				  }
			        		  
	                         });  
						 }  
					  }
				  }
		 
			});
			