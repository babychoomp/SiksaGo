//package com.example.penta.Insert;
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.penta.R;
//
//import java.util.ArrayList;
//
//public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
//    private Context context;
//    private ArrayList<String> korean_id, grams_id, calories_id, carbs_id, proteins_id, fats_id;
//
//    public MyAdapter(Context context, ArrayList korean_id, ArrayList grams_id, ArrayList calories_id, ArrayList carbs_id, ArrayList proteins_id, ArrayList fats_id) {
//        this.context = context;
//        this.korean_id = korean_id;
//        this.grams_id = grams_id;
//        this.calories_id = calories_id;
//        this.carbs_id = carbs_id;
//        this.proteins_id = proteins_id;
//        this.fats_id = fats_id;
//    }
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(context).inflate(R.layout.foodentry, parent, false);
//        MyViewHolder viewHolder = new MyViewHolder(v);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.korean_id.setText(String.valueOf(korean_id.get(position)));
//        holder.grams_id.setText(String.valueOf(grams_id.get(position)));
//        holder.calories_id.setText(String.valueOf(calories_id.get(position)));
//        holder.carbs_id.setText(String.valueOf(carbs_id.get(position)));
//        holder.proteins_id.setText(String.valueOf(proteins_id.get(position)));
//        holder.fats_id.setText(String.valueOf(fats_id.get(position)));
//    }
//
//    @Override
//    public int getItemCount() {
//        return korean_id.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView korean_id, grams_id, calories_id, carbs_id, proteins_id, fats_id;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            korean_id = itemView.findViewById(R.id.textkorean);
//            grams_id = itemView.findViewById(R.id.textgrams);
//            calories_id = itemView.findViewById(R.id.textcalories);
//            carbs_id = itemView.findViewById(R.id.textcarbs);
//            proteins_id = itemView.findViewById(R.id.textproteins);
//            fats_id = itemView.findViewById(R.id.textfats);
//        }
//    }
//}
