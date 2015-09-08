var findMediaStore = Ext.create('Ext.data.Store', {
 
    model: 'MediaModel',
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
            root: 'medias' 
        }
    },
    autoLoad: false,
    autoSync: true,
    listeners : {
    	load : function(view,records){
    		console.log(records);
    		//공백의 리스트를 생성한다.
    		var emptyRaw=10;
    		if((emptyRaw - records.length) > 0){
    			var empty =  emptyRaw - records.length;
    			console.log(records.length);
    			console.log(empty);
    			for(var i =0; i<empty;i++){
    				findMediaStore.add({'ctiFmt':'','flExt':''});
    				console.log(i)
    			}
    		}
    	}
    }
});

