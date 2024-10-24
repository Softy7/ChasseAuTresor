package com.example.Chasse.View;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import java.util.ArrayList;
import java.util.List;
import static android.R.color.holo_red_dark;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Classe qui affiche une carte auquel sera placé des points en pixel grâce aux coordonnées GPS
 */
public class MapWithPointsView extends PhotoView {

    private Paint paint;
    private final List<Point> points = new ArrayList<>();

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
        // Initialisation de Paint
        paint = new Paint();
        // Ajout d'une couleur
        paint.setColor(getResources().getColor(holo_red_dark));
        // Ajout du style FILL
        paint.setStyle(Paint.Style.FILL);
        // Ajout de l'antianliasing
        paint.setAntiAlias(true);
        // Taille du paint
        paint.setStrokeWidth(20);
    }

    /**
     * Permet de rajouter un point en coordonnées en pixel
     * @param x: position x (longueur)
     * @param y: position y (largeur)
     */
    public void addPoint(int x, int y) {
        points.add(new Point(x, y));
        // invalidate() permet d'appeler la méthode onDraw
        invalidate();
    }

    /**
     * Permet d'effacer tous les points
     */
    public void clearPoints() {
        points.clear();
        invalidate();
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
        for (Point point : points) {
            // Ajout des points sur une image
            float[] mappedPoint = new float[2];
            displayMatrix.mapPoints(mappedPoint, new float[]{point.x, point.y});
            // Dessine un cercle sur l'image de dimension 10 en position x, y
            canvas.drawCircle(mappedPoint[0], mappedPoint[1], 10, paint);
        }

    }

    /**
     * Classe statique qui donne des positions x, y des points en pixel
     */
    private static class Point{
        int x;
        int y;

        /**
         * Initialise une classe point
         * @param x: position x
         * @param y: position y
         */

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
