
Header = function() {

	return {
    	init : function() {
    	
    		var defalut  = Ext.create('Ext.Button',{
			text : '클립검색',
			flex : 1
		});
		var loginButton  = Ext.create('Ext.Button',{

			text : '로그인',
			flex : 1,
			handler : function(){
				layer_open('layer2');

			}
		});
		var logOutButton  = Ext.create('Ext.Button',{

			text : '로그아웃',
			flex : 1,
			margin:'0 5 0 0',
			handler : function(){
				checkLogout();
			}
		});
		var balnk  = {
				xtype:'tbspacer',
				flex:1
		};
		
		var banner  = Ext.create('Ext.Img',{

			src : img 
			,autoEl : 'div'
			,region : 'center'
			 
			,height : 30
			,width : 40
		});
		
		var submenu  = Ext.create('Ext.panel.Panel',{
			xtype : 'hbox',
			pack : 'end',
			align : 'middle', 
			items : [ {
				xtype : 'textfield'
				,value : '공지사항 나오는 창입니다'
			}]
		});
		var menus = Ext.create('Ext.panel.Panel', {
			id : 'mainMenu',
			alias : 'topmenu',
			bodyStyle:{"background-color" : "#041588"},
			layout: {
				type: 'hbox',
				padding: '5',
				align: 'stretch'
			},
			height : 50,
			 
			items: [defalut,balnk,balnk,balnk,loginButton]
		});

		
		var top  = Ext.create('Ext.panel.Panel', {
			id : 'top',
			alias : 'top',
			layout: {
				type: 'vbox',				
				align: 'stretch'
				,frame : true
			},
			 default : {
				width : '100%' 
				
			 },
			renderTo : 'header',
			items: [banner,menus,submenu]
		});
		
		if(menuList != ''){
			var panel ='';
		
			var index = menus.items.length;
			menus.removeAll();
			var tempMenus=menuList.split(',');

		//권한이 있는 메뉴의 갯수만큼 메뉴 생성
			for(var i = 0; i < tempMenus.length  ; i++){
				console.log('###############   i  '+ i+'    '+tempMenus[i]);
				if(tempMenus[i] == 'clipsrch_button'){
					menus.add(i+1,clipsrch_button );
				}
				if(tempMenus[i] == 'contents_button'){
					menus.add(i+1,contents_button );
				}
				if(tempMenus[i] == 'del_button'){
					menus.add(i+1,del_button );
				}
				if(tempMenus[i] == 'statistic_button'){
					menus.add(i+1,statistic_button );
				}
				if(tempMenus[i] == 'admin_button'){
					menus.add(i+1,admin_button  );
				}
				if(tempMenus[i] == ''){
					menus.add(i+1,logOutButton  );
				}
			}
		}
		  Ext.EventManager.onWindowResize(top.doLayout, top);
}
	}
}();
