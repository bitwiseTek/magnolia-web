package com.bitwise.magnolia.dao.school;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.bitwise.magnolia.dao.common.AbstractDao;
import com.bitwise.magnolia.dao.school.StudentDao;
import com.bitwise.magnolia.domain.school.Student;

@Repository("studentDao")
public class StudentDaoImpl extends AbstractDao<Student> implements StudentDao{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Student findByStudentId(String studentId) {
		String sql = "from Student s where s.studentId = :studentId";
		TypedQuery<Student> query = em.createQuery(sql, Student.class)
												 .setParameter("studentId", studentId);
		List<Student> students = query.getResultList();
		Student student = students.size() == 1 ? students.get(0) : null;
		return student;
	}

	@Override
	public Student findByStudentApiKey(String apiKey) {
		String sql = "from Student s where s.apiKey = :apiKey";
		TypedQuery<Student> query = em.createQuery(sql, Student.class)
				 								.setParameter("apiKey", apiKey);
		List<Student> students = query.getResultList();
		Student student = students.size() == 1 ? students.get(0) : null;
		return student;
	}

}
