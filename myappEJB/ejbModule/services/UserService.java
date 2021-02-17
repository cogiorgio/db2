package services;

import javax.ejb.Stateless;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;
import entities.User;
import exceptions.CredentialsException;
import exceptions.UpdateProfileException;

import java.util.Date;
import java.util.List;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "myappEJB")
	private EntityManager em;

	public UserService() {
	}
	
	
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
	
	public User signIn(String usrn,String psswrd) throws CredentialsException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).getResultList();
		} catch (PersistenceException e) {
			throw new CredentialsException("Could not verify credentals");
		}
		if (uList.isEmpty()) {
			/*if(em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, ).getResultList().isEmpty()) {
				User u=new User();
				u.setUsername(usrn);
				u.setPassword(psswrd);
				System.out.println("user created.");
			}else {
				throw new NonUniqueResultException("Email already used.");
			}*/
			User u=new User();
			u.setUsername(usrn);
			u.setLastLog(new Date());
			System.out.println("User created.");
			return u;
		}	
		else if (uList.size() >= 1) 
			throw new NonUniqueResultException("Username already used.");
		return null;
	}
	
}