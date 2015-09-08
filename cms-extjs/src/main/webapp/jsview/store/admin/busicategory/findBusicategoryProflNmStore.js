checkboxArray = [];

var busipartnerCheckbox = Ext.create('Ext.form.CheckboxGroup', {
    columns: 1,
});



var findBusicategoryProflNmStore = Ext.create('Ext.data.Store', {
	model: 'BusipartnerModel',
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
				root: 'busiPartnerTbl'
			}
	},
	autoLoad: false,
	autoSync: true, 
	listeners: {
        load: function (t, records, options) {
        	tmpBusiProNm = t.proxy.reader.jsonData.busiPartner
        }
    }
});