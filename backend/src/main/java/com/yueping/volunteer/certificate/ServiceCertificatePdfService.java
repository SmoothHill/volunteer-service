package com.yueping.volunteer.certificate;

import com.yueping.volunteer.file.FileStorageService;
import com.yueping.volunteer.file.StoredFile;
import com.yueping.volunteer.model.BlockchainProofStatus;
import com.yueping.volunteer.model.ServiceCertificateView;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ServiceCertificatePdfService {

    private static final float PAGE_MARGIN = 40F;
    private static final float CONTENT_WIDTH = PDRectangle.A4.getWidth() - PAGE_MARGIN * 2;
    private static final float GRID_GAP = 16F;
    private static final float BOX_PADDING = 14F;
    private static final float TITLE_SIZE = 24F;
    private static final float SUBTITLE_SIZE = 12F;
    private static final float SECTION_SIZE = 17F;
    private static final float LABEL_SIZE = 11F;
    private static final float VALUE_SIZE = 15F;
    private static final float TEXT_SIZE = 11F;
    private static final float LINE_HEIGHT = 16F;
    private static final float SECTION_GAP = 18F;
    private static final float BOX_MIN_HEIGHT = 78F;
    private static final float SNAPSHOT_IMAGE_HEIGHT = 150F;
    private static final float SNAPSHOT_NOTE_HEIGHT = 42F;
    private static final float STAMP_BOX_WIDTH = 170F;
    private static final float STAMP_BOX_HEIGHT = 96F;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String FONT_RESOURCE_PATH = "fonts/simhei.ttf";
    private static final Color COLOR_TITLE = new Color(15, 23, 42);
    private static final Color COLOR_SUBTITLE = new Color(100, 116, 139);
    private static final Color COLOR_BORDER = new Color(219, 229, 242);
    private static final Color COLOR_FILL = new Color(248, 251, 255);

    private final FileStorageService fileStorageService;

    public ServiceCertificatePdfService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    public byte[] generatePdf(ServiceCertificateView proof) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PDFont font = loadChineseFont(document);
            RenderContext context = new RenderContext(document, font);

            renderHeader(context, proof);
            renderVolunteerSection(context, proof);
            renderServiceSection(context, proof);
            renderProofSection(context, proof);
            renderSnapshotSection(context, proof);
            renderFooter(context, proof);

            context.close();
            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new IllegalArgumentException("\u751f\u6210\u5fd7\u613f\u670d\u52a1\u8bc1\u660e PDF \u5931\u8d25");
        }
    }

    private PDFont loadChineseFont(PDDocument document) throws IOException {
        ClassPathResource resource = new ClassPathResource(FONT_RESOURCE_PATH);
        if (!resource.exists()) {
            throw new IllegalArgumentException("\u672a\u627e\u5230\u9879\u76ee\u5185\u7f6e\u4e2d\u6587\u5b57\u4f53\uff0c\u65e0\u6cd5\u751f\u6210 PDF");
        }
        try (InputStream inputStream = resource.getInputStream()) {
            return PDType0Font.load(document, inputStream, true);
        }
    }

    private void renderHeader(RenderContext context, ServiceCertificateView proof) throws IOException {
        context.ensureSpace(70F);
        context.writeCentered(valueOrDefault(proof.getCertificateTitle(), "\u5fd7\u613f\u670d\u52a1\u8bc1\u660e"), TITLE_SIZE, COLOR_TITLE);
        context.nextLine(14F);
        context.writeCentered(valueOrDefault(proof.getSystemName(), "\u7389\u5c4f\u5fd7\u613f\u5e73\u53f0"), SUBTITLE_SIZE, COLOR_SUBTITLE);
        context.nextLine(SECTION_GAP);
    }

    private void renderVolunteerSection(RenderContext context, ServiceCertificateView proof) throws IOException {
        renderSectionTitle(context, "\u5fd7\u613f\u8005\u4fe1\u606f");
        List<InfoCell> cells = new ArrayList<InfoCell>();
        cells.add(InfoCell.normal("\u5fd7\u613f\u8005\u59d3\u540d", proof.getVolunteerName()));
        cells.add(InfoCell.normal("\u5fd7\u613f\u8005\u7f16\u53f7", proof.getVolunteerCardNo()));
        cells.add(InfoCell.full("\u8eab\u4efd\u8bc1\u53f7\uff08\u8131\u654f\uff09", proof.getMaskedIdCardNo()));
        renderInfoGrid(context, cells);
    }

    private void renderServiceSection(RenderContext context, ServiceCertificateView proof) throws IOException {
        renderSectionTitle(context, "\u670d\u52a1\u4fe1\u606f");
        List<InfoCell> cells = new ArrayList<InfoCell>();
        cells.add(InfoCell.normal("\u6d3b\u52a8\u540d\u79f0", proof.getActivityTitle()));
        cells.add(InfoCell.normal("\u670d\u52a1\u5730\u70b9", proof.getActivityLocation()));
        cells.add(InfoCell.normal("\u6d3b\u52a8\u65f6\u95f4", formatRange(proof.getActivityStartTime(), proof.getActivityEndTime())));
        cells.add(InfoCell.normal("\u7b7e\u5230\u7b7e\u9000", formatRange(proof.getCheckInTime(), proof.getCheckOutTime())));
        cells.add(InfoCell.normal("\u670d\u52a1\u65f6\u957f", formatServiceHours(proof.getServiceHours())));
        cells.add(InfoCell.normal("\u670d\u52a1\u8bc4\u5206 / \u79ef\u5206", formatScoreAndPoints(proof)));
        cells.add(InfoCell.normal("\u7ec4\u7ec7\u8005\u786e\u8ba4\u65f6\u95f4", formatDateTime(proof.getOrganizerConfirmedAt())));
        cells.add(InfoCell.normal("\u51fa\u5177\u65e5\u671f", formatDate(proof.getIssuedAt())));
        cells.add(InfoCell.full("\u670d\u52a1\u8bc4\u4ef7 / \u5907\u6ce8", firstNonBlank(proof.getServiceComment(), proof.getOrganizerConfirmComment(), "\u65e0")));
        renderInfoGrid(context, cells);
    }

    private void renderProofSection(RenderContext context, ServiceCertificateView proof) throws IOException {
        renderSectionTitle(context, "\u8bc1\u660e\u4e0e\u5b58\u8bc1");
        List<InfoCell> cells = new ArrayList<InfoCell>();
        cells.add(InfoCell.normal("\u8bc1\u4e66\u7f16\u53f7", proof.getCertificateNo()));
        cells.add(InfoCell.normal("\u7535\u5b50\u8bc1\u4e66", proof.getCertificateUrl() == null ? "\u6682\u65e0\u7535\u5b50\u8bc1\u4e66" : "\u5df2\u751f\u6210\uff0c\u53ef\u5728\u5c0f\u7a0b\u5e8f\u4e2d\u67e5\u770b"));
        cells.add(InfoCell.full("\u539f\u59cb\u54c8\u5e0c", proof.getEvidenceHash()));
        cells.add(InfoCell.normal("\u533a\u5757\u94fe\u5b58\u8bc1\u72b6\u6001", formatBlockchainProofStatus(proof.getBlockchainProofStatus())));
        cells.add(InfoCell.normal("\u5b58\u8bc1\u6d41\u6c34\u53f7", proof.getBlockchainTransactionNo()));
        cells.add(InfoCell.normal("\u5b58\u8bc1\u65f6\u95f4", formatDateTime(proof.getBlockchainAnchoredAt())));
        cells.add(InfoCell.normal("\u670d\u52a1\u5546", valueOrDefault(proof.getBlockchainProviderName(), "mock-chain")));
        cells.add(InfoCell.normal("\u6d3b\u52a8\u7c7b\u522b", valueOrDefault(proof.getActivityCategory(), "\u7efc\u5408\u670d\u52a1")));
        renderInfoGrid(context, cells);
    }

    private void renderSnapshotSection(RenderContext context, ServiceCertificateView proof) throws IOException {
        renderSectionTitle(context, "\u670d\u52a1\u5feb\u7167");
        List<ServiceCertificateView.SnapshotItem> snapshots = proof.getSnapshots() == null
                ? new ArrayList<ServiceCertificateView.SnapshotItem>()
                : proof.getSnapshots();
        if (snapshots.isEmpty()) {
            renderParagraphCard(context, "\u6682\u65e0\u670d\u52a1\u5feb\u7167");
            return;
        }

        float cardWidth = (CONTENT_WIDTH - GRID_GAP) / 2F;
        int index = 0;
        while (index < snapshots.size()) {
            ServiceCertificateView.SnapshotItem left = snapshots.get(index++);
            ServiceCertificateView.SnapshotItem right = index < snapshots.size() ? snapshots.get(index++) : null;
            float rowHeight = Math.max(
                    estimateSnapshotCardHeight(context, left, cardWidth),
                    estimateSnapshotCardHeight(context, right, cardWidth)
            );
            context.ensureSpace(rowHeight);
            float topY = context.cursorY;
            renderSnapshotCard(context, left, PAGE_MARGIN, topY, cardWidth, rowHeight);
            if (right != null) {
                renderSnapshotCard(context, right, PAGE_MARGIN + cardWidth + GRID_GAP, topY, cardWidth, rowHeight);
            }
            context.cursorY = topY - rowHeight - GRID_GAP;
        }
        context.cursorY += GRID_GAP;
    }

    private void renderFooter(RenderContext context, ServiceCertificateView proof) throws IOException {
        float footerHeight = 120F;
        context.ensureSpace(footerHeight);
        float topY = context.cursorY;
        float metaWidth = CONTENT_WIDTH - STAMP_BOX_WIDTH - GRID_GAP;

        List<String> lines = new ArrayList<String>();
        lines.add("\u51fa\u5177\u5e73\u53f0\uff1a" + valueOrDefault(proof.getSystemName(), "\u7389\u5c4f\u5fd7\u613f\u5e73\u53f0"));
        lines.add("\u672c\u8bc1\u660e\u4f9d\u636e\u6d3b\u52a8\u8bb0\u5f55\u3001\u670d\u52a1\u5feb\u7167\u3001\u8f68\u8ff9\u70b9\u548c\u7ec4\u7ec7\u8005\u786e\u8ba4\u4fe1\u606f\u751f\u6210\u3002");
        lines.add("\u5982\u9700\u6838\u9a8c\uff0c\u53ef\u7ed3\u5408\u8bc1\u4e66\u7f16\u53f7\u3001\u539f\u59cb\u54c8\u5e0c\u4e0e\u5b58\u8bc1\u6d41\u6c34\u53f7\u8fdb\u884c\u590d\u67e5\u3002");

        float textY = topY - 8F;
        for (String line : lines) {
            for (String wrapped : wrapText(line, context.font, TEXT_SIZE, metaWidth)) {
                context.writeAt(wrapped, PAGE_MARGIN, textY, TEXT_SIZE, COLOR_TITLE);
                textY -= LINE_HEIGHT;
            }
            textY -= 4F;
        }

        float stampX = PAGE_MARGIN + metaWidth + GRID_GAP;
        context.fillAndStrokeRect(
                stampX,
                topY - STAMP_BOX_HEIGHT,
                STAMP_BOX_WIDTH,
                STAMP_BOX_HEIGHT,
                Color.WHITE,
                COLOR_BORDER
        );
        context.writeCenteredInBox("\u76d6\u7ae0\u533a", stampX, topY - STAMP_BOX_HEIGHT, STAMP_BOX_WIDTH, STAMP_BOX_HEIGHT, VALUE_SIZE, COLOR_SUBTITLE);
        context.cursorY = topY - footerHeight;
    }

    private void renderSectionTitle(RenderContext context, String title) throws IOException {
        context.ensureSpace(32F);
        context.writeAt(title, PAGE_MARGIN, context.cursorY, SECTION_SIZE, COLOR_TITLE);
        context.cursorY -= SECTION_GAP;
    }

    private void renderInfoGrid(RenderContext context, List<InfoCell> cells) throws IOException {
        float columnWidth = (CONTENT_WIDTH - GRID_GAP) / 2F;
        int index = 0;
        while (index < cells.size()) {
            InfoCell current = cells.get(index);
            if (current.fullWidth) {
                float height = estimateInfoCellHeight(context, current, CONTENT_WIDTH);
                context.ensureSpace(height);
                renderInfoCell(context, current, PAGE_MARGIN, context.cursorY, CONTENT_WIDTH, height);
                context.cursorY -= height + GRID_GAP;
                index++;
                continue;
            }

            InfoCell next = index + 1 < cells.size() && !cells.get(index + 1).fullWidth ? cells.get(index + 1) : null;
            float leftHeight = estimateInfoCellHeight(context, current, columnWidth);
            float rightHeight = next == null ? leftHeight : estimateInfoCellHeight(context, next, columnWidth);
            float rowHeight = Math.max(leftHeight, rightHeight);
            context.ensureSpace(rowHeight);
            float topY = context.cursorY;
            renderInfoCell(context, current, PAGE_MARGIN, topY, columnWidth, rowHeight);
            if (next != null) {
                renderInfoCell(context, next, PAGE_MARGIN + columnWidth + GRID_GAP, topY, columnWidth, rowHeight);
                index += 2;
            } else {
                index++;
            }
            context.cursorY = topY - rowHeight - GRID_GAP;
        }
        context.cursorY += GRID_GAP;
    }

    private void renderInfoCell(RenderContext context, InfoCell cell, float leftX, float topY, float width, float height) throws IOException {
        float bottomY = topY - height;
        context.fillAndStrokeRect(leftX, bottomY, width, height, COLOR_FILL, COLOR_BORDER);
        float labelX = leftX + BOX_PADDING;
        float labelY = topY - BOX_PADDING;
        context.writeAt(cell.label, labelX, labelY, LABEL_SIZE, COLOR_SUBTITLE);

        List<String> lines = wrapText(valueOrDefault(cell.value, "\u6682\u65e0"), context.font, VALUE_SIZE, width - BOX_PADDING * 2);
        float valueY = labelY - 22F;
        for (String line : lines) {
            context.writeAt(line, labelX, valueY, VALUE_SIZE, COLOR_TITLE);
            valueY -= LINE_HEIGHT;
        }
    }

    private float estimateInfoCellHeight(RenderContext context, InfoCell cell, float width) throws IOException {
        List<String> lines = wrapText(valueOrDefault(cell.value, "\u6682\u65e0"), context.font, VALUE_SIZE, width - BOX_PADDING * 2);
        float height = BOX_PADDING * 2 + 20F + lines.size() * LINE_HEIGHT;
        return Math.max(height, BOX_MIN_HEIGHT);
    }

    private void renderParagraphCard(RenderContext context, String text) throws IOException {
        List<String> lines = wrapText(valueOrDefault(text, "\u6682\u65e0"), context.font, TEXT_SIZE, CONTENT_WIDTH - BOX_PADDING * 2);
        float height = BOX_PADDING * 2 + lines.size() * LINE_HEIGHT + 6F;
        context.ensureSpace(height);
        float topY = context.cursorY;
        context.fillAndStrokeRect(PAGE_MARGIN, topY - height, CONTENT_WIDTH, height, COLOR_FILL, COLOR_BORDER);
        float textY = topY - BOX_PADDING;
        for (String line : lines) {
            context.writeAt(line, PAGE_MARGIN + BOX_PADDING, textY, TEXT_SIZE, COLOR_TITLE);
            textY -= LINE_HEIGHT;
        }
        context.cursorY = topY - height;
    }

    private float estimateSnapshotCardHeight(RenderContext context, ServiceCertificateView.SnapshotItem item, float width) throws IOException {
        if (item == null) {
            return 0F;
        }
        List<String> noteLines = wrapText(valueOrDefault(item.getNote(), "\u670d\u52a1\u73b0\u573a\u5feb\u7167"), context.font, TEXT_SIZE, width - BOX_PADDING * 2);
        float noteHeight = Math.max(SNAPSHOT_NOTE_HEIGHT, noteLines.size() * LINE_HEIGHT + 14F);
        return BOX_PADDING * 2 + SNAPSHOT_IMAGE_HEIGHT + noteHeight + 28F;
    }

    private void renderSnapshotCard(RenderContext context,
                                    ServiceCertificateView.SnapshotItem item,
                                    float leftX,
                                    float topY,
                                    float width,
                                    float height) throws IOException {
        float bottomY = topY - height;
        context.fillAndStrokeRect(leftX, bottomY, width, height, COLOR_FILL, COLOR_BORDER);

        float imageX = leftX + BOX_PADDING;
        float imageY = topY - BOX_PADDING - SNAPSHOT_IMAGE_HEIGHT;
        float imageWidth = width - BOX_PADDING * 2;
        drawSnapshotImage(context, item, imageX, imageY, imageWidth, SNAPSHOT_IMAGE_HEIGHT);

        List<String> noteLines = wrapText(valueOrDefault(item.getNote(), "\u670d\u52a1\u73b0\u573a\u5feb\u7167"), context.font, TEXT_SIZE, imageWidth);
        float textY = imageY - 18F;
        for (String line : noteLines) {
            context.writeAt(line, imageX, textY, TEXT_SIZE, COLOR_TITLE);
            textY -= LINE_HEIGHT;
        }
        context.writeAt(formatDateTime(item.getCreatedAt()), imageX, bottomY + BOX_PADDING + 2F, LABEL_SIZE, COLOR_SUBTITLE);
    }

    private void drawSnapshotImage(RenderContext context,
                                   ServiceCertificateView.SnapshotItem item,
                                   float x,
                                   float y,
                                   float width,
                                   float height) throws IOException {
        StoredFile storedFile = fileStorageService.load(item == null ? null : item.getImageUrl());
        if (storedFile == null) {
            drawImagePlaceholder(context, x, y, width, height);
            return;
        }

        try (InputStream inputStream = storedFile.getInputStream()) {
            PDImageXObject image = PDImageXObject.createFromByteArray(
                    context.document,
                    readAllBytes(inputStream),
                    storedFile.getFileName()
            );
            float imageWidth = image.getWidth();
            float imageHeight = image.getHeight();
            float scale = Math.min(width / imageWidth, height / imageHeight);
            float drawWidth = imageWidth * scale;
            float drawHeight = imageHeight * scale;
            float drawX = x + (width - drawWidth) / 2F;
            float drawY = y + (height - drawHeight) / 2F;
            context.strokeRect(x, y, width, height, COLOR_BORDER);
            context.stream.drawImage(image, drawX, drawY, drawWidth, drawHeight);
        } catch (IOException ex) {
            drawImagePlaceholder(context, x, y, width, height);
        }
    }

    private void drawImagePlaceholder(RenderContext context, float x, float y, float width, float height) throws IOException {
        context.fillAndStrokeRect(x, y, width, height, new Color(241, 245, 249), COLOR_BORDER);
        context.writeCenteredInBox("\u6682\u65e0\u5feb\u7167\u56fe\u7247", x, y, width, height, TEXT_SIZE, COLOR_SUBTITLE);
    }

    private byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        return outputStream.toByteArray();
    }

    private String formatRange(LocalDateTime start, LocalDateTime end) {
        if (start == null && end == null) {
            return "\u6682\u65e0";
        }
        return formatDateTime(start) + " \u81f3 " + formatDateTime(end);
    }

    private String formatDateTime(LocalDateTime value) {
        if (value == null) {
            return "\u6682\u65e0";
        }
        return value.format(DATE_TIME_FORMATTER);
    }

    private String formatDate(LocalDateTime value) {
        if (value == null) {
            return "\u6682\u65e0";
        }
        return value.format(DATE_FORMATTER);
    }

    private String formatServiceHours(double hours) {
        return String.format(Locale.CHINA, "%.2f \u5c0f\u65f6", hours);
    }

    private String formatScoreAndPoints(ServiceCertificateView proof) {
        String rating = proof.getServiceRating() == null ? "\u6682\u65e0" : String.valueOf(proof.getServiceRating());
        return rating + " / " + proof.getEarnedPoints() + " \u5206";
    }

    private String formatBlockchainProofStatus(BlockchainProofStatus status) {
        if (status == null) {
            return "\u6682\u65e0";
        }
        switch (status) {
            case MOCK_CHAINED:
                return "\u5df2\u4e0a\u94fe";
            case FAILED:
                return "\u4e0a\u94fe\u5931\u8d25";
            case PENDING:
                return "\u5f85\u4e0a\u94fe";
            default:
                return status.name();
        }
    }

    private String valueOrDefault(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
    }

    private String firstNonBlank(String first, String second, String fallback) {
        if (first != null && !first.trim().isEmpty()) {
            return first.trim();
        }
        if (second != null && !second.trim().isEmpty()) {
            return second.trim();
        }
        return fallback;
    }

    private List<String> wrapText(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<String>();
        String source = valueOrDefault(text, "\u6682\u65e0");
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            current.append(ch);
            float width = font.getStringWidth(current.toString()) / 1000F * fontSize;
            if (width > maxWidth && current.length() > 1) {
                current.deleteCharAt(current.length() - 1);
                lines.add(current.toString());
                current = new StringBuilder().append(ch);
            }
        }
        if (current.length() > 0) {
            lines.add(current.toString());
        }
        return lines;
    }

    private static class InfoCell {
        private final String label;
        private final String value;
        private final boolean fullWidth;

        private InfoCell(String label, String value, boolean fullWidth) {
            this.label = label;
            this.value = value;
            this.fullWidth = fullWidth;
        }

        private static InfoCell normal(String label, String value) {
            return new InfoCell(label, value, false);
        }

        private static InfoCell full(String label, String value) {
            return new InfoCell(label, value, true);
        }
    }

    private static class RenderContext {
        private final PDDocument document;
        private final PDFont font;
        private PDPage page;
        private PDPageContentStream stream;
        private float cursorY;

        private RenderContext(PDDocument document, PDFont font) throws IOException {
            this.document = document;
            this.font = font;
            newPage();
        }

        private void newPage() throws IOException {
            if (stream != null) {
                stream.close();
            }
            page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            stream = new PDPageContentStream(document, page);
            cursorY = PDRectangle.A4.getHeight() - PAGE_MARGIN;
        }

        private void ensureSpace(float neededHeight) throws IOException {
            if (cursorY - neededHeight < PAGE_MARGIN) {
                newPage();
            }
        }

        private void nextLine(float gap) {
            cursorY -= gap;
        }

        private void writeCentered(String text, float fontSize, Color color) throws IOException {
            float textWidth = font.getStringWidth(text) / 1000F * fontSize;
            float startX = (PDRectangle.A4.getWidth() - textWidth) / 2F;
            writeAt(text, startX, cursorY, fontSize, color);
        }

        private void writeCenteredInBox(String text, float x, float y, float width, float height, float fontSize, Color color) throws IOException {
            float textWidth = font.getStringWidth(text) / 1000F * fontSize;
            float textX = x + (width - textWidth) / 2F;
            float textY = y + height / 2F;
            writeAt(text, textX, textY, fontSize, color);
        }

        private void writeAt(String text, float x, float y, float fontSize, Color color) throws IOException {
            stream.beginText();
            stream.setFont(font, fontSize);
            stream.setNonStrokingColor(color);
            stream.newLineAtOffset(x, y);
            stream.showText(text);
            stream.endText();
        }

        private void fillAndStrokeRect(float x, float y, float width, float height, Color fillColor, Color borderColor) throws IOException {
            stream.setNonStrokingColor(fillColor);
            stream.addRect(x, y, width, height);
            stream.fill();
            strokeRect(x, y, width, height, borderColor);
        }

        private void strokeRect(float x, float y, float width, float height, Color borderColor) throws IOException {
            stream.setStrokingColor(borderColor);
            stream.addRect(x, y, width, height);
            stream.stroke();
        }

        private void close() throws IOException {
            if (stream != null) {
                stream.close();
            }
        }
    }
}
