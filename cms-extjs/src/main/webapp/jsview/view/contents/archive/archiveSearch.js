archiveSearch = function () {

	return {
		init: function () {


			//아카이브텝 그리드
			var archiveGrid = Ext.create('Ext.grid.Panel',{

				store: archiveListStore,
				id :  'grid', 
				cls : 'clipColunms',
				viewConfig: {
					loadMask: false
				},
				border : false,
				columns: [{
					text: '카테고리명',
					dataIndex: 'categoryNm',
					sortable: false,
					align: 'center', 
					flex: 1
				},{
					text: '에피소드명',
					dataIndex: 'episodeNm',
					sortable: false,
					align: 'center', 
					flex: 1
				}, {
					text: '영상ID',
					dataIndex: 'ctiId',
					align: 'center',  
					sortable: false, 
					hidden : true,
					flex: 1
				}, {
					text: '제목',
					dataIndex: 'ctNm',
					sortable: false, 
					style: 'text-align:center',
					flex: 4
				},  {
					text: '작업상태',
					dataIndex: 'workStatCd',
					sortable: false,
					align: 'center', 
					flex: 1
				}, {
					text: '진행률',
					dataIndex: 'prgrs',
					sortable: false,
					align: 'center', 
					flex: 1
				}, {
					text: 'SEQ',
					dataIndex: 'seq',
					sortable: false,
					hidden :  true,
					flex: 1
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
					        		var seq = grid.getStore().getAt(rowIndex).get('seq');

					        		console.log(seq);
					        		archiveStore.load({
					        			action: 'update',
					        			url: context + '/contents/archive/updateRetryArchvie.ssc',
					        			params: {
					        				'seq':seq
					        			},
					        			callback: function (records, operation, success) {
					        				var jsonData = Ext.JSON.decode(operation.response.responseText);

					        				if(jsonData.result == "F") {
					        					Ext.MessageBox.alert('Failed', '에러입니다.');
					        					return;
					        				} else {
					        					Ext.MessageBox.alert('Success', '재요청 되었습니다.');
					        					archiveListStore.reload();
					        				}
					        			}
					        		}); 
					        	}
					        }
					        ]
				}],
				height: '100%',
				width: '100%',	
				listeners : {
					cellclick: function (view, td, cellIndex, record, tr, rowIndex, e, eOpts) {
						var ctiId = this.getSelectionModel().getSelection()[0].data.ctiId
						console.log(this.getSelectionModel().getSelection()[0].data)
						var store = archiveStore.load({
							url: context + '/contents/archive/getArchiveInfo.ssc',
							action: 'read',
							params: {
								'ctiId': ctiId 
							},
							callback: function (records, operation, success) {
								var jsonData = Ext.JSON.decode(operation.response.responseText);

								if(jsonData.result == "F") {
									Ext.MessageBox.alert('Failed', '에러입니다.');
									return;
								} else {

									console.log(jsonData)
								}
							}
						});



						store.on('load', function (st, records, opts) {
							console.log(st)
							console.log(records) 
							st.each(function (re) {

								var getCtId = re.get("ctId");
								//	console.log(re)
								var metaForm = Ext.ComponentQuery.query('#metaform'); 
								var loadForm = {
										title: re.get("ctNm") ,
										ctLeng: re.get("ctLeng") ,
										userNm: re.get("userNm") ,
										regDt: re.get("regDt") ,
								}

								metaForm[0].getForm().setValues(loadForm);

							})
						})

					}
				},
				dockedItems: [{
					xtype: 'pagingtoolbar',
					store: archiveListStore,
					dock: 'bottom'
						,listeners : {
							afterrender : function() {
								this.child('#refresh').hide();
							},
							beforechange :  function( view, page, eOpts ){

								tempPage = page


								if (cateogory.length == 0 || cateogory[0].data.id == 'total') {
									categroyId = 0
								} else {
									categroyId = cateogory[0].data.id
								}


								var tem = archiveListStore.load({
									'pageNo': 1 ,
									'startDt' : fromDate,
									'endDt' : toDate,
									'keyword' : keyword,
									'searchGb' : searchGb,
									'categoryId': categroyId 
								});


							}
						}
				}]

			});

			var metaFormPanel = Ext.create('Ext.form.Panel',{
				id : 'metaform',
				defaultType: 'textfield',
				border: false,
				defaults : {
					width :  350
				},
				cls : 'customFormBackground',
				items: [{
					fieldLabel: '제목',
					name: 'title',
					allowBlank:false 
				},{
					fieldLabel: '영상길이',
					name: 'ctLeng'
				},{
					fieldLabel: '등록자',
					name: 'userNm'
				}, {
					fieldLabel: '요청일시',
					name: 'regDt' 
				}]
			})




			//하나의 텝의 두개의 패널을 넣어서 다운로드와 아카이브텝을 개별로 인식하도록 한다.
			var archiveTab = Ext.create('Ext.panel.Panel',{
				layout : {
					type : 'hbox'
				}, 
				border : false,
				items : [{flex : 3 
					,items : [archiveGrid]},
					{flex : 1
						,padding : '5 5 0 5'
						,items : [metaFormPanel]}]
			})

			//----------------------------------------------------아카이브 텝 끝 --------------------------------------     	

			//아카이브텝 그리드
			var downloadGrid = Ext.create('Ext.grid.Panel',{

				store: downloadListStore,
				id :  'downgrid', 
				cls : 'clipColunms',
				border : false,
				viewConfig: {
					loadMask: false
				},
				columns: [{
					text: '카테고리명',
					dataIndex: 'categoryNm',
					sortable: false,
					align: 'center', 
					flex: 1
				},{
					text: '에피소드명',
					dataIndex: 'episodeNm',
					sortable: false,
					align: 'center', 
					flex: 1
				}, {
					text: '영상ID',
					dataIndex: 'ctiId',
					align: 'center',  
					sortable: false, 
					hidden : true,
					flex: 1
				}, {
					text: '제목',
					dataIndex: 'ctNm',
					sortable: false, 
					style: 'text-align:center',
					flex: 4
				},  {
					text: '작업상태',
					dataIndex: 'workStatCd',
					sortable: false,
					align: 'center', 
					flex: 1
				}, {
					text: '진행률',
					dataIndex: 'prgrs',
					sortable: false,
					align: 'center', 
					flex: 1
				}, {
					text: 'SEQ',
					dataIndex: 'seq',
					sortable: false,
					hidden :  true,
					flex: 1
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
					        		var seq = grid.getStore().getAt(rowIndex).get('seq');

					        		console.log(seq);
					        		downloadStore.load({
					        			action: 'update',
					        			url: context + '/contents/archive/updateRetryDownload.ssc',
					        			params: {
					        				'seq':seq
					        			},
					        			callback: function (records, operation, success) {
					        				var jsonData = Ext.JSON.decode(operation.response.responseText);

					        				if(jsonData.result == "F") {
					        					Ext.MessageBox.alert('Failed', '에러입니다.');
					        					return;
					        				} else {
					        					Ext.MessageBox.alert('Success', '재요청 되었습니다.');
					        					downloadListStore.reload();
					        				}
					        			}
					        		}); 
					        	}
					        }
					        ]
				}],
				height: '100%',
				width: '100%',	
				listeners : {
					cellclick: function (view, td, cellIndex, record, tr, rowIndex, e, eOpts) {
						var ctiId = this.getSelectionModel().getSelection()[0].data.ctiId
						console.log(this.getSelectionModel().getSelection()[0].data)
						var store = downloadStore.load({
							url: context + '/contents/archive/getDownloadInfo.ssc',
							action: 'read',
							params: {
								'ctiId': ctiId 
							}
						});

						store.on('load', function (st, records, opts) {
							console.log(records)
							st.each(function (re) {

								var getCtId = re.get("ctId");

								var metaForm = Ext.ComponentQuery.query('#downMetaform'); 
								var loadForm = {
										title: re.get("ctNm") ,
										ctLeng: re.get("ctLeng") ,
										userNm: re.get("userNm") ,
										regDt: re.get("regDt") 
								}

								metaForm[0].getForm().setValues(loadForm);

							})
						})

					}
				},
				dockedItems: [{
					xtype: 'pagingtoolbar',
					store: downloadListStore,
					dock: 'bottom'
						,listeners : {
							afterrender : function() {
								this.child('#refresh').hide();
							},
							beforechange :  function( view, page, eOpts ){

								tempPage = page
								var keyword = Ext.getCmp('keyword').getValue();
								var fromDate = Ext.getCmp('fromDate').getValue();
								var toDate = Ext.getCmp('toDate').getValue();
								var cateogory =  Ext.getCmp('categoryTree').getSelectionModel().getSelection();//treepanel.getSelectionModel().getSelection();
								var categroyId = '';


								if (cateogory.length == 0 || cateogory[0].data.id == 'total') {
									categroyId = 0
								} else {
									categroyId = cateogory[0].data.id
								}


								var tem = downloadListStore.load({
									url : context + '/contents/archive/findDownloadList.ssc',
									action: 'read',
									params: {
										'pageNo': 1 ,
										'startDt' : fromDate,
										'endDt' : toDate,
										'keyword' : keyword,
										'searchGb' : searchGb,
										'categoryId': categroyId 
									}
								});



							}
						}
				}]

			});



			var downMetaFormPanel = Ext.create('Ext.form.Panel',{
				id : 'downMetaform',
				defaultType: 'textfield',
				border: false,
				viewConfig: {
					loadMask: false,
				},
				defaults : {
					width :  350
				},
				cls : 'customFormBackground',
				items: [{
					fieldLabel: '제목',
					name: 'title',
					allowBlank:false 
				},{
					fieldLabel: '길이',
					name: 'ctLeng'
				},{
					fieldLabel: '요청자',
					name: 'userNm'
				}, {
					fieldLabel: '요청일',
					name: 'regDt' 
				}]
			})


			var downloadTab = Ext.create('Ext.panel.Panel',{
				layout : {
					type : 'hbox'
				},

				border : false,
				items : [{flex : 3 
					,items : [downloadGrid]},
					{flex : 1
						,padding : '5 5 0 5'
						,items : [downMetaFormPanel]
					}]
			})


			//----------------------------------------------------다운로드 텝 끝 --------------------------------------      	





			var mainPanel = Ext.create('Ext.panel.Panel',{
				layout : {
					type : 'hbox'
				},
				renderTo : 'body',
				margin : '0 5 0 0',
				border : false,
				plain: true,
				items : [Ext.create('Ext.tab.Panel', {        		    
					// plain: true,
					items: [
					        {
					        	title: '아카이브', 
					        	id : 'archiveTab',
					        	items : [archiveTab]
					        },
					        {
					        	title: '다운로드', 
					        	id : 'downloadTab',
					        	items : [downloadTab]
					        }
					        ],
					        flex : 3,
					        listeners : {
					        	tabchange : function( tabPanel, newCard, oldCard, eOpts ){
					        		if(newCard.id == 'downloadTab'){
					        			tabGb = 'download'
					        				var fromDate = Ext.getCmp('fromDate').getValue();
					        			var toDate = Ext.getCmp('toDate').getValue();
					        			downloadListStore.load({
					        				url : context + '/contents/archive/findDownloadList.ssc',
					        				action: 'read',
					        				params: {
					        					'pageNo': 1 ,
					        					'startDt' : fromDate,
					        					'endDt' : toDate,
					        					'keyword' : '',
					        					'categoryId' : 0
					        				}
					        			}); 

					        		}else{
					        			tabGb='archive'
					        		}
					        	}
					        }
				})],
				listeners : {
					afterrender : function( view, eOpts ){
						var searchPanel = Ext.ComponentQuery.query('#searchPanel')
						console.log(searchPanel[0].getHeight())
						var height  = searchPanel[0].getHeight()

						//다운로드텝, 아카이브텝 높이를 균일화 시킨다
						archiveGrid.setHeight(height-56); 
						downloadGrid.setHeight(height-56); 
					} 

				}
			})

			Ext.EventManager.onWindowResize(function () {
				mainPanel.setSize(undefined, undefined)
			});
			Ext.EventManager.onWindowResize(function () {
				metaFormPanel.setSize(undefined, undefined)
			});

		}
	}
}();