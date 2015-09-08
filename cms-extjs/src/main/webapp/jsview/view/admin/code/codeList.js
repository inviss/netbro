var codeList;
var tmp = "";

codeList = function () {

	return {
		init: function () {
			//메뉴 선택표시
			hilight('admin');
			findCodeStore.load({
				url: context+'/admin/code/findCodeList.ssc',
				action: 'read',
				params: {
					'pageNo': 1
				}
			});

			//유저리스트 패널
			codeList = Ext.create('Ext.grid.Panel', {

				store: findCodeStore,
				cls:'regridColunms',
				viewConfig: {
					loadMask: false,
				},
				columns: [{
					text: '분류코드',
					dataIndex: 'clfCd',
					align: 'center',
					flex: 2
				}, {
					text: '분류명',
					dataIndex: 'clfNm',
					align: 'center',
					flex: 3
				}, {
					text: '상세코드',
					dataIndex: 'sclCd',
					align: 'center',
					flex: 2
				}, {
					text: '상세코드명',
					dataIndex: 'sclNm',
					align: 'center',
					flex: 3
				}, {
					text: '등록일',
					dataIndex: 'regDt',
					align: 'center',
					flex: 3
				}, {
					text: '사용여부',
					dataIndex: 'useYn',
					align: 'center',
					flex: 1
				}],
				dockedItems: [{
					xtype: 'pagingtoolbar',
					store: findCodeStore,
					dock: 'bottom',
					layout: 'hbox',
					listeners: {
						afterrender : function() {
							this.child('#refresh').hide();
						},
						beforechange: function (view, page, eOpts) {
							var tem = findCodeStore.load({
								action: 'read',
								url: context+'/admin/code/findCodeList.ssc',
								params: {
									'pageNo': page
								}
							});
						}
					}
				}],
				height: '100%',
				width: '100%',
				border: false,

				listeners: {
					select: function (selModel, record, index, options) {
						var sclCd = this.getSelectionModel().getSelection()[0].data.sclCd
						var clfCd = this.getSelectionModel().getSelection()[0].data.clfCd

						tmp = sclCd;

						//ctId값으로 기본정보 호출
						var getCodeInfo = new Ext.data.Operation({
							action: 'read',
							url: context + '/admin/code/getCodeInfo.ssc',
							params: {
								'sclCd': sclCd,
								'clfCd': clfCd
							}
						});

						var codeStore = getCodeStore.load(getCodeInfo)

						codeStore.on('load', function (st, records, opts) {
							st.each(function (re) {
								var codeForm = {
										sclCd: re.get("sclCd"),
										clfCd: re.get("clfCd"),
										sclNm: re.get("sclNm"),
										clfNm: re.get("clfNm"),
										codeCont: re.get("codeCont"),
										useYn: re.get("useYn"),

								}
								//userRight.js의 ID를 선언한 userFormList에 값을 set함.
								Ext.getCmp('codeForm').getForm().setValues(codeForm);

								Ext.getCmp('createWay').disable();
								Ext.getCmp('clfGubun').disable();
								Ext.getCmp('clfNm').disable();
								Ext.getCmp('clfCd').disable();
								Ext.getCmp('clfGubun').disable();
								Ext.getCmp('sclNm').enable();
								Ext.getCmp('sclCd').disable();
								Ext.getCmp('codeCont').enable();

								Ext.getCmp('add').enable();


								var useYn = Ext.ComponentQuery.query('#useYn');
								//saveButton[0].hide(true);
								useYn[0].setDisabled(false);

							})
						})

					}

				}
			});

			//좌측패널
			var codeListView = Ext.create('Ext.panel.Panel', {
				//  width : '100%'
				layout: 'auto',
				title: '장비목록',
				plain: true,
				bodyStyle : {
                	'border-width' : '1 1 1 1'
                },
                padding: '0 0 0 5',
				renderTo: 'codeBody',
				items: [{
					items: [codeList],
					border: false,
				}]

			})

			Ext.EventManager.onWindowResize(codeListView.doLayout, codeListView);
		}
	}
}();