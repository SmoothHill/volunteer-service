package com.yueping.volunteer.repository;

import com.yueping.volunteer.model.LoginVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoginVerificationCodeRepository extends JpaRepository<LoginVerificationCode, Long> {

    Optional<LoginVerificationCode> findTopByLoginTicketOrderByCreatedAtDesc(String loginTicket);

    Optional<LoginVerificationCode> findTopByLoginTicketAndCodeOrderByCreatedAtDesc(String loginTicket, String code);

    Optional<LoginVerificationCode> findTopByLoginTicketAndPhoneHashOrderByCreatedAtDesc(String loginTicket, String phoneHash);
}
