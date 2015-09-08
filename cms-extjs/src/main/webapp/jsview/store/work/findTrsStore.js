var findTrsStore = Ext.create('Ext.data.Store', {
	pageSize: 20,
    model: 'TrsModel',
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
            root: 'trsTbls',
            totalProperty: 'totalCount'
        }
    }
});