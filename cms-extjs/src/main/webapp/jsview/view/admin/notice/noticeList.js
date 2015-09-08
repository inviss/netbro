var noticeList;
var tmpNoticeId; 

noticeList = function () {

	return {
		init: function () {
			//메뉴 선택표시
			hilight('admin');
			findNoticeStore.load({
				url: context+'/admin/notice/findNoticeList.ssc',
				action: 'read',
				params: {
					'pageNo': 1
				}
			});

			//공지사항리스트 패널
			noticeList = Ext.create('Ext.grid.Panel', {
				id : 'noticeGrid',
				store: findNoticeStore,
				cls:'regridColunms',
				viewConfig: {
					loadMask: false,
				},
				columns: [{
					text: '번호',
					dataIndex: 'noticeId',
					align:'center',
					flex: 1
				}, {
					text: '제목',
					dataIndex: 'title',
					style: 'text-align:center',
					flex: 3
				}, {
					text: '등록자',
					dataIndex: 'userNm',
					align:'center',
					flex: 2
				}, {
					text: '등록일',
					dataIndex: 'regDt',
					align:'center',
					flex: 3
				}, {
					text: '팝업여부',
					dataIndex: 'popUpYn',
					align:'center',
					flex: 1
				}],
				dockedItems: [{
					xtype: 'pagingtoolbar',
					store: findNoticeStore,
					dock: 'bottom',
					layout: 'hbox',
					listeners: {
						afterrender : function() {
							this.child('#refresh').hide();
						},
						beforechange: function (view, page, eOpts) {
							var tem = findNoticeStore.load({
								url: context+'/admin/notice/findNoticeList.ssc',
								action: 'read',
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
						var noticeId = this.getSelectionModel().getSelection()[0].data.noticeId
						tmpNoticeId = noticeId;
						//ctId값으로 기본정보 호출
						var getNoticeInfo = new Ext.data.Operation({
							action: 'read',
							url: context + '/admin/notice/getNoticeInfo.ssc',
							params: {
								'noticeId': noticeId
							}
						});

						var noticeStore = getNoticeStore.load(getNoticeInfo)

						noticeStore.on('load', function (st, records, opts) {
							st.each(function (re) {
								var noticeForm = {
										noticeId : re.get("noticeId"),
										title : re.get("title"),
										cont : re.get("cont"),
										userNm : re.get("userNm"),
										popUpYn : re.get("popUpYn"),
										startDd : re.get("startDd"),
										endDd : re.get("endDd"),
										regId : re.get("regId")
								}
								//userRight.js의 ID를 선언한 userFormList에 값을 set함.
								Ext.getCmp('noticeForm').getForm().setValues(noticeForm);
								//Ext.getCmp('title').hide();
								//fp.add({xtype : 'textfield',fieldLabel:'a'})


							})

						})

					},


					afterrender : function(view){

						var mainHeight = Ext.ComponentQuery.query('#noticeGrid');
						mainHeight[0].setHeight(mainHeight[0].getHeight()+750);
					/*	var grid = Ext.ComponentQuery.query('#episodeGrid');
						grid[0].setHeight(mainHeight[0].getHeight()-55);*/

					} ,
					resize : function(view, width, height, oldWidth, oldHeight, eOpts ){
						/*var height = Ext.ComponentQuery.query('#url');
						var mainHeight = Ext.ComponentQuery.query('#categotyRight');
						var grid = Ext.ComponentQuery.query('#episodeGrid');
						grid[0].setHeight(mainHeight[0].getHeight()-55);*/
					}
				 

				}
			});

			//좌측패널
			var noticeListView = Ext.create('Ext.panel.Panel', {
				//  width : '100%'
				layout: 'auto',
				bodyStyle : {
                	'border-width' : '1 1 1 1'
                },
                padding: '0 0 0 5',
				title: '공지사항목록',
				plain: true,
				renderTo: 'noticeBody',
				items: [{
					items: [noticeList],
					border: false, 
				}]

			})

			Ext.EventManager.onWindowResize(noticeListView.doLayout, noticeListView);
		}
	}
}();