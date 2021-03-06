/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.repository.proxy;

import com.liferay.counter.service.CounterLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.BaseRepository;
import com.liferay.portal.kernel.repository.LocalRepository;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Query;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.model.Lock;
import com.liferay.portal.service.CompanyLocalService;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portlet.asset.service.AssetEntryLocalService;
import com.liferay.portlet.documentlibrary.service.DLAppHelperLocalService;

import java.io.File;
import java.io.InputStream;

import java.util.List;

/**
 * @author Mika Koivisto
 */
public class BaseRepositoryProxyBean
	extends RepositoryModelProxyBean implements BaseRepository {

	public BaseRepositoryProxyBean(
		BaseRepository baseRepository, ClassLoader classLoader) {

		super(classLoader);

		_baseRepository = baseRepository;
	}

	public FileEntry addFileEntry(
			long folderId, String sourceFileName, String mimeType, String title,
			String description, String changeLog, File file,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		FileEntry fileEntry = _baseRepository.addFileEntry(
			folderId, sourceFileName, mimeType, title, description, changeLog,
			file, serviceContext);

		return newFileEntryProxyBean(fileEntry);
	}

	public FileEntry addFileEntry(
			long folderId, String sourceFileName, String mimeType, String title,
			String description, String changeLog, InputStream is, long size,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		FileEntry fileEntry = _baseRepository.addFileEntry(
			folderId, sourceFileName, mimeType, title, description, changeLog,
			is, size, serviceContext);

		return newFileEntryProxyBean(fileEntry);
	}

	public Folder addFolder(
			long parentFolderId, String title, String description,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		Folder folder = _baseRepository.addFolder(
			parentFolderId, title, description, serviceContext);

		return newFolderProxyBean(folder);
	}

	public void cancelCheckOut(long fileEntryId)
		throws PortalException, SystemException {

		_baseRepository.cancelCheckOut(fileEntryId);
	}

	public void checkInFileEntry(
			long fileEntryId, boolean major, String changeLog,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		_baseRepository.checkInFileEntry(
			fileEntryId, major, changeLog, serviceContext);
	}

	public void checkInFileEntry(long fileEntryId, String lockUuid)
		throws PortalException, SystemException {

		_baseRepository.checkInFileEntry(fileEntryId, lockUuid);
	}

	public FileEntry checkOutFileEntry(
			long fileEntryId, ServiceContext serviceContext)
		throws PortalException, SystemException {

		FileEntry fileEntry = _baseRepository.checkOutFileEntry(
			fileEntryId, serviceContext);

		return newFileEntryProxyBean(fileEntry);
	}

	public FileEntry checkOutFileEntry(
			long fileEntryId, String owner, long expirationTime,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		FileEntry fileEntry = _baseRepository.checkOutFileEntry(
			fileEntryId, owner, expirationTime, serviceContext);

		return newFileEntryProxyBean(fileEntry);
	}

	public FileEntry copyFileEntry(
			long groupId, long fileEntryId, long destFolderId,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		return _baseRepository.copyFileEntry(
			groupId, fileEntryId, destFolderId, serviceContext);
	}

	public void deleteFileEntry(long fileEntryId)
		throws PortalException, SystemException {

		_baseRepository.deleteFileEntry(fileEntryId);
	}

	public void deleteFileEntry(long folderId, String title)
		throws PortalException, SystemException {

		_baseRepository.deleteFileEntry(folderId, title);
	}

	public void deleteFolder(long folderId)
		throws PortalException, SystemException {

		_baseRepository.deleteFolder(folderId);
	}

	public void deleteFolder(long parentFolderId, String title)
		throws PortalException, SystemException {

		_baseRepository.deleteFolder(parentFolderId, title);
	}

	public List<FileEntry> getFileEntries(
			long folderId, int start, int end, OrderByComparator obc)
		throws SystemException {

		List<FileEntry> fileEntries = _baseRepository.getFileEntries(
			folderId, start, end, obc);

		return toFileEntryProxyBeans(fileEntries);
	}

	public List<FileEntry> getFileEntries(
			long folderId, long documentTypeId, int start, int end,
			OrderByComparator obc)
		throws SystemException {

		List<FileEntry> fileEntries = _baseRepository.getFileEntries(
			folderId, documentTypeId, start, end, obc);

		return toFileEntryProxyBeans(fileEntries);
	}

	public List<FileEntry> getFileEntries(
			long folderId, String[] mimeTypes, int start, int end,
			OrderByComparator obc)
		throws PortalException, SystemException {

		List<FileEntry> fileEntries = _baseRepository.getFileEntries(
			folderId, mimeTypes, start, end, obc);

		return toFileEntryProxyBeans(fileEntries);
	}

	public List<Object> getFileEntriesAndFileShortcuts(
			long folderId, int status, int start, int end)
		throws SystemException {

		List<Object> objects = _baseRepository.getFileEntriesAndFileShortcuts(
			folderId, status, start, end);

		return toObjectProxyBeans(objects);
	}

	public int getFileEntriesAndFileShortcutsCount(long folderId, int status)
		throws SystemException {

		return _baseRepository.getFileEntriesAndFileShortcutsCount(
			folderId, status);
	}

	public int getFileEntriesAndFileShortcutsCount(
			long folderId, int status, String[] mimeTypes)
		throws PortalException, SystemException {

		return _baseRepository.getFileEntriesAndFileShortcutsCount(
			folderId, status, mimeTypes);
	}

	public int getFileEntriesCount(long folderId) throws SystemException {
		return _baseRepository.getFileEntriesCount(folderId);
	}

	public int getFileEntriesCount(long folderId, long documentTypeId)
		throws SystemException {

		return _baseRepository.getFileEntriesCount(folderId, documentTypeId);
	}

	public int getFileEntriesCount(long folderId, String[] mimeTypes)
		throws PortalException, SystemException {

		return _baseRepository.getFileEntriesCount(folderId, mimeTypes);
	}

	public FileEntry getFileEntry(long fileEntryId)
		throws PortalException, SystemException {

		FileEntry fileEntry = _baseRepository.getFileEntry(fileEntryId);

		return newFileEntryProxyBean(fileEntry);
	}

	public FileEntry getFileEntry(long folderId, String title)
		throws PortalException, SystemException {

		FileEntry fileEntry = _baseRepository.getFileEntry(folderId, title);

		return newFileEntryProxyBean(fileEntry);
	}

	public FileEntry getFileEntryByUuid(String uuid)
		throws PortalException, SystemException {

		FileEntry fileEntry = _baseRepository.getFileEntryByUuid(uuid);

		return newFileEntryProxyBean(fileEntry);
	}

	public FileVersion getFileVersion(long fileVersionId)
		throws PortalException, SystemException {

		FileVersion fileVersion = _baseRepository.getFileVersion(fileVersionId);

		return newFileVersionProxyBean(fileVersion);
	}

	public Folder getFolder(long folderId)
		throws PortalException, SystemException {

		Folder folder = _baseRepository.getFolder(folderId);

		return newFolderProxyBean(folder);
	}

	public Folder getFolder(long parentFolderId, String title)
		throws PortalException, SystemException {

		Folder folder = _baseRepository.getFolder(parentFolderId, title);

		return newFolderProxyBean(folder);
	}

	public List<Folder> getFolders(
			long parentFolderId, boolean includeMountfolders, int start,
			int end, OrderByComparator obc)
		throws PortalException, SystemException {

		List<Folder> folders = _baseRepository.getFolders(
			parentFolderId, includeMountfolders, start, end, obc);

		return toFolderProxyBeans(folders);
	}

	public List<Object> getFoldersAndFileEntriesAndFileShortcuts(
			long folderId, int status, boolean includeMountFolders, int start,
			int end, OrderByComparator obc)
		throws SystemException {

		List<Object> objects =
			_baseRepository.getFoldersAndFileEntriesAndFileShortcuts(
				folderId, status, includeMountFolders, start, end, obc);

		return toObjectProxyBeans(objects);
	}

	public List<Object> getFoldersAndFileEntriesAndFileShortcuts(
			long folderId, int status, String[] mimeTypes,
			boolean includeMountFolders, int start, int end,
			OrderByComparator obc)
		throws PortalException, SystemException {

		List<Object> objects =
			_baseRepository.getFoldersAndFileEntriesAndFileShortcuts(
				folderId, status, mimeTypes, includeMountFolders, start, end,
				obc);

		return toObjectProxyBeans(objects);
	}

	public int getFoldersAndFileEntriesAndFileShortcutsCount(
			long folderId, int status, boolean includeMountFolders)
		throws SystemException {

		return _baseRepository.getFoldersAndFileEntriesAndFileShortcutsCount(
			folderId, status, includeMountFolders);
	}

	public int getFoldersAndFileEntriesAndFileShortcutsCount(
			long folderId, int status, String[] mimeTypes,
			boolean includeMountFolders)
		throws PortalException, SystemException {

		return _baseRepository.getFoldersAndFileEntriesAndFileShortcutsCount(
			folderId, status, mimeTypes, includeMountFolders);
	}

	public int getFoldersCount(long parentFolderId, boolean includeMountfolders)
		throws PortalException, SystemException {

		return _baseRepository.getFoldersCount(
			parentFolderId, includeMountfolders);
	}

	public int getFoldersFileEntriesCount(List<Long> folderIds, int status)
		throws SystemException {

		return _baseRepository.getFoldersFileEntriesCount(folderIds, status);
	}

	public LocalRepository getLocalRepository() {
		LocalRepository localRepository = _baseRepository.getLocalRepository();

		return newLocalRepositoryProxyBean(localRepository);
	}

	public List<Folder> getMountFolders(
			long parentFolderId, int start, int end, OrderByComparator obc)
		throws SystemException {

		List<Folder> folders = _baseRepository.getMountFolders(
			parentFolderId, start, end, obc);

		return toFolderProxyBeans(folders);
	}

	public int getMountFoldersCount(long parentFolderId)
		throws SystemException {

		return _baseRepository.getMountFoldersCount(parentFolderId);
	}

	public BaseRepository getProxyBean() {
		return _baseRepository;
	}

	public List<FileEntry> getRepositoryFileEntries(
			long userId, long rootFolderId, int start, int end,
			OrderByComparator obc)
		throws SystemException {

		List<FileEntry> fileEntries = _baseRepository.getRepositoryFileEntries(
			userId, rootFolderId, start, end, obc);

		return toFileEntryProxyBeans(fileEntries);
	}

	public List<FileEntry> getRepositoryFileEntries(
			long userId, long rootFolderId, String[] mimeTypes, int status,
			int start, int end, OrderByComparator obc)
		throws PortalException, SystemException {

		List<FileEntry> fileEntries = _baseRepository.getRepositoryFileEntries(
			userId, rootFolderId, mimeTypes, status, start, end, obc);

		return toFileEntryProxyBeans(fileEntries);
	}

	public int getRepositoryFileEntriesCount(long userId, long rootFolderId)
		throws SystemException {

		return _baseRepository.getRepositoryFileEntriesCount(
			userId, rootFolderId);
	}

	public int getRepositoryFileEntriesCount(
			long userId, long rootFolderId, String[] mimeTypes, int status)
		throws PortalException, SystemException {

		return _baseRepository.getRepositoryFileEntriesCount(
			userId, rootFolderId, mimeTypes, status);
	}

	public long getRepositoryId() {
		return _baseRepository.getRepositoryId();
	}

	public void getSubfolderIds(List<Long> folderIds, long folderId)
		throws SystemException {

		_baseRepository.getSubfolderIds(folderIds, folderId);
	}

	public List<Long> getSubfolderIds(long folderId, boolean recurse)
		throws SystemException {

		return _baseRepository.getSubfolderIds(folderId, recurse);
	}

	public String[] getSupportedConfigurations() {
		return _baseRepository.getSupportedConfigurations();
	}

	public String[][] getSupportedParameters() {
		return _baseRepository.getSupportedParameters();
	}

	public void initRepository() throws PortalException, SystemException {
		_baseRepository.initRepository();
	}

	public Lock lockFileEntry(long fileEntryId)
		throws PortalException, SystemException {

		Lock lock = _baseRepository.lockFileEntry(fileEntryId);

		return (Lock)newProxyInstance(lock, Lock.class);
	}

	public Lock lockFileEntry(
			long fileEntryId, String owner, long expirationTime)
		throws PortalException, SystemException {

		Lock lock = _baseRepository.lockFileEntry(
			fileEntryId, owner, expirationTime);

		return (Lock)newProxyInstance(lock, Lock.class);
	}

	public Lock lockFolder(long folderId)
		throws PortalException, SystemException {

		Lock lock = _baseRepository.lockFolder(folderId);

		return (Lock)newProxyInstance(lock, Lock.class);
	}

	public Lock lockFolder(
			long folderId, String owner, boolean inheritable,
			long expirationTime)
		throws PortalException, SystemException {

		Lock lock = _baseRepository.lockFolder(
			folderId, owner, inheritable, expirationTime);

		return (Lock)newProxyInstance(lock, Lock.class);
	}

	public FileEntry moveFileEntry(
			long fileEntryId, long newFolderId, ServiceContext serviceContext)
		throws PortalException, SystemException {

		FileEntry fileEntry = _baseRepository.moveFileEntry(
			fileEntryId, newFolderId, serviceContext);

		return newFileEntryProxyBean(fileEntry);
	}

	public Folder moveFolder(
			long folderId, long newParentFolderId,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		Folder folder = _baseRepository.moveFolder(
			folderId, newParentFolderId, serviceContext);

		return newFolderProxyBean(folder);
	}

	public Lock refreshFileEntryLock(String lockUuid, long expirationTime)
		throws PortalException, SystemException {

		Lock lock = _baseRepository.refreshFileEntryLock(
			lockUuid, expirationTime);

		return (Lock)newProxyInstance(lock, Lock.class);
	}

	public Lock refreshFolderLock(String lockUuid, long expirationTime)
		throws PortalException, SystemException {

		Lock lock = _baseRepository.refreshFolderLock(lockUuid, expirationTime);

		return (Lock)newProxyInstance(lock, Lock.class);
	}

	public void revertFileEntry(
			long fileEntryId, String version, ServiceContext serviceContext)
		throws PortalException, SystemException {

		_baseRepository.revertFileEntry(fileEntryId, version, serviceContext);
	}

	public Hits search(SearchContext searchContext) throws SearchException {
		return _baseRepository.search(searchContext);
	}

	public Hits search(SearchContext searchContext, Query query)
		throws SearchException {

		return _baseRepository.search(searchContext, query);
	}

	public void setAssetEntryLocalService(
		AssetEntryLocalService assetEntryLocalService) {

		_baseRepository.setAssetEntryLocalService(assetEntryLocalService);
	}

	public void setCompanyId(long companyId) {
		_baseRepository.setCompanyId(companyId);
	}

	public void setCompanyLocalService(
		CompanyLocalService companyLocalService) {

		_baseRepository.setCompanyLocalService(companyLocalService);
	}

	public void setCounterLocalService(
		CounterLocalService counterLocalService) {

		_baseRepository.setCounterLocalService(counterLocalService);
	}

	public void setDLAppHelperLocalService(
		DLAppHelperLocalService dlAppHelperLocalService) {

		_baseRepository.setDLAppHelperLocalService(dlAppHelperLocalService);
	}

	public void setGroupId(long groupId) {
		_baseRepository.setGroupId(groupId);
	}

	public void setRepositoryId(long repositoryId) {
		_baseRepository.setRepositoryId(repositoryId);
	}

	public void setTypeSettingsProperties(
		UnicodeProperties typeSettingsProperties) {

		_baseRepository.setTypeSettingsProperties(typeSettingsProperties);
	}

	public void setUserLocalService(UserLocalService userLocalService) {
		_baseRepository.setUserLocalService(userLocalService);
	}

	public void unlockFileEntry(long fileEntryId)
		throws PortalException, SystemException {

		_baseRepository.unlockFileEntry(fileEntryId);
	}

	public void unlockFileEntry(long fileEntryId, String lockUuid)
		throws PortalException, SystemException {

		_baseRepository.unlockFileEntry(fileEntryId, lockUuid);
	}

	public void unlockFolder(long folderId, String lockUuid)
		throws PortalException, SystemException {

		_baseRepository.unlockFolder(folderId, lockUuid);
	}

	public void unlockFolder(long parentFolderId, String title, String lockUuid)
		throws PortalException, SystemException {

		_baseRepository.unlockFolder(parentFolderId, title, lockUuid);
	}

	public FileEntry updateFileEntry(
			long fileEntryId, String sourceFileName, String mimeType,
			String title, String description, String changeLog,
			boolean majorVersion, File file, ServiceContext serviceContext)
		throws PortalException, SystemException {

		FileEntry fileEntry = _baseRepository.updateFileEntry(
			fileEntryId, sourceFileName, mimeType, title, description,
			changeLog, majorVersion, file, serviceContext);

		return newFileEntryProxyBean(fileEntry);
	}

	public FileEntry updateFileEntry(
			long fileEntryId, String sourceFileName, String mimeType,
			String title, String description, String changeLog,
			boolean majorVersion, InputStream is, long size,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		FileEntry fileEntry = _baseRepository.updateFileEntry(
			fileEntryId, sourceFileName, mimeType, title, description,
			changeLog, majorVersion, is, size, serviceContext);

		return newFileEntryProxyBean(fileEntry);
	}

	public Folder updateFolder(
			long folderId, String title, String description,
			ServiceContext serviceContext)
		throws PortalException, SystemException {

		Folder folder = _baseRepository.updateFolder(
			folderId, title, description, serviceContext);

		return newFolderProxyBean(folder);
	}

	public boolean verifyFileEntryCheckOut(long fileEntryId, String lockUuid)
		throws PortalException, SystemException {

		return _baseRepository.verifyFileEntryCheckOut(fileEntryId, lockUuid);
	}

	public boolean verifyFileEntryLock(long fileEntryId, String lockUuid)
		throws PortalException, SystemException {

		return _baseRepository.verifyFileEntryLock(fileEntryId, lockUuid);
	}

	public boolean verifyInheritableLock(long folderId, String lockUuid)
		throws PortalException, SystemException {

		return _baseRepository.verifyInheritableLock(folderId, lockUuid);
	}

	private BaseRepository _baseRepository;

}