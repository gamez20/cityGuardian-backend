package co.edu.uniquindio.cityguardian.services;

import co.edu.uniquindio.cityguardian.dto.EmailDTO;

public interface EmailService {
    void enviarEmail(EmailDTO emailDTO) throws Exception;
}