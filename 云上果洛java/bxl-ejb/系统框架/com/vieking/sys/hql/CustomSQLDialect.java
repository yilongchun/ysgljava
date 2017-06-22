package com.vieking.sys.hql;

import org.hibernate.dialect.MySQL5InnoDBDialect;

public class CustomSQLDialect extends MySQL5InnoDBDialect {
	
	public CustomSQLDialect() {
		super();
		this.registerFunction("bitand", new BitAndFunction());
	}

}
