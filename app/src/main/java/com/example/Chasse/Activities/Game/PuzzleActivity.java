package com.example.Chasse.Activities.Game;

import android.util.Log;
import android.view.ViewTreeObserver;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.Chasse.R;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PuzzleActivity extends MiniGames {

    private volatile Drawable piece;
    private int nbPieces = 0;
    private volatile boolean lock = false;

    private TableLayout mat;
    private CountDownLatch latch = new CountDownLatch(1);
    private final boolean[][] yourPieces = new boolean[4][4];
    private volatile boolean isPiecesReceived = false;
    private volatile int choosenRowPiece;
    private volatile int choosenColumnPiece;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        Log.d("puzzle", "je suis dans l'activité puzzle !");
        List<View> pieceOfImages = Arrays.asList(
                findViewById(R.id.choix1), findViewById(R.id.choix2),
                findViewById(R.id.choix3), findViewById(R.id.choix4),
                findViewById(R.id.choix5), findViewById(R.id.choix6),
                findViewById(R.id.choix7), findViewById(R.id.choix8),
                findViewById(R.id.choix9), findViewById(R.id.choix10),
                findViewById(R.id.choix11), findViewById(R.id.choix12),
                findViewById(R.id.choix13), findViewById(R.id.choix14),
                findViewById(R.id.choix15), findViewById(R.id.choix16)
        );
        for (View pieceOfImage : pieceOfImages) {
            pieceOfImage.setVisibility(View.INVISIBLE);
        }


        this.piece = null;
        // Attend que le dernier éléement soit chargé
        findViewById(R.id.choix16).getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        findViewById(R.id.choix16).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        latch.countDown();
                    }
                }
        );

        Thread secondThread = new Thread(() -> {
            for (int i = 0; i < yourPieces.length; i++) {
                for (int j = 0; j < yourPieces.length; j++) {
                    yourPieces[i][j] = false;
                }
            }

            if (isTheMainUser){
                int countPieces = 0;
                int totalCells = yourPieces.length * yourPieces[0].length;
                while (countPieces < totalCells / 2 && countPieces < totalCells) {
                    int i = (int) (Math.random() * yourPieces.length);
                    int j = (int) (Math.random() * yourPieces[0].length);

                    if (!yourPieces[i][j]) {
                        yourPieces[i][j] = true;
                        countPieces++;
                    }
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ignored) {
                    //
                }
                socket.on("need puzzle pieces", new Emitter.Listener() {

                    @Override
                    public void call(Object... objects) {
                        socket.emit("puzzle pieces main user", booleanMatrixToJson(yourPieces));
                    }
                });

            } else {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {

                }
                socket.on("receive puzzle pieces", new Emitter.Listener() {
                    @Override
                    public void call(Object... objects) {
                        isPiecesReceived = true;
                        Log.d("puzzle", "je recois les pièces non prises de l'autre");
                        Log.d("puzzle", objects[0].toString());
                        boolean[][] matrix = jsonToBooleanMatrix(objects[0].toString());
                        // Permet de transformer les true en false et inversement
                        for (int i = 0; i < matrix.length; i++) {
                            for (int j = 0; j < matrix.length; j++) {
                                yourPieces[i][j] = !matrix[i][j];
                            }
                        }
                    }
                });

                while (!isPiecesReceived){
                    socket.emit("need receive puzzle pieces", booleanMatrixToJson(yourPieces));
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ignored) {

                    }
                }
            }

            this.mat = findViewById(R.id.mat);



            // Enlève toutes les images qui sont "false"
            for (int i = 0; i < yourPieces.length; i++) {
                for (int j = 0; j < yourPieces.length; j++) {
                    if (yourPieces[i][j]){
                        int finalI = i;
                        int finalJ = j;
                        runOnUiThread(() -> pieceOfImages.get(finalI + yourPieces.length * finalJ).setVisibility(View.VISIBLE));
                    }
                }
            }
        });

        secondThread.start();

        socket.on("put piece puzzle", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                int playerRowChoosen = (int) objects[0];
                int playerColumnChoosen = (int) objects[1];
                int pieceRow = (int) objects[2];
                int pieceColumn = (int) objects[3];
                Log.d("puzzle", "je reçois une pièce " + playerRowChoosen + " " + playerColumnChoosen + " " + pieceRow + " " + pieceColumn);
                List<Drawable> drawables = Arrays.asList(
                        getDrawable(R.drawable.puzzle1),
                        getDrawable(R.drawable.puzzle2),
                        getDrawable(R.drawable.puzzle3),
                        getDrawable(R.drawable.puzzle4),
                        getDrawable(R.drawable.puzzle5),
                        getDrawable(R.drawable.puzzle6),
                        getDrawable(R.drawable.puzzle7),
                        getDrawable(R.drawable.puzzle8),
                        getDrawable(R.drawable.puzzle9),
                        getDrawable(R.drawable.puzzle10),
                        getDrawable(R.drawable.puzzle11),
                        getDrawable(R.drawable.puzzle12),
                        getDrawable(R.drawable.puzzle13),
                        getDrawable(R.drawable.puzzle14),
                        getDrawable(R.drawable.puzzle15),
                        getDrawable(R.drawable.puzzle16)
                );
                piece = drawables.get(pieceColumn + pieceRow * yourPieces.length);
                runOnUiThread(() -> {
                    if (playerRowChoosen == 0 && playerColumnChoosen == 0) {
                        remplace(findViewById(R.id.piece1));
                    } else if (playerRowChoosen == 0 && playerColumnChoosen == 1) {
                        remplace(findViewById(R.id.piece2));
                    } else if (playerRowChoosen == 0 && playerColumnChoosen == 2) {
                        remplace(findViewById(R.id.piece3));
                    } else if (playerRowChoosen == 0 && playerColumnChoosen == 3) {
                        remplace(findViewById(R.id.piece4));
                    } else if (playerRowChoosen == 1 && playerColumnChoosen == 0) {
                        remplace(findViewById(R.id.piece5));
                    } else if (playerRowChoosen == 1 && playerColumnChoosen == 1) {
                        remplace(findViewById(R.id.piece6));
                    } else if (playerRowChoosen == 1 && playerColumnChoosen == 2) {
                        remplace(findViewById(R.id.piece7));
                    } else if (playerRowChoosen == 1 && playerColumnChoosen == 3) {
                        remplace(findViewById(R.id.piece8));
                    } else if (playerRowChoosen == 2 && playerColumnChoosen == 0) {
                        remplace(findViewById(R.id.piece9));
                    } else if (playerRowChoosen == 2 && playerColumnChoosen == 1) {
                        remplace(findViewById(R.id.piece10));
                    } else if (playerRowChoosen == 2 && playerColumnChoosen == 2) {
                        remplace(findViewById(R.id.piece11));
                    } else if (playerRowChoosen == 2 && playerColumnChoosen == 3) {
                        remplace(findViewById(R.id.piece12));
                    } else if (playerRowChoosen == 3 && playerColumnChoosen == 0) {
                        remplace(findViewById(R.id.piece13));
                    } else if (playerRowChoosen == 3 && playerColumnChoosen == 1) {
                        remplace(findViewById(R.id.piece14));
                    } else if (playerRowChoosen == 3 && playerColumnChoosen == 2) {
                        remplace(findViewById(R.id.piece15));
                    } else if (playerRowChoosen == 3 && playerColumnChoosen == 3) {
                        remplace(findViewById(R.id.piece16));
                    }
                });
            }
        });




    }


    public void choixPiece(View v) {
        if(this.piece==null) {
            if(v.getId()==R.id.choix1) {
                this.piece = getResources().getDrawable(R.drawable.puzzle3);
                findViewById(R.id.choix1).setVisibility(View.INVISIBLE);
                choosenRowPiece = 0;
                choosenColumnPiece = 2;
            } else if(v.getId()==R.id.choix2) {
                this.piece = getResources().getDrawable(R.drawable.puzzle1);
                findViewById(R.id.choix2).setVisibility(View.INVISIBLE);
                choosenRowPiece = 0;
                choosenColumnPiece = 0;
            } else if(v.getId()==R.id.choix3) {
                this.piece = getResources().getDrawable(R.drawable.puzzle5);
                findViewById(R.id.choix3).setVisibility(View.INVISIBLE);
                choosenRowPiece = 1;
                choosenColumnPiece = 0;
            } else if(v.getId()==R.id.choix4) {
                this.piece = getResources().getDrawable(R.drawable.puzzle11);
                findViewById(R.id.choix4).setVisibility(View.INVISIBLE);
                choosenRowPiece = 2;
                choosenColumnPiece = 2;
            } else if(v.getId()==R.id.choix5) {
                this.piece = getResources().getDrawable(R.drawable.puzzle8);
                findViewById(R.id.choix5).setVisibility(View.INVISIBLE);
                choosenRowPiece = 1;
                choosenColumnPiece = 3;
            } else if(v.getId()==R.id.choix6) {
                this.piece = getResources().getDrawable(R.drawable.puzzle7);
                findViewById(R.id.choix6).setVisibility(View.INVISIBLE);
                choosenRowPiece = 1;
                choosenColumnPiece = 2;
            } else if(v.getId()==R.id.choix7) {
                this.piece = getResources().getDrawable(R.drawable.puzzle9);
                findViewById(R.id.choix7).setVisibility(View.INVISIBLE);
                choosenRowPiece = 2;
                choosenColumnPiece = 0;
            } else if(v.getId()==R.id.choix8) {
                this.piece = getResources().getDrawable(R.drawable.puzzle13);
                findViewById(R.id.choix8).setVisibility(View.INVISIBLE);
                choosenRowPiece = 3;
                choosenColumnPiece = 0;
            } else if(v.getId()==R.id.choix9) {
                this.piece = getResources().getDrawable(R.drawable.puzzle2);
                findViewById(R.id.choix9).setVisibility(View.INVISIBLE);
                choosenRowPiece = 0;
                choosenColumnPiece = 1;
            } else if(v.getId()==R.id.choix10) {
                this.piece = getResources().getDrawable(R.drawable.puzzle14);
                findViewById(R.id.choix10).setVisibility(View.INVISIBLE);
                choosenRowPiece = 3;
                choosenColumnPiece = 1;
            } else if(v.getId()==R.id.choix11) {
                this.piece = getResources().getDrawable(R.drawable.puzzle15);
                findViewById(R.id.choix11).setVisibility(View.INVISIBLE);
                choosenRowPiece = 3;
                choosenColumnPiece = 2;
            } else if(v.getId()==R.id.choix12) {
                this.piece = getResources().getDrawable(R.drawable.puzzle16);
                findViewById(R.id.choix12).setVisibility(View.INVISIBLE);
                choosenRowPiece = 3;
                choosenColumnPiece = 3;
            } else if(v.getId()==R.id.choix13) {
                this.piece = getResources().getDrawable(R.drawable.puzzle12);
                findViewById(R.id.choix13).setVisibility(View.INVISIBLE);
                choosenRowPiece = 2;
                choosenColumnPiece = 3;
            } else if(v.getId()==R.id.choix14) {
                this.piece = getResources().getDrawable(R.drawable.puzzle10);
                findViewById(R.id.choix14).setVisibility(View.INVISIBLE);
                choosenRowPiece = 2;
                choosenColumnPiece = 1;
            } else if(v.getId()==R.id.choix15) {
                this.piece = getResources().getDrawable(R.drawable.puzzle4);
                findViewById(R.id.choix15).setVisibility(View.INVISIBLE);
                choosenRowPiece = 0;
                choosenColumnPiece = 3;
            } else if(v.getId()==R.id.choix16) {
                this.piece = getResources().getDrawable(R.drawable.puzzle6);
                findViewById(R.id.choix16).setVisibility(View.INVISIBLE);
                choosenRowPiece = 1;
                choosenColumnPiece = 1;
            }
        }
    }

    public void remplace1(View view) {
        remplacePiece(view, findViewById(R.id.piece1), 0, 0);
    }

    public void remplace2(View view) {
        remplacePiece(view, findViewById(R.id.piece2), 0, 1);
    }

    public void remplace3(View view) {
        remplacePiece(view, findViewById(R.id.piece3), 0, 2);
    }

    public void remplace4(View view) {
        remplacePiece(view, findViewById(R.id.piece4), 0, 3);
    }

    public void remplace5(View view) {
        remplacePiece(view, findViewById(R.id.piece5), 1, 0);
    }

    public void remplace6(View view) {
        remplacePiece(view, findViewById(R.id.piece6), 1, 1);
    }

    public void remplace7(View view) {
        remplacePiece(view, findViewById(R.id.piece7), 1, 2);
    }

    public void remplace8(View view) {
        remplacePiece(view, findViewById(R.id.piece8), 1, 3);
    }

    public void remplace9(View view) {
        remplacePiece(view, findViewById(R.id.piece9), 2, 0);
    }

    public void remplace10(View view) {
        remplacePiece(view, findViewById(R.id.piece10), 2, 1);
    }

    public void remplace11(View view) {
        remplacePiece(view, findViewById(R.id.piece11), 2, 2);
    }

    public void remplace12(View view) {
        remplacePiece(view, findViewById(R.id.piece12), 2, 3);
    }

    public void remplace13(View view) {
        remplacePiece(view, findViewById(R.id.piece13), 3, 0);
    }

    public void remplace14(View view) {
        remplacePiece(view, findViewById(R.id.piece14), 3, 1);
    }

    public void remplace15(View view) {
        remplacePiece(view, findViewById(R.id.piece15), 3, 2);
    }

    public void remplace16(View view) {
        remplacePiece(view, findViewById(R.id.piece16), 3, 3);
    }

    public void remplacePiece(View view, ImageView imageView, int rowPlayerChoosen, int columnPlayerChoosen) {
        if (!lock) {
            lock = true;
            runOnUiThread(() -> view.setOnClickListener(null));
            remplace(imageView);
            if (this.piece != null){
                sendPiece(rowPlayerChoosen, columnPlayerChoosen, choosenRowPiece, choosenColumnPiece);
            }
            waitBeforeUnlock();
        }
    }

    public void waitBeforeUnlock(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(150);
                lock = false;
            } catch (InterruptedException ignored) {
                //
            }
        });
        t.start();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void remplace(ImageView v) {
        if(this.piece!=null) {
            v.setImageDrawable(this.piece);
            Thread t = new Thread(() -> {
               try {
                   Thread.sleep(90);
                   this.piece = null;
               } catch (InterruptedException ignored) {}
            });
            v.setClickable(false);
            this.nbPieces++;
            t.start();
            try {
                t.join();
            } catch (InterruptedException ignored) {}
            if (isTheMainUser){
                if(this.nbPieces>=16) {
                    //Toast.makeText(PuzzleActivity.this, String.valueOf(score()), Toast.LENGTH_SHORT).show();
                    gameFinished(score());
                }
            }

        }
    }

    public void sendPiece(int rowChoosen, int columnChoosen, int rowPiece, int columnPiece) {
        socket.emit("send puzzle piece", rowChoosen, columnChoosen, rowPiece, columnPiece);
    }

    public Boolean score() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                TableRow t = (TableRow) this.mat.getChildAt(y);
                ImageView p = (ImageView) t.getChildAt(x);
                int c = y * 4 + x + 1;
                String uri = "@drawable/puzzle" + c;
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                Drawable d = getResources().getDrawable(imageResource);
                if (!p.getDrawable().getConstantState().equals(d.getConstantState())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String booleanMatrixToJson(boolean[][] matrix) {
        JSONArray jsonArray = new JSONArray();
        for (boolean[] row : matrix) {
            JSONArray jsonRow = new JSONArray();
            for (boolean value : row) {
                jsonRow.put(value);
            }
            jsonArray.put(jsonRow);
        }
        return jsonArray.toString();
    }

    public boolean[][] jsonToBooleanMatrix(String jsonMatrix) {
        try {
            JSONArray jsonArray = new JSONArray(jsonMatrix);
            boolean[][] matrix = new boolean[jsonArray.length()][];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray jsonRow = jsonArray.getJSONArray(i);
                boolean[] row = new boolean[jsonRow.length()];
                for (int j = 0; j < jsonRow.length(); j++) {
                    row[j] = jsonRow.getBoolean(j);
                }
                matrix[i] = row;
            }
            return matrix;
        } catch (JSONException e) {
            Log.e("error", "JSON ERROR");
            throw new RuntimeException(e);
        }

    }
}