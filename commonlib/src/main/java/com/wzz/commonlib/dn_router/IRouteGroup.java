package com.wzz.commonlib.dn_router;

import com.wzz.dn_annotation_lib.model.RouteMeta;

import java.util.Map;

public interface IRouteGroup {
    void loadInto(Map<String, RouteMeta> atlas);
}
