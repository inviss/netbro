var userList
var tmpUserId; 
var getUserInfo;

userList = function () {

    return {
        init: function () {
        	//메뉴 선택표시
         	 hilight('admin');
        	findUserStore.load({
            	url: context+'/admin/user/findUserList.ssc',
                action: 'read',
                params: {
                    'pageNo': 1
                }
        	});
        	
            //유저리스트 패널
            userList = Ext.create('Ext.grid.Panel', {

                store: findUserStore,
                cls:'regridColunms',
                viewConfig: {
                    loadMask: false,
                },
                columns: [{
                    text: 'ID',
                    dataIndex: 'userId',
                    align:'center',
                    flex: 2
                }, {
                    text: '이름',
                    dataIndex: 'userNm',
                    align:'center',
                    flex: 2
                }, {
                    text: '등록일',
                    dataIndex: 'regDt',
                    align:'center',
                    flex: 3
                }, {
                    text: '사용여부',
                    dataIndex: 'useYn',
                    align:'center',
                    flex: 1
                }],
                dockedItems: [{
                    xtype: 'pagingtoolbar',
                    store: findUserStore,
                    dock: 'bottom',
                    layout: 'hbox',
                    listeners: {
						afterrender : function() {
							this.child('#refresh').hide();
						},
                        beforechange: function (view, page, eOpts) {
                            var tem = findUserStore.load({
                                action: 'read',
                                url: context+'/admin/user/findUserList.ssc',
                                params: {
                                    'pageNo': page
                                }
                            });
                        }
                    }
                }],
                height: '100%',
                width: '100%',
                border: false, 

                listeners: {
                    select: function (selModel, record, index, options) {
                        var userId = this.getSelectionModel().getSelection()[0].data.userId

                        //userId값으로 기본정보 호출
                         getUserInfo = new Ext.data.Operation({
                            action: 'read',
                            url: context + '/admin/user/getUserInfo.ssc',
                            params: {
                                'userId': userId
                            }
                        });
                        
                        //tmpUserId로 userRight(비밀번호변경) 버튼 활성화 체크.
                        tmpUserId = userId;
                        
                        var userStore = getUserStore.load(getUserInfo)

                        userStore.on('load', function (st, records, opts) {
                            st.each(function (re) {
                                var userForm = {
                                    userId: re.get("userId"),
                                    userNm: re.get("userNm"),
                                    userPhone: re.get("userPhone"),
                                    useYn: re.get("useYn"), 
                                    userPasswd: "", 
                                }
                                
                                Ext.getCmp('userForm').getForm().setValues(userForm);
                                Ext.getCmp('userId').disable();
                                
                                if(perm == 'RW') {
                                	 Ext.getCmp('changePasswd').enable();
                                }
                            })
                        })
                        //userRight.js의 ID를 선언한 userFormList에 값을 set함.

                    }

                }
            });

            //좌측패널
            var userListView = Ext.create('Ext.panel.Panel', {
                //  width : '100%'
                layout: 'auto',
                title: '유저목록',
                plain: true,
				bodyStyle : {
                	'border-width' : '1 1 1 1'
                },
                padding: '0 0 0 5',
                renderTo: 'userBody',
                items: [{
                    items: [userList],
                    border: false, 
                }]

            })

            Ext.EventManager.onWindowResize(userListView.doLayout, userListView);
        }
    }
}();