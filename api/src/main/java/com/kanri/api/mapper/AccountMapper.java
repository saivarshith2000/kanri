package com.kanri.api.mapper;


import com.kanri.api.dto.account.AccountDTO;
import com.kanri.api.entity.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDTO toAccountDTO(Account account);
}
