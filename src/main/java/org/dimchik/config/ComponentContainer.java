package org.dimchik.config;

import org.dimchik.dao.CartDao;
import org.dimchik.dao.ProductDao;
import org.dimchik.dao.UserDao;
import org.dimchik.dao.impl.CartDaoImpl;
import org.dimchik.dao.impl.UserDaoImpl;
import org.dimchik.service.AuthService;
import org.dimchik.service.CartService;
import org.dimchik.service.ProductService;
import org.dimchik.service.UserService;
import org.dimchik.service.impl.AuthServiceImpl;
import org.dimchik.service.impl.CartServiceImpl;
import org.dimchik.service.impl.UserServiceImpl;
import org.dimchik.dao.impl.ProductDaoImpl;
import org.dimchik.service.impl.ProductServiceImpl;
import org.dimchik.util.DbUtil;
import org.dimchik.util.TemplateEngine;
import org.dimchik.web.servlet.auth.LoginServlet;
import org.dimchik.web.servlet.auth.LogoutServlet;
import org.dimchik.web.servlet.cart.AddCartServlet;
import org.dimchik.web.servlet.cart.CartServlet;
import org.dimchik.web.servlet.cart.CleanCartServlet;
import org.dimchik.web.servlet.cart.DeleteCartServlet;
import org.dimchik.web.servlet.product.AddProductServlet;
import org.dimchik.web.servlet.product.DeleteProductServlet;
import org.dimchik.web.servlet.product.ProductServlet;
import org.dimchik.web.servlet.product.UpdateProductServlet;

public class ComponentContainer {
    private final DbUtil dbUtil = DbUtil.getInstance();
    private final TemplateEngine templateEngine = new TemplateEngine();
    private final AuthService authService = new AuthServiceImpl();
    //user
    private final UserDao userDao = new UserDaoImpl(dbUtil);
    private final UserService userService = new UserServiceImpl(userDao);
    //product
    private final ProductDao productDao = new ProductDaoImpl(dbUtil);
    private final ProductService productService = new ProductServiceImpl(productDao);
    //cart
    private final CartDao cartDao = new CartDaoImpl(dbUtil);
    private  final CartService cartService = new CartServiceImpl(cartDao);


    public AuthService getAuthService() {
        return authService;
    }

    public LoginServlet loginServlet() {
        return new LoginServlet(authService, templateEngine, userService);
    }

    public LogoutServlet logoutServlet() {
        return new LogoutServlet(authService);
    }

    public ProductServlet productServlet() {
        return new ProductServlet(authService, productService, templateEngine);
    }

    public AddProductServlet addProductServlet() {
        return new AddProductServlet(authService, productService, templateEngine);
    }

    public UpdateProductServlet updateProductServlet() {
        return new UpdateProductServlet(authService, productService, templateEngine);
    }

    public DeleteProductServlet deleteProductServlet() {
        return new DeleteProductServlet(productService);
    }

    public CartServlet cartServlet() {
        return new CartServlet(authService, cartService, templateEngine);
    }

    public AddCartServlet addCartServlet() {
        return new AddCartServlet(authService, cartService);
    }

    public DeleteCartServlet deleteCartServlet() {
        return new DeleteCartServlet(cartService);
    }

    public CleanCartServlet cleanCartServlet() {
        return new CleanCartServlet(authService, cartService);
    }
}
