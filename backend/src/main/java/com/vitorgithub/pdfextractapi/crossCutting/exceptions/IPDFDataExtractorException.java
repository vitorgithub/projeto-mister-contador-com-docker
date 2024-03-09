package com.vitorgithub.pdfextractapi.crossCutting.exceptions;

public interface IPDFDataExtractorException {

    String PDFExtractionException(String pdfPath) throws PDFExtractionException;
}
