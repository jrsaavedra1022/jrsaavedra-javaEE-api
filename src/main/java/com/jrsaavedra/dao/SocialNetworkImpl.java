/**
 * 
 */
package com.jrsaavedra.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jrsaavedra.model.SocialNetwork;
import com.jrsaavedra.model.TeacherHasSocialNetwork;

/**
 * @author Bios-hp
 *
 */

@Repository
@Transactional
public class SocialNetworkImpl extends AbstractSession implements SocialNetworkDao {

	@Override
	public void saveSocialNetwork(SocialNetwork socialNetwork) {
		// TODO Auto-generated method stub
		super.getSession().persist(socialNetwork);
	}

	@Override
	public void deleteSocialNetwork(long id) {
		// TODO Auto-generated method stub
		SocialNetwork socialNetwork = this.findById(id);
		if (socialNetwork != null) {
			super.getSession().delete(socialNetwork);
		}
	}

	@Override
	public void updateSocialNetwork(SocialNetwork socialNetwork) {
		// TODO Auto-generated method stub
		super.getSession().update(socialNetwork);
	}

	@Override
	public List<SocialNetwork> findAllSocialNetwork() {
		// TODO Auto-generated method stub
		return super.getSession().createQuery("from SocialNetwork").list();
	}

	@Override
	public SocialNetwork findById(long id) {
		// TODO Auto-generated method stub
		return (SocialNetwork) super.getSession().get(SocialNetwork.class, id);
	}

	@Override
	public SocialNetwork findByName(String name) {
		// TODO Auto-generated method stub
		return (SocialNetwork) super.getSession().createQuery("from SocialNetwork where name = :name")
				.setParameter("name", name).uniqueResult();
	}

	@Override
	public TeacherHasSocialNetwork findSocialNetworkByIdAndName(Long socialNetwork_id, String nickname) {
		// TODO Auto-generated method stub
		List<Object[]> objects = super.getSession()
				.createQuery("from TeacherHasSocialNetwork tsm join tsm.socialNetwork sn "
						+ " where sn.id = :socialNetwork_id and tsm.nickname = :nickname")
				.setParameter("socialNetwork_id", socialNetwork_id).setParameter("nickname", nickname).list();
		if (objects.size() > 0) {
			for (Object[] objs : objects) {
				for (Object obj : objs) {
					if (obj instanceof TeacherHasSocialNetwork) {
						return (TeacherHasSocialNetwork) obj;
					}
				}
			}
		}
		return null;
	}

	@Override
	public TeacherHasSocialNetwork findSocialNetworkByTeacherIdAndSocialNetworkId(Long teacher_id,
			Long socialNetwork_id) {
		// TODO Auto-generated method stub
		List<Object[]> objects = super.getSession()
				.createQuery("from TeacherHasSocialNetwork tsm "
						+ "join tsm.socialNetwork sn "
						+ "join tsm.teacher t "
						+ " where t.id = :teacher_id and sn.id = :socialNetwork_id")
				.setParameter("teacher_id", teacher_id)
				.setParameter("socialNetwork_id", socialNetwork_id)
				.list();
		if (objects.size() > 0) {
			for (Object[] objs : objects) {
				for (Object obj : objs) {
					if (obj instanceof TeacherHasSocialNetwork) {
						return (TeacherHasSocialNetwork) obj;
					}
				}
			}
		}
		return null;
	}

}
