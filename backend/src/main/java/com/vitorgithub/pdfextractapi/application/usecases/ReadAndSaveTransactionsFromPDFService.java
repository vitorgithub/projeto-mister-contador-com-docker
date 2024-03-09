package com.vitorgithub.pdfextractapi.application.usecases;

import com.vitorgithub.pdfextractapi.application.usecases.IReadAndSaveTransactionsFromPDFService;
import com.vitorgithub.pdfextractapi.crossCutting.GetResponseModel;
import com.vitorgithub.pdfextractapi.crossCutting.ResponseModel;
import com.vitorgithub.pdfextractapi.domain.transaction.model.Transaction;
import com.vitorgithub.pdfextractapi.infrastructure.repository.TransactionRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NonUniqueResultException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ReadAndSaveTransactionsFromPDFService implements IReadAndSaveTransactionsFromPDFService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private TransactionRepository repository;

    @Override
    public ResponseModel uploadAndProcessFile(MultipartFile file) {
        if (file.isEmpty()) {
            return GetResponseModel.badRequest("The file is empty.");
        }

        if (!"application/pdf".equals(file.getContentType())) {
            return GetResponseModel.badRequest("Only PDF files are supported.");
        }

        try {
            File pdfFile = convertMultiPartToFile(file);
            List<String> extractedTextLines = extractText(pdfFile);
            List<Transaction> transactions = parseTransactions(extractedTextLines);

            List<Transaction> uniqueTransactions = filterUniqueTransactions(transactions);

            if (!uniqueTransactions.isEmpty()) {
                repository.saveAll(uniqueTransactions);
                return GetResponseModel.created(uniqueTransactions);
            } else {
                return GetResponseModel.badRequest("No unique transactions found or invalid transaction order detected.");
            }
        }
        catch (NonUniqueResultException e)
        {
            return GetResponseModel.badRequest("Some registers are already on database");
        }
        catch (IncorrectResultSizeDataAccessException e)
        {
            return GetResponseModel.badRequest("Some registers are already on database");
        }
        catch (MaxUploadSizeExceededException e)
        {
            return GetResponseModel.unsupportedMediaType("Maximun size of file exceed");
        }
        catch (IllegalStateException e)
        {
            return GetResponseModel.unsupportedMediaType("Maximun size of file exceed");
        }
        catch (IOException e)
        {
            return GetResponseModel.unsupportedMediaType("An error occurred while processing the file.");
        }
        catch (Exception e)
        {
            return GetResponseModel.internalServerError("Erro desconhecido: com.vitorgithub.pdfextractapi.application.usecases.ReadAndSaveTransactions Line 62 ");
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile(null, ".pdf");
        file.transferTo(tempFile.toFile());
        return tempFile.toFile();
    }

    private List<String> extractText(File pdfFile) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return List.of(text.split("\\r?\\n"));
        }
    }

    private List<Transaction> parseTransactions(List<String> lines) {
        List<Transaction> transactions = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\d{2}/\\d{2}/\\d{4})(.*?)([\\-]?\\d{1,3}(\\.\\d{3})*,\\d{2})\\s+([\\-]?\\d{1,3}(\\.\\d{3})*,\\d{2})");

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                Transaction transaction = createTransaction(matcher, line);
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    private List<Transaction> filterUniqueTransactions(List<Transaction> transactions) {
        List<Transaction> uniqueTransactions = new ArrayList<>();
        transactions.forEach(transaction -> {
            if (repository.findByDateAndDocumentNumberAndAmount(transaction.getDate(), transaction.getDocumentNumber(), transaction.getAmount()).isEmpty()) {
                uniqueTransactions.add(transaction);
            }
        });
        return uniqueTransactions;
    }

    private Transaction createTransaction(Matcher matcher, String line) {
        LocalDate date = LocalDate.parse(matcher.group(1), DATE_FORMAT);
        String documentNumber = matcher.group(2).trim().split("\\s+")[0];
        String history = line.substring(line.indexOf(documentNumber) + documentNumber.length()).trim();
        history = history.substring(0, history.lastIndexOf(matcher.group(3))).trim();
        BigDecimal amount = new BigDecimal(matcher.group(3).replace(".", "").replace(",", "."));
        BigDecimal balance = new BigDecimal(matcher.group(5).replace(".", "").replace(",", "."));

        return new Transaction(null, date, documentNumber, history, amount, balance);
    }
}
