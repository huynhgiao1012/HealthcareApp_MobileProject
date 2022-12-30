package com.healthcareapp.healthcareappbackend.service;

import com.healthcareapp.healthcareappbackend.converter.AccountMapper;
import com.healthcareapp.healthcareappbackend.dto.AccountDto;
import com.healthcareapp.healthcareappbackend.entity.AccountEntity;
import com.healthcareapp.healthcareappbackend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    public AccountDto findAccountByIdCard(String idCard) {
        Optional<AccountEntity> account = accountRepository.findByIdCard(idCard);

        if (account.isEmpty()) {
            return null;
        }

        AccountEntity accountEntity = account.get();

        return accountMapper.INSTANCE.accountEntityToAccountDto(accountEntity);
    }

    public AccountDto save(AccountDto accountDto) {
        // Already registered with this idCard
        if (accountRepository.findByIdCard(accountDto.getIdCard()).isPresent()) {
            return null;
        }

        // Create new record
        AccountEntity accountEntity = accountMapper.INSTANCE.accountDtoToAccountEntity(accountDto);
        accountEntity = accountRepository.save(accountEntity);

        return accountMapper.INSTANCE.accountEntityToAccountDto(accountEntity);
    }

    public AccountDto updateInfo(AccountDto accountDto) {
        // Already registered with this idCard
        if (!accountRepository.findByIdCard(accountDto.getIdCard()).isPresent()) {
            return null;
        }

        // Create new record
        AccountEntity accountEntity = accountRepository.findByIdCard(accountDto.getIdCard()).get();
        if (accountDto.getPassword() != "") {
            accountEntity.setPassword(accountDto.getPassword());
        }
        if (accountDto.getPhone() != "") {
            accountEntity.setPhone(accountDto.getPhone());
        }
        if (accountDto.getName() != "") {
            accountEntity.setName(accountDto.getName());
        }
        accountEntity = accountRepository.save(accountEntity);

        return accountMapper.INSTANCE.accountEntityToAccountDto(accountEntity);
    }

    public AccountDto forgotPassword(AccountDto accountDto) {
        // No idCard
        if (!accountRepository.findByIdCard(accountDto.getIdCard()).isPresent()) {
            return null;
        }

        AccountEntity oldAccount = accountRepository.findByIdCard(accountDto.getIdCard()).get();
        oldAccount.setPassword(accountDto.getPassword());

        return accountMapper.INSTANCE.accountEntityToAccountDto(accountRepository.save(oldAccount));
    }

    public List<AccountDto> listDoctors() {
        List<AccountDto> listDoctor = new ArrayList<>();
        List<AccountEntity> listDoctorEntity = accountRepository.findListOfDoctor();

        for(AccountEntity doctor: listDoctorEntity) {
            listDoctor.add(accountMapper.INSTANCE.accountEntityToAccountDto(doctor));
        }
        return listDoctor;
    }
}
