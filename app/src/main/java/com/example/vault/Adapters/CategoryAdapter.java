package com.example.vault.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vault.ListViewr;
import com.example.vault.R;
import com.example.vault.objects.Categories;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    public List<Categories> uList;
    private Context mContext;


    public CategoryAdapter( List<Categories> uList)

    {
        this.uList=uList;

    }



    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card,parent,false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // item clicked
            }
        });
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return uList.size();
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.uName.setText(uList.get(position).getName());
        holder.uLastM.setCardBackgroundColor(uList.get(position).getColour());
    }


    public class  ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView uName;
        public CardView uLastM;
        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            uName= (TextView) mView.findViewById(R.id.Lname);
            uLastM= (CardView) mView.findViewById(R.id.card);
           /* mView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Context context = mView.getContext();


                    int g=getAdapterPosition();
                    String category=uList.get(g).getName();


                }
            });*/


        }
    }
}
