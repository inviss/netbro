var adminMenuStore = Ext.create('Ext.data.Store', {
    model: 'MenuModel',
    proxy: {
        type: 'ajax',
        actionMethods: {
            create: "POST",
            read: "GET",
            update: "POST",
            destroy: "POST"
        },
        headers: {
            'Content-Type': 'application/json'
        },
        noCache: false,
        reader: {
            type: 'json',
            root : 'menuTbls'
        }
    }

    ,
    autoLoad: false,
    listeners: {
        load: function (view, records) {
        	var menu = Ext.ComponentQuery.query('#admin_menu');
        	 
        	for(var i=0 ; i < records.length ; i++){
        		tempUrl=records[i].data.url
        		menu[0].add(Ext.create('Ext.menu.Item',{
        			text : records[i].data.menuNm,
        			itemId : records[i].data.menuId,
        			href : context+records[i].data.url,
        			hrefTarget : '_self' 
        		}))
        	 
        		
        	}
        }
    }

});


var contentsMenuStore = Ext.create('Ext.data.Store', {
    model: 'MenuModel',
    proxy: {
        type: 'ajax',
        actionMethods: {
            create: "POST",
            read: "GET",
            update: "POST",
            destroy: "POST"
        },
        headers: {
            'Content-Type': 'application/json'
        },
        noCache: false,
        reader: {
            type: 'json',
            root : 'menuTbls'
        }
    }

    ,
    autoLoad: false,
    listeners: {
        load: function (view, records) {
        	var menu = Ext.ComponentQuery.query('#contents_menu');
        	
        	for(var i=0 ; i < records.length ; i++){
        	 
        		menu[0].add(Ext.create('Ext.menu.Item',{
        			text : records[i].data.menuNm,
        			itemId : records[i].data.menuId,
        			href : context+records[i].data.url,
        			hrefTarget : '_self' 
        		}))
        	 
        		
        	}
        }
    }

});


var ServiceMenuStore = Ext.create('Ext.data.Store', {
    model: 'MenuModel',
    proxy: {
        type: 'ajax',
        actionMethods: {
            create: "POST",
            read: "GET",
            update: "POST",
            destroy: "POST"
        },
        headers: {
            'Content-Type': 'application/json'
        },
        noCache: false,
        reader: {
            type: 'json',
            root : 'menuTbls'
        }
    }

    ,
    autoLoad: false,
    listeners: {
        load: function (view, records) {
        	var menu = Ext.ComponentQuery.query('#service_menu');
        	
        	for(var i=0 ; i < records.length ; i++){
        	 
        		menu[0].add(Ext.create('Ext.menu.Item',{
        			text : records[i].data.menuNm,
        			itemId : records[i].data.menuId,
        			href : context+records[i].data.url,
        			hrefTarget : '_self' 
        		}))
        	 
        		
        	}
        }
    }

});


var StatisticsMenuStore = Ext.create('Ext.data.Store', {
    model: 'MenuModel',
    proxy: {
        type: 'ajax',
        actionMethods: {
            create: "POST",
            read: "GET",
            update: "POST",
            destroy: "POST"
        },
        headers: {
            'Content-Type': 'application/json'
        },
        noCache: false,
        reader: {
            type: 'json',
            root : 'menuTbls'
        }
    }

    ,
    autoLoad: false,
    listeners: {
        load: function (view, records) {
        	var menu = Ext.ComponentQuery.query('#statistic_menu');
        	
        	for(var i=0 ; i < records.length ; i++){
        	 
        		menu[0].add(Ext.create('Ext.menu.Item',{
        			text : records[i].data.menuNm,
        			itemId : records[i].data.menuId,
        			href : context+records[i].data.url,
        			hrefTarget : '_self' 
        		}))
        	 
        		
        	}
        }
    }

});
