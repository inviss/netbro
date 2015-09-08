Header = function () {

    return {
        init: function () {
        	  //서브메뉴를 불러온다, 권한메뉴는 목록을 서버에서 관리하으로 서버에서 읽어들인다.
        	//관리메뉴
        	adminMenuStore.load({
				url : context +'/admin/auth/findSubMenuList.ssc'
					,action : 'read'
					,params : {
					'userId' : userId
					,'menuId' : 14
					}
				});
			//컨텐츠관리 메뉴
        	contentsMenuStore.load({
				url : context +'/admin/auth/findSubMenuList.ssc'
					,action : 'read'
					,params : {
					'userId' : userId
					,'menuId' : 3
					}
				});
			
        	//컨텐츠 서비스 메뉴
        	ServiceMenuStore.load({
				url : context +'/admin/auth/findSubMenuList.ssc'
					,action : 'read'
					,params : {
					'userId' : userId
					,'menuId' : 7
					}
				});
			
        	//통계메뉴
        	StatisticsMenuStore.load({
				url : context +'/admin/auth/findSubMenuList.ssc'
					,action : 'read'
					,params : {
					'userId' : userId
					,'menuId' : 10
					}
				});
			
            var logOutButton = Ext.create('Ext.Button', {

                text: '로그아웃',
               width : 100,
                margin: '5 5 5 5', 
                handler: function () {
                    checkLogout();
                }
            });
            
            var headerMenu = Ext.create('Ext.panel.Panel',{
            	layout : {
            		type : 'hbox'
            	},
            	border : false,
            	padding : '0 20 5 0',
            	renderTo : 'headerMenu',
            	items : [ Ext.create('Ext.Img', {
            	    src: context +'/images/web_logo.png' 
            	}) ,{
                    xtype: 'tbfill'
                }
            	,logOutButton]
            });
		
            var balnk = {
                xtype: 'tbspacer',
                width : 40
            };

       
            //탑메뉴
            var menus = Ext.create('Ext.panel.Panel', {
                layout: {
                    type: 'hbox',
                    align: 'stretch'
                },
                margin: '-14 5 14 0',
                width: '100%',
                height: 40,
                renderTo: 'menu',
               
                bodyStyle: {'border-width' : '0px','background-color':'#0B3872' } 
                
            });
            
          
            if(menuList != '') {
            	
            	   var panel = '';

                   var index = menus.items.length;
                  
                   menus.removeAll();
                   var tempMenus = menuList.split(',');

                   //권한이 있는 메뉴의 갯수만큼 메뉴 생성
                   for(var i = 0; i < tempMenus.length; i++) { 
                	   if(i == 0){
                		   
                		   menus.add(i, balnk);
                	   }
                       if(tempMenus[i] == 'clipsrch' && searchEngine == 'Y') {
                           menus.add(i + 1, clipsrch);
                       }
                       if(tempMenus[i] == 'contents') {
                           menus.add(i + 1, contents);
                       }
                       if(tempMenus[i] == 'service') {
                           menus.add(i + 1, service);
                       }
                       if(tempMenus[i] == 'statistic') {
                           menus.add(i + 1, statistic);
                       }
                       if(tempMenus[i] == 'monitoring') {
                           menus.add(i + 1, monitoring);
                       }
                       if(tempMenus[i] == 'admin') {
                           menus.add(i + 1, admin);
                       }
                     
                    }
            }

            Ext.EventManager.onWindowResize(function () {
                menus.setSize(undefined, undefined)
            });
            
            Ext.EventManager.onWindowResize(function () {
            	headerMenu.setSize(undefined, undefined)
            });
        }
    }

}();