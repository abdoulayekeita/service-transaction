package com.servicetransaction.api;


import com.servicetransaction.dto.RequestTransDto;
import com.servicetransaction.entities.Response;
import com.servicetransaction.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/app/save")
    public ResponseEntity<Response> saveUser() throws IOException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/app/save").toUriString());
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("app",transactionService.verifyIappExist("zzvqbvzicmaaewmxfmro", "live_sbzdwgalgkvjzwxvvltw"));
        return ResponseEntity
                .created(uri)
                .body(
                        Response.builder()
                                .timeStamp(now())
                                .message("App registred")
                                .data(userMap)
                                .status(OK)
                                .statusCode(OK.value())
                                .build()
                );
    }

    @PostMapping("/app/account/credit")
    public ResponseEntity<Response> creditAccount(@RequestBody RequestTransDto transDto, HttpServletRequest request) throws IOException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/app/account/credit").toUriString());
        return ResponseEntity
                .created(uri)
                .body(
                        Response.builder()
                                .timeStamp(now())
                                .message("Compte credité avec succès")
                                .data(transactionService.credit(transDto, request))
                                .status(OK)
                                .statusCode(OK.value())
                                .build()
                );
    }

    @PostMapping("/app/account/debit")
    public ResponseEntity<Response> debitAccount(@RequestBody RequestTransDto transDto, HttpServletRequest request) throws IOException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/app/account/debit").toUriString());
        return ResponseEntity
                .created(uri)
                .body(
                        Response.builder()
                                .timeStamp(now())
                                .message("Compte débité avec succès")
                                .data(transactionService.debit(transDto, request))
                                .status(OK)
                                .statusCode(OK.value())
                                .build()
                );
    }
}
