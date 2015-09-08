var findClfStore = Ext.create('Ext.data.Store', {
    model: 'CodeModel',
    proxy: {
        type: 'ajax',
        headers: {
            'Content-Type': 'application/json'
        },
        noCache: false,
        reader: {
            type: 'json',
            root: 'codes'
        }
    }

    ,
    autoLoad: false 

});