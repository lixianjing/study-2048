
package com.xian.g2048;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    public MainActivity() {
        mainActivity = this;
    }

    private int score = 0;
    private TextView tvScore, tvBestScore;
    private RelativeLayout root = null;
    private GameView gameView;
    private AnimLayer animLayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();

        int screenWidth = dm.widthPixels; // 屏幕宽（像素，如：480px）
        int screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）

        int min = Math.min(screenWidth, screenHeight);

        root = (RelativeLayout) findViewById(R.id.container);
        root.setBackgroundColor(0xffbbada0);

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);

        gameView = (GameView) findViewById(R.id.gameView);

        LayoutParams params = gameView.getLayoutParams();
        params.width = min;
        params.height = min;


        animLayer = (AnimLayer) findViewById(R.id.animLayer);

        if (savedInstanceState != null) {
            gameView.loadGame(savedInstanceState);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.action_refresh:
                gameView.startGame();
                Toast.makeText(this, "I am action_refresh", 1000).show();
                break;
            case R.id.action_share:
                Toast.makeText(this, "I am action_share", 1000).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clearScore() {
        score = 0;
        showScore();
    }

    public int getScore() {
        return score;
    }

    public void showScore() {
        tvScore.setText(score + "");
    }

    public void addScore(int s) {
        score += s;
        showScore();

        int maxScore = Math.max(score, getBestScore());
        saveBestScore(maxScore);
        showBestScore(maxScore);
    }

    public void saveBestScore(int s) {
        Editor e = getPreferences(MODE_PRIVATE).edit();
        e.putInt(SP_KEY_BEST_SCORE, s);
        e.commit();
    }

    public int getBestScore() {
        return getPreferences(MODE_PRIVATE).getInt(SP_KEY_BEST_SCORE, 0);
    }

    public void showBestScore(int s) {
        tvBestScore.setText(s + "");
    }

    public AnimLayer getAnimLayer() {
        return animLayer;
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("lmf", ">>>>>>>>>onRestoreInstanceState>>>>>>>>>>>");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub

        Log.e("lmf", ">>>>>>>>>onSaveInstanceState>>>>>>>>>>>");

        gameView.saveGame(outState);
        super.onSaveInstanceState(outState);
    }

    private static MainActivity mainActivity = null;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static final String SP_KEY_BEST_SCORE = "bestScore";

}
