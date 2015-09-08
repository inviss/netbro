checkboxArray = [];

//권한 역활 checkbox
var proBusiCheckbox = Ext.create('Ext.form.CheckboxGroup', {
    columns: 1,
});

var findProBusiStore = Ext.create('Ext.data.Store', {
    model: 'ProfileModel',
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
            root: 'profileInfos'
        }
    },
    autoLoad: false,
    autoSync: true,
    listeners: {
        load: function (t, records, options) {
            for(var i = 0; i < records.length; i++) {
                checkboxArray.push({
                    xtype: 'checkboxfield',
                    id: records[i].data.proFlId,
                    boxLabel: records[i].data.proFlnm,
                    name: 'proFlId',
                    inputValue: records[i].data.proFlId
                });
                arrProBusiId += "," + records[i].data.proFlId;
            }
            proBusiCheckbox.add(checkboxArray);
        }
    }
});