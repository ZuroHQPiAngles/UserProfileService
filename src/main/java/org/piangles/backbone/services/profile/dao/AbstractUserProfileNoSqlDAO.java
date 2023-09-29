package org.piangles.backbone.services.profile.dao;

import com.zurohq.core.services.dao.AbstractDAO;
import org.piangles.backbone.services.profile.UserProfileService;

abstract class AbstractUserProfileNoSqlDAO<T> extends AbstractDAO<T>
{
    static final String COMPONENT_ID = "c5a393c9-f4cb-4f82-b9a1-6297ca7e2609";

    AbstractUserProfileNoSqlDAO() throws Exception
    {
        super(UserProfileService.NAME, COMPONENT_ID);
    }
}
