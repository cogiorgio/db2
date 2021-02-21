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

import exceptions.BlacklistException;
import exceptions.QuestionnaireException;
import model.Questionnaire;
import model.User;
import service.BlacklistService;
import service.QuestionnaireService;
import service.ReviewService;

/**
 * Servlet implementation class GoToStatistical
 */
@WebServlet("/GoToStatistical")
public class GoToStatistical extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "model/BlacklistService")
	private BlacklistService bService;
	
	@EJB(name = "model/QuestionnaireService")
	private QuestionnaireService qstService;
	private TemplateEngine templateEngine;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToStatistical() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ReviewService revService=null;
		revService = (ReviewService) request.getSession().getAttribute("revService");
		User user=(User) request.getSession().getAttribute("user");
		List<String> parameterNames = new ArrayList<String>(request.getParameterMap().keySet());
		for(int i=0;i<parameterNames.size();i++) {
			
			String key=parameterNames.get(i);
			
			String temp = request.getParameter(key) ;
			try {
				bService.checkBlacklist(temp, user);
			} catch (BlacklistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				String path = "/WEB-INF/Home.html";
				Questionnaire q=null;
				try {
					q = qstService.findByDate(DateTime.now().toDate());
				} catch (QuestionnaireException c) {
					// TODO Auto-generated catch block
					c.printStackTrace();
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Could not find the questionnaire of the day");
				}
				if(q!=null) {
				ctx.setVariable("questionnaire", q);
				ctx.setVariable("reviews",q.getReviews());
				templateEngine.process(path, ctx, response.getWriter());
				return;
			}
			
			
			
		}
			revService.addAnswer(key, request.getParameter(key));
		}
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		String path = "/WEB-INF/Statistical.html";
		ctx.setVariable("sex", revService.getSex());
		System.out.println(revService.getSex());
		ctx.setVariable("age", revService.getAge());
		ctx.setVariable("level", revService.getExpertise());
		templateEngine.process(path, ctx, response.getWriter());
		
	}

}
