package org.dimchik.dao.impl;

import org.dimchik.dao.CartDao;
import org.dimchik.entity.Cart;
import org.dimchik.entity.Product;
import org.dimchik.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDaoImpl implements CartDao {
    private final DbUtil dbUtil;

    private static final String EXISTS_SQL = "SELECT 1 FROM carts WHERE user_id = ? AND product_id = ? LIMIT 1";
    private static final String SAVE_SQL = "INSERT INTO carts (user_id, product_id) VALUES (?, ?)";
    private static final String DELETE_SQL = "DELETE FROM carts WHERE id = ?";
    private static final String CLEAR_BY_USER_ID_SQL = "DELETE FROM carts WHERE user_id = ?";
    private static final String SELECT_ALL_SQL =
            "SELECT c.id as cart_id, c.user_id, c.product_id, " +
                    "p.name, p.price, p.description, p.creation_date as product_creation_date " +
                    "FROM carts c JOIN products p ON c.product_id = p.id " +
                    "WHERE c.user_id = ?";

    public CartDaoImpl(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    @Override
    public boolean exists(Cart cart) {
        try (Connection connection = dbUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(EXISTS_SQL)) {

            ps.setLong(1, cart.getUserId());
            ps.setLong(2, cart.getProductId());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if cart item exists", e);
        }
    }

    @Override
    public void save(Cart cart) {
        try (Connection connection = dbUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(SAVE_SQL)) {

            ps.setLong(1, cart.getUserId());
            ps.setLong(2, cart.getProductId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert cart item", e);
        }
    }

    @Override
    public void delete(long cartId) {
        try (Connection connection = dbUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_SQL)) {

            ps.setLong(1, cartId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete cart item", e);
        }
    }

    public void clearCart(long userId) {
        try (Connection connection = dbUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(CLEAR_BY_USER_ID_SQL)) {

            ps.setLong(1, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear carts item", e);
        }
    }

    @Override
    public List<Cart> findCartsByUserId(long userId) {
        List<Cart> carts = new ArrayList<>();

        try (Connection connection = dbUtil.getDataSource().getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_SQL)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cart cart = new Cart();
                    cart.setId(rs.getLong("cart_id"));
                    cart.setUserId(rs.getLong("user_id"));
                    cart.setProductId(rs.getLong("product_id"));

                    Product product = new Product();
                    product.setId(rs.getLong("product_id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setDescription(rs.getString("description"));
                    product.setCreationDate(rs.getTimestamp("product_creation_date").toLocalDateTime());

                    cart.setProduct(product);
                    carts.add(cart);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve cart items", e);
        }

        return carts;
    }
}
