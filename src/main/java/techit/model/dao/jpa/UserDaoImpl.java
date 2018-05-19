package techit.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import techit.model.Unit;
import techit.model.User;
import techit.model.dao.UserDao;

@Repository
public class UserDaoImpl implements UserDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public User getUser(Long id) {
		return entityManager.find(User.class, id);
	}

	@Override
	public User getUser(String username) {
		String query = "from User where username = :username";

		List<User> users = entityManager.createQuery(query, User.class).setParameter("username", username.toLowerCase())
				.getResultList();
		return users.size() == 0 ? null : users.get(0);
	}

	@Override
	public List<User> getUsers() {
		return entityManager.createQuery("from User order by id", User.class).getResultList();
	}

	@Override
	@Transactional
	public User saveUser(User user) {
		return entityManager.merge(user);
	}

	@Override
	public List<User> getTechniciansOfUnit(Unit unit) {
		String query = "select u from User u join u.unit uu where uu=:unit and u.type='TECHNICIAN'";
		return entityManager.createQuery(query, User.class).setParameter("unit", unit).getResultList();
	}

}
