var fp;
var tmpUrl;

noticeRight = function () {

	return {
		init: function () {

			var popup = new Ext.data.ArrayStore({
				fields: ['key', 'popup'],
				data: [
				       ['Y', '사용'],
				       ['N', '미사용']
				       ]
			});

			fp = Ext.create('Ext.form.Panel', {

				id: 'noticeForm',
				layout: 'vbox',
				width: 'auto',
				title: '입력창',
				margin: '0 5 0 5',
				renderTo: 'right',
				items: [{
					xtype: 'fieldset',
					title: '공지사항 정보',
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
						name: 'title',
						id: 'title',
						align: 'center',
						margin: '5 5 5 5',
						fieldLabel: '제목',
						allowBlank: false,
						minLength: 2
					}, {
						width: '80%',
						xtype: 'textareafield',
						name: 'cont',
						autoScroll: true,
						id: 'cont',
						fieldLabel: '내용',
						margin: '5 5 5 5',
						allowBlank: false,
						minLength: 6
					}, {
						width: '80%',
						xtype: 'combobox',
						name: 'popUpYn',
						id: 'popUpYn',
						fieldLabel: '팝업여부',
						store: popup,
						editable: false,
						valueField: 'key',
						displayField: 'popup',
						margin: '5 5 5 5',
					}, {
						margin: '5 5 5 5',
						name: 'startDd',
						id: 'startDd',
						xtype: 'datefield',
						fieldLabel: '시작일',
						flex: 1,
						format: 'Y-m-d',
						allowBlank: false
					}, {
						margin: '5 5 5 5',
						name: 'endDd',
						fieldLabel: '종료일',
						id: 'endDd',
						xtype: 'datefield',
						flex: 1,
						format: 'Y-m-d',
						allowBlank: false

					}]
				}],
				buttons: [{
					text: '저장',
					id: 'save',
					handler: function () {

						if(!tmpNoticeId) {
							tmpUrl = context + '/admin/notice/insertNoticeInfo.ssc'
						} else {
							tmpUrl = context + '/admin/notice/updateNoticeInfo.ssc'
						}

						if(!Ext.getCmp('title').getValue()) {
							Ext.MessageBox.alert('제목', '제목를 입력하세요.');
							return;
						}

						if(!Ext.getCmp('cont').getValue()) {
							Ext.MessageBox.alert('Failed', '내용을 입력하세요.');
							return;
						}

						if(!Ext.getCmp('popUpYn').getValue()) {
							Ext.MessageBox.alert('Failed', '팝업여부를 입력하세요.');
							return;
						}

						if(!Ext.getCmp('startDd').getValue()) {
							Ext.MessageBox.alert('Failed', '시작일을 입력하세요.');
							return;
						}

						if(!Ext.getCmp('endDd').getValue()) {
							Ext.MessageBox.alert('Failed', '종료일을 입력하세요.');
							return;
						}

						if(Ext.getCmp('endDd').getValue() < Ext.getCmp('startDd').getValue()) {
							Ext.MessageBox.alert('Failed', '시작일이 종료일보다 큽니다.');
							return;
						}

						console.log(Ext.getCmp('popUpYn').getValue())
						saveNoticeStore.update({
							url: tmpUrl,
							action: 'update',
							params: {
								noticeId: tmpNoticeId,
								cont: Ext.getCmp('cont').getValue(),
								title: Ext.getCmp('title').getValue(),
								startDd: Ext.getCmp('startDd').getValue(),
								endDd: Ext.getCmp('endDd').getValue(),
								popUpYn: Ext.getCmp('popUpYn').getValue(),
								regId: userId
							},
							callback: function (records, operation, success) {
								var jsonData = Ext.JSON.decode(operation.response.responseText);

								if(jsonData.result == "F") {
									Ext.MessageBox.alert('Failed', '에러입니다.');
									return;
								} else {
									Ext.MessageBox.alert('Success', '저장 되었습니다.');
									noticeList.getStore().reload();
								}
							}
						})
					}
				}, {
					text: '초기화',
					id: 'reset',
					handler: function () {
						fp.getForm().reset();
						tmpNoticeId = "";
					}
				}]
			});
		}
	}
}();