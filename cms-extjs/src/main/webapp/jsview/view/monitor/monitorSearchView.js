var ext;
var graph;
var timeAxis;
monitorSearch = function () {
	return {
		init: function () {
			//메뉴 선택표시
			hilight('monitoring');
			equipMentStore.load({
				url : context +'/monitoring/findMonitoringList.ssc'
				,action : 'read'

			});

			var reload = function () {
				equipMentStore.load({
					url : context +'/monitoring/findMonitoringList.ssc'
					,action : 'read'

				});
			}
			//   setInterval(reload, 5000);

			highStore.load({
				url : context +'/monitoring/findStorageMonitoringGraph.ssc'
				,action : 'read'
					,params : {
						'storageId' : 1
					}

			});

			rowStore.load({
				url : context +'/monitoring/findStorageMonitoringGraph.ssc'
				,action : 'read'
					,params : {
						'storageId' : 2
					}
			});



			var highChart = Ext.create('Ext.chart.Chart', { 
				width: 470,
				height: 250,
				animate: true,
				store: highStore,
				// margin : '5 5 5 150',
				legend : {
					position : 'right'
				},
				series: [{
					type: 'pie',
					angleField: 'volume',
					showInLegend: true,
					tips: {
						trackMouse: true,
						width: 140,
						height: 28,
						renderer: function(storeItem, item) {
							var total = 0;

							highStore.each(function(rec) {
								total += rec.get('volume');
							});

							this.setTitle(storeItem.get('partNm') + ': ' + Math.round(storeItem.get('volume') / total * 100) + '%');
						}
					},
					label: {
						field: 'partNm',
						display: 'rotate',
						contrast: true,
						font: '0px Arial'
					},
					renderer: function(sprite, record, attr, index, store){
						if(index == 0 ){

						}
					}
				}]
			});


			var rowChart = Ext.create('Ext.chart.Chart', { 
				width: 470,
				height: 250,
				animate: true,
				store: rowStore,
				// margin : '5 5 5 150',
				legend : {
					position : 'right'
				},
				series: [{
					type: 'pie',
					angleField: 'volume',
					showInLegend: true,
					tips: {
						trackMouse: true,
						width: 140,
						height: 28,
						renderer: function(storeItem, item) {
							var total = 0;

							rowStore.each(function(rec) {
								total += rec.get('volume');
							});

							this.setTitle(storeItem.get('partNm') + ': ' + Math.round(storeItem.get('volume') / total * 100) + '%');
						}
					},
					label: {
						field: 'partNm',
						display: 'rotate',
						contrast: true,
						font: '0px Arial'
					},
					renderer: function(sprite, record, attr, index, store){
						if(index == 0 ){

						}
					}
				}]
			});



			//모니터링의 화면을 그린다
			var monitoringView = Ext.create('Ext.panel.Panel',{ 
				renderTo : 'body',
				border : false,
				margin : '5', 
				layout: {
					type: 'hbox',
					align: 'center',
					pack: 'center'
				},
				items : [
				         {
				        	 xtype : 'panel',
				        	 layout: {
				        		 type: 'vbox' 

				        	 },
				        	 border : false,
				        	 items : [
				        	          {

				        	        	  title : '고해상도', 
				        	        	  margin : '0 5 5 5',
				        	        	  items : [highChart]

				        	          } ,{
				        	        	  title : '저해상도', 
				        	        	  margin : '5 5 5 5',
				        	        	  items : [rowChart]
				        	          }]
				         },Ext.create('Ext.grid.Panel',{

				        	 title : '장비리스트',
				        	 store : equipMentStore,
				        	 width : '60%', 
				        	 height : 556,
				        	 columns : [
				        	            {text : '장비ID', dataIndex : 'deviceId', align : 'center',flex :1 },
				        	            {text : '장비명', dataIndex : 'deviceNm', align : 'center',flex :1 },
				        	            {text : '장비IP', dataIndex : 'deviceIp', align : 'center',flex :1 },
				        	            {text : '작업컨텐츠명', dataIndex : 'ctNm',flex :4 ,style: 'text-align:center'},
				        	            {text : '진행률', dataIndex : 'prgrs', align : 'center',flex :1 },
				        	            {text : '작업상태', dataIndex : 'workStatCd', align : 'center',flex :1 }
				        	            ]
				         })]

			});

			Ext.EventManager.onWindowResize(function () {
				monitoringView.setSize(undefined, undefined)
			});
		}
	}
}();