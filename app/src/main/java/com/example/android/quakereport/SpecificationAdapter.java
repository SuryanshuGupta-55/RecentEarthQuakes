package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpecificationAdapter extends ArrayAdapter<Specification> {
    private static final String search=" of ";

    public SpecificationAdapter(Activity context, ArrayList<Specification> specs){
        super(context,0,specs);
    }
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
    private String formatMagnitude(Double mag){
        DecimalFormat formatter = new DecimalFormat("0.0");
        return formatter.format(mag);
    }


    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    private int getMagnitudeColor(double mag){
        int magnitude;
        int magnitudefloor=(int)(Math.floor(mag));
        switch (magnitudefloor){
            case 0:
            case 1:magnitude=(R.color.magnitude1);
            break;
            case 2:magnitude=(R.color.magnitude2);
            break;
            case 3:magnitude=(R.color.magnitude3);
            break;
            case 4:magnitude=(R.color.magnitude4);
            break;
            case 5:magnitude=(R.color.magnitude5);
            break;
            case 6:magnitude=(R.color.magnitude6);
            break;
            case 7:magnitude=(R.color.magnitude7);
            break;
            case 8:magnitude=(R.color.magnitude8);
            break;
            case 9:magnitude=(R.color.magnitude9);
            break;
            default:magnitude=(R.color.magnitude10plus);
        }
        return ContextCompat.getColor(getContext(),magnitude);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String orignal="";
        String first="";
        String last="";
        View listItemView = convertView;
        if (listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Specification currentSpecs = getItem(position);

        TextView magnitude=(TextView) listItemView.findViewById(R.id.mag_id);
        String formattedMagnitude = formatMagnitude(currentSpecs.getmMagnitude());
        // Display the magnitude of the current earthquake in that TextView
        magnitude.setText(formattedMagnitude);

        magnitude.setText(formattedMagnitude);


        orignal=currentSpecs.getmLocation();
        if(orignal.contains(search)){
            String[] parts=orignal.split(search);
            first=parts[0] + search;
            last=parts[1];
        }
        else{
            first=getContext().getString(R.string.near_the);
            last=orignal;
        }
        TextView first_location=(TextView) listItemView.findViewById(R.id.location1_id);
        first_location.setText(first);
        TextView last_location=(TextView) listItemView.findViewById(R.id.location2_id);
        last_location.setText(last);

        Date dateObject = new Date(currentSpecs.getmTimeinMilliSecond());

        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date_id);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        // Display the date of the current earthquake in that TextView
        dateView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time_id);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentSpecs.getmMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        return listItemView;
    }
}
