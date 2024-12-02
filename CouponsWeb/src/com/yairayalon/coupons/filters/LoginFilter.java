package com.yairayalon.coupons.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yairayalon.coupons.utils.CookieUtils;

public class LoginFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;

		String url = req.getRequestURL().toString();

		// We let login and register requests pass without any condition
		if (url.endsWith("/login") || url.contains("/register")) {
			chain.doFilter(request, response);
			return;
		}

		Cookie sessionCookie = CookieUtils.getSessionCookie(req.getCookies());

		if (sessionCookie == null) {
			HttpServletResponse res = (HttpServletResponse) response;
			res.setStatus(401);
		}
		else {
			chain.doFilter(request, response);
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}