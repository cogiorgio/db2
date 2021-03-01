package adminControllers;

import java.io.IOException;

import java.io.InputStream;
import java.util.Date;

import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;

import utils.ImageUtils;
import model.Questionnaire;
import service.QuestionnaireService;

/**
 * Servlet implementation class CreateQuestionnaire
 */
@WebServlet("/CreateQuestionnaire")
@MultipartConfig
public class CreateQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private TemplateEngine templateEngine;
	
	private Questionnaire created=null;

	@EJB(name = "service/QuestionnaireService")
	private QuestionnaireService qService;

	
	public CreateQuestionnaire() {
		super();
	}

	public void init() throws ServletException {
	}

	private Date getYesterday() {
		return new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("admin") == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(loginpath);
			return;
		}

		// Get and parse all parameters from request and check the date
		boolean illegalDate = false;
		String product = null;		
		Date date = null;

		try {
			product = StringEscapeUtils.escapeJava(request.getParameter("product"));	
			if(product.isEmpty() | product==null) {
				throw new Exception ("Missing or empty product");
			}
			
			String pattern="yyyy-MM-dd";
			SimpleDateFormat formatter= new SimpleDateFormat(pattern);
			date= formatter.parse(request.getParameter("date"));
			illegalDate =  date.before(getYesterday());

		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad format of the reqeust");
		}
		if (illegalDate) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not possible to create a questionnaire for a past date");
			return;
		}
		
		//handle the image
		Part imgFile = request.getPart("img");
		InputStream thumbContent = imgFile.getInputStream();
		byte[] imgByteArray = ImageUtils.readImage(thumbContent);

		// Create questionnaire in DB
		try {
			created= qService.createQuestionnaire(product, date, imgByteArray);
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
