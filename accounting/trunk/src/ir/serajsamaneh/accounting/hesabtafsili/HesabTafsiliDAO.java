package ir.serajsamaneh.accounting.hesabtafsili;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;

import ir.serajsamaneh.accounting.hesabclassification.HesabClassificationEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.core.base.BaseHibernateDAO;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.organ.OrganEntity;

public class HesabTafsiliDAO extends BaseHibernateDAO<HesabTafsiliEntity,Long> {



/*	public Long getMaxHesabTafsiliCode(OrganEntity currentOrgan, SaalMaaliEntity activeSaalMaaliEntity) {
		Map<String, Object> filter = new HashMap<String, Object>();
//		filter.put("saalMaali.id@eq", currentUserSaalMaaliEntity.getId());
		filter.put("organ.id@eq", currentOrgan.getId());
		filter.put("saalMaali.id@eq", activeSaalMaaliEntity.getId());
		addLocationFilter(filter);

		Criteria criteria = getEmptyCriteria();
		addFilterCriteria(filter, criteria);

		// criteria.setProjection(Projections.projectionList().add(
		// Projections.max(KalaEntity.PROP_CODE)));

		List<HesabTafsiliEntity> list = criteria.setFlushMode(FlushMode.MANUAL).list();
		// .uniqueResult();

		Long maxCode = 0l;
		for (HesabTafsiliEntity kalaEntity : list) {
			Long codeLong = kalaEntity.getCode();
			if(codeLong!=null){
//				Long codeLong = new Long(codeStr);
				maxCode = Math.max(maxCode.longValue(), codeLong.longValue());
			}

		}
//		for (HesabTafsiliEntity kalaEntity : list) {
//			String codeStr = kalaEntity.getCode();
//			if(StringUtils.hasText(codeStr)){
//				Long codeLong = new Long(codeStr);
//				maxCode = Math.max(maxCode.longValue(), codeLong.longValue());
//			}
//			
//		}

		return maxCode;

	}*/
	
	private void addLocationFilter(Map<String, Object> filter) {

	}

	public Long getMaxHesabTafsiliCode(HesabClassificationEntity hesabClassification, Integer kalaCodeCharactersNumber, OrganEntity organEntity, SaalMaaliEntity saalMaaliEntity) {
//		groupKala = getGroupKalaDAO().load(groupKala.getId());
		Map<String, Object> filter = new HashMap<String, Object>();
		
		if(hesabClassification!=null && hesabClassification.getId()!=null)
			filter.put("hesabClassification.id@eq", hesabClassification.getId());
		else
			filter.put("hesabClassification@isNull", "ding");
		filter.put("organ.id@eq", organEntity.getId());
		filter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		filter.put("code@isNotNull", "ding");

		addLocationFilter(filter);

		Criteria criteria = getEmptyCriteria();
		addFilterCriteria(filter, criteria);

		List<HesabTafsiliEntity> list = criteria.setFlushMode(FlushMode.MANUAL).list();
		// .uniqueResult();

		Long maxCode = 0l;
		for (HesabTafsiliEntity habTafsiliEntity : list) {
			
//			if(habTafsiliEntity.getCode().indexOf("-")!=-1){
//				String groupPart = habTafsiliEntity.getCode().substring(0, habTafsiliEntity.getCode().indexOf("-"));
//				if(!groupPart.equals(groupKala.getCode()))
//					throw new FatalException(SerajMessageUtil.getMessage("KalaCodeIsInvalid",habTafsiliEntity));
//			}
//			
			String classificationCode = (hesabClassification!=null && hesabClassification.getId()!=null) ? hesabClassification.getCode().toString() : "";
			if(habTafsiliEntity.getCode() == null)
				continue;
			String codeStr = habTafsiliEntity.getCode().toString().replaceFirst(classificationCode, "");
//			String codeNumberOnly = codeStr.replaceAll("-", "");
			Long codeLong = new Long(codeStr);
			maxCode = Math.max(maxCode.longValue(), codeLong.longValue());

		}
		maxCode++;
		
		String maxCodeStr = createCode(hesabClassification, maxCode , kalaCodeCharactersNumber);

		return new Long(maxCodeStr);
	}

	private String createCode(HesabClassificationEntity hesabClassification, Long maxCode,	Integer kalaCodeCharactersNumber) {
		if(hesabClassification!=null && hesabClassification.getId()!=null){
			String maxCodeStr="";
			String parentCode = hesabClassification.getCode().toString();
			
			if(kalaCodeCharactersNumber>0){
				Integer noeZeroDigitCount = parentCode.length() + maxCode.toString().length();
				Integer zeroDigitCount =  kalaCodeCharactersNumber - noeZeroDigitCount;
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