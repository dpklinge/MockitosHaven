package com.fdmgroup.mockitoshaven.dataaccess;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MockitosHavenJpaDao implements MockitosHavenDao {
	@Autowired
	private EntityManagerFactory emf;
	
	@Override
	public <T> boolean writeObject(T object) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(object);
		em.getTransaction().commit();
		em.close();
		return true;
	}

	@Override
	public <T, ID> T readObject(Class<T> type , ID id) {
		EntityManager em = emf.createEntityManager();
		T object = em.find(type, id);
		em.close();
		return object;
	}

	@Override
	public<T, ID> boolean deleteObject(Class<T> type, ID id) {
		EntityManager em = emf.createEntityManager();
		T object = em.find(type, id);
		em.getTransaction().begin();
		em.remove(object);
		em.getTransaction().commit();
		em.close();
		return true;
	}

	@Override
	public <T, ID> T updateObject(Class<T> type, T object,ID id ) {   
		EntityManager em = emf.createEntityManager();
		T objectFound = em.find(type, id);
		em.getTransaction().begin();
		if(objectFound!=null) {
			em.merge(object);
		}
		em.getTransaction().commit();
		return object;
	}

	@Override
	public <T> List<T> findAllObjects(Class<T> type) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<T> query = em.createQuery("SELECT o FROM "+type.getSimpleName()+" o", type);
		List<T> objects = query.getResultList();
		em.close();
		return objects;
	}

	@Override
	public <T, FIELD> List<T> findAllObjectsByField(Class<T> type, String fieldName, FIELD fieldValue) {
		EntityManager em = emf.createEntityManager();
		TypedQuery<T> query = em.createQuery("SELECT o FROM "+type.getSimpleName()+" o WHERE "+fieldName+"= ?1", type);
		query.setParameter(1, fieldValue);
		List<T> objects = query.getResultList();
		em.close();
		return objects;
	}

	@Override
	public <T, U, FIELD> List<T> findAllFieldValuesByObject(Class<U> type, String fieldName) {
		
		EntityManager em = emf.createEntityManager();
		String jpql = "SELECT o."+fieldName+" FROM "+type.getSimpleName()+" o";
		System.out.println("Executing jpql: "+jpql);
		Query query = em.createQuery(jpql);
		List<T> objects = query.getResultList();
		System.out.println("Jpql results: "+objects);
		em.close();
		return objects;
	}


	
	

}
