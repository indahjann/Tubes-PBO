package wumpusworld;

import java.awt.Graphics;
import java.awt.Image;

public abstract class GameObject {
    private int x;
    private int y;
    protected String textureName;
    
    public GameObject(int x, int y, String textureName) {
        this.x = x;
        this.y = y;
        this.textureName = textureName;
    }
    
    public void draw(Graphics g, int xPos, int yPos, int tileWidth, int tileHeight) {
        
        Image texture = TextureManager.getInstance().getTexture(textureName);
        if (texture != null) {
            g.drawImage(texture, xPos, yPos, tileWidth, tileHeight, null);
        } else {
            System.out.println("ERROR: Failed to get texture for " + textureName);
        }
    }
    
    // Implementasi abstract methods
    public abstract void interact(Player player);
    public abstract String getSensation();
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
