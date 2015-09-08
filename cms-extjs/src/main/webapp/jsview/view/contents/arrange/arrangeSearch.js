contentsSearch = function () {

	return {
		init: function () {

			//상세화면용 스토리보드화면을 불러온다
			var detailstore = Ext.data.StoreManager.get("detailCategoryTreestore"); 
			detailstore.setProxy({
				type: 'ajax',
				url:  context+'/contents/arrange/findCategoryListForExtJs.ssc'
				,actionMethods:  {create: "POST", read: "GET", update: "POST", destroy: "POST"}
			,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
			,noCache: false
			,reader : {
				type : 'json'
					,root : 'store'
			}
			});
			detailstore.load();
			//컨테츠 화면의 우클리 메뉴를 위해서 브라주져가 제공하는 우클릭 매뉴를 막는다
			if(catalog == 'Y'){
				Ext.getBody().on("contextmenu", Ext.emptyFn, null, {
					preventDefault: true
				});

			}

			//현재 선택된 ctId
			var currentCtId = '';

			//임시 코너 나눔저장소
			var tempCornerList = ''
				
				//우측검색결과 패널
				var gridSearch = Ext.create('Ext.grid.Panel', {

					store: contentsStore,
					cls : 'arrangeColunms',
					selModel: Ext.create('Ext.selection.CheckboxModel'),
					selType: 'cellmodel',                
					autoScroll :  true, 
					id : 'grid',
					columns: [{
						text: '대표화면',
						dataIndex: 'flPath',
						align: 'center',
						flex: 1,
						renderer: function (value) {
							return '<img src="' + value + '"  height=50 width="100">';
						},
						listeners : {
							beforerender : function( view,  eOpts  ){ 
								//버전에따라서 화면에 추가하기전에 해당 컬럼을 hidden 처리한다.

								if(catalog == 'N'){

									view.setHiddenState(true) ;

								}


							}
						}
					}, {
						text: '영상ID',
						dataIndex: 'ctId',
						align: 'center',
						hidden :  true,
						flex: 1
					}, {
						text: '제목',
						dataIndex: 'ctNm',
						style: 'text-align:center',
						flex: 4
					}, {
						text: '영상길이',
						dataIndex: 'ctLeng',
						align: 'center',
						flex: 1
					}, {
						text: '화질',
						dataIndex: 'vdQlty',
						align: 'center',
						flex: 1
					}, {
						text: '화면비',
						dataIndex: 'aspRtoCd',
						align: 'center',
						flex: 1
					}, {
						text: '방송일',
						dataIndex: 'brdDd',
						align: 'center',
						flex: 1
					},{
						text: '등록일',
						dataIndex: 'regDt',
						align: 'center',
						flex: 1
					}, {
						text: '정리상태',
						dataIndex: 'sclNm',
						align: 'center',
						flex: 1
					}],
					height: '100%',
					width: '100%',
					listeners: {

						cellclick: function (view, td, cellIndex, record, tr, rowIndex, e, eOpts) {

							//스트리밍 서비스가 N이면 대표화면이 없어지기때문에 cellindex를 제목위치에 맞게 다시 지정해준다.
							if((cellIndex == 2 && catalog =='Y' )||(cellIndex == 1 && catalog =='N' )) {

								var ctId = this.getSelectionModel().getSelection()[0].data.ctId
								var title = this.getSelectionModel().getSelection()[0].data.ctNm

								//ctId값으로 기본정보 호출
								var callBaseInfo = new Ext.data.Operation({
									action: 'read',
									params: {
										'ctId': ctId,
										'modrId': loginUserId
									}
								});
								if(!Ext.getCmp(ctId)) {
									this.addNewTab(ctId, title);
									var store = contentsBasicStore.load({
										url: context + '/contents/arrange/getContentsBasicInfo.ssc',
										action: 'read',
										params: {
											'ctId': ctId,
											'modrId': loginUserId
										}
									});




									var mediaList = findMediaStore.load({
										url :  context + '/contents/arrange/findMediaList.ssc'  
										,params:{'ctId': ctId}
									});

									//form 필드에 데이터를 set해주기 위해서 json데이터를 parsinng 한다.

									//기본정보 셋팅
									store.on('load', function (st, records, opts) {

										st.each(function (re) {

											var getCtId = re.get("ctId");

											if(ctId = getCtId) {

												var videoMetaForm = Ext.ComponentQuery.query('#viedeoMeta_' + ctId);
												var videoFrom = {
														ctLeng : re.get("ctLeng"),
														aspRtoCd : re.get("aspRtoCd"),
														vdResol : re.get("vdVresol")+"X"+re.get("vdHresol"),
														frmPerSec :re.get("frmPerSec")
												}
												videoMetaForm[0].getForm().setValues(videoFrom);

												var comboTree = Ext.ComponentQuery.query('#treeCombo_' + ctId);
												var rist = Ext.ComponentQuery.query('#rist_' + ctId);
												var metaForm = Ext.ComponentQuery.query('#form_' + ctId); 
												var loadForm = {
														ctNm: re.get("ctNm"),
														episodeNm: re.get("episodeNm"),
														regDt: re.get("regDt"),
														ctLeng: re.get("ctLeng"),
														keyWords: re.get("keyWords"),
														cont: re.get("cont"),
														ctId: re.get("ctId"),
														episodeId: re.get("episodeId"),
														categoryId: re.get("categoryId"),
														rpimgKfrmSeq: re.get("rpimgKfrmSeq"),
														ctLeng : re.get("ctLeng"),
														aspRtoCd : re.get("aspRtoCd"),
														frmPerSec :re.get("frmPerSec"),
														vdQlty :re.get("vdQlty"),
														dataStatNm :re.get("dataStatNm") 
												}

												comboTree[0].setValue('' + re.get("categoryNm") + '');
												rist[0].setValue(re.get('ristClfCd'));
												metaForm[0].getForm().setValues(loadForm);

												//카테고리별 에피소드 추가

												episodeStore.load({
													url : context+'/contents/arrange/getEpisodeSearch.ssc',
													action: 'read',
													params: {
														'categoryId': re.get("categoryId"),
														'pageNo' : 1
													}
												});
												
												ristClfStore.load({
													url : context+'/contents/arrange/findSclListForClf.ssc',
													action: 'read',
													params: {
														'clfCd': "RIST" 
													}
												});

											}

										})
									})


									//프로퍼티의 값이 Y인경우에만 플레이를 한다.
									if(streaming == 'Y'){
										//palyer url 경로 받아온다.
										var count=0;
										var player = playerStore.load({
											url: context + '/clip/getVideoPlayInfo.ssc',
											action: 'read',
											params: {
												'ctId': ctId,
												'modrId': loginUserId
											}
										});
										player.on('load', function (st, records, opts) {

											st.each(function (re) {

												var getCtId = re.get("ctId");
												src = re.get("fullPath") + ".mp4"

												//store에서 조회한 ctId와 현재 클릭한 ctId와 동일 할때만 해당 탭을 열어준다.
												if(count==0){
													if(getCtId == ctId) {
														var videoPanel = Ext.ComponentQuery.query('#video_' + ctId);

														videoPanel[0].update('<video id="videoplay_' + ctId + '"  width="100%" height ="160" controls="true"  name="Player"  ><source src="' + src + '" type="video/mp4"></video>');
														var video = document.getElementById('videoplay_'+ctId);

														video.play();
														count++;
													}
												}
											})
										})
									}

								} else {
									//기존에 존재하는 텝을 다시 보여준다.
									Ext.getCmp(ctId).show();
								}
							}
						},
						afterrender: function (view, eOpts) {
							//초반 그리드 화면의 버튼을 권한에 따라 활성화 비활성화 한다.
							var disCardButton = Ext.ComponentQuery.query('#insertDisCard');
							var cancleErrorButton = Ext.ComponentQuery.query('#cancleError');
							if(perm != 'RW') {
								disCardButton[0].setDisabled(true);
								cancleErrorButton[0].setDisabled(true);
							}
						}

					},
					addNewTab: function (ctId, title) {
						var newTab = mainView.add(

								Ext.create('Ext.tab.Panel', {

									id: ctId,
									plain: true,
									title: title,
									closable: true,  
									cls : 'customFormBackground',
									items: [{
										xtype: 'panel',
										hidden: true,
										layout: 'vbox',
										autoScroll : true,
										border : false,
										items: [{
											width: '100%',
											id : 'tabItem_'+ctId,
											height : 275,
											layout: 'hbox',
											items: [{
												title: '영상',
												height: '100%', 
												width : 300,
												border : false,

												items : [{
													id: 'video_' + ctId
													,border : false
													,flex :1 
												},{
													id : 'viedeoMeta_' + ctId
													,xtype : 'form'
														,width : '100%'
															,border : false
															,items : [ Ext.create('Ext.form.Panel', {
																layout: 'hbox',                                              
																border:false ,
																padding : '10',
																items: [{
																	xtype: 'textfield',
																	fieldLabel: '영상길이', 
																	labelWidth : 60,
																	labelSeparator: '', 
																	width : 140, 
																	readOnly : true,
																	name: 'ctLeng'
																}, {
																	xtype: 'textfield',
																	fieldLabel: '화면비',   
																	labelSeparator: '',
																	margin : '0 0 0 20',
																	labelWidth : 75,
																	readOnly : true,
																	width : 120, 
																	name: 'aspRtoCd'
																}]
															}),
															Ext.create('Ext.form.Panel', {
																layout: 'hbox',
																padding : '10',
																bodyStyle:  'border-width: 0px' ,
																items: [{
																	xtype: 'textfield',
																	fieldLabel: '해상도',
																	width : 140, 
																	readOnly: true,
																	labelSeparator: '', 
																	labelWidth : 60,
																	name: 'vdResol'
																}, {
																	xtype: 'textfield',
																	fieldLabel: '초당프레임',  
																	labelSeparator: '',
																	labelWidth : 75,
																	margin : '0 0 0 20',
																	readOnly: true,
																	width :120, 
																	name: 'frmPerSec'
																}]
															})
															]
												}]
											,listeners :{
												beforerender : function( view,  eOpts  ){ 
													//버전에따라서 화면에 추가하기전에 해당 컬럼을 hidden 처리한다.

													if(streaming == 'N'){

														view.setHiddenState(true) ; 
													}


												}
											}
											}, {
												title : '프로파일 리스트' ,
												margin: '0 0 5 5',
												width  :200,

												items : [{
													xtype : 'grid',
													viewConfig: {
														loadMask: false
													},
													border : false,
													store: findMediaStore,
													id : ctId+'_mediaGrid',
													columns: [{

														text: '프로파일',
														dataIndex: 'flExt',
														sortable: false, 
														align: 'center', 
														flex: 4

													}, {
														text: '영상인스턴스ID',
														dataIndex: 'ctiId',
														align: 'center',
														hidden :  true,
														flex: 1
													}, {
														text: '영상ID',
														dataIndex: 'ctId',
														align: 'center',
														hidden :  true,
														flex: 1
													}],
													listeners: {
														cellclick: function (view, td, cellIndex, record, tr, rowIndex, e, eOpts) {


															if(record.data.flExt != ''){
																var media = getMediaStore.load({
																	url :  context + '/contents/arrange/getMediaInfo.ssc'  
																	,params:{'ctId': ctId,'ctiFmt':record.data.ctiFmt}
																});

															}
														},
														cellcontextmenu : function(view, td, cellIndex, record, tr, rowIndex, e, eOpts){
															 
															console.log(record);
															var ctiId = record.data.ctiId
															var ctId = record.data.ctId
															//프로파일 리스트 우클릭 메뉴
															var profileContextMenu = Ext.create('Ext.menu.Menu',{
																items : [{
																	text: '대표영상지정',
																	id : 'rpContents'
																}],
																listeners: {
																	click: function (menu, item, e) {
																		  findMediaStore.update({
																			url :  context + '/contents/arrange/updateRpContent.ssc'  
																			,params:{'ctiId': ctiId,'ctId': ctId}
																		});
																	}
																}
															
															});
															
															if(record.data.ctiId != 0){																
																profileContextMenu.showAt(e.getXY());
															}
														}
													}
												}]
											,listeners :{
												beforerender : function( view,  eOpts  ){ 
													//버전에따라서 화면에 추가하기전에 해당 컬럼을 hidden 처리한다.

													if(streaming == 'N'){

														view.setHiddenState(true) ; 
													}


												}
											}
											}, {
												title: '기본정보',

												flex: 3,
												xtype: 'form',
												id: 'form_' + ctId,
												layout: 'vbox',
												margin: '0 0 3 3',
												autoScroll : true,
												bodyStyle : {
													"border-width" : "0 0 0 0" 
												},

												items: [{
													xtype : 'panel',
													layout : 'hbox',
													width : '100%',
													border :false,
													id: 'dock_' + ctId,
													defaults : {
														margin : '5'
													},
													items : [{ xtype: 'tbfill' },
													         {
														xtype : 'button',
														text : '첨부파일 등록',
														handler: function () {
															var win = Ext.create('Ext.window.Window',{
																title : '첨부파일 등록'
																,items : [Ext.create('Ext.form.Panel', { 
																    width: 400,
																    bodyPadding: 10,
																    frame: false ,
																    items: [{
																        xtype: 'filefield',
																        name: 'uploadFile', 
																        fieldLabel: '파일',
																        labelWidth: 50,
																        msgTarget: 'side',
																        allowBlank: false,
																        anchor: '100%',
																        buttonText: '찾기'
																    }],

																    buttons: [{
																        text: 'Upload',
																        handler: function() {
																            var form = this.up('form').getForm();
																            console.log(form);
																            if(form.isValid()){
																                form.submit({
																                    url: context+'/contents/arrange/uploadFile.ssc',
																                    method: 'POST',  
																                    headers: {'Content-Type': 'multipart/form-data'},
																                    params : {'ctId' : ctId},
																                    waitMsg: '첨부파일을 올리는 중입니다.....',
																                    
																                    failure:function(form, result){
																                    	 Ext.Msg.alert('성공', '첨부파일 등록에 성공하였습니다');
																                    	win.close();
														                            }
																                });
																            }
																        }
																    }]
																})]
																
															}).show();
														
														}
											         },{
														xtype : 'button',
														text : '아카이브 요청',
														handler: function () {
															 
								 

															contentsArchiveStore.update({
																url : context+'/contents/arrange/insertArchiveContent.ssc',
																action: 'update',
																params: {
																	'ctId' : ctId,
																	'userId' : loginUserId
																},
																callback: function(records, operation, success) {
																	var obj = Ext.JSON.decode(operation.response.responseText);

																	if(obj.result =='N'){
																		Ext.Msg.alert('실패',obj.reason)
																	}else if(obj.result =='E'){
																		Ext.Msg.alert('알림',obj.reason)
																	}else{
																		Ext.Msg.alert('알림','아카이브 요청 되었습니다.')

																	}

																},  scope : this

															}); 

														}
											         },{
																xtype : 'button',
																text : '다운로드 요청',
																handler: function () {
																	 
										 

																	contentsArchiveStore.update({
																		url : context+'/contents/arrange/insertDownloadContent.ssc',
																		action: 'update',
																		params: {
																			'ctId' : ctId,
																			'userId' : loginUserId
																		},
																		callback: function(records, operation, success) {
																			var obj = Ext.JSON.decode(operation.response.responseText);

																			if(obj.result =='N'){
																				Ext.Msg.alert('실패',obj.reason)
																			}else if(obj.result =='F'){
																				Ext.Msg.alert('알림',obj.reason)
																			}else{
																				Ext.Msg.alert('알림','다운로드 요청 되었습니다.')

																			}

																		},  scope : this

																	}); 

																}
													         },
													         {
														xtype: 'button',
														text: '저장',
														iconAlign: 'right',
														handler: function () {
															
															//메타 정보 저장.
															var metaForm = Ext.ComponentQuery.query('#form_' + currentCtId)
															var comboTree = Ext.ComponentQuery.query('#treeCombo_' + currentCtId);
															var rist = Ext.ComponentQuery.query('#rist_' + currentCtId);
														 
															var ctNm = metaForm[0].getForm().findField('ctNm').getSubmitValue();
															var categoryId = metaForm[0].getForm().findField('categoryId').getSubmitValue();
															var episodeId = metaForm[0].getForm().findField('episodeId').getSubmitValue();
															var cont = metaForm[0].getForm().findField('cont').getSubmitValue();
															var keyword = metaForm[0].getForm().findField('keyWords').getSubmitValue();
															var ctId = metaForm[0].getForm().findField('ctId').getSubmitValue();
															var rpImg =  metaForm[0].getForm().findField('rpimgKfrmSeq').getValue();
															var modrId = loginUserId;
															var ristClfCd = rist[0].getValue();
															//treeCombo에서 기초값을 셋팅할때 한글로 되고 있음으로 값이 바뀌지 않았다면  categoryId필드에 저장되어있는 값으로 저장한다.
															if(!isNaN(comboTree[0].getValue())) {
																categoryId = comboTree[0].getValue()
															}
															
															//ctId값으로 기본정보 호출

															contentsBasicStore.update({
																url : context+'/contents/arrange/updateBasicInfo.ssc',
																action: 'update',
																params: {
																	'ctNm': ctNm,
																	'categoryId': categoryId,
																	'episodeId': episodeId, 
																	'cont': cont,
																	'keyword': keyword,
																	'modrId': modrId,
																	'ctId': ctId,
																	'segmentId': 1,
																	'rpImg' : rpImg,
																	'ristClfCd' :  ristClfCd


																},
																callback: function(records, operation, success) {
																	var obj = Ext.JSON.decode(operation.response.responseText);

																	if(obj.result =='N'){
																		Ext.Msg.alert('실패',obj.reason)
																	}else{
																		Ext.Msg.alert('알림','수정 되었습니다.')

																	}

																},  scope : this

															}); 

															if(catalog == 'Y'){

																/*
																 * 코너정보를 저장한다. 저장을 함과 동시에 스토리보드 관련 데이터를 모두 수정후 다시 저장한다.

																 */
																//코너 나누기,합치기의 정보를 모두 beans에 담는다.

																var select = document.getElementsByName('storyboardCheck');

																for(var i = 0; i < select.length; i++) {

																	//현재 스토리보드에 뿌려진 모든 id 값을 map에 넣는다.                        	
																	totalStoryBoard.put(select[i].value, select[i].value);

																}



																var cornerKeys = cornerMap.keys();

																var totalImg = totalStoryBoard.keys();
																var titles = ''
																	var cnConts = ''
																		var dividImgs = ''
																			var showImgs = ''
																				for(var i = 0; i < cornerKeys.length; i++) {

																					var tempCorner = Ext.ComponentQuery.query('#temp_' + cornerKeys[i]);
																					var title = tempCorner[0].header.title;
																					var targetCont = Ext.ComponentQuery.query('field[name=cnCont_' + cornerKeys[i] + ']');

																					titles += title + ',';
																					cnConts += targetCont[0].getValue() + ',';
																					dividImgs += cornerKeys[i] + ',';
																				}

																//화면에 뿌려진 스토리 보드 정보 모두 beans에 담는다.
																
																for(var i = 0; i < totalImg.length; i++) {

																	showImgs += totalImg[i] + ',';
																}

																contentsCornerStore.update({
																	url: context + '/contents/arrange/updateStoryBoardInfo.ssc',
																	action: 'update',
																	params: {

																		'dividImgs': dividImgs,
																		'ctId': ctId,
																		'cnNm': titles,
																		'cnCont': cnConts,
																		'showImgs': showImgs

																	}

																})

															}


														}},{

															xtype: 'button',
															text: '정리완료',
															iconAlign: 'right',
															handler: function () {
																var metaForm = Ext.ComponentQuery.query('#form_' + currentCtId)
																var ctId = metaForm[0].getForm().findField('ctId').getSubmitValue();
																var modrId = loginUserId;


																arrangeCompleteStore.update({
																	url :  context+'/contents/arrange/updateCompleteArrange.ssc',
																	action: 'update',
																	params: {

																		'modrId': modrId,
																		'ctId': ctId,
																		'dataStatCd': '002'

																	},
																	callback: function(records, operation, success) {
																		var obj = Ext.JSON.decode(operation.response.responseText);
																		if(obj.result =='N'){
																			Ext.Msg.alert('실패',obj.info)
																		}else{
																			Ext.Msg.alert('알림','수정 되었습니다.')

																		}

																	},  scope : this

																});
															}
														},{

															xtype: 'button',
															text: '오류등록',
															iconAlign: 'right',
															handler: function () {
																var metaForm = Ext.ComponentQuery.query('#form_' + currentCtId)
																var ctId = metaForm[0].getForm().findField('ctId').getSubmitValue();
																var modrId = loginUserId;


																errorRegistStore.update({
																	url :  context+'/contents/arrange/updateErrorArrange.ssc',
																	action: 'update',
																	params: {

																		'modrId': modrId,
																		'ctId': ctId,
																		'dataStatCd': '003'

																	},
																	callback: function(records, operation, success) {
																		var obj = Ext.JSON.decode(operation.response.responseText);
																		if(obj.result =='N'){
																			Ext.Msg.alert('실패',obj.info)
																		}else{
																			Ext.Msg.alert('알림','수정 되었습니다.')

																		}

																	},  scope : this

																});
															}

														}]
												},{
													xtype: 'textfield',
													fieldLabel: '제목',
													labelSeparator: '',
													width: '90%',
													margin: '5 0 0 0',
													name: 'ctNm'
												},
												Ext.create('Ext.form.Panel', {
													layout: 'hbox',
													margin: '5 0 0 0',
													bodyStyle: 'border-width: 0px',

													items: [Ext.create('Ext.ux.TreeCombo', {
														fieldLabel: '카테고리',
														labelSeparator: '',
														treeHeight: 10,
														treeWidth: 240,
														id: 'treeCombo_' + ctId,
														store: detailstore,
														selectChildren: false,
														canSelectFolders: true,
														name: 'categoryNm',
														listeners: {
															change: function (view, newValue, oldValue, eOpts) {

																var categoryId = view.getValue();
															
																//카테고리id의 값이 문자가 아닐경우에만 로드
																if(!isNaN(categoryId)) {

																	episodeStore.load({
																		url : context+'/contents/arrange/getEpisodeSearch.ssc',
																		action: 'read',
																		params: {
																			'categoryId': categoryId,
																			'pageNo' : 1
																		}
																	});
																}
															}
														}

													}), {
														xtype: 'combobox',
														fieldLabel: '회차',
														labelSeparator: '',
														store: episodeStore,
														editable: false,
														margin: '0 0 0 40',
														queryMode: 'local',
														displayField: 'episodeNm',
														valueField: 'episodeId',
														name: 'episodeNm'
													}]
												})

												, Ext.create('Ext.form.Panel', {
													layout: 'hbox',
													margin: '5 0 0 0',
													bodyStyle: 'border-width: 0px',

													items: [{
														xtype: 'textfield',
														fieldLabel: '등록일',
														labelSeparator: '',
														readOnly: true,
														name: 'regDt'
													}, {
														xtype: 'combobox',
														fieldLabel: '사용등급',
														labelSeparator: '',
														store: ristClfStore,
														editable: false,
														margin: '0 0 0 40',
														queryMode: 'local',
														displayField: 'sclNm',
														valueField: 'sclCd',
														name: 'ristClfNm',
														id : 'rist_'+ctId
													}]
												}) , Ext.create('Ext.form.Panel', {
													layout: 'hbox',
													margin: '5 0 0 0',
													bodyStyle: 'border-width: 0px', 
													hidden : true,
													items: [{
														xtype: 'textfield',
														fieldLabel: '화질',
														labelSeparator: '',
														readOnly: true,
														name: 'vdQlty'
													}, {
														xtype: 'textfield',
														fieldLabel: '화면비',
														labelSeparator: '',
														margin: '0 0 0 40',
														readOnly: true,
														name: 'aspRtoCd'
													}],
													listeners : {
														beforerender : function( view,  eOpts  ){ 
															//버전에따라서 화면에 추가하기전에 해당 컬럼을 hidden 처리한다.

															if(streaming == 'N' || catalog == 'N'){

																view.setHiddenState(false) ; 
															}


														}
													}
												})
												, Ext.create('Ext.form.Panel', {
													layout: 'hbox',
													margin: '5 0 0 0',
													bodyStyle: 'border-width: 0px',
													hidden : true, 
													items: [{
														xtype: 'textfield',
														fieldLabel: '정리상태',
														labelSeparator: '',
														readOnly: true,
														name: 'dataStatNm'
													}, {
														xtype: 'textfield',
														fieldLabel: '초당프레임',
														labelSeparator: '',
														margin: '0 0 0 40',
														readOnly: true,
														name: 'frmPerSec'
													}],
													listeners : {
														beforerender : function( view,  eOpts  ){ 
															//버전에따라서 화면에 추가하기전에 해당 컬럼을 hidden 처리한다.

															if(streaming == 'N' || catalog == 'N'){

																view.setHiddenState(false) ; 
															}


														}
													}
												})
												, {
													xtype: 'textfield',
													fieldLabel: '검색키워드',
													labelSeparator: '',
													margin: '5 0 0 0',
													width: '90%',
													name: 'keyWords'
												}, {
													xtype: 'textareafield',
													fieldLabel: '특이사항',
													labelSeparator: '',
													margin: '5 0 0 0',
													width: '90%',

													name: 'cont'
												}, {
													xtype: 'hiddenfield',
													name: 'categoryId'

												}, {
													xtype: 'hiddenfield',
													name: 'ctId'

												}, {
													xtype: 'hiddenfield',
													name: 'episodeId'

												}, {
													xtype: 'hiddenfield',
													name: 'rpimgKfrmSeq'

												}, {
													xtype: 'hiddenfield',
													name: 'ristClfCd'

												}
												]
											}]   
										,listeners :{
											beforerender : function( view,  eOpts  ){ 
												//버전에따라서 화면에 추가하기전에 해당 컬럼을 hidden 처리한다.
											
												if(catalog == 'N'){                								
													var mainHeight = Ext.ComponentQuery.query('#arrangeLeft');                								         
													var formPanel = Ext.ComponentQuery.query('#form_'+ctId)

													formPanel[0].margin = '0 0 0 0'	
													view.setHeight(mainHeight[0].getHeight()-25); 
													
												}else if(catalog == 'Y' && streaming == 'N'){
													var formPanel = Ext.ComponentQuery.query('#form_'+ctId)
													formPanel[0].margin = '0 0 0 0'	
													 
													 view.setHeight(320); 
												}


											}
										}
										}, {
											title: '스토리보드',
											width: '100%', 
											flex: 3,
											autoScroll: true,
											id: 'storyBoard_' + ctId,
											items: Ext.create('Ext.view.View', {
												store: Ext.create('Ext.data.Store', {
													model : 'StoryBoard'
														,proxy : {
															type : 'ajax'
																// ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
																,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
												,noCache: false 
												,url : context+'/contents/arrange/getContentsStroyboardInfoForExtJs.ssc'
												,extraParams:{'ctId': ctId}
												,reader : {
													type : 'json'
														,root: 'storyboard' 

												}
														}
												,autoLoad : true 
												,listeners : {
													load : function(view,records){

														if(records[0] != undefined){
															var corner = contentsCornerStore.load({
																url : context + '/contents/arrange/getContentsSearchStroyboardCornerInfo.ssc',
																action: 'read',
																params: {'ctId' :  records[0].data.ctId

																}
															});  
														}
													} 
													
												}

												}),
												id: 'view_' + ctId,
												padding: '5 5 5 48',
												height  : 400,
												 
												tpl: [
												      '<tpl for=".">',
												      '<div class="thumb-wrap" id="{img}_' + ctId + '" style="float:left;margin:10px;padding:3px">',
												      '<tpl if="img == rpImg">',
												      '<div class="thumb" id="{img}_background" style="background:#CD1039;padding:3px; ">',
												      '<tpl else>',
												      '<div class="thumb"id="{img}_background" style="background:#dddddd;padding:3px;">',
												      '</tpl>',
												      '<a href="#"><input name="storyboardCheck" type="checkbox" value="{img}" style ="position: absolute" onclick="console.log(\'dddd\');"><img src="{url}" style="height:70px;width:120px" ></a></div>',
												      '<span class="x-editable"  style="margin-left:30px;">{ctLeng:htmlEncode}</span>',
												      '</div>',
												      '</tpl>',
												      '<div class="x-clear"></div>'
												      ],
												      trackOver: true,
												      overItemCls: 'x-item-over',
												      itemSelector: 'div.thumb-wrap',
												      emptyText: '뿌려질 이미지가 존재하지 않습니다',
												      listeners: {

												    	  itemclick: function (view, record, item, index, e) {
												    		 
												    		    
												    		  //jquery로 스토리보드의 스크롤 길이정보를 구한다음 dataview 테이블에 저장하도록 한다.
												    		 
												    		  
												    		  var video = document.getElementById('videoplay_' + ctId);
												    		  video.currentTime = record.data.duration;
												    		 
												    		  video.play();
												    		 
												    		  var metaForm = Ext.ComponentQuery.query('#form_' + currentCtId)

												    		  var select = document.getElementsByName('storyboardCheck');
												    		  //체크박스의 정보가 true이라면 선택 정보 표기
												    		  var oldRpImg = metaForm[0].getForm().findField('rpimgKfrmSeq').getSubmitValue();
												    		  for(var i = 0; i < select.length; i++) {
												    			  if(select[i].checked == true) {
												    				  $jq('#'+select[i].value + "_background").css("background","#28B4B4");
												    				  var Background = document.getElementById(select[i].value + "_background")
												    				  Background.style = 'background:#28B4B4;padding:3px;'
												    				 
												    			  } else if(select[i].value == oldRpImg) {
												    				  $jq('#'+select[i].value + "_background").css("background","#CD1039");
												    				  var Background = document.getElementById(select[i].value + "_background")
												    				  Background.style = 'background:#CD1039;padding:3px;'
												    				 
												    			  } else {
												    				  $jq('#'+select[i].value + "_background").css("background","#dddddd");
												    				  var Background = document.getElementById(select[i].value + "_background")
												    				  Background.style = 'background:#dddddd;padding:3px;'
												    			 
												    			  }
												    		  }
												    		  return false;
												    	  },
												    	  
												    	  itemcontextmenu :function( view, record, item, index, e, eOpts ){

												    		  var viewId = view.id.split('_');
												    		  var select = document.getElementsByName('storyboardCheck');
												    		  var ctId = viewId[1];

												    		  var isChecked = false
												    		  var check = 0;
												    		  for(var i = 0; i < select.length; i++) {
												    			  if(select[i].checked == true) {
												    				  isChecked = true
												    				  check++;
												    			  }
												    		  }

												    		  if(isChecked) {
												    			  if(check >= 2) {
												    				  Ext.getCmp('setRpImg').setDisabled(true);
												    				  Ext.getCmp('dividCorner').setDisabled(true);
												    				  Ext.getCmp('unionCorner').setDisabled(true);
												    			  } else {
												    				  Ext.getCmp('delImg').setDisabled(false);
												    				  Ext.getCmp('setRpImg').setDisabled(false);
												    				  Ext.getCmp('dividCorner').setDisabled(false);
												    				  Ext.getCmp('unionCorner').setDisabled(false);
												    			  }

												    			  contextMenu.showAt(e.getXY());
												    		  }
												    	  }
												      }

											})
											,listeners :{
												beforerender : function( view,  eOpts  ){ 
													//버전에따라서 화면에 추가하기전에 해당 컬럼을 hidden 처리한다.

													if(catalog == 'N'){

														view.setHiddenState(true) ; 
													}


												}
											}
										}]
									}],
									listeners: {
										afterrender: function (view, eOpts) {

											//초반 그리드 화면의 버튼을 권한에 따라 활성화 비활성화 한다.
											var tabDock = Ext.ComponentQuery.query('#dock_' + ctId);

											if(perm != 'RW') {
												tabDock[0].setDisabled(true);
												// tabDock[0].setVisible(false);
											}
										},

										close : function( panel, eOpts ){
											
											if( catalog == 'Y' && streaming == 'Y'){
												var video = document.getElementById('videoplay_'+ctId);
												video.pause();
											}
										}

									}

								})
						).show()

						mainView.setActiveTab(newTab);
					},
					dockedItems: [{
						xtype: 'pagingtoolbar',
						store: contentsStore,

						dock: 'bottom',
						layout: 'hbox',
						items: [{
							text: '폐기신청',
							itemId: 'insertDisCard',
							handler: function () {
								var selectRow = gridSearch.getSelectionModel().getSelection();
								var selected = "";
								
								//선택된 값을 ','로 구분지어서 string으로 만든다.
								Ext.each(selectRow, function (item) {
									selected += item.data.ctId + ","
								});
								if(selected == '') {
									Ext.Msg.alert('알림', '체크된 정보가 없습니다.');
								} else {
									discardStore.update({
										url : context + '/contents/discard/insertDisUse.ssc',
										action: 'update',
										params: {
											'modrId': loginUserId,
											'ctIds': selected

										},
										callback: function(records, operation, success) {
											var obj = Ext.JSON.decode(operation.response.responseText);
											if(obj.result =='N'){
												Ext.Msg.alert('실패',obj.errorCont)
											}else{
												Ext.Msg.alert('알림','폐기신청되었습니다')
												gridSearch.getSelectionModel().deselectAll();
											}

										},  scope : this
									});  


									contentsStore.reload();
								}
							}
						}, {
							text: '오류취소',
							itemId: 'cancleError',
							handler: function (view) {
								var selectRow = gridSearch.getSelectionModel().getSelection();
								var selected = "";
								//선택된 값을 ','로 구분지어서 string으로 만든다.
								Ext.each(selectRow, function (item) {
									selected += item.data.ctId + ","
								});

								if(selected == '') {
									Ext.Msg.alert('알림', '체크된 정보가 없습니다.');
								} else {

									discardStore.destroy({
										url : context + '/contents/discard/cancleDisUse.ssc',
										action: 'update',
										params: {
											'modrId': loginUserId,
											'ctIds': selected

										},
										callback: function(records, operation, success) {
											var obj = Ext.JSON.decode(operation.response.responseText);
											if(obj.result =='N'){
												Ext.Msg.alert('실패',obj.info)
											}else{
												Ext.Msg.alert('알림','ㅇ류취소 되었습니다')
												gridSearch.getSelectionModel().deselectAll();
											}

										},  scope : this
									});  


									contentsStore.reload();
								}
							}
						}],
						listeners: {
							afterrender : function() {
								this.child('#refresh').hide();

								var mainHeight = Ext.ComponentQuery.query('#arrangeLeft');
								var tab = Ext.ComponentQuery.query('#grid'); 
								tab[0].setHeight(mainHeight[0].getHeight()-25);

							},
							beforechange: function (view, page, eOpts) {

								var keyword = Ext.getCmp('keyword').getValue();
								var fromDate = Ext.getCmp('fromDate').getValue();
								var toDate = Ext.getCmp('toDate').getValue();
								var cateogory = Ext.getCmp('categoryTree').getSelectionModel().getSelection(); //treepanel.getSelectionModel().getSelection();
								var categroyId = '';
								var ristClfCd = Ext.getCmp('ristClfCd').getValue();
								var ctCla = Ext.getCmp('ctCla').getValue();
								var ctTyp = Ext.getCmp('ctTyp').getValue();

								if(cateogory.length == 0 || cateogory[0].data.id == 'total') {
									categroyId = 0
								} else {
									categroyId = cateogory[0].data.id
								}

								var tem = contentsStore.load({
									url: context + '/contents/findSearchList.ssc',
									action: 'read',
									params: {
										'keyword': keyword,
										'startDt': fromDate,
										'endDt': toDate,
										'categoryId': categroyId,
										'ristClfCd': ristClfCd,
										'ctCla': ctCla,
										'ctTyp': ctTyp,
										'searchTyp': 'image',
										'pageNo': page
									}
								});
								//	gridSearch.show();

							}
						}
					}]

				});

		
			//스토리보드 우클릭 메뉴
			var contextMenu = Ext.create('Ext.menu.Menu', {
				items: [{
					text: '대표화면지정',
					id: 'setRpImg'
				}, {
					text: '코너나누기',
					id: 'dividCorner'
				}, {
					text: '샷삭제',
					id: 'delImg'
				}, {
					text: '코너합치기',
					id: 'unionCorner'
				}],
				listeners: {
					click: function (menu, item, e) {
						//현재 선택된 form의 정보를 가져온다.
						var metaForm = Ext.ComponentQuery.query('#form_' + currentCtId)

						//현재 화면에 뿌려진 스토리보드의 checkbox정보를 모은다.
						var select = document.getElementsByName('storyboardCheck');
						var ctId = metaForm[0].getForm().findField('ctId').getSubmitValue();
						var oldRpImg = metaForm[0].getForm().findField('rpimgKfrmSeq').getSubmitValue();

						var checkedNum = ''
							
							for(var i = 0; i < select.length; i++) {

								//현재 스토리보드에 뿌려진 모든 id 값을 map에 넣는다.                        	
								totalStoryBoard.put(select[i].value, select[i].value);
								if(select[i].checked == true) {

									checkedNum += select[i].value + ',';
								}
							}
					
						//메뉴id별로 구분 동작하도록 만든다.
						if(item.id == 'setRpImg') {
							var length = String(checkedNum).length
							var rpImg = String(checkedNum).substr(0, length - 1);

							if(oldRpImg != rpImg) {
								metaForm[0].getForm().findField('rpimgKfrmSeq').setValue(rpImg);
						

								//기존 대표화면의 _background 색상을 변경한다.
								$jq('#'+oldRpImg + "_background").css("background","#dddddd");
								var oldRpImgBackground = document.getElementById(oldRpImg + "_background")
								oldRpImgBackground.style = 'background:#dddddd;padding:3px;'
									
								//신규 대표화면의 _background의 색상을 치환한다
								var newRpImgBackground = document.getElementById(rpImg + "_background")
								$jq('#'+rpImg + "_background").css("background","#CD1039");
								newRpImgBackground.style = 'background:#CD1039;padding:3px;'
								
							} else {
								Ext.Msg.alert('알림', '이미 대표화면으로 지정된 영상입니다.')
							}

							//check값이 true인값을 모두 false로 변경
							for(var i = 0; i < select.length; i++) {
								if(select[i].checked == true) {

									select[i].checked = false;
								}
							}

						} else if(item.id == 'dividCorner') {
							var length = String(checkedNum).length
							var cornerImg = String(checkedNum).substr(0, length - 1);

							var temp = document.getElementById(cornerImg + '_' + ctId);

							var temp_coninfo = '<div id="cornerFill_' + cornerImg + '_' + ctId + '" style="float:left;width:100%"><div   style ="float:left;width:100%"><div id = cn_' + cornerImg + '_' + ctId + '></div></div>'

							//''+value+'' 형식으로 넣어줘야만 정상적으로 값인식.
							var tempId = '' + cornerImg + '_' + ctId + ''

							//코너의 시작 점으로 지정된  obect앞에 생성한 코너 정보를 삽입한다.
							Ext.get(tempId).insertHtml('beforeBegin', temp_coninfo)

							//코너로 나누어진 img의 값을 mapp에 넣는다.
							cornerMap.put(cornerImg, cornerImg);

							//코너내용을 생성한다.
							var tempCorner = Ext.create('Ext.panel.Panel', {
								title: '코너제목',
								itemId: 'temp_' + cornerImg,
								height: 100,
								xtype: 'form',
								items: [{
									xtype: 'textareafield',
									width: '100%',
									height: '100%',
									readOnly: true,
									name: 'cnCont_' + cornerImg
								}],
								tools: [{
									type: 'gear',
									handler: function () {
										Ext.create('Ext.window.Window', {
											title: '정보 변경',
											width: 280,
											height: 180,
											layout: 'form',
											items: [{
												xtype: 'textfield',
												margin: '5',
												fieldLabel: '코너제목',
												name: 'changeCnNm'
											}, {
												xtype: 'textareafield',
												margin: '5',
												fieldLabel: '코너 내용',
												name: 'changeCnCont'
											}],
											buttons: [{
												text: '편집',
												handler: function () {
													var cornerName = Ext.ComponentQuery.query('field[name=changeCnNm]');
													var cornerCont = Ext.ComponentQuery.query('field[name=changeCnCont]');

													var targetPanel = Ext.ComponentQuery.query('#temp_' + cornerImg);
													var targetCont = Ext.ComponentQuery.query('field[name=cnCont_' + cornerImg + ']');

													targetPanel[0].setTitle(cornerName[0].getValue());
													targetCont[0].setValue(cornerCont[0].getValue());
													var me = Ext.ComponentQuery.query('window');
													me[1].close();
												}
											}]

										}).show();
									}
								}],
								renderTo: 'cn_' + cornerImg + '_' + ctId
							});

							//check값이 true인값을 모두 false로 변경
							for(var i = 0; i < select.length; i++) {
								if(select[i].checked == true) {

									select[i].checked = false;
								}
							}
							tempCornerList += cornerImg + ','

						} else if(item.id == 'delImg') {
							var length = String(checkedNum).length
							var deleleteImgs = String(checkedNum).substr(0, length - 1);
							var deleleteImg = deleleteImgs.split(',')
						
							for(var i = 0; i < deleleteImg.length; i++) {
								if(oldRpImg != deleleteImg[i]) {
									totalStoryBoard.remove(deleleteImg[i])
									var temp = document.getElementById(deleleteImg[i] + '_' + ctId);
									//시간대 표기와 이미지 정보를 삭제하기위해서 두번 삭제한다
									temp.style='';
									temp.removeChild(temp.firstChild);
									temp.removeChild(temp.firstChild);
								} else {
									Ext.Msg.alert('알림 ', '대표화면으로 지정된 영상이 포함되어있습니다. 대표영상은 삭제 하실수 없습니다.')
								}
							}
						
							var Info = new Ext.data.Operation({
								action: 'destroy',
								params: {
									'ctId': ctId,
									'deleteImgs': deleleteImgs

								}
							});

						} else if(item.id == 'unionCorner') {
							var length = String(checkedNum).length
							var cornerImg = String(checkedNum).substr(0, length - 1);

							var temp = document.getElementById('cornerFill_' + cornerImg + '_' + ctId);
							temp.removeChild(temp.firstChild);
							temp.setAttribute('style', '');
							temp.setAttribute('id', '');
							
							cornerMap.remove(cornerImg)
							
							//check값이 true인값을 모두 false로 변경
							for(var i = 0; i < select.length; i++) {
								if(select[i].checked == true) {

									select[i].checked = false;
								}
							}
						}

					}
				}
			})

			//우측패널
			var mainView = Ext.create('Ext.tab.Panel', {
				//  width : '100%'
				height: 700,
				margin: '0 5 0 0',
				plain: true,
				id : 'arrangeRight',
				renderTo: 'body',
				bodyStyle: {
					'border-width': '0 0 0 0'
				},
				items: [{
					title: '검색결과',
					id: 'searchResult',
					items: [gridSearch]
				}],
				listeners: {
					tabchange: function (tabPanel, newCard, oldCard, eOpts) {

						//코너를 나눈 정보가 존재한다면 삭제 금지.
						if(tempCornerList != '') {
							tempCornerList = '';
						}
						//플레이중인 비디오를 멈춘다
						if(newCard.id != 'searchResult') {                         
							currentCtId = newCard.id
							//다른텝을 열었다면 자동으로 플레이 중지를 시킨다.
							var preVideo = document.getElementById('videoplay_' + oldCard.id);
							if(preVideo != null && catalog == 'Y') {
								preVideo.pause();
							}

							//코너 나눈 정보를 초기화한다
							cornerMap = new Map();
							totalStoryBoard = new Map();
						}

					}
				}

			})

			Ext.EventManager.onWindowResize(function () {
				mainView.setSize(undefined, undefined)
			});
		}
	}
}();