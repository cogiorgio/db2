package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import exceptions.ReviewException;
import model.Questionnaire;
import model.Review;
import model.User;
import service.QuestionnaireService;
import service.ReviewService;
import service.UserService;

/**
 * Servlet implementation class GoToHomePage
 */
@WebServlet("/GoToHome")
public class GoToHomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "model/QuestionnaireService")
	private QuestionnaireService qstService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToHomePage() {
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
        Questionnaire q=null;
		try {
			q = qstService.findByDate(DateTime.now().toDate());
		} catch (QuestionnaireException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find the questionnaire of the day");
		}
		ReviewService revService=null;
		revService = (ReviewService) request.getSession().getAttribute("revService");
		User u=(User)request.getSession().getAttribute("user");
		Review r = null;
		if(q!=null) {
		try {
			r=revService.findByUserQuestionnaire(u.getId(), q.getId());
		} catch (ReviewException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find the questionnaire of the day");
			}
		}
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		String path = "/WEB-INF/Home.html";
		if(q!=null) {
		ctx.setVariable("questionnaire", q);
		if(r==null) { ctx.setVariable("off", "1");}	
		if(u.getBlocked()) { ctx.setVariable("off", "1");}


	
		ctx.setVariable("reviews",getSubmitted(q.getReviews()));

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
