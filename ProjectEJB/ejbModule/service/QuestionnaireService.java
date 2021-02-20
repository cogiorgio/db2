package service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import exceptions.QuestionnaireException;
import model.Answer;
import model.Questionnaire;
import model.Review;
import model.User;

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
    
	public Questionnaire createQuestionnaire(String product, Date date) throws QuestionnaireException {
		Questionnaire q= findByDate(date);
			
		if(q==null) {
			q= new Questionnaire (product, date);
			em.persist(q);
			em.flush();
			return q;
		}
		else throw new QuestionnaireException("A questionnaire already exists for the selected date");
	}

	public void deleteQuestionnaire(Integer qId, Date today) throws QuestionnaireException {
		Questionnaire q = em.find(Questionnaire.class, qId);
		if (q == null)
			return;		
		if(q.getDate().before(today)) {	
			em.remove(q);
		}
		
		else throw new QuestionnaireException("Not possible to delete a questionnaire of future day ");
	}
	
	public List<Questionnaire> findAllQuestionnaire() throws QuestionnaireException {
		List<Questionnaire> questionnaires = null;
		try {
			
			questionnaires = em.createNamedQuery("Questionnaire.findAll", Questionnaire.class).getResultList();

		} catch (PersistenceException e) {
			throw new QuestionnaireException("Cannot load questionnares");

		}
		return questionnaires;
	}
	
	public Questionnaire findByDate(Date date) throws QuestionnaireException {						
		List<Questionnaire> q=em.createNamedQuery("Questionnaire.findByDate", Questionnaire.class).setParameter("qdate", date).getResultList();

		if(q.isEmpty()) {
			return null;
		}
		else if(q.size()==1) {
			return q.get(0);
		}
		throw new QuestionnaireException("Not unique result");
	}
	
	public List<User> findUserSubmitted(Questionnaire q){
		
		List<User> users = new ArrayList<User>();
		
		for(Review r: q.getReviews()) {
			if(r.getStatus().contains("submitted")) {
				users.add(r.getUser());
			}
		}
		return users;		
	}
	
	public List<User> findUserCancelled(Questionnaire q){		
		List<User> users = new ArrayList<User>();
		
		for(Review r: q.getReviews()) {
			if(r.getStatus().contains("cancelled")) {
				users.add(r.getUser());
			}
		}
		return users;		
	}
	
}
