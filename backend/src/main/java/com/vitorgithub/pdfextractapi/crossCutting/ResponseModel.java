package com.vitorgithub.pdfextractapi.crossCutting;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseModel {
    private Object data;
    private HttpStatus httpStatus;
}
