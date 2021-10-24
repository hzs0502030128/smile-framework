package org.smile.orm.tenantId;

public interface TenantIdLoader<T> {
    /**
     * 加载当前租户ID
     * @return
     */
    public T loadCurrentTenantId();
}
