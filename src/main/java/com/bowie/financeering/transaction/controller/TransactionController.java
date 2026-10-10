package com.bowie.financeering.transaction.controller;

import com.bowie.financeering.transaction.dto.TransactionCreateDTO;
import com.bowie.financeering.transaction.dto.TransactionResponseDTO;
import com.bowie.financeering.transaction.dto.TransactionUpdateDTO;
import com.bowie.financeering.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/finance")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @Valid @RequestBody TransactionCreateDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) {

        String userSub = jwt.getSubject();
        TransactionResponseDTO responseDTO = transactionService.createTransaction(dto, userSub);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(
            @AuthenticationPrincipal Jwt jwt
    ) {

        String userSub = jwt.getSubject();
        List<TransactionResponseDTO> responseDTO = transactionService.getAllTransactions(userSub);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransaction(
            @PathVariable long id,
            @AuthenticationPrincipal Jwt jwt
    ) throws AccessDeniedException {

        String userSub = jwt.getSubject();
        TransactionResponseDTO responseDTO = transactionService.getTransactionById(id, userSub);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionUpdateDTO dto,
            @AuthenticationPrincipal Jwt jwt
    ) throws AccessDeniedException {

        String userSub = jwt.getSubject();
        TransactionResponseDTO responseDTO = transactionService.updateTransaction(id, dto, userSub);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> deleteTransaction(
            @PathVariable long id,
            @AuthenticationPrincipal Jwt jwt
    ) throws AccessDeniedException {

        String userSub = jwt.getSubject();
        transactionService.deleteTransaction(id, userSub);

        return ResponseEntity.noContent().build();
    }
}
