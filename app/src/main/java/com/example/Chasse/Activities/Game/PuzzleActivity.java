package com.example.Chasse.Activities.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.Chasse.R;

public class PuzzleActivity extends AppCompatActivity {

    private Drawable piece;
    private int nbPieces = 0;

    private TableLayout mat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        this.piece = null;

        this.mat = findViewById(R.id.mat);
    }


    public void choixPiece(View v) {
        if(this.piece==null) {
            if(v.getId()==R.id.choix1) {
                this.piece = getResources().getDrawable(R.drawable.puzzle3);
                findViewById(R.id.choix1).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix2) {
                this.piece = getResources().getDrawable(R.drawable.puzzle1);
                findViewById(R.id.choix2).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix3) {
                this.piece = getResources().getDrawable(R.drawable.puzzle5);
                findViewById(R.id.choix3).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix4) {
                this.piece = getResources().getDrawable(R.drawable.puzzle11);
                findViewById(R.id.choix4).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix5) {
                this.piece = getResources().getDrawable(R.drawable.puzzle8);
                findViewById(R.id.choix5).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix6) {
                this.piece = getResources().getDrawable(R.drawable.puzzle7);
                findViewById(R.id.choix6).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix7) {
                this.piece = getResources().getDrawable(R.drawable.puzzle9);
                findViewById(R.id.choix7).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix8) {
                this.piece = getResources().getDrawable(R.drawable.puzzle13);
                findViewById(R.id.choix8).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix9) {
                this.piece = getResources().getDrawable(R.drawable.puzzle2);
                findViewById(R.id.choix9).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix10) {
                this.piece = getResources().getDrawable(R.drawable.puzzle14);
                findViewById(R.id.choix10).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix11) {
                this.piece = getResources().getDrawable(R.drawable.puzzle15);
                findViewById(R.id.choix11).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix12) {
                this.piece = getResources().getDrawable(R.drawable.puzzle16);
                findViewById(R.id.choix12).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix13) {
                this.piece = getResources().getDrawable(R.drawable.puzzle12);
                findViewById(R.id.choix13).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix14) {
                this.piece = getResources().getDrawable(R.drawable.puzzle10);
                findViewById(R.id.choix14).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix15) {
                this.piece = getResources().getDrawable(R.drawable.puzzle4);
                findViewById(R.id.choix15).setVisibility(View.INVISIBLE);
            } else if(v.getId()==R.id.choix16) {
                this.piece = getResources().getDrawable(R.drawable.puzzle6);
                findViewById(R.id.choix16).setVisibility(View.INVISIBLE);
            }
        }
    }

    public void remplace1(View view) {
        remplace(findViewById(R.id.piece1));
    }
    public void remplace2(View view) {
        remplace(findViewById(R.id.piece2));
    }
    public void remplace3(View view) {
        remplace(findViewById(R.id.piece3));
    }
    public void remplace4(View view) {
        remplace(findViewById(R.id.piece4));
    }
    public void remplace5(View view) {
        remplace(findViewById(R.id.piece5));
    }
    public void remplace6(View view) {
        remplace(findViewById(R.id.piece6));
    }
    public void remplace7(View view) {
        remplace(findViewById(R.id.piece7));
    }
    public void remplace8(View view) {
        remplace(findViewById(R.id.piece8));
    }
    public void remplace9(View view) {
        remplace(findViewById(R.id.piece9));
    }
    public void remplace10(View view) {
        remplace(findViewById(R.id.piece10));
    }
    public void remplace11(View view) {
        remplace(findViewById(R.id.piece11));
    }
    public void remplace12(View view) {
        remplace(findViewById(R.id.piece12));
    }
    public void remplace13(View view) {
        remplace(findViewById(R.id.piece13));
    }
    public void remplace14(View view) {
        remplace(findViewById(R.id.piece14));
    }
    public void remplace15(View view) {
        remplace(findViewById(R.id.piece15));
    }
    public void remplace16(View view) {
        remplace(findViewById(R.id.piece16));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void remplace(ImageView v) {
        if(this.piece!=null) {
            v.setImageDrawable(this.piece);
            this.piece = null;
            v.setClickable(false);
            this.nbPieces++;
            if(this.nbPieces>=16) {
                Toast.makeText(PuzzleActivity.this, String.valueOf(score()), Toast.LENGTH_SHORT).show();
            }
        }
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
}