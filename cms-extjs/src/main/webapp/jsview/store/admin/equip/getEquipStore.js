var getEquipStore = Ext.create('Ext.data.Store', {
    model: 'EquipModel',
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
            root: 'equipmentTbl'
        }
    },
    autoLoad: false,
    autoSync: true

});

