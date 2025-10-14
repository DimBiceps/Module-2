package module23.service;

import module22.dao.UserDao;
import module22.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

class DefaultUserServiceTest {

    private UserDao userDao;
    private UserService service;

    @BeforeEach
    void setUp() {
        userDao = mock(UserDao.class);
        service = new DefaultUserService(userDao);
    }

    @Test
    void createUser_delegates_to_DAO_and_returns_id() {
        when(userDao.create(any())).thenReturn(42L);

        Long id = service.createUser(new User("Alice", "a@a", 25));
        assertEquals(42L, id);
        verify(userDao, times(1)).create(any(User.class));
    }

    @Test
    void getAllUsers_returns_list_from_DAO() {
        when(userDao.findAll()).thenReturn(List.of(
                new User("A","a@a",20),
                new User("B","b@b",21)
        ));

        List<User> list = service.getAllUsers();
        assertEquals(2, list.size());
        verify(userDao).findAll();
    }

    @Test
    void getUserById_propagates_found_value() {
        User u = new User("Kek","k@k",40);
        u.setId(5L);
        when(userDao.findById(5L)).thenReturn(Optional.of(u));

        Optional<User> res = service.getUserById(5L);
        assertTrue(res.isPresent());
        assertEquals(5L, res.get().getId());
        verify(userDao).findById(5L);
    }

    @Test
    void updateUser_calls_DAO_update_with_same_object() {
        User u = new User("A","a@a",20);
        u.setId(99L);

        service.updateUser(u);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDao).update(captor.capture());
        assertEquals(99L, captor.getValue().getId());
    }

    @Test
    void deleteUser_skips_when_not_found() {
        when(userDao.findById(7L)).thenReturn(Optional.empty());
        service.deleteUser(7L);
        verify(userDao, never()).delete(any());
    }

    @Test
    void deleteUser_deletes_when_found() {
        User u = new User("X","x@x",30);
        u.setId(8L);
        when(userDao.findById(8L)).thenReturn(Optional.of(u));

        service.deleteUser(8L);
        verify(userDao).delete(u);
    }
}
