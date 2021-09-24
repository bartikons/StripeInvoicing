package third.task.stripe.security.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import third.task.stripe.model.CustomerModel;

import java.util.ArrayList;
import java.util.Collection;

public class UserPrinciple implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();

    public UserPrinciple(Long id, String username) {
        this.id = id;
        this.username = username;
        this.authorities.add(new SimpleGrantedAuthority("Customer"));
    }

    public UserPrinciple() {

    }

    public static UserPrinciple build(CustomerModel user) {
        return new UserPrinciple(
                user.getId(),
                user.getEmail()
        );

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
