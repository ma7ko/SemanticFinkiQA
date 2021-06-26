package mk.ukim.finki.wbs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import mk.ukim.finki.wbs.mapper.ModelToJsonLDMapper;
import mk.ukim.finki.wbs.model.UserModel;
import mk.ukim.finki.wbs.model.jsonld.User;
import mk.ukim.finki.wbs.payload.response.MessageResponse;
import mk.ukim.finki.wbs.repository.UserRepository;
import mk.ukim.finki.wbs.security.services.UserDetailsImpl;
import mk.ukim.finki.wbs.service.UserService;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.AlreadyExists;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserModel userModel;
    private final ModelToJsonLDMapper mapper;

    public UserServiceImpl(UserRepository userRepository,
                           UserModel userModel,
                           ModelToJsonLDMapper mapper) {
        this.userRepository = userRepository;
        this.userModel = userModel;
        this.mapper = mapper;
    }

    @Override
    public Optional<Resource> getUser(String URI) {
        return this.userRepository.getUser(URI);
    }

    @Override
    public Optional<Resource> addUser(String prefix, String username, String email, String code, String role) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (userRepository.existsByUsername(username)) {
            throw new AlreadyExists();
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExists();
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        String uri = userModel.getURIFromUsername(username);

        Resource user = this.userRepository
                .createUser(prefix, uri)
                .orElseThrow(IllegalArgumentException::new);

        user.addProperty(this.userModel.getUsername(), username)
                .addProperty(this.userModel.getEmail(), email)
                .addProperty(this.userModel.getCode(), code)
                .addProperty(this.userModel.getRole(), role)
                .addProperty(this.userModel.getPrefix(), (prefix == null) ? this.userModel.getPrefixes().get("usr") : prefix)
                .addProperty(this.userModel.getUri(), uri);

        return this.userRepository.saveUser(user);
    }

    @Override
    public void deleteUser(String URI) {
        this.userRepository.deleteUser(URI);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Resource userResource = userRepository.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        try {
            User user = this.mapper.mapUserExp(userResource).orElseThrow(IllegalArgumentException::new);
            UserDetails userDetails = UserDetailsImpl.build(user);
            return userDetails;
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
