package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.ServiceSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceSiteRepository extends JpaRepository<ServiceSite, Long> {

    List<ServiceSite> findAllByOrderByEnabledDescStreetNameAscCommunityNameAscNameAsc();
}
