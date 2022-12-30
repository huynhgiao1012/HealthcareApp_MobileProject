package com.healthcareapp.healthcareappbackend.controller;

import com.healthcareapp.healthcareappbackend.dto.AccountDto;
import com.healthcareapp.healthcareappbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PutMapping(path = "/getAccountByIdCard")
    public AccountDto getAccountByIdCard(@RequestParam String idCard, @RequestParam String password) {
        AccountDto accountDtoInput = new AccountDto();
        accountDtoInput.setIdCard(idCard);
        accountDtoInput.setPassword(password);
        
        AccountDto account = accountService.findAccountByIdCard(accountDtoInput.getIdCard());

        if (!account.getPassword().equals(accountDtoInput.getPassword())) {
            return null;
        }

        return account;
    }

    @PostMapping(path = "/signup")
    public AccountDto signUp(@RequestParam String idCard, @RequestParam String name, @RequestParam String password, @RequestParam String role) {
        AccountDto accountDtoInput = new AccountDto();
        accountDtoInput.setIdCard(idCard);
        accountDtoInput.setName(name);
        accountDtoInput.setPassword(password);
        accountDtoInput.setRole(role);
        accountDtoInput.setActive(true);
        return accountService.save(accountDtoInput);
    }

    @PutMapping(path = "/updateInfo")
    public AccountDto updateInfo(@RequestParam String idCard, @RequestParam(required = false) String name, @RequestParam(required = false) String password, @RequestParam(required = false) String role) {
        AccountDto accountDtoInput = new AccountDto();
        accountDtoInput.setIdCard(idCard);
        accountDtoInput.setName(name);
        accountDtoInput.setPassword(password);
        accountDtoInput.setRole(role);
        accountDtoInput.setActive(true);
        return accountService.updateInfo(accountDtoInput);
    }

    @PutMapping(path = "/forgotPassword")
    public AccountDto forgotPassword(@RequestParam String idCard, @RequestParam String password) {
        AccountDto accountDtoInput = new AccountDto();
        accountDtoInput.setIdCard(idCard);
        accountDtoInput.setPassword(password);

        return accountService.forgotPassword(accountDtoInput);
    }

    @GetMapping(path = "/listOfDoctors")
    public List<AccountDto> listOfDoctors() {
        return accountService.listDoctors();
    }
}
