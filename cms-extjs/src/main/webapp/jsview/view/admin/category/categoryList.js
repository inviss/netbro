categoryList = function () {

	return {
		init: function () {
			//메뉴 선택표시
			hilight('admin');
			//하위폴터 오픈 횟수 제약을 위한 변수
			var count=0;
			//context 를 넣기위해서 tree에서 url을 강제로 삽입해준다.
			var store = Ext.data.StoreManager.get("adminCategoryTree"); 
			store.setProxy({
				type: 'ajax',
				url:  context+'/clip/findCategoryListForExtJs.ssc',
				actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
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

				id: 'categoryTree',
				store: store,
			 	viewConfig: {
					plugins : {
						ptype : 'treeviewdragdrop'
					}
				}, 
				rootVisible: true,
				useArrows: true,
				dockedItems : [{
					xtype: 'panel',
					dock: 'bottom', 
					tools: [{
						type : 'save',
						handler : function(){
							
							var tree = Ext.ComponentQuery.query('#categoryTree');
							var treeLength = tree[0].getRootNode().store.tree.root.stores[1].data.items.length; 
							var parentsIds='';
							var categoryIds='';
							var depths='';
						 	
							for(var i=1; i < treeLength; i++){
								var parentsId = tree[0].getRootNode().store.tree.root.stores[1].data.items[i].data.parentId ;
								var categoryId = tree[0].getRootNode().store.tree.root.stores[1].data.items[i].data.id ;
								var depth = tree[0].getRootNode().store.tree.root.stores[1].data.items[i].data.depth ;
								 
								parentsIds += parentsId +',';
								categoryIds += categoryId +',';
								depths += depth +',';
							} 
						 
							categroyStore.update({
								url : context + '/admin/category/updateChangeOrder.ssc',
								action: 'update',
								params: {
									'parentsIds': parentsIds.substr(0,parentsIds.lastIndexOf(',')),
									'categoryIds': categoryIds.substr(0,categoryIds.lastIndexOf(',')),
									'depths': depths.substr(0,depths.lastIndexOf(',')) 
								},
								callback: function(records, operation, success) {
									console.log(operation.response.responseText);
									var obj = Ext.JSON.decode(operation.response.responseText);
									if(obj.result =='N'){
										Ext.Msg.alert('실패',obj.info)
									}else{
										Ext.Msg.alert('성공','카테고리 순서 변경이 적용되었습니다.')
									}

								},  scope : this
							});
						}
					}
					]
				}],
				listeners: {

					itemclick: {
						fn: function (view, record, item, index, event) {
						 
							nodeId = record.data.id;
						 
							if(nodeId == 'total'){
								nodeId = 0
							}
							episodeStore.load({

								url : context + '/admin/category/findEpisodeSearch.ssc',
								action: 'read',
								params: {
									'categoryId': nodeId,
									'episodeNm': '',
									'pageNo': 1,
								}
							})
						}
					},
					load : function(view, node, records, successful, eOpts ){
						 //모든 노드를 무조건 보이도록 한다.
						//여러번 중복해서 하위폴더를 여는 오류대문에 1번만 하위폴더를 오픈하도록 수정
						 
						if(count==0){
							treepanel.expandAll();
							count++;
						} 
						 
					},
					afteritemexpand : function( node, index, item, eOpts ){
						console.log(node.data.cls);
					  
					}
				}
			});



			//좌측패널
			var left = Ext.create('Ext.panel.Panel', {
				layout: {
					type: 'vbox'
				},
				padding: '0 5 0 5',
				height: '80%',
				width: '100%',
				renderTo: 'categoryBody',
				title: '좌측메뉴',
				items: [treepanel],
				buttons: [{
					text: '카테고리 생성',
					handler: function (button) {

						if(!button.insert){

							button.insert = Ext.create('Ext.window.Window', {
								title: '카테고리신규 생성',
								width: 280,
								height: 160,
								layout: 'vbox',  
								id : 'createCategory',
								closable : true,
								closeAction : 'hide',
								items: [{
									xtype: 'textfield',
									fieldLabel: '상위 카테고리명',
									margin : 5,
									labelSeparator : '',
									width : '100%',
									name: 'parentsCategory'
								}, {
									xtype: 'textfield',
									fieldLabel: '신규 카테고리명',
									margin : 5,
									labelSeparator : '',
									width : '100%',
									name: 'insertCategoryNm'
								}, {
									xtype: 'fieldcontainer',
									fieldLabel: '위치지정',
									labelSeparator : '',
									margin : 5,

									defaultType: 'radiofield',
									layout: 'hbox',
									items: [{ 
										boxLabel : '하위노드',
										name: 'setPosition',
										inputValue : 'SUB',                    					 
										id : 'SUB'},
										{ 
											boxLabel : '다음노드',
											name: 'setPosition',
											margin : '0 0 0 20',
											inputValue : 'NEXT',
											id : 'NEXT'
										}]
								}, {
									xtype: 'hiddenfield',
									name: 'categoryId'

								}],
								buttons: [{
									text: '저장',
									handler : function(){

										var category  = treepanel.getSelectionModel().getSelection(); 
										var insertCategoryNm = Ext.ComponentQuery.query('field[name=insertCategoryNm]');
										var categoryId = Ext.ComponentQuery.query('field[name=categoryId]');
										var setPosition = Ext.ComponentQuery.query('field[name=setPosition][value=true]');

										if(insertCategoryNm[0].getValue() != ''){
											if(categoryId[0].getValue() == 'total'){
												categoryId[0].setValue(0) 
											}
											var tem = categroyStore.update({
												url : context + '/admin/category/insertCategory.ssc',
												action: 'update',
												params: {
													'categoryId': categoryId[0].getValue(),
													'categoryNm': insertCategoryNm[0].getValue(),
													'type':setPosition[0].id
												},
												callback: function(records, operation, success) {
													var obj = Ext.JSON.decode(operation.response.responseText);
													if(obj.result =='N'){
														Ext.Msg.alert('실패',obj.reason)
													}else{
														Ext.Msg.alert('알림','정상적으로 생성  되었습니다.')
														store.load();
														 if(count==0){
															treepanel.expandAll();
															count++;
														}else{
															count=0
														} 
													}

												},  scope : this
											});

											//리로드후 창을 닫는다.
											var tree = Ext.ComponentQuery.query('#createCategory');
											tree[0].close();
										} else{
											Ext.Msg.alert('알림','카테고리명이 비어있습니다.')
										}
									}
								}, {
									text: '닫기',
									handler : function(){
										var tree = Ext.ComponentQuery.query('#createCategory');
										tree[0].close();
									}
								}]
							,listeners : {
								show : function( view, eOpts ){

									var category  = treepanel.getSelectionModel().getSelection();
									var parentsCategory = Ext.ComponentQuery.query('field[name=parentsCategory]');
									var categoryId = Ext.ComponentQuery.query('field[name=categoryId]'); 


									//각각의 컬럼에 트리 view에서 선택한 값을 넣어준다.
									if(category[0] != undefined){
										parentsCategory[0].setValue(category[0].data.text);
										categoryId[0].setValue(category[0].data.id);
									}else{
										parentsCategory[0].setValue('전체');
										categoryId[0].setValue('total');
									}

									Ext.getCmp('SUB').setValue(true);
								}
							}
							}).show();
						}else{
							button.insert.show();
						}
					}
				}, {
					text: '카테고리 수정',
					handler: function (button) { 

						if(!button.updateCategory){

							button.updateCategory = Ext.create('Ext.window.Window', {
								title: '카테고리 수정',
								width: 290,
								height: 135,
								id : 'updateCategory',
								layout: 'vbox',          
								closable : true,
								closeAction : 'hide',
								items: [{
									xtype: 'textfield',
									fieldLabel: ' 카테고리명',
									labelSeparator : '',
									margin : 5,
									width : '100%',
									name: 'originCategory'
								}, {
									xtype: 'textfield',
									fieldLabel: '수정  카테고리명',
									labelSeparator : '',
									margin : 5,
									width : '100%',
									name: 'updateCategoryNm'
								}, {
									xtype: 'hiddenfield',
									name: 'upCategoryId'

								}],
								buttons: [{
									text: '저장',
									handler : function(){

										var updateCategoryNm = Ext.ComponentQuery.query('field[name=updateCategoryNm]');
										var categoryId = Ext.ComponentQuery.query('field[name=upCategoryId]'); 

										if(updateCategoryNm[0].getValue() != ''){
											var tem = categroyStore.update({
												url : context + '/admin/category/updateCategory.ssc',
												action: 'update',
												params: {
													'categoryId': categoryId[0].getValue(),
													'categoryNm': updateCategoryNm[0].getValue() ,
													'direction' : ''
												},
												callback: function(records, operation, success) {
													var obj = Ext.JSON.decode(operation.response.responseText);
													if(obj.result =='N'){
														Ext.Msg.alert('실패',obj.reason)
													}else{
														Ext.Msg.alert('알림','수정 되었습니다.')
														store.load();
														
														if(count==0){
															treepanel.expandAll();
															count++;
														}else{
															count=0
														} 
													
													}

												},  scope : this
											});

											//리로드후 창을 닫는다.
											var tree = Ext.ComponentQuery.query('#updateCategory');
											 
											tree[0].close();
										} else{                           				 
											Ext.Msg.alert('알림','카테고리명이 비어있습니다.')

										}

									}
								}, {
									text: '닫기'
										,handler : function(){
											var tree = Ext.ComponentQuery.query('#updateCategory');
											tree[0].close();
										}
								}]
							,listeners : {
								show : function( view, eOpts ){
									var category  = treepanel.getSelectionModel().getSelection();
									var parentsCategory = Ext.ComponentQuery.query('#updateCategory field[name=originCategory]');
									var categoryId = Ext.ComponentQuery.query('field[name=upCategoryId]');

									//각각의 컬럼에 트리 view에서 선택한 값을 넣어준다.
									if(category[0] != undefined){
										parentsCategory[0].setValue(category [0].data.text);
										categoryId[0].setValue(category [0].data.id);
									}else{
										//리로드후 창을 닫는다.
										var tree = Ext.ComponentQuery.query('#updateCategory');
										tree[0].close();
										Ext.Msg.alert('알림','선택된 카테고리가 없습니다.');

									}
								}
							}
							}).show();
						}else{
							button.updateCategory.show();
						}
					}
				}, {
					text: '카테고리 삭제',
					handler: function (button) {
						var category  = treepanel.getSelectionModel().getSelection();
						if(category[0] != undefined){

							categroyStore.update({
								url : context + '/admin/category/deleteCategory.ssc',
								action: 'update',
								params: {
									'categoryId': category[0].data.id 
								},
								callback: function(records, operation, success) {
									var obj = Ext.JSON.decode(operation.response.responseText);
									if(obj.result =='N'){
										Ext.Msg.alert('실패',obj.reason)
									}else{
										Ext.Msg.alert('알림','정상적으로 삭제 되었습니다.')
										store.load();
									}

								},  scope : this
							});


						}else{
							Ext.Msg.alert('알림','선택된 카테고리가 없습니다.');

						}
					}
				}],
				listeners: {
					afterrender: function (view, eOpts) {

						var tree = Ext.ComponentQuery.query('#categoryTree');

						tree[0].setHeight(view.getHeight()-55);

					},
					resize : function(view, width, height, oldWidth, oldHeight, eOpts ){
						var tree = Ext.ComponentQuery.query('#categoryTree');

						tree[0].setHeight(height-55);
					}
				}
			})

			Ext.EventManager.onWindowResize(function () {
				left.setSize(undefined, undefined)
				treepanel.setSize(undefined, undefined)
			});
		}
	}
}();