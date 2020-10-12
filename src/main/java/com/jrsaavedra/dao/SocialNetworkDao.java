package com.jrsaavedra.dao;

import java.util.List;

import com.jrsaavedra.model.SocialNetwork;
import com.jrsaavedra.model.TeacherHasSocialNetwork;

public interface SocialNetworkDao {
	// methods to implement
	void saveSocialNetwork(SocialNetwork socialNetwork);
	void deleteSocialNetwork(long id);
	void updateSocialNetwork(SocialNetwork socialNetwork);
	
	List<SocialNetwork> findAllSocialNetwork();
	SocialNetwork findById(long id);
	SocialNetwork findByName(String name);
	
	TeacherHasSocialNetwork findSocialNetworkByIdAndName(Long socialNetwork_id, String nickname);
	TeacherHasSocialNetwork findSocialNetworkByTeacherIdAndSocialNetworkId(Long teacher_id, Long socialNetwork_id);
}
