var getRoleAuthStore = Ext.create('Ext.data.Store', {
	model: 'roleAuthModel',
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
				root: 'controlGubun',

			}
	},  
	autoLoad: false,
	autoSync: true,
});





