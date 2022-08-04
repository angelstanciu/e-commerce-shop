package com.endava.mentorship2022.service;

import com.endava.mentorship2022.exception.UserNotFound;
import com.endava.mentorship2022.model.Role;
import com.endava.mentorship2022.model.User;
import com.endava.mentorship2022.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.endava.mentorship2022.model.UserStatus.ACTIVE;
import static com.endava.mentorship2022.model.UserStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should find all users")
    void findAllUsersTest() {
        // given
        User user1 = new User(
                1L,
                "Stanciu",
                "Angel",
                "angel@gmail.com",
                "pass",
                "Romania, Bucuresti, Strada Gabroveni 030089",
                "+40721058124",
                LocalDate.of(2010, 1, 1),
                PENDING,
                Set.of(new Role("ADMIN")));

        User user2 = new User(
                1L,
                "Stanciu",
                "Angel",
                "angel@gmail.com",
                "pass",
                "Romania, Bucuresti, Strada Gabroveni 030089",
                "+40721058124",
                LocalDate.of(2010, 1, 1),
                PENDING,
                Set.of(new Role("ADMIN")));

        List<User> usersList = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(usersList);

        // when
        List<User> actualUsers = userService.findAll();

        // then
        assertThat(actualUsers).hasSize(2);
        assertThat(usersList).isEqualTo(actualUsers);
    }

    @Test
    @DisplayName("Should find a user by ID")
    void findUserByIdTest() throws UserNotFound {
        // given
        User userToBeFound = new User(
                1L,
                "Stanciu",
                "Angel",
                "angel@gmail.com",
                "pass",
                "Romania, Bucuresti, Strada Gabroveni 030089",
                "+40721058124",
                LocalDate.of(2010, 1, 1),
                PENDING,
                Set.of(new Role("ADMIN")));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userToBeFound));

        // when
        User actualUser = userService.findById(anyLong());

        // then
        assertThat(actualUser).isEqualTo(userToBeFound);
    }

    @Test
    @DisplayName("Should throw UserNotFound Exception")
    void findUserById_ExceptionTest() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFound.class, () -> userService.findById(anyLong()));
    }

    @Test
    @DisplayName("Should update a user")
    void updateUserTest() {
        // given
        User userToUpdate = new User(
                1L,
                "Stanciu",
                "Angel",
                "angel@gmail.com",
                "pass",
                "Romania, Bucuresti, Strada Gabroveni 030089",
                "+40721058124",
                LocalDate.of(2010, 1, 1),
                PENDING,
                Set.of(new Role("ADMIN")));

        User newUser = new User(
                1L,
                "updated",
                "updated",
                "updated@gmail.com",
                "updated",
                "updated",
                "updated",
                LocalDate.of(2222, 2, 2),
                PENDING,
                Set.of(new Role("ADMIN")));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userToUpdate));
        when(userRepository.save(userToUpdate)).thenReturn(newUser);

        // when
        User resultedUser = userService.update(anyLong(), newUser);

        //then
        assertThat(resultedUser).isEqualTo(newUser);
    }

    @Test
    @DisplayName("Should throw UserNotFound Exception")
    void updateUser_ExceptionTest() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFound.class, () -> userService.deleteById(anyLong()));
    }

    @Test
    @DisplayName("Should update user status")
    void updateUserStatusTest() {
        // given
        User user = new User(
                1L,
                "Stanciu",
                "Angel",
                "angel@gmail.com",
                "pass",
                "Romania, Bucuresti, Strada Gabroveni 030089",
                "+40721058124",
                LocalDate.of(2010, 1, 1),
                PENDING,
                Set.of(new Role("ADMIN")));

        User updatedUser = new User(
                1L,
                "Stanciu",
                "Angel",
                "angel@gmail.com",
                "pass",
                "Romania, Bucuresti, Strada Gabroveni 030089",
                "+40721058124",
                LocalDate.of(2010, 1, 1),
                ACTIVE,
                Set.of(new Role("ADMIN")));

        when(userRepository.save(user)).thenReturn(updatedUser);

        // when
        User resultedUser = userService.updateStatus(ACTIVE, user);

        // then
        assertThat(resultedUser).isEqualTo(updatedUser);
    }

    @Test
    void shouldSaveOneUser() {
        // given
        User user = new User(1L,
                "Gigel",
                "Popescu",
                "gigelp@gmail.com",
                "pass",
                "Str. Paltinului, Pitesti, Arges",
                "0700112233",
                LocalDate.now(),
                PENDING,
                Set.of(new Role("ADMIN"))
        );
        when(userRepository.save(user)).thenReturn(user);

        // when
        User actualUser = userService.save(user);

        // then
        assertThat(actualUser).isEqualTo(user);

    }

    @Test
    void shouldDeleteUserById() {
        // given
        User user = new User(1L,
                "Gigel",
                "Popescu",
                "gigelp@gmail.com",
                "pass",
                "Str. Paltinului, Pitesti, Arges",
                "0700112233",
                LocalDate.now(),
                PENDING,
                Set.of(new Role("ADMIN"))
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        userService.deleteById(1L);

        // then
        verify(userRepository).deleteById(1L);

    }

    @Test
    void shouldThrowUserNotFoundForDeleteById() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        assertThrows(UserNotFound.class, () -> userService.deleteById(anyLong()));

        // then
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldFindAllByPageTest() {
        // given
        List<User> users = List.of(
                new User(1L,
                        "Stanciu",
                        "Angel",
                        "angel@gmail.com",
                        "pass",
                        "Romania, Bucuresti, Strada Gabroveni 030089",
                        "+40721058124",
                        LocalDate.of(2010, 1, 1),
                        PENDING,
                        Set.of(new Role("ADMIN"))),
                new User(2L,
                        "Stanciu",
                        "Angel",
                        "angel@gmail.com",
                        "pass",
                        "Romania, Bucuresti, Strada Gabroveni 030089",
                        "+40721058124",
                        LocalDate.of(2010, 1, 1),
                        ACTIVE,
                        Set.of(new Role("ADMIN")))
        );
        Page<User> foundPage = new PageImpl<>(users);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(foundPage);

        // when
        List<User> actual = userService.findAllByPage(1, 5, "id", "asc");

        // then
        assertThat(actual).isEqualTo(users);
    }

    @Test
    void shouldFindUserByEmail() {
        // given
        User user = new User(1L,
                "Stanciu",
                "Angel",
                "angel@gmail.com",
                "pass",
                "Romania, Bucuresti, Strada Gabroveni 030089",
                "+40721058124",
                LocalDate.of(2010, 1, 1),
                ACTIVE,
                Set.of(new Role("ADMIN")));
        when(userRepository.findUserByEmail(anyString())).thenReturn(user);

        // when
        User actual = userService.findUserByEmail("angel@gmail.com");

        // then
        assertThat(actual).isEqualTo(user);
    }

}

