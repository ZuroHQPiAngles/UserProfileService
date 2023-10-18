package org.piangles.backbone.services.profile.dao;

import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.piangles.backbone.services.profile.PendingEmailChange;
import org.piangles.core.dao.DAOException;
import org.piangles.core.resources.ResourceException;

public class PendingEmailChangeNoSQLDAOImpl extends  AbstractUserProfileNoSqlDAO<PendingEmailChange> implements PendingEmailChangeNoSQLDAO {

    public PendingEmailChangeNoSQLDAOImpl() throws ResourceException
    {
        super();
    }

    @Override
    public void persistPendingEmailChange(PendingEmailChange pendingEmailChange) throws DAOException
    {
        super.upsert(createFilterUserId(pendingEmailChange.getUserId()), pendingEmailChange);
    }

    @Override
    public boolean pendingEmailChangeExists(String emailId) throws DAOException
    {
        boolean pendingChangeExists = false;

        PendingEmailChange pendingEmailChange = super.readOne(createFilter(emailId));

        if(pendingEmailChange != null)
        {
            pendingChangeExists = true;
        }

        return pendingChangeExists;
    }

    @Override
    public void deletePendingEmailChange(PendingEmailChange pendingEmailChange) throws DAOException
    {
        super.delete(createFilterUserId(pendingEmailChange.getUserId()));
    }

	@Override
	public PendingEmailChange getPendingEmailChange(String userId) throws DAOException
	{
		//it is guaranteed to have only one record as we always upsert in save function
		return super.readOne(createFilterUserIdStatus(userId));
	}

	@Override
	public PendingEmailChange getVerifiedNewEmail(String oldEmail)  throws DAOException
	{
		return super.readOne(createOldEmailVerifiedFilter(oldEmail));
	}

	private Bson createFilter(String newEmailId)
    {
        return Filters.and(
				Filters.eq("newEmail", newEmailId),  
				Filters.eq("emailChangeStatus", "Pending"));
    }

    private Bson createFilterUserId(String userId)
    {
        return Filters.eq("userId", userId);
    }

	private Bson createFilterUserIdStatus(String userId)
	{
		return Filters.and(
				Filters.eq("userId", userId), 
				Filters.eq("emailChangeStatus", "Pending"));
	}

	private Bson createOldEmailVerifiedFilter(String oldEmail)
	{
		return Filters.and(
				Filters.eq("oldEmail", oldEmail),
				Filters.eq("emailChangeStatus", "Verified"));
	}

    @Override
    protected Class<PendingEmailChange> getTClass()
    {
        return PendingEmailChange.class;
    }
}
