package wumpusworld;

public class Cell {
    // Attributes
    private GameObject content;
    private boolean isVisible;
    private String floorTexture; // Untuk tipe lantai
    
    // Constructor
    public Cell() {
        this.content = null;
        this.isVisible = false;
        this.floorTexture = "floor"; 
    }
    
    // Methods
    public GameObject getContent() {
        return content;
    }
    
    public void setContent(GameObject content) {
        this.content = content;
        
        // Mengubah texture ke gold floor kalau gold
        if (content instanceof Gold) {
            setFloorTexture("gold_floor");
        }
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
    
    public boolean isEmpty() {
        return content == null;
    }
    
    public String getFloorTexture() {
        return floorTexture;
    }
    
    public void setFloorTexture(String floorTexture) {
        this.floorTexture = floorTexture;
    }
}
