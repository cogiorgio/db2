package adminControllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.joda.time.DateTime;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import exceptions.QuestionnaireException;

import java.util.List;

import model.Questionnaire;
import model.Review;
import model.User;
import service.QuestionnaireService;

/**
 * Servlet implementation class GoToInspectionPage
 */
@WebServlet("/GoToInspection")
public class GoToInspection extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name = "service/QuestionnaireService")
	private QuestionnaireService qService;
	
	private Questionnaire q;

	
	public GoToInspection() {
		super();
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

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	private Date getToday() {
		Date today=new Date(System.currentTimeMillis());
		return today;
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
		Date date = null;

		try {
			/*String datetime= request.getParameter("date");
			DateTime time = DateTime.parse(datetime);
			date= time.toDate();*/
			String pattern="yyyy-MM-dd";
			SimpleDateFormat formatter= new SimpleDateFormat(pattern);
			date= formatter.parse(request.getParameter("date"));
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad format of the request");
		}
		if (date.after(getToday())) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The questionnaire is not of a past day");
			return;
		}
		
		//search the questionnaire
		try {
			q= qService.findByDate(date);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}
		if(q==null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "A questionnaire for that day does not exist");
			return;
		}
		
		//Get the list of users and the reviews
		List<User> userSubmitted=null;
		List<User> userCancelled=null;
		List<Review> reviews=null;
		try {
			userSubmitted = qService.findUserSubmitted(q);
			userCancelled= qService.findUserCancelled(q);
		} catch (QuestionnaireException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
		}
							
		String path = "/WEB-INF/Inspection.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("subUsers", userSubmitted);
		ctx.setVariable("cancUsers", userCancelled);
		ctx.setVariable("reviews",getSubmitted(q.getReviews()));

		templateEngine.process(path, ctx, response.getWriter());

	}
	

	public void destroy() {
	}

}
