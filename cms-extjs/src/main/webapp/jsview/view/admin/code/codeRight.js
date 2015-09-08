var tmpUrl;
var win;

codeRight = function () {

	return {
		init: function () {

			/**
			 * 분류코드명
			 */
			var te = findClfStore.load({
				url: context + '/admin/code/findCodeClfList.ssc',
				action: 'read'
			});

			/**
			 *
			 */
			var clfGubun = new Ext.data.ArrayStore({
				fields: ['value', 'key'],
				data: [
				       ['', '선택하세요'],
				       ['U', '유저코드'],
				       ['S', '시스템코드'],
				       ]
			});

			/**
			 * 분류코드 생성방식
			 */
			var createWay = new Ext.data.ArrayStore({
				fields: ['value', 'key'],
				data: [
				       ['', '선택하세요'],
				       ['auto', '자동'],
				       ['manual', '수동'],
				       ]
			});

			var fp = Ext.create('Ext.form.Panel', {

				id: 'codeForm',
				layout: 'vbox',
				width: 'auto',
				title: '입력창',
				margin: '0 5 0 5',
				renderTo: 'right',
				viewConfig: {

				},
				items: [{
					xtype: 'fieldset',
					title: '장비 정보',
					margin: '5 5 5 5',
					collapsible: false,
					width: '100%',
					listeners: {
						afterrender: function (view, eOpts) {
							//초반 그리드 화면의 버튼을 권한에 따라 활성화 비활성화 한다.
							var saveButton = Ext.ComponentQuery.query('#save');
							var resetButton = Ext.ComponentQuery.query('#reset');
							var addButton = Ext.ComponentQuery.query('#add');
							console.log(saveButton)
							if(perm != 'RW') {
								//saveButton[0].hide(true);
								saveButton[0].setDisabled(true);
								resetButton[0].setDisabled(true);
								addButton[0].setDisabled(true);
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
						fieldLabel: '코드 생성방식',
						labelSeparator : '',
						width: '100%',
						margin: '5 5 5 5',
						xtype: 'combobox',
						name: 'createWay',
						id: 'createWay',
						store: createWay,
						valueField: 'value',
						displayField: 'key',
						editable: false,
						value: createWay.getAt(0).get('value'),
						listeners: {
							change: function (field, newValue, oldValue) {
								this.newValue = newValue;
								if(this.newValue == 'auto') {
									Ext.getCmp('clfCd').disable();
								} else {
									Ext.getCmp('clfCd').enable();
								}
							}
						}
					}, {
						fieldLabel: '코드유형',
						labelSeparator : '',
						width: '100%',
						margin: '5 5 5 5',
						xtype: 'combobox',
						name: 'clfGubun',
						id: 'clfGubun',
						store: clfGubun,
						valueField: 'value',
						displayField: 'key',
						editable: false,
						value: clfGubun.getAt(0).get('value'),
						listeners: {
							change: function (field, newValue, oldValue) {
								this.newValue = newValue;
							}
						}

					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'clfCd',
						id: 'clfCd',
						fieldLabel: '분류코드',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						fieldLabel: '분류코드명',
						labelSeparator : '',
						width: '100%',
						margin: '5 5 5 5',
						xtype: 'textfield',
						name: 'clfNm',
						id: 'clfNm',
						editable: false,
						valueField: 'clfCd',
						displayField: 'clfNm',
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'sclCd',
						id: 'sclCd',
						fieldLabel: '상세코드',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
						disabled: true,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'sclNm',
						id: 'sclNm',
						fieldLabel: '상세코드명',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
						disabled: true,
					}, {
						width: '80%',
						xtype: 'textareafield',
						name: 'codeCont',
						id: 'codeCont',
						fieldLabel: '코드설명',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
						disabled: true,
					}, {
						width: '80%',
						xtype: 'radiogroup',
						fieldLabel: '사용여부',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
						disabled: true,
						itemId: 'useYn',
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
				}],
				buttons: [{
					text: '저장',
					id: 'save',
					handler: function () {

						if(!tmp) {
							tmpUrl = context + '/admin/code/insertClfInfo.ssc'
						} else {
							tmpUrl = context + '/admin/code/updateCodeInfo.ssc'
						}

						if(!tmp) {
							if(!Ext.getCmp('createWay').getValue()) {
								Ext.MessageBox.alert('Failed', '코드생성방식을 입력하세요.')
								return;
							}
						}
						if(!tmp) {
							if(!fp.getForm().getValues()['clfGubun']) {
								Ext.MessageBox.alert('Failed', '코드유형을 입력하세요.')
								return;
							}

							if(Ext.getCmp('createWay').getValue() != 'auto') {
								if(!Ext.getCmp('clfCd').getValue()) {
									Ext.MessageBox.alert('Failed', '분류코드를 입력하세요.')
									return;
								}
							}
						}

						saveCodeStore.update({
							url: tmpUrl,
							action: 'update',
							params: {
								createWay: Ext.getCmp('createWay').getValue(),
								clfGubun: Ext.getCmp('clfGubun').getValue(),
								clfCD: Ext.getCmp('clfCd').getValue(),
								clfNM: Ext.getCmp('clfNm').getValue(),
								sclNm: Ext.getCmp('sclNm').getValue(),
								sclCd: Ext.getCmp('sclCd').getValue(),
								codeCont: Ext.getCmp('codeCont').getValue(),
								useYn: fp.getForm().getValues()['useYn']

							},
							callback: function (records, operation, success) {
								var jsonData = Ext.JSON.decode(operation.response.responseText);

								if(jsonData.result == "F") {
									Ext.MessageBox.alert('Failed', '에러입니다.');
									return;
								} else {
									Ext.MessageBox.alert('Success', '저장 되었습니다.');
									codeList.getStore().reload();
								}
							}
						})

					}
				}, {
					text: '추가',
					id: 'add',
					disabled: true,
					handler: function () {

						Ext.getCmp('save').disable();
						Ext.getCmp('reset').disable();
						Ext.getCmp('add').disable();

						win = Ext.create('Ext.window.Window', {
							title: '코드 추가',
							height: 230,
							width: 300,
							resizable: false,
							hidden: true,
							closable: false,
							items: [{
								width: 'auto',
								xtype: 'combobox',
								name: 'inputClfCd',
								id: 'inputClfCd',
								queryMode: 'local',
								fieldLabel: '분류 코드명',
								margin: '5 5 5 5',
								allowBlank: false,
								store: findClfStore,
								valueField: 'clfCd',
								displayField: 'clfNm',
								editable: false,
							}, {
								width: 'auto',
								xtype: 'textfield',
								name: 'inputSclCd',
								id: 'inputSclCd',
								fieldLabel: '상세 코드명',
								margin: '5 5 5 5',
								allowBlank: false,
							}, {

								width: 'auto',
								xtype: 'textareafield',
								name: 'inputCodeCont',
								id: 'inputCodeCont',
								fieldLabel: '코드설명',
								margin: '5 5 5 5',

							}],
							buttons: [{
								text: '저장',
								handler: function () {

									if(!Ext.getCmp('inputClfCd').getValue()) {
										Ext.MessageBox.alert('Failed', '분류코드명을 입력하세요.');
										return;
									}

									if(!Ext.getCmp('inputSclCd').getValue()) {
										Ext.MessageBox.alert('Failed', '상세코드명을 입력하세요.');
										return;
									}

									Ext.Ajax.request({
										url: context + '/admin/code/insertCodeInfo.ssc',
										method: 'post',
										params: {
											clfCD: Ext.getCmp('clfCd').getValue(),
											sclCd: Ext.getCmp('sclCd').getValue(),
											clfNM: Ext.getCmp('inputClfCd').getValue(),
											sclNm: Ext.getCmp('inputSclCd').getValue(),
											codeCont: Ext.getCmp('inputCodeCont').getValue(),
										},
										success: function (result, request) {
											var jsonData = Ext.JSON.decode(result.responseText);
											if(jsonData.result == "F") {
												Ext.MessageBox.alert('Failed', '');
												return;
											} else if(jsonData.result == "E") {
												Ext.MessageBox.alert('Failed', '');
												return;

											} else {
												codeList.getStore().reload();
												Ext.MessageBox.alert('Success', '저장 되었습니다.');
												Ext.getCmp('save').enable();
												Ext.getCmp('reset').enable();
												win.close();
											}

										},
										failure: function (result, request) {
											Ext.MessageBox.alert('Failed', 'Error');
										},
									});
								}

							}, {
								text: '닫기',
								id: 'close',
								handler: function () {
									win.close();
									Ext.getCmp('save').enable();
									Ext.getCmp('reset').enable();
									Ext.getCmp('add').enable();
								}
							}]

						})
						win.show();
					}
				}, {
					text: '초기화',
					id: 'reset',
					handler: function () {
						fp.getForm().reset();
						Ext.getCmp('createWay').enable();
						Ext.getCmp('clfGubun').enable();

						Ext.getCmp('clfCd').enable();
						Ext.getCmp('clfNm').enable();

						Ext.getCmp('sclCd').disable();
						Ext.getCmp('sclNm').disable();
						Ext.getCmp('codeCont').disable();

						Ext.getCmp('add').disable();

						var useYn = Ext.ComponentQuery.query('#useYn');
						useYn[0].setDisabled(true);

						tmp = "";

					}
				}]
			});
		}
	}
}();