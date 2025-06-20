/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package wumpusworld;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Haidar Ali
 */
public class GameGrid {
    // Attributes
    private Cell[][] cells;
    private int ROWS;
    private int COLS;
    private Player player;
    private List<Gold> goldList;
    private List<Pit> pitList;
    private List<Wumpus> wumpusList; // Gunakan list untuk menyimpan banyak Wumpus
    
    // Constructor
    public GameGrid(int rows, int cols) {
        this.ROWS = rows;
        this.COLS = cols;
        this.cells = new Cell[rows][cols];
        
        // Initialize cells
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell();
            }
        }
        
        this.goldList = new ArrayList<>();
        this.pitList = new ArrayList<>();
        this.wumpusList = new ArrayList<>(); // Inisialisasi wumpusList
    }

    public List<Pit> getPitList() {
        return pitList;
    }
    public List<Wumpus> getWumpusList() {
        return wumpusList;
    }
    
    public Cell getCell(int r, int c) {
        if (isValidPosition(r, c)) {
            return cells[r][c];
        }
        return null;
    }
    
    public void placeObject(GameObject obj, int r, int c) {
        if (isValidPosition(r, c)) {
            cells[r][c].setContent(obj);
            obj.setPosition(r, c);
            
            // Add to respective list if object is Gold, Pit or Wumpus
            if (obj instanceof Gold) {
                goldList.add((Gold) obj);
            } else if (obj instanceof Pit) {
                pitList.add((Pit) obj);
            } else if (obj instanceof Wumpus) {
                wumpusList.add((Wumpus) obj); // Tambahkan ke list, bukan ke field tunggal
            } else if (obj instanceof Player) {
                this.player = (Player) obj;
            }
        }
    }
    
    public void removeObject(GameObject obj, int r, int c) {
        if (isValidPosition(r, c) && cells[r][c].getContent() == obj) {
            cells[r][c].setContent(null);
            
            // remove gold
            if (obj instanceof Gold) {
                goldList.remove(obj);
            } else if (obj instanceof Pit) {
                pitList.remove(obj);
            } else if (obj instanceof Wumpus) {
                wumpusList.remove(obj); // Hapus dari list
            }
        }
    }
    
    
    public int getRows() {
        return ROWS;
    }
    
    public int getCols() {
        return COLS;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public void setPlayer(Player p) {
        this.player = p;
    }
    
    public Wumpus getWumpus() {
        return wumpusList.isEmpty() ? null : wumpusList.get(0);
    }
    
    public List<Gold> getGoldList() {
        return goldList;
    }
    
    public boolean isValidPosition(int r, int c) {
        return r >= 0 && r < ROWS && c >= 0 && c < COLS;
    }
}
