package org.dimchik.config;

import org.dimchik.dao.ProductDao;
import org.dimchik.dao.UserDao;
import org.dimchik.dao.impl.UserDaoImpl;
import org.dimchik.service.impl.UserServiceImpl;
import org.dimchik.servlet.*;
import org.dimchik.dao.impl.ProductDaoImpl;
import org.dimchik.service.impl.ProductServiceImpl;
import org.dimchik.util.DbUtil;
import org.dimchik.util.TemplateEngine;

public class ComponentContainer {
    private final DbUtil dbUtil = DbUtil.getInstance();
    private final UserDao userDao = new UserDaoImpl(dbUtil);
    private final ProductDao productDao = new ProductDaoImpl(dbUtil);
    private final UserServiceImpl userService = new UserServiceImpl(userDao);
    private final ProductServiceImpl productService = new ProductServiceImpl(productDao);
    private final TemplateEngine templateEngine = new TemplateEngine();

    public LoginServlet loginServlet() {
        return new LoginServlet(templateEngine, userService);
    }

    public LogoutServlet logoutServlet() {
        return new LogoutServlet();
    }

    public ProductServlet productServlet() {
        return new ProductServlet(productService, templateEngine);
    }

    public AddProductServlet addProductServlet() {
        return new AddProductServlet(productService, templateEngine);
    }

    public UpdateProductServlet updateProductServlet() {
        return new UpdateProductServlet(productService, templateEngine);
    }

    public DeleteProductServlet deleteProductServlet() {
        return new DeleteProductServlet(productService);
    }
}
