package com.example.hansangjin.froot.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansangjin.froot.R;
import com.example.hansangjin.froot.Store;

import java.util.ArrayList;

public class CardLayoutActivity extends AppCompatActivity {
    private CardView cardView;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    private ArrayList<Store> storeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_layout);

        init();

    }

    private void init(){
        storeList = new ArrayList<>();

        recyclerViewAdapter = new RecyclerViewAdapter();

        storeList.add(new Store());
        storeList.add(new Store());
        storeList.add(new Store());
        storeList.add(new Store());
        storeList.add(new Store());
        storeList.add(new Store());
        storeList.add(new Store());
        storeList.add(new Store());


        cardView = findViewById(R.id.cardView);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setAdapter(recyclerViewAdapter);
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //2줄
    }

    private void setUpUI(){

    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(CardLayoutActivity.this, "ClickListener", Toast.LENGTH_LONG).show();
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(CardLayoutActivity.this, "LongClickListener", Toast.LENGTH_LONG).show();

                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return storeList.size();
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        private TextView store_name_textView;
        private ImageView store_image;


        public MyViewHolder(View parent) {
            super(parent);

            this.store_name_textView = parent.findViewById(R.id.store_name_textView);
        }
    }


}




