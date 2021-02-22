package adminControllers;

import java.io.IOException;


import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.DateTime;

import exceptions.QuestionnaireException;
import model.Questionnaire;
import service.QuestionService;
import service.QuestionnaireService;

/**
 * Servlet implementation class AddQuestion
 */
@WebServlet("/AddQuestion")
public class AddQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "service/QuestionService")
	private QuestionService qService;
	
	@EJB(name = "service/QuestionnaireService")
	private QuestionnaireService qstService;
	
	public AddQuestion() {
		super();
	}

	public void init() throws ServletException {
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("questionnaire") == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(loginpath);
			return;
		}

		// Get and parse all parameters from request

		String text = null;
		try {
			text = StringEscapeUtils.escapeJava(request.getParameter("text"));
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		// Create and add question in DB
		Questionnaire questionnaire = (Questionnaire) session.getAttribute("questionnaire");
		try {
			questionnaire = qstService.findByDate(DateTime.now().toDate());
		} catch (QuestionnaireException e1) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e1.getMessage());
		}
		try {
			qService.addQuestion(questionnaire, text);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}

		//return to the view to add other questions
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToAddQuestions";
		response.sendRedirect(path);
	}

	public void destroy() {
	}

}
