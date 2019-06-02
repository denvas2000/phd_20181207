/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Administrator
 */
public class Predictions {

/*  *****
    Print_Predictions: Prints the predictions for all users. It can be modified so as to print any info of
    any/all users.
    *****
*/
    
public static void Print_Predictions (int totalUsers, User[] Users){

int  i;

System.out.println("Print Predictions\n");
for (i=0;i<=totalUsers;i++) 
    System.out.println("User:"+i+" Lastmovie:"+Users[i].lastMovieId +" Prediction:"+Users[i].getPrediction());
                  

} //END Print_Predictions


public static double[] Compute_Prediction (
int totalUsers, 
int totalMovies,
List<UserSimilarity>[] userSim,
User[] Users,
HashMap<CellCoor,UserMovie>  userMovies,
int predictionSign,
int bestNeigh)

{

int i, k, l;    
int simNeighbors=0, revSimNeighbors=0, NO3RevSimNeighbors=0;
List<UserSimilarity> UserList = new ArrayList<>();
double Numerator_Pred, Denominator_Pred;            //Numerator and Denominator of Prediction function.
int predictedValues=0, revPredictedValues=0, NO3RevPredictedValues=0;    //The total number of actually predicted values
double MAE=0.0, RevMAE=0.0, NO3RevMAE=0.0;                         //Mean Absolute Error of Prediction.
int minSimNeigh=0;
CellCoor cell = new CellCoor();
CellCoor cell1 = new CellCoor();

for (i=0;i<=totalUsers;i++) 
{

    UserList=userSim[i];
    
    Numerator_Pred=0;Denominator_Pred=0;
    k=Users[i].lastMovieId;
            
    //if (!UserList.isEmpty()) 
    if (UserList.size()>minSimNeigh) 
    {   
        if (bestNeigh<userSim[i].size())
            UserList=userSim[i].subList(0, bestNeigh-1);
        if (predictionSign==0)
            simNeighbors++;
        else
            if (predictionSign==1)
                revSimNeighbors++;
            else 
                NO3RevSimNeighbors++;

        for (UserSimilarity io: UserList)
        {
            cell.user=io.SUser_Id;cell.movie=k;        
            if (userMovies.get(cell)!=null)
            {  
                Denominator_Pred += Math.abs((io.Similarity));
                Numerator_Pred += io.Similarity*(userMovies.get(cell).getRating()-Users[io.SUser_Id].UserAverageRate());
                        
            }//if
        }//for
    }//if
    
          
    
    if (Denominator_Pred==0)                                            //Special Condition. When there are no NN that rated LastMovie or
        if (predictionSign==0)                                          //there are no NNs
            Users[i].setPrediction(Global_Vars.NO_PREDICTION);                      
        else
            if (predictionSign==1)
                Users[i].setRevPrediction(Global_Vars.NO_PREDICTION);
            else
                Users[i].setNO3RevPrediction(Global_Vars.NO_PREDICTION); 
                        
    else    //Maybe the check "!=NO_PREDICTION" is unnecessary
        if (predictionSign==0)
        {
            Users[i].setPrediction((int)Math.round(Users[i].UserAverageRate()+Numerator_Pred/Denominator_Pred));  //Normal Condition. When there are NN that rated LastMovie
                    
            if (Users[i].getPrediction()>5) Users[i].setPrediction(5);
            else
            if ((Users[i].getPrediction()<1) && (Users[i].getPrediction()!=Global_Vars.NO_PREDICTION))Users[i].setPrediction(1);                
            
            cell.user=i;cell.movie=Users[i].lastMovieId;              
            MAE += Math.abs(Users[i].getPrediction()-userMovies.get(cell).getRating());
            predictedValues++;
        }
        else 
            if (predictionSign==1)
            {
                Users[i].setRevPrediction((int)Math.round(Users[i].UserAverageRate()+Numerator_Pred/Denominator_Pred));  //Normal Condition. When there are FN that rated LastMovie
                   
                if (Users[i].getRevPrediction()>5) Users[i].setRevPrediction(5);
                else
                if ((Users[i].getRevPrediction()<1) && (Users[i].getRevPrediction()!=Global_Vars.NO_PREDICTION)) Users[i].setRevPrediction(1);          

                cell.user=i;cell.movie=k;              
                RevMAE += Math.abs(Users[i].getRevPrediction()-userMovies.get(cell).getRating());
                revPredictedValues++;
            }
            else
            {
                Users[i].setNO3RevPrediction((int)Math.round(Users[i].UserAverageRate()+Numerator_Pred/Denominator_Pred));  //Normal Condition. When there are FN that rated LastMovie                

                if (Users[i].getNO3RevPrediction()>5) Users[i].setNO3RevPrediction(5);
                else
                if ((Users[i].getNO3RevPrediction()<1) && (Users[i].getNO3RevPrediction()!=Global_Vars.NO_PREDICTION)) Users[i].setNO3RevPrediction(1);   

                cell.user=i;cell.movie=k;           
                NO3RevMAE += Math.abs(Users[i].getNO3RevPrediction()-userMovies.get(cell).getRating());
                NO3RevPredictedValues++;     
            }
            
    UserList=new ArrayList<>();
    
}    
return new double[] {simNeighbors, revSimNeighbors, NO3RevSimNeighbors, predictedValues, revPredictedValues, NO3RevPredictedValues, MAE, RevMAE, NO3RevMAE};

} //END OF METHOD Compute_Prediction 

/*
Function POSITIVE PREDICTION
For each user, predict the rating of the last item he rated, with the help of the ratings of its similar neighbors.
*/

public static double[] Positive_Prediction (
int totalUsers, 
int totalMovies,
List<UserSimilarity>[] userSim,
User[] users,
HashMap<CellCoor,UserMovie>  userMovies,
int bestNeigh)

{

int i, k, l;    
int simNeighbors=0;
List<UserSimilarity> UserList;
double Numerator_Pred, Denominator_Pred;            //Numerator and Denominator of Prediction function.
int predictedValues=0;                              //The total number of actually predicted values
double MAE=0.0;                                     //Mean Absolute Error of Prediction.
int minSimNeigh=0;
CellCoor cell = new CellCoor();                     //UserMovie coordinates
CellCoor cell1 = new CellCoor();

/*
    Scan all users 
*/
for (i=0;i<=totalUsers;i++) 
{

    UserList=userSim[i];
    
    Numerator_Pred=0;Denominator_Pred=0;
    k=users[i].lastMovieId;

/*
    1.For each  user i, whose last rated item is the "k", with at least "minSimNeigh" similar (positive)r neighbors.
    2.Keep the first "bestNeigh" more similar neighbors
    3.For each neightbor that has rated the item "k"
    4.estimate its contribution to the prediction function
    5.if there is no contribution .. move on, else
    6.Estimate prediction (in valid boundaries) 
    7.Estimate MAE
 */    
 
//if (!UserList.isEmpty()) 
    if (UserList.size()>minSimNeigh) //Step 1
    {   
        if (bestNeigh<userSim[i].size())                    //Step 2
            UserList=userSim[i].subList(0, bestNeigh-1);
        
        simNeighbors++;
        
        for (UserSimilarity io: UserList)                   
        {
            cell.user=io.SUser_Id;cell.movie=k;
            if (userMovies.get(cell)!=null)                 //Step 3
            {  
                Denominator_Pred += Math.abs((io.Similarity));  //Step 4
                Numerator_Pred += io.Similarity*(userMovies.get(cell).getRating()-users[io.SUser_Id].UserAverageRate());
                        
            }//if userMovies.get
        }//for UserSimilarity
    }//if serList.size
            
    if (Denominator_Pred==0)                                    //Step 5: Special Condition. When there are no NN that rated LastMovie or
        users[i].setPrediction(Global_Vars.NO_PREDICTION);                      
    else    //Maybe the check "!=NO_PREDICTION" is unnecessary  //Step 6
    {
            users[i].setPrediction((int)Math.round(users[i].UserAverageRate()+Numerator_Pred/Denominator_Pred));  //Normal Condition. When there are NN that rated LastMovie
                    
            if (users[i].getPrediction()>5) users[i].setPrediction(5);
            else
            if ((users[i].getPrediction()<1) && (users[i].getPrediction()!=Global_Vars.NO_PREDICTION))users[i].setPrediction(1);         
            
            cell.user=i;cell.movie=users[i].lastMovieId;        
            MAE += Math.abs(users[i].getPrediction()-userMovies.get(cell).getRating());     //Step 7
            //System.out.println(i+" "+MAE);
            predictedValues++;
    } //else
            
    UserList=new ArrayList<>();
    
} //for i

//Return: Number of users having sim neigh, Number of users having prediction for last item, MAE for the predictions 
return new double[] {simNeighbors, predictedValues, MAE};

} //END OF METHOD Positive_Prediction 



/*  *****
    Combined_Prediction: Combines both Positive and Negative Similarities, to make a prediction.
    ASSUMPTION: All input Lists, are ready for use. They contain just the data they have to.
    *****
*/
    
public static double[] Combined_Prediction (
int totalUsers, 
int totalMovies,
List<UserSimilarity>[] posSim,
List<UserSimilarity>[] negSim,
List<UserSimilarity>[] comSim,
User[] Users,
HashMap<CellCoor,UserMovie>  userMovies,
int bestNeigh)              //Select just the "bestNeigh" (absolute number of most similar heighbors)

{

int i, k, l;    
int combinedNeighbors=0;
List<UserSimilarity>[] combinedSim = new List[Phd_Hub_Hashtable.MAX_USERS];    //Array of list holding for each user the FN
List<UserSimilarity> posList = new ArrayList<>();
List<UserSimilarity> negList = new ArrayList<>();
List<UserSimilarity> combinedList;
double Numerator_Pred, Denominator_Pred;            //Numerator and Denominator of Prediction function.
int predictedValues=0;                              //The total number of actually predicted values
double combinedMAE=0.0;                             //Mean Absolute Error of Prediction.
double sim;                                         //Similarity value of a current record
HashSet<Integer> usersSet = new HashSet<>();        //Set containg the ID of similar users of a specific user
HashSet<Integer> userRatingSet = new HashSet<>();   //Set containg for a specific user the Movies that has rated
Integer curUser;                                    //User under manipulation
Iterator<UserSimilarity> itr;
UserSimilarity tempSim =new UserSimilarity();
int temp=0, pos=0, neg=0;
int minSimNeigh=0;
CellCoor cell = new CellCoor();
CellCoor cell1 = new CellCoor();

for (i=0;i<=totalUsers;i++) 
{

    posList=posSim[i];
    negList=negSim[i];
    combinedList= new ArrayList<>(posList);
    combinedList.addAll(negList);
    
    //Sort array in DESCending order (=maximum similarity first)
    //Collections.sort(combinedList, Collections.reverseOrder());
    combinedList.sort(Comparator.comparingDouble(UserSimilarity::GetCombinedSimilarity).reversed());
    comSim[i]= new ArrayList<>();
    
    //Keep each user ONCE. EITHER ITS POSTTIVE OR ITS NEGATIVE RATING
   //System.out.print(i+" :");
    itr=combinedList.iterator();
    if (combinedList.size()>0) 
    {   
        //temp++; //Δίνει 936
        usersSet.clear();
        /*if (usersSet.isEmpty()) 
            System.out.println(" Set is Empty ");
        else
            System.out.print(" Problem");*/
        while (itr.hasNext())
        {

            tempSim = new UserSimilarity();
            tempSim = itr.next();
            curUser=tempSim.SUser_Id;
            //System.out.print(" "+curUser);
            if (usersSet.contains(curUser)) { //System.out.println(" Remove* "+curUser+" * ");
                itr.remove();}
            else{//System.out.println(" Add* "+curUser+" * ");
                usersSet.add(curUser);}
                        
        }

    }
    comSim[i].addAll(combinedList);

    //System.out.println();            
    
    //if (combinedList.size()>0) temp++; //Δινει 126
    
    Numerator_Pred=0;Denominator_Pred=0;
    k=Users[i].lastMovieId;
            
    //if (!UserList.isEmpty()) 
    if (combinedList.size()>minSimNeigh) 
    {   
        if (bestNeigh<combinedList.size())                      //Select just the "bestNeigh" best neighbors. If total neighbors less than
            combinedList=combinedList.subList(0, bestNeigh-1);  //n=bestNeigh then select them all.

        combinedNeighbors++;                                    
        
        
        for (UserSimilarity io: combinedList)
        {
                   
            cell.user=io.SUser_Id;cell.movie=k;
            if (userMovies.get(cell)!=null)
            {  
                if (io.flag==1) pos++; else neg++;
                Denominator_Pred += io.GetCombinedSimilarity();
                Numerator_Pred += io.GetCombinedSimilarity()*(userMovies.get(cell).getRating()-Users[io.SUser_Id].UserAverageRate());
                        
            }// if userMovies
        }// for UserSimilarity
    }//if combinedList
            
    if (Denominator_Pred==0)                                            //Special Condition. When there are no NN that rated LastMovie or
        Users[i].combinedPrediction=Global_Vars.NO_PREDICTION;           //there are no NNs
    else    //Maybe the check "!=NO_PREDICTION" is unnecessary
    {
        Users[i].combinedPrediction=(int)Math.round(Users[i].UserAverageRate()+Numerator_Pred/Denominator_Pred);  //Normal Condition. When there are NN that rated LastMovie
        
        if (Users[i].combinedPrediction>5) Users[i].combinedPrediction=5;
        else
        if ((Users[i].combinedPrediction<1) && (Users[i].combinedPrediction!=Global_Vars.NO_PREDICTION))Users[i].combinedPrediction=1;                
        
        cell.user=i;cell.movie=Users[i].lastMovieId;
        combinedMAE += Math.abs(Users[i].combinedPrediction-userMovies.get(cell).getRating());
        predictedValues++;
    }
            
    combinedList=new ArrayList<>();
    
} //for i

//System.out.println("pos "+pos+" neg:"+neg+" total: "+combinedNeighbors+" "+predictedValues);

return new double[] {combinedNeighbors, predictedValues, combinedMAE };

} //END OF METHOD Combined_Prediction 

public static double[] Inverted_Prediction (
int totalUsers, 
int totalMovies,
List<UserSimilarity>[] userSim,
User[] Users,
HashMap<CellCoor,UserMovie>  userMovies,
int bestNeigh)

{

int i, k, l;    
int simNeighbors=0;
List<UserSimilarity> UserList;
double Numerator_Pred, Denominator_Pred;            //Numerator and Denominator of Prediction function.
int predictedValues=0;    //The total number of actually predicted values
double MAE=0.0;                         //Mean Absolute Error of Prediction.
int minSimNeigh=0;
CellCoor cell = new CellCoor();
CellCoor cell1 = new CellCoor();

for (i=0;i<=totalUsers;i++) 
{

    UserList=userSim[i];
    
    Numerator_Pred=0;Denominator_Pred=0;
    k=Users[i].lastMovieId;
            
    //if (!UserList.isEmpty()) 
    if (UserList.size()>minSimNeigh) 
    {   
        if (bestNeigh<userSim[i].size())
            UserList=userSim[i].subList(0, bestNeigh-1);
        
        simNeighbors++;
        
        for (UserSimilarity io: UserList)
        {
            
            cell.user=io.SUser_Id;cell.movie=k;
            if (userMovies.get(cell)!=null)
            {  
                Denominator_Pred += Math.abs((io.Similarity));
                Numerator_Pred += io.Similarity*(userMovies.get(cell).getRating()-Users[io.SUser_Id].UserAverageRate());
                        
            }// userMovies
        } // UserSimilarity
    }// UserList
            
    if (Denominator_Pred==0)                                            //Special Condition. When there are no NN that rated LastMovie or
        Users[i].setPrediction(Global_Vars.NO_PREDICTION);                      
    else    //Maybe the check "!=NO_PREDICTION" is unnecessary
    {
            Users[i].setPrediction((int)Math.round(Users[i].UserAverageRate()+Numerator_Pred/Denominator_Pred));  //Normal Condition. When there are NN that rated LastMovie
                    
            if (Users[i].getPrediction()>5) Users[i].setPrediction(5);
            else
            if ((Users[i].getPrediction()<1) && (Users[i].getPrediction()!=Global_Vars.NO_PREDICTION))Users[i].setPrediction(1);                
             
            cell.user=i;cell.movie=Users[i].lastMovieId;
            MAE += Math.abs(Users[i].getPrediction()-userMovies.get(cell).getRating());
            //System.out.println(i+" "+MAE);
            predictedValues++;
    }
            
    UserList=new ArrayList<>();
    
}    
return new double[] {simNeighbors, predictedValues, MAE};

} //END OF METHOD Positive_Prediction 

}//class
