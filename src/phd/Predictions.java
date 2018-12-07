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
UserMovie[][] userMovies,
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
                    
            if (userMovies[io.SUser_Id][k]!=null)
            {  
                Denominator_Pred += Math.abs((io.Similarity));
                Numerator_Pred += io.Similarity*(userMovies[io.SUser_Id][k].getRating()-Users[io.SUser_Id].UserAverageRate());
                        
            }
        }
    }
            
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
                    
            MAE += Math.abs(Users[i].getPrediction()-userMovies[i][Users[i].lastMovieId].getRating());
            predictedValues++;
        }
        else 
            if (predictionSign==1)
            {
                Users[i].setRevPrediction((int)Math.round(Users[i].UserAverageRate()+Numerator_Pred/Denominator_Pred));  //Normal Condition. When there are FN that rated LastMovie
                   
                if (Users[i].getRevPrediction()>5) Users[i].setRevPrediction(5);
                else
                if ((Users[i].getRevPrediction()<1) && (Users[i].getRevPrediction()!=Global_Vars.NO_PREDICTION)) Users[i].setRevPrediction(1);          
                    
                RevMAE += Math.abs(Users[i].getRevPrediction()-userMovies[i][k].getRating());
                revPredictedValues++;
            }
            else
            {
                Users[i].setNO3RevPrediction((int)Math.round(Users[i].UserAverageRate()+Numerator_Pred/Denominator_Pred));  //Normal Condition. When there are FN that rated LastMovie                

                if (Users[i].getNO3RevPrediction()>5) Users[i].setNO3RevPrediction(5);
                else
                if ((Users[i].getNO3RevPrediction()<1) && (Users[i].getNO3RevPrediction()!=Global_Vars.NO_PREDICTION)) Users[i].setNO3RevPrediction(1);   

                NO3RevMAE += Math.abs(Users[i].getNO3RevPrediction()-userMovies[i][k].getRating());
                NO3RevPredictedValues++;     
            }
            
    UserList=new ArrayList<>();
    
}    
return new double[] {simNeighbors, revSimNeighbors, NO3RevSimNeighbors, predictedValues, revPredictedValues, NO3RevPredictedValues, MAE, RevMAE, NO3RevMAE};

} //END OF METHOD Compute_Prediction 

public static double[] Positive_Prediction (
int totalUsers, 
int totalMovies,
List<UserSimilarity>[] userSim,
User[] users,
UserMovie[][] userMovies,
int bestNeigh)

{

int i, k, l;    
int simNeighbors=0;
List<UserSimilarity> UserList = new ArrayList<>();
double Numerator_Pred, Denominator_Pred;            //Numerator and Denominator of Prediction function.
int predictedValues=00;    //The total number of actually predicted values
double MAE=0.0;                         //Mean Absolute Error of Prediction.
int minSimNeigh=0;

for (i=0;i<=totalUsers;i++) 
{

    UserList=userSim[i];
    
    Numerator_Pred=0;Denominator_Pred=0;
    k=users[i].lastMovieId;
            
    //if (!UserList.isEmpty()) 
    if (UserList.size()>minSimNeigh) 
    {   
        if (bestNeigh<userSim[i].size())
            UserList=userSim[i].subList(0, bestNeigh-1);
        
        simNeighbors++;
        
        for (UserSimilarity io: UserList)
        {
                    
            if (userMovies[io.SUser_Id][k]!=null)
            {  
                Denominator_Pred += Math.abs((io.Similarity));
                Numerator_Pred += io.Similarity*(userMovies[io.SUser_Id][k].getRating()-users[io.SUser_Id].UserAverageRate());
                        
            }
        }
    }
            
    if (Denominator_Pred==0)                                            //Special Condition. When there are no NN that rated LastMovie or
        users[i].setPrediction(Global_Vars.NO_PREDICTION);                      
    else    //Maybe the check "!=NO_PREDICTION" is unnecessary
    {
            users[i].setPrediction((int)Math.round(users[i].UserAverageRate()+Numerator_Pred/Denominator_Pred));  //Normal Condition. When there are NN that rated LastMovie
                    
            if (users[i].getPrediction()>5) users[i].setPrediction(5);
            else
            if ((users[i].getPrediction()<1) && (users[i].getPrediction()!=Global_Vars.NO_PREDICTION))users[i].setPrediction(1);                
                    
            MAE += Math.abs(users[i].getPrediction()-userMovies[i][users[i].lastMovieId].getRating());
            predictedValues++;
    }
            
    UserList=new ArrayList<>();
    
}    
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
UserMovie[][] userMovies,
int bestNeigh)              //Select just the "bestNeigh" (absolute number of most similar heighbors)

{

int i, k, l;    
int combinedNeighbors=0;
List<UserSimilarity>[] combinedSim = new List[FN_100K_OLD.MAX_USERS];    //Array of list holding for each user the FN
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
                    
            if (userMovies[io.SUser_Id][k]!=null)
            {  
                if (io.flag==1) pos++; else neg++;
                Denominator_Pred += io.GetCombinedSimilarity();
                Numerator_Pred += io.GetCombinedSimilarity()*(userMovies[io.SUser_Id][k].getRating()-Users[io.SUser_Id].UserAverageRate());
                        
            }
        }
    }
            
    if (Denominator_Pred==0)                                            //Special Condition. When there are no NN that rated LastMovie or
        Users[i].combinedPrediction=Global_Vars.NO_PREDICTION;           //there are no NNs
    else    //Maybe the check "!=NO_PREDICTION" is unnecessary
    {
        Users[i].combinedPrediction=(int)Math.round(Users[i].UserAverageRate()+Numerator_Pred/Denominator_Pred);  //Normal Condition. When there are NN that rated LastMovie
                    
        if (Users[i].combinedPrediction>5) Users[i].combinedPrediction=5;
        else
        if ((Users[i].combinedPrediction<1) && (Users[i].combinedPrediction!=Global_Vars.NO_PREDICTION))Users[i].combinedPrediction=1;                
                    
        combinedMAE += Math.abs(Users[i].combinedPrediction-userMovies[i][Users[i].lastMovieId].getRating());
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
UserMovie[][] userMovies,
int bestNeigh)

{

int i, k, l;    
int simNeighbors=0;
List<UserSimilarity> UserList = new ArrayList<>();
double Numerator_Pred, Denominator_Pred;            //Numerator and Denominator of Prediction function.
int predictedValues=00;    //The total number of actually predicted values
double MAE=0.0;                         //Mean Absolute Error of Prediction.
int minSimNeigh=0;

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
                    
            if (userMovies[io.SUser_Id][k]!=null)
            {  
                Denominator_Pred += Math.abs((io.Similarity));
                Numerator_Pred += io.Similarity*(userMovies[io.SUser_Id][k].getRating()-Users[io.SUser_Id].UserAverageRate());
                        
            }
        }
    }
            
    if (Denominator_Pred==0)                                            //Special Condition. When there are no NN that rated LastMovie or
        Users[i].setPrediction(Global_Vars.NO_PREDICTION);                      
    else    //Maybe the check "!=NO_PREDICTION" is unnecessary
    {
            Users[i].setPrediction((int)Math.round(Users[i].UserAverageRate()+Numerator_Pred/Denominator_Pred));  //Normal Condition. When there are NN that rated LastMovie
                    
            if (Users[i].getPrediction()>5) Users[i].setPrediction(5);
            else
            if ((Users[i].getPrediction()<1) && (Users[i].getPrediction()!=Global_Vars.NO_PREDICTION))Users[i].setPrediction(1);                
                    
            MAE += Math.abs(Users[i].getPrediction()-userMovies[i][Users[i].lastMovieId].getRating());
            predictedValues++;
    }
            
    UserList=new ArrayList<>();
    
}    
return new double[] {simNeighbors, predictedValues, MAE};

} //END OF METHOD Positive_Prediction 

}//class
