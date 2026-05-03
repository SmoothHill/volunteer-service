package com.yueping.volunteer.service;

import com.yueping.volunteer.dto.SaveServiceSiteRequest;
import com.yueping.volunteer.model.ServiceSite;
import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.repository.ServiceSiteRepository;
import com.yueping.volunteer.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ServiceSiteService {

    private final ServiceSiteRepository serviceSiteRepository;
    private final UserProfileRepository userProfileRepository;
    private final IdentityVerificationService identityVerificationService;
    private final AdminAuditLogService adminAuditLogService;

    public ServiceSiteService(ServiceSiteRepository serviceSiteRepository,
                              UserProfileRepository userProfileRepository,
                              IdentityVerificationService identityVerificationService,
                              AdminAuditLogService adminAuditLogService) {
        this.serviceSiteRepository = serviceSiteRepository;
        this.userProfileRepository = userProfileRepository;
        this.identityVerificationService = identityVerificationService;
        this.adminAuditLogService = adminAuditLogService;
    }

    @Transactional(readOnly = true)
    public List<ServiceSite> listSites() {
        return serviceSiteRepository.findAllByOrderByEnabledDescStreetNameAscCommunityNameAscNameAsc();
    }

    @Transactional(readOnly = true)
    public ServiceSite getSite(Long siteId) {
        return serviceSiteRepository.findById(siteId)
                .orElseThrow(() -> new IllegalArgumentException("服务站点不存在"));
    }

    public ServiceSite createSite(SaveServiceSiteRequest request, Long operatorId) {
        ensureVerifiedOperator(operatorId, "维护服务站点");
        ServiceSite site = new ServiceSite();
        applyChanges(site, request, true);
        site.setCreatedAt(LocalDateTime.now());
        site.setUpdatedAt(LocalDateTime.now());
        ServiceSite saved = serviceSiteRepository.save(site);
        adminAuditLogService.log(
                operatorId,
                "SERVICE_SITE_CREATED",
                "SERVICE_SITE",
                String.valueOf(saved.getId()),
                "创建服务站点 " + saved.getName() + "，街道：" + saved.getStreetName() + "，社区：" + saved.getCommunityName()
        );
        return saved;
    }

    public ServiceSite updateSite(Long siteId, SaveServiceSiteRequest request, Long operatorId) {
        ensureVerifiedOperator(operatorId, "维护服务站点");
        ServiceSite site = getSite(siteId);
        applyChanges(site, request, false);
        site.setUpdatedAt(LocalDateTime.now());
        ServiceSite saved = serviceSiteRepository.save(site);
        adminAuditLogService.log(
                operatorId,
                "SERVICE_SITE_UPDATED",
                "SERVICE_SITE",
                String.valueOf(saved.getId()),
                "更新服务站点 " + saved.getName() + "，地址：" + saved.getAddress()
        );
        return saved;
    }

    public ServiceSite toggleSite(Long siteId, Long operatorId) {
        ensureVerifiedOperator(operatorId, "维护服务站点");
        ServiceSite site = getSite(siteId);
        site.setEnabled(!site.isEnabled());
        site.setUpdatedAt(LocalDateTime.now());
        ServiceSite saved = serviceSiteRepository.save(site);
        adminAuditLogService.log(
                operatorId,
                "SERVICE_SITE_TOGGLED",
                "SERVICE_SITE",
                String.valueOf(saved.getId()),
                (saved.isEnabled() ? "启用" : "停用") + "服务站点 " + saved.getName()
        );
        return saved;
    }

    private void applyChanges(ServiceSite site, SaveServiceSiteRequest request, boolean creating) {
        site.setName(request.getName().trim());
        site.setCommunityName(request.getCommunityName().trim());
        site.setStreetName(request.getStreetName().trim());
        site.setAddress(request.getAddress().trim());
        site.setLatitude(request.getLatitude());
        site.setLongitude(request.getLongitude());
        site.setRecommendedRadiusMeters(request.getRecommendedRadiusMeters());
        if (creating) {
            site.setEnabled(request.getEnabled() == null || request.getEnabled());
        } else if (request.getEnabled() != null) {
            site.setEnabled(request.getEnabled());
        }
    }

    private UserProfile ensureVerifiedOperator(Long operatorId, String actionName) {
        UserProfile operator = userProfileRepository.findById(operatorId)
                .orElseThrow(() -> new IllegalArgumentException("操作用户不存在"));
        identityVerificationService.ensureVerified(operator, actionName);
        return operator;
    }
}
