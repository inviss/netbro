var saveStorageStore = Ext.create('Ext.data.Store', {
	model : 'StorageModel',
    proxy: {
        type: 'ajax',
        actionMethods: {
            create: "POST",
            read: "GET",
            update: "POST",
            destroy: "POST"
        },
        headers: {
            'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache'
        },
        noCache: false
            ,
        reader: {
            type: 'json',
        }
    }
});