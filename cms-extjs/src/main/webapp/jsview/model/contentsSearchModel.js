
var clip = Ext.define('ContentsList',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'ctId', type : 'long'}
		           ,{name : 'ctTyp', type : 'string'}
		           ,{name : 'ctCla', type : 'string'} 
					,{name : 'ctNm', type : 'string'}
					,{name : 'cont', type : 'string'} 
					,{name : 'vdQlty', type : 'string'}
					,{name : 'aspRtoCd', type : 'string'} 
					,{name : 'keyWords', type : 'string'}
					,{name : 'kfrmPath', type : 'string'} 
					,{name : 'rpimgKfrmSeq', type : 'string'}
					,{name : 'totKfrmNums', type : 'string'} 
					,{name : 'useYn', type : 'string'}
					,{name : 'regDt',  type : '**datetime**',convert:function(v,record){
						return Ext.Date.format(new Date(v),'Y-m-d')
					}} 
					,{name : 'regrId', type : 'string'}
					,{name : 'modrId', type : 'string'} 
					,{name : 'modDt',  type : '**datetime**',convert:function(v,record){
						return Ext.Date.format(new Date(v),'Y-m-d')
					}}
					,{name : 'delDd', type : 'date'} 
					,{name : 'dataStatCd', type : 'string'}
					,{name : 'ctLeng', type : 'string'} 
					,{name : 'ctSeq', type : 'int'}
					,{name : 'duration', type : 'int'} 
					,{name : 'brdDd', type : 'string'}
					,{name : 'spcInfo', type : 'string'} 
					,{name : 'lockStatcd', type : 'string'}
					,{name : 'prodRoute', type : 'string'} 
					,{name : 'categoryId', type : 'int'}
					,{name : 'episodeId', type : 'int'} 
					,{name : 'segmentId', type : 'int'}
					,{name : 'categoryNm', type : 'string'} 
					,{name : 'episodeNm', type : 'string'}
					,{name : 'segmentNm', type : 'string'} 
					,{name : 'vdHresol', type : 'string'}
					,{name : 'vdVresol', type : 'string'} 
					,{name : 'lft', type : 'int'}
					,{name : 'rgt', type : 'int'} 
					,{name : 'ctiId', type : 'int'}
					,{name : 'flPath', type : 'string',convert:function(v,record){
						 
						 if(record.data.rpimgKfrmSeq == ''){
							 v = context+"/images/no_image.jpg"
						 }
							 
						return v;
					}}  
					,{name : 'wrkFileNm', type : 'string'}
					,{name : 'sclNm', type : 'string'}
				]
	});
