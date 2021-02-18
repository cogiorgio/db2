package controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


import service.QuestionnaireService;

/**
 * Servlet implementation class AddQuestion
 */
@WebServlet("/AddQuestion")
public class AddQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "service/QuestionnaireService")
	private QuestionnaireService qService;

	public AddQuestion() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Integer qId = null;
		try {
			qId = Integer.parseInt(request.getParameter("qId"));
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Questionnaire parameters");
			return;
		}

		String text = request.getParameter("text");

		if (text == null | text.isEmpty() ) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid photo parameters");
			return;
		}

		qService.addQuestion(qId, text);
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToHomePage";
		if (qId != null)
			path = path + "?qId=" + qId;
		response.sendRedirect(path);
	}

}
