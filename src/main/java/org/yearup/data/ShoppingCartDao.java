package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here

    void addItem(int userId, ShoppingCartItem item);

    void removeItem(int userId, int productId);

    void updateItemQuantity(int userId, int productId, int quantity);

    void clearCart(int userId);

    void saveCart(int userId, ShoppingCart cart);
}
