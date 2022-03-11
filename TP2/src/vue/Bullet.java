package vue;
import static controleur.Controleur.HAUTEUR;
import static controleur.Controleur.HAUTEUR_PLANCHER;
import static controleur.Controleur.LONGUEUR;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 1767250
 */
public class Bullet extends Entite implements Bougeable, Collisionnable {

    private int deltaX = 12, deltaY = 0;
  /**
     * @param x position en x
     * @param y position en y
     */
    
    public Bullet(float x, float y) {
        super(x, y, 16,16,"images/boulet.png");
    }

    /**
     *fait bouger vers la droite ou vers le haut si besoin et prépare
     * à la destruction si l'objet dépasse les bornes
     */
    @Override
    public void bouger() {
        x = x + deltaX;
        y = y + deltaY;

        if (x > LONGUEUR + buffer||y>HAUTEUR-HAUTEUR_PLANCHER-height||y<0) {
            this.setDetruire(true);
        }
    }

  
    protected void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }

}
