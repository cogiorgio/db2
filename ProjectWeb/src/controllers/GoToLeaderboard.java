package controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import model.Questionnaire;
import model.User;
import service.QuestionnaireService;

/**
 * Servlet implementation class GoToLeaderboard
 */
@WebServlet("/GoToLeaderboard")
public class GoToLeaderboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "service/QuestionnaireService")
	private QuestionnaireService qService;
	
	private Questionnaire q;

	
	public GoToLeaderboard() {
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


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(loginpath);
			return;
		}

		// Get and parse all parameters from request	
		Date date = null;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = (Date) sdf.parse(request.getParameter("date"));
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad format of the reqeust");
		}

		try {
			System.out.println(date);
			q= qService.findByDate(date);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Questionnaire not found");
			return;
		}
		if(q==null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "A questionnaire for that day does not exist");
			return;
		}
		
		List<User> userSubmitted= qService.findUserSubmitted(q);
	
							
		String path = "/WEB-INF/Leaderboard.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("subUsers", userSubmitted);
		
		templateEngine.process(path, ctx, response.getWriter());


	}
	

	public void destroy() {
	}

}
