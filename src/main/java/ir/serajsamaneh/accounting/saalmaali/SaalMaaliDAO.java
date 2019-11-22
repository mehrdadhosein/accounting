package ir.serajsamaneh.accounting.saalmaali;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Projections;

import ir.serajsamaneh.core.base.BaseHibernateDAO;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.util.SerajMessageUtil;

public class SaalMaaliDAO extends BaseHibernateDAO<SaalMaaliEntity,Long> {

	public SaalMaaliEntity load(Map<String, Object> filter){
		List<SaalMaaliEntity> list = getDataList(null, filter, null, false, FlushMode.MANUAL, true);
		if (list.size() ==1)
			return list.get(0);
		else if(list.size()==0)
			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_NoCurrentYearForThisOrgan"));
		else throw new IllegalStateException();

	}
	
	public SaalMaaliEntity getLastSaalMaali(Long currentOrganId){
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organId@eq", currentOrganId);
		
		Criteria criteria = getEmptyCriteria();
		
		addFilterCriteria(filter, criteria);
		
		criteria.setProjection(Projections.projectionList().add(Projections.max(SaalMaaliEntity.PROP_ID)));
		
		Long saalMaaliID = (Long) criteria.setFlushMode(FlushMode.MANUAL)
		.uniqueResult();
		
		if (saalMaaliID == null)
			return null;
		return load(saalMaaliID);
	}


}