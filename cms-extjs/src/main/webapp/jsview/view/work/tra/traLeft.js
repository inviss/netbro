var keyword = "";
var workStat = "";
var fromDate = "";
var toDate = "";
var cateogory = "";
var categoryId = "";
var tmpWorkstat = "";

traLeft = function () {

	return {
		init: function () {
			//메뉴 선택표시
			hilight('service');
			var tmpFromDate = "";

			var workStat = new Ext.data.ArrayStore({
				fields: ['id', 'url'],
				data: [
				       ['S', '진행중'],
				       ['C', '완료'],
				       ['E', '에러'],
				       ]
			});


			var store = Ext.data.StoreManager.get("workCategory"); 
			console.log(store)
			store.setProxy({
				type: 'ajax',
				url:  context+'/admin/category/findCategoryListForExtJs.ssc'
				,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
			,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
			,noCache: false
			,reader : {
				type : 'json'
					,root : 'store'
			}
			});
			store.load();
			//좌측 트리패널
			var treepanel = Ext.create('Ext.tree.Panel', {
				width: '100%',
				height: 300,
				bodyStyle: {
					'border-width': '0 0 0 0'
				},
				id: 'categoryTree',
				store: store,
				rootVisible: true,
				useArrows: true,
				border: false,
			});

			//좌측 검색패널
			var searchPanel = Ext.create('Ext.Panel', {
				width: '100%',
				//height: 900,

				layout: 'form',
				bodyPadding: 5,
				defaultType: 'textfield',
				border: false,
				defaults: {
					labelWidth: 50
				},
				items: [{
					xtype: 'fieldset',
					title: '기간검색',
					defaultType: 'textfield',
					layout: 'anchor',
					defaults: {
						anchor: '100%',
						labelWidth: 50
					},
					items: [{
						layout: 'hbox',
						xtype: 'fieldcontainer',
						defaultType: 'radiofield',
						defaults: {
							flex: 1
						},
					}, {
						name: 'startDate',
						id: 'fromDate',
						xtype: 'datefield',
						fieldLabel: '시작일',
						labelSeparator: '',
						flex: 2,
						format: 'Y-m-d',
						allowBlank: false
					}, {
						name: 'endDt',
						fieldLabel: '종료일',
						labelSeparator: '',
						id: 'toDate',
						xtype: 'datefield',
						flex: 2,
						format: 'Y-m-d',
						margins: '0 0 0 6',
						allowBlank: false
					}]
				}, {
					xtype: 'fieldset',
					title: '상세검색',
					collapsible: true,
					defaultType: 'textfield',
					layout: 'anchor',
					defaults: {
						anchor: '100%',
						labelWidth: 60
					},
					items: [{
						fieldLabel: '작업상태',
						name: 'workStat',
						id: 'workStat',
						xtype: 'combobox',
						store: workStat,
						width: '80%',
						valueField: 'id',
						displayField: 'url',
					}]
				}, {
					fieldLabel: '컨텐츠명',
					labelSeparator: '',
					id: 'keyword',
					listeners: {
						scope: this,
						specialkey: function (f, e) {

							if(e.getKey() == e.ENTER) {
								

								keyword = Ext.getCmp('keyword').getValue();
								fromDate = Ext.getCmp('fromDate').getValue();
								toDate = Ext.getCmp('toDate').getValue();
								cateogoryId = treepanel.getSelectionModel().getSelection();
								tmpWorkstat = Ext.getCmp('workStat').getValue();
								
								

								var tem = findTraStore.load({
									url: context + '/tra/findTraList.ssc',
									action: 'read',
									params: {
										'traContentNm': keyword,
										'startDt': fromDate,
										'endDt': toDate,
										'categoryId': categoryId,
										'workStat': tmpWorkstat,
									}
								});

							}
						}
					}
				}],
				listeners: {
					afterrender: function (view, eOpts) {
						tmpFromDate = new Date();
						tmpFromDate.setDate(tmpFromDate.getDate() - 30);

						Ext.getCmp('fromDate').setValue(tmpFromDate);
						Ext.getCmp('toDate').setValue(new Date);
					}
				}

			});

			//좌측패널
			var left = Ext.create('Ext.panel.Panel', {
				layout: {
					type: 'vbox'
				},
				padding: '0 5 0 5'
					//,height : '100%'
					,
					width: '100%',
					renderTo: 'left',
					title: '좌측메뉴',
					items: [treepanel, searchPanel],
					buttons: [{
						text: '검색',
						handler: function () {

							keyword = Ext.getCmp('keyword').getValue();
							workStat = Ext.getCmp('workStat').getValue();
							fromDate = Ext.getCmp('fromDate').getValue();
							toDate = Ext.getCmp('toDate').getValue();
							cateogory = treepanel.getSelectionModel().getSelection();
							categoryId = '';

							tmpWorkstat = workStat;

							if(cateogory.length == 0 || cateogory[0].data.id == 'total') {
								categoryId = 0
							} else {
								categoryId = cateogory[0].data.id
							}

							if(Ext.getCmp('toDate').getValue() < Ext.getCmp('fromDate').getValue()) {
								Ext.MessageBox.alert('Failed', '시작일이 종료일보다 큽니다.');
								return;
							}
							

							var tem = findTraStore.load({
								url: context + '/tra/findTraList.ssc',
								action: 'read',
								params: {
									'traContentNm': keyword,
									'startDt': fromDate,
									'endDt': toDate,
									'categoryId': categoryId,
									'workStat': workStat,
								}
							});
							//	gridSearch.show();

						}
					}, {
						text: '초기화',
						handler: function () {

							Ext.getCmp('keyword').setValue(null);
							Ext.getCmp('workStat').setValue(null);
							Ext.getCmp('fromDate').setValue(tmpFromDate);
							Ext.getCmp('toDate').setValue(new Date());

							keyword = "";
							tmpWorkstat = "";
							fromDate = "";
							toDate = "";
							cateogory = "";
							categoryId = "";

							clipCategoryTreestore.reload();

						}
					}]
			})

			Ext.EventManager.onWindowResize(function () {
				left.setSize(undefined, undefined)
			});

			Ext.EventManager.onWindowResize(function () {
				treepanel.setSize(undefined, undefined)
			});

			Ext.EventManager.onWindowResize(function () {
				searchPanel.setSize(undefined, undefined)
			});

		}
	}
}();