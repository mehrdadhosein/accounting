package ir.serajsamaneh.accounting.hesabmoeen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;

import ir.serajsamaneh.accounting.hesabkol.HesabKolDAO;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.core.base.BaseHibernateDAO;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.StringUtil;

public class HesabMoeenDAO extends BaseHibernateDAO<HesabMoeenEntity,Long> {

	HesabKolDAO hesabKolDAO;

	public HesabKolDAO getHesabKolDAO() {
		return hesabKolDAO;
	}

	public void setHesabKolDAO(HesabKolDAO hesabKolDAO) {
		this.hesabKolDAO = hesabKolDAO;
	}


	
	private void addLocationFilter(Map<String, Object> filter) {

	}
	
	public String getMaxHesabMoeenCode(HesabKolEntity hesabKol, Integer codeCharactersNumber, Long organId, SaalMaaliEntity saalMaaliEntity) {
//		groupKala = getGroupKalaDAO().load(groupKala.getId());
		Map<String, Object> filter = new HashMap<String, Object>();
		
		if(hesabKol!=null && hesabKol.getId()!=null)
			filter.put("hesabKol.id@eq", hesabKol.getId());
		else
			filter.put("hesabKol@isNotNull", "ding");
		filter.put("organId@eq", organId);
		filter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		filter.put("code@isNotNull", "ding");

		addLocationFilter(filter);

		Criteria criteria = getEmptyCriteria();
		addFilterCriteria(filter, criteria);

		List<HesabMoeenEntity> list = criteria.setFlushMode(FlushMode.MANUAL).list();
		// .uniqueResult();

		Long maxCode = 0l;
		for (HesabMoeenEntity hasabMoeenEntity : list) {
//			
			String kolCode = (hesabKol!=null && hesabKol.getId()!=null) ? hesabKol.getCode().toString() : "";
			if(!StringUtil.hasText(hasabMoeenEntity.getCode()))
				continue;
			String codeStr = hasabMoeenEntity.getCode().toString().replaceFirst(kolCode, "");
//			String codeNumberOnly = codeStr.replaceAll("-", "");
			Long codeLong = new Long(codeStr);
			maxCode = Math.max(maxCode.longValue(), codeLong.longValue());

		}
		maxCode++;
		
		String maxCodeStr = createCode(hesabKol, maxCode , codeCharactersNumber);

		return maxCodeStr;
	}

	private String createCode(HesabKolEntity hesabKol, Long maxCode,	Integer codeCharactersNumber) {
		if(hesabKol!=null && hesabKol.getId()!=null){
			String maxCodeStr="";
			String parentCode = hesabKol.getCode().toString();
			
			if(codeCharactersNumber>0){
				Integer noeZeroDigitCount = parentCode.length() + maxCode.toString().length();
				Integer zeroDigitCount =  codeCharactersNumber - noeZeroDigitCount;
				if(zeroDigitCount < 0)
					throw new FatalException();
				
				for(int i = 0 ; i<zeroDigitCount; ++i)
					parentCode = parentCode + "0";
			}
			maxCodeStr = parentCode + maxCode;
			return maxCodeStr;
		}else
			return  maxCode.toString();
	}


}