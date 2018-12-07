/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phd;
   
import java.util.HashSet;

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
int i=2;
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

}



}
