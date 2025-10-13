package module23.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InOrder;
import org.mockito.Mock;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import module22.dao.UserDao;
import module22.entity.User;

class DefaultUserServiceTest {

    @Mock
    private UserDao userDao;

    private DefaultUserService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        service = new DefaultUserService(userDao);
    }

    @Test
    void createUser_delegatesToDao_andReturnsId() {
        User u = new User("Alice","a@ex.com",25);
        when(userDao.create(any(User.class))).thenReturn(42L);

        Long id = service.createUser(u);

        assertEquals(42L, id);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDao).create(captor.capture());
        assertEquals("Alice", captor.getValue().getName());
    }

    @Test
    void getAllUsers_returnsListFromDao() {
        when(userDao.findAll()).thenReturn(List.of(new User("A","a@ex.com",null)));
        List<User> res = service.getAllUsers();
        assertEquals(1, res.size());
        verify(userDao).findAll();
    }

    @Test
    void getUserById_returnsFromDao() {
        when(userDao.findById(1L)).thenReturn(Optional.of(new User("X","x@ex.com",20)));
        assertTrue(service.getUserById(1L).isPresent());
        verify(userDao).findById(1L);
    }

    @Test
    void updateUser_callsDaoUpdate() {
        User u = new User("B","b@ex.com",30);
        u.setId(7L);
        service.updateUser(u);
        verify(userDao).update(u);
    }

    @Test
    void deleteUser_whenExists_deletes() {
        User u = new User("C","c@ex.com",40);
        u.setId(5L);
        when(userDao.findById(5L)).thenReturn(Optional.of(u));

        service.deleteUser(5L);

        InOrder inOrder = inOrder(userDao);
        inOrder.verify(userDao).findById(5L);
        inOrder.verify(userDao).delete(u);
    }

    @Test
    void deleteUser_whenNotExists_doNothing() {
        when(userDao.findById(99L)).thenReturn(Optional.empty());
        service.deleteUser(99L);
        verify(userDao).findById(99L);
        verify(userDao, never()).delete(any());
    }
}
