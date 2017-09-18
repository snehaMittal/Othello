package com.javahelps.othello;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    int row , column ;
    int currentPlayer = 0 ;
    private static int[][] board = new int[10][10];
    boolean done = false;
    Map<Integer,Integer> valid = new HashMap<Integer,Integer>();
    RadioButton playerWhite , playerBlack ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        playerWhite = (RadioButton)findViewById(R.id.player1radio);
        playerBlack = (RadioButton)findViewById(R.id.player2radio);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        String black = i.getStringExtra("Black");
        String white = i.getStringExtra("White");

        playerBlack.setText(black);
        playerWhite.setText(white);
        playerWhite.setChecked(true);
        playerBlack.setChecked(false);

        start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.newgame){
            start();
        }
        else if (id == R.id.back){
            Intent i = new Intent(MainActivity.this , OpenActivity.class);
            startActivity(i);
        }
        return true ;
    }

    private void start() {
        genOthelloBoard();
        checkValidMoves(currentPlayer);
        updateBoard();
    }

    private void genOthelloBoard() {
        for(int i = 0;i<10;i++){
            for(int j = 0;j<10;j++ ){
                board[i][j] = -1;
            }
        }
        board[4][4] = 0;
        board[4][5] = 1;
        board[5][4] = 1;
        board[5][5] = 0;
    }

    public void onButtonClick(View view){
        int tagID = Integer.parseInt(view.getTag().toString());
        row = tagID/10 ;
        column = tagID%10 ;

        changes(currentPlayer , row, column);
        updateBoard();

        if (currentPlayer == 0)
            currentPlayer = 1;
        else
            currentPlayer = 0 ;

        checkValidMoves(currentPlayer);
        updateBoard();
        updateData();
        checkWinners();
    }

    TextView score1 , score2 ;
    private void updateData() {
        score1 = (TextView)findViewById(R.id.player1score);
        score2 = (TextView)findViewById(R.id.player2score);
        if(currentPlayer == 0){
            playerWhite.setChecked(true);
            playerBlack.setChecked(false);
        }
        else{
            playerBlack.setChecked(true);
            playerWhite.setChecked(false);
        }
        Integer white = countValue(0);
        score1.setText(white.toString());
        Integer black = countValue(1);
        score2.setText(black.toString());
    }

    private void updateBoard() {
        GridLayout gl = (GridLayout) findViewById(R.id.grid);
        for(int i = 1;i<=8;i++){
            for(int j = 1;j<=8;j++){
                ImageButton ib = (ImageButton) gl.findViewWithTag(((Integer)(10*i+j)).toString());

                if(valid.containsKey(10*i+j)){
                    ib.setEnabled(true);
                    ib.setAlpha(1F);
                }
                else{
                    if(board[i][j] == 2){
                        board[i][j] = -1;
                    }
                    ib.setEnabled(false);
                    ib.setAlpha(0.6F);
                }
                if(board[i][j] == -1){
                    ib.setImageResource(R.drawable.green_dark);
                }
                else if(board[i][j] == 0){
                    ib.setImageResource(R.drawable.white_gree_dark);
                }
                else if(board[i][j] == 1){
                    ib.setImageResource(R.drawable.black_green_dark);
                }
                else
                    ib.setImageResource(R.drawable.green_dark_valid);
            }
        }
    }

    private void checkWinners(){
        if(valid.isEmpty()){
            if(done == true){
                if(countValue(0) >  countValue(1)){
                    Toast t = Toast.makeText(this, " White Wins", Toast.LENGTH_SHORT);
                    t.show();
                }
                else if(countValue(0) <  countValue(1)){
                    Toast t = Toast.makeText(this," Black Wins", Toast.LENGTH_SHORT);
                    t.show();
                }
                else {
                    Toast t = Toast.makeText(this, "Draw Match", Toast.LENGTH_SHORT);
                    t.show();
                }
            }
            else{
                done = true;
                currentPlayer = currentPlayer == 0?1:0;
                checkValidMoves(currentPlayer);
                updateBoard();
                updateData();
                checkWinners();
            }
        }
        else{
            done = false;
        }
    }

    private int countValue(int value) {
        int temp = 0;
        for(int i = 1;i<9;i++){
            for(int j = 1;j<9;j++){
                if(board[i][j] == value)
                    temp++;
            }
        }
        return temp;
    }

    private void checkValidMoves(int value){
        valid.clear();
        int[] x = {1,-1,1,-1,1,-1,0,0};
        int[] y = {0,0,1,-1,-1,1,1,-1};
        for(int m = 1;m<=8;m++){
            for(int n = 1;n<=8;n++){
                if(board[m][n] == -1 || board[m][n] == 2){
                    for(int i = 0;i<8;i++){
                        int j = m;
                        int k = n;
                        boolean flipped = false;
                        while ((j<9&&j>0) && (k>0&&k <9)){
                            if(j > 8 && x[i] == 1)
                                break;
                            if(k > 8 && y[i] == 1)
                                break;
                            if(j < 1 && x[i] == -1)
                                break;
                            if(k < 1 && y[i] == -1)
                                break;
                            j += x[i];
                            k += y[i];
                            if(board[j][k] == -1 || board[j][k] == 2){
                                break;
                            }
                            else if(board[j][k] == value){
                                if(flipped){
                                    valid.put(10*m+n, 10*n+m);
                                    board[m][n] = 2;
                                }
                                break;
                            }
                            else {
                                flipped = true;
                            }
                        }
                    }
                }
            }
        }
    }
    private void changes(int value , int row ,int column){

        int temp ;
        board[row][column] = value ;
        int[] x = {1,-1,1,-1,1,-1,0,0};
        int[] y = {0,0,1,-1,-1,1,1,-1};

        if (value == 0)
            temp = 1;
        else
            temp = 0 ;

        for (int i=0 ; i<8 ; i++){
            int j = row ;
            int k = column ;
            boolean flipped = false ;

            try{
                while ((j<9&&j>0) && (k>0&&k <9)){
                    if(j > 8 && x[i] == 1)
                        break;
                    if(k > 8 && y[i] == 1)
                        break;
                    if(j < 1 && x[i] == -1)
                        break;
                    if(k < 1 && y[i] == -1)
                        break;
                    j += x[i];
                    k += y[i];
                    if(board[j][k] == -1 || board[j][k] == 2){
                        throw new InvalidOthelloException();
                    }
                    else if(board[j][k] == value){
                        if(!flipped)
                            throw new InvalidOthelloException();
                        break;
                    }
                    else{
                        board[j][k] = value;
                        flipped = true;
                    }
                }
            }
            catch (InvalidOthelloException ex){
                j -= x[i];
                k -= y[i];
                while(j!=row || k!= column){
                    board[j][k] = temp;
                    j -= x[i];
                    k -= y[i];
                }
            }
        }
    }
}
