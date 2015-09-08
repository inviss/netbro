equipRight = function () {

	return {
		init: function () {

			var tmp = "";
			var tmpUrl = "";
			var equipCheck = "";

			var equipKinds = new Ext.data.ArrayStore({
				fields: ['value', 'key'],
				data: [
				       ['TC', '트랜스코더'],
				       ['TS', '트랜스퍼'],
				       ]
			});

			

			var fp = Ext.create('Ext.form.Panel', {

				id: 'equipForm',
				layout: 'vbox',
				width: 'auto',
				title: '입력창',
				margin: '0 5 0 5',
				renderTo: 'right',
				items: [{
					xtype: 'fieldset',
					margin: '5 5 5 5',
					collapsible: false,
					width: '100%',
					listeners: {
						afterrender: function (view, eOpts) {
							//초반 그리드 화면의 버튼을 권한에 따라 활성화 비활성화 한다.
							var saveButton = Ext.ComponentQuery.query('#save');
							var resetButton = Ext.ComponentQuery.query('#reset');

							if(perm != 'RW') {
								saveButton[0].setDisabled(true);
								resetButton[0].setDisabled(true);
							}
						}
					},
					defaults: {
						anchor: '100%',
						layout: {
							type: 'hbox',
							defaultMargins: {
								top: 5,
								right: 5,
								bottom: 5,
								left: 5
							},
						}
					},
					items: [{
						width: '80%',
						xtype: 'combobox',
						name: 'tmpEquipKinds',
						id: 'tmpEquipKinds',
						store: equipKinds,
						editable: false,
						align: 'center',
						margin: '5 5 5 5',
						fieldLabel: '장비선택',
						labelSeparator : '',
						allowBlank: false,
						valueField: 'value',
						displayField: 'key',
						listeners: {
							change: function(a,b,c,d) {  
								Ext.getCmp('equipAdd_').enable();
								//장비선택에서 인스턴스 추가 클릭
								//인스턴스 정보의 인스턴스추가장비에 트랜스코더,트랜스퍼로 바뀌게 selectbox로
								//각각의 트랜스코더,트랜스퍼로 맞게 화면에 표현됨.
								tmp = b;
								Ext.getCmp('instanceAdd').reset();
								Ext.getCmp('equipAdd_').enable();
							},
							select: function(combo, records) {
								
								var comboBox = Ext.ComponentQuery.query('#instanceAdd');
								
								if(tmp == "TC"){
									comboBox[0].bindStore(traKinds);
								}else{
									comboBox[0].bindStore(trsKinds);
								}
							}
						}
					},{
						width: '80%',
						xtype: 'radiogroup',
						margin: '5 5 5 100',
						allowBlank: false,
						disabled:true,
						id:'equipAdd_',
						items: [{
							xtype: 'radiofield',
							boxLabel: '장비 추가',
							name: 'equipAdd',
							inputValue: 'E'
						}, {
							xtype: 'radiofield',
							boxLabel: '인스턴스 추가',
							name: 'equipAdd',
							inputValue: 'I'
						}],
						listeners: {
							change: function(a,b,c,d) {  
								if(b.equipAdd == 'E'){
									Ext.getCmp('instanceAdd').disable();
									Ext.getCmp('deviceNm').enable();
									Ext.getCmp('deviceIp').enable();
									Ext.getCmp('devicePort').enable();
									Ext.getCmp('useYn_').enable();
									Ext.getCmp('instanceAdd').reset();

									equipCheck = "";

								}else{
									Ext.getCmp('instanceAdd').enable();
									Ext.getCmp('deviceNm').disable();
									Ext.getCmp('deviceIp').disable();
									Ext.getCmp('devicePort').disable();
									Ext.getCmp('useYn_').disable();

									equipCheck = "checked";
								}
							}
						}
					}]
				},{
					xtype: 'fieldset',
					title: '장비 정보',
					margin: '5 5 5 5',
					collapsible: false,
					width: '100%',
					defaults: {
						anchor: '100%',
						layout: {
							type: 'hbox',
							defaultMargins: {
								top: 5,
								right: 5,
								bottom: 5,
								left: 5
							},
						}
					},
					items: [{
						width: '80%',
						xtype: 'textfield',
						name: 'deviceNm',
						id: 'deviceNm',
						fieldLabel: '장비명',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'deviceIp',
						id: 'deviceIp',
						fieldLabel: 'IP',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'devicePort',
						id: 'devicePort',
						fieldLabel: 'PORT',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'radiogroup',
						id:'useYn_',
						fieldLabel: '사용여부',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
						items: [{
							xtype: 'radiofield',
							boxLabel: '사용',
							name: 'useYn',
							inputValue: 'Y'
						}, {
							xtype: 'radiofield',
							boxLabel: '미사용',
							name: 'useYn',
							inputValue: 'N'
						}, ]
					}]
				},{
					xtype: 'fieldset',
					title: '인스턴스 정보',
					labelSeparator : '',
					margin: '5 5 5 5',
					collapsible: false,
					width: '100%',
					defaults: {
						anchor: '100%',
						layout: {
							type: 'hbox',
							defaultMargins: {
								top: 5,
								right: 5,
								bottom: 5,
								left: 5
							},
						}
					},
					items: [{
						width: '80%',
						xtype: 'combobox',
						name: 'instanceAdd',
						id: 'instanceAdd',
						align: 'center',
						margin: '5 5 5 5', 
						valueField: 'value',
						displayField: 'key',
						fieldLabel: '인스턴스 추가장비',
						labelSeparator : '',
						allowBlank: false,
						disabled:true,
						minLength: 2,

					}]
				}],
				buttons: [{
					text: '저장',
					id: 'save',
					handler: function () {
						
						if(equipCheck != 'checked'){
							
							if(!Ext.getCmp('tmpEquipKinds').getValue()) {
								Ext.MessageBox.alert('Failed', '장비선택을 입력하세요.');
								return;
							}

							if(!Ext.getCmp('deviceNm').getValue()) {
								Ext.MessageBox.alert('Failed', '장비명을 입력하세요.');
								return;
							}

							if(!Ext.getCmp('deviceIp').getValue()) {
								Ext.MessageBox.alert('Failed', '장비IP를 입력하세요.');
								return;
							}

							if(!Ext.getCmp('devicePort').getValue()) {
								Ext.MessageBox.alert('Failed', '장비PORT를 입력하세요.');
								return;
							}

							if(!fp.getForm().getValues()['useYn']) {
								Ext.MessageBox.alert('Failed', '사용여부를 입력하세요.')
								return;
							}
							
							if(!tmpDeviceId) {
								tmpUrl = context + '/admin/equipment/saveEquipInfo.ssc'
							} else {
								tmpUrl = context + '/admin/equipment/updateEquipInfo.ssc'
							}

						}else{
							tmpUrl = context + '/admin/equipment/saveEquipInstance.ssc'
						}
						
	

						saveEquipStore.update({
							url: tmpUrl,
							action: 'update',
							params: {
								deviceId: tmpDeviceId,
								deviceNum: tmpDeviceNum,
								equipKinds: fp.getForm().getValues()['tmpEquipKinds'],
								deviceNm: Ext.getCmp('deviceNm').getValue(),
								deviceIp: Ext.getCmp('deviceIp').getValue(),
								devicePort: Ext.getCmp('devicePort').getValue(),
								equipAdd: fp.getForm().getValues()['equipAdd'],
								instanceAdd: fp.getForm().getValues()['instanceAdd'],
								useYn: fp.getForm().getValues()['useYn']
							},
							callback: function (records, operation, success) {
								
								var jsonData = Ext.JSON.decode(operation.response.responseText);

								if(jsonData.result == "F") {
									Ext.MessageBox.alert('Failed', '에러입니다.');
									return;
								} else {
									Ext.MessageBox.alert('Success', '저장 되었습니다.');
									equipList.getStore().reload();
								}
							}
						})
					}
				}, {
					text: '초기화',
					id: 'reset',
					handler: function () {
						fp.getForm().reset();
						Ext.getCmp('equipAdd_').disable();
						Ext.getCmp('instanceAdd').disable();
						Ext.getCmp('deviceNm').enable();
						Ext.getCmp('deviceIp').enable();
						Ext.getCmp('devicePort').enable();
						Ext.getCmp('useYn_').enable();
						tmp = "";
					}
				}]
			});
		}
	}
}();