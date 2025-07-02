package org.dimchik.dao.impl;

import org.dimchik.dao.ProductDao;
import org.dimchik.entity.Product;
import org.dimchik.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    private final DbUtil dbUtil;

    private static final String SELECT_ALL = "SELECT * FROM products";
    private static final String SEARCH = "SELECT * FROM products WHERE name ILIKE ? or description ILIKE ?";
    private static final String SELECT_BY_ID = "SELECT * FROM products WHERE id = ?";
    private static final String INSERT = "INSERT INTO products (name, price, description, creation_date) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE products SET name = ?, price = ?, description = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM products WHERE id = ?";

    public ProductDaoImpl(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();

        try (Connection connection = dbUtil.getDataSource().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SELECT_ALL)) {

            while (resultSet.next()) {
                products.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed", e);
        }

        return products;
    }

    @Override
    public List<Product> findBySearch(String search) {
        List<Product> products = new ArrayList<>();

        try (Connection connection = dbUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH)) {

            String pattern = "%" + search + "%";
            preparedStatement.setString(1, pattern);
            preparedStatement.setString(2, pattern);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed by search = " + search, e);
        }

        return products;
    }

    @Override
    public Product findById(Long id) {
        Product product = null;

        try (Connection connection = dbUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    product = mapRow(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed by id = " + id, e);
        }

        return product;
    }

    @Override
    public void save(Product product) {
        try (Connection connection = dbUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(product.getCreationDate()));

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save product", e);
        }
    }

    @Override
    public void update(Product product) {
        try (Connection connection = dbUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setLong(4, product.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update product", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = dbUtil.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete product", e);
        }
    }

    private Product mapRow(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getLong("id"));
        product.setName(resultSet.getString("name"));
        product.setPrice(resultSet.getDouble("price"));
        product.setDescription(resultSet.getString("description"));
        product.setCreationDate(resultSet.getTimestamp("creation_date").toLocalDateTime());
        return product;
    }
}
