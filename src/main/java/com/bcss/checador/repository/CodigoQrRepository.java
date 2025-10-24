package com.bcss.checador.repository;

import com.bcss.checador.domain.CodigoQR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodigoQrRepository extends JpaRepository<CodigoQR, Integer> {
    void deleteBySucursal(Sucursal sucursal);
}
