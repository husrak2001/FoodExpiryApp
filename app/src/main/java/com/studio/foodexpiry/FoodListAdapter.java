package com.studio.foodexpiry;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import foodexpiry.R;


public class FoodListAdapter extends  RecyclerView.Adapter<FoodListAdapter.PetViewHolder> {

    private Activity context;
    private ArrayList<Food> foodsList;
    private List<String> mData;

    public void add(String s,int position) {
        position = position == -1 ? getItemCount()  : position;
        mData.add(position,s);
        notifyItemInserted(position);
    }

    public void remove(int position){
        if (position < getItemCount()  ) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public FoodListAdapter(ArrayList<Food> foodsList, Activity activity) {
        this.context = activity;
        this.foodsList = foodsList;
    }
    @Override
    public int getItemCount() {
        return foodsList.size();
    }


    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PetViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.food_items,null));
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Food food = foodsList.get(position);

        if (food.getFavStatus().equals("0")) {
            holder.favbtn.setBackgroundResource(R.drawable.dil2);
        } else {
            holder.favbtn.setBackgroundResource(R.drawable.dil);
        }

        holder.txtName.setText(food.getName());
        holder.edtExpiry.setText("Expiry Date: "+food.getExpiry());

        byte[] foodImage = food.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(foodImage, 0, foodImage.length);
        holder.imageView.setImageBitmap(bitmap);



        holder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CharSequence[] items = {"Update", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // update
                            Cursor c = FoodList.sqLiteHelper.getData("SELECT id FROM FOOD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            // show dialog update at here
                            showDialogUpdate((Activity) context, arrID.get(position));

                        } else {
                            // delete
                            Cursor c = FoodList.sqLiteHelper.getData("SELECT id FROM FOOD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });



        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("name", food.getName());
                intent.putExtra("type", food.getType());
                intent.putExtra("expiry", food.getExpiry());
                intent.putExtra("expected", food.getExpected());
                intent.putExtra("image", food.getImage());
                context.startActivity(intent);

            }
        });


        holder.favbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (food.getFavStatus().equals("0")) {
                    try{
                        FoodList.sqLiteHelper.updateData(
                                food.getName(),
                                food.getType(),
                                food.getExpiry(),
                                food.getExpected(),
                                food.getImage(),
                                "1",
                                position
                        );
                        food.setFavStatus("1");
                        holder.favbtn.setBackgroundResource(R.drawable.dil);
                        Toast.makeText(context, "Added to favorite: "+food.getFavStatus(), Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception error) {
                        Log.e("Update error", error.getMessage());
                    }

                } else {
                    try{
                        FoodList.sqLiteHelper.updateData(
                                food.getName(),
                                food.getType(),
                                food.getExpiry(),
                                food.getExpected(),
                                food.getImage(),
                                "0",
                                position
                        );
                        food.setFavStatus("0");
                        holder.favbtn.setBackgroundResource(R.drawable.dil);
                        Toast.makeText(context, "Removed from favorite: "+food.getFavStatus(), Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception error) {
                        Log.e("Update error", error.getMessage());
                    }


                }
            }
        });
    }


    private void showDialogDelete(final int idFood){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(context);

        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure you want to this delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    FoodList.sqLiteHelper.deleteData(idFood);
                    Toast.makeText(context, "Delete successfully!!! ",Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, Reload.class));
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                //updateFoodList();
            }
        });

        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    ImageView imageViewFood;
    private void showDialogUpdate(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_food_activity);
        dialog.setTitle("Update");

        imageViewFood = (ImageView) dialog.findViewById(R.id.imageViewFood);
        final EditText edtName = (EditText) dialog.findViewById(R.id.edtName);
        final EditText edtType = (EditText) dialog.findViewById(R.id.etType);
        final EditText edtExpiry = (EditText) dialog.findViewById(R.id.etExpiryDate);
        final EditText edtExpected = (EditText) dialog.findViewById(R.id.etExpected);
        final EditText edtUpdate = (EditText) dialog.findViewById(R.id.etUpdate);

        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageViewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FoodList.sqLiteHelper.updateData(
                            edtName.getText().toString().trim(),
                            edtType.getText().toString().trim(),
                            edtExpiry.getText().toString().trim(),
                            edtExpected.getText().toString().trim(),
                            MainActivity.imageViewToByte(imageViewFood),
                            edtUpdate.getText().toString(),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(context, "Update successfully!!!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error) {
                    Log.e("Update error", error.getMessage());
                }
               // updateFoodList();
            }
        });
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtName;
        RelativeLayout root;
        TextView edtExpiry;
        Button favbtn;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgFood);
            txtName = itemView.findViewById(R.id.txtName);
            root = itemView.findViewById(R.id.root);
            edtExpiry =  itemView.findViewById(R.id.textView7);
            favbtn = itemView.findViewById(R.id.favbtn);

        }
    }
}
