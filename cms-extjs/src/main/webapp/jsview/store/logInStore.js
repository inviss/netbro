var loginStore = Ext.create('Ext.data.Store', {
    model: 'LogInModel',
    proxy: {
        type: 'ajax',
        actionMethods: {
            create: "POST",
            read: "POST",
            update: "POST",
            destroy: "POST"
        },
        headers: {
            'Content-Type': 'application/json'
        },
        noCache: false,
        reader: {
            type: 'json',
            //root : 'user'
        }
    }

    ,
    autoLoad: false,
    listeners: {
        load: function (view, records) {
        	searchEngine = records[0].data.searchEngine 
            if(records[0].data.user == 'N') {
                Ext.Msg.alert('알림', 'ID 혹은 PASSWORD가 일치하지 않습니다.')
            } else {
                var userId = Ext.ComponentQuery.query('field[name$=userId]');
                var password = Ext.ComponentQuery.query('field[name$=password]');
                login.load({
                    url: context + '/userLoginAction.ssc',
                    action: 'update',
                    params: {
                        'userId': userId[0].getValue(),
                        'userPasswd': password[0].getValue()
                    }

                })
            }

        }
    }

});

var login = Ext.create('Ext.data.Store', {
    model: 'LogInModel',
    proxy: {
        type: 'ajax',
        actionMethods: {
            create: "POST",
            read: "POST",
            update: "POST",
            destroy: "POST"
        },
        headers: {
            'Content-Type': 'application/json'
        },
        noCache: false
    }
    ,
    autoLoad: false
    ,
    listeners: {
        load: function (view, records) {
        	console.log(searchEngine);
        	if(searchEngine == 'N'){        		
        	  location.href = context + '/contents/arrange/search.ssc'
        	}else{
        	 location.href = context + '/clip/search.ssc'
        	}
        }
    }

});