package adminControllers;

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

import model.Answer;
import model.Questionnaire;
import model.Review;
import model.User;
import service.QuestionnaireService;
import service.ReviewService;
import service.UserService;

/**
 * Servlet implementation class GoToUserAnswers
 */
@WebServlet("/GoToUserAnswers")
public class GoToUserAnswers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "service/ReviewService")
	private ReviewService rService;
	
	
	public GoToUserAnswers() {
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
		if (session.isNew() || session.getAttribute("admin") == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(loginpath);
			return;
		}

		// Get and parse all parameters from request	
		Integer userId = null;
		Integer qId= null;
		Review r=null;

		try {
			userId = Integer.parseInt(request.getParameter("userid"));
			qId = (Integer) session.getAttribute("qid");
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad format of the reqeust");
		}


		try {
			r= rService.findByUserQuestionnaire(userId, qId);
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "bad format");
			return;
		}
		if(r==null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "review nulla");
			return;
		}
		
		List<Answer> answers= r.getAnswers();
				
							
		String path = "/WEB-INF/InspectionUser.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("answers", answers);
		ctx.setVariable("r", r);
		templateEngine.process(path, ctx, response.getWriter());


	}
	

	public void destroy() {
	}


}
