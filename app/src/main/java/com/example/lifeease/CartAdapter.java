package com.example.lifeease;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private CartItemListener listener;

    public CartAdapter(Context context, List<CartItem> cartItems, CartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.productName.setText(item.getProductName());
        holder.quantity.setText("Quantity: " + item.getQuantity());
        holder.totalCost.setText("Price: " + item.getTotalCost());

        // Delete button click listener
        holder.btnDelete.setOnClickListener(v -> {
            listener.onDeleteClick(item); // Notify listener to delete this item from cart
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size(); // Return the number of items in the cart
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, quantity, totalCost;
        Button btnDelete;

        public CartViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.text_view_product_name); // Matching ID
            quantity = itemView.findViewById(R.id.text_view_quantity); // Matching ID
            totalCost = itemView.findViewById(R.id.text_view_total_cost); // Matching ID
            btnDelete = itemView.findViewById(R.id.btn_delete_product_admin); // Matching ID
        }
    }

    // Interface for item delete listener
    public interface CartItemListener {
        void onDeleteClick(CartItem item); // Method to be implemented for deleting items from cart
    }
}