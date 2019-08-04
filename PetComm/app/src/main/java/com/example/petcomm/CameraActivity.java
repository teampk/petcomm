package com.example.petcomm;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivityCameraBinding;
import com.example.petcomm.model.Dog;
import com.example.petcomm.model.FeedSchedule;
import com.example.petcomm.model.Res;
import com.example.petcomm.network.NetworkUtil;
import com.example.petcomm.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class CameraActivity extends AppCompatActivity {

    ActivityCameraBinding binding;
    private Dog selectedDog;
    private CompositeSubscription mSubscriptions;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera);
        binding.setCamera(this);
        mSubscriptions = new CompositeSubscription();

        Intent intent = getIntent();
        selectedDog = (Dog) intent.getSerializableExtra("dog");

        setView();

    }



    public void setView(){

        binding.tvDogName.setText(selectedDog.dogName);
        binding.tvDeviceId.setText(selectedDog.feederId);

        binding.wvCamera.getSettings().setJavaScriptEnabled(true);
        binding.wvCamera.loadUrl(Constants.CCTV_URL);
        binding.wvCamera.setWebChromeClient(new WebChromeClient());
        binding.wvCamera.setWebViewClient(new WebViewClientClass());
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            Log.d("CheckingURL", url);
            view.loadUrl(url);
            return true;
        }
    }
/*
    public void speakerListener(View view){
        mSubscriptions.add(NetworkUtil.getRetrofit().playVoice(selectedDog)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleFeedResponseVoice,this::handleError));
    }
*/


    public void feedListener(View view){

        CustomDialog customDialogFeed = new CustomDialog(CameraActivity.this);
        customDialogFeed.callFunction(1, "배식할 양을 설정해주세요.", "배식", "취소");
        customDialogFeed.setDialoglistener(new CustomDialog.CustomDialogListener() {
            @Override
            public void onPositiveClicked(String feedAmount, String a) {
                FeedSchedule feedManually = new FeedSchedule(0, selectedDog.feederId,0,  "99:99", feedAmount);
                feedManually(feedManually);

            }

            @Override
            public void onNegativeClicked() {
                Toast.makeText(CameraActivity.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void feedManually(FeedSchedule feedManually){
        mSubscriptions.add(NetworkUtil.getRetrofit().feedManually(feedManually)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleFeedResponseFeed,this::handleError));
    }

    private void handleFeedResponseFeed(Res res){
        Toast.makeText(CameraActivity.this, res.getMessage()+"g 배식되었습니다.", Toast.LENGTH_SHORT).show();
    }
    private void handleFeedResponseVoice(Res res){
        Toast.makeText(CameraActivity.this, String.valueOf(res.getMessage()), Toast.LENGTH_SHORT).show();
    }

    private void handleError(Throwable error) {
        if (error instanceof HttpException) {
            Gson gson = new GsonBuilder().create();
            try {
                String errorBody = ((HttpException) error).response().errorBody().string();
                Res response = gson.fromJson(errorBody, Res.class);
                Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("NetworkERROR", String.valueOf(error));
            Toast.makeText(getApplicationContext(), "NETWORK ERROR :(", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }

    public void speakerListener(View view){
        Log.d("PAENGCamera", "Speaker Clicked");
        mSubscriptions.add(NetworkUtil.getRetrofitCameraShutDown().cctvCameraShutDown()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleFeedResponseCamera,this::handleError));
    }

    private void handleFeedResponseCamera(Res res){
        Toast.makeText(this, res.getMessage(), Toast.LENGTH_SHORT).show();
        finish();
    }


/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//뒤로가기 버튼 이벤트
        if ((keyCode == KeyEvent.KEYCODE_BACK) && binding.wvCamera.canGoBack()) {//웹뷰에서 뒤로가기 버튼을 누르면 뒤로가짐
            binding.wvCamera.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
*/

}
