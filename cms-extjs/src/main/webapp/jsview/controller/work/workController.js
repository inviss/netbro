

workController = function() {

	return {
    	init : function() {

    	var videoHtml = '<video width="640px" height="370px" autoplay="autoplay"  controls="true" ID="videoplay" name="Player">';
    	videoHtml += '<source src="" type="video/mp4"></video>';
    		var contentsDateView = Ext.create('Ext.tab.Panel',{
    			xtype : 'vbox'
    			,renderTo : 'section1'
    			,height : 800
    			,items : [{
					 title : '검색결과'
			}]

    		});
    	}
	}
}();
