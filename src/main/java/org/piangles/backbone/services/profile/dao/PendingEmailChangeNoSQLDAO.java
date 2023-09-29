package org.piangles.backbone.services.profile.dao;

import org.piangles.core.dao.DAOException;

public interface PendingEmailChangeNoSQLDAO {

    public void persistPendingEmailChange(PendingEmailChange pendingEmailChange) throws DAOException;
    public boolean pendingEmailChangeExists(String newEmailId) throws DAOException;

}
