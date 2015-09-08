busipartnerRight = function () {

	return {
		init: function () {

			findProBusiStore.load({
				url: context + '/admin/profile/findProfileList.ssc',
				action: 'read',
				params: {
					'pageNo': 1
				}
			});

			var fp = Ext.create('Ext.form.Panel', {

				store: findProBusiStore,

				id: 'busipartnerForm',
				layout: 'vbox',
				width: 'auto',
				title: '서비스 정보',
				margin: '0 5 0 5',
				renderTo: 'right',
				items: [{
					xtype: 'fieldset',
					title: '사업자 정보',
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
						name: 'company',
						id: 'company',
						align: 'center',
						margin: '5 5 5 5',
						fieldLabel: '업체명',
						allowBlank: false,
						minLength: 2
					}, {
						width: '80%',
						xtype: 'radiogroup',
						fieldLabel: '사용여부',
						margin: '5 5 5 5',
						allowBlank: false,
						items: [{
							xtype: 'radiofield',
							boxLabel: '사용',
							name: 'servYn',
							inputValue: 'Y'
						}, {
							xtype: 'radiofield',
							boxLabel: '미사용',
							name: 'servYn',
							inputValue: 'N'
						}]
					}, {
						width: '80%',
						xtype: 'radiogroup',
						fieldLabel: 'FTP서비스 여부',
						margin: '5 5 5 5',
						allowBlank: false,
						items: [{
							xtype: 'radiofield',
							boxLabel: '사용',
							name: 'ftpServYn',
							inputValue: 'Y'
						}, {
							xtype: 'radiofield',
							boxLabel: '미사용',
							name: 'ftpServYn',
							inputValue: 'N'
						}, ]
					}, {
						width: '80%',
						xtype: 'radiogroup',
						fieldLabel: '서비스URL 전달',
						margin: '5 5 5 5',
						allowBlank: false,
						items: [{
							xtype: 'radiofield',
							boxLabel: '사용',
							name: 'srvUrl',
							inputValue: 'Y'
						}, {
							xtype: 'radiofield',
							boxLabel: '미사용',
							name: 'srvUrl',
							inputValue: 'N'
						}, ]
					}, {
						width: '80%',
						xtype: 'radiogroup',
						fieldLabel: 'ContentML 전달',
						margin: '5 5 5 5',
						allowBlank: false,
						items: [{
							xtype: 'radiofield',
							boxLabel: '사용',
							name: 'srvUrl1',
							inputValue: 'Y'
						}, {
							xtype: 'radiofield',
							boxLabel: '미사용',
							name: 'srvUrl1',
							inputValue: 'N'
						}, ]
					}, {
						width: '80%',
						xtype: 'radiogroup',
						fieldLabel: 'Smil 전달',
						margin: '5 5 5 5',
						allowBlank: false,
						items: [{
							xtype: 'radiofield',
							boxLabel: '사용',
							name: 'srvUrl2',
							inputValue: 'Y'
						}, {
							xtype: 'radiofield',
							boxLabel: '미사용',
							name: 'srvUrl2',
							inputValue: 'N'
						}, ]
					}]
				}, {
					width: '100%',
					title: 'FTP 정보',
					border: false,
				}, {
					xtype: 'fieldset',
					title: 'FTP 정보',
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
						name: 'ip',
						id: 'ip',
						fieldLabel: 'IP',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'port',
						id: 'port',
						fieldLabel: 'PORT',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'ftpId',
						id: 'ftpId',
						fieldLabel: 'FTP ID',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'password',
						id: 'password',
						fieldLabel: 'PASSWORD',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'radiogroup',
						fieldLabel: '전송방식',
						margin: '5 5 5 5',
						allowBlank: false,
						items: [{
							xtype: 'radiofield',
							boxLabel: 'Passive Mode',
							name: 'transMethod',
							inputValue: 'P'
						}, {
							xtype: 'radiofield',
							boxLabel: 'Active Mode',
							name: 'transMethod',
							inputValue: 'A'
						}, ]
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'remoteDir',
						id: 'remoteDir',
						fieldLabel: '사용자경로',
						margin: '5 5 5 5',
						allowBlank: false,
					}]
				}, {
					width: '100%',
					columns: 1,
					title: '프로파일 정보',
					border: false,
				}, {
					xtype: 'fieldset',
					title: '프로파일 정보',
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
					items: proBusiCheckbox
				}],
				buttons: [{
					text: '저장',
					id: 'save',
					handler: function () {

						var proFlId = fp.getForm().getValues()['proFlId'];

						if(!tmpbusiPartnerId) {
							tmpUrl = context + '/admin/business/saveBusiInfo.ssc'
						} else {
							tmpUrl = context + '/admin/business/updateBusiInfo.ssc'
						}

						if(!Ext.getCmp('company').getValue()) {
							Ext.MessageBox.alert('Failed', '업체명을 입력하세요.')
							return;
						}

						if(!fp.getForm().getValues()['servYn']) {
							Ext.MessageBox.alert('Failed', '사용여부를 입력하세요.')
							return;
						}

						if(!fp.getForm().getValues()['ftpServYn']) {
							Ext.MessageBox.alert('Failed', 'FTP서비스 여부를 입력하세요.')
							return;
						}

						if(!fp.getForm().getValues()['srvUrl']) {
							Ext.MessageBox.alert('Failed', '서비스URL를 입력하세요.')
							return;
						}

						if(!Ext.getCmp('ip').getValue()) {
							Ext.MessageBox.alert('Failed', 'ip를 입력하세요.')
							return;
						}

						if(!Ext.getCmp('port').getValue()) {
							Ext.MessageBox.alert('Failed', 'port 입력하세요.')
							return;
						}

						if(!Ext.getCmp('ftpId').getValue()) {
							Ext.MessageBox.alert('Failed', 'FTPID를 입력하세요.')
							return;
						}

						if(!Ext.getCmp('password').getValue()) {
							Ext.MessageBox.alert('Failed', 'password를 입력하세요.')
							return;
						}

						if(!fp.getForm().getValues()['transMethod']) {
							Ext.MessageBox.alert('Failed', '전송방식을 입력하세요.')
							return;
						}

						if(!Ext.getCmp('remoteDir').getValue()) {
							Ext.MessageBox.alert('Failed', '사용자경로를 입력하세요.')
							return;
						}

						saveBusipartnerStore.update({
							url: tmpUrl,
							action: 'update',
							params: {
								company: Ext.getCmp('company').getValue(),
								servYn: fp.getForm().getValues()['servYn'],
								ftpServYn: fp.getForm().getValues()['ftpServYn'],
								srvUrl: fp.getForm().getValues()['srvUrl'],
								ip: Ext.getCmp('ip').getValue(),
								port: Ext.getCmp('port').getValue(),
								ftpId: Ext.getCmp('ftpId').getValue(),
								password: Ext.getCmp('password').getValue(),
								transMethod: fp.getForm().getValues()['transMethod'],
								remoteDir: Ext.getCmp('remoteDir').getValue(),
								tmpProflId: '' + proFlId + ''
								//busiPartnerId: tmpbusiPartnerId
							},
							callback: function (records, operation, success) {
								var jsonData = Ext.JSON.decode(operation.response.responseText);

								if(jsonData.result == "F") {
									Ext.MessageBox.alert('Failed', '에러입니다.');
									return;
								} else {
									Ext.MessageBox.alert('Success', '저장 되었습니다.');
									busipartnerList.getStore().reload();
								}
							}
						})
					}
				}, {
					text: '초기화',
					id: 'reset',
					handler: function () {
						fp.getForm().reset();
						tmpbusiPartnerId = "";
					}
				}]
			});
		}
	}
}();