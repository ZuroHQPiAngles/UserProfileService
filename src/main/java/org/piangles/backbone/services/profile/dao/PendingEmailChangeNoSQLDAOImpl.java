package org.piangles.backbone.services.profile.dao;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.piangles.backbone.services.profile.PendingEmailChange;
import org.piangles.core.dao.DAOException;
import org.piangles.core.resources.ResourceException;

public class PendingEmailChangeNoSQLDAOImpl extends  AbstractUserProfileNoSqlDAO<PendingEmailChange> implements PendingEmailChangeNoSQLDAO {

    public PendingEmailChangeNoSQLDAOImpl() throws ResourceException {
        super();
    }

    @Override
    public void persistPendingEmailChange(PendingEmailChange pendingEmailChange) throws DAOException
    {
        super.upsert(createFilterUserId(pendingEmailChange.getUserId()), pendingEmailChange);
        //TODO
    }

    @Override
    public boolean pendingEmailChangeExists(String newEmailId) throws DAOException
    {
        boolean pendingChangeExists = false;
        PendingEmailChange pendingEmailChange = super.readOne(createFilter(newEmailId));

        if(pendingEmailChange != null)
        {
            pendingChangeExists = true;
        }

        return pendingChangeExists;
    }

    private Bson createFilter(String newEmailId)
    {
        return Filters.and(Filters.eq("newEmail", newEmailId));
    }

    private Bson createFilterUserId(String userId)
    {
        return Filters.and(Filters.eq("userId", userId));
    }

    @Override
    protected Class<PendingEmailChange> getTClass()
    {
        return PendingEmailChange.class;
    }
}
