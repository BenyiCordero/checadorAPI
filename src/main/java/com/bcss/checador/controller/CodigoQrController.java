package com.bcss.checador.controller;

import com.bcss.checador.service.CodigoQrService;
import com.google.zxing.WriterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/qr")
public class CodigoQrController {

    private final CodigoQrService codigoQrService;

    public CodigoQrController(CodigoQrService codigoQrService) {
        this.codigoQrService = codigoQrService;
    }

    @PostMapping
    public ResponseEntity<?> generateQr(Sucursal sucursal) throws IOException, WriterException {
        codigoQrService.generate(sucursal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
