/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.piangles.backbone.services.profile;

import org.piangles.backbone.services.Locator;
import org.piangles.backbone.services.id.IdException;
import org.piangles.backbone.services.id.IdService;
import org.piangles.backbone.services.id.Identifier;
import org.piangles.backbone.services.logging.LoggingService;
import org.piangles.backbone.services.profile.dao.PendingEmailChangeNoSQLDAO;
import org.piangles.backbone.services.profile.dao.PendingEmailChangeNoSQLDAOImpl;
import org.piangles.backbone.services.profile.dao.UserProfileDAO;
import org.piangles.backbone.services.profile.dao.UserProfileDAOImpl;
import org.piangles.core.dao.DAOException;

public final class UserProfileServiceImpl implements UserProfileService
{
	private static final String USER_ID_TYPE = "UserId";	
	private final LoggingService logger = Locator.getInstance().getLoggingService();
	private final IdService idService	 = Locator.getInstance().getIdService();
	private UserProfileDAO userProfileDAO = null;
	private PendingEmailChangeNoSQLDAO pendingEmailChangeNoSQLDAO = null;

	public UserProfileServiceImpl() throws Exception
	{
		userProfileDAO = new UserProfileDAOImpl();
		pendingEmailChangeNoSQLDAO = new PendingEmailChangeNoSQLDAOImpl();
	}
	
	@Override
	public String createProfile(BasicUserProfile profile) throws UserProfileException
	{
		Identifier id = null;
		try
		{
			id = idService.getIdentifier(USER_ID_TYPE);
			logger.info("Creating a new UserProfile for id:" + id.getValue());
			userProfileDAO.insertUserProfile(id.getValue(), profile);
		}
		catch (IdException | DAOException e)
		{
			String message = "Failed creating UserProfile for LoginId: " + profile.getEMailId();
			logger.error(message + ". Reason: " + e.getMessage(), e);
			throw new UserProfileException(message);
		}
		
		return id.getValue();
	}

	@Override
	public String searchProfile(BasicUserProfile profile) throws UserProfileException
	{
		String userId = null;
		try
		{
			logger.info("Searching for UserProfile.");
			userId = userProfileDAO.searchUserProfile(profile);
		}
		catch (DAOException e)
		{
			String message = "Failed searching for UserProfile for LoginId: " + profile.getEMailId();
			logger.error(message + ". Reason: " + e.getMessage(), e);
			throw new UserProfileException(message);
		}
		
		return userId;
	}
	
	/**
	 * TODO: The calls below in this service needs a valid session
	 */
	@Override
	public BasicUserProfile getProfile(String userId) throws UserProfileException
	{
		BasicUserProfile profile = null;
		try
		{
			logger.info("Retrieving UserProfile for: " + userId);
			profile = userProfileDAO.retrieveUserProfile(userId);
		}
		catch (DAOException e)
		{
			String message = "Failed retrieving UserProfile for UserId: " + userId;
			logger.error(message + ". Reason: " + e.getMessage(), e);
			throw new UserProfileException(message);
		}

		return profile;
	}

	@Override
	public void updateProfile(String userId, BasicUserProfile profile) throws UserProfileException
	{
		try
		{
			logger.info("Updating UserProfile for: " + userId);
			userProfileDAO.updateUserProfile(userId, profile);
		}
		catch (DAOException e)
		{
			String message = "Failed updating UserProfile for UserId: " + userId;
			logger.error(message + ". Reason: " + e.getMessage(), e);
			throw new UserProfileException(message);
		}
	}

	/**
	 * This method checks if the email has been used already by another user in pending email change request
	 * This method cannot be used to check if a given user has the email in question in the pending request
	 */
	@Override
	public boolean pendingEmailChangeExists(String userId, String newEmailId) throws UserProfileException
	{
		boolean pendingChangeExists = false;
		try
		{
			logger.info("Checking if pending email change exists for: " + newEmailId);
			if(newEmailId != null)
			{
				pendingChangeExists = pendingEmailChangeNoSQLDAO.pendingEmailChangeExists(newEmailId);
			}
		}
		catch (DAOException e)
		{
			String message = "Failed to check if pending change exists for: " + newEmailId;
			logger.error(message + ". Reason: " + e.getMessage(), e);
			throw new UserProfileException(message);
		}

		return pendingChangeExists;
	}
	
	@Override
	public void savePendingEmailChange(PendingEmailChange pendingEmailChange) throws UserProfileException {
		try
		{
			if (pendingEmailChange != null)
			{
				logger.info("Persisting pending email change");
				pendingEmailChangeNoSQLDAO.persistPendingEmailChange(pendingEmailChange);
				logger.info("Successfully persisted pending email change");

			}
		}
		catch (DAOException e)
		{
			String message = "Failed persisting pending email change";
			logger.error(message + ". Reason: " + e.getMessage(), e);
			throw new UserProfileException(message);
		}
	}

	@Override
	public void revertPendingEmailChange(PendingEmailChange pendingEmailChange) throws UserProfileException {
		try
		{
			if (pendingEmailChange != null)
			{
				logger.info("Deleting pending email change for userId: " + pendingEmailChange.getUserId());
				pendingEmailChangeNoSQLDAO.deletePendingEmailChange(pendingEmailChange);
				logger.info("Successfully deleted pending email change for userId: " + pendingEmailChange.getUserId());

			}
		}
		catch (DAOException e)
		{
			String message = "Failed reverting pending email change for userId: " + pendingEmailChange.getUserId();
			logger.error(message + ". Reason: " + e.getMessage(), e);
			throw new UserProfileException(message);
		}
	}

	/**
	 * This method checks if a given user has the email in question in the pending request
	 */
	@Override
	public PendingEmailChange getPendingEmailChange(String userId) throws UserProfileException
	{
		PendingEmailChange pendingEmailChange = null;
		try
		{
			logger.info("Getting pending email change for userId: " + userId);
			if(userId != null)
			{
				pendingEmailChange = pendingEmailChangeNoSQLDAO.getPendingEmailChange(userId);
			}
		}
		catch (DAOException e)
		{
			String message = "Failed to get pending email change for userId: " + userId;
			logger.error(message + ". Reason: " + e.getMessage(), e);
			throw new UserProfileException(message);
		}

		return pendingEmailChange;
	}

	/**
	 * For a given old email, the system checks if a verified new email exists and returns the new email
	 */
	@Override
	public PendingEmailChange getVerifiedNewEmail(String oldEmail) throws UserProfileException 
	{
		PendingEmailChange pendingEmailChange = null;
		try
		{
			logger.info("Getting verified email change exists with old emailId: " + oldEmail);
			pendingEmailChange = pendingEmailChangeNoSQLDAO.getVerifiedNewEmail(oldEmail);
		}
		catch (DAOException e)
		{
			String message = "Failed to getVerifiedNewEmail with old emailId: " + oldEmail;
			logger.error(message + ". Reason: " + e.getMessage(), e);
			throw new UserProfileException(message);
		}

		return pendingEmailChange;
	}

}