package com.digitosolutions.jogo;

import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class main extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView yellow;
    private ImageView pink;
    private ImageView black;

    // Tamanho
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    // Posiçao
    private int boxY;
    private int yellowY;
    private int yellowX;
    private int pinkY;
    private int pinkX;
    private int blackY;
    private int blackX;

    // Score
    private int score = 0;

    // Iniciar a classe
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    // Check Status
    private boolean action_flg = false;
    private boolean start_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        box = (ImageView) findViewById(R.id.box);
        pink = (ImageView) findViewById(R.id.pink);
        yellow = (ImageView) findViewById(R.id.yellow);
        black = (ImageView) findViewById(R.id.black);

        // Get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size );

        screenWidth = size.x;
        screenHeight = size.y;

        // Mover imagens para fora do ecra
        pink.setX(-80);
        pink.setY(-80);
        yellow.setX(-80);
        yellow.setY(-80);
        black.setX(-80);
        black.setY(-80);

        scoreLabel.setText("Score : 0");
    }

    public boolean onTouchEvent(MotionEvent me) {

        if (start_flg == false) {
            start_flg = true;

            // Porque +e que estamos a ir buscar o valor da box?
            // Porque o UI nao foi defenido no OnCreate()!!

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();

            boxY = (int)box.getY();

            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);


            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }, 0 , 20);

        } else {
            if (me.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;
            } else if (me.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;
            }
        }

        return true;
    }


    public void changePos() {

        hitCheck();

        // Yellow
        yellowX -= 12;
        if (yellowX < 0) {
            yellowX = screenWidth + 20;
            yellowY = (int) Math.floor(Math.random() * (frameHeight - yellow.getHeight()));
        }
        yellow.setX(yellowX);
        yellow.setY(yellowY);

        // Black
        blackX -= 16;
        if (blackX < 0) {
            blackX = screenWidth + 10;
            blackY = (int) Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        // Pink
        pinkX -= 20;
        if (pinkX < 0) {
            pinkX = screenWidth + 5000;
            pinkY = (int) Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        // Move Box
        if (action_flg == true) {
            // Touching
            boxY -= 20;
        } else {
            // Releasing
            boxY += 20;
        }

        // Ver a posicao da box
        if (boxY < 0) boxY = 0;

        if (boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;

        box.setY(boxY);

        scoreLabel.setText("Score : " + score);
    }

    public void hitCheck() {
        // Se o centro da bola bater na box, conta um hit

        // Amarela
        int yellowCenterX = yellowX + yellow.getWidth() / 2;
        int yellowCenterY = yellowY + yellow.getHeight() / 2;

        // 0 <= yellowCenterX <= boxWIdth
        // boxY <= orangeCenterY <= boxY + boxHeight

        if (0 <= yellowCenterX && yellowCenterX <= boxSize && boxY <= yellowCenterY && yellowCenterY
                <= yellowCenterY && yellowCenterY <= boxY + boxSize) {

            score += 10;
            yellowX = -10;

        }

        // Pink
        int pinkCenterX = pinkX + pink.getWidth() / 2;
        int pinkCenterY = pinkY + pink.getHeight() / 2;

        // 0 <= yellowCenterX <= boxWIdth
        // boxY <= orangeCenterY <= boxY + boxHeight

        if (0 <= pinkCenterX && pinkCenterX <= boxSize && boxY <= pinkCenterY && pinkCenterY
                <= pinkCenterY && pinkCenterY <= boxY + boxSize) {

            score += 30;
            pinkX = -10;

        }

        // BLack
        int blackCenterX = blackX + black.getWidth() / 2;
        int blackCenterY = blackY + black.getHeight() / 2;

        // 0 <= yellowCenterX <= boxWIdth
        // boxY <= orangeCenterY <= boxY + boxHeight

        if (0 <= blackCenterX && blackCenterX <= boxSize && boxY <= blackCenterY && blackCenterY
                <= blackCenterY && blackCenterY <= boxY + boxSize) {

            // Stop
            timer.cancel();
            timer = null;

            // Apresentar Resultado
            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }


    }
}
