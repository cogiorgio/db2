package adminControllers;

import java.io.IOException;

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
import service.QuestionnaireService;
import service.UserService;

/**
 * Servlet implementation class GoToDeletePage
 */
@WebServlet("/GoToDeletePage")
public class GoToDeletePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private TemplateEngine templateEngine;
	@EJB(name = "service/UserService")
	private UserService usrService; 
	
	@EJB(name = "service/QuestionnaireService")
	private QuestionnaireService qService; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToDeletePage() {
        super();
        // TODO Auto-generated constructor stub
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
		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("admin") == null) {
			response.sendRedirect(loginpath);
			return;
		}
		
		List<Questionnaire> questionnaires = null;
		
		try {			
			questionnaires = qService.findAllQuestionnaire();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to get data");
			return;
		}
		// Redirect to the Home page and add missions to the parameters
		//così vedo la tabella con tutti i questionnaire, un altro modo più sensato potrebbe essere inserire il prodotto e/o la data/id
		String path = "/WEB-INF/Delete.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("questionnaires", questionnaires);
		
		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}


}
