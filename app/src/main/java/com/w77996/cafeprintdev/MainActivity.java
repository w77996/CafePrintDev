package com.w77996.cafeprintdev;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.w77996.cafeprintdev.broadcast.ServerManager;

import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ServerManager mServerManager;
    private TextView mTvMessage;

   // private LoadingDialog mDialog;
    private List<String> mAddressList;
    private Bitmap mBitmap;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        ZXingLibrary.initDisplayOpinion(this);

        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_stop).setOnClickListener(this);
        findViewById(R.id.btn_browse).setOnClickListener(this);

        mTvMessage = (TextView) findViewById(R.id.tv_message);
        imageView = (ImageView)findViewById(R.id.img);
        // AndServer run in the service.
        mServerManager = new ServerManager(this);
        mServerManager.register();

        // startServer;
        findViewById(R.id.btn_start).performClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mServerManager.unRegister();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_start: {
                //showDialog();
                mServerManager.startService();
                break;
            }
            case R.id.btn_stop: {
                mServerManager.stopService();
                break;
            }
            case R.id.btn_browse: {
                if (mAddressList != null) {
                    String address = mAddressList.get(1);

                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(address);
                    intent.setData(content_url);
                    startActivity(intent);
                }
                break;
            }
        }
    }

    /**
     * Start notify.
     */
    public void serverStart(String ip) {
      //  closeDialog();
        if (!TextUtils.isEmpty(ip)) {
            mAddressList = new LinkedList<>();
            mAddressList.add(getString(R.string.server_start_succeed));
            mAddressList.add("http://" + ip + ":8080/demo.html");
            mBitmap = CodeUtils.createImage("http://" + ip + ":8080/demo.html", 400, 400, null);
            imageView.setImageBitmap(mBitmap);
            mAddressList.add("http://" + ip + ":8080/index.html");
            mAddressList.add("http://" + ip + ":8080/image");
            mAddressList.add("http://" + ip + ":8080/download");
            mAddressList.add("http://" + ip + ":8080/upload");
        }

        mTvMessage.setText(TextUtils.join(",\n", mAddressList));
    }

    /**
     * Error notify.
     */
    public void serverError(String message) {
      //  closeDialog();
        mTvMessage.setText(message);
    }

    /**
     * Started notify.
     */
    public void serverHasStarted(String ip) {
        serverStart(ip);
    }

    /**
     * Stop notify.
     */
    public void serverStop() {
       // closeDialog();
        mAddressList = null;
        mTvMessage.setText(R.string.server_stop_succeed);
    }

   /* private void showDialog() {
        if (mDialog == null)
            mDialog = new LoadingDialog(this);
        if (!mDialog.isShowing()) mDialog.show();
    }

    private void closeDialog() {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }*/

}
