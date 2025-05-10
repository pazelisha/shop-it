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


public class ProductAdapter extends ArrayAdapter<Product> {

    private List<Product> products;
    private Context context;
    private OnProductActionListener actionListener;


    public interface OnProductActionListener {
        void onRemove(String productId);
    }


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


        Product currentProduct = products.get(position);


        viewHolder.productNameTextView.setText(currentProduct.getName());
        viewHolder.productQuantityTextView.setText(context.getString(R.string.quantity_format, currentProduct.getQuantity()));


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

    private static class ViewHolder {
        TextView productNameTextView;
        TextView productQuantityTextView;
        Button removeButton;
    }
}