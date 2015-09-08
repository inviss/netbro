var tmpbusiPartnerId;
var tmpUrl;

busicategoryRight = function() {

    return {
        init: function() {

           findBusicategoryStore.load({
                url: context + '/admin/busicategory/findBusiCategoryList.ssc',
            });

            var fp = Ext.create('Ext.form.Panel', {

                store: findBusipartnerStore,

                id: 'busipartnerForm',
                layout: 'vbox',
                width: 'auto',
                title: '카테고리맵핑 정보',
                margin: '0 5 0 5',
                renderTo: 'right',
                items: [{
                    xtype: 'fieldset',
                    title: '카테고리 맵핑 정보',
                    margin: '5 5 5 5',
                    collapsible: false,
                    width: '100%',
                    listeners: {
                        afterrender: function(view, eOpts) {
                            //초반 그리드 화면의 버튼을 권한에 따라 활성화 비활성화 한다.
                            var saveButton = Ext.ComponentQuery.query('#save');
                            var resetButton = Ext.ComponentQuery.query('#reset');

                            if(perm != 'RW') {
                                //saveButton[0].hide(true);
                                saveButton[0].setDisabled(true);
                                resetButton[0].setDisabled(true);
                            }
                        }
                    },
                    defaults: {
                        anchor: '100%',
                        layout: {
                            type: 'hbox',
                            defaultMargins: {
                                top: 5,
                                right: 5,
                                bottom: 5,
                                left: 5
                            },

                        }
                    },
                    items: busipartnerCheckbox
                }],
                buttons: [{
                    text: '저장',
                    id: 'save',
                    handler: function() {

                        if(!tmpbusiPartnerId) {
                            tmpUrl = context + '/admin/busicategory/saveBusiCategoryInfo.ssc'
                        } else {
                            tmpUrl = context + '/admin/busicategory/saveBusiCategoryInfo.ssc'
                        }

                        var busiPartId = fp.getForm().getValues()['busiPartnerId'];

                        if(!busiPartId) {
                            Ext.MessageBox.alert('Error', '카테고리맵핑정보를 입력하세요.');
                            return;
                        }

                        Ext.Ajax.request({
                            url: tmpUrl,
                            method: 'post',
                            params: {
                                tmpBusiPartnerId: '' + busiPartId + '',
                                categoryId: tmpbusiPartnerId
                            },
                            success: function(result, request) {
                                Ext.MessageBox.alert('Success', '저장되었습니다.');
                            },
                            failure: function(result, request) {
                                Ext.MessageBox.alert('Failed', 'Error');
                            }
                        });
                    }
                }, {
                    text: '초기화',
                    id: 'reset',
                    handler: function() {
                        fp.getForm().reset();
                    }
                }]
            });
        }
    }
}();