package com.bitwise.magnolia.domain.support;
/**
 *  
 * @author Sika Kay
 * @date 16/06/16
 *
 */
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.bitwise.magnolia.domain.school.School;
import com.bitwise.magnolia.domain.support.EntityBuilder.EntityBuilderManager;

public class InitialDataSetup {
	
	public InitialDataSetup(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	@PersistenceContext
	private EntityManager em;
	
	private School school;
	
	private TransactionTemplate transactionTemplate;
	
	public void initialize() {
		EntityBuilderManager.setEntityManager(em);
		this.transactionTemplate.execute(new TransactionCallback<Void>() {
			public Void doInTransaction(TransactionStatus status) {
				if (dataIsAlreadyPresent()) {
					return null;
				}
				
				//Tables Creation
				{
					InitialDataSetup.this.school = new SchoolBuilder() {
						{
							school("info@magnolia.com", "Choba", "mg", "https://magnolia.com", new Date().toString(), 365, "logo", "Magnolia", "a5ukhxhEObzv8TBW8yxeNYy6hm1knu", "Active");
						}
					}.build();
				
				}
				return null;
			}
			
			private Boolean dataIsAlreadyPresent() {
				return InitialDataSetup.this.em.createQuery("select count(s.id) from School s", Long.class).getSingleResult().longValue() > 0;
			}
		});
		EntityBuilderManager.clearEntityManager();
	}
	
}
