package org.smile.core;

import org.junit.Test;
import org.smile.Smile;
import org.smile.util.SysUtils;

public class TestProperties {
    @Test
    public void testActivePro(){
        System.getProperties().put("smile.profiles.active","dev");
        SysUtils.log(Smile.config);

    }
}
