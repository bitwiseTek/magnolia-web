package com.bitwise.magnolia.dao.school;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.bitwise.magnolia.dao.common.AbstractDao;
import com.bitwise.magnolia.dao.common.ApplicationConstant;
import com.bitwise.magnolia.dao.school.SchoolDao;
import com.bitwise.magnolia.domain.school.School;

@Repository("schoolDao")
public class SchoolDaoImpl extends AbstractDao<Object> implements SchoolDao{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public boolean isSchoolExist(String alias) {
		String sql = "from School school where school.alias  = :alias and school.status = :status";
		School school = (School) this.em.createQuery(sql)
														 .setParameter("alias", alias)
														 .setParameter("status", ApplicationConstant.ACTIVE_STATUS)
														 .getSingleResult();
		if(school != null){
			return true;
		}
		return false;
	}

	@Override
	public School findSchoolByAlias(String alias) {
		String sql = "from School s where s.alias  = :alias and s.status = :status";
		TypedQuery<School> query = em.createQuery(sql, School.class)
														 .setParameter("alias", alias)
														 .setParameter("status", ApplicationConstant.ACTIVE_STATUS);
		List<School> schools = query.getResultList();
		School school = schools.size() == 1 ? schools.get(0) : null;
		if (school != null) {
			Hibernate.initialize(school.getSubSchoolList());
		}
		return school;
	}

	@Override
	public boolean isApiKeyExist(String apiKey) {
		String sql = "from School s where s.status = :status and s.apiKey = :apiKey";
		return ((School) this.em.createQuery(sql)
												.setParameter("status", ApplicationConstant.ACTIVE_STATUS)
												.setParameter("apiKey", apiKey)
												.getSingleResult()) == null ? false : true;
	}

}
