package kr.co.d2net.utils;

import org.hibernate.dialect.MySQL5InnoDBDialect;

public class CustomMysqlDialect extends MySQL5InnoDBDialect {
	public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
		//return " ENGINE=MyISAM DEFAULT CHARSET=utf8";
    }
}	
