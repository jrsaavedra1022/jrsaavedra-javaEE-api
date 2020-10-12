package com.jrsaavedra.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jrsaavedra.dao.SocialNetworkDao;
import com.jrsaavedra.model.SocialNetwork;
import com.jrsaavedra.model.TeacherHasSocialNetwork;

@Service("socialNetworkService")
@Transactional
public class SocialNetworkServiceImpl implements SocialNetworkService{
	@Autowired
	private SocialNetworkDao _socialNetworkDao;
	@Override
	public void saveSocialNetwork(SocialNetwork socialNetwork) {
		// TODO Auto-generated method stub
		this._socialNetworkDao.saveSocialNetwork(socialNetwork);
	}

	@Override
	public void deleteSocialNetwork(long id) {
		// TODO Auto-generated method stub
		this._socialNetworkDao.deleteSocialNetwork(id);
	}

	@Override
	public void updateSocialNetwork(SocialNetwork socialNetwork) {
		// TODO Auto-generated method stub
		this._socialNetworkDao.updateSocialNetwork(socialNetwork);
	}

	@Override
	public List<SocialNetwork> findAllSocialNetwork() {
		// TODO Auto-generated method stub
		return this._socialNetworkDao.findAllSocialNetwork();
	}

	@Override
	public SocialNetwork findById(long id) {
		// TODO Auto-generated method stub
		return this._socialNetworkDao.findById(id);
	}

	@Override
	public SocialNetwork findByName(String name) {
		// TODO Auto-generated method stub
		return this._socialNetworkDao.findByName(name);
	}

	@Override
	public TeacherHasSocialNetwork findSocialNetworkByIdAndName(Long socialNetwork_id, String nickname) {
		// TODO Auto-generated method stub
		return this._socialNetworkDao.findSocialNetworkByIdAndName(socialNetwork_id, nickname);
	}

	@Override
	public TeacherHasSocialNetwork findSocialNetworkByTeacherIdAndSocialNetworkId(Long teacher_id,
			Long socialNetwork_id) {
		// TODO Auto-generated method stub
		return this._socialNetworkDao.findSocialNetworkByTeacherIdAndSocialNetworkId(teacher_id, socialNetwork_id);
	}

}
