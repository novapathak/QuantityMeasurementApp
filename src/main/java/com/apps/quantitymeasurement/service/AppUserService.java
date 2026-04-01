package com.apps.quantitymeasurement.service;

import com.apps.quantitymeasurement.config.SecurityProperties;
import com.apps.quantitymeasurement.exception.QuantityMeasurementException;
import com.apps.quantitymeasurement.model.AppUserEntity;
import com.apps.quantitymeasurement.model.AuthProvider;
import com.apps.quantitymeasurement.model.RegisterRequest;
import com.apps.quantitymeasurement.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        AppUserEntity user = getByIdentifier(usernameOrEmail);
        return toUserDetails(user);
    }

    @Transactional(readOnly = true)
    public AppUserEntity getByIdentifier(String usernameOrEmail) {
        return appUserRepository.findByUsernameIgnoreCase(usernameOrEmail)
                .or(() -> appUserRepository.findByEmailIgnoreCase(usernameOrEmail))
                .filter(AppUserEntity::isEnabled)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));
    }

    @Transactional
    public AppUserEntity registerLocalUser(RegisterRequest request) {
        String normalizedUsername = normalizeUsername(request.getUsername());
        String normalizedEmail = normalizeEmail(request.getEmail());

        if (appUserRepository.existsByUsernameIgnoreCase(normalizedUsername)) {
            throw new QuantityMeasurementException("Username is already registered");
        }
        if (appUserRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new QuantityMeasurementException("Email is already registered");
        }

        AppUserEntity user = new AppUserEntity();
        user.setUsername(normalizedUsername);
        user.setEmail(normalizedEmail);
        user.setFullName(request.getFullName().trim());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setEnabled(true);
        user.setRoles(defaultRoles());

        return appUserRepository.save(user);
    }

    @Transactional
    public AppUserEntity upsertGoogleUser(String email, String fullName) {
        String normalizedEmail = normalizeEmail(email);

        AppUserEntity user = appUserRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseGet(AppUserEntity::new);

        if (user.getId() == null) {
            user.setUsername(normalizedEmail);
            user.setRoles(defaultRoles());
        }

        user.setEmail(normalizedEmail);
        user.setFullName(StringUtils.hasText(fullName) ? fullName.trim() : normalizedEmail);
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setEnabled(true);

        return appUserRepository.save(user);
    }

    public CommandLineRunner seedDefaultUser() {
        return args -> {
            SecurityProperties.DefaultUser defaultUser = securityProperties.getDefaultUser();
            if (!defaultUser.isEnabled()) {
                return;
            }

            if (appUserRepository.existsByUsernameIgnoreCase(defaultUser.getUsername())
                    || appUserRepository.existsByEmailIgnoreCase(defaultUser.getEmail())) {
                return;
            }

            AppUserEntity user = new AppUserEntity();
            user.setUsername(normalizeUsername(defaultUser.getUsername()));
            user.setEmail(normalizeEmail(defaultUser.getEmail()));
            user.setFullName(defaultUser.getFullName());
            user.setPasswordHash(passwordEncoder.encode(defaultUser.getPassword()));
            user.setAuthProvider(AuthProvider.LOCAL);
            user.setEnabled(true);
            user.setRoles(normalizeRoles(new LinkedHashSet<>(defaultUser.getRoles())));
            appUserRepository.save(user);
        };
    }

    public UserDetails toUserDetails(AppUserEntity user) {
        return User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash() == null ? "" : user.getPasswordHash())
                .disabled(!user.isEnabled())
                .authorities(user.getRoles().stream().map(this::toAuthority).toList())
                .build();
    }

    private GrantedAuthority toAuthority(String role) {
        return new SimpleGrantedAuthority(normalizeRole(role));
    }

    private Set<String> defaultRoles() {
        return normalizeRoles(Set.of("ROLE_USER"));
    }

    private Set<String> normalizeRoles(Set<String> roles) {
        LinkedHashSet<String> normalizedRoles = new LinkedHashSet<>();
        for (String role : roles) {
            normalizedRoles.add(normalizeRole(role));
        }
        return normalizedRoles;
    }

    private String normalizeRole(String role) {
        String trimmed = role.trim().toUpperCase(Locale.ROOT);
        return trimmed.startsWith("ROLE_") ? trimmed : "ROLE_" + trimmed;
    }

    private String normalizeUsername(String username) {
        return username.trim();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
