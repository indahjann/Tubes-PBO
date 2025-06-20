/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wumpusworld;

/**
 *
 * @author Haidar Ali
 */
public class Pit extends GameObject {
    
    public Pit(int x, int y) {
        super(x, y, "pit");
    }
    
    @Override
    public void interact(Player player) {
        System.out.println("Player fell into a pit!");
    }
    
    @Override
    public String getSensation() {
        return "You feel a breeze.";
    }
}
