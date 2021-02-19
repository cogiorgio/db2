package service;

import java.util.List;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import exceptions.CredentialsException;
import exceptions.UpdateProfileException;
import model.Review;
import model.User;

/**
 * Session Bean implementation class UserService
 */
@Stateless
public class UserService {
	
	@PersistenceContext(unitName = "projectEJB")
	private EntityManager em;

    /**
     * Default constructor. 
     */
    public UserService() {}
        // TODO Auto-generated constructor stub
    	
    	
    	public User checkCredentials(String usrn, String pwd) throws CredentialsException, NonUniqueResultException {
    		List<User> uList = null;
    		try {
    			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
    					.getResultList();
    		} catch (PersistenceException e) {
    			throw new CredentialsException("Could not verify credentals");
    		}
    		if (uList.isEmpty())
    			return null;
    		else if (uList.size() == 1)
    			return uList.get(0);
    		throw new NonUniqueResultException("More than one user registered with same credentials");

    	}

    	public void updateProfile(User u) throws UpdateProfileException {
    		try {
    			em.merge(u);
    		} catch (PersistenceException e) {
    			throw new UpdateProfileException("Could not change profile");
    		}
    	}
    	
    	public User signIn(String username,String password, String mail) throws CredentialsException {
    		List<User> uList = null;
 
    		try {
    			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, username).setParameter(2,password).getResultList();
    		} catch (PersistenceException e) {
    			throw new CredentialsException("Could not verify credentals");
    		}
    		if (uList.isEmpty()) {
    			//if(em.createNamedQuery("User.checkCredentials", User.class).setParameter(1,usrn ).getResultList().isEmpty());   
    			User u=new User(username, password, mail);
    			em.persist(u);
    			System.out.println("User created.");
    			return u;
    		}	
    		else if (uList.size() >= 1) 
    			throw new NonUniqueResultException("Username already used.");
    		return null;
    	}

}

