ClipSearch = function () {

	return {
		init: function () {

			//우측검색결과 패널
			var gridSearch = Ext.create('Ext.grid.Panel', {

				store: clipStore,
				id :  'grid', 
				cls : 'clipColunms',
				 
				columns: [ {
					text: '대표화면',
					dataIndex: 'fl_path',
					align: 'center', 
					flex: 1, 
					renderer : function(value){ 
						return  '<img src="'+value+'"   height=50 width="100" style=" vertical-align: middle">';
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
					dataIndex: 'ct_id',
					align: 'center',  
					sortable: false, 
					flex: 1
				}, {
					text: '제목',
					dataIndex: 'ct_nm',
					sortable: false, 
					style: 'text-align:center',
					flex: 4
				}, {
					text: '카테고리명',
					dataIndex: 'category_nm',
					sortable: false,
					align: 'center', 
					flex: 1
				}, {
					text: '방송일',
					dataIndex: 'brd_dd',
					sortable: false,
					align: 'center', 
					flex: 1
				}, {
					text: '등록일',
					dataIndex: 'reg_dt',
					sortable: false,
					align: 'center', 
					flex: 1
				}, {
					text: '영상길이',
					dataIndex: 'ct_leng',
					sortable: false,
					align: 'center', 
					flex: 1
				}],
				height: '100%',
				width: '100%',
				listeners: {
				
					cellclick: function (view, td, cellIndex, record, tr, rowIndex, e, eOpts) {
						 
						var ctId = this.getSelectionModel().getSelection()[0].data.ct_id
						var title = this.getSelectionModel().getSelection()[0].data.ct_nm
						//ctId값으로 기본정보 호출
						var callBaseInfo = new Ext.data.Operation({
							action: 'read',
							params: {
								'ctId': ctId

							}
						});

						//이미 텝이 존재한다면 다시 생성을 못하도록 막는다. 중복id인 경우 화면이 깨짐.
						if(!Ext.getCmp(ctId)){
							this.addNewTab(ctId, title);

							/*	  //멀티루트 로딩 parser 예제 2014.06.11
				        		  //기존 단일 건루트만 되던 것을 멀티루트로 인식 가능하게 구현
				                 // 기본 스토어를 콜한다.
				        	   var test = testStore.load(callBaseInfo);

				        		  //불러들인 json 데이터를 사정에 지정한 스토어로 호출한다.
				        		  var r = test.getStory()	;
				        		 console.log(r);
							 */
							var store;
							var player;
							 
							 store = clipBasicStore.load({
								url :  context + '/clip/getClipSearchBasicInfo.ssc'  
								,params:{'ctId': ctId}
							});
 
							//기본정보 셋팅

							store.on('load', function (st, records, opts) {
								st.each(function (re) { 
									var getCtId = re.get("ctId");

									if(ctId = getCtId){  
										var videoMetaForm = Ext.ComponentQuery.query('#viedeoMeta_' + ctId);
										var videoFrom = {
												ctLeng : re.get("ctLeng"),
												aspRtoCd : re.get("aspRtoCd"),
												vdResol : re.get("vdVresol")+"X"+re.get("vdHresol"),
												frmPerSec :re.get("frmPerSec")
										}
										videoMetaForm[0].getForm().setValues(videoFrom);



										var metaForm = Ext.ComponentQuery.query('#form_' + ctId);
										var loadForm = {
												ctNm: re.get("ctNm"),
												categoryNm: re.get("categoryNm"),
												episodeNm: re.get("episodeNm"),
												regDt: re.get("regDt"),
												ctLeng: re.get("ctLeng"),
												keyWords: re.get("keyWords"),
												cont: re.get("cont"),
												ctLeng : re.get("ctLeng"),
                                        		aspRtoCd : re.get("aspRtoCd"),
                                        		frmPerSec :re.get("frmPerSec"),
                                        		vdQlty :re.get("vdQlty"),
                                        		dataStatNm :re.get("dataStatNm"),
                                        		ristClfNm :re.get("ristClfNm")
										}

										metaForm[0].getForm().setValues(loadForm);
									}


								})
							})


							if(streaming == 'Y'){
							//palyer url 경로 받아온다.
							player = playerStore.load({
								url :  context + '/clip/getVideoPlayInfo.ssc'  
								,params:{'ctId': ctId}
							});
							
							var count=0;
							player.on('load', function (st, records, opts) {

								st.each(function (re) { 

									var getCtId = re.get("ctId");
									src = re.get("fullPath") + ".mp4"

									//store에서 조회한 ctId와 현재 클릭한 ctId와 동일 할때만 해당 탭을 열어준다.
									if (getCtId == ctId) {
										//중복플레이로 플레이가 느려지는 것을 막기위해서 최초 호출되었을때만 플레이 명령어를 지시한다. 현재 extjs의 영우 해당id을 누릇 횟수만큼 값을 불러들인다.
										if(count==0){

											var videoPanel = Ext.ComponentQuery.query('#video_' + ctId);   

											videoPanel[0].update('<video id="videoplay_' + ctId + '"   width="100%" height ="160" controls="true"  name="Player"  ><source src="' + src + '" type="video/mp4"></video>');
											var video = document.getElementById('videoplay_'+ctId);

											video.play();
											count++;
										}
									}
								})
							})
						}

						}else{
							//기존에 존재하는 텝을 다시 보여준다.
							Ext.getCmp(ctId).show();
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
								closeAction: 'hide',
								cls : 'customFormBackground',
								items: [{
									xtype: 'panel',
									hidden: true,
									layout: 'vbox',
									items: [{
										width: '100%',
										height : 275,
										border : false,
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
																readOnly : true,
																labelSeparator: '', 
																labelWidth : 60,
																name: 'vdResol'
															}, {
																xtype: 'textfield',
																fieldLabel: '초당프레임',  
																labelSeparator: '',
																labelWidth : 75,
																margin : '0 0 0 20',
																readOnly : true,
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
											title: '기본정보',
											height: '100%',
											flex : 3,
											border : false,
											xtype: 'form', 
											id : 'form_'+ctId,
											layout: 'vbox',
											margin: '0 0 3 5',
											defaults: {
												border: false,
												xtype: 'panel',
												layout: 'anchor'
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
													text : '다운로드 요청', 
													handler: function () {
														 
							 

														downloadStore.update({
															url : context+'/clip/insertDownloadContent.ssc',
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
	                                              
	                                                items: [	Ext.create('Ext.form.Panel', {
	    												layout: 'hbox',
	    												margin: '5 0 0 0',
	    												labelSeparator: '',
	    												bodyStyle:  'border-width: 0px' ,
	    												items: [{
	    													xtype: 'textfield',
	    													fieldLabel: '카테고리',
	    													labelSeparator: '',
	    													name: 'categoryNm'
	    												}, {
	    													xtype: 'textfield',
	    													fieldLabel: '회차',
	    													labelSeparator: '',
	    													margin: '0 0 0 40',
	    													name: 'episodeNm'
	    												}]
	    											})]
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
	                                                    xtype: 'textfield',
	                                                    fieldLabel: '사용등급',
	                                                    labelSeparator: '',
	                                                    margin: '0 0 0 40',
	                                                    readOnly: true,
	                                                    name: 'ristClfNm'
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

	                                            }
	                                        ]
										}] 
									,listeners :{
                   						beforerender : function( view,  eOpts  ){ 
                							//버전에따라서 화면에 추가하기전에 해당 컬럼을 hidden 처리한다.
                							 
                							if(catalog == 'N'){
                								var formPanel = Ext.ComponentQuery.query('#form_'+ctId)
                							  formPanel[0].margin = '0 0 0 0'	
                								 view.setHeight(300);
                							 
                							}else if(catalog == 'Y' && streaming == 'N'){
												var formPanel = Ext.ComponentQuery.query('#form_'+ctId)
												formPanel[0].margin = '0 0 0 0'	
												 
												 view.setHeight(300); 
											}
                						  
                							
                						}
                					}
									}, {
										title: '스토리보드',
										width: '100%',
										flex: 3,
										autoScroll: true,
										bodyStyle : {
											'border-width' : '0 0 0 0'
										},
										id : 'storyBoard_'+ctId,
										items: Ext.create('Ext.view.View', {
											store:  Ext.create('Ext.data.Store',{
												model : 'StoryBoard'
													,proxy : {
														type : 'ajax'
															// ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
															,headers: { 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' }
											,noCache: false 
											,url : context +'/clip/getClipSearchStroyboardInfoForExtJs.ssc'
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
														var corner = clipCornerStore.load({
															url :  context + '/clip/getClipSearchStroyboardCornerInfo.ssc',

															action :'read'
																,params : {'ctId' :  records[0].data.ctId

																}

														});  
													}  
												}
											}

											}),
											height  : 300,
											padding: '5 5 5 15',
											tpl: [
											      '<tpl for=".">',
											      '<div class="thumb-wrap" id="{img}_' + ctId + '" style="float:left;margin:10px;padding:3px">',
											      '<tpl if="img == rpImg">',
											      '<div class="thumb" id="{img}_background" style="background:#CD1039;padding:3px; ">',
											      '<tpl else>',
											      '<div class="thumb"id="{img}_background" style="background:#dddddd;padding:3px;">',
											      '</tpl>',
											      '<a href="#"><img src="{url}" style="height:70px;width:120px" ></a></div>',
											      '<span class="x-editable" style="margin-left:30px;">{ctLeng:htmlEncode}</span>',
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

											    		  var video = document.getElementById('videoplay_'+ctId);
											    		  video.currentTime = record.data.duration;
											    		  video.play();
											    	  },
											    	  afterrender : function(view, eOpts ){
											    		  view.getEl().on('scroll', function(e, target) {

											    			  console.log('sss')
											    		  });

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
									} ]
								}],
								listeners : {

									beforeclose : function( panel, eOpts ){
										
										if(streaming=='Y'){
											
											console.log('close ct_id    ' + ctId)
											var video = document.getElementById('videoplay_'+ctId);
											console.log('close video    ' + video)
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
					store: clipStore,
					dock: 'bottom'
						,listeners : {
							afterrender : function() {
								this.child('#refresh').hide();
							},
							beforechange :  function( view, page, eOpts ){


								var keyword = Ext.getCmp('keyword').getValue();
								var fromDate = Ext.getCmp('fromDate').getValue();
								var toDate = Ext.getCmp('toDate').getValue();
								var cateogory =  Ext.getCmp('categoryTree').getSelectionModel().getSelection();//treepanel.getSelectionModel().getSelection();
								var categroyId = '';
								var ristClfCd = Ext.getCmp('ristClfCd').getValue();
								var ctCla = Ext.getCmp('ctCla').getValue();
								var ctTyp = Ext.getCmp('ctTyp').getValue();
								var period = Ext.ComponentQuery.query('[name=period]')[0].getGroupValue();

								if (cateogory.length == 0 || cateogory[0].data.id == 'total') {
									categroyId = 0
								} else {
									categroyId = cateogory[0].data.id
								}


								var tem = clipStore.load({
									url : context + '/clip/findClipSearchList.ssc',
									action: 'read',
									params: {
										'keyword' : keyword
										,'startDt' : fromDate
										,'endDt' :toDate
										, 'categoryId' : categroyId
										, 'ristClfCd' :ristClfCd
										, 'ctCla' :  ctCla
										, 'dateGb' : period
										, 'ctTyp' : ctTyp
										, 'pageNo' : page
										,'searchTyp' : 'image'

									}
								});
								//	gridSearch.show();


							}
						}
				}]

			});

			//하단 메인 패널
			var mainView = Ext.create('Ext.tab.Panel', {
				//  width : '100%'
				height: 700,
				padding: '0 5 0 5' ,
				plain: true,
				bodyStyle : {
					'border-width' : '1 0 0 0'
				},
				renderTo: 'body',
				items: [{
					title: '검색결과',
					id : 'searchResult',
					items: [gridSearch]
				}],
				listeners : {
					tabchange : function( tabPanel, newCard, oldCard, eOpts ){

						//스토리보드를 다시 불러온다.
						if(newCard.id != 'searchResult'){
							//ctId값으로 기본정보 호출


						}

//						다른텝을 열었다면 자동으로 플레이 중지를 시킨다.
						var preVideo = document.getElementById('videoplay_'+ oldCard.id);
						if(preVideo != null){
							preVideo.pause();
						}


					}
				}

			})



			Ext.EventManager.onWindowResize(function () {
				mainView.setSize(undefined, undefined)
			});

			Ext.EventManager.onWindowResize(function () {
				gridSearch.setSize(undefined, undefined)
			});
		}
	}
}();
