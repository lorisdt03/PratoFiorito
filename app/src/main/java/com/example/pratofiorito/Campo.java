package com.example.pratofiorito;

import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Campo {

    private final ImageButton[][] b = new ImageButton[DIM][DIM];
    private final GameActivity ga;
    private final TableLayout layout;
    private final int[][] matBombe= new int[DIM][DIM];
    private final int difficulty;
    private boolean gameEnded;
    private final MyImages mi;
    private int placedFlags;
    public static final int DIM = 10;
    public static final int EASY = 10;
    public static final int NORMAL = 15;
    public static final int HARD = 20;

    //creo i il campo di bottoni, genero la matrice contenente le bombe e stampo a schermo le bandierine piazzabili
    Campo(TableLayout layout, GameActivity ga, int diff){
        this.layout = layout;
        this.ga =ga;
        mi = new MyImages(ga);
        difficulty=diff;
        gameEnded = false;
        placedFlags = 0;
        numberFlags();
        createButtons();
        generaBombe();
    }
    //genera una matrice (matBombe) di posizione DIM*DIM
    //con una quantità di bombe difficulty in posizioni casuali
    private void generaBombe() {
        int[] I = new int[difficulty];
        int[] J = new int[difficulty];
        for (int i = 0; i < difficulty; i++) {
            I[i] = (int) (Math.random() * DIM);
            J[i] = (int) (Math.random() * DIM);
            if (i != 0) {
                if (doubles(i - 1, I, J, I[i], J[i])) {
                    i--;
                }
            }
        }
        for (int i = 0; i < difficulty; i++) {
            matBombe[I[i]][J[i]]=-1;
        }
    }
    //restituisce se alle cordinate [x,y] c'è una bomba
    public boolean isBomb(int x, int y){
        return matBombe[x][y]==-1;
    }
    //dalla matrice di bombe matBombe viene generato il campo
    //ovvero di fianco alle bombe vengono inseriti i numeri
    public void bombsToField(){
        for(int i=0;i<DIM;i++){
            for(int j=0;j<DIM;j++){
                if(matBombe[i][j]!=-1){
                    matBombe[i][j] = countNearBombs(i,j);
                }
            }
        }
    }
    //conto il numero di bombe intorno a una determinata cella
    private int countNearBombs(int x, int y) {
        int cont = 0;
        for(int i = getInBoundMin(x);i<=getInBoundMax(x);i++){
            for(int j = getInBoundMin(y);j<=getInBoundMax(y);j++){
                if(matBombe[x+i][y+j]==-1){
                    cont++;
                }
            }
        }
        return cont;
    }
    //se il valore di n è sul bordo restituisco 0 così da non causare un out of bounds
    private int getInBoundMax(int n) {
        if(n+1==DIM)
            return 0;
        return 1;
    }
    //se il valore di n è sul bordo restituisco 0 così da non causare un out of bounds
    private int getInBoundMin(int n) {
        if(n-1<0)
            return 0;
        return -1;
    }
    //creo una matrice di bottoni e la mostro su schermo all'interno di un table layout
    private void createButtons() {
        int k=0;
        MyImages mi = new MyImages(ga);
        int size = mi.getButtonSize(ga);
        ButtonListener bl = new ButtonListener(this);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        TableRow row;
        for(int i = 0; i< 10;i ++){
            row = new TableRow(ga);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
            for(int j = 0; j<10; j++){
                b[i][j] = new ImageButton(ga);
                b[i][j].setId(k++);
                b[i][j].setImageDrawable(mi.getButton());
                row.addView(b[i][j],size,size);
                b[i][j].setOnClickListener(bl);
            }
            layout.addView(row);
            row.setGravity(Gravity.CENTER);
        }

    }
    //restituisco la matrice di bottoni
    public ImageButton[][] getB() {
        return b;
    }
    //restituisco il context attuale (GameActivity)
    public GameActivity getContext() {
        return ga;
    }
    //guardo se esistono coppie di valori uguali all'interno di v1 e v2 e in caso le rimuovo
    boolean doubles(int dim, int[] v1, int[] v2, int e1, int e2) {
        if (dim == 0) {
            return (e1 == v1[dim] && e2 == v2[dim]);
        } else {
            return (doubles(dim - 1, v1, v2, e1, e2) || (e1 == v1[dim] && e2 == v2[dim]));
        }
    }
    //restituisco la matrice contenente tutti i dati del campo
    public int[][] getMatBombe() {
        return matBombe;
    }
    //restituisco la difficoltà
    public int getDifficulty(){
        return difficulty;
    }
    //sposto la bomba in posizione [x,y] in una nuova posizione
    public void moveBomb(int x, int y) {
        matBombe[x][y]=0;
        int i, j;
        do{
            i = (int) (Math.random() * DIM);
            j= (int) (Math.random() * DIM);
        }while((i==x && j==y)||matBombe[i][j]==-1);
        matBombe[i][j]=-1;
    }
    //restituisco se il gioco sia finito o meno
    public boolean isGameEnded() {
        return gameEnded;
    }
    //imposto che il gioco sia finito
    public void endGame() {
        gameEnded = true;
    }
    //sostituisco a tutti i bottoni che non sono ancora stati premuti i rispettivi valori numerici
    //e se le bandierine erano sbagliate le sostituisco con delle bandierine crociate
    public void showField() {
        for(int i=0;i<Campo.DIM;i++){
            for(int j=0;j<Campo.DIM;j++){
                if(mi.isButton(b[i][j])){
                    replaceElement(i,j);
                }
                else if(mi.isFlag(b[i][j]) && matBombe[i][j]!=-1){
                    replaceWrongFlag(i,j);
                }
            }
        }
        Button reset = ga.findViewById(R.id.reset);
        reset.setText(R.string.sconfitta);
    }
    //ottengo la posizione dall'id e richiamo il metodo per rimpiazziare dalle coordinate
    public void replaceElement(int id){
        int [] pos = getButtonCoordinates(id);
        replaceElement(pos[0],pos[1]);
    }
    //rimpiazzo un bottone in coordinate [i,j] con l'elemento corrispondente all'interno della matrice matBombe (Bomba o numero)
    public void replaceElement(int i, int j) {
        if(matBombe[i][j]==-1){
            b[i][j].setImageDrawable(mi.getBomb());
        }else{
            b[i][j].setImageDrawable(mi.getNumber(matBombe[i][j]));
        }
    }
    //ottengo le coordinate di un bottone dal suo id
    public int[] getButtonCoordinates(int B_id){
        int[] coordinates = new int[2];
        for(int i = 0;i<10;i++){
            for(int j = 0;j<10;j++){
                if(B_id== b[i][j].getId()){
                    coordinates[0]=i;
                    coordinates[1]=j;
                    return coordinates;
                }
            }
        }
        return coordinates;
    }
    //restituisco un bottone dal suo id
    public ImageButton getButton(int B_id){
        int [] pos = getButtonCoordinates(B_id);
        return b[pos[0]][pos[1]];
    }
    //ottengo la posizione dall'id e richiamo il metodo per rimpiazzare gli elementi vuoti vicini
    public void replaceNearEmpty(int id){
        int [] pos = getButtonCoordinates(id);
        activateNearEmpty(pos[0],pos[1],0);
    }
    //sostituisco ricorsivamente gli elementi intorno a un bottone che corrisponde a un punto vuoto di matBombe
    public void activateNearEmpty(int i, int j,int status) {
            if((matBombe[i][j]==0 && mi.isButton(b[i][j])) || status ==0){
                replaceElement(i,j);
                if(i+1<DIM){
                    activateNearEmpty(i+1,j,1);
                    replaceElement(i+1,j);
                    if(j+1<DIM){
                        activateNearEmpty(i+1,j+1,1);
                        replaceElement(i+1,j+1);
                    }
                    if(j-1>0){
                        activateNearEmpty(i+1,j-1,1);
                        replaceElement(i+1,j-1);
                    }
                }
                if(i-1>0){
                    activateNearEmpty(i-1,j,1);
                    replaceElement(i-1,j);
                    if(j+1<DIM){
                        activateNearEmpty(i-1,j+1,1);
                        replaceElement(i-1,j+1);
                    }
                    if(j-1>0){
                        activateNearEmpty(i-1,j-1,1);
                        replaceElement(i-1,j-1);
                    }
                }
                if(j-1>0){
                    activateNearEmpty(i,j-1,1);
                    replaceElement(i,j-1);
                }
                if(j+1<DIM){
                    activateNearEmpty(i,j+1,1);
                    replaceElement(i,j+1);
                }
            }
        placedFlags = countFlags();
        numberFlags();
    }
    //posiziono una flag sul bottone con id B_id
    public void placeFlag(int B_id) {
        ImageButton b = getButton(B_id);
        if(mi.isFlag(b)){
            placedFlags--;
            b.setImageDrawable(mi.getButton());
        }
        else if(mi.isButton(b) && placedFlags< difficulty){
            placedFlags++;
            b.setImageDrawable(mi.getFlag());
        }
        numberFlags();
    }
    //conto il numero di bandierine che sono piazzate sul campo
    private int countFlags() {
        int cont=0;
        for(int i=0;i<Campo.DIM;i++){
            for(int j=0;j<Campo.DIM;j++){
                if(mi.isFlag(b[i][j])){
                    cont++;
                }
            }
        }
        return cont;
    }
    //rimpiazzo la bandierina in posizione [i,j] con una bandierina crociata
    private void replaceWrongFlag(int i, int j) {
        b[i][j].setImageDrawable(mi.getWrongFlag());
    }
    //rendo non cliccabile la matrice il campo
    public void setNotClickable() {
        for(int i= 0;i<Campo.DIM;i++){
            for(int j= 0;j<Campo.DIM;j++){
                b[i][j].setClickable(false);
            }
        }
    }
    //aggiorno il numero presente a schermo in base al numero di bandierina ancora piazzabili
    private void numberFlags() {
        TextView t = ga.findViewById(R.id.flags_number);
        String s = Integer.toString(difficulty-placedFlags);
        t.setText(s);
    }

}
