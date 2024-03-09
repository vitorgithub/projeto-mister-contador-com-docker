package com.vitorgithub.pdfextractapi.controller;

import com.vitorgithub.pdfextractapi.application.usecases.IReadAndSaveTransactionsFromPDFService;
import com.vitorgithub.pdfextractapi.crossCutting.ResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "File Upload Controller", description = "Controlador para upload e processamento de arquivos PDF contendo extratos bancários")
@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final IReadAndSaveTransactionsFromPDFService readAndSaveTransactionsFromPDFService;

    public FileUploadController(IReadAndSaveTransactionsFromPDFService readAndSaveTransactionsFromPDFService) {
        this.readAndSaveTransactionsFromPDFService = readAndSaveTransactionsFromPDFService;
    }

    @ApiOperation(value = "Upload e processamento de arquivo PDF", notes = "Recebe um arquivo PDF para extrair transações bancárias")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Arquivo processado com sucesso"),
            @ApiResponse(code = 400, message = "Requisição inválida"),
            @ApiResponse(code = 500, message = "Erro interno no servidor")
    })
    @PostMapping("/upload")
    public ResponseEntity<ResponseModel> uploadFile(@ApiParam(value = "Arquivo PDF contendo o extrato bancário", required = true) @RequestParam("file") MultipartFile file) {
        ResponseModel result = readAndSaveTransactionsFromPDFService.uploadAndProcessFile(file);
        return new ResponseEntity<>(result, result.getHttpStatus());
    }
}
