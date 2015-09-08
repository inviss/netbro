var tmpUrl = "";

profileRight = function () {

	return {
		init: function () {

			var priority = new Ext.data.ArrayStore({
				fields: ['key', 'priority'],
				data: [
				       ['1', '1'],
				       ['2', '2'],
				       ['3', '3'],
				       ['4', '4'],
				       ['5', '5'],
				       ['6', '6'],
				       ['7', '7'],
				       ['8', '8'],
				       ['9', '9'],
				       ['10', '10']
				       ]
			});

			var fp = Ext.create('Ext.form.Panel', {

				id: 'profileForm',
				layout: 'vbox',
				width: 'auto',
				title: '입력창',
				margin: '0 5 0 5',
				renderTo: 'right',
				items: [{
					xtype: 'fieldset',
					title: '프로파일 정보',
					margin: '5 5 5 5',
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
						xtype: 'textfield',
						name: 'proFlnm',
						id: 'proFlnm',
						align: 'center',
						margin: '5 5 5 5',
						fieldLabel: '이름',
						allowBlank: false,
						minLength: 2
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'flNameRule',
						id: 'flNameRule',
						fieldLabel: '파일네임규칙',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'vdoCodec',
						id: 'vdoCodec',
						fieldLabel: '코덱',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'vdoBitRate',
						id: 'vdoBitRate',
						fieldLabel: '비트레이트',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'vdoFS',
						id: 'vdoFS',
						fieldLabel: 'F/S',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'vdoSync',
						id: 'vdoSync',
						fieldLabel: '종횡맞춤',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'ext',
						id: 'ext',
						fieldLabel: '확장자',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'servBit',
						id: 'servBit',
						fieldLabel: '서비스Bit',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'keyFrame',
						id: 'keyFrame',
						fieldLabel: 'Key프레임',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						fieldLabel: '우선순위',
						width: '100%',
						xtype: 'combobox',
						name: 'priority',
						margin: '5 5 5 5',
						store: priority,
						valueField: 'key',
						displayField: 'priority',
						value: priority.getAt(0).get('priority'),
						listeners: {
							change: function (field, newValue, oldValue) {
								this.newValue = newValue;
							}
						}
					}, {
						xtype: 'radiogroup',
						fieldLabel: '사용여부',
						margin: '0 5 5 5',
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
				}],
				buttons: [{
					text: '저장',
					id: 'save',
					handler: function () {

						if(!tmpProFlId) {
							tmpUrl = '/admin/profile/saveProfileInfo.ssc'
						} else {
							tmpUrl = '/admin/profile/updateProfileInfo.ssc'
						}
						
						saveProfileStore.update({
							url: tmpUrl,
							action: 'update',
							params: {
								proFlId: tmpProFlId,
								proFlnm: Ext.getCmp('proFlnm').getValue(),
								flNameRule: Ext.getCmp('flNameRule').getValue(),
								vdoCodec: Ext.getCmp('vdoCodec').getValue(),
								vdoBitRate: Ext.getCmp('vdoBitRate').getValue(),
								vdoFS: Ext.getCmp('vdoFS').getValue(),
								vdoSync: Ext.getCmp('vdoSync').getValue(),
								ext: Ext.getCmp('ext').getValue(),
								servBit: Ext.getCmp('servBit').getValue(),
								keyFrame: Ext.getCmp('keyFrame').getValue(),
								priority: fp.getForm().getValues()['priority'],
								useYn: fp.getForm().getValues()['useYn'],
								regrId: loginUserId,
								modrId: loginUserId,
							},
							callback: function (records, operation, success) {
								var jsonData = Ext.JSON.decode(operation.response.responseText);

								if(jsonData.result == "F") {
									Ext.MessageBox.alert('Failed', '에러입니다.');
									return;
								} else {
									Ext.MessageBox.alert('Success', '저장 되었습니다.');
									profileList.getStore().reload();
								}
							}
						})
					}
				}, {
					text: '초기화',
					id: 'reset',
					handler: function () {
						fp.getForm().reset();
						tmpProFlId = "";
					}
				}]
			});
		}
	}
}();