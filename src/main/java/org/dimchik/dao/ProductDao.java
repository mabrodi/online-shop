package org.dimchik.dao;

import org.dimchik.entity.Product;
import org.dimchik.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao implements IProductDao {
    private final DbUtil dbUtil;

    private static final String SELECT_ALL = "SELECT * FROM products";
    private static final String SEARCH = "SELECT * FROM products WHERE name ILIKE ? or description ILIKE ?";
    private static final String SELECT_BY_ID = "SELECT * FROM products WHERE id = ?";
    private static final String INSERT = "INSERT INTO products (name, price, description, creation_date) VALUES (?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE products SET name = ?, price = ?, description = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM products WHERE id = ?";

    public ProductDao(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();

        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL)) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed", e);
        }

        return products;
    }

    @Override
    public List<Product> findBySearch(String search) {
        List<Product> products = new ArrayList<>();

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH)) {

            String pattern = "%" + search + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
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

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    product = mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed by id = " + id, e);
        }

        return product;
    }

    @Override
    public void save(Product product) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setString(3, product.getDescription());
            stmt.setTimestamp(4, Timestamp.valueOf(product.getCreationDate()));

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
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
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setString(3, product.getDescription());
            stmt.setLong(4, product.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update product", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete product", e);
        }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getDouble("price"));
        product.setDescription(rs.getString("description"));
        product.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
        return product;
    }
}
