package org.piangles.backbone.services.profile.dao;

import org.piangles.backbone.services.profile.PendingEmailChange;
import org.piangles.core.dao.DAOException;

public interface PendingEmailChangeNoSQLDAO {

    public void persistPendingEmailChange(PendingEmailChange pendingEmailChange) throws DAOException;
    public boolean pendingEmailChangeExists(String emailId) throws DAOException;
}
