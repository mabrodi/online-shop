package org.dimchik.context;

import org.dimchik.entity.Cart;
import org.dimchik.entity.User;

import java.time.LocalDateTime;

public class Session {
    private final User user;
    private final Cart cart;
    private String token;
    private final LocalDateTime expiresAt;

    public Session(User user, String token, int maxAgeSeconds) {
        this.user = user;
        this.cart = new Cart();
        this.token = token;
        this.expiresAt = LocalDateTime.now().plusSeconds(maxAgeSeconds);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public User getUser() {
        return user;
    }

    public Cart getCart() {
        return cart;
    }

    public String getToken() {
        return token;
    }
}
