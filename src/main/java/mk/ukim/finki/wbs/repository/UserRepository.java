package mk.ukim.finki.wbs.repository;

import org.apache.jena.rdf.model.Resource;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<Resource> listUsers();
    Optional<Resource> getUser(String URI);
    Optional<Resource> createUser(String prefix, String URI);
    Optional<Resource> saveUser(Resource resource);
    Optional<Resource> getUserByUsername(String username);
    Optional<Resource> getUserByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteUser(String URI);
}
