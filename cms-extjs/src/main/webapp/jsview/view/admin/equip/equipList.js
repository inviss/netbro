var equipList;
var tmpDeviceId = "";
var tmpDeviceNum = "";

equipList = function () {
	
	var tmpEquipKinds = "";

	return {
		init: function () {
			//메뉴 선택표시
			hilight('admin');
			findEquipStore.load({
				url: context+'/admin/equipment/findEquipmentList.ssc',
				action: 'read',
				params: {
					'pageNo': 1
				}
			});
			//유저리스트 패널
			equipList = Ext.create('Ext.grid.Panel', {

				store: findEquipStore,
				cls:'regridColunms',
				viewConfig: {
					loadMask: false,
				},
				columns: [{
					text: '장비종류',
					dataIndex: 'tmpDeviceId',
					style: 'text-align:center',
					flex: 3
				},{
					text: '장비ID',
					dataIndex: 'deviceId',
					style: 'text-align:center',
					flex: 3,
					hidden:true
				},{
					text: '장비이름',
					dataIndex: 'deviceNm',
					style: 'text-align:center',
					flex: 3
				}, {
					text: '장비IP',
					dataIndex: 'deviceIp',
					align:'center',
					flex: 3
				}, {
					text: '장비인스턴스',
					dataIndex: 'deviceNum',
					align:'center',
					flex: 3
				}, {
					text: '사용여부',
					dataIndex: 'useYn',
					align:'center',
					flex: 1
				}],
				dockedItems: [{
					xtype: 'pagingtoolbar',
					store: findEquipStore,
					dock: 'bottom',
					layout: 'hbox',
					listeners: {
						afterrender : function() {
							this.child('#refresh').hide();
						},
						beforechange: function (view, page, eOpts) {
							var tem = findEquipStore.load({
								url: context+'/admin/equipment/findEquipmentList.ssc',
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
						var deviceId = this.getSelectionModel().getSelection()[0].data.deviceId
						var deviceNum = this.getSelectionModel().getSelection()[0].data.deviceNum
						//ctId값으로 기본정보 호출
						var getEquipInfo = new Ext.data.Operation({
							action: 'read',
							url: context + '/admin/equipment/getEquipmentInfo.ssc',
							params: {
								'deviceId': deviceId,
								'deviceNum': deviceNum
							}
						});

						tmpDeviceId = deviceId;	
						tmpDeviceNum = deviceNum;
						var comboBox = Ext.ComponentQuery.query('#tmpEquipKinds');
						var addComboBox = Ext.ComponentQuery.query('#instanceAdd');
						 console.log(comboBox)
						if(tmpDeviceId.substr(0,2) == "TC"){ 
							 comboBox[0].setValue("TC")
							 addComboBox[0].bindStore(traKinds);
						}else{ 
							comboBox[0].setValue("TS")
							addComboBox[0].bindStore(trsKinds);
						}
						
						
						var equipStore = getEquipStore.load(getEquipInfo)

						equipStore.on('load', function (st, records, opts) {
							
							st.each(function (re) {
								var equipForm = {
										deviceId:tmpDeviceId,
										//tmpEquipKinds:tmpEquipKinds,
										deviceNm: re.get("deviceNm"),
										deviceIp: re.get("deviceIp"),
										devicePort: re.get("devicePort"),
										useYn: re.get("useYn"),
								}
								//userRight.js의 ID를 선언한 userFormList에 값을 set함.
								Ext.getCmp('equipForm').getForm().setValues(equipForm);
							})
						})

					}

				}
			});

			//좌측패널
			var equipListView = Ext.create('Ext.panel.Panel', {
				//  width : '100%'
				layout: 'auto',
				title: '장비목록',
				plain: true,
				bodyStyle : {
                	'border-width' : '1 1 1 1'
                },
                padding: '0 0 0 5',
				renderTo: 'equipBody',
				items: [{
					items: [equipList],
					border: false
				}]

			})

			Ext.EventManager.onWindowResize(equipListView.doLayout, equipListView);
		}
	}
}();