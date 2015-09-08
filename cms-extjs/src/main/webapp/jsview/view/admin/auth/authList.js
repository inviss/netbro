var authList
var tmpAuthId;

authList = function() {

	return {
		init: function() {

			//메뉴 선택표시
			hilight('admin');
			findAuthStore.load({
				url: context + '/admin/auth/findAuthList.ssc',
			});
			//유저리스트 패널
			authList = Ext.create('Ext.grid.Panel', {
				viewConfig: {
					loadMask: false,
				},
				store: findAuthStore,
				cls: 'regridColunms',
				columns: [{
					text: '역활명',
					dataIndex: 'authNm',
					align: 'center',
					flex: 3
				}, {
					text: '역활설명',
					dataIndex: 'authSubNm',
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
				height: '100%',
				width: '100%',
				border: false,

				listeners: {
					select: function(selModel, record, index, options) {

						var authId = this.getSelectionModel().getSelection()[0].data.authId
						//ctId값으로 기본정보 호출
						var getAuthInfo = new Ext.data.Operation({
							action: 'read',
							url: context + '/admin/auth/getAuthInfo.ssc',
							params: {
								'authId': authId
							}
						});

						tmpAuthId = authId;

						var authStore = getAuthStore.load(getAuthInfo)

						var roleAuthStore = getRoleAuthStore.load(getAuthInfo)

						authStore.on('load', function(st, records, opts) {
							st.each(function(re) {
								var authForm = {
										authId: re.get("authId"),
										authNm: re.get("authNm"),
										authSubNm: re.get("authSubNm"),
										useYn: re.get("useYn"),
								}

								//userRight.js의 ID를 선언한 userFormList에 값을 set함.
								Ext.getCmp('authForm').getForm().setValues(authForm);
							})
						}),
						/**
						 * 권한Radiobox setValue
						 */
						roleAuthStore.on('load', function(st, records, opts) {
							var controlGubun = st.proxy.reader.jsonData.controlGubun;
							var tmpControlGubun = controlGubun.split(',');
							var treeGrid = Ext.ComponentQuery.query('#authTree');

							console.log(treeGrid);
							
							treeGrid[0].getSelectionModel().getStore().each(function(rec) {

								var rowData = rec.data

								for(var i = 0; i < tmpControlGubun.length + 2; i++) {
									if(rowData.id == i) {
										if(tmpControlGubun[i - 2] == "R") {
											rec.set('read', true)
											rec.set('write', false)
											rec.set('limit', false)

											if(map.values(rowData.id) != "") {
												map.remove(rowData.id);
											}

											map.put(rowData.id, "R");
										}else if(tmpControlGubun[i - 2] == "RW") {
											rec.set('read', false)
											rec.set('write', true)
											rec.set('limit', false)

											if(map.values(rowData.id) != "") {
												map.remove(rowData.id);
											}

											map.put(rowData.id, "RW");
										}else {
											rec.set('read', false)
											rec.set('write', false)
											rec.set('limit', true)

											if(map.values(rowData.id) != "") {
												map.remove(rowData.id);
											}

											map.put(rowData.id, "L");
										}
									}
								}
							})
						})
					}
				}
			});

			//좌측패널
			var authListView = Ext.create('Ext.panel.Panel', {
				//  width : '100%'
				layout: 'auto',
				title: '역활목록',
				plain: true,
				renderTo: 'authBody',
				bodyStyle: {
					'border-width': '1 1 1 1'
				},
				padding: '0 0 0 5',
				items: [{
					items: [authList],
					border: false,
				}]

			})

			Ext.EventManager.onWindowResize(authListView.doLayout, authListView);
		}
	}
}();