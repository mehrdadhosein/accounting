package ir.serajsamaneh.accounting.moeentafsili;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.base.BaseHibernateDAO;

import org.hibernate.FlushMode;
import org.springframework.transaction.annotation.Transactional;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.exception.FatalException;

public class MoeenTafsiliService extends
		BaseEntityService<MoeenTafsiliEntity, Long> {

	@Override
	protected MoeenTafsiliDAO getMyDAO() {
		return moeenTafsiliDAO;
	}

	MoeenTafsiliDAO moeenTafsiliDAO;

	public void setMoeenTafsiliDAO(MoeenTafsiliDAO moeenTafsiliDAO) {
		this.moeenTafsiliDAO = moeenTafsiliDAO;
	}

	public MoeenTafsiliDAO getMoeenTafsiliDAO() {
		return moeenTafsiliDAO;
	}

	public MoeenTafsiliEntity load(HesabTafsiliEntity hesabTafsiliEntity,
			HesabMoeenEntity hesabMoeenEntity, Integer level, FlushMode flushMode) {
		Map<String, Object> localFilter = new HashMap<String, Object>();
		localFilter.put("hesabTafsili.id@eq", hesabTafsiliEntity.getId());
		localFilter.put("hesabMoeen.id@eq", hesabMoeenEntity.getId());
		localFilter.put("level@eq", level);
		List<MoeenTafsiliEntity> dataList = getDataList(null, localFilter, flushMode);
		if(dataList.size() == 1)
			return dataList.get(0);
		else if(dataList.size() == 0)
			return null;
		throw new FatalException();
	}

}