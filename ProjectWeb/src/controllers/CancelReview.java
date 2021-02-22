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
@WebServlet("/CancelReview")
public class CancelReview extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "model/QuestionnaireService")
	private QuestionnaireService qstService;
       
	@EJB(name = "model/AnswerService")
	private AnswerService answerService;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CancelReview() {
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

    public List<Review> getSubmitted(List<Review> reviews){
    	List<Review> submitted= new ArrayList<Review>();
    	
    	for(Review r: reviews) {
    		if(r.getStatus().contains("submitted")) {
    			submitted.add(r);
    		}
    	}
    	return submitted;
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
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}

		ReviewService revService=null;
		revService = (ReviewService) request.getSession().getAttribute("revService");
		
		Review r=revService.cancelReview(q,(User) request.getSession().getAttribute("user"));
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		String path = "/WEB-INF/Home.html";
		
		if(q!=null) {
			ctx.setVariable("questionnaire", q);
			ctx.setVariable("reviews",getSubmitted(q.getReviews()));
		}

		templateEngine.process(path, ctx, response.getWriter());	
		
	}

}