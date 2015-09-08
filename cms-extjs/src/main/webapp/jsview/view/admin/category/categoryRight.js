categoryRight = function () {

	return {
		init: function () {

			var fp = Ext.create('Ext.panel.Panel', {

				id: 'categotyRight', 
				width: 'auto',
				title: '에피소드 상세 창',
				height : '80%',
				margin: '0 5 30 5',
				border : false,
				renderTo: 'right', 
				items: [{
					xtype : 'panel' 
					,width : '100%'
					,id : 'episode'
					,bodyStyle : {
							'border-width' : '1 0 0 0'
								}

					,items : [{
						xtype : 'grid'
						,store : episodeStore
						,id : 'episodeGrid'
						//,cls :'regridColunms'
						,autoScroll :  true
					,columns : [
				            {
				            	text :'에피소드ID'
				            		,flex : 1
				            		,sortable : false
				            		,align : 'center'
				            			,dataIndex : 'episodeId'
				            },
				            {
				            	text :'에피소드명'
				            		,flex : 3
				            		,sortable : false
				            		,dataIndex : 'episodeNm'
				            },{
				            	text :'등록일'
				            		,flex : 1
				            		,sortable : false
				            		,align : 'center'
				            			,dataIndex : 'regDt'
				            },{
				            	text :'카테고리Id'
				            		,flex : 1
				            		,hidden : true
				            		,sortable : false
				            		,dataIndex : 'categoryId'
				            }
				            ] ,
							dockedItems: [{
								xtype: 'pagingtoolbar',
								store: episodeStore, 
								id : 'pageing',
								dock: 'bottom',                                 
								listeners : {
									beforechange :  function( view, page, eOpts ){



										var cateogory =  Ext.getCmp('categoryTree').getSelectionModel().getSelection(); 
										var categroyId = '';


										if (cateogory.length == 0 || cateogory[0].data.id == 'total') {
											categroyId = 0
										} else {
											categroyId = cateogory[0].data.id
										}


										var tem = episodeStore.load({
											url : context + '/admin/category/findEpisodeSearch.ssc',
											action: 'read',
											params: {

												'categoryId': categroyId,                                              
												'pageNo' : page
											}
										});



									}
								}
							}]

				}],
				listeners : {

					afterrender : function(view){

						var mainHeight = Ext.ComponentQuery.query('#categotyRight');
						var grid = Ext.ComponentQuery.query('#episodeGrid');
						grid[0].setHeight(mainHeight[0].getHeight()-55);

					} ,
					resize : function(view, width, height, oldWidth, oldHeight, eOpts ){
						var height = Ext.ComponentQuery.query('#url');
						var mainHeight = Ext.ComponentQuery.query('#categotyRight');
						var grid = Ext.ComponentQuery.query('#episodeGrid');
						grid[0].setHeight(mainHeight[0].getHeight()-55);
					}
				 }

				} 

				],
				buttons: [{
					text: '에피소드 생성',
					handler: function (button) {

						if(!button.insert){

							button.insert = Ext.create('Ext.window.Window', {
								title: '에피소드 신규 생성',
								width: 280,
								height: 160,
								layout: 'vbox',  
								id : 'createEpisode',
								closable : true,
								closeAction : 'hide',
								items: [{
									xtype: 'textfield',
									fieldLabel: '카테고리명',
									margin : 5,
									labelSeparator : '',
									width : '100%',
									name: 'categoryNm'
								}, {
									xtype: 'textfield',
									fieldLabel: '신규 에피소드명',
									margin : 5,
									labelSeparator : '',
									width : '100%',
									name: 'insertEpisodeNm'
								}, {
									xtype: 'hiddenfield',
									name: 'categoryId'

								}],
								buttons: [{
									text: '저장',
									handler : function(){
										var tree = Ext.ComponentQuery.query('#categoryTree');
										var category  = tree[0].getSelectionModel().getSelection();
										var insertEpisodeNm = Ext.ComponentQuery.query('field[name=insertEpisodeNm]');
										var categoryId = Ext.ComponentQuery.query('field[name=categoryId]');
										var pageNum = Ext.ComponentQuery.query('#pageing');

										if(insertEpisodeNm[0].getValue() != ''){
											var tem = episodeStore.update({
												url : context + '/admin/category/insertEpisode.ssc',
												action: 'update',
												params: {
													'categoryId': categoryId[0].getValue(),
													'episodeNm': insertEpisodeNm[0].getValue() 
												},
												callback: function(records, operation, success) {
													var obj = Ext.JSON.decode(operation.response.responseText);
													if(obj.result =='N'){
														Ext.Msg.alert('실패',obj.reason)
													}else{
														Ext.Msg.alert('알림','정상적으로 생성  되었습니다.')
														episodeStore.load({

															url : context + '/admin/category/findEpisodeSearch.ssc',
															action: 'read',
															params: {

																'categoryId': categoryId[0].getValue(),                                              
																'pageNo' : pageNum[0].getPageData().currentPage
															}

														}); 
														insertEpisodeNm[0].setValue('') 
													}

												},  scope : this
											});



											//리로드후 창을 닫는다.
											var close = Ext.ComponentQuery.query('#createEpisode');
											close[0].close();
										} else{
											Ext.Msg.alert('알림','카테고리명이 비어있습니다.')
										}
									}
								}, {
									text: '닫기',
									handler : function(){
										var tree = Ext.ComponentQuery.query('#createEpisode');
										tree[0].close();
									}
								}]
							,listeners : {
								show : function( view, eOpts ){
									var tree = Ext.ComponentQuery.query('#categoryTree');
									var category  = tree[0].getSelectionModel().getSelection();
									var categoryNm = Ext.ComponentQuery.query('field[name=categoryNm]');
									var categoryId = Ext.ComponentQuery.query('field[name=categoryId]'); 

									//각각의 컬럼에 트리 view에서 선택한 값을 넣어준다.
									if(category[0]  != undefined){
										categoryNm[0].setValue(category[0].data.text);
										categoryId[0].setValue(category[0].data.id);
									}else{
										Ext.Msg.alert('alert','선택된 카테고리가 없습니다.')
										var tree = Ext.ComponentQuery.query('#createEpisode');
										tree[0].close();
									}


								}
							}
							}).show();
						}else{
							button.insert.show();
						}
					}
				}, {
					text: '에피소드 수정',
					handler: function (button) { 

						if(!button.updateCategory){

							button.updateCategory = Ext.create('Ext.window.Window', {
								title: '에피소드 수정',
								width: 290,
								height: 135,
								id : 'updateEpisode',
								layout: 'vbox',          
								closable : true,
								closeAction : 'hide',
								items: [{
									xtype: 'textfield',
									fieldLabel: '에피소드명',
									labelSeparator : '',
									margin : 5,
									width : '100%',
									name: 'originEpisodeNm'
								}, {
									xtype: 'textfield',
									fieldLabel: '수정  에피소드명',
									labelSeparator : '',
									margin : 5,
									width : '100%',
									name: 'updateEpisodeNm'
								}, {
									xtype: 'hiddenfield',
									name: 'upCategoryId'

								}, {
									xtype: 'hiddenfield',
									name: 'upEpisodeId'

								}],
								buttons: [{
									text: '저장',
									handler : function(){

										var updateEpisodeNm = Ext.ComponentQuery.query('field[name=updateEpisodeNm]');
										var categoryId = Ext.ComponentQuery.query('field[name=upCategoryId]'); 
										var episodeId = Ext.ComponentQuery.query('field[name=upEpisodeId]'); 

										if(updateEpisodeNm[0].getValue() != ''){
											var tem = categroyStore.update({
												url : context + '/admin/category/updateEpisode.ssc',
												action: 'update',
												params: {
													'categoryId': categoryId[0].getValue(),
													'episodeNm': updateEpisodeNm[0].getValue() ,
													'episodeId' : episodeId[0].getValue()
												},
												callback: function(records, operation, success) {
													var obj = Ext.JSON.decode(operation.response.responseText);
													if(obj.result =='N'){
														Ext.Msg.alert('실패',obj.reason)
													}else{
														Ext.Msg.alert('알림','수정 되었습니다.');
														episodeStore.load({

															url : context + '/admin/category/findEpisodeSearch.ssc',
															action: 'read',
															params: {

																'categoryId': categoryId[0].getValue(),                                              
																'pageNo' : 1
															}

														});
														updateEpisodeNm[0].setValue(''); 
													}

												},  scope : this
											});


											//리로드후 창을 닫는다.
											var tree = Ext.ComponentQuery.query('#updateEpisode');
											tree[0].close();
										} else{
											Ext.Msg.alert('알림','카테고리명이 비어있습니다.')
										}

									}
								}, {
									text: '닫기'
										,handler : function(){
											var tree = Ext.ComponentQuery.query('#updateEpisode');
											tree[0].close();
										}
								}]
							,listeners : {
								show : function( view, eOpts ){
									var episode = Ext.ComponentQuery.query('#episodeGrid');

									var gridEpisodeId  =  episode[0].getSelectionModel().getSelection();                    				 
									var originEpisodeNm = Ext.ComponentQuery.query('#updateEpisode field[name=originEpisodeNm]');
									var categoryId = Ext.ComponentQuery.query('#updateEpisode field[name=upCategoryId]');
									var episodeId = Ext.ComponentQuery.query('#updateEpisode field[name=upEpisodeId]');
									 
									//각각의 컬럼에 트리 view에서 선택한 값을 넣어준다.
									if(gridEpisodeId[0] != undefined){
										originEpisodeNm[0].setValue(gridEpisodeId [0].data.episodeNm);
										categoryId[0].setValue(gridEpisodeId [0].data.categoryId);
										episodeId[0].setValue(gridEpisodeId [0].data.episodeId);
									}else{
										Ext.Msg.alert('알림','선택된 에피소드 가 없습니다.');
										var tree = Ext.ComponentQuery.query('#updateEpisode');
										tree[0].close();
									}
								}
							}
							}).show();
						}else{
							button.updateCategory.show();
						}
					}
				}, {
					text: '에피소드 삭제',
					handler: function (button) {
						var episode = Ext.ComponentQuery.query('#episodeGrid');           			 
						var gridEpisodeId  =  episode[0].getSelectionModel().getSelection(); 

						if(gridEpisodeId[0] != undefined){

							categroyStore.update({
								url : context + '/admin/category/deleteEpisode.ssc',
								action: 'update',
								params: {
									'categoryId': gridEpisodeId[0].data.categoryId ,
									'episodeId': gridEpisodeId[0].data.episodeId 
								},
								callback: function(records, operation, success) {
									var obj = Ext.JSON.decode(operation.response.responseText);
									if(obj.result =='N'){
										Ext.Msg.alert('실패',obj.reason)
									}else{
										Ext.Msg.alert('알림','정상적으로 삭제 되었습니다.')
										episodeStore. load({

											url : context + '/admin/category/findEpisodeSearch.ssc',
											action: 'read',
											params: {

												'categoryId': gridEpisodeId[0].data.categoryId,                                              
												'pageNo' : 1
											}

										});
									}

								},  scope : this
							});

						}else{
							Ext.Msg.alert('알림','선택된 카테고리가 없습니다.');

						}
					}
				}]

			});


			Ext.EventManager.onWindowResize(function () {
				fp.setSize(undefined, undefined)

			});
		}
	}
}();