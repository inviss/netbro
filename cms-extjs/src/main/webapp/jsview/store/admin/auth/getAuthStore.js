var getAuthStore = Ext.create('Ext.data.Store', {
    model: 'AuthModel',
    proxy: {
        type: 'ajax'
             ,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
            ,
        headers: {
        	 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' 
        },
        noCache: false,
        api: {
            read: '/admin/auth/getAuthInfo.ssc',
            create: '/admin/auth/saveAuthInfo.ssc',
            update: '/admin/auth/updateAuthInfo.ssc',

        },
        reader: {
            type: 'json',
            root: 'authTbl',

        },

    },

    listeners: {
        load: function (view, records) {
            roleAuthStore.loadRawData(view.proxy.reader.jsonData)

        }
    },
    getAuth: function () {
        return roleAuthStore;
    }

});

//사전 정의 스토어 
//사용하고자하는 root를 parsing하는 함수를 호출하여 구현한다
var roleAuthStore = Ext.create('Ext.data.Store', {
    model: 'roleAuthModel',

    proxy: {
        type: 'memory',
        reader: {
            type: 'json',
            root: 'authTbl.roleAuth'
        },
    },
    listeners: {
        load: function () {
            console.log('ddd')
        }
    }

});