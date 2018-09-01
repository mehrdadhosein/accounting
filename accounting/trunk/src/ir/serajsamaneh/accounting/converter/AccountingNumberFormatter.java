package ir.serajsamaneh.accounting.converter;

import java.text.DecimalFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.util.StringUtils;

import ir.serajsamaneh.core.util.NumberUtil;

public class AccountingNumberFormatter implements Converter {


	public Object getAsObject(FacesContext context, UIComponent comp, String value) throws ConverterException {
    	//Class enumType = null;
    	if(!StringUtils.hasText(value)|| value==null)
    		return null;
    	value=value.replaceAll(",","");
    	return new Double(value);
    	//return Enum.valueOf(enumType,value);
    }
	public String getAsString(FacesContext context, UIComponent comp, Object object) throws ConverterException {
 
		if (object!=null){
//			if(object.toString().equals("0") || object.toString().equals("0.0"))
//				return "";
//			DecimalFormat df = new DecimalFormat("###,###");
//		    String Formated = df.format(object);
//			return  Formated;
			
			return NumberUtil.getBigDecimalFormatted((Double)object);
			 
		}
		return null;
    }

}
