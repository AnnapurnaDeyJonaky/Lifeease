package com.example.lifeease;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DoctorAdapter extends CursorAdapter {

    public DoctorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewName = view.findViewById(R.id.text_view_doctor_name);
        TextView textViewSpecialization = view.findViewById(R.id.text_view_specialization);
        TextView textViewPhone = view.findViewById(R.id.text_view_phone);
        TextView textViewFee = view.findViewById(R.id.text_view_fee);
        TextView textViewVisitingTime = view.findViewById(R.id.text_view_visiting_time);
        ImageView imageViewDoctor = view.findViewById(R.id.image_view_doctor);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCTOR_NAME));
        String specialization = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_SPECIALIZATION));
        String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE));
        double fee = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FEE));
        String visitingTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_VISITING_TIME));
        byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DOCTOR_IMAGE_URI));

        textViewName.setText(name);
        textViewSpecialization.setText("Specialization: " + specialization);
        textViewPhone.setText("Phone: " + phone);
        textViewFee.setText("Fee: ₹" + fee);
        textViewVisitingTime.setText("Visiting Time: " + visitingTime);

        if (image != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageViewDoctor.setImageBitmap(bitmap);
        } else {
            imageViewDoctor.setImageResource(R.drawable.person); // Default placeholder
        }
    }
}
