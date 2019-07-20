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
import android.widget.Toast;

public class CustomDialog {
    private Context context;
    private TextView tvTitle, tvFeedAmount;
    private SeekBar sbFeedAmount;

    public CustomDialog(Context context){
        this.context = context;
    }

    public void callFunction(int dialogMode, String dialogTitle, String dialogBtOkay, String dialogBtCancel, final ProgressBar pb){
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialogMode == 1){
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

        btOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(checkFeed()){
                    String feedAmount = tvFeedAmount.getText().toString();
                    Toast.makeText(context, feedAmount+"gram 배식 완료", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.INVISIBLE);
                    dlg.dismiss();
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                pb.setVisibility(View.INVISIBLE);
                dlg.dismiss();
            }
        });
        /////////////////////////////

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
        }
        else if (dialogMode == 2){
            RecyclerView recyclerScheduleList = dlg.findViewById(R.id.recycler_schedule_list);
            recyclerScheduleList.setHasFixedSize(true);
            recyclerScheduleList.setLayoutManager(new LinearLayoutManager(context));
            recyclerScheduleList.scrollToPosition(0);


        }


    }

    private boolean checkFeed(){
        if(Integer.valueOf(tvFeedAmount.getText().toString())==0){
            Toast.makeText(context, "0g은 배식되지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
