/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phd;

import java.util.*;

/**
 *
 * @author Administrator
 */
public class HashTest {
    public static void main (String args[]) {
        
        HashMap hm = new HashMap();
        HashMap<Integer, User> hmuser = new HashMap<>();
        HashMap<Integer, User> hmuser1 = new HashMap<>();
        HashMap<Integer, HashMap<Integer, User>> hmuser2 = new HashMap<>();
        User curuser;
        HashMap hasharray[] = new HashMap[10];
        
        
        hmuser.put(1,new User(2001,200,200));
        hmuser.put(2,new User(3001,200,200));
        
        hm.put(1, 10);
        hm.put(2, 50);
        hm.put(1, 10);
        hm.put(1, 40);
        
        
        //SIMPLE HASH MAP
        
        // Get a set of the entries
        Set set = hm.entrySet();
        
        // Get an iterator
        Iterator i = set.iterator();
        
        while(i.hasNext()) {
         Map.Entry me = (Map.Entry)i.next();
         System.out.print(me.getKey() + ": ");
         System.out.println(me.getValue());
        }
        
        System.out.println(hm.get(2));
        
        
        //COMPOSITE HASH MAP #1
        
        Set userset = hmuser.entrySet();
        i=userset.iterator();
        
        while(i.hasNext()) {
         Map.Entry me = (Map.Entry)i.next();
         System.out.print(me.getKey() + ": ");
         curuser=(User)me.getValue();
         System.out.println(curuser.UserId);
        }
        System.out.println();

        
        //COMPOSITE HASH MAP #2
        hmuser1.put(1,new User(1200,200,200));
        hmuser1.put(2,new User(600,200,200));
        hmuser2.put(1,hmuser1);

        hmuser1= new HashMap(); //CRUCIAL
        
        hmuser1.put(3,new User(500,200,200));
        hmuser1.put(4,new User(800,200,200));
        hmuser2.put(5,hmuser1);
        
        Set userset2 = hmuser2.entrySet();
        userset = hmuser.entrySet();
        

        Iterator j=userset2.iterator();
        
        while(j.hasNext())  {
         
        Map.Entry me = (Map.Entry)j.next();
        System.out.println("a "+me.getKey() + ": ");
        hmuser1=(HashMap)me.getValue();
        
        userset = hmuser1.entrySet();
        i=userset.iterator();
        
        while(i.hasNext()){

            Map.Entry me2 = (Map.Entry)i.next();
            System.out.print(me2.getKey() + ": ");
            curuser=(User)me2.getValue();
            System.out.println("b "+curuser.UserId);
        }
        System.out.println();
        }
        
        //COMPOSITE HASH MAP WITH ARRAY
        
        hmuser1= new HashMap(); //CRUCIAL
        
        hmuser1.put(3,new User(1500,200,200));
        hmuser1.put(4,new User(1800,200,200));
        hasharray[0]=hmuser1;        
        
        hmuser1= new HashMap(); //CRUCIAL
        
        hmuser1.put(1,new User(11500,200,200));
        hmuser1.put(2,new User(11800,200,200));
        
        hasharray[1]=hmuser1;
        
        int k=0;
        for (k=0;k<2;k++) {

            userset = hasharray[k].entrySet();
            i=userset.iterator();
            System.out.println("C "+k+ ": ");
            while(i.hasNext()){

            Map.Entry me2 = (Map.Entry)i.next();
            System.out.print(me2.getKey() + ": ");
            curuser=(User)me2.getValue();
            System.out.println("c "+curuser.UserId);
            }
            System.out.println();
        }
        
        User den = (User)hasharray[0].get(4);
        System.out.println("Get [0,4] value "+den.UserId);
        
        den = (User)hasharray[1].get(2);
        System.out.println("Get [1,2] value "+den.UserId);
    }

}    
