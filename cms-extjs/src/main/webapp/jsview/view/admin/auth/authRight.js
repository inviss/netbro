var fp;
var tmpUrl;
var map = new Map();

var tmpMenuId = ['3','7','10','14'];
var tmpMenuLeng = 23;

	authRight = function() {

	return {
		init: function() {

			map.clear();

			var store = Ext.data.StoreManager.get("roleAuthTree");
			store.setProxy({
				type: 'ajax',
				url: context + '/admin/auth/findMenuTree.ssc',
				actionMethods: {
					read: "GET",
				},
				headers: {
					'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache'
				},
				noCache: false,
				reader: {
					type: 'json',
					root: 'root',
				}
			});
			store.load();

			fp = Ext.create('Ext.form.Panel', {

				id: 'authForm',
				layout: 'vbox',
				width: 'auto',
				title: '권한정보',
				margin: '0 5 0 5',
				renderTo: 'right',
				items: [{
					xtype: 'fieldset',
					title: '역활 정보',
					margin: '5 5 5 5',
					collapsible: false,
					width: '100%',
					listeners: {
						afterrender: function(view, eOpts) {
							//초반 그리드 화면의 버튼을 권한에 따라 활성화 비활성화 한다.
							var saveButton = Ext.ComponentQuery.query('#save');
							var resetButton = Ext.ComponentQuery.query('#reset');

							if(perm != 'RW') {
								saveButton[0].setDisabled(true);
								resetButton[0].setDisabled(true);
							}
						}
					},
					defaults: {
						anchor: '100%',
						layout: {
							type: 'hbox',
							defaultMargins: {
								top: 5,
								right: 5,
								bottom: 5,
								left: 5
							},
						}
					},
					items: [{
						width: '80%',
						xtype: 'textfield',
						name: 'authNm',
						id: 'authNm',
						align: 'center',
						margin: '5 5 5 5',
						fieldLabel: '역활명',
						labelSeparator: '',
						allowBlank: false,
						minLength: 2
					}, {
						width: '80%',
						xtype: 'textfield',
						name: 'authSubNm',
						id: 'authSubNm',
						fieldLabel: '역활설명',
						labelSeparator: '',
						margin: '5 5 5 5',
						allowBlank: false,
					}, {
						width: '80%',
						xtype: 'radiogroup',
						fieldLabel: '사용여부',
						labelSeparator: '',
						margin: '5 5 5 5',
						allowBlank: false,
						items: [{
							xtype: 'radiofield',
							boxLabel: '사용',
							name: 'useYn',
							inputValue: 'Y'
						}, {
							xtype: 'radiofield',
							boxLabel: '미사용',
							name: 'useYn',
							inputValue: 'N'
						}, ]
					}]
				},
				Ext.create('Ext.tree.Panel', {
					width: '100%',
					id: 'authTree',
					border: false,
					title: '권한설정',
					useArrows: true,
					rootVisible: false,
					store:store,
					expanded:'true',
					multiSelect: true,
					//singleExpand: true,
					//the 'columns' property is now 'headers'
					columns: [{
						xtype: 'treecolumn', //this is so we know which column will show the tree
						text: '메뉴',
						flex: 2,
						dataIndex: 'text'
					}, {
						xtype: 'checkcolumn',
						header: '읽기', 
						name:'read',
						id:'read',
						dataIndex: 'read',

						width: 55,
						stopSelection: false,
						menuDisabled: true,
						listeners : { 
							checkchange : function( view, rowIndex, checked, eOpts ){
								var treeGrid = Ext.ComponentQuery.query('#authTree');
								var children = treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children;
								var obj_length;

								if(treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children != null){
									obj_length =  Object.keys(children).length;
								}else{
									obj_length = 0
								}

								treeGrid[0].getSelectionModel().getStore().each(function(rec){

									var rowData = rec.data
									var selectId = treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.id 

									if(selectId == rowData.id){
										rec.set('read',true)
										rec.set('write',false)
										rec.set('limit',false)	

										if(map.values(rowData.id) != ""){
											map.remove(rowData.id);
										}

										map.put(rowData.id,"R");

									}else if(selectId == 3){
										for(var i = 0; i < obj_length; i++){
											if(rowData.id == treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children[i].id){
												rec.set('read',true)
												rec.set('write',false)
												rec.set('limit',false)

												if(map.values(rowData.id) != ""){
													map.remove(rowData.id);
												}

												map.put(rowData.id,"R");
											}
										}
									}else if(selectId == 7){
										for(var i = 0; i < obj_length; i++){
											if(rowData.id == treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children[i].id){
												rec.set('read',true)
												rec.set('write',false)
												rec.set('limit',false)

												if(map.values(rowData.id) != ""){
													map.remove(rowData.id);
												}

												map.put(rowData.id,"R");
											}
										}
									}else if(selectId == 10){
										for(var i = 0; i < obj_length; i++){
											if(rowData.id == treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children[i].id){
												rec.set('read',true)
												rec.set('write',false)
												rec.set('limit',false)

												if(map.values(rowData.id) != ""){
													map.remove(rowData.id);
												}

												map.put(rowData.id,"R");
											}
										}
									}else if(selectId == 14){
										for(var i = 0; i < obj_length; i++){
											if(rowData.id == treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children[i].id){
												rec.set('read',true)
												rec.set('write',false)
												rec.set('limit',false)

												if(map.values(rowData.id) != ""){
													map.remove(rowData.id);
												}

												map.put(rowData.id,"R");
											}
										}
									}
								})
							}
						}
					}, {
						xtype: 'checkcolumn',
						header: '쓰기', 
						name:'write',
						dataIndex: 'write',
						width: 55,
						stopSelection: false,
						menuDisabled: true,
						listeners : { 
							checkchange : function( view, rowIndex, checked, eOpts ){
								var treeGrid = Ext.ComponentQuery.query('#authTree');
								var children = treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children;
								var obj_length;

								if(treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children != null){
									obj_length =  Object.keys(children).length;
								}else{
									obj_length = 0
								}

								treeGrid[0].getSelectionModel().getStore().each(function(rec){
									var rowData = rec.data
									var selectId = treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.id 

									if(selectId == rowData.id){
										rec.set('read',false)
										rec.set('write',true)
										rec.set('limit',false)

										if(map.values(selectId) != ""){
											map.remove(selectId);
										}

										map.put(selectId,"RW");

									}else if(selectId == 3){
										for(var i = 0; i < obj_length; i++){
											if(rowData.id == treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children[i].id){
												rec.set('read',false)
												rec.set('write',true)
												rec.set('limit',false)		

												if(map.values(rowData.id) != ""){
													map.remove(rowData.id);
												}

												map.put(rowData.id,"RW");
											}
										}
									}else if(selectId == 7){
										for(var i = 0; i < obj_length; i++){
											if(rowData.id == treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children[i].id){
												rec.set('read',false)
												rec.set('write',true)
												rec.set('limit',false)

												if(map.values(rowData.id) != ""){
													map.remove(rowData.id);
												}

												map.put(rowData.id,"RW");
											}
										}
									}else if(selectId == 10){
										for(var i = 0; i < obj_length; i++){
											if(rowData.id == treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children[i].id){
												rec.set('read',false)
												rec.set('write',true)
												rec.set('limit',false)

												if(map.values(rowData.id) != ""){
													map.remove(rowData.id);
												}

												map.put(rowData.id,"RW");
											}
										}
									}else if(selectId == 14){
										for(var i = 0; i < obj_length; i++){
											if(rowData.id == treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children[i].id){
												rec.set('read',false)
												rec.set('write',true)
												rec.set('limit',false)

												if(map.values(rowData.id) != ""){
													map.remove(rowData.id);
												}

												map.put(rowData.id,"RW");
											}
										}
									}
								})
								console.log(map.values());
							}
						}
					}, {
						xtype: 'checkcolumn',
						header: '제한', 
						name:'limit',
						dataIndex: 'limit',
						width: 55,
						stopSelection: false,
						menuDisabled: true,
						listeners : { 
							checkchange : function( view, rowIndex, checked, eOpts ){
								var treeGrid = Ext.ComponentQuery.query('#authTree');
								var children = treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children;
								var obj_length;

								if(treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children != null){
									obj_length =  Object.keys(children).length;
								}else{
									obj_length = 0
								}

								treeGrid[0].getSelectionModel().getStore().each(function(rec){
									var rowData = rec.data
									var selectId = treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.id 

									if(selectId == rowData.id){
										rec.set('read',false)
										rec.set('write',false)
										rec.set('limit',true)	

										if(map.values(selectId) != ""){
											map.remove(selectId);
										}

										map.put(selectId,"L");

									}else if(selectId == 3){
										for(var i = 0; i < obj_length; i++){
											if(rowData.id == treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children[i].id){
												rec.set('read',false)
												rec.set('write',false)
												rec.set('limit',true)

												if(map.values(rowData.id) != ""){
													map.remove(rowData.id);
												}

												map.put(rowData.id,"L");
											}

										}
									}else if(selectId == 7){
										for(var i = 0; i < obj_length; i++){
											if(rowData.id == treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children[i].id){
												rec.set('read',false)
												rec.set('write',false)
												rec.set('limit',true)	

												if(map.values(rowData.id) != ""){
													map.remove(rowData.id);
												}

												map.put(rowData.id,"L");
											}
										}
									}else if(selectId == 10){
										for(var i = 0; i < obj_length; i++){
											if(rowData.id == treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children[i].id){
												rec.set('read',false)
												rec.set('write',false)
												rec.set('limit',true)

												if(map.values(rowData.id) != ""){
													map.remove(rowData.id);
												}

												map.put(rowData.id,"L");
											}
										}
									}else if(selectId == 14){
										for(var i = 0; i < obj_length; i++){
											if(rowData.id == treeGrid[0].getSelectionModel().store.data.items[rowIndex].raw.children[i].id){
												rec.set('read',false)
												rec.set('write',false)
												rec.set('limit',true)

												if(map.values(rowData.id) != ""){
													map.remove(rowData.id);
												}

												map.put(rowData.id,"L");
											}
										}
									}
								})
								console.log(map.values());
							}
						}
					}],
					listeners : {
						itemappend : function(view, record, index, eOpts){
							// console.log( view)
						}
					}
				})
				],
				buttons: [{
					text: '저장',
					id: 'save',
					handler: function() {
						
						if(!Ext.getCmp('authNm').getValue()) {
							Ext.MessageBox.alert('Failed', '역활명을 입력하세요.')
							return;
						}

						if(!Ext.getCmp('authSubNm').getValue()) {
							Ext.MessageBox.alert('Failed', '역활설명을 입력하세요.')
							return;
						}

						if(!fp.getForm().getValues()['useYn']) {
							Ext.MessageBox.alert('Failed', '사용여부를 입력하세요.')
							return;
						}
						
						if(map.keys().length == 0){
							Ext.MessageBox.alert('Failed', '권한설정을 입력하세요.')
							return;
						}else{
							for(var i = 0; i<tmpMenuLeng; i++){
								if(map.get(i+2) == undefined || map.get(i+2) == null || map.get(i+2) == "" ){
									Ext.MessageBox.alert('Failed', '권한설정을 입력하세요.')
									return;
								}
							}
						}
						
						if(!tmpAuthId) {
							tmpUrl = context + '/admin/auth/saveAuthInfo.ssc'
						} else {
							tmpUrl = context + '/admin/auth/updateAuthInfo.ssc'
						}

						saveAuthStore.update({
							url: tmpUrl,
							action: 'update',
							params: {
								authNm: Ext.getCmp('authNm').getValue(),
								authSubNm: Ext.getCmp('authSubNm').getValue(),
								useYn: fp.getForm().getValues()['useYn'],
								authId: tmpAuthId,
								controlGubun: map.values().toString(),
							},
							callback: function(records, operation, success) {
								var jsonData = Ext.JSON.decode(operation.response.responseText);

								if(jsonData.result == "F") {
									Ext.MessageBox.alert('Failed', '에러입니다.');
									map.clear();
									return;
								} else {
									Ext.MessageBox.alert('Success', '저장 되었습니다.');
									authList.getStore().reload();
									map.clear();
								}
							}
						})
					}
				}, {
					text: '초기화',
					id: 'reset',
					handler: function() {
						fp.getForm().reset();
						tmpAuthId = "";
					}
				},{
					text: 'expandAll',
					id: 'tree',
					handler: function() {
						tree_panel = Ext.getCmp('authTree');
						console.log(tree_panel);
						console.log(tree_panel.expandAll());
					}
				},{
					text: 'collapseAll',
					id: 'tree1',
					handler: function() {
						tree_panel = Ext.getCmp('authTree');
						console.log(tree_panel);
						tree_panel.collapseAll();
					}
				}],
				listeners : {
					itemappend : function(view,records){

						tree_panel.expandAll()

					}
				}
			});
		}
	}
}();