package org.smile.db;

import org.smile.Smile;

public class DbConstans implements Db{
	/**默认数据库方言*/
	public static  Dialect DIALECT=Dialect.valueOf(Smile.DB_DIALECT);
	/**数据库配置文件*/
	public static  String CONFIG_FILE=Smile.DB_FILE_NAME;
}
