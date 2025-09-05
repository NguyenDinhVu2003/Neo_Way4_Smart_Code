package security.instagram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import security.instagram.entity.Decentralization;
import security.instagram.entity.Role;
import security.instagram.repository.DecentralizationRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class DecentralizationService {
    private final DecentralizationRepository repository;
    private final AntPathMatcher matcher = new AntPathMatcher();
    // Cache: role -> list
    private final Map<Role, List<Decentralization>> cache = new ConcurrentHashMap<>();



    public boolean isAllowed(Role role, String path, String method) {
        return repository.checkPemission(role, path, method);
    }

    public Decentralization addDecentralization(Decentralization decentralization) {
        Decentralization saved = repository.save(decentralization);
        // Optionally refresh cache if you use one
        return saved;
    }

    public Decentralization updateDecentralization(Decentralization decentralization) {
        if (decentralization.getId() == null || !repository.existsById(decentralization.getId())) {
            throw new NoSuchElementException("Decentralization rule not found");
        }
        Decentralization updated = repository.save(decentralization);
        // Optionally refresh cache if you use one
        return updated;
    }

    public void deleteDecentralization(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Decentralization rule not found");
        }
        repository.deleteById(id);
        // Optionally refresh cache if you use one
    }

    public List<Decentralization> getAll() {
        return repository.findAll();
    }
}
