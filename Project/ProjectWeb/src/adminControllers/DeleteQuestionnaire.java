package adminControllers;

import java.io.IOException;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import service.QuestionnaireService;

/**
 * Servlet implementation class DeleteQuestionnaire
 */
@WebServlet("/DeleteQuestionnaire")
public class DeleteQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "service/QuestionnaireService")
	private QuestionnaireService qService;
	
	public DeleteQuestionnaire() {
		super();
	}

	public void init() throws ServletException {
	}
	
	private Date getToday() {
		Date today=new Date(System.currentTimeMillis());
		return today;
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

		// get and check parameters
		Integer questionnaireId = null;
		try {
			questionnaireId = Integer.parseInt(request.getParameter("questionnaireid"));
		} catch (NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect parameter values");
			return;
		}

		// Delete the questionnaire in DB
		try {
			qService.deleteQuestionnaire(questionnaireId, getToday());
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			return;
		}

		// Return view of the Home
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToAdminHome";
		response.sendRedirect(path);

	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}
}
