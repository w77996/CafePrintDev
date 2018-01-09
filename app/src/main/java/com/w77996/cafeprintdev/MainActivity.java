package com.w77996.cafeprintdev;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;

public class MainActivity extends AppCompatActivity {
    Server mServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndServer andServer = new AndServer.Build()
                .listener(mListener)
                .port(8080) // 默认是8080，Android平台允许的端口号都可以。
                .timeout(10 * 1000) // 默认10 * 1000毫秒。
                .build();

// 创建服务器。
         mServer = andServer.createServer();

// 启动服务器。
        mServer.start();



    }
    private Server.Listener mListener = new Server.Listener() {
        @Override
        public void onStarted() {
            // 服务器启动成功.
        }

        @Override
        public void onStopped() {
            // 服务器停止了，一般是开发者调用server.stop()才会停止。
        }

        @Override
        public void onError(Exception e) {
            // 服务器启动发生错误，一般是端口被占用。
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止服务器。
        mServer.stop();

// 服务器正在运行吗？
        boolean running = mServer.isRunning();
    }
}
