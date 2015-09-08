var storageList;

var tmpStorageId;

storageList = function () {

	return {
		init: function () {
			//메뉴 선택표시
			hilight('admin');
			findStorageStore.load({
				url: context + '/admin/storage/findStorageList.ssc',
			});
			//유저리스트 패널
			storageList = Ext.create('Ext.grid.Panel', {

				store: findStorageStore,
				cls:'regridColunms',
				border: false, 
				viewConfig: {
					loadMask: false,
					border: false, 
				},
				columns: [{
					text: '번호',
					dataIndex: 'storageId',
					flex: 1,
					align:'center',
					width:'10'
				}, {
					text: '전체 용량',
					dataIndex: 'totalVolume',
					align:'center',
					flex: 3
				}, {
					text: '사용 용량',
					dataIndex: 'useVolume',
					align:'center',
					flex: 3
				}, {
					text: '가용 용량',
					dataIndex: 'idleVolume',
					align:'center',
					flex: 3
				},{
					text: '스토리지 경로',
					dataIndex: 'storagePath',
					align:'center',
					flex: 2
				},{
					text: '임계치',
					dataIndex: 'limit',
					align:'center',
					flex: 2
				},{
					text: '스토리지구분',
					dataIndex: 'storageGubun',
					align:'center',
					flex: 2
				}],
				height: '100%',
				width: '100%',

				listeners: {
					select: function (selModel, record, index, options) {
						var storageId = this.getSelectionModel().getSelection()[0].data.storageId
						//storageId값으로 기본정보 호출
						var getStorageInfo = new Ext.data.Operation({
							action: 'read',
							url: context + '/admin/storage/getStorageInfo.ssc',
							params: {
								'storageId': storageId
							}
						});


						var storageStore = getStorageStore.load(getStorageInfo)

						storageStore.on('load', function (st, records, opts) {
							st.each(function (re) {
								var storageForm = {
										totalVolume: re.get("totalVolume"),
										useVolume: re.get("useVolume"),
										idleVolume: re.get("idleVolume"),
										limit: re.get("limit"),
										storagePath: re.get("storagePath"),
										storageGubun: re.get("storageGubun"),
								}
								//userRight.js의 ID를 선언한 userFormList에 값을 set함.
								Ext.getCmp('storageForm').getForm().setValues(storageForm);

								tmpStorageId = storageId;
							})
						})

					}

				}
			});

			//좌측패널
			var storageListView = Ext.create('Ext.panel.Panel', {
				//  width : '100%'
				layout: 'auto',
				bodyStyle : {
                	'border-width' : '1 1 1 1'
                },
                padding: '0 0 0 5',
				title: '스토리지목록',
				plain: true,
				renderTo: 'storageBody',
				items: [{
					items: [storageList],
					border: false
				}]

			})

			Ext.EventManager.onWindowResize(storageListView.doLayout, storageListView);
		}
	}
}();