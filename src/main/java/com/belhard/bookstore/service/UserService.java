package com.belhard.bookstore.service;

import com.belhard.bookstore.service.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();
    UserDto getByEmail (String email);
    UserDto getById (Long id);
    UserDto create (UserDto dto);
    UserDto update (UserDto dto);
    void delete(Long id);
    UserDto login(String email, String password);
}