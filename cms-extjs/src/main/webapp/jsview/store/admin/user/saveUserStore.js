var saveUserStore = Ext.create('Ext.data.Store', {
	model : 'UserModel',
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
        },
        success: function () {
            Ext.Msg.Alert("저장 되었습니다")
        }
    },
    listeners: {
        load: function (view, records) {

        }
    },

});