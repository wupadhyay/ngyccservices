package com.yodlee.yccdomx.entity;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.yodlee.dom.entity.SumInfoSptdLocale;
import com.yodlee.domx.base.ServiceBaseExt;
import com.yodlee.domx.storage.SearchSiteLocalStorage;
import com.yodlee.framework.runtime.annotation.Derived;
import com.yodlee.framework.runtime.dao.Criteria;
import com.yodlee.nextgen.logging.MessageController;
import com.yodlee.yccdom.entity.CobGlobalMessage;
import com.yodlee.yccdom.entity.FiRepository;
import com.yodlee.yccdom.entity.FiRepository_;
/**
 * 
 * @author knavuluri
 *
 */
public class CobGlobalMessageExt extends ServiceBaseExt<CobGlobalMessage> {

	private static final String FQCN = CobGlobalMessageExt.class.getName();
	
	public CobGlobalMessageExt() {
		super(CobGlobalMessage.class);
	}

	@Override
	public SumInfoSptdLocale getDftSptdLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SumInfoSptdLocale> getSptdLocales() {
		// TODO Auto-generated method stub
		return null;
	}
	@Derived(arguments = { "cobrandId" })
	public String getCobrandNameExt() {
		long cobrandId = entity.getCobrandId();
	/**	if(cobrandId == Long.valueOf(cobrandIdstr).longValue()) {
			Criteria c = new Criteria();
			c.add(FiRepository_.cobrandId, cobrandId);
			FiRepository fis = null;
			try {
				//List<FiRepository> list2 = FiRepository.DAO.get(FiRepository_.cobrandId,cobrandId);
				List<FiRepository> list = FiRepository.DAO.select(c);
				if (list != null && !list.isEmpty())
					return list.get(0).getFiName();
				//fis = FiRepository.DAO.getSingle(FiRepository_.cobrandId, cobrandId);
//			if (fis != null)
//				return fis.getFiName();
			} catch (Exception e) {
				MessageController.log(FQCN, 47, "Exception while getting the fi repository:" + ExceptionUtils.getStackTrace(e), MessageController.SERIOUS);
			}
		}
*/
		return "";
	}
}
