/**
 * 카테고리 리스트에서 클릭시 카테고리맵핑 정보의 체크박스에 표현되는 store.
 */
var getBusicategoryStore = Ext.create('Ext.data.Store', {
	model: 'BusicategoryModel',
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

			var tmpBusicategoryId = arrBusicategoryId.substring(1);            
			var tmpBusicategoryIdArr = tmpBusicategoryId.split(',');

			for(var i = 0; i < tmpBusicategoryIdArr.length; i++) {
				var k = 0;
				var checkedBusicategoryId;
				Ext.getCmp(tmpBusicategoryIdArr[i]).setValue(false);

				for(var j = 0; j < records.length; j++) {
					if(tmpBusicategoryIdArr[i] == t.proxy.reader.jsonData.busiPartnerCategories[j]) {
						k++;
						checkedBusicategoryId = t.proxy.reader.jsonData.busiPartnerCategories[j];
					}
				}

				if(k != 0) {
					Ext.getCmp(checkedBusicategoryId).setValue(true);
				}
			}

		}
	}

});