package ir.serajsamaneh.accounting.sanadhesabdariitemtemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.serajsamaneh.accounting.enumeration.HesabTypeEnum;
import ir.serajsamaneh.accounting.enumeration.SanadStateEnum;
import ir.serajsamaneh.accounting.enumeration.SanadFunctionEnum;
import ir.serajsamaneh.accounting.hesabkol.BaseHesabKolEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariEntity;
import ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariService;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.base.BaseHibernateDAO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.core.base.BaseEntityService;

public class SanadHesabdariItemTemplateService extends
		BaseEntityService<SanadHesabdariItemTemplateEntity, Long> {

	@Override
	protected SanadHesabdariItemTemplateDAO getMyDAO() {
		return sanadHesabdariItemTemplateDAO;
	}

	SanadHesabdariItemTemplateDAO sanadHesabdariItemTemplateDAO;

	public SanadHesabdariItemTemplateDAO getSanadHesabdariItemTemplateDAO() {
		return sanadHesabdariItemTemplateDAO;
	}

	public void setSanadHesabdariItemTemplateDAO(
			SanadHesabdariItemTemplateDAO sanadHesabdariItemTemplateDAO) {
		this.sanadHesabdariItemTemplateDAO = sanadHesabdariItemTemplateDAO;
	}

}