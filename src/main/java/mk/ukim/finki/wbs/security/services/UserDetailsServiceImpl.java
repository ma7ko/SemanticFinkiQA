package mk.ukim.finki.wbs.security.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import mk.ukim.finki.wbs.mapper.ModelToJsonLDMapper;
import mk.ukim.finki.wbs.model.jsonld.User;
import mk.ukim.finki.wbs.repository.UserRepository;
import org.apache.jena.rdf.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelToJsonLDMapper mapper;

    public UserDetailsServiceImpl(UserRepository userRepository, ModelToJsonLDMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
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
