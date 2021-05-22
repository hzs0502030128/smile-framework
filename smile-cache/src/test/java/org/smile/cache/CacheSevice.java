package org.smile.cache;

import org.smile.cache.plugin.ann.Cacheable;
import org.smile.collection.MapUtils;
import org.smile.collection.ResultMap;

import java.util.ArrayList;
import java.util.List;

public class CacheSevice {
    @Cacheable(validTime = 500)
    public List<ResultMap> getList(){
        List list= new ArrayList();
        for(int i=0;i<100;i++){
            list.add(MapUtils.hashMap("age",i));
        }
        return list;
    }
}
