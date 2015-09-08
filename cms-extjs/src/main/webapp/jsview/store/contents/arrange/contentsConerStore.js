
var contentsCornerStore = Ext.create('Ext.data.Store',{
				model : 'ConerInfo'
			   ,proxy : {
				   			type : 'ajax'
				   			 ,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 
				   			//,url : '/contents/getContentsSearchStroyboardCornerInfo.ssc'
				   			 
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
                          	tools :[{
                          		type : 'gear',
                          		id : 'win_'+tempImg,
                          		handler : function(){
                          			 
                          		},
                          		listeners : {
                          			click : function( view, e, eOpts ){
                          				
                          				var winId = view.id.split('win_');
                          			 
                              			Ext.create('Ext.window.Window',{
                              				title : '정보 변경'
                              				,width : 280
                              				,height : 180
                              				,layout : 'form' 
                              				
                              				,items : [{
                              					xtype : 'textfield'
                              						,margin : '5'
                              						,fieldLabel : '코너제목'
                              						,name : 'changeCnNm'
                              				},{
                              					xtype : 'textareafield'
                              						,margin : '5'
                              						,fieldLabel : '코너 내용'
                              						,name : 'changeCnCont'
                              				}]
                              			,buttons : [{
                              				text : '편집',
                              				
                              				handler : function(){
                              					var cornerName = Ext.ComponentQuery.query('field[name=changeCnNm]');
                              					var cornerCont = Ext.ComponentQuery.query('field[name=changeCnCont]');
                              					 
                              					var targetPanel = Ext.ComponentQuery.query('#temp_'+''+winId[1]+'');
                              					var targetCont = Ext.ComponentQuery.query('field[name=cnCont_'+winId[1]+']');
                              					var me = this;
                              				 
                              					 targetPanel[0].setTitle(cornerName[0].getValue());
                              					 targetCont[0].setValue(cornerCont[0].getValue()) ;
                              					var win  = Ext.ComponentQuery.query('window');
                              					win[1].close();
                              				}
                              			}]
                              			 
                              			}).show();
                              		
                          			}
                          		}
                          	}],
                          	renderTo : 'cn_'+records[i].data.sDuration+'_'+records[i].data.ctId
                          });
                          
					   }
					 
					  
				  }
			  }
			 
		 
			});
			