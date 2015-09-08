<!-- 헤더시작 -->
<script type="text/javascript">
	var menuList="";
	
	var userId = '${user.userId}'

	function checkLogout(){
		document.header.action=context +'/loginout.ssc';
		document.header.submit();
	};
	
 	Ext.onReady(Header.init);
 	
 	var admin_sub = Ext.create('Ext.menu.Menu',{
				id :  'admin_menu' ,
        		listeners : {
        		mouseleave : function( menu, e, eOpts ){
        		hideSubMenu('admin');
        		}
        		}  				 
        	})
        	
   var contents_sub = Ext.create('Ext.menu.Menu',{
				id :  'contents_menu' ,
        		listeners : {
        		mouseleave : function( menu, e, eOpts ){
        		hideSubMenu('contents');
        		}
        		}  				 
        	})
   var service_sub = Ext.create('Ext.menu.Menu',{
				id :  'service_menu' ,
        		listeners : {
        		mouseleave : function( menu, e, eOpts ){
        		hideSubMenu('service');
        		}
        		}  				 
        	})
        	
    var statistic_sub = Ext.create('Ext.menu.Menu',{
				id :  'statistic_menu' ,
        		listeners : {
        		mouseleave : function( menu, e, eOpts ){
        		hideSubMenu('statistic');
        		}
        		}  				 
        	})
 	<#list tpl.findUserMenus('${user.userId}') as menu>

	 if('${menu.menuEnNm}' == 'clipsrch' || '${menu.menuEnNm}' == 'monitoring'){
		var ${menu.menuEnNm} = Ext.create('Ext.Panel',{
		 	layout : {
		 		type : 'hbox',
		 		align : 'middle',
		 		pack : 'center'
		 	}
		 	,id : '${menu.menuEnNm}_panel'
			,width : 150
			 ,bodyStyle: {'border-width' : '0px','background-color':'#0B3872' } 
			,items : { 
			width : '60%'
			,height : '45%'
			, html : '<a href="'+context+'${menu.url}'+'" onclick="hilight(\'${menu.menuEnNm}\')" style="text-decoration:none"><b><font id = "${menu.menuEnNm}" color="white">${menu.menuNm}</font></b></a>'
			  ,bodyStyle: {'border-width' : '0px','background-color':'#0B3872' ,'width' : '50px'} 
			 }
		});
		}else{
		var ${menu.menuEnNm} = Ext.create('Ext.panel.Panel',{
		 	layout : {
		 		type : 'hbox',
		 		align : 'middle',
		 		pack : 'center'
		 	}
		 	,id : '${menu.menuEnNm}_panel'
			,width : 150
			 ,bodyStyle: {'border-width' : '0px','background-color':'#0B3872' } 
			,items : { 
			width : '60%'
			,height : '45%'
			,menu : '${menu.menuEnNm}_sub'
			, html : '<a href="#"  onmouseover="showSubMenu(\'${menu.menuEnNm}\')" style="text-decoration:none"><b><font id = "${menu.menuEnNm}" color="white">${menu.menuNm}</font></b></a>'
			  ,bodyStyle: {'border-width' : '0px','background-color':'#0B3872' ,'width' : '50px'} 
			 }
			 
		});
		}
         				
			menuList +=  '${menu.menuEnNm}' +","
			
		
	</#list> 
	
	
	 <#assign searchEngine = tpl.getCmsSearchEngine() >  
	 	var searchEngine = '${searchEngine}'
	 
	 <#assign streaming = tpl.getStreamingYn() >  
	 	var streaming = '${streaming}'
	 		 	
	 <#assign catalog = tpl.getCatalogingYn() >  
	 	var catalog = '${catalog}'
	 	
		function openNotice(Id){
				var noticeWindow = Ext.getCmp(Id);
				noticeWindow.show();
				
				var popup = Ext.getCmp(Id+"_popup");
    			console.log(popup);
    			popup.close();
    		}
</script>
<form name="header" method = "post">
	</form>
<div id="menu"></div> 
<!-- 헤더끝 -->