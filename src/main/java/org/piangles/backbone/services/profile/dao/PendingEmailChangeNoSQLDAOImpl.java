package org.piangles.backbone.services.profile.dao;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.piangles.backbone.services.logging.LoggingService;
import org.piangles.backbone.services.profile.PendingEmailChange;
import org.piangles.core.dao.DAOException;

public class PendingEmailChangeNoSQLDAOImpl extends  AbstractUserProfileNoSqlDAO<PendingEmailChange> implements PendingEmailChangeNoSQLDAO {

    public PendingEmailChangeNoSQLDAOImpl(LoggingService loggingService) throws Exception {
        super();
    }

    @Override
    public void persistPendingEmailChange(PendingEmailChange pendingEmailChange) throws DAOException
    {
        super.upsert(null, pendingEmailChange);
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
        return Filters.and(Filters.eq("emailId", newEmailId));
    }

    @Override
    protected Class<PendingEmailChange> getTClass()
    {
        return PendingEmailChange.class;
    }
}
