package org.dimchik.config;

import org.dimchik.dao.ProductDao;
import org.dimchik.dao.UserDao;
import org.dimchik.dao.jdbc.ProductDaoJdbc;
import org.dimchik.dao.jdbc.UserDaoJdbc;
import org.dimchik.service.CartService;
import org.dimchik.service.ProductService;
import org.dimchik.service.SecurityService;
import org.dimchik.service.UserService;
import org.dimchik.service.base.CartServiceBase;
import org.dimchik.service.base.ProductServiceBase;
import org.dimchik.service.base.SecurityServiceBase;
import org.dimchik.service.base.UserServiceBase;
import org.dimchik.web.session.SessionCookieHandler;
import org.dimchik.web.view.ErrorViewRenderer;
import org.dimchik.web.view.TemplateRenderer;
import org.postgresql.ds.PGSimpleDataSource;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static final Map<Class<?>, Object> SERVICES = new HashMap<>();

    public static void init() {
        PGSimpleDataSource pgSimpleDataSource = createDataSource();
        SessionCookieHandler sessionCookieHandler = new SessionCookieHandler(
                PropertyReader.get("cookie.name"),
                PropertyReader.getInt("cookie.max-age")
        );

        TemplateRenderer templateRenderer = new TemplateRenderer();
        ErrorViewRenderer errorViewRenderer = new ErrorViewRenderer(templateRenderer);

        UserDao userDao = new UserDaoJdbc(pgSimpleDataSource);
        ProductDao productDao = new ProductDaoJdbc(pgSimpleDataSource);
        UserService userService = new UserServiceBase(userDao);
        SecurityService securityService = new SecurityServiceBase(userService, PropertyReader.getInt("cookie.max-age"));
        ProductService productService = new ProductServiceBase(productDao);
        CartService cartService = new CartServiceBase(productService);

        addService(SessionCookieHandler.class, sessionCookieHandler);
        addService(SecurityService.class, securityService);
        addService(TemplateRenderer.class, templateRenderer);
        addService(UserService.class, userService);
        addService(ProductService.class, productService);
        addService(CartService.class, cartService);
        addService(ErrorViewRenderer.class, errorViewRenderer);
    }

    private static PGSimpleDataSource createDataSource() {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL(PropertyReader.get("db.url"));
        pgSimpleDataSource.setUser(PropertyReader.get("db.username"));
        pgSimpleDataSource.setPassword(PropertyReader.get("db.password"));
        return pgSimpleDataSource;
    }

    public static void addService(Class<?> serviceName, Object service) {
        if (SERVICES.containsKey(serviceName)) {
            throw new IllegalStateException("Service already registered: " + serviceName.getName());
        }
        SERVICES.put(serviceName, service);
    }

    public static <T> T getService(Class<T> clazz) {
        Object service = SERVICES.get(clazz);
        if (service == null) {
            throw new IllegalStateException("No service registered for: " + clazz.getName());
        }
        return clazz.cast(service);
    }
}
