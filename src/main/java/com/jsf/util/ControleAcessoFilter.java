package com.jsf.util;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import com.jsf.controller.SessaoController;

@WebFilter(urlPatterns = "/pagina/*")
public class ControleAcessoFilter implements Filter {


	@Inject
	protected SessaoController sessao;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (sessao.getUsuario().isLogado() == false) {
			HttpServletResponse resp = (HttpServletResponse) response;
			resp.sendRedirect("../");
		} else {			
			chain.doFilter(request, response);
		}

	}

	@Override
	public void init(FilterConfig arg) throws ServletException {

	}


}
