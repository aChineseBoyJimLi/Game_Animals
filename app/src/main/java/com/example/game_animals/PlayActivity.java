package com.example.game_animals;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class PlayActivity extends AppCompatActivity {

    private ProgressBar m_progressBar = null; // 进度条对象
    private int m_iFirstProgress = 100;       // 第一进度的值
    private int m_iSencodProgress = 100;      // 第二进度的值

    private int[] m_iImageId = {R.id.option_1, R.id.option_2, R.id.option_3, R.id.option_4};    // 四张图的 id

    private int m_iRightAnswerId = 0;         // 正确的回答的 id

    private MediaPlayer m_MediaPlayer;        // 背景音乐

    private HashMap<Integer, Integer> spMap = new HashMap();
    private SoundPool soundPools;


    private void InitSounds(){
        soundPools = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        spMap.put(0, soundPools.load(this, R.raw.win, 1));
        spMap.put(1, soundPools.load(this, R.raw.end, 1));
        spMap.put(2, soundPools.load(this, R.raw.right, 1));
        spMap.put(3, soundPools.load(this, R.raw.wrong, 1));
    }

    private void PlaySound(int sound){
        AudioManager am = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumnRatio = volumnCurrent / audioMaxVolumn;
        soundPools.play(spMap.get(sound), volumnRatio * 4, volumnRatio * 4, 1,0,1);
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(m_iFirstProgress > 0){
                m_iFirstProgress -= msg.what * 2;
                m_iSencodProgress -= msg.what;
                m_progressBar.setProgress(m_iFirstProgress);
                m_progressBar.setSecondaryProgress(m_iSencodProgress);

                // 游戏胜利
                if(s_iCount == 10){
                    timer.cancel();
                    m_MediaPlayer.pause();
                    PlaySound(0);
                    ShowWinDialog("Congratulations !", "Do you want to play again ?");
                }
            }
            else{
                // 游戏失败
                timer.cancel();
                m_MediaPlayer.pause();
                PlaySound(1);
                ShowWinDialog("Sorry", "You didn't find all the different images in a limited time.");
            }
            super.handleMessage(msg);
        }
    };

    Timer timer = new Timer();
    TimerTask timerTask = new TimerTask(){
        @Override
        public void run(){
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        m_MediaPlayer = MediaPlayer.create(this, R.raw.happy_tree_friends);
        m_MediaPlayer.start();
        m_MediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });

        InitSounds();

        RefreshImages();

        m_progressBar = findViewById(R.id.progress_bar_h);
        timer.schedule(timerTask,0,200);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        m_MediaPlayer.release();
    }

    // 点击图片
    public void imageClick(View view){
        CheckAnswers(view.getId());
    }

    // 检查答案
    private void CheckAnswers(int id){
        if(id == m_iRightAnswerId){
            m_iFirstProgress = m_iSencodProgress;
            PlaySound(2);
            addCount();
        }
        else{
            m_iSencodProgress = m_iFirstProgress;
            PlaySound(3);
        }
        RefreshImages();
    }

    // 刷新关卡
    private void RefreshImages(){
        Random random = new Random();
        int max, min;

        // 生产随机答案
        max = 3; min = 0;
        int right_index = random.nextInt(max+2)%(max-min+1) + min;
        m_iRightAnswerId = m_iImageId[right_index];

        // 选择随机同类组
        max=13; min=0;
        int same_group_index_1 = random.nextInt(max+2)%(max-min+1) + min;

        // 选择不同类的一个元素
        max=2; min=0;
        int diff_group_index_2 = random.nextInt(max+2)%(max-min+1) + min;
        int diff_group_index_1 = 0;
        if(same_group_index_1 <= 4){
            max=13; min=10;
            diff_group_index_1 = random.nextInt(max+2)%(max-min+1) + min;
        }
        if(same_group_index_1 > 4 &&same_group_index_1 < 6){
            max=13; min=8;
            diff_group_index_1 = random.nextInt(max+2)%(max-min+1) + min;
        }
        if(same_group_index_1 == 6 ){
            max=13; min=8;
            diff_group_index_1 = random.nextInt(max+2)%(max-min+1) + min;
        }
        if(same_group_index_1 == 7 ){
            max=13; min=8;
            diff_group_index_1 = random.nextInt(max+2)%(max-min+1) + min;
        }
        if(same_group_index_1 > 7 &&same_group_index_1 < 10){
            max=13; min=10;
            diff_group_index_1 = random.nextInt(max+2)%(max-min+1) + min;
        }
        if(same_group_index_1 == 10){
            if(random.nextBoolean()){
                max=9; min=0;
                diff_group_index_1 = random.nextInt(max+2)%(max-min+1) + min;
            }
            else{
                max=13; min=10;
                diff_group_index_1 = random.nextInt(max+2)%(max-min+1) + min;
            }
        }

        if(same_group_index_1 > 10 && same_group_index_1 < 13){
            if(random.nextBoolean()){
                max=10; min=0;
                diff_group_index_1 = random.nextInt(max+2)%(max-min+1) + min;
            }
            else{
                diff_group_index_1 = 13;
            }
        }

        if(same_group_index_1 == 13){
            max=13; min=0;
            diff_group_index_1 = random.nextInt(max+2)%(max-min+1) + min;
        }

        System.out.println(m_iRightAnswerId);
        System.out.println(same_group_index_1);
        System.out.println(diff_group_index_1);
        System.out.println(diff_group_index_2);

        ((TextView) findViewById(R.id.count_view)).setText(s_iCount + "/10");

        for(int i = 0, j=0; i<m_iImageId.length; i++){
            if(m_iImageId[i] == m_iRightAnswerId){
                ((ImageView) findViewById(m_iImageId[i])).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),m_aType[diff_group_index_1][diff_group_index_2]));
            }
            else{
                ((ImageView) findViewById(m_iImageId[i])).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),m_aType[same_group_index_1][j]));
                j++;
            }
        }
    }

    // 对图片的分类
    private int[][] m_aType = {
            {R.drawable.bear, R.drawable.cat, R.drawable.elephant},         // 哺乳动物 1
            {R.drawable.giraffe, R.drawable.hypo, R.drawable.kangaroo},     // 哺乳动物 2
            {R.drawable.leo, R.drawable.lion, R.drawable.pig},              // 哺乳动物 3
            {R.drawable.dog, R.drawable.tiger, R.drawable.wolf},            // 哺乳动物 4

            {R.drawable.bear, R.drawable.leo, R.drawable.lion},             // 食肉动物 1
            {R.drawable.lion, R.drawable.tiger, R.drawable.wolf},           // 食肉动物 2

            {R.drawable.leo, R.drawable.lion, R.drawable.tiger},            // 猫科动物

            {R.drawable.bear, R.drawable.wolf, R.drawable.dog},             // 犬科动物

            {R.drawable.elephant, R.drawable.giraffe, R.drawable.hypo},     // 食草动物 1
            {R.drawable.kangaroo, R.drawable.pig, R.drawable.rhino},        // 食草动物 2

            {R.drawable.fish, R.drawable.frog, R.drawable.crocodile},       // 喜水动物

            {R.drawable.bird, R.drawable.penguin, R.drawable.owl},          // 鸟类
            {R.drawable.bird, R.drawable.honey, R.drawable.owl},            // 能飞的

            {R.drawable.flower, R.drawable.house, R.drawable.sun}           // 非动物
    };

    static public int s_iCount = 0; // 答对题目的题数

    static public void addCount(){  // 累加对题数
        s_iCount += 1;
    }

    static public void goZero(){    // 重置计数器
        s_iCount = 0;
    }

    private void ShowWinDialog(String title, String message){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(PlayActivity.this);
        normalDialog.setTitle(title);
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("Play again",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goZero();
                        m_iFirstProgress = 100;
                        m_iSencodProgress = 100;
                        timer = new Timer();
                        timerTask = new TimerTask(){
                            @Override
                            public void run(){
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                        };
                        timer.schedule(timerTask,0,100);
                        m_MediaPlayer.start();
                        RefreshImages();
                    }
            });
        normalDialog.setNegativeButton("Back to start",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());   //获取PID
                        System.exit(0);
                    }
            });
        // 显示
        normalDialog.show();
    }
}
