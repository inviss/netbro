var tmpUrl;
var win = "";
userRight = function () {

    return {
        init: function () {

            findUserAuthStore.load({
                url: context + '/admin/auth/findAuthList.ssc',
            });

            var fp = Ext.create('Ext.form.Panel', {

                store: findUserAuthStore,

                id: 'userForm',
                layout: 'vbox',
                width: 'auto',
                title: '입력창',
                margin: '0 5 0 5',
                renderTo: 'right',
                items: [{
                    xtype: 'fieldset',
                    title: '사용자 정보',
                    margin: '5 5 5 5',
                    collapsible: false,
                    width: '100%',
                    listeners: {
                        afterrender: function (view, eOpts) {
                            //초반 그리드 화면의 버튼을 권한에 따라 활성화 비활성화 한다.
                            var saveButton = Ext.ComponentQuery.query('#Save');
                            var resetButton = Ext.ComponentQuery.query('#Reset');
                            if(perm != 'RW') {
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
                    items: [{
                        width: '80%',
                        xtype: 'textfield',
                        name: 'userId',
                        id: 'userId',
                        fieldLabel: 'ID',
                        labelSeparator : '',
                        margin: '5 5 5 5',
                        allowBlank: false
                    }, {
                        width: '80%',
                        xtype: 'textfield',
                        name: 'userNm',
                        id: 'userNm',
                        align: 'center',
                        margin: '5 5 5 5',
                        fieldLabel: '이름',
                        labelSeparator : '',
                        allowBlank: false,
                        minLength: 2
                    }, {
                        width: '80%',
                        xtype: 'textfield',
                        hideTrigger: true,
                        name: 'userPhone',
                        id: 'userPhone',
                        fieldLabel: '전화번호',
                        labelSeparator : '',
                        margin: '5 5 5 5',
                        allowBlank: false,
                        minLength: 9,
                        enforceMaxLength: true, // 입력란 길이 제한
                        regex: /^[0-9,-]{0,30}$/,
                        regexText: '전화번호는 숫자와 - 를 입력 할 수 있습니다.',
                        validator: function (v) {
                            return /^[0-9,-]{0,30}$/.test(v) ? true : "NOTICE";
                        }
                    }, {
                        width: '80%',
                        xtype: 'textfield',
                        name: 'userPasswd',
                        id: 'userPasswd',
                        fieldLabel: '비밀번호',
                        labelSeparator : '',
                        inputType: 'password',
                        margin: '5 5 5 5',
                        allowBlank: false,
                        minLength: 6
                    }, {
                        width: '80%',
                        xtype: 'radiogroup',
                        fieldLabel: '사용여부',
                        labelSeparator : '',
                        margin: '5 5 5 5',
                        allowBlank: false,
                        items: [{
                            xtype: 'radiofield',
                            boxLabel: '사용',
                            name: 'useYn',
                            inputValue: 'Y'
                        }, {
                            xtype: 'radiofield',
                            boxLabel: '미사용',
                            name: 'useYn',
                            inputValue: 'N'
                        }, ]
                    }]
                }, {
                    xtype: 'fieldset',
                    title: '권한 정보',
                    margin: '5 5 5 5',
                    collapsible: false,
                    width: '100%',
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
                    items: [{
                        margin: '5 5 5 5',
                        xtype: 'checkboxgroup',
                        inputValue: 'true',
                        items: userAuthCheckbox
                    }]
                }],
                buttons: [{
                    text: '저장',
                    id: 'Save',
                    handler: function () {

                        var authId = fp.getForm().getValues()['authId'];

                        if(!tmpUserId) {
                            tmpUrl = context + '/admin/user/saveUserInfo.ssc'
                        } else {
                            tmpUrl = context + '/admin/user/updateUserInfo.ssc'
                        }

                        if(Ext.getCmp('userId').getValue() == "") {
                            Ext.MessageBox.alert('Failed', 'ID를 입력하세요.');
                            return;
                        }

                        if(Ext.getCmp('userNm').getValue() == "") {
                            Ext.MessageBox.alert('Failed', '이름를 입력하세요.');
                            return;
                        }

                        if(Ext.getCmp('userPhone').getValue() == "") {
                            Ext.MessageBox.alert('Failed', '전화번호를 입력하세요.');
                            return;
                        }

                        if(Ext.getCmp('userPasswd').getValue() == "") {
                            Ext.MessageBox.alert('Failed', '비밀번호를 입력하세요.');
                            return;
                        }

                        if(!fp.getForm().getValues()['useYn']) {
                            Ext.MessageBox.alert('Failed', '사용여부를 입력하세요.');
                            return;
                        }

                        if(!authId) {
                            Ext.MessageBox.alert('Failed', '권한 정보를 입력하세요.');
                            return;
                        }

                        saveUserStore.update({
                            url: context + tmpUrl,
                            action: 'update',
                            params: {
                                userId: Ext.getCmp('userId').getValue(),
                                userNm: Ext.getCmp('userNm').getValue(),
                                userPhone: Ext.getCmp('userPhone').getValue(),
                                userPasswd: Ext.getCmp('userPasswd').getValue(),
                                useYn: fp.getForm().getValues()['useYn'],
                                authId: '' + authId + '',
                                modrId: loginUserId,
                                regrId: loginUserId,

                            },
                            callback: function (records, operation, success) {
                                var jsonData = Ext.JSON.decode(operation.response.responseText);
                                
                                if(jsonData.result == "F") {
                                    Ext.MessageBox.alert('Failed', '비밀번호를 잘못입력하였습니다.');
                                    return;
                                }else if(jsonData.result == "Y"){
                                	Ext.MessageBox.alert('Failed', '기존 사용자가 존재합니다. ID와 전화번호를 확인하십시오.');
                                    return;
                                } else {
                                    Ext.MessageBox.alert('Success', '저장 되었습니다.');
                                    userList.getStore().reload();
                                }
                            }
                        });
                    }
                }, {
                    text: '초기화',
                    id: 'Reset',
                    handler: function () {
                        fp.getForm().reset();
                        Ext.getCmp('userId').enable();
                        Ext.getCmp('changePasswd').disable();
                        tmpUserId = "";
                    }
                }, {
                    text: '비밀번호 변경',
                    id: 'changePasswd',
                    name: 'changePasswd',
                    disabled: true,
                    handler: function () {

                        Ext.getCmp('changePasswd').disable();
                        Ext.getCmp('Save').disable();
                        Ext.getCmp('Reset').disable();

                        win = Ext.create('Ext.window.Window', {
                            title: '비밀번호 변경',
                            height: 150,
                            width: 300,
                            resizable: false,
                            hidden: true,
                            closable: false,
                            items: [{
                                xtype: 'textfield',
                                width: '95%',
                                name: 'oldPasswd',
                                id: 'oldPasswd',
                                fieldLabel: '기존 비밀번호',
                                margin: '5 5 5 5',
                                allowBlank: false,
                                inputType: 'password'
                            }, {
                                width: '95%',
                                xtype: 'textfield',
                                name: 'newPasswd',
                                id: 'newPasswd',
                                fieldLabel: '신규 비밀번호',
                                margin: '5 5 5 5',
                                allowBlank: false,
                                inputType: 'password',
                            }],
                            buttons: [{
                                text: '저장',
                                handler: function () {

                                    if(!Ext.getCmp('oldPasswd').getValue()) {
                                        Ext.MessageBox.alert('Failed', '기존 비밀번호를 입력하세요.');
                                        return;
                                    }

                                    if(!Ext.getCmp('newPasswd').getValue()) {
                                        Ext.MessageBox.alert('Failed', '신규 비밀번호를 입력하세요.');
                                        return;
                                    }

                                    updateUserPasswdStore.update({
                                        url: context + '/admin/user/updatePasswd.ssc',
                                        action: 'update',
                                        params: {
                                            userId: Ext.getCmp('userId').getValue(),
                                            oldPasswd: Ext.getCmp('oldPasswd').getValue(),
                                            newPasswd: Ext.getCmp('newPasswd').getValue(),
                                        },
                                        callback: function (records, operation, success) {
                                            var jsonData = Ext.JSON.decode(operation.response.responseText);

                                            if(jsonData.result == "F") {
                                                Ext.MessageBox.alert('Failed', '기존 비밀번호가 잘못되었습니다.');
                                                return;
                                            } else if(jsonData.result == "E") {
                                                Ext.MessageBox.alert('Failed', '기존 비밀번호와 신규 비밀번호가 일치합니다.');
                                                return;

                                            } else {
                                                Ext.MessageBox.alert('Success', '저장 되었습니다.');
                                                win.close();
                                                Ext.getCmp('Save').enable();
                                                Ext.getCmp('Reset').enable();
                                            }
                                        }
                                    })
                                }

                            }, {
                                text: '닫기',
                                id: 'close',
                                handler: function () {
                                    Ext.getCmp('changePasswd').enable();
                                    Ext.getCmp('Save').enable();
                                    Ext.getCmp('Reset').enable();
                                    win.close();
                                }
                            }]

                        })
                        win.show();
                    }
                }]
            });
        }
    }
}();