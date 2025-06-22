package org.dimchik.main;

import org.dimchik.controller.*;
import org.dimchik.dao.ProductDao;
import org.dimchik.service.ProductService;
import org.dimchik.util.DbUtil;
import org.dimchik.util.TemplateEngine;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception {
        DbUtil dbUtil = DbUtil.getInstance();
        ProductDao productDao = new ProductDao(dbUtil);
        ProductService productService = new ProductService(productDao);
        TemplateEngine templateEngine = new TemplateEngine();

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");

        context.addServlet(new ServletHolder(new ProductServlet(productService, templateEngine)), "/products");
        context.addServlet(new ServletHolder(new ProductServlet(productService, templateEngine)), "/");
        context.addServlet(new ServletHolder(new AddProductServlet(productService, templateEngine)), "/products/add");
        context.addServlet(new ServletHolder(new DeleteProductServlet(productService)), "/products/delete/*");
        context.addServlet(new ServletHolder(new UpdateProductServlet(productService, templateEngine)), "/products/update/*");


        Server server = new Server(7080);
        server.setHandler(context);
        server.start();

    }
}