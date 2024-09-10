package com.online.banking.account.controller;

import com.online.banking.account.dto.AccountRequestDto;
import com.online.banking.account.dto.AccountResponseDto;
import com.online.banking.account.service.AccountService;
import com.online.banking.account.util.ConstantUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    private AccountRequestDto requestDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();

        requestDto = new AccountRequestDto();
        requestDto.setAccountType("savings");
        requestDto.setUserId(1L);
        requestDto.setBalance(new BigDecimal("5000.00"));
    }

    @Test
    public void testCreateAccount() throws Exception {
        when(accountService.createAccount(any(AccountRequestDto.class))).thenReturn(new AccountResponseDto());

        mockMvc.perform(post("/api/v1/account/create")
                        .contentType(APPLICATION_JSON)
                        .content("{\"accountType\": \"savings\", \"userId\": 1, \"balance\": 5000.00}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteAccount() throws Exception {
        long accountId = 1L;

        doNothing().when(accountService).deleteAccount(accountId);

        mockMvc.perform(delete("/api/v1/account/delete/{id}", accountId))
                .andExpect(status().isNoContent());

        verify(accountService, times(1)).deleteAccount(accountId);
    }


    @Test
    void testChangeAccountType() throws Exception {
        long accountId = 1L;
        String changeType = "current";

        // Mock the service method to perform its task
        when(accountService.changeAccountType(accountId, changeType)).thenReturn(null);

        mockMvc.perform(put("/api/v1/account/change-account-type")
                        .param("change-type", changeType)
                        .param("id", String.valueOf(accountId)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)) // Ignore charset
                .andExpect(MockMvcResultMatchers.content().string(ConstantUtil.ACCOUNT_TYPE_CHANGED_SUCCESS));

        verify(accountService, times(1)).changeAccountType(accountId, changeType);
    }


    @Test
    void testActivateDeactivateAccount() throws Exception {
        long accountId = 1L;
        Boolean isActive = true;

        doNothing().when(accountService).activateDeactivateAccount(accountId, isActive);

        mockMvc.perform(patch("/api/v1/account/activate-deactivate/{id}", accountId)
                        .param("isActive", String.valueOf(isActive)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))  // Expect JSON content
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ConstantUtil.ACCOUNT_ACTIVATED_SUCCESS));  // Check the JSON message

        verify(accountService, times(1)).activateDeactivateAccount(accountId, isActive);
    }



    @Test
    void testGetAllAccounts() throws Exception {
        int page = 0;
        int size = 10;
        String sortBy = "accountNumber";
        String direction = "asc";

        List<AccountResponseDto> accountList = Collections.singletonList(new AccountResponseDto());
        Page<AccountResponseDto> accountPage = new PageImpl<>(accountList, PageRequest.of(page, size), 1);

        when(accountService.getAllAccounts(page, size, sortBy, direction)).thenReturn(accountPage);

        mockMvc.perform(get("/api/v1/account/all")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy)
                        .param("direction", direction))
                .andExpect(status().isOk());

        verify(accountService, times(1)).getAllAccounts(page, size, sortBy, direction);
    }


    @Test
    void testGetAccountById() throws Exception {
        long accountId = 1L;
        AccountResponseDto accountResponse = new AccountResponseDto();
        // Set up the expected account response

        when(accountService.getAccountById(accountId)).thenReturn(accountResponse);

        mockMvc.perform(get("/api/v1/account/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON));
        // Add further JSON content assertions here as needed

        verify(accountService, times(1)).getAccountById(accountId);
    }


    @Test
    void testGetAccountByUserId() throws Exception {
        long userId = 1L;

        // Assuming the accountResponse is a proper DTO
        AccountResponseDto accountResponse = new AccountResponseDto();
        accountResponse.setUserId(userId);
        accountResponse.setAccountNumber("12345678901");  // setting some data for validation

        // Mocking the service to return this response when the method is called
        when(accountService.getAccountByUserId(userId)).thenReturn(accountResponse);

        // Performing the mock request and checking the result
        mockMvc.perform(get("/api/v1/account/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON));

        // Verifying that the service method was called once with the expected userId
        verify(accountService, times(1)).getAccountByUserId(userId);
    }



    @Test
    void testSearchAccounts() throws Exception {
        String accountNumberPrefix = "12345";
        List<AccountResponseDto> accountList = Collections.singletonList(new AccountResponseDto());

        when(accountService.searchAccounts(accountNumberPrefix)).thenReturn(accountList);

        mockMvc.perform(get("/api/v1/account/search")  // Fixed URL path
                        .param("accountNumberPrefix", accountNumberPrefix))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON));

        verify(accountService, times(1)).searchAccounts(accountNumberPrefix);
    }

}


