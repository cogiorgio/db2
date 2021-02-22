package controllers;

import java.io.IOException;
import java.util.Calendar;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.persistence.NonUniqueResultException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import exceptions.BlacklistException;
import exceptions.CredentialsException;
import model.User;
import service.UserService;
import model.Blacklist;
import service.BlacklistService;
import service.ReviewService;

/**
 * Servlet implementation class CheckLogin
 */
@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {	
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	@EJB(name = "model/UserService")
	private UserService usrService;

	public CheckLogin() {
		super();
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String usrn = null;
		String pwd = null;
		try {
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			if (usrn == null || pwd == null || usrn.isEmpty() || pwd.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
			return;
		}
		User user = null;
		try {
			// query db to authenticate for user
			user = usrService.checkCredentials(usrn, pwd);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message

		String path;
		if (user == null) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "Incorrect username or password");
			path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
		} else 
		{
			ReviewService revService=null;
			try {
				/*
				 * We need one distinct EJB for each user. Get the Initial Context for the JNDI
				 * lookup for a local EJB. Note that the path may be different in different EJB
				 * environments. In IntelliJ use: ic.lookup(
				 * "java:/openejb/local/ArtifactFileNameWeb/ArtifactNameWeb/QueryServiceLocalBean"
				 * );
				 */
				InitialContext ic = new InitialContext();
				// Retrieve the EJB using JNDI lookup
				revService = (ReviewService) ic.lookup("java:/openejb/local/ReviewServiceLocalBean");
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());

			}
			request.getSession().setAttribute("revService", revService);
			request.getSession().setAttribute("user", user);
			path = getServletContext().getContextPath() + "/GoToHome";
			response.sendRedirect(path); 

		}
		
		

	}

	public void destroy() {
	}


}
