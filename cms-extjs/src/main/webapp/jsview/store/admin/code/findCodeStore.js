var findCodeStore = Ext.create('Ext.data.Store', {
	pageSize: 20,
    model: 'CodeModel',
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
            root: 'codeInfos',
            totalProperty : 'totalCount'
        }
    },
    autoLoad: false,
    autoSync: true,

});


