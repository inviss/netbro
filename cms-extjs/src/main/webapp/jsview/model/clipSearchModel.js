 Ext.define('ClipList',{
	extend : 'Ext.data.Model'
		,fields : [
		           {name : 'categoryId', type : 'int'}
		           ,{name : 'episodeId', type : 'int'}
		           ,{name : 'segmentId', type : 'int'} 
					,{name : 'categoryNm', type : 'string'}
					,{name : 'episodeNm', type : 'string'} 
					,{name : 'segmentNm', type : 'string'}
					,{name : 'ctId', type : 'long'} 
					,{name : 'vdHresol', type : 'int'}
					,{name : 'vdVresol', type : 'int'} 
					,{name : 'rpImgKfrmSeq', type : 'long'}
					,{name : 'depth', type : 'int'} 
					,{name : 'regDt', type : '**datetime**',convert:function(v,record){
						return Ext.Date.format(new Date(v),'Y-m-d')
					} }
					,{name : 'duration', type : 'long'} 
					,{name : 'flPath', type : 'string'}
					,{name : 'prodRoute', type : 'string'} 
					,{name : 'ctTyp', type : 'string'}
					,{name : 'ctCla', type : 'string'} 
					,{name : 'ctiFmt', type : 'string'}
					,{name : 'ctNm', type : 'string'} 
					,{name : 'keyWords', type : 'string'}
					,{name : 'keyWord', type : 'string'}
					,{name : 'brdDd', type : 'string'} 
					,{name : 'cont', type : 'string'}
					,{name : 'regrid', type : 'string'} 
					,{name : 'aspRtpCd', type : 'string'}
					,{name : 'vdQlty', type : 'string'} 
					,{name : 'nodes', type : 'string'}
					,{name : 'ctLeng', type : 'string'} 
					,{name : 'svrFlNm', type : 'string'}
					,{name : 'ristClfCd', type : 'string'} 
					,{name : 'lockStatCd', type : 'string'}
					//클립검색용메타
					 ,{name : 'duration', type : 'int'}
			           ,{name : 'cont', type : 'int'} 
						,{name : 'regrid', type : 'string'}
						,{name : 'nodes', type : 'string'} 
						,{name : 'category_id', type : 'string'}
						,{name : 'episode_id', type : 'long'} 
						,{name : 'segment_id', type : 'int'}
						,{name : 'category_nm', type : 'string'} 
						,{name : 'episode_nm', type : 'long'}
						,{name : 'segment_nm', type : 'int'} 
						,{name : 'reg_dt', type : '**datetime**',convert:function(v,record){
							return Ext.Date.format(new Date(v),'Y-m-d')
						} }
						,{name : 'ct_id', type : 'long'} 
						,{name : 'vd_hresol', type : 'string'}
						,{name : 'vd_vresol', type : 'string'} 
						,{name : 'rp_img_kfrm_seq', type : 'string'}
						,{name : 'fl_path', type : 'string' ,convert:function(v,record){
						 
							 if(record.data.rp_img_kfrm_seq == ''){
								 v = context+"/images/no_image.jpg"
							 }
							 
							return v;
						}} 
						,{name : 'prod_route', type : 'string'}
						,{name : 'ct_typ', type : 'string'} 
						,{name : 'ct_cla', type : 'string'}
						,{name : 'cti_fmt', type : 'string'} 
						,{name : 'ct_nm', type : 'string'}
						,{name : 'key_words', type : 'string'} 
						,{name : 'brd_dd',type : '**datetime**',convert:function(v,record){
							return Ext.Date.format(new Date(v),'Y-m-d')
						} }
						,{name : 'asp_rtp_cd', type : 'string'} 
						,{name : 'vd_qlty', type : 'string'}
						,{name : 'ct_leng', type : 'string'} 
						,{name : 'svr_fl_nm', type : 'string'}
						,{name : 'rist_clf_cd', type : 'string'} 
						,{name : 'lock_stat_cd', type : 'string'}
					]
				
	});  
 