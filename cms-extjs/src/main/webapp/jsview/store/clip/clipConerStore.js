
var clipCornerStore = Ext.create('Ext.data.Store',{
				model : 'ConerInfo'
			   ,proxy : {
				   			type : 'ajax'
				   		  ,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 
				   		//	,url : '/clip/getClipSearchStroyboardCornerInfo.ssc'
				   			 
				   			,reader : {
				   			type : 'json'
				   		   ,root : 'cornerTbls'
				   			 
				   			 
				   			}
						
							}
			  ,listeners : {
				  load : function(view,records){
					 
					  for(var i=0 ;i<view.data.length;i++){ 
					 
						  var temp = document.getElementById(records[i].data.sDuration+'_'+records[i].data.ctId);
						  var temp_coninfo = '<div id="cornerFill_'+records[i].data.sDuration+'_'+records[i].data.ctId+'" style="float:left;width:100%"><div   style ="float:left;width:100%"><div id = cn_'+records[i].data.sDuration+'_'+records[i].data.ctId+'></div></div>'
						  var tempImg = records[i].data.sDuration;
                          //''+value+'' 형식으로 넣어줘야만 정상적으로 값인식.
                          var tempId = '' + records[i].data.sDuration+'_'+records[i].data.ctId+ ''
                         
                              //코너의 시작 점으로 지정된  obect앞에 생성한 코너 정보를 삽입한다.
                          Ext.get(tempId).insertHtml('beforeBegin', temp_coninfo)
                          
                         //코너로 나누어진 img의 값을 mapp에 넣는다.
                           cornerMap.put(records[i].data.sDuration ,records[i].data.sDuration);
          				 
                          //코너내용을 생성한다.
                          var tempCorner = Ext.create('Ext.panel.Panel' ,{
                          	title : records[i].data.cnNm,                            	 
                          	itemId : 'temp_'+records[i].data.sDuration,
                          	height : 100,
                          	xtype : 'form',
                          	items : [{
                          		xtype : 'textareafield',
                          		width : '100%',
                          		height : '100%',
                          		readOnly : true,
                          		value : records[i].data.cnCont,
                          		name : 'cnCont_'+records[i].data.sDuration 
                          	}],                          
                          	renderTo : 'cn_'+records[i].data.sDuration+'_'+records[i].data.ctId
                          });
                          
					   }
					 
					  
				  }
			  }
			 
		 
			});
			