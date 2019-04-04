/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phd;
   
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class test {
 
public static void den1 (int i){
     
 i++;
}

public static int[] den2 (int i){
    
 return new int[] {1,2};
 
}

public static void main(String[] args) {

int k;    
String a="aaa"+5+"aaa";
int i=2,j;
Boolean input = Boolean.valueOf("1>2");
HashSet<Integer> aSet =new HashSet<Integer>();

/*
if (input) {
    System.out.println("YES");
} else 
{
    System.out.println("NO");
}
System.out.println(Boolean.valueOf(a));
System.out.println(Boolean.getBoolean(a));
System.out.println(Boolean.parseBoolean(a));

System.out.println("a:"+a);
den1(i);
System.out.println("i:"+i);

System.out.println("Reverse testing");
    for (i=1;i<=5;i++)
    {
        System.out.println(i+" "+(i%6+1));
    }
    int a1=Phd_Repeat.MAX_RATING+1;
        for (i=1;i<=5;i++)
    {
        System.out.println( +i+" "+i%a1+" "+(a1-(i%a1)));
    }

*/    

/*
k=den2(1)[1];

System.out.println(k);
*/
/*
    for (i=1;i<5;i++)
    {
        System.out.println("i="+i);
    }
    */

/*
aSet.add(1);aSet.add(2);aSet.add(3);aSet.add(3);
//iterate over set
for (int s: aSet) {
      System.out.println("Number = " + s);
}
k=3;

if (aSet.contains(k))
System.out.println("AEK OLE");
else
    aSet.add(k);


for (int s: aSet) {
      System.out.println("Number = " + s);
}

for (int s: aSet) {
      System.out.println("Number = " + s);
}

*/

/* HASHTABLE TEST1 (IF TEST3 Succeeds i can erase it)
Hashtable<CellCoor,UserMovie>  userMovies = new Hashtable(10000);
CellCoor cell = new CellCoor(2,2);

System.out.println("Size"+userMovies.size());
userMovies.put(new CellCoor(1,1), new UserMovie(1,1,1, 1,0));
System.out.println("Size"+userMovies.size());
userMovies.put(new CellCoor(1,2), new UserMovie(1,1,1, 1,0));

userMovies.put(cell, new UserMovie(10,10,1, 1,0));
System.out.println("Size"+userMovies.size());
System.out.println(userMovies.get(cell).Movie_Id);

i=5;j=5;
cell=new CellCoor(); 
cell.movie=10;cell.user=1;
userMovies.put(cell, new UserMovie(20,20,1, 1,0));
System.out.println("Size"+userMovies.size());
System.out.println(userMovies.get(cell).Movie_Id);

Set<CellCoor> keys = userMovies.keySet();

for (CellCoor key: keys)
{
        //System.out.println("Size"+userMovies.size());
        System.out.println("Value of:"+key.user+" "+key.movie+" is:"+userMovies.get(key).User_Id+" "+userMovies.get(key).Movie_Id + " Rating "+ userMovies.get(key).getRating()+" InvRating "+ userMovies.get(key).invRating);
}  

CellCoor  cell1 = new CellCoor(199,1);
if (userMovies.containsKey(cell1)==true)
    System.out.println("Null"+userMovies.containsKey(cell1));
else
{
    System.out.println("Not Null"+userMovies.containsKey(cell1));
    System.out.println(cell1.user+" "+cell1.movie+" ");

}

*/

// HASHTABLE TEST2

Hashtable<String,String> hashtest = new Hashtable();

hashtest.put("A","A1");
hashtest.put("A","A2");
hashtest.put("B","A1");
hashtest.put("C","A1");
hashtest.put("D","A1");

Set<String> keys = hashtest.keySet();
for (String key: keys)
{
        //System.out.println("Size"+userMovies.size());
        System.out.println("Value of key:"+key+" Value of Object:"+hashtest.get(key));
}  
if (hashtest.containsKey("A"))
    System.out.println("Key A exists:"+hashtest.containsKey("A")+" object:"+hashtest.get("A"));
else
    System.out.println("No Key A exists:"+hashtest.containsKey("A"));

if (hashtest.containsKey("B"))
    System.out.println("Key B exists:"+hashtest.containsKey("B")+" object:"+hashtest.get("B"));
else
    System.out.println("No Key B exists:"+hashtest.containsKey("B"));

if (hashtest.containsKey("E"))
    System.out.println("Key E exists:"+hashtest.containsKey("E")+" object:"+hashtest.get("E"));
else
{
    System.out.println("No Key E exists:"+hashtest.containsKey("E")+" object:"+hashtest.get("E"));
    if (hashtest.get("E")==null) System.out.println("Got the null!"); else System.out.println("Cannot Got the null!");
}

// HASHTABLE TEST3
System.out.println("\nnew test\n");

Hashtable<CellCoor,String> hashtest2 = new Hashtable();
CellCoor cell = new CellCoor(1,1);

hashtest2.put(cell,"A1");
//hashtest2.put(new CellCoor(1,1),"A1");
//hashtest2.put(new CellCoor(1,1),"B1");
hashtest2.put(new CellCoor(2,1),"C1");
hashtest2.put(new CellCoor(3,1),"D1");
hashtest2.put(new CellCoor(4,1),"E1");

Set<CellCoor> keys2 = hashtest2.keySet();
for (CellCoor key2: keys2)
{
        //System.out.println("Size"+userMovies.size());
        System.out.println("Value of key:"+key2.user+" "+key2.movie+" Value of Object:"+hashtest2.get(key2));
}  

CellCoor test = new CellCoor(2,1);

if (hashtest2.get(cell)!=null)
    System.out.println("Key (1,1) exists:"+hashtest2.containsKey(cell)+" object:"+hashtest2.get(cell));
else
    System.out.println("No Key (1,1) exists:"+hashtest2.containsKey(test));

cell.user=2;cell.movie=1;
if (hashtest2.get(cell)!=null)
    System.out.println("Key (2,1) exists:"+hashtest2.containsKey(cell)+" object:"+hashtest2.get(cell));
else
    System.out.println("No Key (2,1) exists:"+hashtest2.containsKey(test));


if (hashtest.containsKey(test))
    System.out.println("Key (2,1) exists:"+hashtest2.containsKey(test)+" object:"+hashtest2.get(test));
else
    System.out.println("No Key (2,1) exists:"+hashtest2.containsKey(test));

if (hashtest.containsKey("E"))
    System.out.println("Key E exists:"+hashtest.containsKey("E")+" object:"+hashtest.get("E"));
else
{
    System.out.println("No Key E exists:"+hashtest.containsKey("E")+" object:"+hashtest.get("E"));
    if (hashtest.get("E")==null) System.out.println("Got the null!"); else System.out.println("Cannot Got the null!");
}
} //Main

} //class Test
