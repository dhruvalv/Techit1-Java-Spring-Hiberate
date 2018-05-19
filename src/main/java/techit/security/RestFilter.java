package techit.security;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;

public class RestFilter extends GenericFilterBean {

	@Autowired
	UserDao userDao;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		String path = httpRequest.getRequestURI();
		// only login url will not be Authorized/Authenticated
		if (path.endsWith("/api/login")) {
			chain.doFilter(request, response);
		} else {

			HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(httpRequest) {
				@Override
				public String getHeader(String name) {
					final String value = request.getParameter(name);
					if (value != null) {
						return value;
					}
					return super.getHeader(name);
				}
			};

			String authorization = wrapper.getHeader("Authorization");
			// if jwt token is not present in the request header then throw an error.
			if (null == authorization) {
				throw new RestException(501, "Missing Token");
			}
			if (!authorization.startsWith("Bearer ")) {
				throw new RestException(501, "Invalid Token Exception");
			}

			String key = authorization.substring(7);
			try {
				Map user = (Map) Jwts.parser().setSigningKey("dhruval").parseClaimsJws(key).getBody().get("user");
				// if signature/jwt token is broken/invalid then raise and error.
				if (user == null || user.isEmpty()) {
					throw new RestException(501, "Invalid Token Exception");
				}
				request.setAttribute("user", user);
			} catch (SignatureException e) {
				throw new RestException(501, "Invalid Token' Signature Exception");
			}
			chain.doFilter(request, response);
		}
	}

}
