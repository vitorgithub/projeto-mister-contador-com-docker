package com.vitorgithub.pdfextractapi.application.usecases;

import com.vitorgithub.pdfextractapi.crossCutting.ResponseModel;
import org.springframework.web.multipart.MultipartFile;


public interface IReadAndSaveTransactionsFromPDFService {
    ResponseModel uploadAndProcessFile(MultipartFile file);
}