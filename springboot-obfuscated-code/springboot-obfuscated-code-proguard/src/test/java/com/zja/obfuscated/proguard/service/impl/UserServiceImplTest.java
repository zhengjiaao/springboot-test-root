package com.zja.obfuscated.proguard.service.impl;

import com.zja.obfuscated.proguard.dao.UserRepo;
import com.zja.obfuscated.proguard.mapper.UserMapper;
import com.zja.obfuscated.proguard.model.dto.PageData;
import com.zja.obfuscated.proguard.model.dto.UserDTO;
import com.zja.obfuscated.proguard.model.entity.User;
import com.zja.obfuscated.proguard.model.request.UserPageSearchRequest;
import com.zja.obfuscated.proguard.model.request.UserRequest;
import com.zja.obfuscated.proguard.model.request.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * squaretest使用:
 * 1.先运行一下根据错误进行修改
 * 2.被测试类一般要加上 @spy
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo mockRepo;
    @Mock
    private UserMapper mockMapper;

    @InjectMocks
    @Spy
    private UserServiceImpl userServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        userServiceImplUnderTest = new UserServiceImpl();
        userServiceImplUnderTest.repo = mockRepo;
        userServiceImplUnderTest.mapper = mockMapper;
    }

    @Test
    void testFindById() {
        // Setup
        final UserDTO expectedResult = new UserDTO();
        expectedResult.setId("id");

        // Configure UserRepo.findById(...).
        final User user1 = new User();
        user1.setName("name");
        user1.setRemarks("remarks");
        user1.setSort(0L);
        user1.setCreateTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        user1.setLastModifiedDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final Optional<User> user = Optional.of(user1);
        when(mockRepo.findById("id")).thenReturn(user);

        // Configure UserMapper.map(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId("id");
        when(mockMapper.map(any(User.class))).thenReturn(userDTO);

        // Run the test
        final UserDTO result = userServiceImplUnderTest.findById("id");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFindById_UserRepoReturnsAbsent() {
        // Setup
        when(mockRepo.findById("id")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userServiceImplUnderTest.findById("id")).isInstanceOf(RuntimeException.class);
    }

    @Test
    void testPageList() {
        // Setup
        final UserPageSearchRequest request = new UserPageSearchRequest();
        request.setPage(1);
        request.setSize(1);
        request.setName("name");

        final PageData<UserDTO> expectedResult = new PageData<>();
        expectedResult.setData(Arrays.asList());
        expectedResult.setIndex(0);
        expectedResult.setSize(0);
        expectedResult.setLength(0);
        expectedResult.setCount(0L);

        // Configure UserRepo.findAll(...).
        final User user = new User();
        user.setName("name");
        user.setRemarks("remarks");
        user.setSort(0L);
        user.setCreateTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        user.setLastModifiedDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final Page<User> users = new PageImpl<>(Arrays.asList(user));
        when(mockRepo.findAll(any(Specification.class), eq(PageRequest.of(0, 1)))).thenReturn(users);

        // Configure UserMapper.mapList(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId("id");
        final List<UserDTO> userDTOS = Arrays.asList(userDTO);
        final User user1 = new User();
        user1.setName("name");
        user1.setRemarks("remarks");
        user1.setSort(0L);
        user1.setCreateTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        user1.setLastModifiedDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final List<User> entityList = Arrays.asList(user1);
        when(mockMapper.mapList(entityList)).thenReturn(userDTOS);

        // Run the test
        final PageData<UserDTO> result = userServiceImplUnderTest.pageList(request);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testPageList_UserRepoReturnsNoItems() {
        // Setup
        final UserPageSearchRequest request = new UserPageSearchRequest();
        request.setPage(1);
        request.setSize(1);
        request.setName("name");

        final PageData<UserDTO> expectedResult = new PageData<>();
        expectedResult.setData(Arrays.asList());
        expectedResult.setIndex(0);
        expectedResult.setSize(0);
        expectedResult.setLength(0);
        expectedResult.setCount(0L);

        when(mockRepo.findAll(any(Specification.class), eq(PageRequest.of(0, 1))))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        // Configure UserMapper.mapList(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId("id");
        final List<UserDTO> userDTOS = Arrays.asList(userDTO);
        final User user = new User();
        user.setName("name");
        user.setRemarks("remarks");
        user.setSort(0L);
        user.setCreateTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        user.setLastModifiedDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final List<User> entityList = Arrays.asList(user);
        when(mockMapper.mapList(entityList)).thenReturn(userDTOS);

        // Run the test
        final PageData<UserDTO> result = userServiceImplUnderTest.pageList(request);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testPageList_UserMapperReturnsNoItems() {
        // Setup
        final UserPageSearchRequest request = new UserPageSearchRequest();
        request.setPage(1);
        request.setSize(1);
        request.setName("name");

        final PageData<UserDTO> expectedResult = new PageData<>();
        expectedResult.setData(Arrays.asList());
        expectedResult.setIndex(0);
        expectedResult.setSize(0);
        expectedResult.setLength(0);
        expectedResult.setCount(0L);

        // Configure UserRepo.findAll(...).
        final User user = new User();
        user.setName("name");
        user.setRemarks("remarks");
        user.setSort(0L);
        user.setCreateTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        user.setLastModifiedDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final Page<User> users = new PageImpl<>(Arrays.asList(user));
        when(mockRepo.findAll(any(Specification.class), eq(PageRequest.of(0, 1)))).thenReturn(users);

        // Configure UserMapper.mapList(...).
        final User user1 = new User();
        user1.setName("name");
        user1.setRemarks("remarks");
        user1.setSort(0L);
        user1.setCreateTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        user1.setLastModifiedDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final List<User> entityList = Arrays.asList(user1);
        when(mockMapper.mapList(entityList)).thenReturn(Collections.emptyList());

        // Run the test
        final PageData<UserDTO> result = userServiceImplUnderTest.pageList(request);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testAdd() {
        // Setup
        final UserRequest request = new UserRequest();
        request.setName("name");

        final UserDTO expectedResult = new UserDTO();
        expectedResult.setId("id");

        // Configure UserMapper.map(...).
        final User user = new User();
        user.setName("name");
        user.setRemarks("remarks");
        user.setSort(0L);
        user.setCreateTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        user.setLastModifiedDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final UserRequest request1 = new UserRequest();
        request1.setName("name");
        when(mockMapper.map(request1)).thenReturn(user);

        // Configure UserMapper.map(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId("id");
        when(mockMapper.map(any(User.class))).thenReturn(userDTO);

        // Run the test
        final UserDTO result = userServiceImplUnderTest.add(request);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
        verify(mockRepo).save(any(User.class));
    }

    @Test
    void testUpdate() {
        // Setup
        final UserUpdateRequest request = new UserUpdateRequest();
        request.setName("name");

        final UserDTO expectedResult = new UserDTO();
        expectedResult.setId("id");

        // Configure UserRepo.findById(...).
        final User user1 = new User();
        user1.setName("name");
        user1.setRemarks("remarks");
        user1.setSort(0L);
        user1.setCreateTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        user1.setLastModifiedDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final Optional<User> user = Optional.of(user1);
        when(mockRepo.findById("id")).thenReturn(user);

        // Configure UserRepo.save(...).
        final User user2 = new User();
        user2.setName("name");
        user2.setRemarks("remarks");
        user2.setSort(0L);
        user2.setCreateTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        user2.setLastModifiedDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        when(mockRepo.save(any(User.class))).thenReturn(user2);

        // Configure UserMapper.map(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId("id");
        when(mockMapper.map(any(User.class))).thenReturn(userDTO);

        // Run the test
        final UserDTO result = userServiceImplUnderTest.update("id", request);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testUpdate_UserRepoFindByIdReturnsAbsent() {
        // Setup
        final UserUpdateRequest request = new UserUpdateRequest();
        request.setName("name");

        when(mockRepo.findById("id")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userServiceImplUnderTest.update("id", request)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void testDeleteById() {
        // Setup
        // Configure UserRepo.findById(...).
        final User user1 = new User();
        user1.setName("name");
        user1.setRemarks("remarks");
        user1.setSort(0L);
        user1.setCreateTime(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        user1.setLastModifiedDate(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        final Optional<User> user = Optional.of(user1);
        when(mockRepo.findById("id")).thenReturn(user);

        // Run the test
        userServiceImplUnderTest.deleteById("id");

        // Verify the results
        verify(mockRepo).deleteById("id");
    }

    @Test
    void testDeleteById_UserRepoFindByIdReturnsAbsent() {
        // Setup
        when(mockRepo.findById("id")).thenReturn(Optional.empty());

        // Run the test
        userServiceImplUnderTest.deleteById("id");

        // Verify the results
    }
}
