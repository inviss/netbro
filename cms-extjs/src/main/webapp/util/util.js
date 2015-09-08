Map = function(){
	this.map = new Object();
};   
Map.prototype = { 
		put : function(key, value){   
			this.map[key] = value;
		},   
		get : function(key){   
			return this.map[key];
		},
		containsKey : function(key){    
			return key in this.map;
		},
		containsValue : function(value){    
			for(var prop in this.map){
				if(this.map[prop] == value) return true;
			}
			return false;
		},
		isEmpty : function(key){    
			return (this.size() == 0);
		},
		clear : function(){   
			for(var prop in this.map){
				delete this.map[prop];
			}
		},
		remove : function(key){    
			delete this.map[key];
		},
		keys : function(){   
			/*
        var keys = new Array();   
        for(var prop in this.map){   
            keys.push(prop);
        }   
        return keys;
			 */
			return Object.keys(this.map);
		},
		values : function(){   
			var values = new Array();   
			for(var prop in this.map){   
				values.push(this.map[prop]);
			}   
			return values;
		},
		size : function(){
			var count = 0;
			for (var prop in this.map) {
				count++;
			}
			return count;
		},
		index : function(key){
			var count = 0;
			for (var prop in this.map) {
				if(prop == key) return count;
				else count++;
			}
			return null;
		}
};



function hilight(id){ 
	 
	$jq('#'+id).css("color","#71F871");

};

function showSubMenu(id){ 

	if(id == 'service'){ 
		service_sub.showAt(service.x,85);
	}else if(id == 'statistic'){
		statistic_sub.showAt(statistic.x,85);
	}else if(id == 'admin'){
		admin_sub.showAt(admin.x,85);
	}else if(id == 'contents'){
		contents_sub.showAt(contents.x,85);
	}

};

function hideSubMenu(id){ 

	if(id == 'service'){ 
		service_sub.hide();
	}else if(id == 'statistic'){
		statistic_sub.hide();
	}else if(id == 'admin'){
		admin_sub.hide();
	}else if(id == 'contents_sub'){
		contents_sub.showAt(contents.x,85);
	}

};