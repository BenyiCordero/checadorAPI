package com.bcss.checador.service;

import com.bcss.checador.domain.CodigoQR;
import com.bcss.checador.exception.QrCodeNotFoundException;
import com.bcss.checador.exception.ReadingQrException;
import com.bcss.checador.repository.CodigoQrRepository;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class CodigoQrServiceImpl implements CodigoQrService {

    private final CodigoQrRepository codigoQrRepository;

    public CodigoQrServiceImpl(CodigoQrRepository codigoQrRepository) {
        this.codigoQrRepository = codigoQrRepository;
    }

    @Override
    public void generate(Sucursal sucursal) throws IOException, WriterException {
        int ancho = 600;
        int alto = 600;

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(
                sucursal.getSucursalKey(),
                BarcodeFormat.QR_CODE,
                ancho,
                alto,
                hints
        );

        Path dir = Paths.get("qr");
        if (!Files.exists(dir)) {
            Files.createDirectory(dir);
        }

        String filename = "qr-" + sucursal + ".png";
        Path filePath = dir.resolve(filename);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

        return codigoQrRepository.save(sucursal);
    }

    @Override
    public String readQr(MultipartFile file) throws IOException {
        try{
            BufferedImage image = ImageIO.read(file.getInputStream());
            if(image == null) throw new ReadingQrException();
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result resultado = new MultiFormatReader().decode(binaryBitmap);
            return resultado.getText();
        } catch(ReadingQrException | NotFoundException e){
            throw new ReadingQrException();
        }
    }

    @Override
    public void deleteCodigo(Sucursal sucursal) {
        try{
            CodigoQR codigoQR = codigoQrRepository.findBySucursal(sucursal);
            if(codigoQR == null){
                throw new QrCodeNotFoundException();
            }
            codigoQrRepository.deleteBySucursal(sucursal);
        } catch(QrCodeNotFoundException e){
            throw new QrCodeNotFoundException();
        }
    }

}