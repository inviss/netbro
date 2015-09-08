var profileList;
var tmpProFlId = "";

profileList = function () {

	return {
		init: function () {
			//메뉴 선택표시
			hilight('admin');
			findProfileStore.load({
				url: context + '/admin/profile/findProfileList.ssc',
				action: 'read',
				params: {
					'pageNo': 1
				}
			});
			//유저리스트 패널
			profileList = Ext.create('Ext.grid.Panel', {

				store: findProfileStore,
				cls:'regridColunms',
				viewConfig: {
					loadMask: false,
				},
				columns: [{
					text: '프로파일이름',
					dataIndex: 'proFlnm',
					style: 'text-align:center',
					flex: 3
				}, {
					text: '코덱',
					dataIndex: 'vdoCodec',
					style: 'text-align:center',
					flex: 3
				}, {
					text: 'bitrate',
					dataIndex: 'vdoBitRate',
					align:'center',
					flex: 3
				}, {
					text: '등록일',
					dataIndex: 'regDt',
					align:'center',
					align:'center',
					flex: 3
				}, {
					text: '사용여부',
					dataIndex: 'useYn',
					align:'center',
					flex: 1
				}],
				dockedItems: [{
					xtype: 'pagingtoolbar',
					store: findProfileStore,
					dock: 'bottom',
					layout: 'hbox',
					listeners: {
						afterrender : function() {
							this.child('#refresh').hide();
						},
						beforechange: function (view, page, eOpts) {
							var tem = findProfileStore.load({
								action: 'read',
								url: context + '/admin/profile/findProfileList.ssc',
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
						var proFlId = this.getSelectionModel().getSelection()[0].data.proFlId
						//ctId값으로 기본정보 호출
						var getProfileInfo = new Ext.data.Operation({
							action: 'read',
							url: context + '/admin/profile/getProfileInfo.ssc',
							params: {
								'proFlId': proFlId
							}
						});

						tmpProFlId = proFlId;

						var profileStore = getProfileStore.load(getProfileInfo)

						profileStore.on('load', function (st, records, opts) {
							st.each(function (re) {
								var profileForm = {
										proFlnm: re.get("proFlnm"),
										flNameRule: re.get("flNameRule"),
										vdoCodec: re.get("vdoCodec"),
										vdoBitRate: re.get("vdoBitRate"),
										vdoFS: re.get("vdoFS"),
										vdoSync: re.get("vdoSync"),
										ext: re.get("ext"),
										servBit: re.get("servBit"),
										keyFrame: re.get("keyFrame"),
										priority: re.get("priority"),
										useYn: re.get("useYn"),

								}
								//userRight.js의 ID를 선언한 userFormList에 값을 set함.
								Ext.getCmp('profileForm').getForm().setValues(profileForm);
							})
						})

					}

				}
			});

			//좌측패널
			var profileListView = Ext.create('Ext.panel.Panel', {
				//  \\width : '100%'
				layout: 'auto',
				title: '장비목록',
				plain: true,
				bodyStyle : {
                	'border-width' : '1 1 1 1'
                },
                padding: '0 0 0 5',
				renderTo: 'profileBody',
				items: [{
					items: [profileList],
					border: false, 
				}]

			})

			Ext.EventManager.onWindowResize(profileListView.doLayout, profileListView);
		}
	}
}();