package com.bcss.checador.controller;

import com.bcss.checador.service.CodigoQrService;
import com.google.zxing.WriterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/qr")
public class CodigoQrController {

    private final CodigoQrService codigoQrService;

    public CodigoQrController(CodigoQrService codigoQrService) {
        this.codigoQrService = codigoQrService;
    }

    @PostMapping
    public ResponseEntity<?> generateQr(@RequestBody Sucursal sucursal) throws IOException, WriterException {
        codigoQrService.generate(sucursal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteQr(@RequestBody Sucursal sucursal){
        codigoQrService.deleteCodigo(sucursal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
