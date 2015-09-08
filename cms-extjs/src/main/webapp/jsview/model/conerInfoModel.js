
var clip = Ext.define('ConerInfo',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'cnId', type : 'long'}
		           ,{name : 'cnNm', type : 'string'}
		           ,{name : 'bgmTime', type : 'date'}
		           ,{name : 'endTime', type : 'date'}
		           ,{name : 'cnCont', type : 'string'}
		           ,{name : 'regDt', type : 'date'}
		           ,{name : 'regId', type : 'string'}
		           ,{name : 'modDt', type : 'date'}
		           ,{name : 'modId', type : 'string'}
		           ,{name : 'duration', type : 'long'}
		           ,{name : 'rpimgKfrmSeq', type : 'long'}
		           ,{name : 'ctId', type : 'long'}
		           ,{name : 'sDuration', type : 'long'}]
	});
