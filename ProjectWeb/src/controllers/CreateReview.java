package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import exceptions.QuestionnaireException;
import model.Question;
import model.Questionnaire;
import model.Review;
import model.User;
import service.AnswerService;
import service.QuestionnaireService;
import service.ReviewService;

/**
 * Servlet implementation class GoToStatistical
 */
@WebServlet("/CreateReview")
public class CreateReview extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "model/QuestionnaireService")
	private QuestionnaireService qstService;
       
	@EJB(name = "model/AnswerService")
	private AnswerService answerService;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateReview() {
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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Questionnaire q=null;
		try {
			q = qstService.findByDate(DateTime.now().toDate());
		} catch (QuestionnaireException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find the questionnaire of the day");
		}
		ReviewService revService=null;
		revService = (ReviewService) request.getSession().getAttribute("revService");
		revService.setAge(Integer.parseInt(request.getParameter("age")));
		revService.setSex(request.getParameter("sex"));
		revService.setExpertise(request.getParameter("level"));
		Review r=revService.createReview(q,(User) request.getSession().getAttribute("user"));
		for (Map.Entry me : revService.getAnswers().entrySet()) {
			if((String) me.getValue()!="")
			answerService.createAnswer((String)me.getKey(), r,(String) me.getValue());
	        }
		
		try {
			q = qstService.findByDate(DateTime.now().toDate());
		} catch (QuestionnaireException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find the questionnaire of the day");
		}
	
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		String path = "/WEB-INF/Thanks.html";
		if(q!=null) {
			List<Review> reviews=null;
			try {
				reviews = qstService.findSubmitted(q);
			} catch (QuestionnaireException e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
			}
			ctx.setVariable("questionnaire", q);
			ctx.setVariable("reviews",reviews);
		}

		templateEngine.process(path, ctx, response.getWriter());
		
		
	}

}