var retryTrsStore = Ext.create('Ext.data.Store', {
	model: 'TrsModel',
	proxy: {
		type: 'ajax'
			// ,actionMethods:  {create: "POST", read: "POST", update: "POST", destroy: "POST"}
			,
			actionMethods: {
				create: "POST",
				read: "GET",
				update: "POST",
				destroy: "POST"
			},
			headers: {
				'Content-Type': 'application/json'
			},
			noCache: false,
			reader: {
				type: 'json',
				root: 'trsTbls',
				totalProperty: 'totalCount'
			}
	}
});