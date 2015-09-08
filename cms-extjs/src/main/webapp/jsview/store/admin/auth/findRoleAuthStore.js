checkboxArray = [];

var tmpMenu = "";
var menuArray;

/**
 * 라디오박스 권한 클릭시 (읽기,쓰기) enable
 */
function enableRadiogroup(){

	menuArray = tmpMenu.split(',');

	for(var i = 7; i<menuArray.length; i++){
		Ext.getCmp(menuArray[i]+'-').enable();
	}
}

/**
 * 라디오박스 권한 클릭시 (제한) disable
 */
function disableRadiogroup(){

	menuArray = tmpMenu.split(',');

	for(var i = 7; i<menuArray.length; i++){
		Ext.getCmp(menuArray[i]+'-').disable();
	}
}

//권한 역활 checkbox
var roleAuthCheckbox = Ext.create('Ext.panel.Panel', {
	columns: 1,
	border: false, 
});

var findRoleAuthStore = Ext.create('Ext.data.Store', {
	model: 'MenuModel',
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
				root: 'menuTbls'
			}
	},
	autoLoad: false,
	autoSync: true,
	listeners: {
		load: function (t, records, options) {
			console.log(t)
			console.log(records)
			for(var i = 0; i < records.length; i++) {
				if(records[i].data.menuNm != 'CMS') {
					
					tmpMenu += ','+records[i].data.menuEnNm;
					
					checkboxArray.push({
						xtype: 'radiogroup',
						fieldLabel: records[i].data.menuNm,
//						fieldLabel : '<span style="color: rgb(255, 0, 0); padding-left: 2px;">'+records[i].data.menuNm + '</span>',
						id : records[i].data.menuEnNm+'-',
//						labelCls: 'x-check-group-alt',
//						labelStyle:'background-color: aliceblue',
						labelSeparator : '',
						width : '100%',
						listeners: {
							change : function(obj, value){
								
								if(this.getValue().admin == "R"){
									/**
									 * tmpAuthId 값이있다는것은 authList를 클릭하였다는 뜻.
									 * authList를 click후 아래 logic 타면 error.
									 */
									if(!tmpAuthId){
										Ext.getCmp("category-").setValue({category:"R"});
										Ext.getCmp("part-").setValue({part:"R"});
										Ext.getCmp("user-").setValue({user:"R"});
										Ext.getCmp("code-").setValue({code:"R"});
										Ext.getCmp("storage-").setValue({storage:"R"});
										Ext.getCmp("equip-").setValue({equip:"R"});
										Ext.getCmp("notice-").setValue({notice:"R"});
										Ext.getCmp("profile-").setValue({profile:"R"});
										Ext.getCmp("busipartner-").setValue({busipartner:"R"});
										Ext.getCmp("busicategory-").setValue({busicategory:"R"});

										enableRadiogroup();
									}

								}else if(this.getValue().admin == "RW"){
									/**
									 * tmpAuthId 값이있다는것은 authList를 클릭하였다는 뜻.
									 * authList를 click후 아래 logic 타면 error.
									 */
									if(!tmpAuthId){
										Ext.getCmp("category-").setValue({category:"RW"});
										Ext.getCmp("part-").setValue({part:"RW"});
										Ext.getCmp("user-").setValue({user:"RW"});
										Ext.getCmp("code-").setValue({code:"RW"});
										Ext.getCmp("storage-").setValue({storage:"RW"});
										Ext.getCmp("equip-").setValue({equip:"RW"});
										Ext.getCmp("notice-").setValue({notice:"RW"});
										Ext.getCmp("profile-").setValue({profile:"RW"});
										Ext.getCmp("busipartner-").setValue({busipartner:"RW"});
										Ext.getCmp("busicategory-").setValue({busicategory:"RW"});

										enableRadiogroup();
									}

								}else if(this.getValue().admin == "L"){
									/**
									 * tmpAuthId 값이있다는것은 authList를 클릭하였다는 뜻.
									 * authList를 click후 아래 logic 타면 error.
									 */
									if(!tmpAuthId){
										
										Ext.getCmp("category-").setValue({category:"L"});
										Ext.getCmp("part-").setValue({part:"L"});
										Ext.getCmp("user-").setValue({user:"L"});
										Ext.getCmp("code-").setValue({code:"L"});
										Ext.getCmp("storage-").setValue({storage:"L"});
										Ext.getCmp("equip-").setValue({equip:"L"});
										Ext.getCmp("notice-").setValue({notice:"L"});
										Ext.getCmp("profile-").setValue({profile:"L"});
										Ext.getCmp("busipartner-").setValue({busipartner:"L"});
										Ext.getCmp("busicategory-").setValue({busicategory:"L"});

										disableRadiogroup();
									}

								}
							}
						},
						items: [{
							boxLabel: '읽기',
							name: records[i].data.menuEnNm,
							inputValue: 'R',
							width: '100%',
						}, {
							boxLabel: '쓰기',
							name: records[i].data.menuEnNm,
							inputValue: 'RW',
							width: '100%',
						}, {
							boxLabel: '제한',
							name: records[i].data.menuEnNm,
							inputValue: 'L',
							width: '100%',
						},

						]
					});
				}
			}
			roleAuthCheckbox.add(checkboxArray);
		}
	}
});


