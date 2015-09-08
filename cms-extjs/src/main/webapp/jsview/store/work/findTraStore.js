var findTraStore = Ext.create('Ext.data.Store', {
	pageSize: 20,
    model: 'TraModel',
    proxy: {
        type: 'ajax'
            // ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
            ,
        headers: {
            'Content-Type': 'application/json'
        },
        noCache: false,
        reader: {
            type: 'json',
            root: 'traTbls',
            totalProperty: 'totalCount'
        }
    }
});