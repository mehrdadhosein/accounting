package ir.serajsamaneh.accounting.sanadhesabdari;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import ir.serajsamaneh.accounting.base.BaseAccountingDAO;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;

@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SanadHesabdariDAO extends BaseAccountingDAO<SanadHesabdariEntity, Long> {

	public Long getMaxSanadSerial(SaalMaaliEntity saalMaaliEntity, Long organId) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		filter.put("organId@eq", organId);

		addLocationFilter(filter);

		Criteria criteria = getEmptyCriteria();

		addFilterCriteria(filter, criteria);

		criteria.setProjection(Projections.projectionList().add(Projections.max(SanadHesabdariEntity.PROP_SERIAL)));

		Long uniqueResult = (Long) criteria.setFlushMode(FlushMode.ALWAYS).uniqueResult();

		if (uniqueResult == null)
			return getLocationStart() + 0l;
		return uniqueResult;
	}

	public Long getMaxTempSanadSerial(SaalMaaliEntity saalMaaliEntity) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("saalMaali.id@eq", saalMaaliEntity.getId());

		addLocationFilter(filter);

		Criteria criteria = getEmptyCriteria();

		addFilterCriteria(filter, criteria);

		criteria.setProjection(
				Projections.projectionList().add(Projections.max(SanadHesabdariEntity.PROP_TEMP_SERIAL)));

		Long uniqueResult = (Long) criteria.setFlushMode(FlushMode.MANUAL).uniqueResult();

		if (uniqueResult == null)
			return getLocationStart() + 0l;
		return uniqueResult;
	}

	public Long getMaxDaemSanadSerial(Long organId, SaalMaaliEntity saalMaaliEntity) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organId@eq", organId);
		filter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		filter.put("state@eq", SanadStateEnum.DAEM);

		addLocationFilter(filter);

		Criteria criteria = getEmptyCriteria();

		addFilterCriteria(filter, criteria);

		criteria.setProjection(Projections.projectionList().add(Projections.max(SanadHesabdariEntity.PROP_SERIAL)));

		Long uniqueResult = (Long) criteria.setFlushMode(FlushMode.MANUAL).uniqueResult();

		return uniqueResult;

	}

	public Date getMaxDaemSanadTarikh(Long organId, SaalMaaliEntity saalMaaliEntity) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organId@eq", organId);
		filter.put("saalMaali.id@eq", saalMaaliEntity.getId());
		filter.put("state@eq", SanadStateEnum.DAEM);

		addLocationFilter(filter);

		Criteria criteria = getEmptyCriteria();

		addFilterCriteria(filter, criteria);

		criteria.setProjection(
				Projections.projectionList().add(Projections.max(SanadHesabdariEntity.PROP_TARIKH_SANAD)));

		Date uniqueResult = (Date) criteria.setFlushMode(FlushMode.MANUAL).uniqueResult();

		return uniqueResult;

	}

	private void addLocationFilter(Map<String, Object> filter) {

	}

	public SanadHesabdariEntity loadLastSanadEntity(Long organId, SaalMaaliEntity activeSaalmaali) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organId@eq", organId);
		filter.put("saalMaali.id@eq", activeSaalmaali.getId());

		addLocationFilter(filter);

		Criteria criteria = getEmptyCriteria();

		addFilterCriteria(filter, criteria);

		criteria.setProjection(Projections.projectionList().add(Projections.max(SanadHesabdariEntity.PROP_ID)));

		Long maxId = (Long) criteria.setFlushMode(FlushMode.MANUAL).uniqueResult();

		if (maxId != null)
			return load(maxId);
		return null;
	}

	public Long getLastSanadEntityID(Long organId, SaalMaaliEntity activeSaalmaali) {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("organId@eq", organId);
		filter.put("saalMaali.id@eq", activeSaalmaali.getId());

		addLocationFilter(filter);

		Criteria criteria = getEmptyCriteria();

		addFilterCriteria(filter, criteria);

		criteria.setProjection(Projections.projectionList().add(Projections.max(SanadHesabdariEntity.PROP_ID)));

		Long maxId = (Long) criteria.setFlushMode(FlushMode.MANUAL).uniqueResult();
		return maxId;
	}

}