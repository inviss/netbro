var traList;
var tmpPage = "";
traList = function () {
	return {
		init: function () {

			var reload = function () {
				findTraStore.load({
					url: context + '/tra/findTraList.ssc',
					action: 'read',
					params: {
						'pageNo': tmpPage,
						'traContentNm': keyword,
						'startDt': fromDate,
						'endDt': toDate,
						'categoryId': categoryId,
						'workStat': tmpWorkstat,
					}
				});
			}
			setInterval(reload, 5000);

			var win = ""

				findTraStore.load({
					url: context + '/tra/findTraList.ssc',
					action: 'read',
					params: {
						'pageNo': 1
					}
				});

			//유저리스트 패널
			traList = Ext.create('Ext.grid.Panel', {

				store: findTraStore,
				cls:'regridColunms',
				viewConfig: {
					loadMask: false,
				},
				columns: [{
					text: '장비ID',
					dataIndex: 'seq',
					align: 'center',

					flex: 1
				}, {
					text: '컨텐츠명',
					dataIndex: 'ctNm',
					style: 'text-align:center',
					flex: 4
				}, {
					text: '카테고리명',
					dataIndex: 'categoryNm',
					align: 'center',
					flex: 1
				},{
					text: '프로파일명',
					dataIndex: 'proFlnm',
					align: 'center',
					flex: 1
				}, {
					text: '등록일',
					dataIndex: 'regDt',
					align: 'center',
					flex: 1
				}, {
					text: '시작시간',
					dataIndex: 'reqDt',
					align: 'center',
					flex: 1
				}, {
					text: '종료시간',
					dataIndex: 'modDt',
					align: 'center',
					flex: 1
				}, {
					text: '상태',
					dataIndex: 'jobStatus',
					align: 'center',
					flex: 1,
				}, {
					text: '진행률',
					align: 'center',
					dataIndex: 'prgrs',
					flex: 1,
					//                    renderer: function (v, m, r) {
					//                        var id = Ext.id();
					//                        Ext.defer(function () {
					//                            Ext.widget('progressbar', {
					//                                renderTo: id,
					//                                value: v / 100,
					//                                width: 'auto',
					//                                height: '12'
					//
					//                            });
					//                        }, 1);
					//                        return Ext.String.format('<div id="{0}"></div>', id);
					//                    }
				}],
				dockedItems: [{
					xtype: 'pagingtoolbar',
					store: findTraStore,
					dock: 'bottom',
					layout: 'hbox',
					listeners: {
						beforechange: function (view, page, eOpts) {

							tmpPage = page

							var tem = findTraStore.load({
								action: 'read',
								url: context + '/tra/findTraList.ssc',
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
						var seq = this.getSelectionModel().getSelection()[0].data.seq
						//ctId값으로 기본정보 호출
						var geTraInfo = new Ext.data.Operation({
							action: 'read',
							params: {
								'seq': seq
							}
						});

						if(!win) {
							win = Ext.create('Ext.window.Window', {
								title: '상세리스트',
								height: 300,
								width: 200,
								html: 'hello'
								// ,id : searchcolomn+'_'+categoryId
									,
									closable: true,
									closeAction: 'hide',
									layout: {
										type: 'fit'
									},
									//items: [detailList]
							})

						}
						//win.show();
				/*		var equipStore = getEquipStore.load(geTraInfo)

						equipStore.on('load', function (st, records, opts) {
							st.each(function (re) {
								var equipForm = {
										deviceId: re.get("deviceId"),
										deviceNm: re.get("deviceNm"),
										deviceIp: re.get("deviceIp"),
										deviceClfCd: re.get("deviceClfCd"),
										useYn: re.get("useYn"),

								}
								//userRight.js의 ID를 선언한 userFormList에 값을 set함.
								Ext.getCmp('equipForm').getForm().setValues(equipForm);
							})
						})*/

					}

				}
			});

			//좌측패널
			var traListView = Ext.create('Ext.panel.Panel', {
				//  width : '100%'
				bodyStyle : {
                	'border-width' : '1 1 1 1'
                },
                padding: '0 5 0 0',
				layout: 'auto',
				title: '변환목록',
				plain: true,
				renderTo: 'traBody',
				items: [{
					items: [traList],
					border: false,
				}]

			})

			Ext.EventManager.onWindowResize(traListView.doLayout, traListView);
		}
	}
}();