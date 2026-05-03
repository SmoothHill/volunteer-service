package com.yueping.volunteer.certificate;

import com.yueping.volunteer.file.FileStorageService;
import com.yueping.volunteer.service.SystemSettingService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class CertificateService {

    private final FileStorageService fileStorageService;
    private final SystemSettingService systemSettingService;

    public CertificateService(FileStorageService fileStorageService,
                              SystemSettingService systemSettingService) {
        this.fileStorageService = fileStorageService;
        this.systemSettingService = systemSettingService;
    }

    public String generateCertificate(CertificatePayload payload) {
        BufferedImage image = new BufferedImage(1400, 900, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        String certificateTitle = systemSettingService.getCertificateTitle();
        String systemName = systemSettingService.getSystemName();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setPaint(new GradientPaint(0, 0, new Color(242, 248, 255), 1400, 900, new Color(224, 236, 255)));
            graphics.fillRect(0, 0, 1400, 900);

            graphics.setColor(new Color(29, 78, 216));
            graphics.fillRoundRect(60, 60, 1280, 780, 40, 40);
            graphics.setColor(Color.WHITE);
            graphics.fillRoundRect(80, 80, 1240, 740, 32, 32);

            graphics.setColor(new Color(29, 78, 216));
            graphics.setFont(new Font("Microsoft YaHei", Font.BOLD, 42));
            graphics.drawString(certificateTitle, 480, 180);

            graphics.setColor(new Color(15, 23, 42));
            graphics.setFont(new Font("Microsoft YaHei", Font.BOLD, 34));
            graphics.drawString(payload.getVolunteerName() + "：", 160, 290);

            graphics.setFont(new Font("Microsoft YaHei", Font.PLAIN, 28));
            graphics.drawString("感谢你参与《" + payload.getActivityTitle() + "》志愿服务活动。", 160, 360);
            graphics.drawString("服务地点：" + payload.getActivityLocation(), 160, 420);
            graphics.drawString("服务时长：" + payload.getServiceHours() + " 小时", 160, 480);
            graphics.drawString("获得积分：" + payload.getEarnedPoints() + " 分", 160, 540);
            graphics.drawString("发证日期：" + payload.getIssueDate(), 160, 600);
            graphics.drawString("证书编号：" + payload.getCertificateNo(), 160, 660);

            graphics.setColor(new Color(71, 85, 105));
            graphics.setFont(new Font("Microsoft YaHei", Font.PLAIN, 24));
            graphics.drawString("本证书由" + systemName + "出具，用于记录服务成果。", 160, 760);
        } finally {
            graphics.dispose();
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            return fileStorageService.storeBytes(
                    outputStream.toByteArray(),
                    "certificates",
                    payload.getCertificateNo() + ".png",
                    "image/png"
            );
        } catch (IOException ex) {
            throw new IllegalArgumentException("生成电子证书失败");
        }
    }
}
