var serverResourceStore = Ext.create('Ext.data.Store', {
    model: 'monitoringModel',
    proxy: {
        type: 'ajax',
        // ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
        //url: '/monitor/search.ssc',
        headers: {'Content-Type': 'application/json'},
        noCache: false,
        reader: {
            type: 'json',
            root: 'cpuAndMem',
        }
    },
    autoLoad: false,
    autoSync: true,
    listeners: {
        load: function (t, records, options) {
        	//console.log(records)
        },
        servers: function() {
        	
        }
    }
    
});





