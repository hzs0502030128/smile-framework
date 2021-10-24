package org.smile.ormdb;

import org.smile.orm.tenantId.TenantIdLoader;

public class TestTenantIdLoader implements TenantIdLoader<String> {

    @Override
    public String loadCurrentTenantId() {
        return "000000";
    }
}
