package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    long countByRole(UserRole role);

    Optional<UserProfile> findByOpenId(String openId);

    List<UserProfile> findByRoleIn(Collection<UserRole> roles);

    boolean existsByIdCardHashAndIdNot(String idCardHash, Long id);

    boolean existsByVolunteerCardNoAndIdNot(String volunteerCardNo, Long id);

    boolean existsByPhoneHashAndIdNot(String phoneHash, Long id);
}
