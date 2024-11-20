package com.example.Chasse.View;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import java.util.ArrayList;
import java.util.List;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Classe qui affiche une carte auquel sera placé des points en pixel grâce aux coordonnées GPS
 */
public class MapWithPointsView extends PhotoView {

    //private Paint paintRed; // pour le point où aller
    //private Paint paintBlue; // pour le joueur 1
    //private Paint paintGreen; // pour le joueur 2
    //private Paint paintDark; // autres points
    private final Paint[] paints = new Paint[3];
    private final Point[] pointsTab = new Point[3];
    private static final int[] COLORS = new int[]{
            android.R.color.holo_red_dark,
            android.R.color.holo_blue_dark,
            android.R.color.holo_green_dark
    };
    private final static double MULTIPLICATOR = 2.63;
    //private final List<Point> points = new ArrayList<>();

    public MapWithPointsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MapWithPointsView(Context context) {
        super(context);
        init(context);
    }

    /**
     * Permet d'initialiser la classe
     * @param context: Context récupéré lors de la création de classe
     */
    private void init(Context context) {

        for (int i = 0; i < paints.length; i++) {
            // Initialisation de Paint
            paints[i] = new Paint();
            // Ajout d'une couleur
            paints[i].setColor(getResources().getColor(COLORS[i]));
            // Ajout du style FILL
            paints[i].setStyle(Paint.Style.FILL);
            // Ajout de l'antianliasing
            paints[i].setAntiAlias(true);
            // Taille du paint
            paints[i].setStrokeWidth(20);
            pointsTab[i] = new Point(-99999, -99999); // permet d'initialiser et de ne pas afficher le point
        }
    }

    public int[] getPointPosition(int index) {
        return new int[]{(int) ((int) pointsTab[index].x / MULTIPLICATOR), (int) ((int) pointsTab[index].y / MULTIPLICATOR)};
    }

    /**
     * Permet de rajouter un point en coordonnées en pixel
     * @param x: position x (longueur)
     * @param y: position y (largeur)
     * @param codeToAddPoint :
     *        1 = Pour la localisation où aller
     *        2 = Pour la position du joueur 1
     *        3 = Pour la position du joueur 2
     */
    public void modifyPointPosition(int x, int y, int codeToAddPoint) {
        if (codeToAddPoint >= 1 && codeToAddPoint <= pointsTab.length) {
            pointsTab[codeToAddPoint - 1].x = (int) Math.round(x * MULTIPLICATOR);
            pointsTab[codeToAddPoint - 1].y = (int) Math.round(y * MULTIPLICATOR);
            invalidate();
        }
        //points.add(new Point(x, y));
        // invalidate() permet d'appeler la méthode onDraw
        //invalidate();
    }

    /**
     * Permet d'effacer tous les points
     * @param codeToClearPoint :
     *    1 = Pour la localisation où aller
     *    2 = Pour la position du joueur 1
     *    3 = Pour la position du joueur 2
     */
    public void removePoint(int codeToClearPoint) {
        if (codeToClearPoint >= 1 && codeToClearPoint <= pointsTab.length) {
            pointsTab[codeToClearPoint - 1].x = -999999;
            pointsTab[codeToClearPoint - 1].y = -999999;
            invalidate();
        }
    }

    /**
     * Permet de dessiner sur l'image
     * @param canvas: Canvas utilisé pour dessiner sur l'image
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Récupère la matrice de l'image
        Matrix displayMatrix = this.getImageMatrix();
        for (int i = 0; i < pointsTab.length; i++) {
            // Ajout des points sur une image
            float[] mappedPoint = new float[2];
            displayMatrix.mapPoints(mappedPoint, new float[]{pointsTab[i].x, pointsTab[i].y});
            // Dessine un cercle sur l'image de dimension 10 en position x, y
            canvas.drawCircle(mappedPoint[0], mappedPoint[1], 10, paints[i]);
        }

    }

    /**
     * Classe statique qui donne des positions x, y des points en pixel
     */
    private static class Point{
        private int x;
        private int y;

        /**
         * Initialise une classe point
         * @param x: position x
         * @param y: position y
         */

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

}
