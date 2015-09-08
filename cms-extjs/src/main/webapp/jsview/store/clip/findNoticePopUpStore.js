var findNoticePopUpStore = Ext.create('Ext.data.Store', {
 
    model: 'NoticeModel',
    proxy: {
        type: 'ajax'
        // ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
        ,
        headers: {
        	 'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache' 
        },
        noCache: false,
        reader: {
            type: 'json',
            root: 'noticeInfos' 
        }
    },
    autoLoad: false,
    autoSync: true,
    listeners : {
    	load : function(view,records){ 
    		 
    		for(var i = 0; i < records.length; i++){
    		
    			
    			//공지사항 좌측하단 팝업
    			Ext.create('Ext.ux.window.Notification', {
					title: '알림',
					id : records[i].data.noticeId+"_popup",   				 
					position: 'br',
					manager: 'instructions',
					cls: 'ux-notification-light',
					width : 400,
					iconCls: 'ux-notification-icon-information',
					html: '<a href="#" style="text-decoration:none" onclick="openNotice('+records[i].data.noticeId+')"><font color="black"><b>'+records[i].data.title+'</b></font></a>',
					autoCloseDelay: 5000,
					slideBackDuration: 500,
					slideInAnimation: 'bounceOut',
					slideBackAnimation: 'easeIn'
				}).show();
    			 
    		      Ext.create('Ext.window.Window',{
    				title : '공지사항'
    				,width : 400
    				,height : 250
    				,id : records[i].data.noticeId
    				 
    				,items :[Ext.create('Ext.panel.Panel' ,{
                      	title : records[i].data.title,  
                      	height : 250,
                      	xtype : 'form',
                      	items : [{
                      		xtype : 'textareafield',
                      		width : '100%',
                      		height : 250,
                      		readOnly : true,
                      		value : records[i].data.cont
                      		 
                      	}] 
    				})]
    				,listeners : {
    					close : function( panel, eOpts ){
    						var now = new Date();
    						   var expiry = new Date(now.getTime() +  24 * 60 * 60 * 1000); 
    						Ext.util.Cookies.set('popUp','close',expiry);
    					}
    			
    				}
    			}) 
    			 
    			 
    		}
    		
    	
    	}
    }
});

