package com.vitorgithub.pdfextractapi.crossCutting.exceptions;

/**
 * Exceção específica para erros de extração de dados de PDF.
 */
public class PDFExtractionException extends Exception {

    public PDFExtractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
