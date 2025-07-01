package org.dimchik.config;

import org.dimchik.dao.IProductDao;
import org.dimchik.dao.IUserDao;
import org.dimchik.dao.UserDao;
import org.dimchik.service.UserService;
import org.dimchik.servlet.*;
import org.dimchik.dao.ProductDao;
import org.dimchik.service.ProductService;
import org.dimchik.util.DbUtil;
import org.dimchik.util.TemplateEngine;

public class ComponentContainer {
    private final DbUtil dbUtil = DbUtil.getInstance();
    private final IUserDao userDao = new UserDao(dbUtil);
    private final IProductDao productDao = new ProductDao(dbUtil);
    private final UserService userService = new UserService(userDao);
    private final ProductService productService = new ProductService(productDao);
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
