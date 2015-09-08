 
var clipStore = Ext.create('Ext.data.Store',{
				model : 'ClipList'
				,pageSize : 10
			   ,proxy : {
				   			type : 'ajax'
				   			// ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
							,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
							,noCache: false 
				   			,reader : {
				   			type : 'json'
				   			,root : 'metas'
				   			,totalProperty : 'total'
				   			}
			   } 
				,autoLoad : false
				,autoSync : true
				 ,listeners : {
					  load : function(view,records){
						 
						  contentsInstStore.loadRawData(view.proxy.reader.jsonData)
					  }
				  }
				
				//사용자 정의 함수 이함수는 사용자 정의 로 구현되는 함수이다. 스토리보드 root를 파싱하는 함수로구현
				,getInst : function(){
					 
					   return contentsInstStore;
				}
			});
			


//사전 정의 스토어 
//사용하고자하는 root를 parsing하는 함수를 호출하여 구현한다
var contentsInstStore = Ext.create('Ext.data.Store',{
	model : 'ContentsInstModel'
	,proxy : {
		type : 'memory'
		,reader : {
			type : 'json'
		   ,root : 'contentsInst'
		}
	}});
