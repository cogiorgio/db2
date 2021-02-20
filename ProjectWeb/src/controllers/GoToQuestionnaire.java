package controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import model.Questionnaire;
import service.QuestionnaireService;

/**
 * Servlet implementation class GoToQuestionnaire
 */
@WebServlet("/GoToQuestionnaire")
public class GoToQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "model/QuestionnaireService")
	private QuestionnaireService qstService;
	
	private TemplateEngine templateEngine;
	
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToQuestionnaire() {
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		if(request.getParameterMap().containsKey("productId")) {
			System.out.println("arrivo");
			Questionnaire q= qstService.getQuestionnaireById(Integer.parseInt(request.getParameter("productId")));
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			String path = "/WEB-INF/Questions.html";
			ctx.setVariable("questionnaire", q);
			ctx.setVariable("answers", null);

			templateEngine.process(path, ctx, response.getWriter());
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
