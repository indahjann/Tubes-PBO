/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wumpusworld;

/**
 *
 * @author User
 */
public class Player extends GameObject {
    private int score;
    private int goldCollected;
    
    public Player(int x, int y) {
        super(x, y, "player");
        this.score = 0;
        this.goldCollected = 0;
    }
    
    
    public void move(int dx, int dy) {
        setPosition(getX() + dx, getY() + dy);
    }
    
    public void pickupGold() {
        goldCollected++;
    }
    
    public void setScore(int amount) {
        score = amount;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getGoldCollected() {
        return goldCollected;
    }
    
    @Override
    public void interact(Player player) {
        // Player gada interaksi ke dia sendiri
    }
    
    @Override
    public String getSensation() {
        return "";
    }
}
