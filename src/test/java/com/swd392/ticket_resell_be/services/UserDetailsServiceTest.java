package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.dtos.responses.UserDetailsResponse;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import com.swd392.ticket_resell_be.services.impls.UserDetailsServiceImplement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {
    @InjectMocks
    UserDetailsServiceImplement userDetailsServiceImplement;
    @Mock
    UserRepository userRepository;
    @Mock
    ModelMapper modelMapper;

    @Test
    void testLoadUserByUsername_Success() {
        //given
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("test")
                .password("12345678")
                .email("email@gmail.com")
                .status(Categorize.ACTIVE)
                .role(Categorize.ADMIN)
                .typeRegister(Categorize.SYSTEM)
                .build();
        UserDetailsResponse userDetailsResponse =
                new UserDetailsResponse("test", "12345678", Categorize.ACTIVE, Categorize.ADMIN);
        //when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDetailsResponse.class)).thenReturn(userDetailsResponse);
        UserDetails response = userDetailsServiceImplement.loadUserByUsername("test");
        //then
        assertEquals(userDetailsResponse, response);
    }

    @Test
    void testLoadUserByUsername_Fail() {
        //given
        //when
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        //then
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsServiceImplement.loadUserByUsername("test"),
                "USER_NOT_FOUND");
    }

}
