package org.dimchik.dao;

import org.dimchik.model.Product;
import org.dimchik.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDao implements IProductDao {
    private final DbUtil dbUtil;

    public ProductDao() {
        this(new DbUtil());
    }

    public ProductDao(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = dbUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database query failed", e);
        }

        return products;
    }

    @Override
    public Product findById(Long id) {
        String sql = "SELECT * FROM products WHERE id = ? order by products.id desc";
        Product product = null;

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "INSERT INTO products (name, price, creation_date) VALUES (?, ?, ?)";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setTimestamp(3, Timestamp.valueOf(product.getCreationDate()));

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
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setLong(3, product.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update product", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = dbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        product.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());

        return product;
    }
}
