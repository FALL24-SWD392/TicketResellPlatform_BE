package com.swd392.ticket_resell_be.utils;

import com.nimbusds.jose.JOSEException;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.test_utils.SetInitField;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class TokenUtilTest {
    @InjectMocks
    TokenUtil tokenUtil;

    @Test
    void testGenerateToken() throws NoSuchFieldException, IllegalAccessException, JOSEException {
        //given
        User user = new User();
        user.setUsername("username");
        SetInitField.setField(tokenUtil, "secretKey",
                "stoX9OQuI1RO4tpPak9rXRzILbdHAQWYJwXjpesuG5oRS689CmoT+wzI/X5quFCi");
        //when
        String token = tokenUtil.generateToken(user);
        //then
        assertNotNull(token);
    }
}
