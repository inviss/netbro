
var roleAuthTreeStore = Ext.create('Ext.data.TreeStore',{
	storeId : 'roleAuthTree'
		,model : 'MenuTreeModel'
			,viewConfig : {
				containerScroll : true
			},
});
