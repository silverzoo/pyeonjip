package com.team5.pyeonjip.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.team5.pyeonjip.user.dto.SignUpDto;
import com.team5.pyeonjip.user.dto.UserUpdateDto;
import com.team5.pyeonjip.user.entity.User;
import com.team5.pyeonjip.user.repository.UserRepository;
import com.team5.pyeonjip.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;


    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        userRepository.deleteAll();
    }


    /* Create */

    @DisplayName("createUser: 유저 추가")
    @Test
    public void createUser() throws Exception {

        // given
        final String url = "/api/user/new";
        final String email = "test@test.com";
        final String name = "test";
        final String phoneNum = "01012345678";
        final String password = "1234";
        final String address = "test address";
        final String passwordHint = "blue";

        // 유저 생성
        SignUpDto dto = new SignUpDto();
        dto.setEmail(email);
        dto.setName(name);
        dto.setPhoneNumber(phoneNum);
        dto.setPassword(password);
        dto.setAddress(address);
        dto.setPasswordHint(passwordHint);


        //when
        String requestBody = objectMapper.writeValueAsString(dto);


        // then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());

    }


    /* Update */

    @DisplayName("updateUserInfo: 유저 정보 주소, 비밀번호 힌트 업데이트")
    @Test
    public void updateUserInfoBoth() throws Exception {
        // given
        final String url = "/api/user/update/{id}";
        final String email = "test@test.com";
        final String name = "test";
        final String phoneNum = "01012345678";
        final String password = "1234";
        final String address = "test address";
        final String passwordHint = "blue";

        // 유저 생성
        SignUpDto createDto = new SignUpDto();
        createDto.setEmail(email);
        createDto.setName(name);
        createDto.setPhoneNumber(phoneNum);
        createDto.setPassword(password);
        createDto.setAddress(address);
        createDto.setPasswordHint(passwordHint);
        userService.createUser(createDto);

        User savedUser = userRepository.findAll().get(0);


        final String newAddress = "new address";
        final String newPasswordHint = "green";
        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setAddress(newAddress);
        updateDto.setPasswordHint(newPasswordHint);

        String requestBody = objectMapper.writeValueAsString(updateDto);

        // when & then
        mockMvc.perform(put(url, savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();
        assertEquals(newAddress, updatedUser.getAddress());
        assertEquals(newPasswordHint, updatedUser.getPasswordHint());
    }


    @DisplayName("updateUserInfo: 유저 정보 주소 업데이트")
    @Test
    public void updateUserInfoAddress() throws Exception {
        // given
        final String url = "/api/user/update/{id}";
        final String email = "test@test.com";
        final String name = "test";
        final String phoneNum = "01012345678";
        final String password = "1234";
        final String address = "test address";
        final String passwordHint = "blue";

        // 유저 생성
        SignUpDto createDto = new SignUpDto();
        createDto.setEmail(email);
        createDto.setName(name);
        createDto.setPhoneNumber(phoneNum);
        createDto.setPassword(password);
        createDto.setAddress(address);
        createDto.setPasswordHint(passwordHint);
        userService.createUser(createDto);

        User savedUser = userRepository.findAll().get(0);


        final String newAddress = "new address";
        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setAddress(newAddress);

        String requestBody = objectMapper.writeValueAsString(updateDto);

        // when & then
        mockMvc.perform(put(url, savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();
        assertEquals(newAddress, updatedUser.getAddress());

        // passwordHint는 기존과 동일해야 함.
        assertEquals(passwordHint, updatedUser.getPasswordHint());
    }


    @DisplayName("updateUserInfo: 유저 정보 비밀번호 힌트 업데이트")
    @Test
    public void updateUserInfoPasswordHint() throws Exception {
        // given
        final String url = "/api/user/update/{id}";
        final String email = "test@test.com";
        final String name = "test";
        final String phoneNum = "01012345678";
        final String password = "1234";
        final String address = "test address";
        final String passwordHint = "blue";

        // 유저 생성
        SignUpDto createDto = new SignUpDto();
        createDto.setEmail(email);
        createDto.setName(name);
        createDto.setPhoneNumber(phoneNum);
        createDto.setPassword(password);
        createDto.setAddress(address);
        createDto.setPasswordHint(passwordHint);
        userService.createUser(createDto);

        User savedUser = userRepository.findAll().get(0);


        final String newPasswordHint = "green";
        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setPasswordHint(newPasswordHint);

        String requestBody = objectMapper.writeValueAsString(updateDto);

        // when & then
        mockMvc.perform(put(url, savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();

        // address는 기존과 동일해야 함.
        assertEquals(address, updatedUser.getAddress());
        assertEquals(newPasswordHint, updatedUser.getPasswordHint());
    }


    /* Read */

    @DisplayName("getAllUsers: 전체 유저 조회")
    @Test
    public void getAllUsers() throws Exception {
        // given
        final String url = "/api/user";
        final String email = "test@test.com";
        final String name = "test";
        final String phoneNum = "01012345678";
        final String password = "1234";
        final String address = "test address";
        final String passwordHint = "blue";

        // 유저 생성
        SignUpDto createDto = new SignUpDto();
        createDto.setEmail(email);
        createDto.setName(name);
        createDto.setPhoneNumber(phoneNum);
        createDto.setPassword(password);
        createDto.setAddress(address);
        createDto.setPasswordHint(passwordHint);
        userService.createUser(createDto);

        // when & then
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @DisplayName("getUser: 특정 유저 조회")
    @Test
    public void getUser() throws Exception {
        // given
        final String url = "/api/user/{id}";
        final String email = "test@test.com";
        final String name = "test";
        final String phoneNum = "01012345678";
        final String password = "1234";
        final String address = "test address";
        final String passwordHint = "blue";

        // 유저 생성
        SignUpDto createDto = new SignUpDto();
        createDto.setEmail(email);
        createDto.setName(name);
        createDto.setPhoneNumber(phoneNum);
        createDto.setPassword(password);
        createDto.setAddress(address);
        createDto.setPasswordHint(passwordHint);
        userService.createUser(createDto);

        User savedUser = userRepository.findAll().get(0);

        // when & then
        mockMvc.perform(get(url, savedUser.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    /* Delete */

    @DisplayName("deleteUser: 유저 삭제")
    @Test
    public void deleteUser() throws Exception {
        // given
        final String url = "/api/user/delete/{id}";
        final String email = "test@test.com";
        final String name = "test";
        final String phoneNum = "01012345678";
        final String password = "1234";
        final String address = "test address";
        final String passwordHint = "blue";

        // 유저 생성
        SignUpDto createDto = new SignUpDto();
        createDto.setEmail(email);
        createDto.setName(name);
        createDto.setPhoneNumber(phoneNum);
        createDto.setPassword(password);
        createDto.setAddress(address);
        createDto.setPasswordHint(passwordHint);
        userService.createUser(createDto);

        User savedUser = userRepository.findAll().get(0);

        // when & then
        mockMvc.perform(delete(url, savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(0, userRepository.count());
    }

}
