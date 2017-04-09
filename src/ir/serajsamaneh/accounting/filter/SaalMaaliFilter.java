package ir.serajsamaneh.accounting.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ir.serajsamaneh.accounting.exception.NoActiveSaalMaaliFoundException;
import ir.serajsamaneh.accounting.exception.NoSaalMaaliFoundException;
import ir.serajsamaneh.accounting.saalmaali.SaalMaaliService;
import ir.serajsamaneh.core.organ.OrganEntity;
import ir.serajsamaneh.core.security.SecurityUtil;
import ir.serajsamaneh.core.systemconfig.SystemConfigService;
import ir.serajsamaneh.core.user.UserEntity;
import ir.serajsamaneh.core.util.SpringUtils;

public class SaalMaaliFilter implements Filter {

	public SaalMaaliService saalMaaliService; 
	SystemConfigService systemConfigService;
	
	public SystemConfigService getSystemConfigService() {
		if(systemConfigService == null)
			systemConfigService = SpringUtils.getBean("systemConfigService");
		return systemConfigService;
	}

	public SaalMaaliService getSaalMaaliService() {
		if(saalMaaliService == null)
			saalMaaliService = SpringUtils.getBean("saalMaaliService");
		return saalMaaliService;
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		if(SecurityUtil.isLogin() && !SecurityUtil.getCurrentUser().equals("system")){
			HttpServletRequest hrequest = (HttpServletRequest) request;
			try{
				if(hrequest.getRequestURI().indexOf("hesabKol.hierarchicalList") != -1
						|| hrequest.getRequestURI().indexOf("hesabMoeen.hierarchicalList") != -1){ 
//						hrequest.getRequestURI().indexOf("/login.jsf") == -1 
//						&& hrequest.getRequestURI().indexOf("/rfRes/") == -1
//						&& hrequest.getRequestURI().indexOf("/javax.faces.resource/") == -1
//						&& hrequest.getRequestURI().indexOf("/logout.jsf") == -1
//						&& hrequest.getRequestURI().indexOf("/license/licenseExpired.view.jsf") == -1
//						&& hrequest.getRequestURI().indexOf("saalMaali.message.jsf") == -1
//					&& hrequest.getRequestURI().indexOf("saalmaali") == -1){
////					getSaalMaaliService().getCurrentSaalmaali();
//					//to check if there is any saalMaali
					OrganEntity currentOrgan = SecurityUtil.getUserDetails().getOrganEntity();
					UserEntity userEntity = SecurityUtil.getUserDetails().getUserEntity();
////					String isAccountingEnabled = getSystemConfigService().getValue(currentOrgan, null, "isAccountingEnabled");
////					if(StringUtil.hasText(isAccountingEnabled) && isAccountingEnabled.equals("YES")){
						getSaalMaaliService().getUserActiveSaalMaali(currentOrgan,/*null,*/userEntity);
////					}
				}
			}catch (NoSaalMaaliFoundException e) {
				HttpServletResponse hresponse = (HttpServletResponse) response;
				hresponse.sendRedirect(hrequest.getContextPath()+ "/saalmaali/saalMaali.message.jsf");
				return;
			}catch (NoActiveSaalMaaliFoundException e) {
				HttpServletResponse hresponse = (HttpServletResponse) response;
				hresponse.sendRedirect(hrequest.getContextPath()+ "/saalmaali/saalMaali.message.jsf");
				return;
			}catch (Exception e) {
				Throwable cause = e.getCause();
				while (cause != null) {
					System.out.println(cause.getMessage());
					cause = cause.getCause();
				}
				
				HttpServletResponse hresponse = (HttpServletResponse) response;
				hresponse.sendRedirect(hrequest.getContextPath()+ "/access/error.jsf");
				return;
			}
		}
		filterChain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
