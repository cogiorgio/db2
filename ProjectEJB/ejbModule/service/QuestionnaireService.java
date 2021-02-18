package service;

import java.sql.Date;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Questionnaire;

/**
 * Session Bean implementation class QuestionnaireService
 */
@Stateless
@LocalBean
public class QuestionnaireService {
	@PersistenceContext(unitName = "projectEJB")
	private EntityManager em;
    public QuestionnaireService() {
        // TODO Auto-generated constructor stub
    }
    
	public int createQuestionnaire(String product, Date date, String question) {
		
		Questionnaire q= new Questionnaire (product, date);
		em.persist(q);
		em.flush();
		return q.getId();
	}


	public void deleteQuestionnaire(int qId) {
		Questionnaire q = em.find(Questionnaire.class, qId);
		if (q == null)
			return;
		em.remove(q);
	}

}
