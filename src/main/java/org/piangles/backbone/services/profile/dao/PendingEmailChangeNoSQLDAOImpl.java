package org.piangles.backbone.services.profile.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.piangles.backbone.services.logging.LoggingService;
import org.piangles.core.dao.DAOException;

public class PendingEmailChangeNoSQLDAOImpl extends  AbstractUserProfileNoSqlDAO<PendingEmailChange> implements PendingEmailChangeNoSQLDAO {

    private final LoggingService loggingService;

    public PendingEmailChangeNoSQLDAOImpl(LoggingService loggingService) throws Exception {
        super();
        this.loggingService = loggingService;
    }

    @Override
    public void persistPendingEmailChange(PendingEmailChange pendingEmailChange) throws DAOException
    {
        super.persist(pendingEmailChange);
    }

    @Override
    public boolean pendingEmailChangeExists(String newEmailId) throws DAOException
    {
        boolean pendingChangeExists = false;
        FindIterable<Document> pendingEmailChangeDoc = super.getCollection(PendingEmailChange.class.getSimpleName())
                .find(createFilter(newEmailId))
                .sort(Sorts.descending("insertedTimestamp"));

        if(pendingEmailChangeDoc != null)
        {
            pendingChangeExists = true;
        }

        return pendingChangeExists;
    }

    private Bson createFilter(String newEmailId)
    {
        return Filters.and(Filters.eq("emailId", newEmailId));
    }
}
