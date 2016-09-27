package ir.serajsamaneh.accounting.hesabtafsili;

import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.core.base.BaseHibernateDAO;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.security.SecurityUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.springframework.util.StringUtils;

public class HesabTafsiliDAO extends BaseHibernateDAO<HesabTafsiliEntity,Long> {



	public Long getMaxHesabTafsiliCode(OrganEntity currentOrgan, SaalMaaliEntity currentUserSaalMaaliEntity) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("saalMaali.id@eq", currentUserSaalMaaliEntity.getId());
//		filter.put("organ.id@eq", currentOrgan.getId());
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
		if (maxCode == null)
			return getLocationStart() + 0l;
		return maxCode;

	}
	
	private void addLocationFilter(Map<String, Object> filter) {

	}


}