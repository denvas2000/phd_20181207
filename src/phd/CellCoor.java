/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phd;

/**
 *
 * @author denis
 */
public class CellCoor{
    
public int user;
public int movie;

public CellCoor (int row, int col)
{
    user=row;
    movie=col;
}        

public CellCoor()
{
    
}        

@Override
public boolean equals (Object o) {

if (this ==o) return true;
if ((o==null) || this.getClass()!=o.getClass()) return false;

CellCoor cell = (CellCoor) o;

return user==cell.user && movie==cell.movie;

}

//As seen at Josh Bloch's Effecrive Java (Item 8) 2nd Ed.
@Override
public int hashCode() {
    int hash = 7;
    hash = 59 * hash + this.user;
    hash = 59 * hash + this.movie;
    return hash;
}

}
