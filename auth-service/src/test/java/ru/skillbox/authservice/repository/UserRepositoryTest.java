package ru.skillbox.authservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.authservice.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepositoryJpa;

    @Test
    public void whenGetByDescription_thenReturnOrder() {
        User user = new User(
                "Petrov Petr",
                "superpas99"
        );
        entityManager.persist(user);
        entityManager.flush();

        String userName = user.getName();
        User gotUser = userRepositoryJpa.findByName(userName).get();

        assertThat(gotUser.getPassword())
                .isEqualTo(user.getPassword());
    }
}
