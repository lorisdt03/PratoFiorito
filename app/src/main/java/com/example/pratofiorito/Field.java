package com.example.pratofiorito;


import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Field {

    private final ImageButton[][] b = new ImageButton[DIM][DIM];
    private final GameActivity ga;
    private final TableLayout layout;
    private final int[][] matBombe = new int[DIM][DIM];
    private final int difficulty;
    private boolean gameEnded;
    private final MyImages mi;
    private int placedFlags;
    private final int[][] matPressed;
    public static final int DIM = 10;
    public static final int EASY = 10;
    public static final int NORMAL = 15;
    public static final int HARD = 20;

    //creo i il campo di bottoni, genero la matrice contenente le bombe e stampo a schermo le bandierine piazzabili
    Field(TableLayout layout, GameActivity ga, int diff) {
        this.layout = layout;
        this.ga = ga;
        mi = new MyImages(ga);
        matPressed = new int[DIM][DIM];
        difficulty = diff;
        gameEnded = false;
        placedFlags = 0;
        numberFlags();
        createButtons();
        generateBombs();
        fillMatPressed();
    }

    //riempio la matrice marPressed di 0
    private void fillMatPressed() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                matPressed[i][j] = 0;
            }
        }
    }

    //genera una matrice (matBombe) di posizione DIM*DIM
    //con una quantità di bombe difficulty in posizioni casuali
    private void generateBombs() {
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
            matBombe[I[i]][J[i]] = -1;
        }
    }

    //restituisce se alle cordinate [x,y] c'è una bomba
    public boolean isBomb(int x, int y) {
        return matBombe[x][y] == -1;
    }

    //restituisco se l'oggetto in posizione [x,y] sia stato premuto
    public boolean isNotPressed(int x, int y) {
        return matPressed[x][y] != 1;
    }

    //restituisco se l'oggetto in posizione [x,y] sia una bandierina
    public boolean isFlag(int x, int y) {
        return matPressed[x][y] == -1;
    }

    //dalla matrice di bombe matBombe viene generato il campo
    //ovvero di fianco alle bombe vengono inseriti i numeri
    public void bombsToField() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (matBombe[i][j] != -1) {
                    matBombe[i][j] = countNearBombs(i, j);
                }
            }
        }
    }

    //conto il numero di bombe intorno a una determinata cella
    private int countNearBombs(int x, int y) {
        int cont = 0;
        for (int i = getInBoundMin(x); i <= getInBoundMax(x); i++) {
            for (int j = getInBoundMin(y); j <= getInBoundMax(y); j++) {
                if (matBombe[x + i][y + j] == -1) {
                    cont++;
                }
            }
        }
        return cont;
    }

    //se il valore di n è sul bordo restituisco 0 così da non causare un out of bounds
    private int getInBoundMax(int n) {
        return (n + 1 == DIM) ? 0 : 1;
    }

    //se il valore di n è sul bordo restituisco 0 così da non causare un out of bounds
    private int getInBoundMin(int n) {
        return (n - 1 < 0) ? 0 : -1;
    }

    //creo una matrice di bottoni e la mostro su schermo all'interno di un table layout
    private void createButtons() {
        int k = 0;
        MyImages mi = new MyImages(ga);
        int size = mi.getButtonSize();
        ButtonListener bl = new ButtonListener(this);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        TableRow row;
        for (int i = 0; i < DIM; i++) {
            row = new TableRow(ga);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            for (int j = 0; j < DIM; j++) {
                b[i][j] = new ImageButton(ga);
                b[i][j].setId(k++);
                b[i][j].setSoundEffectsEnabled(false);
                b[i][j].setImageDrawable(mi.getButton());
                row.addView(b[i][j], size, size);
                b[i][j].setOnClickListener(bl);
            }
            layout.addView(row);
            row.setGravity(Gravity.CENTER);
        }

    }

    //restituisco il context attuale (GameActivity)
    public GameActivity getContext() {
        return ga;
    }

    //guardo se esistono coppie di valori uguali all'interno di v1 e v2 e in caso le rimuovo
    private boolean doubles(int dim, int[] v1, int[] v2, int e1, int e2) {
        if (dim == 0) {
            return (e1 == v1[dim] && e2 == v2[dim]);
        } else {
            return (doubles(dim - 1, v1, v2, e1, e2) || (e1 == v1[dim] && e2 == v2[dim]));
        }
    }

    //restituisco la difficoltà
    public int getDifficulty() {
        return difficulty;
    }

    //sposto la bomba in posizione [x,y] in una nuova posizione
    public void moveBomb(int x, int y) {
        matBombe[x][y] = 0;
        int i, j;
        do {
            i = (int) (Math.random() * DIM);
            j = (int) (Math.random() * DIM);
        } while ((i == x && j == y) || matBombe[i][j] == -1);
        matBombe[i][j] = -1;
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
        for (int i = 0; i < Field.DIM; i++) {
            for (int j = 0; j < Field.DIM; j++) {
                if (isButton(i, j)) {
                    replaceElement(i, j);
                } else if (isFlag(i, j) && !isBomb(i, j)) {
                    replaceWrongFlag(i, j);
                }
            }
        }
        Button reset = ga.findViewById(R.id.reset);
        reset.setText(R.string.sconfitta);
    }

    //imposta l'oggetto alle coordinate [x,y] come premuto
    private void setPressed(int x, int y) {
        matPressed[x][y] = 1;
    }

    //ottengo la posizione dall'id e richiamo il metodo per rimpiazziare dalle coordinate
    public void replaceElement(int B_id) {
        int[] pos = getButtonCoordinates(B_id);
        if (isEmpty(pos[0], pos[1])) {
            replaceNearEmpty(B_id);
        } else {
            setPressed(pos[0], pos[1]);
            replaceElement(pos[0], pos[1]);
        }
    }

    //rimpiazzo un bottone in coordinate [i,j] con l'elemento corrispondente all'interno della matrice matBombe (Bomba o numero)
    private void replaceElement(int x, int y) {
        setPressed(x, y);
        if (matBombe[x][y] == -1) {
            b[x][y].setImageDrawable(mi.getBomb());
        } else {
            b[x][y].setImageDrawable(mi.getNumber(matBombe[x][y]));
        }
    }

    //ottengo le coordinate di un bottone dal suo id
    public int[] getButtonCoordinates(int B_id) {
        int[] coordinates = new int[2];
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (B_id == b[i][j].getId()) {
                    coordinates[0] = i;
                    coordinates[1] = j;
                    return coordinates;
                }
            }
        }
        return coordinates;
    }

    //restituisco un bottone dal suo id
    private ImageButton getButton(int B_id) {
        int[] pos = getButtonCoordinates(B_id);
        return b[pos[0]][pos[1]];
    }

    //ottengo la posizione dall'id e richiamo il metodo per rimpiazzare gli elementi vuoti vicini
    private void replaceNearEmpty(int B_id) {
        int[] pos = getButtonCoordinates(B_id);
        activateNearEmpty(pos[0], pos[1]);
    }

    //sostituisco ricorsivamente gli elementi intorno a un bottone che corrisponde a un punto vuoto di matBombe
    private void activateNearEmpty(int x, int y) {
        if (isNotPressed(x, y) && isEmpty(x, y)) {
            replaceElement(x, y);
            if (x + 1 < DIM) {
                activateNearEmpty(x + 1, y);
                replaceElement(x + 1, y);
                if (y + 1 < DIM) {
                    activateNearEmpty(x + 1, y + 1);
                    replaceElement(x + 1, y + 1);
                }
                if (y - 1 >= 0) {
                    activateNearEmpty(x + 1, y - 1);
                    replaceElement(x + 1, y - 1);
                }
            }
            if (x - 1 >= 0) {
                activateNearEmpty(x - 1, y);
                replaceElement(x - 1, y);
                if (y + 1 < DIM) {
                    activateNearEmpty(x - 1, y + 1);
                    replaceElement(x - 1, y + 1);
                }
                if (y - 1 >= 0) {
                    activateNearEmpty(x - 1, y - 1);
                    replaceElement(x - 1, y - 1);
                }
            }
            if (y - 1 >= 0) {
                activateNearEmpty(x, y - 1);
                replaceElement(x, y - 1);
            }
            if (y + 1 < DIM) {
                activateNearEmpty(x, y + 1);
                replaceElement(x, y + 1);
            }
        }
        placedFlags = countFlags();
        numberFlags();
    }

    //posiziono una flag sul bottone con id B_id
    public void placeFlag(int B_id) {
        ImageButton b = getButton(B_id);
        int[] pos = getButtonCoordinates(B_id);
        if (isFlag(pos[0], pos[1])) {
            placedFlags--;
            setNotPressed(pos[0], pos[1]);
            b.setImageDrawable(mi.getButton());
        } else if (isButton(pos[0], pos[1]) && placedFlags < difficulty) {
            placedFlags++;
            setFlag(pos[0], pos[1]);

            b.setImageDrawable(mi.getFlag());
        }
        numberFlags();
    }

    //imposta il bottone in posizione [x,y] come non premuto
    private void setNotPressed(int x, int y) {
        matPressed[x][y] = 0;
    }

    //imposta il bottone in posizione [x,y] come bandierina
    private void setFlag(int x, int y) {
        matPressed[x][y] = -1;
    }

    //conto il numero di bandierine che sono piazzate sul campo
    private int countFlags() {
        int cont = 0;
        for (int i = 0; i < Field.DIM; i++) {
            for (int j = 0; j < Field.DIM; j++) {
                if (isFlag(i, j)) {
                    cont++;
                }
            }
        }
        return cont;
    }

    //rimpiazzo la bandierina in posizione [i,j] con una bandierina crociata
    private void replaceWrongFlag(int x, int y) {
        b[x][y].setImageDrawable(mi.getWrongFlag());
    }

    //aggiorno il numero presente a schermo in base al numero di bandierina ancora piazzabili
    private void numberFlags() {
        TextView t = ga.findViewById(R.id.flags_number);
        String s = Integer.toString(difficulty - placedFlags);
        t.setText(s);
    }

    //restituisco se un oggetto in posizione [x,y] è uno spazio vuoto
    private boolean isEmpty(int x, int y) {
        return matBombe[x][y] == 0;
    }

    //restituisco se un oggetto in posizione [x,y] è un bottone
    private boolean isButton(int x, int y) {
        return (isNotPressed(x, y) && !isFlag(x, y));
    }
}
