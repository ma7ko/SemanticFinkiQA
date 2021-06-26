package mk.ukim.finki.wbs.service;

import org.apache.jena.rdf.model.Resource;

import java.util.Optional;

public interface UserService {
    Optional<Resource> getUser(String URI);
    Optional<Resource> addUser(String prefix, String username, String email, String code, String role);
    void deleteUser(String URI);
}
