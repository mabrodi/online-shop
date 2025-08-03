package org.dimchik;

import org.dimchik.config.ServiceLocator;
import org.dimchik.web.security.AuthFilter;
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
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class App {
    public static void main(String[] args) throws Exception {
        ServiceLocator.init();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        //auth
        context.addServlet(new ServletHolder(new LoginServlet()), "/login");
        context.addServlet(new ServletHolder(new LogoutServlet()), "/logout");

        //product
        context.addServlet(new ServletHolder(new ProductServlet()), "/products");
        context.addServlet(new ServletHolder(new ProductServlet()), "/");
        context.addServlet(new ServletHolder(new AddProductServlet()), "/products/add");
        context.addServlet(new ServletHolder(new DeleteProductServlet()), "/products/delete/*");
        context.addServlet(new ServletHolder(new UpdateProductServlet()), "/products/update/*");

        //cart
        context.addServlet(new ServletHolder(new CartServlet()), "/cart");
        context.addServlet(new ServletHolder(new AddCartServlet()), "/cart/add/*");
        context.addServlet(new ServletHolder(new DeleteCartServlet()), "/cart/delete/*");
        context.addServlet(new ServletHolder(new CleanCartServlet()), "/cart/clean");


        FilterHolder authFilter = new FilterHolder(new AuthFilter());
        context.addFilter(authFilter, "/*", null);

        Server server = new Server(7080);
        server.setHandler(context);
        server.start();

    }
}