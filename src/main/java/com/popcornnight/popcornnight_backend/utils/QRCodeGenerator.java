package com.popcornnight.popcornnight_backend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.popcornnight.popcornnight_backend.dto.ticket.TicketQRcodeInfo;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class QRCodeGenerator {

    public static BufferedImage generateQRCodeImage(TicketQRcodeInfo qRcodeInfo, int width, int height)
            throws Exception {
        Map<String, Object> qrData = new HashMap<>();
        qrData.put("qrId", qRcodeInfo.getQrId());
        qrData.put("ticketId", qRcodeInfo.getTicketId());
        qrData.put("ticketStatus", qRcodeInfo.getTicketStatus());
        qrData.put("seatNumber", qRcodeInfo.getSeatNumbers());
        qrData.put("movieTitle", qRcodeInfo.getMovieTitle());
        qrData.put("showTimeslot", qRcodeInfo.getShowTimeslot());
        qrData.put("geneartedAt", qRcodeInfo.getGeneartedAt());

        ObjectMapper objectMapper = new ObjectMapper();
        String qrContentJson = objectMapper.writeValueAsString(qrData);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(qrContentJson, BarcodeFormat.QR_CODE, width, height, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

}
