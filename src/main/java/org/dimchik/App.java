package org.dimchik;

import org.dimchik.config.ComponentContainer;
import org.dimchik.web.security.AuthFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class App {
    public static void main(String[] args) throws Exception {
        ComponentContainer container = new ComponentContainer();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        //auth
        context.addServlet(new ServletHolder(container.loginServlet()), "/login");
        context.addServlet(new ServletHolder(container.logoutServlet()), "/logout");

        //product
        context.addServlet(new ServletHolder(container.productServlet()), "/products");
        context.addServlet(new ServletHolder(container.productServlet()), "/");
        context.addServlet(new ServletHolder(container.addProductServlet()), "/products/add");
        context.addServlet(new ServletHolder(container.deleteProductServlet()), "/products/delete/*");
        context.addServlet(new ServletHolder(container.updateProductServlet()), "/products/update/*");

        //cart
        context.addServlet(new ServletHolder(container.cartServlet()), "/cart");
        context.addServlet(new ServletHolder(container.addCartServlet()), "/cart/add/*");
        context.addServlet(new ServletHolder(container.deleteCartServlet()), "/cart/delete/*");
        context.addServlet(new ServletHolder(container.cleanCartServlet()), "/cart/clean");


        FilterHolder authFilter = new FilterHolder(new AuthFilter(container.getAuthService()));
        context.addFilter(authFilter, "/*", null);

        Server server = new Server(7080);
        server.setHandler(context);
        server.start();

    }
}