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

package com.liferay.portal.dao.orm.hibernate;

import com.liferay.portal.kernel.dao.orm.CacheMode;
import com.liferay.portal.kernel.dao.orm.LockMode;
import com.liferay.portal.kernel.dao.orm.ORMException;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.ScrollableResults;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.UnmodifiableList;

import java.io.Serializable;

import java.sql.Timestamp;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public class SQLQueryImpl implements SQLQuery {

	public SQLQueryImpl(org.hibernate.SQLQuery sqlQuery, boolean strictName) {
		_sqlQuery = sqlQuery;

		if (!_strictName) {
			_names = sqlQuery.getNamedParameters();

			Arrays.sort(_names);
		}
	}

	public SQLQuery addEntity(String alias, Class<?> entityClass) {
		_sqlQuery.addEntity(alias, entityClass);

		return this;
	}

	public SQLQuery addScalar(String columnAlias, Type type) {
		_sqlQuery.addScalar(columnAlias, TypeTranslator.translate(type));

		return this;
	}

	public int executeUpdate() throws ORMException {
		try {
			return _sqlQuery.executeUpdate();
		}
		catch (Exception e) {
			throw ExceptionTranslator.translate(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public Iterator iterate() throws ORMException {
		return iterate(true);
	}

	@SuppressWarnings("rawtypes")
	public Iterator iterate(boolean unmodifiable) throws ORMException {
		try {
			return list(unmodifiable).iterator();
		}
		catch (Exception e) {
			throw ExceptionTranslator.translate(e);
		}
	}

	public Object iterateNext() throws ORMException {
		Iterator<?> iterator = iterate(false);

		if (iterator.hasNext()) {
			return iterator.next();
		}

		return null;
	}

	public List<?> list() throws ORMException {
		return list(false, false);
	}

	public List<?> list(boolean unmodifiable) throws ORMException {
		return list(true, unmodifiable);
	}

	public List<?> list(boolean copy, boolean unmodifiable)
		throws ORMException {

		try {
			List<?> list = _sqlQuery.list();

			if (unmodifiable) {
				list = new UnmodifiableList<Object>(list);
			}
			else if (copy) {
				list = ListUtil.copy(list);
			}

			return list;
		}
		catch (Exception e) {
			throw ExceptionTranslator.translate(e);
		}
	}

	public ScrollableResults scroll() throws ORMException {
		try {
			return new ScrollableResultsImpl(_sqlQuery.scroll());
		}
		catch (Exception e) {
			throw ExceptionTranslator.translate(e);
		}
	}

	public Query setBoolean(int pos, boolean value) {
		_sqlQuery.setBoolean(pos, value);

		return this;
	}

	public Query setBoolean(String name, boolean value) {
		if (!_strictName && (Arrays.binarySearch(_names, name) < 0)) {
			return this;
		}

		_sqlQuery.setBoolean(name, value);

		return this;
	}

	public Query setCacheable(boolean cacheable) {
		_sqlQuery.setCacheable(cacheable);

		return this;
	}

	public Query setCacheMode(CacheMode cacheMode) {
		_sqlQuery.setCacheMode(CacheModeTranslator.translate(cacheMode));

		return this;
	}

	public Query setCacheRegion(String cacheRegion) {
		_sqlQuery.setCacheRegion(cacheRegion);

		return this;
	}

	public Query setDouble(int pos, double value) {
		_sqlQuery.setDouble(pos, value);

		return this;
	}

	public Query setDouble(String name, double value) {
		if (!_strictName && (Arrays.binarySearch(_names, name) < 0)) {
			return this;
		}

		_sqlQuery.setDouble(name, value);

		return this;
	}

	public Query setFirstResult(int firstResult) {
		_sqlQuery.setFirstResult(firstResult);

		return this;
	}

	public Query setFloat(int pos, float value) {
		_sqlQuery.setFloat(pos, value);

		return this;
	}

	public Query setFloat(String name, float value) {
		if (!_strictName && (Arrays.binarySearch(_names, name) < 0)) {
			return this;
		}

		_sqlQuery.setFloat(name, value);

		return this;
	}

	public Query setInteger(int pos, int value) {
		_sqlQuery.setInteger(pos, value);

		return this;
	}

	public Query setInteger(String name, int value) {
		if (!_strictName && (Arrays.binarySearch(_names, name) < 0)) {
			return this;
		}

		_sqlQuery.setInteger(name, value);

		return this;
	}

	public Query setLockMode(String alias, LockMode lockMode) {
		_sqlQuery.setLockMode(alias, LockModeTranslator.translate(lockMode));

		return this;
	}

	public Query setLong(int pos, long value) {
		_sqlQuery.setLong(pos, value);

		return this;
	}

	public Query setLong(String name, long value) {
		if (!_strictName && (Arrays.binarySearch(_names, name) < 0)) {
			return this;
		}

		_sqlQuery.setLong(name, value);

		return this;
	}

	public Query setMaxResults(int maxResults) {
		_sqlQuery.setMaxResults(maxResults);

		return this;
	}

	public Query setSerializable(int pos, Serializable value) {
		_sqlQuery.setSerializable(pos, value);

		return this;
	}

	public Query setSerializable(String name, Serializable value) {
		if (!_strictName && (Arrays.binarySearch(_names, name) < 0)) {
			return this;
		}

		_sqlQuery.setSerializable(name, value);

		return this;
	}

	public Query setShort(int pos, short value) {
		_sqlQuery.setShort(pos, value);

		return this;
	}

	public Query setShort(String name, short value) {
		if (!_strictName && (Arrays.binarySearch(_names, name) < 0)) {
			return this;
		}

		_sqlQuery.setShort(name, value);

		return this;
	}

	public Query setString(int pos, String value) {
		_sqlQuery.setString(pos, value);

		return this;
	}

	public Query setString(String name, String value) {
		if (!_strictName && (Arrays.binarySearch(_names, name) < 0)) {
			return this;
		}

		_sqlQuery.setString(name, value);

		return this;
	}

	public Query setTimestamp(int pos, Timestamp value) {
		_sqlQuery.setTimestamp(pos, value);

		return this;
	}

	public Query setTimestamp(String name, Timestamp value) {
		if (!_strictName && (Arrays.binarySearch(_names, name) < 0)) {
			return this;
		}

		_sqlQuery.setTimestamp(name, value);

		return this;
	}

	public Object uniqueResult() throws ORMException {
		try {
			return _sqlQuery.uniqueResult();
		}
		catch (Exception e) {
			throw ExceptionTranslator.translate(e);
		}
	}

	private String[] _names;
	private org.hibernate.SQLQuery _sqlQuery;
	private boolean _strictName;

}