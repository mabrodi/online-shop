package org.dimchik.main;

import org.dimchik.controller.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(7080);

        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");

        context.addServlet(new ServletHolder(new ProductServlet()), "/products");
        context.addServlet(new ServletHolder(new ProductServlet()), "/");
        context.addServlet(new ServletHolder(new AddProductServlet()), "/products/add");
        context.addServlet(new ServletHolder(new DeleteProductServlet()), "/products/delete/*");
        context.addServlet(new ServletHolder(new UpdateProductServlet()), "/products/update/*");

        server.setHandler(context);
        server.start();

    }
}