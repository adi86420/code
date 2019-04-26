package com.example.achen.surfaceviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    GameSurface gameSurface;
    int change = 0;
    ConstraintLayout layout;
    int enemChange = 14;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameSurface = new GameSurface(this);
        setContentView(gameSurface);

    }

    @Override
    protected void onPause(){
        super.onPause();
        gameSurface.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        gameSurface.resume();
    }



    public class GameSurface extends SurfaceView implements Runnable,SensorEventListener{

        Thread gameThread;
        SurfaceHolder holder;
        volatile boolean running = false;
        Bitmap ball;
        Bitmap enem;
        int enemX = 0;
        int enemY = -250;
        int ballX=0;
        int x=200;
        String sensorOutput="";
        Paint paintProperty;
        int count = 0;
        String time = "30";
        boolean hit = false;//hit bool
        boolean noise = false;
        boolean over = false;
        int score = 0;//score var

        int screenWidth;
        int screenHeight;

        MediaPlayer backgroundMusic;
        MediaPlayer hitNoise;
        Handler thread;

        public GameSurface(Context context) {
            super(context);

            holder=getHolder();

            ball= BitmapFactory.decodeResource(getResources(),R.drawable.greencar);
            enem = BitmapFactory.decodeResource(getResources(),R.drawable.purplecar);

            Display screenDisplay = getWindowManager().getDefaultDisplay();
            Point sizeOfScreen = new Point();
            screenDisplay.getSize(sizeOfScreen);
            screenWidth=sizeOfScreen.x;
            screenHeight=sizeOfScreen.y;

            SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this,accelerometerSensor,sensorManager.SENSOR_DELAY_NORMAL);

            paintProperty= new Paint();
            paintProperty.setTextSize(100);

            backgroundMusic = MediaPlayer.create(context, R.raw.background);
            backgroundMusic.start();

            hitNoise = MediaPlayer.create(context, R.raw.hit);

            thread = new Handler();
        }

        @Override
        public void run() {
            while (running == true){
                if (holder.getSurface().isValid() == false)
                    continue;

                Canvas canvas = holder.lockCanvas();

                if(!over) {
                    if (count == 0) {
                        thread.post(getRunnable());
                        count++;
                    }

                    canvas.drawRGB(0, 0, 150);

                    gameSurface.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (enemChange == 14)
                                enemChange = 28;
                            else if (enemChange == 28)
                                enemChange = 14;
                        }
                    });

                    if ((((screenWidth / 2) - ball.getWidth() / 2 + ballX) - enemX < 250 && ((screenWidth / 2) - ball.getWidth() / 2 + ballX) - enemX > -200) &&
                            (enemY - ((screenHeight) - (3 * ball.getHeight())) > -200 && enemY - ((screenHeight) - (3 * ball.getHeight())) < 200)) {
                        ball = BitmapFactory.decodeResource(getResources(), R.drawable.damagedcar);
                        hit = true;//no point
                        if (!noise) {
                            hitNoise.start();
                            noise = true;
                        }
                    }

                    if (enemY > ((screenHeight) - (3 * ball.getHeight())) && !hit) {
                        score++;
                        hit = true;//one point for cycle
                    }

                    canvas.drawBitmap(ball, (screenWidth / 2) - ball.getWidth() / 2 + ballX, (screenHeight) - (3 * ball.getHeight()), null);

                    if (enemY == -250) {
                        enemX = (int) (Math.random() * (screenWidth - 300));
                    }

                    canvas.drawBitmap(enem, enemX, enemY, null);

                    if ((ballX > -1 * (screenWidth / 2 - 120) && ballX < (screenWidth / 2 - 150) ||
                            ((ballX <= -1 * (screenWidth / 2 - 120)) && change > 0) || (ballX >= screenWidth / 2 - 150) && change < 0)) {
                        ballX += change;
                    }

                    if (enemY >= -250 && enemY < screenHeight) {
                        enemY += enemChange;
                    } else if (enemY >= screenHeight) {
                        enemY = -250;
                        ball = BitmapFactory.decodeResource(getResources(), R.drawable.greencar);
                        hit = false;//reset
                        noise = false;
                    }

                    canvas.drawText("Timer: " + time, 25, 100, paintProperty);
                    canvas.drawText("Score: " + score, 660, 100, paintProperty);
                }
                else{
                    canvas.drawRGB(0, 0, 0);
                    paintProperty.setARGB(250,250,250, 250);
                    paintProperty.setTextSize(200);
                    canvas.drawText("Game Over", 43, 700, paintProperty);
                    paintProperty.setTextSize(150);
                    canvas.drawText("Final Score: " + score, 43, 1000, paintProperty);
                }

                holder.unlockCanvasAndPost(canvas);
            }
        }

        public void resume(){
            running=true;
            gameThread=new Thread(this);
            gameThread.start();
        }

        public void pause() {
            running = false;
            while (true) {
                try {
                    gameThread.join();
                } catch (InterruptedException e) {
                }
            }
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if((double)event.values[0] < 0) {
                if ((double) event.values[0] >= -2.8)
                    change = 6;
                else if ((double) event.values[0] < -2.8)
                    change = 12;
            }
            else  if((double)event.values[0] > 0) {
                if ((double) event.values[0] <= 2.8)
                    change = -6;
                else if ((double) event.values[0] > 2.8)
                    change = -12;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        public Runnable getRunnable(){
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    new CountDownTimer(31000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if(String.valueOf(millisUntilFinished).length() == 5)
                                time = (String.valueOf(millisUntilFinished/1000));
                            else if(String.valueOf(millisUntilFinished).length() == 4) {
                                time = (String.valueOf(millisUntilFinished/1000));
                            }
                        }

                        @Override
                        public void onFinish() {
                            time = "0";
                            over = true;
                        }
                    }.start();
                }
            };
            return runnable;
        }
    }
}
