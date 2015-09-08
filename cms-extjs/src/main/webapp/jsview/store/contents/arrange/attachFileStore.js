var attatchFileStore = Ext.create('Ext.data.Store', {
 
    model: 'AttachFileModel',
    proxy: {
        type: 'ajax'
        // ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
        ,
        headers: {
        	 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' 
        },
        noCache: false,
        reader: {
            type: 'json',
            root: 'attatchs' 
        }
    },
    autoLoad: false,
    autoSync: true,
    listeners : {
    	load : function(view,records){
    		
    		//공백의 리스트를 생성한다.
    		var emptyRaw=10;
    		if((emptyRaw - records.length) > 0){
    			var empty =  emptyRaw - records.length;
    			
    			for(var i =0; i<empty;i++){
    				//findMediaStore.add({'ctiFmt':'','flExt':''});
    			}
    		}
    	}
    }
});

