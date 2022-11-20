package ir.serajsamaneh.accounting.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ir.serajsamaneh.accounting.enumeration.MahyatGroupEnum;
import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabKolService;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenService;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliService;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliEntity;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.accounting.sanadhesabdari.SanadHesabdariEntity;
import ir.serajsamaneh.accounting.sanadhesabdariitem.SanadHesabdariItemEntity;
import ir.serajsamaneh.accounting.sanadtype.SanadTypeService;
import ir.serajsamaneh.core.base.BaseEntity;
import ir.serajsamaneh.core.base.BaseEntityService;
import ir.serajsamaneh.core.common.OrganVO;
import ir.serajsamaneh.core.exception.FatalException;
import ir.serajsamaneh.core.exception.NoOrganFoundException;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.organ.OrganService;
import ir.serajsamaneh.core.user.UserService;
import ir.serajsamaneh.core.util.SerajMessageUtil;
import ir.serajsamaneh.enumeration.SaalMaaliStatusEnum;
import ir.serajsamaneh.enumeration.YesNoEnum;

public abstract class BaseAccountingService<T extends BaseEntity<U>, U extends Serializable>
		extends BaseEntityService<T, U> {

	@Autowired
	protected UserService userService;
	@Autowired
	protected OrganService organService;
	@Autowired
	protected SaalMaaliService saalMaaliService;
	@Autowired
	protected SanadTypeService sanadTypeService;
	@Autowired
	protected HesabKolService hesabKolService;
	@Autowired
	protected HesabMoeenService hesabMoeenService;
	@Autowired
	protected HesabTafsiliService hesabTafsiliService;

	public boolean checkIfMustValidateTafsiliOneAndTwoAreRelated(Long organId) {
		return false;
	}

	public Boolean checkIfMustValidateHesabTafsiliHasChild(OrganVO organ) {
		String validateHesabTafsiliHasChild = systemConfigService.getValue(organ.getId(), null,
				"validateHesabTafsiliHasChild");
		if (validateHesabTafsiliHasChild == null)
			throw new FatalException(
					SerajMessageUtil.getMessage("Accounting_system_config_is_not_compelete", organ.getDesc()));
		return YesNoEnum.getName(Integer.valueOf(validateHesabTafsiliHasChild)).equals(YesNoEnum.YES);
	}

	public Boolean checkIfMustValidateHesabMoeenHasChild(OrganVO organ) {
		String validateHesabMoeenHasChild = systemConfigService.getValue(organ.getId(), null,
				"validateHesabMoeenHasChild");
		if (validateHesabMoeenHasChild == null)
			throw new FatalException(SerajMessageUtil.getMessage("Accounting_system_config_is_not_compelete", organ));
		return YesNoEnum.getName(Integer.valueOf(validateHesabMoeenHasChild)).equals(YesNoEnum.YES);
	}

	public Boolean checkIfMustValidateHesabMoeenHasMarkaz(OrganVO organ) {
		String validateHesabMoeenHasMarkaz = systemConfigService.getValue(organ.getId(), null,
				"validateHesabMoeenHasMarkaz");
		if (validateHesabMoeenHasMarkaz == null)
			throw new FatalException("validateHesabMoeenHasMarkaz is null");
		return YesNoEnum.getName(Integer.valueOf(validateHesabMoeenHasMarkaz)).equals(YesNoEnum.YES);
	}

	public SaalMaaliEntity getCurrentUserActiveSaalMaali(Long organId, Long userId) {
		if (organId == null)
			throw new NoOrganFoundException("");
		OrganEntity organEntity = organService.load(organId);
		OrganVO organVO = organService.getOrganVO(organEntity);
		SaalMaaliEntity currentUserSaalMaaliEntity = saalMaaliService.getUserActiveSaalMaali(organVO,
				/* getTopOrgan(organEntity), */ userId);

		return currentUserSaalMaaliEntity;
	}

	public SaalMaaliEntity getCurrentUserActiveSaalMaali(OrganVO organVO, Long userId) {
		if (organVO == null)
			throw new NoOrganFoundException("");
		SaalMaaliEntity currentUserSaalMaaliEntity = saalMaaliService.getUserActiveSaalMaali(organVO,
				/* getTopOrgan(organEntity), */ userId);

		return currentUserSaalMaaliEntity;
	}

	public void checkSaalMaaliIsInProgress(SaalMaaliEntity saalmaali, SanadHesabdariEntity sanadHesabdariEntity) {
		if (!(saalmaali.getStatus().equals(SaalMaaliStatusEnum.InProgress)
				|| saalmaali.getStatus().equals(SaalMaaliStatusEnum.TemporalAccountsClosed)))
			throw new FatalException(SerajMessageUtil.getMessage("SaalMaali_operationNotAllowed", saalmaali.getSaal()));

		if (saalmaali.getStatus().equals(SaalMaaliStatusEnum.TemporalAccountsClosed)) {
			List<SanadHesabdariItemEntity> sanadHesabdariItems = sanadHesabdariEntity.getSanadHesabdariItem();
			for (SanadHesabdariItemEntity sanadHesabdariItemEntity : sanadHesabdariItems) {
				HesabKolEntity hesabKol = sanadHesabdariItemEntity.getHesabKol();
				if (!hesabKol.getHesabGroup().getMahyatGroup().equals(MahyatGroupEnum.TARAZNAMEH))
					throw new FatalException(SerajMessageUtil.getMessage(
							"SanadHesabdari_cantDoOperationOnArticleThatArentOfTypeTARAZNAMEH",
							sanadHesabdariItemEntity));
			}
		}
	}

}
