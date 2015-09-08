var findProfileStore = Ext.create('Ext.data.Store', {
	pageSize: 20,
    model: 'ProfileModel',
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
            root: 'profileInfos'
        }
    },
    autoLoad: false,
    autoSync: true

});
