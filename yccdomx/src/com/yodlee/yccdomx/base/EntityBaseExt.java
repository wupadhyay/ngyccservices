/*
* Copyright (c) 2016 Yodlee, Inc. All Rights Reserved.
*
* This software is the confidential and proprietary information of Yodlee, Inc.
* Use is subject to license terms.
*/
package com.yodlee.yccdomx.base;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.metamodel.SingularAttribute;

import com.yodlee.common.context.CobrandContext_;
import com.yodlee.common.context.UserContext_;
import com.yodlee.dom.entity.Country;
import com.yodlee.dom.entity.DbMessageCatalog;
import com.yodlee.dom.entity.DbMessageCatalog_;
import com.yodlee.dom.entity.Language;
import com.yodlee.dom.entity.Locale;
import com.yodlee.framework.runtime.dao.Criteria;
import com.yodlee.framework.runtime.dao.EntityDao;
import com.yodlee.framework.runtime.meta.MetamodelHelper;
import com.yodlee.framework.runtime.meta.MetamodelHelper.DomTypeExt;
import com.yodlee.framework.runtime.shared.context.ContextAccessorUtil;
import com.yodlee.framework.runtime.shared.dao.IEntityDao;
import com.yodlee.framework.runtime.shared.dom.IEntity;
import com.yodlee.framework.runtime.shared.dom.INotDerivedEntity;
import com.yodlee.framework.runtime.util.ReflectionUtil;
import com.yodlee.framework.service.dao.DataAccessor;
import com.yodlee.framework.service.handlers.IServiceContext;
import com.yodlee.framework.service.json.domx.proxy.DomxDaoProxyJson;
import com.yodlee.framework.service.permission.Permissions;
import com.yodlee.framework.service.permission.Permissions.Access;
import com.yodlee.framework.service.shared.domx.IDomExt;
import com.yodlee.framework.service.shared.domx.IFilterOption;
/**
 * 
 * @author knavuluri
 *
 */
abstract public class EntityBaseExt<E extends IEntity, I> implements IDomExt<E> {
	private static final String FQCN = EntityBaseExt.class.getName();

	public static final String DECRYPT_DISABLED_PROPERTY_NAME = "com.yodlee.DecryptDisabled";
	private static final Object CRYPT;
    private static final Method CRYPT_HANDLER;
    private static final Object MESSAGE_DIGEST;
    private static final Method MESSAGE_DIGEST_HANDLER;
	protected Class<E> entityJavaType;
	protected Method idGetter;
	protected E entity;
	protected I id;
	protected String displayNameBundleName;
	protected String helpTextBundleName;
	//sjr TODO Service dependency - refactor
	protected Object parentBinding;
	protected DataAccessor ancestorAccessor;
	protected IServiceContext serviceContext;
	//
	protected Criteria ancestorCriteria;
	protected boolean isUseSdkDaoProxy;
	protected boolean isUseJsonDaoProxy;
	private Map<Class<?>, IEntityDao<?>> entityDaosByRelatedEntityType = new HashMap<Class<?>, IEntityDao<?>>();
	public static final boolean IS_DECRYPT_DISABLED;
	public static final String USE_NEW_ARG = "USE_NEW";
	public static final String USE_NONE_ARG = null;
	public static final Object[] USE_NEW = new Object[]{USE_NEW_ARG};
	public static final Object[] USE_NONE = new Object[]{USE_NONE_ARG};
	private Map<String, String> mfaTypeCache = new HashMap<String, String>(5, 1);

	static {
		IS_DECRYPT_DISABLED = "TRUE".equalsIgnoreCase(System.getProperty(DECRYPT_DISABLED_PROPERTY_NAME));
		try {
			Class<?> cryptUtil = null;
			Class<?> hashUtil = null;
			cryptUtil = Class.forName("com.yodlee.util.security.EncryptionFactoryUtil");
			hashUtil = Class.forName("com.yodlee.util.security.HashUtil");
			CRYPT = cryptUtil.getMethod("getInstance").invoke(null);
			CRYPT_HANDLER = cryptUtil.getMethod("handleDecryptEncrypt", String.class, String.class, String.class, boolean.class);
			MESSAGE_DIGEST = hashUtil.getMethod("getInstance").invoke(null);
			MESSAGE_DIGEST_HANDLER = hashUtil.getMethod("hash", String.class, String.class);
			//
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	protected EntityBaseExt() {
	}
		
	protected EntityBaseExt(Class<E> entityJavaType) {
		this.entityJavaType = entityJavaType;
	}
	
	@Override
	public void initParent(Object parentBinding, DataAccessor ancestorAccessor, Criteria ancestorCriteria, 
		boolean isUseDaoProxy) 
	{
		this.parentBinding = parentBinding;
		this.ancestorAccessor = ancestorAccessor;
		this.ancestorCriteria = ancestorCriteria;
		this.isUseSdkDaoProxy = isUseDaoProxy && ancestorAccessor == null;
		this.isUseJsonDaoProxy = isUseDaoProxy && ancestorAccessor != null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(E domObject, Object context) {
		this.entity = domObject;
		if (idGetter != null)
			id = (I)ReflectionUtil.invokeGetter(idGetter, domObject);
		serviceContext = (IServiceContext)context;
	}
	
	public boolean isCacheable() {
		return false;
	}

	public static long getCobrandId() {
		return (Long)ContextAccessorUtil.getContextAccessor().getValue(CobrandContext_.cobrandId);
	}
	
	public static long getMemId() {
		return (Long)ContextAccessorUtil.getContextAccessor().getValue(UserContext_.memId);
	}

	//TODO: Pass and check the param key for encryption / decryption enabled
	protected static String[] crypt(String[] values, boolean isEncrypt) {
		if (values == null || values.length == 0)
			return null;
		String[] resultValues = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			resultValues[i] = crypt(values[i], true);
		}
		return resultValues;
	}

	protected static String crypt(String value, boolean isEncrypt) {
		/*sjr TODO!!! TEMPORARY
		if (CRYPT == null)
			return value;
		*/
		return value != null ? (String) ReflectionUtil.invokeMethod(
				CRYPT_HANDLER, CRYPT, "account_credentials", null, value, isEncrypt) : null;
	}
	
	protected static String hash(String value, String algorithm) {
		return value != null ? (String) ReflectionUtil.invokeMethod(
				MESSAGE_DIGEST_HANDLER, MESSAGE_DIGEST, value, algorithm) : null;
	}		

	protected static String decrypt(String attrName, String attrValue) {
		if(attrValue != null) { 
			try {
			String value = (String)ReflectionUtil.invokeMethod(
					CRYPT_HANDLER, CRYPT, "itemsummary", attrName, attrValue, false);
			return value;
			} catch(Exception e) {
				return attrValue;
			}
			
		}
		return null;
	}
	
	protected static String decryptPKI(String value) {
		return value != null ? (String) ReflectionUtil.invokeMethod(
				CRYPT_HANDLER, CRYPT, "pki_credentials", null, value, false) : null;
	}

	protected boolean isSingle(List<?> list) {
		return list != null && list.size() == 1;
	}
	
	protected boolean isMulti(List<?> list) {
		return list != null && list.size() > 1;
	}
	
	
	public static String encryptOrDecrypt(String value, boolean isEncrypt) {
		if (value == null) {
			return null;
		}
		String result = null;
		try {
			Class<?> decryptionUtil = Class.forName("com.yodlee.util.security.EncryptionFactoryUtil");
			Method instanceMethod = decryptionUtil.getMethod("getInstance");
			Object objDecryptionUtil = instanceMethod.invoke(null);
			Method method = decryptionUtil.getMethod("handleDecryptEncrypt",String.class, String.class, String.class, boolean.class);
			result = (String)method.invoke(objDecryptionUtil,"account_credentials", null , value, isEncrypt);
		} catch (Throwable e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private <E1 extends IEntity> IEntityDao<E1> getDao(Class<E1> relatedEntityJavaType) {
		IEntityDao<E1> entityDao;
		if (/*sjr isUseSdkDaoProxy || */isUseJsonDaoProxy) {
			entityDao = (IEntityDao<E1>)entityDaosByRelatedEntityType.get(relatedEntityJavaType);
			if (entityDao == null) {
				entityDao = /* sjrisUseSdkDaoProxy
					? DomxDaoProxySdk.getDomxDaoProxy(relatedEntityJavaType, IEntityDao.class, 
							EntityDao.getEntityDao(relatedEntityJavaType), entityJavaType, parentBinding, ancestorCriteria)
					:*/ DomxDaoProxyJson.getDomxDaoProxy(relatedEntityJavaType, IEntityDao.class, 
							EntityDao.getEntityDao(relatedEntityJavaType), ancestorAccessor, entityJavaType);
				entityDaosByRelatedEntityType.put(relatedEntityJavaType, entityDao);
			}
		} else
			entityDao = (IEntityDao<E1>)EntityDao.getEntityDao(relatedEntityJavaType);
		return entityDao;
	}
	
	@SuppressWarnings("unused")
	private boolean isRelated(Class<?> relatedEntityJavaType) {
		return MetamodelHelper.getAssociation(MetamodelHelper.getEntityType(relatedEntityJavaType).type, entityJavaType)
			!= null;
	}
	
	@SuppressWarnings("unchecked")
	private <E1 extends IEntity> SingularAttribute<? super E1, I> getRelatedEntityForeignId(Class<E1> relatedEntityJavaType) {
		return (SingularAttribute<? super E1, I>)MetamodelHelper
			.getForeignIdAttr(MetamodelHelper.getEntityType(relatedEntityJavaType).type, entityJavaType);
	}
	
	@Override
	public <E1 extends IEntity> List<E1> get(Class<E1> relatedEntityJavaType) {
		IEntityDao<E1> entityDao = getDao(relatedEntityJavaType);
		if (entityDao.isProxy()) {
			entityDao.init(id, entity, serviceContext);
			return entityDao.get();
		}
		return entityDao.get(getRelatedEntityForeignId(relatedEntityJavaType), id);
	}
	
	@Override
	public <E1 extends INotDerivedEntity, E2> List<E1> get(SingularAttribute<E1, E2> attr, E2 value) {
		Class<E1> relatedEntityJavaType = attr.getDeclaringType().getJavaType();
		IEntityDao<E1> entityDao = getDao(relatedEntityJavaType);
		if (entityDao.isProxy()) {
			entityDao.init(id, entity, serviceContext);
			return entityDao.get(attr, value);
		}
		return entityDao.get(getRelatedEntityForeignId(relatedEntityJavaType), id, attr, value);
	}
	
	@Override
	public <E1 extends IEntity> E1 getSingle(Class<E1> relatedEntityJavaType) {
		IEntityDao<E1> entityDao = getDao(relatedEntityJavaType);
		if (entityDao.isProxy()) {
			entityDao.init(id, entity, serviceContext);
			return entityDao.getSingle();
		}
		return entityDao.getSingle(getRelatedEntityForeignId(relatedEntityJavaType), id);
	}
	
	@Override
	public <E1 extends INotDerivedEntity, E2> E1 getSingle(SingularAttribute<E1, E2> attr, E2 value) {
		Class<E1> relatedEntityJavaType = attr.getDeclaringType().getJavaType();
		IEntityDao<E1> entityDao = getDao(relatedEntityJavaType);
		if (entityDao.isProxy()) {
			entityDao.init(id, entity, serviceContext);
			return entityDao.getSingle(attr, value);
		}
		return entityDao.getSingle (attr, value);
	}

	@Override
	public <E1 extends INotDerivedEntity, R> List<R> get(SingularAttribute<E1, R> returnAttr) {
		Class<E1> relatedEntityJavaType = returnAttr.getDeclaringType().getJavaType();
		IEntityDao<E1> entityDao = getDao(relatedEntityJavaType);
		if (entityDao.isProxy()) {
			entityDao.init(id, entity, serviceContext);
			return EntityDao.getReturnAttrs(entityDao.get(), returnAttr);
		}
		return entityDao.get(getRelatedEntityForeignId(relatedEntityJavaType), id, returnAttr);
	}

	
	protected String getMessageForKey(String bundleName, String key, Locale locale, Locale dftLocale) {
		Long localeId = getLocaleId(locale, dftLocale);
		if (localeId == null)
			return null;
		String keyToLookUp = new StringBuilder("com.yodlee").append(".").append(bundleName).append(".").append(key).toString();
		List<DbMessageCatalog> dbmc = DbMessageCatalog.DAO.get(DbMessageCatalog_.mcKey, keyToLookUp, DbMessageCatalog_.localeId, 
			localeId);			
		return dbmc != null && dbmc.size() > 0
			? dbmc.get(0).getValue()
			 : null;
		}
	
	

	
	protected String getMessage(String bundleName, Locale locale, Locale dftLocale) {
		Long localeId = getLocaleId(locale, dftLocale);
		if (localeId == null)
				return null;
		List<DbMessageCatalog> messages = DbMessageCatalog.DAO.get(DbMessageCatalog_.mcKey, bundleName, 
			DbMessageCatalog_.localeId, localeId);
		return messages.size() > 0 ? messages.get(0).getValue() : null;
	}
	
	protected Long getLocaleId(Locale locale, Locale dftLocale) {
		//sjr Fall back on a regular if because a ternary expression fails here :-(
		if (locale != null)
			return locale.getLocaleId();
		if (dftLocale != null)
			return dftLocale.getLocaleId();
		return null;
	}
	
	protected Locale getLocaleForLocaleId(Long localeId) {
		return localeId != null ? Locale.DAO.get(localeId) : null;
	}
	
	protected Country getCountryForCountryId(long countryId) {
		return Country.DAO.get(countryId);
	}
	
	protected Language getLanguageForLanguageId(long languageId) {
		return Language.DAO.get(languageId);
	}
	
	public static void addPermission(DomTypeExt domType, Access access, String name, String value) {
		addPermission(domType, null, access, name, value);
	}

	public static void addPermission(DomTypeExt domType, String attrName, Access access, String name, String value) {
		Permissions.addPermission(domType, attrName, access, name, value);
	}

	public static void addPermission(DomTypeExt domType, Access access, IFilterOption filterOption, String name) {
		addPermission(domType, null, access, filterOption, name);
	}
	
	public static void addPermission(DomTypeExt domType, String attrName, Access access, IFilterOption filterOption, 
		String name)
	{
		Permissions.addPermission(domType, attrName, access, filterOption, name);
	}

	public static void addPermission(DomTypeExt domType, Access access, IFilterOption filterOption, 
		String name, String value) 
	{
		addPermission(domType, null, access, filterOption, name, value);
	}

	public static void addPermission(DomTypeExt domType, String attrName, Access access, IFilterOption filterOption, 
		String name, String value) 
	{
		Permissions.addPermission(domType, attrName, access, filterOption, name, value);
	}
	
}

