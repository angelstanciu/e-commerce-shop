package com.endava.mentorship2022.service;

import com.endava.mentorship2022.exception.UserNotFound;
import com.endava.mentorship2022.model.User;
import com.endava.mentorship2022.model.UserStatus;
import com.endava.mentorship2022.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllByPage(int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        if (pageNum < 1) pageNum = 1;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<User> pageUsers = userRepository.findAll(pageable);

        return pageUsers.getContent();
    }

    public User findById(Long id) {
        return userRepository.findById(id).
                orElseThrow(() -> new UserNotFound("The user with the id: " + id + " doesn't exists"));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        findById(id); // just call the method, if the user is not found it throws UserNotFound Exception
        userRepository.deleteById(id);
    }

    public User update(Long id, User newUser) {
        User userToUpdate = findById(id);
        userToUpdate.setFirstName(newUser.getFirstName());
        userToUpdate.setLastName(newUser.getLastName());
        userToUpdate.setEmail(newUser.getEmail());
        userToUpdate.setPassword(newUser.getPassword());
        userToUpdate.setAddress(newUser.getAddress());
        userToUpdate.setPhoneNumber(newUser.getPhoneNumber());
        userToUpdate.setBirthDate(newUser.getBirthDate());
        return userRepository.save(userToUpdate);
    }

    public User updateStatus(UserStatus status, User user) {
        user.setStatus(status);
        return userRepository.save(user);
    }

    // returns the list of users sorted by their birthdate and after that by their first name
    public List<User> findAllSorted() {

        // findAll() method gets all the users from the DB and then, the sorting method sorts them
        return sortUsersByBirthdateAndByFirstName(findAll());
    }

    private List<User> sortUsersByBirthdateAndByFirstName(List<User> usersList) {

        Comparator<User> compareByBirthdateAndByFirstName =
                Comparator.comparing(User::getBirthDate)
                        .reversed()                        // used reversed() to sort them ascending by their birthdate
                        .thenComparing(User::getFirstName);

        usersList.sort(compareByBirthdateAndByFirstName);

        return usersList;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

}
