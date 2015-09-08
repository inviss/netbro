
var getProBusiStore = Ext.create('Ext.data.Store', {
	model: 'ProBusiModel',
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
				root: 'proBusiTbls'
			}
	},
	autoLoad: false,
	autoSync: true,
	listeners: {
		load: function (t, records, options) {

			var tmpProBusiId = arrProBusiId.substring(1);            
			var tmpProBusiArr = tmpProBusiId.split(',');
			
			
            for(var i = 0; i < tmpProBusiArr.length; i++) {
                var k = 0;
                var checkedProfileId;
                Ext.getCmp(tmpProBusiArr[i]).setValue(false);

                for(var j = 0; j < t.proxy.reader.jsonData.proBusiTbls.length; j++) {
                    if(tmpProBusiArr[i] == t.proxy.reader.jsonData.proBusiTbls[j]) {
                        k++;
                        checkedProfileId = t.proxy.reader.jsonData.proBusiTbls[j];
                    }
                }
                
                if(k != 0) {
                    Ext.getCmp(checkedProfileId).setValue(true);
                }
            }

		}
	}

});