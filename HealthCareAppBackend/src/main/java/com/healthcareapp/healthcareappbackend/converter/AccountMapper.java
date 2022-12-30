package com.healthcareapp.healthcareappbackend.converter;

import com.healthcareapp.healthcareappbackend.dto.AccountDto;
import com.healthcareapp.healthcareappbackend.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDto accountEntityToAccountDto(AccountEntity account);

    AccountEntity accountDtoToAccountEntity(AccountDto accountDto);
}
