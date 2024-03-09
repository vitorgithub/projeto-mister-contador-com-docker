package com.vitorgithub.pdfextractapi.crossCutting;

import org.springframework.http.HttpStatus;

public class GetResponseModel {
    public static ResponseModel created(Object data) {
        return new ResponseModel(data, HttpStatus.CREATED);
    }

    public static ResponseModel badRequest(String Data) {
        String _data = Data == null ? "Formato de requisição inválido" : Data;
        return new ResponseModel(_data, HttpStatus.BAD_REQUEST);
    }

    public static ResponseModel unsupportedMediaType(String Data) {
        String _data = Data == null ? "Formato de arquivo ou requisição inválido." : Data;
        return new ResponseModel(_data, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    public static ResponseModel internalServerError(String Data) {
        String _data = Data == null ? "Um erro desconhecido ocorreu." : Data;
        return new ResponseModel(_data, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
