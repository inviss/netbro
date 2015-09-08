var trsList;
var tmpPage = "";
trsList = function () {
	return {
		init: function () {

			var reload = function () {
				findTrsStore.load({
					url: context + '/trs/findTrsList.ssc',
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
			//setInterval(reload, 5000);

			var win = ""

				findTrsStore.load({
					url: context + '/trs/findTrsList.ssc',
					action: 'read',
					params: {
						'pageNo': 1,
						'categoryId': categoryId
					}
				});

			//유저리스트 패널
			trsList = Ext.create('Ext.grid.Panel', {

				store: findTrsStore,
				viewConfig: {
					loadMask: false,
				},
				cls:'regridColunms',
				columns: [{
					text: '장비ID',
					dataIndex: 'seq',
					align: 'center',
					flex: 1,
					tdCls :'x-cell'
				}, {
					text: '컨텐츠명',
					dataIndex: 'ctNm',
					style: 'text-align:center',
					flex: 3.5
				}, {
					text: '카테고리명',
					dataIndex: 'categoryNm',
					align: 'center',
					flex: 1.5
				}, {
					text: '프로파일명',
					dataIndex: 'proFlnm',
					align: 'center',
					flex: 1
				}, {
					text: '사업자명',
					dataIndex: 'company',
					align: 'center',
					flex: 1
				}, {
					text: '등록일시',
					dataIndex: 'regDt',
					align: 'center',
					flex: 1
				}, {
					text: '등록시간',
					dataIndex: 'trsStrDt',
					align: 'center',
					flex: 1
				}, {
					text: '완료시간',
					dataIndex: 'trsEndDt',
					align: 'center',
					flex: 1
				}, {
					text: '진행률',
					dataIndex: 'prgrs',
					align: 'center',
					flex: 0.7,
				}, {
					text: '상태',
					dataIndex: 'workStatcd',
					align: 'center',
					flex: 0.7,
				}, {
					text: '재요청',
					align: 'center',
					xtype: 'actioncolumn',
					flex: 0.7,
					items: [
					        {
					        	xtype: 'button',
					        	icon: context + '/images/icon_retry_01.jpg',
					        	handler: function(grid, rowIndex, colIndex) {
					        		var trsSeq = grid.getStore().getAt(rowIndex).get('seq');

					        		retryTrsStore.load({
					        			action: 'update',
					        			url: context + '/trs/retryTrsObj.ssc',
					        			params: {
					        				'trsSeq':trsSeq
					        			},
					        			callback: function (records, operation, success) {
					        				var jsonData = Ext.JSON.decode(operation.response.responseText);

					        				if(jsonData.result == "F") {
					        					Ext.MessageBox.alert('Failed', '에러입니다.');
					        					return;
					        				} else {
					        					Ext.MessageBox.alert('Success', '재요청 되었습니다.');
					        					trsList.getStore().reload();
					        				}
					        			}
					        		});
					        	}
					        }
					        ]
				}],
				dockedItems: [{
					xtype: 'pagingtoolbar',
					store: findTrsStore,
					dock: 'bottom',
					layout: 'hbox',
					listeners: {
						afterrender : function() {
							this.child('#refresh').hide();
						},
						beforechange: function (view, page, eOpts) {

							tmpPage = page

							var tem = findTrsStore.load({
								action: 'read',
								url: context + '/trs/findTrsList.ssc',
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
			});

			//좌측패널
			var trsListView = Ext.create('Ext.panel.Panel', {
				//  width : '100%'
				layout: 'auto',
				title: '변환목록',
				plain: true,
				//border: false,
				bodyStyle : {
					'border-width' : '1 1 1 1'
				},
				padding: '0 5 0 0',
				renderTo: 'trsBody',
				items: [{
					items: [trsList],
					border: false,
				}]

			})

			Ext.EventManager.onWindowResize(trsListView.doLayout, trsListView);
		}
	}
}();