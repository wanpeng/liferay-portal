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

package com.liferay.portlet.asset.service.persistence;

import com.liferay.portal.kernel.bean.PortalBeanLocatorUtil;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.service.ServiceTestUtil;
import com.liferay.portal.service.persistence.PersistenceExecutionTestListener;
import com.liferay.portal.test.ExecutionTestListeners;
import com.liferay.portal.test.LiferayIntegrationJUnitTestRunner;
import com.liferay.portal.util.PropsValues;

import com.liferay.portlet.asset.NoSuchCategoryException;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.impl.AssetCategoryModelImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import java.util.List;

/**
 * @author Brian Wing Shun Chan
 */
@ExecutionTestListeners(listeners =  {
	PersistenceExecutionTestListener.class})
@RunWith(LiferayIntegrationJUnitTestRunner.class)
public class AssetCategoryPersistenceTest {
	@Before
	public void setUp() throws Exception {
		_persistence = (AssetCategoryPersistence)PortalBeanLocatorUtil.locate(AssetCategoryPersistence.class.getName());
	}

	@Test
	public void testCreate() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetCategory assetCategory = _persistence.create(pk);

		Assert.assertNotNull(assetCategory);

		Assert.assertEquals(assetCategory.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		AssetCategory newAssetCategory = addAssetCategory();

		_persistence.remove(newAssetCategory);

		AssetCategory existingAssetCategory = _persistence.fetchByPrimaryKey(newAssetCategory.getPrimaryKey());

		Assert.assertNull(existingAssetCategory);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addAssetCategory();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetCategory newAssetCategory = _persistence.create(pk);

		newAssetCategory.setUuid(ServiceTestUtil.randomString());

		newAssetCategory.setGroupId(ServiceTestUtil.nextLong());

		newAssetCategory.setCompanyId(ServiceTestUtil.nextLong());

		newAssetCategory.setUserId(ServiceTestUtil.nextLong());

		newAssetCategory.setUserName(ServiceTestUtil.randomString());

		newAssetCategory.setCreateDate(ServiceTestUtil.nextDate());

		newAssetCategory.setModifiedDate(ServiceTestUtil.nextDate());

		newAssetCategory.setLeftCategoryId(ServiceTestUtil.nextLong());

		newAssetCategory.setRightCategoryId(ServiceTestUtil.nextLong());

		newAssetCategory.setName(ServiceTestUtil.randomString());

		newAssetCategory.setTitle(ServiceTestUtil.randomString());

		newAssetCategory.setDescription(ServiceTestUtil.randomString());

		newAssetCategory.setVocabularyId(ServiceTestUtil.nextLong());

		_persistence.update(newAssetCategory, false);

		AssetCategory existingAssetCategory = _persistence.findByPrimaryKey(newAssetCategory.getPrimaryKey());

		Assert.assertEquals(existingAssetCategory.getUuid(),
			newAssetCategory.getUuid());
		Assert.assertEquals(existingAssetCategory.getCategoryId(),
			newAssetCategory.getCategoryId());
		Assert.assertEquals(existingAssetCategory.getGroupId(),
			newAssetCategory.getGroupId());
		Assert.assertEquals(existingAssetCategory.getCompanyId(),
			newAssetCategory.getCompanyId());
		Assert.assertEquals(existingAssetCategory.getUserId(),
			newAssetCategory.getUserId());
		Assert.assertEquals(existingAssetCategory.getUserName(),
			newAssetCategory.getUserName());
		Assert.assertEquals(Time.getShortTimestamp(
				existingAssetCategory.getCreateDate()),
			Time.getShortTimestamp(newAssetCategory.getCreateDate()));
		Assert.assertEquals(Time.getShortTimestamp(
				existingAssetCategory.getModifiedDate()),
			Time.getShortTimestamp(newAssetCategory.getModifiedDate()));
		Assert.assertEquals(existingAssetCategory.getParentCategoryId(),
			newAssetCategory.getParentCategoryId());
		Assert.assertEquals(existingAssetCategory.getLeftCategoryId(),
			newAssetCategory.getLeftCategoryId());
		Assert.assertEquals(existingAssetCategory.getRightCategoryId(),
			newAssetCategory.getRightCategoryId());
		Assert.assertEquals(existingAssetCategory.getName(),
			newAssetCategory.getName());
		Assert.assertEquals(existingAssetCategory.getTitle(),
			newAssetCategory.getTitle());
		Assert.assertEquals(existingAssetCategory.getDescription(),
			newAssetCategory.getDescription());
		Assert.assertEquals(existingAssetCategory.getVocabularyId(),
			newAssetCategory.getVocabularyId());
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		AssetCategory newAssetCategory = addAssetCategory();

		AssetCategory existingAssetCategory = _persistence.findByPrimaryKey(newAssetCategory.getPrimaryKey());

		Assert.assertEquals(existingAssetCategory, newAssetCategory);
	}

	@Test
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		try {
			_persistence.findByPrimaryKey(pk);

			Assert.fail("Missing entity did not throw NoSuchCategoryException");
		}
		catch (NoSuchCategoryException nsee) {
		}
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		AssetCategory newAssetCategory = addAssetCategory();

		AssetCategory existingAssetCategory = _persistence.fetchByPrimaryKey(newAssetCategory.getPrimaryKey());

		Assert.assertEquals(existingAssetCategory, newAssetCategory);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetCategory missingAssetCategory = _persistence.fetchByPrimaryKey(pk);

		Assert.assertNull(missingAssetCategory);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting()
		throws Exception {
		AssetCategory newAssetCategory = addAssetCategory();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetCategory.class,
				AssetCategory.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("categoryId",
				newAssetCategory.getCategoryId()));

		List<AssetCategory> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		AssetCategory existingAssetCategory = result.get(0);

		Assert.assertEquals(existingAssetCategory, newAssetCategory);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetCategory.class,
				AssetCategory.class.getClassLoader());

		dynamicQuery.add(RestrictionsFactoryUtil.eq("categoryId",
				ServiceTestUtil.nextLong()));

		List<AssetCategory> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting()
		throws Exception {
		AssetCategory newAssetCategory = addAssetCategory();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetCategory.class,
				AssetCategory.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("categoryId"));

		Object newCategoryId = newAssetCategory.getCategoryId();

		dynamicQuery.add(RestrictionsFactoryUtil.in("categoryId",
				new Object[] { newCategoryId }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingCategoryId = result.get(0);

		Assert.assertEquals(existingCategoryId, newCategoryId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(AssetCategory.class,
				AssetCategory.class.getClassLoader());

		dynamicQuery.setProjection(ProjectionFactoryUtil.property("categoryId"));

		dynamicQuery.add(RestrictionsFactoryUtil.in("categoryId",
				new Object[] { ServiceTestUtil.nextLong() }));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		if (!PropsValues.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			return;
		}

		AssetCategory newAssetCategory = addAssetCategory();

		_persistence.clearCache();

		AssetCategoryModelImpl existingAssetCategoryModelImpl = (AssetCategoryModelImpl)_persistence.findByPrimaryKey(newAssetCategory.getPrimaryKey());

		Assert.assertTrue(Validator.equals(
				existingAssetCategoryModelImpl.getUuid(),
				existingAssetCategoryModelImpl.getOriginalUuid()));
		Assert.assertEquals(existingAssetCategoryModelImpl.getGroupId(),
			existingAssetCategoryModelImpl.getOriginalGroupId());

		Assert.assertEquals(existingAssetCategoryModelImpl.getParentCategoryId(),
			existingAssetCategoryModelImpl.getOriginalParentCategoryId());
		Assert.assertTrue(Validator.equals(
				existingAssetCategoryModelImpl.getName(),
				existingAssetCategoryModelImpl.getOriginalName()));
		Assert.assertEquals(existingAssetCategoryModelImpl.getVocabularyId(),
			existingAssetCategoryModelImpl.getOriginalVocabularyId());
	}

	protected AssetCategory addAssetCategory() throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetCategory assetCategory = _persistence.create(pk);

		assetCategory.setUuid(ServiceTestUtil.randomString());

		assetCategory.setGroupId(ServiceTestUtil.nextLong());

		assetCategory.setCompanyId(ServiceTestUtil.nextLong());

		assetCategory.setUserId(ServiceTestUtil.nextLong());

		assetCategory.setUserName(ServiceTestUtil.randomString());

		assetCategory.setCreateDate(ServiceTestUtil.nextDate());

		assetCategory.setModifiedDate(ServiceTestUtil.nextDate());

		assetCategory.setLeftCategoryId(ServiceTestUtil.nextLong());

		assetCategory.setRightCategoryId(ServiceTestUtil.nextLong());

		assetCategory.setName(ServiceTestUtil.randomString());

		assetCategory.setTitle(ServiceTestUtil.randomString());

		assetCategory.setDescription(ServiceTestUtil.randomString());

		assetCategory.setVocabularyId(ServiceTestUtil.nextLong());

		_persistence.update(assetCategory, false);

		return assetCategory;
	}

	@Test
	public void testMoveTree() throws Exception {
		long groupId = ServiceTestUtil.nextLong();

		AssetCategory rootAssetCategory = addAssetCategory(groupId, null);

		long previousRootLeftCategoryId = rootAssetCategory.getLeftCategoryId();
		long previousRootRightCategoryId = rootAssetCategory.getRightCategoryId();

		AssetCategory childAssetCategory = addAssetCategory(groupId,
				rootAssetCategory.getCategoryId());

		rootAssetCategory = _persistence.fetchByPrimaryKey(rootAssetCategory.getPrimaryKey());

		Assert.assertEquals(previousRootLeftCategoryId,
			rootAssetCategory.getLeftCategoryId());
		Assert.assertEquals(previousRootRightCategoryId + 2,
			rootAssetCategory.getRightCategoryId());
		Assert.assertEquals(rootAssetCategory.getLeftCategoryId() + 1,
			childAssetCategory.getLeftCategoryId());
		Assert.assertEquals(rootAssetCategory.getRightCategoryId() - 1,
			childAssetCategory.getRightCategoryId());
	}

	@Test
	public void testMoveTreeFromLeft() throws Exception {
		long groupId = ServiceTestUtil.nextLong();

		AssetCategory parentAssetCategory = addAssetCategory(groupId, null);

		AssetCategory childAssetCategory = addAssetCategory(groupId,
				parentAssetCategory.getCategoryId());

		parentAssetCategory = _persistence.fetchByPrimaryKey(parentAssetCategory.getPrimaryKey());

		AssetCategory rootAssetCategory = addAssetCategory(groupId, null);

		long previousRootLeftCategoryId = rootAssetCategory.getLeftCategoryId();
		long previousRootRightCategoryId = rootAssetCategory.getRightCategoryId();

		parentAssetCategory.setParentCategoryId(rootAssetCategory.getCategoryId());

		_persistence.update(parentAssetCategory, false);

		rootAssetCategory = _persistence.fetchByPrimaryKey(rootAssetCategory.getPrimaryKey());
		childAssetCategory = _persistence.fetchByPrimaryKey(childAssetCategory.getPrimaryKey());

		Assert.assertEquals(previousRootLeftCategoryId - 4,
			rootAssetCategory.getLeftCategoryId());
		Assert.assertEquals(previousRootRightCategoryId,
			rootAssetCategory.getRightCategoryId());
		Assert.assertEquals(rootAssetCategory.getLeftCategoryId() + 1,
			parentAssetCategory.getLeftCategoryId());
		Assert.assertEquals(rootAssetCategory.getRightCategoryId() - 1,
			parentAssetCategory.getRightCategoryId());
		Assert.assertEquals(parentAssetCategory.getLeftCategoryId() + 1,
			childAssetCategory.getLeftCategoryId());
		Assert.assertEquals(parentAssetCategory.getRightCategoryId() - 1,
			childAssetCategory.getRightCategoryId());
	}

	@Test
	public void testMoveTreeFromRight() throws Exception {
		long groupId = ServiceTestUtil.nextLong();

		AssetCategory rootAssetCategory = addAssetCategory(groupId, null);

		long previousRootLeftCategoryId = rootAssetCategory.getLeftCategoryId();
		long previousRootRightCategoryId = rootAssetCategory.getRightCategoryId();

		AssetCategory parentAssetCategory = addAssetCategory(groupId, null);

		AssetCategory childAssetCategory = addAssetCategory(groupId,
				parentAssetCategory.getCategoryId());

		parentAssetCategory = _persistence.fetchByPrimaryKey(parentAssetCategory.getPrimaryKey());

		parentAssetCategory.setParentCategoryId(rootAssetCategory.getCategoryId());

		_persistence.update(parentAssetCategory, false);

		rootAssetCategory = _persistence.fetchByPrimaryKey(rootAssetCategory.getPrimaryKey());
		childAssetCategory = _persistence.fetchByPrimaryKey(childAssetCategory.getPrimaryKey());

		Assert.assertEquals(previousRootLeftCategoryId,
			rootAssetCategory.getLeftCategoryId());
		Assert.assertEquals(previousRootRightCategoryId + 4,
			rootAssetCategory.getRightCategoryId());
		Assert.assertEquals(rootAssetCategory.getLeftCategoryId() + 1,
			parentAssetCategory.getLeftCategoryId());
		Assert.assertEquals(rootAssetCategory.getRightCategoryId() - 1,
			parentAssetCategory.getRightCategoryId());
		Assert.assertEquals(parentAssetCategory.getLeftCategoryId() + 1,
			childAssetCategory.getLeftCategoryId());
		Assert.assertEquals(parentAssetCategory.getRightCategoryId() - 1,
			childAssetCategory.getRightCategoryId());
	}

	@Test
	public void testMoveTreeIntoTreeFromLeft() throws Exception {
		long groupId = ServiceTestUtil.nextLong();

		AssetCategory parentAssetCategory = addAssetCategory(groupId, null);

		AssetCategory parentChildAssetCategory = addAssetCategory(groupId,
				parentAssetCategory.getCategoryId());

		parentAssetCategory = _persistence.fetchByPrimaryKey(parentAssetCategory.getPrimaryKey());

		AssetCategory rootAssetCategory = addAssetCategory(groupId, null);

		AssetCategory leftRootChildAssetCategory = addAssetCategory(groupId,
				rootAssetCategory.getCategoryId());

		rootAssetCategory = _persistence.fetchByPrimaryKey(rootAssetCategory.getPrimaryKey());

		AssetCategory rightRootChildAssetCategory = addAssetCategory(groupId,
				rootAssetCategory.getCategoryId());

		rootAssetCategory = _persistence.fetchByPrimaryKey(rootAssetCategory.getPrimaryKey());

		long previousRootLeftCategoryId = rootAssetCategory.getLeftCategoryId();
		long previousRootRightCategoryId = rootAssetCategory.getRightCategoryId();

		parentAssetCategory.setParentCategoryId(rightRootChildAssetCategory.getCategoryId());

		_persistence.update(parentAssetCategory, false);

		rootAssetCategory = _persistence.fetchByPrimaryKey(rootAssetCategory.getPrimaryKey());
		leftRootChildAssetCategory = _persistence.fetchByPrimaryKey(leftRootChildAssetCategory.getPrimaryKey());
		rightRootChildAssetCategory = _persistence.fetchByPrimaryKey(rightRootChildAssetCategory.getPrimaryKey());
		parentChildAssetCategory = _persistence.fetchByPrimaryKey(parentChildAssetCategory.getPrimaryKey());

		Assert.assertEquals(previousRootLeftCategoryId - 4,
			rootAssetCategory.getLeftCategoryId());
		Assert.assertEquals(previousRootRightCategoryId,
			rootAssetCategory.getRightCategoryId());
		Assert.assertEquals(rootAssetCategory.getLeftCategoryId() + 1,
			leftRootChildAssetCategory.getLeftCategoryId());
		Assert.assertEquals(rootAssetCategory.getRightCategoryId() - 7,
			leftRootChildAssetCategory.getRightCategoryId());
		Assert.assertEquals(rootAssetCategory.getLeftCategoryId() + 3,
			rightRootChildAssetCategory.getLeftCategoryId());
		Assert.assertEquals(rootAssetCategory.getRightCategoryId() - 1,
			rightRootChildAssetCategory.getRightCategoryId());
		Assert.assertEquals(rightRootChildAssetCategory.getLeftCategoryId() +
			1, parentAssetCategory.getLeftCategoryId());
		Assert.assertEquals(rightRootChildAssetCategory.getRightCategoryId() -
			1, parentAssetCategory.getRightCategoryId());
		Assert.assertEquals(parentAssetCategory.getLeftCategoryId() + 1,
			parentChildAssetCategory.getLeftCategoryId());
		Assert.assertEquals(parentAssetCategory.getRightCategoryId() - 1,
			parentChildAssetCategory.getRightCategoryId());
	}

	@Test
	public void testMoveTreeIntoTreeFromRight() throws Exception {
		long groupId = ServiceTestUtil.nextLong();

		AssetCategory rootAssetCategory = addAssetCategory(groupId, null);

		AssetCategory leftRootChildAssetCategory = addAssetCategory(groupId,
				rootAssetCategory.getCategoryId());

		rootAssetCategory = _persistence.fetchByPrimaryKey(rootAssetCategory.getPrimaryKey());

		AssetCategory rightRootChildAssetCategory = addAssetCategory(groupId,
				rootAssetCategory.getCategoryId());

		rootAssetCategory = _persistence.fetchByPrimaryKey(rootAssetCategory.getPrimaryKey());

		long previousRootLeftCategoryId = rootAssetCategory.getLeftCategoryId();
		long previousRootRightCategoryId = rootAssetCategory.getRightCategoryId();

		AssetCategory parentAssetCategory = addAssetCategory(groupId, null);

		AssetCategory parentChildAssetCategory = addAssetCategory(groupId,
				parentAssetCategory.getCategoryId());

		parentAssetCategory = _persistence.fetchByPrimaryKey(parentAssetCategory.getPrimaryKey());

		parentAssetCategory.setParentCategoryId(leftRootChildAssetCategory.getCategoryId());

		_persistence.update(parentAssetCategory, false);

		rootAssetCategory = _persistence.fetchByPrimaryKey(rootAssetCategory.getPrimaryKey());
		leftRootChildAssetCategory = _persistence.fetchByPrimaryKey(leftRootChildAssetCategory.getPrimaryKey());
		rightRootChildAssetCategory = _persistence.fetchByPrimaryKey(rightRootChildAssetCategory.getPrimaryKey());
		parentChildAssetCategory = _persistence.fetchByPrimaryKey(parentChildAssetCategory.getPrimaryKey());

		Assert.assertEquals(previousRootLeftCategoryId,
			rootAssetCategory.getLeftCategoryId());
		Assert.assertEquals(previousRootRightCategoryId + 4,
			rootAssetCategory.getRightCategoryId());
		Assert.assertEquals(rootAssetCategory.getLeftCategoryId() + 1,
			leftRootChildAssetCategory.getLeftCategoryId());
		Assert.assertEquals(rootAssetCategory.getRightCategoryId() - 3,
			leftRootChildAssetCategory.getRightCategoryId());
		Assert.assertEquals(rootAssetCategory.getLeftCategoryId() + 7,
			rightRootChildAssetCategory.getLeftCategoryId());
		Assert.assertEquals(rootAssetCategory.getRightCategoryId() - 1,
			rightRootChildAssetCategory.getRightCategoryId());
		Assert.assertEquals(leftRootChildAssetCategory.getLeftCategoryId() + 1,
			parentAssetCategory.getLeftCategoryId());
		Assert.assertEquals(leftRootChildAssetCategory.getRightCategoryId() -
			1, parentAssetCategory.getRightCategoryId());
		Assert.assertEquals(parentAssetCategory.getLeftCategoryId() + 1,
			parentChildAssetCategory.getLeftCategoryId());
		Assert.assertEquals(parentAssetCategory.getRightCategoryId() - 1,
			parentChildAssetCategory.getRightCategoryId());
	}

	protected AssetCategory addAssetCategory(long groupId, Long parentCategoryId)
		throws Exception {
		long pk = ServiceTestUtil.nextLong();

		AssetCategory assetCategory = _persistence.create(pk);

		assetCategory.setUuid(ServiceTestUtil.randomString());
		assetCategory.setGroupId(groupId);

		assetCategory.setCompanyId(ServiceTestUtil.nextLong());

		assetCategory.setUserId(ServiceTestUtil.nextLong());

		assetCategory.setUserName(ServiceTestUtil.randomString());

		assetCategory.setCreateDate(ServiceTestUtil.nextDate());

		assetCategory.setModifiedDate(ServiceTestUtil.nextDate());

		assetCategory.setLeftCategoryId(ServiceTestUtil.nextLong());

		assetCategory.setRightCategoryId(ServiceTestUtil.nextLong());

		assetCategory.setName(ServiceTestUtil.randomString());

		assetCategory.setTitle(ServiceTestUtil.randomString());

		assetCategory.setDescription(ServiceTestUtil.randomString());

		assetCategory.setVocabularyId(ServiceTestUtil.nextLong());

		if (parentCategoryId != null) {
			assetCategory.setParentCategoryId(parentCategoryId);
		}

		_persistence.update(assetCategory, false);

		return assetCategory;
	}

	private AssetCategoryPersistence _persistence;
}