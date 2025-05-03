package com.example.shopit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shopit.R;
import com.example.shopit.models.Product;

import java.util.List;

/**
 * Adapter for displaying products in a ListView
 */
public class ProductAdapter extends ArrayAdapter<Product> {

    private List<Product> products;
    private Context context;
    private OnProductActionListener actionListener;

    /**
     * Interface for product actions callbacks
     */
    public interface OnProductActionListener {
        void onRemove(String productId);
    }

    /**
     * Constructor
     * @param context Application context
     * @param products List of products
     * @param actionListener Callback for product actions
     */
    public ProductAdapter(Context context, List<Product> products, OnProductActionListener actionListener) {
        super(context, R.layout.product_item, products);
        this.context = context;
        this.products = products;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.productNameTextView = convertView.findViewById(R.id.productNameTextView);
            viewHolder.productQuantityTextView = convertView.findViewById(R.id.productQuantityTextView);
            viewHolder.removeButton = convertView.findViewById(R.id.removeButton);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get the current product
        Product currentProduct = products.get(position);

        // Set the product data to views
        viewHolder.productNameTextView.setText(currentProduct.getName());
        viewHolder.productQuantityTextView.setText(context.getString(R.string.quantity_format, currentProduct.getQuantity()));

        // Set click listener for remove button
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.onRemove(currentProduct.getId());
                }
            }
        });

        return convertView;
    }

    /**
     * ViewHolder pattern class for better performance
     */
    private static class ViewHolder {
        TextView productNameTextView;
        TextView productQuantityTextView;
        Button removeButton;
    }
}