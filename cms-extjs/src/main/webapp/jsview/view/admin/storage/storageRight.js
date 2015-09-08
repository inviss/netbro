storageRight = function () {

	return {
		init: function () {

			var fp = Ext.create('Ext.form.Panel', {

				id: 'storageForm',
				layout: 'vbox',
				width: 'auto',
				title: '입력창',
				margin: '0 5 0 5',
				renderTo: 'right',
				items: [{
					xtype: 'fieldset',
					title: '스토리지 정보',
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
						xtype: 'textfield',
						name: 'totalVolume',
						id: 'totalVolume',
						align: 'center',
						margin: '5 5 5 5',
						fieldLabel: '전체 용량',
						labelSeparator : '',
						allowBlank: false,
						minLength: 2,
						disabled: true,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'useVolume',
						id: 'useVolume',
						fieldLabel: '사용 용량',
						labelSeparator : '',
						margin: '5 5 5 5',
						disabled: true,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'idleVolume',
						id: 'idleVolume',
						fieldLabel: '가용 용량',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
						disabled: true,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'storagePath',
						id: 'storagePath',
						fieldLabel: '스토리지 경로',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
						minLength: 3,
						disabled: true,
					}, {
						width: '80%',
						xtype: 'radiogroup',
						fieldLabel: '스토리지구분',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
						disabled: true,
						items: [{
							xtype: 'radiofield',
							boxLabel: '고용량(H)',
							name: 'storageGubun',
							inputValue: 'H'
						}, {
							xtype: 'radiofield',
							boxLabel: '저용량(L)',
							name: 'storageGubun',
							inputValue: 'L'
						}, ]

					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'limit',
						id: 'limit',
						fieldLabel: '임계치',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
						minLength: 2
					}, ]
				}],
				buttons: [{
					text: '저장',
					id: 'save',
					handler: function () {

						var chkLimit = Ext.getCmp('limit').getValue();

						if(chkLimit < 80) {
							Ext.MessageBox.alert('Failed', '임계치 최소값은 80입니다.');
							return;
						} else if(chkLimit > 100) {
							Ext.MessageBox.alert('Failed', '임계치 최대값은 100입니다.');
							return;
						}

						saveStorageStore.update({
							url: context + '/admin/storage/saveStorageInfo.ssc',
							action: 'update',
							params: {
								storageId: tmpStorageId,
								limit: Ext.getCmp('limit').getValue(),
							},
							callback: function (records, operation, success) {
								var jsonData = Ext.JSON.decode(operation.response.responseText);

								if(jsonData.result == "F") {
									Ext.MessageBox.alert('Failed', '에러입니다.');
									return;
								} else {
									Ext.MessageBox.alert('Success', '저장 되었습니다.');
									storageList.getStore().reload();
								}
							}
						})
					}
				}, {
					text: '초기화',
					id: 'reset',
					handler: function () {
						fp.getForm().reset();
					}
				}]
			});
		}
	}
}();