
    	var videoHtml = '<video width="640px" height="370px" autoplay="autoplay"  controls="true" ID="videoplay" name="Player">';
    	videoHtml += '<source src="" type="video/mp4"></video>';
    		var ClipSearch = Ext.create('Ext.tab.Panel',{
    			xtype : 'vbox'
    			,alias : 'widget.clipseach'
    			,height : 800
    			,items : [{
					 title : '검색결과'
			}]

    		});
    
