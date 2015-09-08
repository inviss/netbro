var controlGubun = [];
var fp;
var tmpUrl;

authRight = function () {

	return {
		init: function () {

			findRoleAuthStore.load({
				url: context + '/admin/auth/findMenuList.ssc',
				action: 'read',
			});

			fp = Ext.create('Ext.form.Panel', {

				store: findRoleAuthStore,

				id: 'authForm',
				layout: 'vbox',
				width: 'auto',
				title: '권한정보',
				margin: '0 5 0 5',
				renderTo: 'right',
				items: [{
					xtype: 'fieldset',
					title: '역활 정보',
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
						name: 'authNm',
						id: 'authNm',
						align: 'center',
						margin: '5 5 5 5',
						fieldLabel: '역활명',
						labelSeparator : '',
						allowBlank: false,
						minLength: 2
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'authSubNm',
						id: 'authSubNm',
						fieldLabel: '역활설명',
						labelSeparator : '',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'radiogroup',
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
				}, {
					xtype: 'fieldset',
					title: '권한 정보',
					margin: '5 5 5 5',
					collapsible: false,
					width: '100%',
					cls:'customFormBackground',
//					labelStyle:'background-color: aliceblue',
					defaults: {
						anchor: '100%',
						//autoScroll : true,
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
					items: roleAuthCheckbox
				}],
				buttons: [{
					text: '저장',
					id: 'save',
					handler: function () {

						if(!Ext.getCmp('authNm').getValue()) {
							Ext.MessageBox.alert('Failed', '역활명을 입력하세요.')
							return;
						}

						if(!Ext.getCmp('authSubNm').getValue()) {
							Ext.MessageBox.alert('Failed', '역활설명을 입력하세요.')
							return;
						}

						if(!fp.getForm().getValues()['useYn']) {
							Ext.MessageBox.alert('Failed', '사용여부를 입력하세요.')
							return;
						}

						menuArray = tmpMenu.split(',');

						for(var i = 1; i < menuArray.length; i++) {
							controlGubun += fp.getForm().getValues()[menuArray[i]] + ",";
							if(!fp.getForm().getValues()[menuArray[i]]) {
								Ext.MessageBox.alert('Failed', '권한정보을 입력하세요.')
								return;
							}
						}

						if(!tmpAuthId) {
							tmpUrl = context + '/admin/auth/saveAuthInfo.ssc'
						} else {
							tmpUrl = context + '/admin/auth/updateAuthInfo.ssc'
						}

						saveAuthStore.update({
							url: tmpUrl,
							action: 'update',
							params: {
								authNm: Ext.getCmp('authNm').getValue(),
								authSubNm: Ext.getCmp('authSubNm').getValue(),
								useYn: fp.getForm().getValues()['useYn'],
								authId: tmpAuthId,
								controlGubun: controlGubun,
							},
							callback: function (records, operation, success) {
								var jsonData = Ext.JSON.decode(operation.response.responseText);

								if(jsonData.result == "F") {
									Ext.MessageBox.alert('Failed', '에러입니다.');
									controlGubun = "";
									return;
								} else {
									Ext.MessageBox.alert('Success', '저장 되었습니다.');
									authList.getStore().reload();
									controlGubun = "";
								}
							}
						})
					}
				}, {
					text: '초기화',
					id: 'reset',
					handler: function () {
						fp.getForm().reset();
						tmpAuthId = "";
					}
				}]
			});
		}
	}
}();