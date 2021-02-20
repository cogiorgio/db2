package adminControllers;

import java.io.IOException;




import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.DateTime;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import model.Questionnaire;
import service.QuestionnaireService;

/**
 * Servlet implementation class CreateQuestionnaire
 */
@WebServlet("/CreateQuestionnaire")

public class CreateQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	private Questionnaire created=null;

	@EJB(name = "service/QuestionnaireService")
	private QuestionnaireService qService;

	
	public CreateQuestionnaire() {
		super();
	}

	public void init() throws ServletException {
	}

	private Date getToday() {
		Date today=new Date(System.currentTimeMillis());
		return today;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(loginpath);
			return;
		}

		// Get and parse all parameters from request
		boolean illegalDate = false;
		String product = null;		
		Date date = null;

		try {
			product = StringEscapeUtils.escapeJava(request.getParameter("product"));
			
			if(product.isEmpty() | product==null) {
				throw new Exception ("Missing or empty product");
			}

			String datetime= request.getParameter("date");
			DateTime time = DateTime.parse(datetime);

			date= time.toDate();
			illegalDate =  date.before(getToday());

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad format of the reqeust");
		}
		if (illegalDate) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not possible to create a questionnaire for a past date");
			return;
		}

		// Create questionnaire in DB
		try {
			created= qService.createQuestionnaire(product, date);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}
		
		request.getSession().setAttribute("questionnaire", created);
		String path = getServletContext().getContextPath() + "/GoToAddQuestions";
		response.sendRedirect(path);

	}
	

	public void destroy() {
	}

}
