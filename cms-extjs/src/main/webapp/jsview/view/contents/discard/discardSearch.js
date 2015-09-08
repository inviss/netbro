discardSearch = function () {
   
    return {
        init: function () {
            
        	
                //우측검색결과 패널
            var gridSearch = Ext.create('Ext.grid.Panel', {
            	selModel: Ext.create('Ext.selection.CheckboxModel'),
				store: disuseStore, 
				id : 'discard',
				cls : 'discardGrid',
				autoScroll : false,
				viewConfig: {
					//loadMask: false,
				}, 
				columns: [{
					text: '영상ID',
					dataIndex: 'ctId',
					align: 'center',
					
					flex: 1
				}, {
					text: '컨텐츠명',
					dataIndex: 'ctNm',
					style: 'text-align:center',
					flex: 4
				}, {
					text: '폐기상태',
					dataIndex: 'disuseClf',
					align: 'center',
					flex: 1
				}, {
					text: '등록일',
					dataIndex: 'regDt',
					align: 'center',
					flex: 1
				}, {
					text: '폐기일',
					dataIndex: 'disuseDd',
					align: 'center',
					flex: 1
				}, {
                    text: '폐기번호',
                    hidden : true,
                    dataIndex: 'disuseNo'

                } ],
				dockedItems: [{
					xtype: 'pagingtoolbar',
					store: disuseStore,
					dock: 'bottom',
					layout: 'hbox',
					items : [ {
						text : '폐기취소'
				             ,itemId : 'cancelDiscard'
				             ,handler : function(){
				            	 var selectRow = gridSearch.getSelectionModel().getSelection();
		                            var selected = "";
		                           
		                            console.log(selectRow)
		                                //선택된 값을 ','로 구분지어서 string으로 만든다.
		                            Ext.each(selectRow, function (item) {
		                                selected += item.data.disuseNo + ","
		                            });
		                            if(selected == '') {
		                                Ext.Msg.alert('알림', '체크된 정보가 없습니다.');
		                            }else{
		                            	discardStore.destroy({
		 		                           	 url : context + '/contents/discard/cancleDisuse.ssc',
		 		                             action: 'destroy',
		 		                            params: {
		                                        'modrId': loginUserId,
		                                        'disuseNos': selected

		                                    },
											callback: function(records, operation, success) {
												var obj = Ext.JSON.decode(operation.response.responseText);
												if(obj.result =='N'){
													Ext.Msg.alert('실패',obj.errorCont)
												}else{
													Ext.Msg.alert('알림','폐기취소되었습니다')
													 gridSearch.getSelectionModel().deselectAll();
													disuseStore.reload();
												}

											},  scope : this
		 		                         });  
		                            }
				             }
					}],
					listeners: {
						beforechange: function (view, page, eOpts) {
 
							 
							var tem = disuseStore.load({
								action: 'read',
								url: context + '/contents/discard/findDiscardInfoList.ssc',
								params: {
									'pageNo': page
								}
							});
						},
						afterrender : function(view){
							this.child('#refresh').hide();
							var mainHeight = Ext.ComponentQuery.query('#discardLeft');
							var grid = Ext.ComponentQuery.query('#discard');
							grid[0].setHeight(mainHeight[0].getHeight() -26);

						} ,
						resize : function(view, width, height, oldWidth, oldHeight, eOpts ){/*
							var height = Ext.ComponentQuery.query('#discard');
							var mainHeight = Ext.ComponentQuery.query('#categotyRight');
							var grid = Ext.ComponentQuery.query('#episodeGrid');
							grid[0].setHeight(mainHeight[0].getHeight()-55);
						*/}
					}}],
				height: '100%',
				width: '100%',
				border: false 
			});

           

            //우측패널
            var mainView = Ext.create('Ext.panel.Panel', {
                //  width : '100%'
                height: 601,
                margin: '0 5 0 0',
                plain: true,
                renderTo: 'body',
               border : false,
                items: [{
                    title: '폐기 목록',
                    id: 'searchResult',
                    items: [gridSearch]
                }] 
             

            })

            Ext.EventManager.onWindowResize(function () {
                mainView.setSize(undefined, undefined)
            });
        }
    }
}();