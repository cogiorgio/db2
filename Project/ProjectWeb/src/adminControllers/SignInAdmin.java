package adminControllers;

import java.io.IOException;

import javax.ejb.EJB;
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

import exceptions.CredentialsException;
import model.Admin;
import model.User;
import service.AdminService;
import service.UserService;

/**
 * Servlet implementation class SignInAdmin
 */
@WebServlet("/SignInAdmin")
public class SignInAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	@EJB(name = "model/AdminService")
	private AdminService admService;

	public SignInAdmin() {
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
		String mail = null;
		try {
			usrn = StringEscapeUtils.escapeJava(request.getParameter("username"));
			pwd = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			mail= StringEscapeUtils.escapeJava(request.getParameter("mail"));
			if (usrn == null || pwd == null || mail==null || usrn.isEmpty() || pwd.isEmpty() || mail.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
			return;
		}
		Admin admin = null;
		try {
			// create the admin in DB
			admin = admService.signIn(usrn, pwd, mail );
		} catch (CredentialsException | NonUniqueResultException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			return;
		}

		// If the user exists, add info to the session and go to home page, otherwise
		// show login page with error message
		String path;
		if (admin == null) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", "error");
			path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
		} else {
			request.getSession().setAttribute("admin", admin);
			path = getServletContext().getContextPath() + "/GoToAdminHome";
			response.sendRedirect(path);
		}

	}

	public void destroy() {
	}

}
