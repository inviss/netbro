checkboxArray = [];

//권한 역활 checkbox
var userAuthCheckbox = Ext.create('Ext.form.CheckboxGroup', {
    columns: 1,
});

var findUserAuthStore = Ext.create('Ext.data.Store', {
    model: 'AuthModel',
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
            root: 'authTbl'
        }
    },
    autoLoad: false,
    autoSync: true,
    listeners: {
        load: function (t, records, options) {
            for(var i = 0; i < records.length; i++) {
                checkboxArray.push({
                    xtype: 'checkboxfield',
                    id: records[i].data.authId,
                    boxLabel: records[i].data.authNm,
                    name: 'authId',
                    inputValue: records[i].data.authId
                });
                arrAuthId += "," + records[i].data.authId;
            }
            userAuthCheckbox.add(checkboxArray);
        }
    }
});