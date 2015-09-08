var tmpProflNm = "";

var busipartnerCheckbox = Ext.create('Ext.form.CheckboxGroup', {
    columns: 1,
});

/**
 * 카테고리 맵핑 정보 store.
 */
var findBusicategoryStore = Ext.create('Ext.data.Store', {
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

            for(var i = 0; i < records.length; i++) {
                for(var z = 0; z < tmpBusiProNm.length; z++) {

                    if(records[i].data.busiPartnerId == tmpBusiProNm[z].busiPartnerId) {
                    	tmpProflNm += tmpBusiProNm[z].proFlNm + ', ';
                        checkboxArray.push({
                            xtype: 'checkboxfield',
                            id: records[i].data.busiPartnerId,
                            boxLabel: records[i].data.company + ' ( ' + tmpProflNm.substring(0, tmpProflNm.length - 2) + ' )',
                            name: 'busiPartnerId',
                            inputValue: records[i].data.busiPartnerId
                        });
                    }else{
                    	if(tmpProflNm.substring(0, tmpProflNm.length - 2) == ""){
                    		checkboxArray.push({
                                xtype: 'checkboxfield',
                                id: records[i].data.busiPartnerId,
                                boxLabel: records[i].data.company,
                                name: 'busiPartnerId',
                                inputValue: records[i].data.busiPartnerId
                            });
                    	}else{
                    		checkboxArray.push({
                                xtype: 'checkboxfield',
                                id: records[i].data.busiPartnerId,
                                boxLabel: records[i].data.company + ' ( ' + tmpProflNm.substring(0, tmpProflNm.length - 2) + ' )',
                                name: 'busiPartnerId',
                                inputValue: records[i].data.busiPartnerId
                            });
                    	}
                        
                    }

                }
                tmpProflNm = "";
                arrBusicategoryId += "," + records[i].data.busiPartnerId;
            }
            busipartnerCheckbox.add(checkboxArray);
        }
    }
});