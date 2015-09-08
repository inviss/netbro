var getNoticeStore = Ext.create('Ext.data.Store', {
	pageSize: 20,
    model: 'NoticeModel',
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
            root: 'noticeTbl'
        }
    },
    autoLoad: false,
    autoSync: true,
});

