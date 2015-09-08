var saveBusicategoryStore = Ext.create('Ext.data.Store', {
	model : 'BusicategoryModel',
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