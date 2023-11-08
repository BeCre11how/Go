import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Game extends JPanel implements MouseListener{
    enum Farbe{
        transparent, schwarz, weiss
    }
    ArrayList<Integer> scoreg = new ArrayList<>();
    boolean p1IsHuman;
    boolean p2IsHuman;
    final int size = 18;
    Farbe otherColor;
    final int depth = 3;
    boolean waitingPlayerInput;
    int[][] board;
    boolean inApp;
    String s;
    boolean[][] possibleMoves;
    Farbe currentTurn;
    final int blockSize = 40;
    boolean inGame;

    public Game(int z){
        s = "";
        addMouseListener(this);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the amount of Players you want please! ");
        int temp = z;
        if(temp % 3 == 0){
            p1IsHuman = false;
            p2IsHuman = false;
        }
        else if(temp % 3 == 1){
            p1IsHuman = true;
            p2IsHuman = false;
        }
        else{
            p1IsHuman = true;
            p2IsHuman = true;
        }
        board = new int[size + 1][size + 1];
        possibleMoves = new boolean[size + 1][size + 1];
        setFocusable(true);
        currentTurn = Farbe.schwarz;
        inApp = true;
        inGame = true;
        otherColor = Farbe.weiss;
        waitingPlayerInput = false;
        board[9][9] = Farbe.weiss.ordinal();
        board[10][10] = Farbe.weiss.ordinal();
        board[9][10] = Farbe.schwarz.ordinal();
        board[10][9] = Farbe.schwarz.ordinal();
        possibleMoves = generateNewPossibilities(board);
        thread.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.cyan);
        g.fillRect(blockSize,blockSize,size * blockSize, size * blockSize);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                g.setColor(Color.BLACK);
                g.drawRect(blockSize + j * blockSize, blockSize + i * blockSize, blockSize, blockSize);
            }
        }
        //int count = 0;
        int countb = 0;
        int countw = 0;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                if(board[i][j] == Farbe.schwarz.ordinal()){
                    g.setColor(Color.BLACK);
                    g.fillOval((int)(0.75 * blockSize) + j * blockSize,(int)(0.75 * blockSize) + i * blockSize, (int) (0.5 * blockSize), (int) (0.5 * blockSize));
                    countb++;
                }
                else if(board[i][j] == Farbe.weiss.ordinal()) {
                    g.setColor(Color.WHITE);
                    g.fillOval((int) (0.75 * blockSize) + j * blockSize,(int)(0.75 * blockSize) + i * blockSize, (int) (0.5 * blockSize), (int) (0.5 * blockSize));
                    countw++;
                }
                /*if(possibleMoves[i][j] && !scoreg.isEmpty()){
                    g.setColor(Color.magenta);
                    g.drawString("" + scoreg.get(count), j * blockSize + blockSize, i * blockSize + blockSize);
                    count++;
                }*/
                }
            }
        g.setColor(Color.magenta);
        g.drawString("Score white: "+ countw, 100, 20);
        g.drawString("Score black: " + countb, 600, 20);
        //scoreg.clear();
        if(!inApp){
            g.setColor(Color.magenta);
            g.drawString(s + " Player won!", 350, 20);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if(p1IsHuman && currentTurn == Farbe.schwarz){
            int x = e.getX();
            int y = e.getY();
            x = getX(x);
            y = getY(y);
            if(possibleMoves[(y - blockSize) / blockSize][(x - blockSize) / blockSize] && board[(y - blockSize) / blockSize][(x - blockSize) / blockSize] < 1){
                placeStoneOnScreenPosition(x, y, currentTurn);
                waitingPlayerInput = false;
            }
        }
        if(p2IsHuman && currentTurn == Farbe.weiss){
            int x = e.getX();
            int y = e.getY();
            x = getX(x);
            y = getY(y);
            if(possibleMoves[(y - blockSize) / blockSize][(x - blockSize) / blockSize] && board[(y - blockSize) / blockSize][(x - blockSize) / blockSize] < 1){
                placeStoneOnScreenPosition(x, y, currentTurn);
                waitingPlayerInput = false;
            }
        }
    }
    public int getX(int x){
        if(x < blockSize){
            x = blockSize;
        }
        else if(x > 760){
            x = 760;
        }
        else if((x - blockSize) % blockSize < 20){
            x -= x % blockSize;
        }
        else{
            int k = (x - blockSize) / blockSize;
            x = (k + 1) * blockSize + blockSize;
        }
        return x;

    }
    public int getY(int y){
        if(y < blockSize){
            y = blockSize;
        }
        else if(y > 760){
            y = 760;
        }
        else if((y - blockSize) % blockSize < 20){
            y -= y % blockSize;
        }
        else{
            int k = (y - blockSize) / blockSize;
            y = (k + 1) * blockSize + blockSize;
        }
        return y;
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public boolean[][] generateNewPossibilities(int[][] board){
        boolean[][] possibilities = new boolean[board.length][board.length];
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == Farbe.transparent.ordinal()) {
                    if(i - 1 > 0 && j + 1  < board.length && board[i - 1][j + 1] > 0) {
                        possibilities[i][j] = true;
                    }
                    if(i - 1 > 0 && j - 1 > 0 && board[i - 1][j - 1] > 0) {
                        possibilities[i][j] = true;
                    }
                    if(i + 1 < board.length && j - 1 > 0 && board[i + 1][j - 1] > 0) {
                        possibilities[i][j] = true;
                    }
                    if(i + 1 < board.length  && j + 1 < board.length && board[i + 1][j + 1] > 0) {
                        possibilities[i][j] = true;
                    }
                    if(i - 1 > 0 && board[i - 1][j] > 0) {
                        possibilities[i][j] = true;
                    }
                    if(i + 1 < board.length && board[i + 1][j] > 0) {
                        possibilities[i][j] = true;
                    }
                    if(j - 1 > 0 && board[i][j - 1] > 0) {
                        possibilities[i][j] = true;
                    }
                    if(j + 1 < board.length && board[i][j + 1] > 0) {
                        possibilities[i][j] = true;
                    }
                }
            }
        }
        return possibilities;
    }
    public ArrayList<Integer[]> getAllFlips(int[][] board, int j, int i, int color){
        ArrayList<Integer[]> sol = new ArrayList<>();
        sol.addAll(getFlips(board, j, i + 1, 0, 1, color));
        sol.addAll(getFlips(board,j, i - 1, 0, -1, color));
        sol.addAll(getFlips(board, j + 1, i,1,0,color));
        sol.addAll(getFlips(board,j - 1, i, -1, 0 , color));
        sol.addAll(getFlips(board,j + 1, i + 1, 1, 1, color));
        sol.addAll(getFlips(board,j + 1, i - 1, 1, -1, color));
        sol.addAll(getFlips(board,j - 1, i + 1, -1, 1, color));
        sol.addAll(getFlips(board,j - 1, i - 1, -1, -1, color));
        return sol;
    }
    public int[][] applyFlips(int[][] board, ArrayList<Integer[]> sol, int color){
        for (Integer[] integers : sol) {
            int x = integers[0];
            int y = integers[1];
            int k = board[y][x];
            if (k != color) {
                board[y][x] = color;
            }
        }
        return board;
    }
    public int[] findRandomBestPlacement(int[][] board, int depth) {
        int maxScore = Integer.MIN_VALUE;
        if(depth % 2 == 1){
            maxScore = Integer.MAX_VALUE;
        }
        int maxX = -1;
        int maxY = -1;
        boolean[][] possibilities = generateNewPossibilities(board);
        if (possibilities[0][0]) {
            return new int[]{0, 0, 0};
        } else if (possibilities[0][board.length - 1]) {
            return new int[]{board.length - 1, 0, 0};
        } else if (possibilities[board.length - 1][0]) {
            return new int[]{0, board.length - 1, 0};
        } else if (possibilities[board.length - 1][board.length - 1]) {
            return new int[]{board.length - 1, board.length - 1, 0};
        }
        int color;
        if (depth % 2 == 0) {
            color = currentTurn.ordinal();
        } else {
            color = otherColor.ordinal();
        }
        ArrayList<Integer[]> currPossibilities = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (possibilities[i][j]) {
                    currPossibilities.add(new Integer[]{j, i});
                }
            }
        }
        while (currPossibilities.size() > 30) {
            int random = (int) (Math.random() * currPossibilities.size());
            currPossibilities.remove(random);
        }
        for (Integer[] currPossibility : currPossibilities) {
            int[][] boardCopy = new int[board.length][board.length];
            for (int k = 0; k < board.length; k++) {
                System.arraycopy(board[k], 0, boardCopy[k], 0, board.length);
            }
            boardCopy[currPossibility[1]][currPossibility[0]] = color;
            ArrayList<Integer[]> sol = getAllFlips(boardCopy, currPossibility[0], currPossibility[1], color);
            int score = sol.size();
            for (Integer[] integers : sol) {
                if (boardCopy[integers[1]][integers[0]] == color) {
                    score--;
                }
            }
            int j = currPossibility[0];
            int y = currPossibility[1];
            if ((boardCopy[0][0] < 1 && ((j == 1 && y == 0) || (j == 0 && y == 1) || (j == 1 && y == 1))) || (boardCopy[board.length - 1][0] < 1 && ((j == 0 && y == board.length - 2) || (j == 1 && y == board.length - 1) || (j == 1 && y == board.length - 2))) || (boardCopy[0][board.length - 1] < 1 && ((j == board.length - 2 && y == 0) || (j == board.length - 1 && y == 1) || (j == board.length - 2 && y == 1))) || (boardCopy[board.length - 1][board.length - 1] < 1 && ((j == board.length - 2 && y == board.length - 1) || (j == board.length - 1 && y == board.length - 2) || (j == board.length - 2 && y == board.length - 2)))) {
                score -= 10;
            }
            boardCopy = applyFlips(boardCopy, sol, color);
            if (depth % 2 == 1) {
                score *= -1;
            }
            if (depth > 0) {
                score += findRandomBestPlacement(boardCopy, depth - 1)[2];
            }
            // k += j + " " +  i + " " + score;
            if (maxScore < score && depth % 2 == 0) {
                maxScore = score;
                maxX = currPossibility[0];
                maxY = currPossibility[1];
            }
            if(depth % 2 == 1){
                if(score < maxScore){
                    maxScore = score;
                    maxX = currPossibility[0];
                    maxY = currPossibility[1];
                }
            }
        }
        return new int[]{maxX, maxY, maxScore};
    }
    public ArrayList<Integer[]> findingBestTopTenOnlyHelper(int[][] board, int color){
        int score = 0;
        ArrayList<Integer[]>topTen = new ArrayList<>();
        boolean[][] possibilities = generateNewPossibilities(board);
        if(possibilities[0][0]){
            Integer[] curr = new Integer[]{0, 0, 15};
            topTen.add(curr);
            return topTen;
        }
        else if(possibilities[0][board.length - 1]){
            Integer[] curr = new Integer[]{board.length - 1, 0, 15};
            topTen.add(curr);
            return topTen;
        }
        else if(possibilities[board.length - 1][0]){
            Integer[] curr = new Integer[]{0, board.length - 1, 15};
            topTen.add(curr);
            return topTen;
        }
        else if(possibilities[board.length - 1][board.length - 1]){
            Integer[] curr = new Integer[]{board.length - 1, board.length - 1, 8};
            topTen.add(curr);
            return topTen;
        }
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                if(possibilities[i][j]){
                    score = getAllFlips(board, j, i, color).size();
                    topTen.add(new Integer[]{j, i, score});
                }
            }
        }
        Collections.sort(topTen, new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                return o1[2].compareTo(o2[2]);
            }
        });

        while(topTen.size() > 10){
            topTen.remove(0);
        }
        return topTen;
    }
    public int[] findingBestTopTenOnly(int[][]board, int depth){
        ArrayList<Integer[]> iterate = new ArrayList<>();
        int color;
        int maxX = 0;
        int maxY = 0;
        int maxScore;
        if(depth % 2 == 0){
            maxScore = Integer.MIN_VALUE;
        }
        else{
            maxScore = Integer.MAX_VALUE;
        }
        if(depth % 2 == 0){
            color = currentTurn.ordinal();
        }
        else{
            color = otherColor.ordinal();
        }
        iterate = findingBestTopTenOnlyHelper(board, color);
        for(int i = 0; i < iterate.size(); i++){
            int x = iterate.get(i)[0];
            int y = iterate.get(i)[1];
            int[][] boardCopy = new int[board.length][board.length];
            for(int k = 0; k < board.length; k++){
                System.arraycopy(board[k], 0, boardCopy[k], 0, board.length);
            }
            boardCopy[y][x] = color;
            ArrayList<Integer[]> sol = new ArrayList<>();
            sol = getAllFlips(boardCopy, x, y, color);
            int score = sol.size();
            for (Integer[] integers : sol) {
                if (boardCopy[integers[1]][integers[0]] == color) {
                    score--;
                }
            }
            boardCopy = applyFlips(boardCopy, sol, color);
            if(isGameOver(boardCopy)){
                score += 50;
            }
            if(y == 0 || x == 0 || x == board.length - 1 || y == board.length - 1){
                score += 3;
            }
            if(y == 1 || x == 1 || y == board.length - 2 || x == board.length - 2){
                score -= 3;
            }
            if ((boardCopy[0][0] < 1 && ((x == 1 && y == 0) || (x == 0 && y == 1) || (x == 1 && y == 1))) || (boardCopy[board.length - 1][0] < 1 && ((x == 0 && y == board.length - 2) || (x == 1 && y == board.length - 1) || (x == 1 && y == board.length - 2))) || (boardCopy[0][board.length - 1] < 1 && ((x == board.length - 2 && y == 0) || (x == board.length - 1 && y == 1) || (x == board.length - 2 && y == 1))) || (boardCopy[board.length - 1][board.length - 1] < 1 && ((x == board.length - 2 && y == board.length - 1) || (x == board.length - 1 && y == board.length - 2) || (x == board.length - 2 && y == board.length - 2)))) {
                score -= 15;
            }
            if(depth % 2 == 1){
                score *= -1;
            }
            score = score * ((depth + 1) / 2 + 1);
            if(depth > 0){
                score += findingBestTopTenOnly(boardCopy, depth - 1)[2];
            }
            if(maxScore < score && depth % 2 == 0){
                maxScore = score;
                maxX = x;
                maxY = y;
            }
            if(depth % 2 == 1){
                if(score < maxScore){
                    maxScore = score;
                    maxX = x;
                    maxY = y;
                }
            }
        }
        return new int[]{maxX, maxY, maxScore};
    }
    public int[] findRandomPlacement(int[][] board){
        ArrayList<int[]> sol = new ArrayList<>();
        boolean[][] possibilities = generateNewPossibilities(board);
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                if(possibilities[i][j]){
                    sol.add(new int[]{j, i});
                }
            }
        }
        int random = (int) (Math.random() * sol.size());
        return sol.get(random);
    }
    public int[] findBestPlacement(int[][] board, int depth){
        int maxScore = Integer.MIN_VALUE;
        if((depth % 2) == 1){
            maxScore = Integer.MAX_VALUE;
        }
        int maxX = -1;
        int maxY = -1;
        boolean[][] possibilities = generateNewPossibilities(board);
        if(possibilities[0][0]){
            return new int[]{0, 0, 15};
        }
        else if(possibilities[0][board.length - 1]){
            return new int[]{board.length - 1, 0, 15};
        }
        else if(possibilities[board.length - 1][0]){
            return new int[]{0, board.length - 1, 15};
        }
        else if(possibilities[board.length - 1][board.length - 1]){
            return new int[]{board.length - 1, board.length - 1, 15};
        }
        int color;
        if(depth % 2 == 0){
            color = currentTurn.ordinal();
        }
        else{
            color = otherColor.ordinal();
        }
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                if(possibilities[i][j]){
                    int[][] boardCopy = new int[board.length][board.length];
                    for(int k = 0; k < board.length; k++){
                        System.arraycopy(board[k], 0, boardCopy[k], 0, board.length);
                    }
                    boardCopy[i][j] = color;
                    ArrayList<Integer[]> sol = getAllFlips(boardCopy, j, i, color);
                    int score = sol.size();
                    if ((boardCopy[0][0] < 1 && ((j == 1 && i == 0) || (j == 0 && i == 1) || (j == 1 && i == 1))) || (boardCopy[board.length - 1][0] < 1 && ((j == 0 && i == board.length - 2) || (j == 1 && i == board.length - 1) || (j == 1 && i == board.length - 2))) || (boardCopy[0][board.length - 1] < 1 && ((j == board.length - 2 && i == 0) || (j == board.length - 1 && i == 1) || (j == board.length - 2 && i == 1))) || (boardCopy[board.length - 1][board.length - 1] < 1 && ((j == board.length - 2 && i == board.length - 1) || (j == board.length - 1 && i == board.length - 2) || (j == board.length - 2 && i == board.length - 2)))) {
                        score -= 15;
                    }
                    for (Integer[] integers : sol) {
                        if (boardCopy[integers[1]][integers[0]] == color) {
                            score--;
                        }
                    }
                    boardCopy = applyFlips(boardCopy, sol, color);
                    if(isGameOver(boardCopy)){
                        score += 50;
                    }
                    if(i == 0 || j == 0 || j == board.length - 1 || i == board.length - 1){
                        score += 2;
                    }
                    if(i == 1 || j == 1 || i == board.length - 2 || j == board.length - 2){
                        score -= 3;
                    }
                    if(depth % 2 == 1){
                        score *= -1;
                    }
                    score = score * ((depth + 1) / 2 + 1);
                    if(depth > 0){
                        score += findBestPlacement(boardCopy, depth - 1)[2];
                    }

                    //if(depth == 2){
                        //scoreg.add(score);}
                   // k += j + " " +  i + " " + score;
                    if(maxScore < score && depth % 2 == 0){
                        maxScore = score;
                        maxX = j;
                        maxY = i;
                    }
                    else if(maxScore == score){
                        int random = (int)(Math.random() * 2);
                        if(random == 0){
                            maxScore = score;
                            maxX = j;
                            maxY = i;
                        }
                    }
                    if(depth % 2 == 1){
                        if(score < maxScore){
                            maxScore = score;
                            maxX = j;
                            maxY = i;
                        }
                    }
                }
            }
        }
        return new int[]{maxX, maxY, maxScore};
    }
    public String findWinner(int[][] board){
        int countB = 0;
        int countW = 0;
        for (int[] ints : board) {
            for (int j = 0; j < board.length; j++) {
                if (ints[j] == Farbe.schwarz.ordinal()) {
                    countB++;
                } else if (ints[j] == Farbe.weiss.ordinal()) {
                    countW++;
                }
            }
        }
        if(countW > countB){
            return "White";
        }
        else {
            return "Black";
        }
    }
    public boolean isGameOver(int[][]board){
        boolean black = false;
        boolean white = false;
        boolean empty = false;
        for (int[] ints : board) {
            for (int j = 0; j < board.length; j++) {
                if (ints[j] == Farbe.schwarz.ordinal()) {
                    black = true;
                } else if (ints[j] == Farbe.weiss.ordinal()) {
                    white = true;
                } else {
                    empty = true;
                }
            }
        }
        return !black || !white || !empty;
    }
    public ArrayList<Integer[]> getFlips(int[][] board, int x, int y, int dirX, int dirY, int color){
        ArrayList<Integer[]> sol = new ArrayList<>();
        if(y < 0 || y > board.length - 1 || x < 0 || x > board.length - 1 || board[y][x] == Farbe.transparent.ordinal()){
            return sol;
        }
        else if(board[y][x] == color){
            sol.add(new Integer[]{x,y});
            return sol;
        }
        else{
            sol.addAll(getFlips(board, x + dirX, y + dirY, dirX, dirY, color));
            if(sol.size() != 0){
                sol.add(new Integer[]{x, y});}
            return sol;
        }
    }
    public void placeStoneOnScreenPosition(int x, int y, Farbe color){
        board[(y - blockSize) / blockSize][(x - blockSize) / blockSize] = color.ordinal();
        ArrayList<Integer[]>curr = getAllFlips(board, (x - blockSize) / blockSize, (y - blockSize) / blockSize, color.ordinal());
        board = applyFlips(board, curr, color.ordinal());
        switchPlayer();
        repaint();
    }
    public void placeStoneOnGridPosition(int x, int y, Farbe color){
        board[y][x] = color.ordinal();
        ArrayList<Integer[]>curr = getAllFlips(board, x, y, color.ordinal());
        board = applyFlips(board, curr, color.ordinal());
        switchPlayer();
        repaint();
    }
    public void switchPlayer(){
        if(currentTurn == Farbe.schwarz){
            currentTurn = Farbe.weiss;
            otherColor = Farbe.schwarz;
        }
        else{
            currentTurn = Farbe.schwarz;
            otherColor = Farbe.weiss;
        }
        possibleMoves = generateNewPossibilities(board);
    }
    private Thread thread = new Thread(new Runnable() {
        public void run() {
            try{
            while(inGame){
                if(isGameOver(board)){
                    s = findWinner(board);
                    inApp = false;
                    repaint();
                }
                if(inApp){
                    if(currentTurn == Farbe.schwarz) {
                        if (p1IsHuman) {
                            waitingPlayerInput = true;
                            while (waitingPlayerInput) {
                                thread.sleep(20);
                            }
                        }
                        else {
                            int[] currPos;
                            currPos = findBestPlacement(board, 3 - 1);

                            placeStoneOnGridPosition(currPos[0], currPos[1], Farbe.schwarz);
                        }
                    }
                    else {
                        if (!p2IsHuman) {
                            int[] currPos;
                            currPos = findingBestTopTenOnly(board, 5 - 1);

                            placeStoneOnGridPosition(currPos[0], currPos[1], Farbe.weiss);
                        }
                        else {
                            waitingPlayerInput = true;
                            while (waitingPlayerInput) {
                                thread.sleep(20);
                            }
                        }
                    }
                    thread.sleep(5);
                    }
                thread.sleep(5);
                }
            }
            catch(InterruptedException e){
            }
        }
    });
}
