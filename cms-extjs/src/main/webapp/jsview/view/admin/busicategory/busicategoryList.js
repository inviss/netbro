var tmpCategoryId;
var busicategoryList;

busicategoryList = function() {

    return {
        init: function() {
            //메뉴 선택표시
            hilight('admin');
            findBusicategoryProflNmStore.load({
                url: context + '/admin/busicategory/findBusiCategoryList.ssc',
            });

            var store = Ext.data.StoreManager.get("clipCategory");

            store.setProxy({
                type: 'ajax',
                url: context + '/admin/category/findCategoryListForExtJs.ssc',
                actionMethods: {
                    create: "POST",
                    read: "GET",
                    update: "POST",
                    destroy: "POST"
                },
                headers: {
                    'Content-Type': 'application/json; charset=utf-8; Cache-Control: no-cache'
                },
                noCache: false,
                reader: {
                    type: 'json',
                    root: 'store'
                }
            });
            store.load();

            //좌측 트리패널
            var treepanel = Ext.create('Ext.tree.Panel', {
                width: '100%',
                id: 'categoryTree',
                store: store,
                rootVisible: true,
                useArrows: true,
                border: false,
                listeners: {
                    itemclick: {
                        fn: function(view, record, item, index, event) {
                            //the record is the data node that was clicked
                            //the item is the html dom element in the tree that was clicked
                            //index is the index of the node relative to its parent
                            nodeId = record.data.id;

                            tmpbusiPartnerId = nodeId;

                            var getBusicategoryInfo = new Ext.data.Operation({
                                action: 'read',
                                url: context + '/admin/busicategory/findBusiCategoryList.ssc',
                                params: {
                                    'categoryId': tmpbusiPartnerId
                                }
                            });
                            getBusicategoryStore.load(getBusicategoryInfo);
                        }
                    }
                }
            });

            //좌측패널
            var left = Ext.create('Ext.panel.Panel', {
                layout: {
                    type: 'vbox'
                },
				bodyStyle : {
                	'border-width' : '1 1 1 1'
                },
                padding: '0 0 0 5',
                width: '100%',
                renderTo: 'busicategoryBody',
                title: '카테고리 리스트',
                items: [treepanel],

            })

            Ext.EventManager.onWindowResize(function() {
                left.setSize(undefined, undefined)
                treepanel.setSize(undefined, undefined)
                    //searchPanel.setSize(undefined, undefined)
            });
        }
    }
}();