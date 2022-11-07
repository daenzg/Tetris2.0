import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.List;

/**
 * @author (Daniel Furrer, Christian Cidecian)
 * @version (v2.69)
 */
public class Game extends World
{
    public Game()
    {    
        super(10, 20, 32, false);
        prepare();
    }
    Block currentBlocks[] = new Block[4];
    BlockType currentShape;
    int rotation;
    boolean rotatedLeft = false;
    boolean rotatedRight = false;
    int time = 0;
    int delay = 0;
    int gravity = 48;
    int drop = 2;
    boolean dropped = false;
    boolean downHeld = false;
    int rows[] = new int[getHeight()];
    private void prepare()
    {
        Greenfoot.setSpeed(50);
        GreenfootImage image = new GreenfootImage(getWidth(), getHeight());
        image.setColor(Color.BLACK);
        image.fillRect(0, 0, getWidth(), getHeight());
        this.setBackground(image);

        newBlocks();
    }

    public void act()
    {
        boolean collideRight = collideRight();
        boolean collideLeft = collideLeft();
        boolean collideBottomNoStop = collideBottomNoStop();
        
        time++;
        if (Greenfoot.isKeyDown("down") && !dropped && !downHeld)
        {
            time = gravity;                                                     //
            dropped = true;                                                     //Blöcke schneller senken
            downHeld = true;                                                    //
        }
        else if (!Greenfoot.isKeyDown("down"))
        {
            dropped = false;                                                    //Sicherstellen dass Blöcke nicht schneller gesenkt werden
            downHeld = false;                                                   //
        }
        
        if (Greenfoot.isKeyDown("left") && delay >= 10 && !collideLeft)
        {
            delay = 0;                                                                              //
            currentBlocks[0].setLocation(currentBlocks[0].getX() - 1, currentBlocks[0].getY());     //
            currentBlocks[1].setLocation(currentBlocks[1].getX() - 1, currentBlocks[1].getY());     //Bewegung der Blöcke nach links (X Koordinaten -1)
            currentBlocks[2].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY());     //
            currentBlocks[3].setLocation(currentBlocks[3].getX() - 1, currentBlocks[3].getY());     //
        }
        else { delay++; }
        if (Greenfoot.isKeyDown("right") && delay >= 10 && !collideRight)
        {
            delay = 0;                                                                              //
            currentBlocks[0].setLocation(currentBlocks[0].getX() + 1, currentBlocks[0].getY());     //
            currentBlocks[1].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY());     //Bewegung der Blöcke nach links (X Koordinaten +1)
            currentBlocks[2].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY());     //
            currentBlocks[3].setLocation(currentBlocks[3].getX() + 1, currentBlocks[3].getY());     //
        }
        else { delay++; }
        
        boolean shapeWillCollide = 
            (currentShape == BlockType.I && rotation == 90 ||                                                                   //
            (currentShape == BlockType.J || currentShape == BlockType.L) && (rotation == 90 || rotation == 270) ||              //
            (currentShape == BlockType.S || currentShape == BlockType.Z) && (rotation == 90) ||                                 //Prüft ob bei Rotierung die Form mir der Wand Kollidiert
            currentShape == BlockType.T && (rotation == 90 || rotation == 270)                                                  //
            );                                                                                                                  //
        
        boolean stopRotateLeft = shapeWillCollide && collideLeft || currentShape == BlockType.I && rotation == 90 && currentBlocks[0].getX() + 1 <= 2;              // Blockiert drehung wenn sie nicht möglich ist.
        boolean stopRotateRight = shapeWillCollide && collideRight || currentShape == BlockType.I && rotation == 90 && currentBlocks[0].getX() + 1 >= getWidth();   //
        
        if (!rotatedLeft && Greenfoot.isKeyDown("a") && !(stopRotateLeft || stopRotateRight) && !collideBottomNoStop)   //
        {                                                                                                               //
            rotateLeft();                                                                                               //
            rotatedLeft = true;                                                                                         //
        }                                                                                                               // Bewegung des Blockes nach links
        else if (!Greenfoot.isKeyDown("a"))                                                                             //
        {                                                                                                               //
            rotatedLeft = false;                                                                                        //
        }                                                                                                               //
        if (!rotatedRight && Greenfoot.isKeyDown("d") && !(stopRotateRight || stopRotateLeft)&& !collideBottomNoStop)   //
        {                                                                                                               //
            rotateRight();                                                                                              //
            rotatedRight = true;                                                                                        //
        }                                                                                                               // Bewegung des Blockes nach rechts
        else if (!Greenfoot.isKeyDown("d"))                                                                             //
        {                                                                                                               //
            rotatedRight = false;                                                                                       //
        }                                                                                                               //
        
        if ((time >= gravity || (dropped && time > drop)) && !collideBottom())                                          //
        {                                                                                                               //
            time = 0;                                                                                                   //
            currentBlocks[0].setLocation(currentBlocks[0].getX(), currentBlocks[0].getY() + 1);                         //
            currentBlocks[1].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() + 1);                         // Bewegung des Blockes nach unten
            currentBlocks[2].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() + 1);                         //
            currentBlocks[3].setLocation(currentBlocks[3].getX(), currentBlocks[3].getY() + 1);                         //
        }                                                                                                               //
    }
    
    public void newBlocks()
    {
        currentShape = BlockType.fromInt(Greenfoot.getRandomNumber(BlockType.values().length));                         // Wählt random Zahl und schribt Sie einem Buchstaben in BlockType zu.
        dropped = false; 
        rotation = 0;
        Color blockColor = Color.WHITE; 
        
        switch (currentShape) {                                                                                         // Erstellt Block wenn Bedingung Zutrifft
            case I:
                blockColor = Color.RED;
                currentBlocks[0] = new Block(blockColor);
                addObject(currentBlocks[0], 3, 0);
                currentBlocks[1] = new Block(blockColor);
                addObject(currentBlocks[1], 4, 0);                                                                      // erstellt I Block in rot
                currentBlocks[2] = new Block(blockColor);
                addObject(currentBlocks[2], 5, 0);
                currentBlocks[3] = new Block(blockColor);                                                               
                addObject(currentBlocks[3], 6, 0);
                break;

            case J:
                blockColor = Color.BLUE;
                currentBlocks[0] = new Block(blockColor);
                addObject(currentBlocks[0], 6, 1);
                currentBlocks[1] = new Block(blockColor);
                addObject(currentBlocks[1], 4, 0);                                                                      // erstellt J Block in blau
                currentBlocks[2] = new Block(blockColor);
                addObject(currentBlocks[2], 5, 0);
                currentBlocks[3] = new Block(blockColor);
                addObject(currentBlocks[3], 6, 0);
                break;

            case L:
                blockColor = Color.GREEN;
                currentBlocks[0] = new Block(blockColor);
                addObject(currentBlocks[0], 4, 1);
                currentBlocks[1] = new Block(blockColor);
                addObject(currentBlocks[1], 4, 0);                                                                      // erstellt L Block in grün
                currentBlocks[2] = new Block(blockColor);
                addObject(currentBlocks[2], 5, 0);
                currentBlocks[3] = new Block(blockColor);
                addObject(currentBlocks[3], 6, 0);
                break;

            case O:
                blockColor = Color.CYAN;
                currentBlocks[0] = new Block(blockColor);
                addObject(currentBlocks[0], 4, 0);
                currentBlocks[1] = new Block(blockColor);
                addObject(currentBlocks[1], 5, 0);                                                                      // erstellt O Block in türkis
                currentBlocks[2] = new Block(blockColor);
                addObject(currentBlocks[2], 4, 1);
                currentBlocks[3] = new Block(blockColor);
                addObject(currentBlocks[3], 5, 1);
                break;

            case S:
                blockColor = Color.ORANGE;
                currentBlocks[0] = new Block(blockColor);
                addObject(currentBlocks[0], 5, 0);
                currentBlocks[1] = new Block(blockColor);
                addObject(currentBlocks[1], 6, 0);                                                                      // erstellt S Block in rot
                currentBlocks[2] = new Block(blockColor);
                addObject(currentBlocks[2], 4, 1);
                currentBlocks[3] = new Block(blockColor);
                addObject(currentBlocks[3], 5, 1);
                break;

            case T:
                blockColor = Color.YELLOW;
                currentBlocks[0] = new Block(blockColor);
                addObject(currentBlocks[0], 4, 0);
                currentBlocks[1] = new Block(blockColor);
                addObject(currentBlocks[1], 5, 0);                                                                      // erstellt T Block in gelb
                currentBlocks[2] = new Block(blockColor);
                addObject(currentBlocks[2], 6, 0);
                currentBlocks[3] = new Block(blockColor);
                addObject(currentBlocks[3], 5, 1);
                break;

            case Z:
                blockColor = Color.MAGENTA;
                currentBlocks[0] = new Block(blockColor);
                addObject(currentBlocks[0], 4, 0);
                currentBlocks[1] = new Block(blockColor);
                addObject(currentBlocks[1], 5, 0);                                                                      // erstellt Z Block in pink
                currentBlocks[2] = new Block(blockColor);
                addObject(currentBlocks[2], 5, 1);
                currentBlocks[3] = new Block(blockColor);
                addObject(currentBlocks[3], 6, 1);
                break;
        }
    }
    
    public void checkRows()
    {
        for (int row = 0; row < rows.length; row++) //
        {
            if (rows[row] == 10)
            {
                for (int x = 0; x < getWidth(); x++)
                {
                    removeObjects(getObjectsAt(x, row, Block.class));
                }
                for (int y = row; y >= 0; y--)
                {
                    for (int x = 0; x < getWidth(); x++)
                    {
                        List blocks = getObjectsAt(x, y, Block.class);
                        for (Actor b : (List<Actor>) blocks)
                        {
                            b.setLocation(x, y + 1);
                        }
                    }
                    if (y != 0) { rows[y] = rows[y - 1]; }
                }
                rows[0] = 0;
            }
        }
    }
    
    public boolean collideBottom()
    {
        if (collideBottomNoStop())
        {
            if (currentBlocks[0].getY() == 0 || currentBlocks[1].getY() == 0 || currentBlocks[2].getY() == 0 || currentBlocks[3].getY() == 0) { Greenfoot.stop(); }
            rows[currentBlocks[0].getY()]++;
            rows[currentBlocks[1].getY()]++;
            rows[currentBlocks[2].getY()]++;
            rows[currentBlocks[3].getY()]++;
            checkRows();
            newBlocks();
            return true;
        }
        return false;
    }
    
    public boolean collideBottomNoStop()
    {
        for (Block b : currentBlocks)
        {
            List objs = getObjectsAt(b.getX(), b.getY() + 1, Block.class);
            objs.remove(currentBlocks[0]);
            objs.remove(currentBlocks[1]);
            objs.remove(currentBlocks[2]);
            objs.remove(currentBlocks[3]);
            if (b.getY() + 1 == getHeight() || !objs.isEmpty())
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean collideLeft()                                                
    {
        for (Block b : currentBlocks)
        {
            List objs = getObjectsAt(b.getX() - 1, b.getY(), Block.class);
            objs.remove(currentBlocks[0]);
            objs.remove(currentBlocks[1]);
            objs.remove(currentBlocks[2]);
            objs.remove(currentBlocks[3]);
            if (b.getX() == 0 || !objs.isEmpty())
            {
                return true;
            }
        }
        return false;
    }
    
    public boolean collideRight()
    {
        for (Block b : currentBlocks)
        {
            List objs = getObjectsAt(b.getX() + 1, b.getY(), Block.class);
            objs.remove(currentBlocks[0]);
            objs.remove(currentBlocks[1]);
            objs.remove(currentBlocks[2]);
            objs.remove(currentBlocks[3]);
            if (b.getX() + 1 == getWidth() || !objs.isEmpty())
            {
                return true;
            }
        }
        return false;
    }
    
    public void rotateLeft()
    {
        switch (currentShape) {
            case I:
                if (rotation == 0)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() - 2);
                    currentBlocks[1].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() - 1);             
                    currentBlocks[3].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() + 1);
                    rotation = 90;
                }
                else if (rotation == 90)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX() - 2, currentBlocks[2].getY());
                    currentBlocks[1].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY());
                    currentBlocks[3].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY());
                    rotation = 0;
                }
                break;

            case J:
                if (rotation == 0)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() + 1);
                    currentBlocks[1].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY() + 1);
                    currentBlocks[3].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() - 1);
                    rotation = 90;
                }
                else if (rotation == 90)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY() - 1);
                    currentBlocks[1].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY());
                    currentBlocks[3].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY());
                    rotation = 180;
                }
                else if (rotation == 180)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY() - 1);
                    currentBlocks[1].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() - 1);
                    currentBlocks[3].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() + 1);
                    rotation = 270;
                }
                else if (rotation == 270)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY() + 1);
                    currentBlocks[1].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY());
                    currentBlocks[3].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY());
                    rotation = 0;
                }
                break;

            case L:
                if (rotation == 0)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() - 1);
                    currentBlocks[2].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() + 1);
                    currentBlocks[3].setLocation(currentBlocks[1].getX() - 1, currentBlocks[1].getY() - 1);
                    rotation = 90;
                }
                else if (rotation == 90)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY());
                    currentBlocks[2].setLocation(currentBlocks[1].getX() - 1, currentBlocks[1].getY());
                    currentBlocks[3].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY() - 1);
                    rotation = 180;
                }
                else if (rotation == 180)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX() , currentBlocks[1].getY() + 1);
                    currentBlocks[2].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() - 1);
                    currentBlocks[3].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY() + 1);
                    rotation = 270;
                }
                else if (rotation == 270)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX() - 1, currentBlocks[1].getY());
                    currentBlocks[2].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY());
                    currentBlocks[3].setLocation(currentBlocks[1].getX() - 1, currentBlocks[1].getY() + 1);
                    rotation = 0;
                }
                break;

            case S:
                if (rotation == 0)
                {
                    currentBlocks[1].setLocation(currentBlocks[0].getX(), currentBlocks[0].getY() - 1);
                    currentBlocks[2].setLocation(currentBlocks[0].getX() + 1, currentBlocks[0].getY());
                    currentBlocks[3].setLocation(currentBlocks[0].getX() + 1, currentBlocks[0].getY() + 1);
                    rotation = 90;
                }
                else if (rotation == 90)
                {
                    currentBlocks[1].setLocation(currentBlocks[0].getX() + 1, currentBlocks[0].getY());
                    currentBlocks[2].setLocation(currentBlocks[0].getX() - 1, currentBlocks[0].getY() + 1);
                    currentBlocks[3].setLocation(currentBlocks[0].getX(), currentBlocks[0].getY() + 1);
                    rotation = 0;
                }
                break;

            case T:
                if (rotation == 0)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() - 1);
                    currentBlocks[2].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() + 1);
                    currentBlocks[3].setLocation(currentBlocks[1].getX() - 1, currentBlocks[1].getY());
                    rotation = 90;
                }
                else if (rotation == 90)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY());
                    currentBlocks[2].setLocation(currentBlocks[1].getX() - 1, currentBlocks[1].getY());
                    currentBlocks[3].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() - 1);
                    rotation = 180;
                }
                else if (rotation == 180)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() + 1);
                    currentBlocks[2].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() - 1);
                    currentBlocks[3].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY());
                    rotation = 270;
                }
                else if (rotation == 270)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX() - 1, currentBlocks[1].getY());
                    currentBlocks[2].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY());
                    currentBlocks[3].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() + 1);
                    rotation = 0;
                }
                break;

            case Z:
                if (rotation == 0)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() + 1);
                    currentBlocks[2].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY());
                    currentBlocks[3].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY() - 1);
                    rotation = 90;
                }
                else if (rotation == 90)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX() - 1, currentBlocks[1].getY());
                    currentBlocks[2].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() + 1);
                    currentBlocks[3].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY() + 1);
                    rotation = 0;
                }
                break;
        }
    }
    
    public void rotateRight()
    {
        switch (currentShape) {
            case I:
                if (rotation == 0)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() - 2);
                    currentBlocks[1].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() - 1);
                    currentBlocks[3].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() + 1);
                    rotation = 90;
                }
                else if (rotation == 90)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX() - 2, currentBlocks[2].getY());
                    currentBlocks[1].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY());
                    currentBlocks[3].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY());
                    rotation = 0;
                }
                break;

            case J:
                if (rotation == 0)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY() - 1);
                    currentBlocks[1].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() + 1);
                    currentBlocks[3].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() - 1);
                    rotation = 270;
                }
                else if (rotation == 90)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY() + 1);
                    currentBlocks[1].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY());
                    currentBlocks[3].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY());
                    rotation = 0;
                }
                else if (rotation == 180)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY() + 1);
                    currentBlocks[1].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() - 1);
                    currentBlocks[3].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() + 1);
                    rotation = 90;
                }
                else if (rotation == 270)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY() - 1);
                    currentBlocks[1].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY());
                    currentBlocks[3].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY());
                    rotation = 180;
                }
                break;

            case L:
                if (rotation == 0)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY() + 1);
                    currentBlocks[1].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() + 1);
                    currentBlocks[3].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() - 1);
                    rotation = 270;
                }
                else if (rotation == 90)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY() + 1);
                    currentBlocks[1].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY());
                    currentBlocks[3].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY());
                    rotation = 0;
                }
                else if (rotation == 180)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY() - 1);
                    currentBlocks[1].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() - 1);
                    currentBlocks[3].setLocation(currentBlocks[2].getX(), currentBlocks[2].getY() + 1);
                    rotation = 90;
                }
                else if (rotation == 270)
                {
                    currentBlocks[0].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY() - 1);
                    currentBlocks[1].setLocation(currentBlocks[2].getX() + 1, currentBlocks[2].getY());
                    currentBlocks[3].setLocation(currentBlocks[2].getX() - 1, currentBlocks[2].getY());
                    rotation = 180;
                }
                break;

            case S:
                if (rotation == 0)
                {
                    currentBlocks[1].setLocation(currentBlocks[0].getX(), currentBlocks[0].getY() - 1);
                    currentBlocks[2].setLocation(currentBlocks[0].getX() + 1, currentBlocks[0].getY());
                    currentBlocks[3].setLocation(currentBlocks[0].getX() + 1, currentBlocks[0].getY() + 1);
                    rotation = 90;
                }
                else if (rotation == 90)
                {
                    currentBlocks[1].setLocation(currentBlocks[0].getX() + 1, currentBlocks[0].getY());
                    currentBlocks[2].setLocation(currentBlocks[0].getX() - 1, currentBlocks[0].getY() + 1);
                    currentBlocks[3].setLocation(currentBlocks[0].getX(), currentBlocks[0].getY() + 1);
                    rotation = 0;
                }
                break;

            case T:
                if (rotation == 0)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() + 1);
                    currentBlocks[2].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() - 1);
                    currentBlocks[3].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY());
                    rotation = 270;
                }
                else if (rotation == 90)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX() - 1, currentBlocks[1].getY());
                    currentBlocks[2].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY());
                    currentBlocks[3].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() + 1);
                    rotation = 0;
                }
                else if (rotation == 180)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() - 1);
                    currentBlocks[2].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() + 1);
                    currentBlocks[3].setLocation(currentBlocks[1].getX() - 1, currentBlocks[1].getY());
                    rotation = 90;
                }
                else if (rotation == 270)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY());
                    currentBlocks[2].setLocation(currentBlocks[1].getX() - 1, currentBlocks[1].getY());
                    currentBlocks[3].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() - 1);
                    rotation = 180;
                }
                break;

            case Z:
                if (rotation == 0)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() + 1);
                    currentBlocks[2].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY());
                    currentBlocks[3].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY() - 1);
                    rotation = 90;
                }
                else if (rotation == 90)
                {
                    currentBlocks[0].setLocation(currentBlocks[1].getX() - 1, currentBlocks[1].getY());
                    currentBlocks[2].setLocation(currentBlocks[1].getX(), currentBlocks[1].getY() + 1);
                    currentBlocks[3].setLocation(currentBlocks[1].getX() + 1, currentBlocks[1].getY() + 1);
                    rotation = 0;
                }
                break;
        }
    }
}
