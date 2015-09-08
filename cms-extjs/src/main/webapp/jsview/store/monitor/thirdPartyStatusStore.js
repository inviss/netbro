var thirdPartyStatus = Ext.create('Ext.data.Store', {
    model: 'ThirdModel',
    proxy: {
        type: 'ajax'
        // ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
        ,
        headers: {
            'Content-Type': 'application/json'
        },
        noCache: false,
        url: '/rest/monitor/find/status/',
        reader: {
            type: 'json',
            root: 'userTbl',
        }
    },
    autoLoad: false,
    autoSync: true,
    listeners: {
        load: function (t, records, options) {/*
 
        	//findAuthStore에서 가져온 AuthId

            var tmpAuthId = arrAuthId.substring(1);            
            var tmpAuthIdArr = tmpAuthId.split(',');
          
            for(var i = 0; i < tmpAuthIdArr.length; i++) {
                var k = 0;
                var checkedAuthId;
                Ext.getCmp(tmpAuthIdArr[i]).setValue(false);

                for(var j = 0; j < records.length; j++) {
                    if(tmpAuthIdArr[i] == records[j].data.authId) {
                        k++;
                        checkedAuthId = records[j].data.authId;
                    }
                }
                
                if(k != 0) {
                    Ext.getCmp(checkedAuthId).setValue(true);
                }
            }
        */}
    }
    
});





