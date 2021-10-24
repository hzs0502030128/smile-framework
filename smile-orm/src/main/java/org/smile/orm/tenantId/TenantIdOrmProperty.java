package org.smile.orm.tenantId;

import lombok.Setter;
import org.smile.orm.mapping.FieldPropertyException;
import org.smile.orm.mapping.OrmTableMapping;
import org.smile.orm.mapping.property.OrmFieldProperty;
import org.smile.orm.mapping.property.OrmProperty;

import java.lang.reflect.Field;

public class TenantIdOrmProperty extends OrmFieldProperty {
    @Setter
    private TenantIdLoader tenantIdLoader;

    private OrmFieldProperty sourceProperty;

    public TenantIdOrmProperty(OrmFieldProperty property) {
        this.tenantIdLoader=OrmTableMapping.getTenantIdLoader();
        this.sourceProperty=property;
        this.field= property.getField();
        this.columnName=property.getColumnName();
        this.propertyName=property.getPropertyName();
        this.persistence=property.isPersistence();
        this.setFieldTypes(this.field);
    }

    @Override
    public Object readValue(Object bean) {
        try {
            Object value= field.get(bean);
            if(value == null ){
                return this.tenantIdLoader.loadCurrentTenantId();
            }
            return value;
        } catch (Exception e) {
            throw new FieldPropertyException("读取"+bean+"字段"+field.getName()+"出错",e);
        }
    }

    @Override
    public void writeValue(Object bean, Object value) {
        try {
            if(value ==null){
                value=this.tenantIdLoader.loadCurrentTenantId();
            }
            field.set(bean, value);
        } catch (Exception e) {
            throw new FieldPropertyException("设置"+bean+"字段"+field.getName()+"的值"+value+"出错",e);
        }
    }

    @Override
    public String getPropertyExp() {
        return this.sourceProperty.getPropertyExp();
    }
}
