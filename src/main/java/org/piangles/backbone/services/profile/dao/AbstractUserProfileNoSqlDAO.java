package org.piangles.backbone.services.profile.dao;

import org.piangles.backbone.services.config.DefaultConfigProvider;
import org.piangles.core.dao.nosql.AbstractDAO;
import org.piangles.backbone.services.profile.UserProfileService;
import org.piangles.core.resources.ResourceManager;
import org.piangles.core.util.abstractions.ConfigProvider;

abstract class AbstractUserProfileNoSqlDAO<T> extends AbstractDAO<T>
{
    static final String COMPONENT_ID = "368e2276-5ed6-11ee-8c99-0242ac120002";

    AbstractUserProfileNoSqlDAO() throws Exception
    {
        ConfigProvider cp = new DefaultConfigProvider(UserProfileService.NAME, COMPONENT_ID);
        super.init(ResourceManager.getInstance().getMongoDataStore(cp));
    }
}
