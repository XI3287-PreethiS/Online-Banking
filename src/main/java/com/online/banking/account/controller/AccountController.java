package com.online.banking.account.controller;

import com.online.banking.account.dto.AccountRequestDto;
import com.online.banking.account.dto.AccountResponseDto;
import com.online.banking.account.service.AccountService;
import com.online.banking.account.util.ConstantUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountRequestDto requestDto) {
        AccountResponseDto response = accountService.createAccount(requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<String> changeAccountType(@RequestParam("change-type") String changeType, @RequestParam("id") long id) {
        accountService.changeAccountType(id, changeType);
        return ResponseEntity.ok(ConstantUtil.ACCOUNT_TYPE_CHANGED_SUCCESS);
    }

    @PatchMapping("/activate-deactivate/{id}")
    public ResponseEntity<String> activateDeactivateAccount(@PathVariable Long id, @RequestParam Boolean isActive) {
        accountService.activateDeactivateAccount(id, isActive);
        return ResponseEntity.ok(isActive ? ConstantUtil.ACCOUNT_ACTIVATED_SUCCESS : ConstantUtil.ACCOUNT_DEACTIVATED_SUCCESS);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<AccountResponseDto>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "accountNumber") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<AccountResponseDto> accounts = accountService.getAllAccounts(page, size, sortBy, direction);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<AccountResponseDto> getAccountByUserId(@PathVariable long userId) {
        return ResponseEntity.ok(accountService.getAccountByUserId(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AccountResponseDto>> searchAccounts(@RequestParam String accountNumberPrefix) {
        return ResponseEntity.ok(accountService.searchAccounts(accountNumberPrefix));
    }
}
