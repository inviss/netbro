var getMediaStore = Ext.create('Ext.data.Store', {
 
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
    		
    		
			var videoMetaForm = Ext.ComponentQuery.query('#viedeoMeta_' + records[0].data.ctId);
			var videoFrom = {
					ctLeng : records[0].data.ctLeng,
					aspRtoCd : records[0].data.aspRtoCd 
			}
			
			videoMetaForm[0].getForm().setValues(videoFrom);
			
    	}
    }
});

