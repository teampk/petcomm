package com.example.petcomm;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class CustomDialog {
    private Context context;
    private TextView tvTitle, tvFeedAmount, tvFeedTime;
    private SeekBar sbFeedAmount;
    private TimePicker timePicker;

    private CustomDialogListener customDialogListener;

    public CustomDialog(Context context){
        this.context = context;
    }

    interface CustomDialogListener{
        void onPositiveClicked(String feedTime, String feedAmount);
        void onNegativeClicked();
    }
    public void setDialoglistener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }

    public void callFunction(int dialogMode, String dialogTitle, String dialogBtOkay, String dialogBtCancel){
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialogMode == 1) {
            dlg.setContentView(R.layout.custom_dialog_feed);
        }else if (dialogMode == 2){
            dlg.setContentView(R.layout.custom_dialog_feed_auto);
        }
        dlg.show();


        // common variable
        final Button btOk = dlg.findViewById(R.id.bt_dialog_ok);
        final Button btCancel = dlg.findViewById(R.id.bt_dialog_cancel);
        tvTitle = dlg.findViewById(R.id.tv_dialog_title);
        btOk.setText(dialogBtOkay);
        btCancel.setText(dialogBtCancel);
        tvTitle.setText(dialogTitle);

        btCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dlg.dismiss();
            }
        });
        /////////////////////////////////////////////

        if (dialogMode == 1){
            sbFeedAmount = dlg.findViewById(R.id.sb_feed_amount);
            tvFeedAmount = dlg.findViewById(R.id.tv_feed_amount);
            sbFeedAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tvFeedAmount.setText(String.valueOf(progress*5));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            btOk.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(checkFeedMode1()){
                        String feedAmount = tvFeedAmount.getText().toString();
                        Toast.makeText(context, feedAmount+"gram 배식 완료", Toast.LENGTH_SHORT).show();
                        dlg.dismiss();
                    }
                }
            });


        }else if(dialogMode == 2){
            sbFeedAmount = dlg.findViewById(R.id.sb_feed_amount);
            tvFeedAmount = dlg.findViewById(R.id.tv_feed_amount);
            tvFeedTime = dlg.findViewById(R.id.tv_feed_time);
            timePicker = dlg.findViewById(R.id.time_picker);
            timePicker.setCurrentHour(0);
            timePicker.setCurrentMinute(0);


            sbFeedAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tvFeedAmount.setText(String.valueOf(progress*5));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });



            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    String hourString, minuteString;
                    if (hourOfDay<10){
                        hourString = "0"+hourOfDay;
                    }else{
                        hourString = String.valueOf(hourOfDay);
                    }
                    if (minute<10){
                        minuteString = "0"+minute;
                    }else{
                        minuteString = String.valueOf(minute);
                    }


                    tvFeedTime.setText(hourString+":"+minuteString);
                }
            });


            btOk.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(checkFeedMode2()){
                        String feedTime = tvFeedTime.getText().toString();
                        String feedAmount = tvFeedAmount.getText().toString();
                        customDialogListener.onPositiveClicked(feedTime, feedAmount);
                        dlg.dismiss();
                    }
                }
            });




        }
    }

    private boolean checkFeedMode1(){
        if(Integer.valueOf(tvFeedAmount.getText().toString())==0){
            Toast.makeText(context, "0g은 배식되지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean checkFeedMode2(){
        if(Integer.valueOf(tvFeedAmount.getText().toString())==0) {
            Toast.makeText(context, "0g은 배식되지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
