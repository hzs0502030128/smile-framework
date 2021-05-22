package org.smile.orm.mapping.flag;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.smile.annotation.AnnotationUtils;
import org.smile.orm.ann.HumpColumns;
import org.smile.orm.ann.Orm;
import org.smile.util.StringUtils;
/**
 * 类的映射处理类
 * @author 胡真山
 *
 */
public class SmileTableFlag extends TableFlag {
	//是否是驼峰规则
	boolean isHumpColumns=false;
	@Override
	public boolean checkFlag(Class<?> clazz) {
		this.rawClass=clazz;
		Orm orm=AnnotationUtils.getAnnotation(this.rawClass,Orm.class);
		if(orm!=null){
			this.name=orm.name();
			this.flaged=true;
			this.isTable=orm.isTable();
			if(StringUtils.isEmpty(this.name)){
				this.name=clazz.getSimpleName().toLowerCase();
				logger.info(clazz.getName() + " annotation not configure table, user class simple name for database table name ");
			}
			HumpColumns humpColumns=AnnotationUtils.getAnnotation(this.rawClass, HumpColumns.class);
			if(humpColumns!=null){
				this.isHumpColumns =true;
			}
		}
		return flaged;
	}

	@Override
	public boolean isHumpColumns() {
		return isHumpColumns;
	}
}
