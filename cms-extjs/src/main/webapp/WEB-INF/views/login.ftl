<#include "/WEB-INF/views/includes/commonTags.ftl"> 
 
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<META http-equiv="Expires" content="-1"> 
<META http-equiv="Pragma" content="no-cache"> 
<META http-equiv="Cache-Control" content="No-Cache"> 
        <link rel="stylesheet" type="text/css" href="<@spring.url '/extjs/resources/css/ext-all.css'/>" />
        <script type="text/javascript" src="<@spring.url '/extjs/ext-all-debug.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/model/logInModel.js'/>"></script>
        <script type="text/javascript" src="<@spring.url '/jsview/store/logInStore.js'/>"></script>
        <script type="text/javascript">
        var context = '<@spring.url ''/>'
	  var version='';
	   
        Ext.onReady(function () {

            Ext.create('Ext.window.Window', {
                title: '로그인',
                width: '320',
                height: '300',
                resizable : false,
                defaults: {
                    margin: '10 0 0 20'
                },
                items: [{
                xtype : 'image',
                 margin: '10 0 0 76',
                src : context+'/images/logo.png',
                height : 144,
                width : 172
                },{
                    xtype: 'textfield',
                    fieldLabel: 'ID',
                    name: 'userId',
                    width: '90%'
                }, {
                    xtype: 'textfield',
                    width: '90%',
                    inputType: 'password',
                    fieldLabel: 'PASSWORD',
                    name: 'password',
                        listeners: {
                scope: this,
                specialkey: function (f, e) {
                var mask = new Ext.LoadMask(Ext.getBody(), {msg:"Loading..."});
                   Ext.Ajax.on('beforerequest', function(){
      				 	 mask.show();
  					 });

  					 Ext.Ajax.on('requestcomplete', function(){
       					 mask.hide();
   					});
                    if (e.getKey() == e.ENTER) {


                        var userId = Ext.ComponentQuery.query('field[name$=userId]');
                        var password = Ext.ComponentQuery.query('field[name$=password]');
                        loginStore.load({
                            url: '<@spring.url "/getUseYn.ssc" />',
                            action: 'update',
                            params: {
                                'userId': userId[0].getValue(),
                                'userPasswd': password[0].getValue()

                            }
                        });


                    }

                }
            }
                }],
                buttons: [{
                    text: '로그인',
                    handler: function () {

                        var userId = Ext.ComponentQuery.query('field[name$=userId]');
                        var password = Ext.ComponentQuery.query('field[name$=password]');
                        loginStore.load({
                            url: '<@spring.url "/getUseYn.ssc" />',
                            action: 'update',
                            params: {
                                'userId': userId[0].getValue(),
                                'userPasswd': password[0].getValue()

                            }
                        });

                    }
                }] 
            }).show();

			//loading후 id필드에 focus를 자동으로 놓는다.
			var userIdField = Ext.ComponentQuery.query('field[name$=userId]');
            userIdField[0].focus();
        })
        </script>
<head>
</head>
<body>
</body>
</html>