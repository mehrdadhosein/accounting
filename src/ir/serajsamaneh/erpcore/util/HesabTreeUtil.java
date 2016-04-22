package ir.serajsamaneh.erpcore.util;

import ir.serajsamaneh.accounting.hesabkol.HesabKolEntity;
import ir.serajsamaneh.accounting.hesabkol.HesabVO;
import ir.serajsamaneh.accounting.hesabmoeen.HesabMoeenEntity;
import ir.serajsamaneh.accounting.hesabtafsili.HesabTafsiliEntity;
import ir.serajsamaneh.accounting.moeentafsili.MoeenTafsiliEntity;

import java.util.List;
import java.util.Set;

public class HesabTreeUtil {





	public static HesabVO addHesabKolToHesabVOs(HesabKolEntity hesabKolEntity, List<HesabVO> hesabVOs) {
		for (HesabVO hesabVO : hesabVOs) {
			if(hesabVO.getHesabType().equals(HesabKolEntity.class.getSimpleName()) && new Long(hesabVO.getEntityId()).equals(hesabKolEntity.getId()))
				return hesabVO;
		}
		HesabVO hesabKolVO = new HesabVO(hesabKolEntity, HesabKolEntity.class.getSimpleName(), "folder_vector.png");
		hesabVOs.add(hesabKolVO);
		return hesabKolVO;
	}


	public static void addHesabMoeensToHesabHierarchy(HesabVO hesabKolVO,
			List<HesabMoeenEntity> activeMoeens, List<HesabVO> hesabVOs) {
		for (HesabMoeenEntity hesabMoeenEntity : activeMoeens) {
			addHesabMoeenToHesabHierarchy(hesabKolVO, hesabMoeenEntity, hesabVOs, true);
		}
	}



	public static HesabVO addHesabMoeenToHesabHierarchy(HesabVO hesabKolVO, HesabMoeenEntity hesabMoeenEntity, List<HesabVO> hesabVOs, boolean addMoeenTafsilies) {

		HesabVO localHesabMoeenVO = null;
		Boolean found = false;
		for (HesabVO hkVO : hesabVOs) {
			List<HesabVO> hesabMoeenVOs = hkVO.getChilds();
			for (HesabVO hesabMoeenVO : hesabMoeenVOs) {
				if(hesabMoeenVO.getHesabType().equals(HesabMoeenEntity.class.getSimpleName()) && new Long(hesabMoeenVO.getEntityId()).equals(hesabMoeenEntity.getId())){
					localHesabMoeenVO = hesabMoeenVO;
					found = true;	
					break;
				}
			}
		}
		
		if(!found){
			localHesabMoeenVO = new HesabVO(hesabMoeenEntity, HesabMoeenEntity.class.getSimpleName(), "folder-vectors-icon.png");
			localHesabMoeenVO.setParent(hesabKolVO);
			hesabKolVO.getChilds().add(localHesabMoeenVO);
		}
		
		if(addMoeenTafsilies){
			Set<MoeenTafsiliEntity> moeenTafsili = hesabMoeenEntity.getMoeenTafsili();
			for (MoeenTafsiliEntity moeenTafsiliEntity : moeenTafsili) {
				HesabTafsiliEntity hesabTafsili = moeenTafsiliEntity.getHesabTafsili();
				addHesabTafsilisToHesabHierarchy(localHesabMoeenVO, hesabTafsili, "folder-documents-icon.png", hesabVOs, true);
			}
		}
		
		return localHesabMoeenVO;
	}

	public static void addHesabTafsilisToHesabHierarchy(HesabVO hesabMoeenVO,
			HesabTafsiliEntity hesabTafsili, String icon, List<HesabVO> hesabVOs, boolean addTafsiliShenavars) {
		if(hesabTafsili.getHidden() == true)
			return;

		HesabVO tafsiliVO = null;
		Boolean found = false;

		for (HesabVO hkVO : hesabVOs) {
			List<HesabVO> hesabMoeenVOs = hkVO.getChilds();
			for (HesabVO hmVO : hesabMoeenVOs) {
				List<HesabVO> htVOs = hmVO.getChilds();
				for (HesabVO htVO : htVOs) {
					if(htVO.getHesabType().equals(HesabTafsiliEntity.class.getSimpleName()) && new Long(htVO.getEntityId()).equals(hesabTafsili.getId())
							&& htVO.getParent().getHesabType().equals(HesabMoeenEntity.class.getSimpleName()) && htVO.getParent().getEntityId().equals(hesabMoeenVO.getEntityId())){
						tafsiliVO = htVO;
						found = true;
						break;
					}
				}
			}
		}
		
		if(!found){
			tafsiliVO = new HesabVO(hesabTafsili, HesabTafsiliEntity.class.getSimpleName(), icon);
			tafsiliVO.setParent(hesabMoeenVO);
			hesabMoeenVO.getChilds().add(tafsiliVO);
		}
		
		if(addTafsiliShenavars){
			Set<HesabTafsiliEntity> tafsiliChilds = hesabTafsili.getChilds();
			for (HesabTafsiliEntity childTafsiliEntity : tafsiliChilds) {
				addHesabTafsilisShenavarToHesabHierarchy(tafsiliVO, childTafsiliEntity, "1365270554_stock_group.png", hesabVOs);
			}
		}
	}

	public static void addHesabTafsilisToHesabHierarchy(List<HesabVO> hesabVOs,
			HesabTafsiliEntity hesabTafsiliEntity, boolean addMoeenTafsilies, boolean addTafsiliShenavars) {
		Set<MoeenTafsiliEntity> moeenTafsili = hesabTafsiliEntity.getMoeenTafsili();
		for (MoeenTafsiliEntity moeenTafsiliEntity : moeenTafsili) {
			HesabMoeenEntity hesabMoeenEntity = moeenTafsiliEntity.getHesabMoeen();
			HesabKolEntity hesabKolEntity = hesabMoeenEntity.getHesabKol();
			
			HesabVO hesabKolVO = addHesabKolToHesabVOs(hesabKolEntity, hesabVOs);

			HesabVO hesabMoeenVO = addHesabMoeenToHesabHierarchy(hesabKolVO, hesabMoeenEntity, hesabVOs, addMoeenTafsilies);
			
			addHesabTafsilisToHesabHierarchy(hesabMoeenVO, hesabTafsiliEntity, "folder-documents-icon.png", hesabVOs, addTafsiliShenavars);
		}
	}

	public static void addHesabTafsilisShenavarToHesabHierarchy(HesabVO HesabTafsiliVO,
			HesabTafsiliEntity hesabTafsiliShenavar, String icon, List<HesabVO> hesabVOs) {
		if(hesabTafsiliShenavar.getHidden() == true)
			return;

		HesabVO tafsiliShenavarVO = null;
		Boolean found = false;

		for (HesabVO hkVO : hesabVOs) {
			List<HesabVO> hesabMoeenVOs = hkVO.getChilds();
			for (HesabVO hmVO : hesabMoeenVOs) {
				List<HesabVO> htVOs = hmVO.getChilds();
				for (HesabVO htVO : htVOs) {
					List<HesabVO> htsVOs = htVO.getChilds();
					for (HesabVO hesabVO : htsVOs) {
						if(hesabVO.getHesabType().equals(HesabTafsiliEntity.class.getSimpleName()+"Shenavar") && new Long(hesabVO.getEntityId()).equals(hesabTafsiliShenavar.getId())
								&& hesabVO.getParent().getHesabType().equals(HesabTafsiliEntity.class.getSimpleName()) && hesabVO.getParent().getEntityId().equals(HesabTafsiliVO.getEntityId())){
							tafsiliShenavarVO = hesabVO;
							found = true;	
							break;
						}
					}
				}
			}
		}
		

		
		if(!found){
			tafsiliShenavarVO = new HesabVO(hesabTafsiliShenavar, HesabTafsiliEntity.class.getSimpleName()+"Shenavar", icon);
			tafsiliShenavarVO.setParent(HesabTafsiliVO);
			HesabTafsiliVO.getChilds().add(tafsiliShenavarVO);
		}
		
		Set<HesabTafsiliEntity> tafsiliChilds = hesabTafsiliShenavar.getChilds();
		for (HesabTafsiliEntity childTafsiliEntity : tafsiliChilds) {
			addHesabTafsilisShenavarToHesabHierarchy(tafsiliShenavarVO, childTafsiliEntity, "1365270554_stock_group.png", hesabVOs);
		}
	}
}
