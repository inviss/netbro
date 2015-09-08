var busipartnerList;
var tmpbusiPartnerId;
busipartnerList = function () {

	return {
		init: function () {
			//메뉴 선택표시
			hilight('admin');
			findBusipartnerStore.load({
				action: 'read',
				url: context + '/admin/busipartner/findBusiPartnerList.ssc',
				params: {
					'pageNo': 1
				}

			});
			//유저리스트 패널
			busipartnerList = Ext.create('Ext.grid.Panel', {

				store: findBusipartnerStore,
				cls:'regridColunms',
				viewConfig: {
					loadMask: false,
				},
				columns: [{
					text: '업체명',
					dataIndex: 'company',
					flex: 3
				}, {
					text: 'IP',
					dataIndex: 'ip',
					align:'center',
					flex: 3
				}, {
					text: '서비스여부',
					dataIndex: 'servYn',
					align:'center',
					flex: 1
				}, {
					text: 'FTP사용여부',
					dataIndex: 'ftpServYn',
					align:'center',
					flex: 1
				}],
				dockedItems: [{
					xtype: 'pagingtoolbar',
					store: findBusipartnerStore,
					dock: 'bottom',
					layout: 'hbox',
					listeners: {
						afterrender : function() {
							this.child('#refresh').hide();
						},
						beforechange: function (view, page, eOpts) {
							var tem = findBusipartnerStore.load({
								action: 'read',
								url: context + '/admin/busipartner/findBusiPartnerList.ssc',
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
						var busiPartnerId = this.getSelectionModel().getSelection()[0].data.busiPartnerId
						//ctId값으로 기본정보 호출
						var getBusipartnerInfo = new Ext.data.Operation({
							action: 'read',
							url: context + '/admin/business/getBusipartnerInfo.ssc',
							params: {
								'busiPartnerId': busiPartnerId
							}
						});

						tmpbusiPartnerId = busiPartnerId;

						var busipartnerStore = getBusipartnerStore.load(getBusipartnerInfo)
						var proBusiStore = getProBusiStore.load(getBusipartnerInfo)

						/**
						 * 프로파일 정보 체크박스 store load.
						 */
						proBusiStore.on('load', function (st, records, opts) {

						}),
						/**
						 * 서비스정보, FTP정보 store load.
						 */
						busipartnerStore.on('load', function (st, records, opts) {
							st.each(function (re) {
								var busipartnerForm = {
										busiPartnerId: re.get("busiPartnerId"),
										servYn: re.get("servYn"),
										ftpServYn: re.get("ftpServYn"),
										srvUrl: re.get("srvUrl"),
										company: re.get("company"),
										remoteDir: re.get("remoteDir"),
										transMethod: re.get("transMethod"),
										ip: re.get("ip"),
										port: re.get("port"),
										ftpId: re.get("ftpId"),
										password: re.get("password"),
								}
								//userRight.js의 ID를 선언한 userFormList에 값을 set함.
								Ext.getCmp('busipartnerForm').getForm().setValues(busipartnerForm);
							})
						})

					}

				}
			});

			//좌측패널
			var busipartnerListView = Ext.create('Ext.panel.Panel', {
				//  width : '100%'
				layout: 'auto',
				title: '사업자목록',
				plain: true,
				bodyStyle : {
                	'border-width' : '1 1 1 1'
                },
                padding: '0 0 0 5',
				renderTo: 'busipartnerBody',
				items: [{
					items: [busipartnerList],
					border: false, 
				}]

			})

			Ext.EventManager.onWindowResize(busipartnerListView.doLayout, busipartnerListView);
		}
	}
}();