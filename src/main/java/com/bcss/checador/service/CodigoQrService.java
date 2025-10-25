package com.bcss.checador.service;

import com.bcss.checador.domain.CodigoQR;
import com.google.zxing.WriterException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CodigoQrService {
    void generate(Sucursal sucursal) throws IOException, WriterException;
    String readQr(MultipartFile file) throws IOException;
    void deleteCodigo(Sucursal sucursal);
}