package ir.serajsamaneh.accounting.sanadhesabdariitem;

import ir.serajsamaneh.accounting.enumeration.HesabTypeEnum;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
import ir.serajsamaneh.core.base.BaseHibernateDAO;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.AggregateProjection;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.sql.JoinType;
import org.springframework.transaction.annotation.Transactional;

public class SanadHesabdariItemDAO  extends BaseHibernateDAO<SanadHesabdariItemEntity,Long> {

	@Transactional(readOnly=true)
	public List<Object[]> getTarazKolAzmayeshi(Map<String,Object> filter) {
		
		Criteria criteria = getEmptyCriteria();
		Map<String, String> aliasMap = addFilterCriteria(filter, criteria);
		
		//String hesabKolAlias = newCreateAlias(criteria, aliasMap, "hesabKol", 0);
		String kolIdProp = newCreateAlias(criteria, aliasMap, "hesabKol.id",JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);
		String kolCodeProp = newCreateAlias(criteria, aliasMap, "hesabKol.code", JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);
		
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.groupProperty(kolIdProp));
		projections.add(Projections.groupProperty(kolCodeProp));
		projections.add(Projections.sum("bestankar"));
		projections.add(Projections.sum("bedehkar"));
		
		criteria.setProjection(projections);
		criteria.addOrder(Order.asc(kolCodeProp));
		
		List<Object[]> list = criteria.list();
		return list;
	}

	@Transactional(readOnly=true)
	public List<Object[]> getTarazMoeenAzmayeshi(Map<String,Object> filter) {
		
		Criteria criteria = getEmptyCriteria();
		Map<String, String> aliasMap = addFilterCriteria(filter, criteria);
		
		//String alias = newCreateAlias(criteria, aliasMap, "hesabMoeen", 0);
		String moeenId = newCreateAlias(criteria, aliasMap, "hesabMoeen.id", JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);
		String moeenCode = newCreateAlias(criteria, aliasMap, "hesabMoeen.code", JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);
		
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.groupProperty(moeenId));
		projections.add(Projections.groupProperty(moeenCode));
		projections.add(Projections.sum("bestankar"));
		projections.add(Projections.sum("bedehkar"));
		
		criteria.setProjection(projections);
		
		criteria.addOrder(Order.asc(moeenCode));
		List<Object[]> list = criteria.list();
		return list;
	}
	
	@Transactional(readOnly=true)
	public List<Object[]> getTarazTafsiliAzmayeshi(Map<String,Object> filter) {
		
		Criteria criteria = getEmptyCriteria();
		Map<String, String> aliasMap = addFilterCriteria(filter, criteria);
		
		String tafsiliId = newCreateAlias(criteria, aliasMap, "hesabTafsili.id", JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);
		String tafsiliCode = newCreateAlias(criteria, aliasMap, "hesabTafsili.code", JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);		
		
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.groupProperty(tafsiliId));
		projections.add(Projections.groupProperty(tafsiliCode));
		projections.add(Projections.sum("bestankar"));
		projections.add(Projections.sum("bedehkar"));
		
		criteria.setProjection(projections);
		criteria.addOrder(Order.asc(tafsiliCode));
		
		List<Object[]> list = criteria.list();
		return list;
	}
	
	@Transactional(readOnly=true)
	public List<Object[]> getTarazTafsiliAzmayeshiTwo(Map<String,Object> filter) {
		
		Criteria criteria = getEmptyCriteria();
		Map<String, String> aliasMap = addFilterCriteria(filter, criteria);
		
		String tafsiliId = newCreateAlias(criteria, aliasMap, "hesabTafsiliTwo.id", JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);
		String tafsiliCode = newCreateAlias(criteria, aliasMap, "hesabTafsiliTwo.code", JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);		
		
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.groupProperty(tafsiliId));
		projections.add(Projections.groupProperty(tafsiliCode));
		projections.add(Projections.sum("bestankar"));
		projections.add(Projections.sum("bedehkar"));
		
		criteria.setProjection(projections);
		criteria.addOrder(Order.asc(tafsiliCode));
		
		List<Object[]> list = criteria.list();
		return list;
	}
	
	@Transactional(readOnly=true)
	public List<Object[]> getTarazShenavarAzmayeshi(Map<String,Object> filter) {
		
		Criteria criteria = getEmptyCriteria();
		Map<String, String> aliasMap = addFilterCriteria(filter, criteria);
		
		String tafsiliId = newCreateAlias(criteria, aliasMap, "articleTafsili.hesabTafsili.id", JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);
		String tafsiliCode = newCreateAlias(criteria, aliasMap, "articleTafsili.hesabTafsili.code", JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);		
		
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.groupProperty(tafsiliId));
		projections.add(Projections.groupProperty(tafsiliCode));
		projections.add(Projections.sum("bestankar"));
		projections.add(Projections.sum("bedehkar"));
		
		criteria.setProjection(projections);
		criteria.addOrder(Order.asc(tafsiliCode));
		
		List<Object[]> list = criteria.list();
		return list;
	}
	
	@Transactional(readOnly=true)
	public List<Object[]> getTarazAccountingMarkazAzmayeshi(Map<String,Object> filter) {
		
		Criteria criteria = getEmptyCriteria();
		Map<String, String> aliasMap = addFilterCriteria(filter, criteria);
		
		String accountingMarkazId = newCreateAlias(criteria, aliasMap, "accountingMarkaz.id", JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);
		String accountingMarkazCode = newCreateAlias(criteria, aliasMap, "accountingMarkaz.code", JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);		
		
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.groupProperty(accountingMarkazId));
		projections.add(Projections.groupProperty(accountingMarkazCode));
		projections.add(Projections.sum("bestankar"));
		projections.add(Projections.sum("bedehkar"));
		
		criteria.setProjection(projections);
		criteria.addOrder(Order.asc(accountingMarkazCode));
		
		List<Object[]> list = criteria.list();
		return list;
	}
	
	@Transactional(readOnly=true)
	public List<Object[]> getTarazAccountingMarkazShenavarAzmayeshi(Map<String,Object> filter) {
		
		Criteria criteria = getEmptyCriteria();
		Map<String, String> aliasMap = addFilterCriteria(filter, criteria);
		
		String accountingMarkazId = newCreateAlias(criteria, aliasMap, "articleAccountingMarkaz.accountingMarkaz.id", JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);
		String accountingMarkazCode = newCreateAlias(criteria, aliasMap, "articleAccountingMarkaz.accountingMarkaz.code", JoinType.INNER_JOIN /*CriteriaSpecification.INNER_JOIN*/);		
		
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.groupProperty(accountingMarkazId));
		projections.add(Projections.groupProperty(accountingMarkazCode));
		projections.add(Projections.sum("bestankar"));
		projections.add(Projections.sum("bedehkar"));
		
		criteria.setProjection(projections);
		criteria.addOrder(Order.asc(accountingMarkazCode));
		
		List<Object[]> list = criteria.list();
		return list;
	}
	
	@Transactional(readOnly=true)
	public Double[] getHesabAggregate(Long kolId, HesabTypeEnum hesabTypeEnum, Date from, Date to, Map<String, Object> filter){
		Criteria criteria = getEmptyCriteria();
		filter.put("hesabMoeen.hesabKol.id@eq", kolId);
		filter.put("sanadHesabdari.date@ge", from);
		filter.put("sanadHesabdari.date@le", to);
		filter.put("sanadHesabdari.state@eq", SanadStateEnum.DAEM);
		addFilterCriteria(filter, criteria);
		AggregateProjection bedehkar = Projections.sum("bedehkar");
		AggregateProjection bestankar = Projections.sum("bestankar");
		ProjectionList projectionList = Projections.projectionList().add(bedehkar).add(bestankar);
		criteria.setProjection(projectionList);
		Object[] result = (Object[]) criteria.uniqueResult();
		Double[] ar = new Double[]{(Double) result[0], (Double) result[1]};
		return ar;
	}




}