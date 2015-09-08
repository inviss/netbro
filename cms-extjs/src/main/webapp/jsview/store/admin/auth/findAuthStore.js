
var findAuthStore = Ext.create('Ext.data.Store', {
    model: 'AuthModel',
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
            root: 'authTbl'
        }
    },
    autoLoad: false,
    autoSync: true,
});