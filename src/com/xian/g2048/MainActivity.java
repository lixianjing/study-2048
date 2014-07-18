package com.xian.g2048;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


    private int score = 0;
    private int bestScore = 0;
    private TextView tvScore, tvBestScore;
    private RelativeLayout root = null;
    private GameView gameView;
    private AnimLayer animLayer = null;
    private long mExitTime;

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
        gameView.setActivity(this);

        LayoutParams params = gameView.getLayoutParams();
        params.width = min;
        params.height = min;


        animLayer = (AnimLayer) findViewById(R.id.animLayer);


        if (savedInstanceState != null) {
            loadGame(savedInstanceState);
        } else {
            loadGame();
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
                break;
            case R.id.action_share:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clearScore() {
        score = 0;
        showScore();
    }

    public void showScore() {
        tvScore.setText(score + "");
    }

    public void addScore(int s) {
        score += s;
        showScore();

        bestScore = Math.max(score, bestScore);
        showBestScore();
    }


    public void showBestScore() {
        tvBestScore.setText(bestScore + "");
    }

    public AnimLayer getAnimLayer() {
        return animLayer;
    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

    }



    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        gameView.setActivity(null);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        saveGame(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                saveGame();
                finish();
            }
            return true;
        }
        // 拦截MENU按钮点击事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    private void loadGame() {
        String data = getPreferences(MODE_PRIVATE).getString(Config.SP_KEY_CURRENT_DATA, null);
        score = getPreferences(MODE_PRIVATE).getInt(Config.SP_KEY_CURRENT_SCORE, 0);
        bestScore = getPreferences(MODE_PRIVATE).getInt(Config.SP_KEY_BEST_SCORE, 0);
        gameView.setData(data);
        showScore();
        showBestScore();
    }

    private void loadGame(Bundle bundle) {
        String data = bundle.getString(Config.SP_KEY_CURRENT_DATA);
        score = bundle.getInt(Config.SP_KEY_CURRENT_SCORE, 0);
        bestScore = bundle.getInt(Config.SP_KEY_BEST_SCORE, 0);
        gameView.setData(data);
        showScore();
        showBestScore();
    }



    private void saveGame() {
        String data = gameView.saveGame();
        Editor e = getPreferences(MODE_PRIVATE).edit();
        e.putString(Config.SP_KEY_CURRENT_DATA, data);
        e.putInt(Config.SP_KEY_CURRENT_SCORE, score);
        e.putInt(Config.SP_KEY_BEST_SCORE, bestScore);
        e.commit();
    }


    private void saveGame(Bundle bundle) {
        String data = gameView.saveGame();
        bundle.putString(Config.SP_KEY_CURRENT_DATA, data);
        bundle.putInt(Config.SP_KEY_CURRENT_SCORE, score);
        bundle.putInt(Config.SP_KEY_BEST_SCORE, bestScore);
    }

}
