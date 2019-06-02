/**
 *
 * @author Dennis Vassilopoulos
 * Creation Date: 23/1/2018
 * 
 * This class defines all mathods used in computing all kind of similarities, 
 * Initially only one composite method was implemented, but during the implementation of
 * the project, rose the need to split the original method into several, district, and more
 * managable methods.
 * 
 * Ver 2.0
 * From Ver 2.0 most arrays are converted to List or Set data structure. 
 * When the main array userMovies (or more accurately user-item pairs array) transformed to HashTable in order
 * to accommodate memory restrictions, the speed of similarity testing has slowed down.
 *
 * Ver 2.1 
 * 1.Removed all setter/getter functions
 * 2.Removed all code left from previous versions (those in comments)
 * Some observations about speed:
 * 1.HashTable.all and HashTable.retainAll methods are VERY SLOW. So merging of two sets is a crucial factor of the program execution speed.
 * Writing my own mergeSet function improved the speed about 40%, but it is still much slower (400%) than the original version (up to 1.4) where
 * typical array structures were used.
 * 2.NOT using setter/getter functions does not improve perfomance.
 * 2.Creating HasTable of zero initial size slows down speed by a factor of 10%.
 * 3.Creating HasTable of double, than needed, size does not improve performance.
 * 4.Reducing HashTable.get() calls, improves by an additional 25%
 */

package phd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
//import static phd.FN_Amazon_Video_Games.usersRatingSet;

public class Similarities {

/*  *****
    Print Similarities: For every user, prints both its similar users and the similarity score.
    Handles normal, reverse and NO3 similarities.
    *****
*/
public static void Print_Similarities (int totalUsers, List<UserSimilarity>[] userSim){

int  i;
List<UserSimilarity> UserList = new ArrayList<>();

System.out.println("Print Similarities:"+totalUsers+"\n");

for (i=0;i<=totalUsers;i++) 
{
    UserList=userSim[i];
    //System.out.println(UserList.size());
    if (UserList.size()>0) 
    {   
        //if (k>UserList.size()) k=UserList.size();
        for (UserSimilarity io: UserList)
        {
            System.out.println(io.FUser_Id+" "+io.SUser_Id+" "+io.Similarity+"<-->");
        } //for io
        System.out.println();
    }//if 
                  
}//for i

}//END Print_Similarities

public static HashSet<Integer> mergeSet(
HashSet<Integer> set1, 
HashSet<Integer> set2)
{

HashSet<Integer> mergeSet = new HashSet();

set2.stream().filter((k) -> (set1.contains(k))).forEach((k) -> {
    mergeSet.add(k);
    });    

/* 
    Slightly slower from the above line 

for (int k: set1) {
    if(set2.contains(k)) {
        mergeSet.add(k);
    }

}
*/

return mergeSet;

}



/**
 * 
 * Compute_Positive-Similarity: Method to compute ONLY POSITIVE similarities among all neighbors. 
 * Accepts as imput the following variables
 * 
 * @param totalUsers
 * @param totalMovies
 * @param userSim
 * @param Users
 * @param userMovies
 * @param usersRatingSet
 
 */

public static void Positive_Similarity (
int totalUsers, 
int totalMovies,
List<UserSimilarity>[] userSim,
User[] users,
HashMap<CellCoor,UserMovie>  userMovies,
HashSet<Integer>[] usersRatingSet,
double simBase,
int commonMovies
//int absMinTimeStamp,
//int absMaxTimeStamp
)
    
{
        
int    i, j;
double tempWeight;
int    tempMovies;
double averageUI, averageUJ;                    //Hold average rating of user i and j, respectively
double numeratorSimij, denominatorSimij;        //Numerator and Denominator of Similarity (Pearson) function.
double denominatorPartA, denominatorPartB;      //Denominator consists of two parts        
double Similarity;
double maxSimValue=Integer.MIN_VALUE, MinSimValue=Integer.MAX_VALUE;
HashSet<Integer> commonRatingSet;
CellCoor cell0 = new CellCoor();
CellCoor cell1 = new CellCoor();
UserMovie tempUM1;
UserMovie tempUM2;

//System.out.println("Similarity"+simBase);

//Phd_Utils.Print_Ratings(totalUsers, totalMovies, users, userMovies);
for (i=0;i<=totalUsers;i++) 
    userSim[i]=new ArrayList<>(); 

for (i=0;i<=totalUsers-1;i++)
{    
            
    averageUI=users[i].UserAverageRate();   
    //System.out.println(i+" "+averageUI);
    //System.out.println(i+" "+users[i].MaxTimeStamp+" "+users[i].MinTimeStamp+" "+Global_Vars.MIN_TIMESPACE);
    //System.out.println(i+" "+users[i].getMaxTimeStamp()+" "+users[i].getMinTimeStamp()+" "+Global_Vars.MIN_TIMESPACE);
    
    if ((users[i].getMaxTimeStamp()-users[i].getMinTimeStamp())>Global_Vars.MIN_TIMESPACE)
       for (j=i+1;j<=totalUsers;j++)
       {
           
            numeratorSimij=0.0;                    //Initializing variables used in computing similarity
            denominatorPartA=0.0;denominatorPartB=0.0;

            averageUJ=users[j].UserAverageRate();
            tempMovies=0;
                
            //for (k=0;k<=totalMovies;k++)
            //commonRatingSet.addAll(usersRatingSet[i]);
            //commonRatingSet.retainAll(usersRatingSet[j]);
            
            //System.out.print("\nUser:"+i+" "+j+" Items:");
            commonRatingSet=mergeSet(usersRatingSet[i],usersRatingSet[j]);
            
            for (int k: commonRatingSet)
            //for (int k: usersRatingSet[i])
            {
                //System.out.print(" "+k);
                cell0.user=i;cell0.movie=k;
                tempUM1=userMovies.get(cell0);
                cell1.user=j;cell1.movie=k;
                //System.out.println("Cells:"+cell0.user+" "+cell0.movie+" "+i+" "+j+" "+k);
                //if (userMovies.containsKey(cell0) && userMovies.containsKey(cell1))
                if (userMovies.containsKey(cell1))
                {
                    //System.out.println("aek1");

                    tempUM2=userMovies.get(cell1);
                    tempMovies++;
                    if (Global_Vars.WEIGHT_TYPE==1)
                    {
                        /*tempWeight=(double)(userMovies.get(cell0).Time_Stamp-users[i].getMinTimeStamp())/(double)(users[i].getMaxTimeStamp()-users[i].getMinTimeStamp());
                        userMovies.get(cell0).setWeight(tempWeight);   
                        tempWeight=(double)(userMovies.get(cell1).Time_Stamp-users[j].getMinTimeStamp())/(double)(users[j].getMaxTimeStamp()-users[j].getMinTimeStamp());
                        userMovies.get(cell1).setWeight(tempWeight); */
                        
                        /*tempWeight=(double)(userMovies.get(cell0).Time_Stamp-users[i].MinTimeStamp)/(double)(users[i].MaxTimeStamp-users[i].MinTimeStamp);
                        userMovies.get(cell0).Weight=tempWeight;   */

                        tempWeight=(double)(tempUM1.Time_Stamp-users[i].MinTimeStamp)/(double)(users[i].MaxTimeStamp-users[i].MinTimeStamp);
                        tempUM1.Weight=tempWeight; 
                        
                        //-System.out.print(i+" "+k+" "+tempWeight+"<->");
                        /*tempWeight=(double)(userMovies.get(cell1).Time_Stamp-users[j].MinTimeStamp)/(double)(users[j].MaxTimeStamp-users[j].MinTimeStamp);
                        userMovies.get(cell1).Weight=tempWeight;*/
                        
                        tempWeight=(double)(tempUM2.Time_Stamp-users[j].MinTimeStamp)/(double)(users[j].MaxTimeStamp-users[j].MinTimeStamp);
                        tempUM2.Weight=tempWeight;
                        
                        //-System.out.println(j+" "+k+" "+tempWeight+"<->");
                        //System.out.println(userMovies.get(cell0).Time_Stamp+" "+users[i].getMinTimeStamp()+" "+users[i].getMaxTimeStamp());
                        //System.out.println(userMovies.get(cell1).Time_Stamp+" "+users[j].getMinTimeStamp()+" "+users[j].getMaxTimeStamp());
                    }
                    else
                    {
                        /*userMovies.get(cell0).setWeight(1);   
                        userMovies.get(cell1).setWeight(1);   */
                        tempUM1.Weight=1;   
                        tempUM2.Weight=1;
                    }
                    
                    
                    /*numeratorSimij += (userMovies.get(cell0).getRating()-averageUI)*(userMovies.get(cell1).getRating()-averageUJ)*userMovies.get(cell0).getWeight()*userMovies.get(cell1).getWeight();
                    denominatorPartA += (userMovies.get(cell0).getRating()-averageUI)*(userMovies.get(cell0).getRating()-averageUI);
                    denominatorPartB += (userMovies.get(cell1).getRating()-averageUJ)*(userMovies.get(cell1).getRating()-averageUJ);*/
                    
                    /*numeratorSimij += (userMovies.get(cell0).Rating-averageUI)*(userMovies.get(cell1).Rating-averageUJ)*userMovies.get(cell0).Weight*userMovies.get(cell1).Weight;
                    denominatorPartA += (userMovies.get(cell0).Rating-averageUI)*(userMovies.get(cell0).Rating-averageUI);
                    denominatorPartB += (userMovies.get(cell1).Rating-averageUJ)*(userMovies.get(cell1).Rating-averageUJ);*/

                    numeratorSimij += (tempUM1.Rating-averageUI)*(tempUM2.Rating-averageUJ)*tempUM1.Weight*tempUM2.Weight;
                    
                    denominatorPartA += (tempUM1.Rating-averageUI)*(tempUM1.Rating-averageUI);
                    denominatorPartB += (tempUM2.Rating-averageUJ)*(tempUM2.Rating-averageUJ);

                }//if
                  
            }//for k
            
            commonRatingSet.clear();
            
            denominatorSimij= denominatorPartA * denominatorPartB;
            Similarity=(double)(numeratorSimij/Math.sqrt(denominatorSimij));
            
            //System.out.println("Similarity:"+Similarity);
            //find min/max similarity values
            if (MinSimValue>Similarity) MinSimValue=Similarity;
            else
            if (maxSimValue<Similarity) maxSimValue=Similarity;
                
            
            //At least "commonMovies" common ratings
            if (tempMovies>commonMovies) {
            
                if (Similarity>=simBase)    //Only Positive Similarities are Considered
                {
                        //if (i==0) System.out.println(Similarity+" j "+j+" i "+i);
                        userSim[i].add(new UserSimilarity(i,j,Similarity,1));
                        userSim[j].add(new UserSimilarity(j,i,Similarity,1));

                } 
            }    
            
        }//for

}//for i
//System.out.println("max:"+absMaxTimeStamp+" min:"+absMinTimeStamp);//System.out.println("max:"+absMaxTimeStamp+" min:"+absMinTimeStamp);
} //END OF METHOD Positive_Similarity

public static void Positive_Similarity_Parallel (
int low,
int upper,        
int totalUsers, 
List<UserSimilarity>[] userSim,
User[] users,
HashMap<CellCoor,UserMovie>  userMovies,
HashSet<Integer>[] usersRatingSet,
double simBase,
int commonMovies

)
    
{
        
int    i, j;
double tempWeight;
int    tempMovies;
double averageUI, averageUJ;                    //Hold average rating of user i and j, respectively
double numeratorSimij, denominatorSimij;        //Numerator and Denominator of Similarity (Pearson) function.
double denominatorPartA, denominatorPartB;      //Denominator consists of two parts        
double Similarity;
double maxSimValue=Integer.MIN_VALUE, MinSimValue=Integer.MAX_VALUE;
HashSet<Integer> commonRatingSet;
CellCoor cell0 = new CellCoor();
CellCoor cell1 = new CellCoor();
UserMovie tempUM1;
UserMovie tempUM2;

//System.out.println("Similarity"+simBase);

//Phd_Utils.Print_Ratings(totalUsers, totalMovies, users, userMovies);
//for (i=0;i<=totalUsers;i++) 
//;           userSim[i]=new ArrayList<>(); //Collections.synchronozedList(new ArrayList<>());

for (i=low;i<=upper;i++)
{    
            
    averageUI=users[i].UserAverageRate();   
    //System.out.println(i+" "+averageUI);
    //System.out.println(i+" "+users[i].MaxTimeStamp+" "+users[i].MinTimeStamp+" "+Global_Vars.MIN_TIMESPACE);
    //System.out.println(i+" "+users[i].getMaxTimeStamp()+" "+users[i].getMinTimeStamp()+" "+Global_Vars.MIN_TIMESPACE);
    
    if ((users[i].MaxTimeStamp-users[i].MinTimeStamp)>Global_Vars.MIN_TIMESPACE)
       for (j=i+1;j<=totalUsers;j++)
       {
           
            numeratorSimij=0.0;                    //Initializing variables used in computing similarity
            denominatorPartA=0.0;denominatorPartB=0.0;

            averageUJ=users[j].UserAverageRate();
            tempMovies=0;
                
            //for (k=0;k<=totalMovies;k++)
            //commonRatingSet.addAll(usersRatingSet[i]);
            //commonRatingSet.retainAll(usersRatingSet[j]);
            
            //System.out.print("\nUser:"+i+" "+j+" Items:");
            commonRatingSet=mergeSet(usersRatingSet[i],usersRatingSet[j]);
            
            for (int k: commonRatingSet)
            //for (int k: usersRatingSet[i])
            {
                //System.out.print(" "+k);
                cell0.user=i;cell0.movie=k;
                tempUM1=userMovies.get(cell0);
                cell1.user=j;cell1.movie=k;
                //System.out.println("Cells:"+cell0.user+" "+cell0.movie+" "+i+" "+j+" "+k);
                //if (userMovies.containsKey(cell0) && userMovies.containsKey(cell1))
                //if (userMovies.containsKey(cell1))
                tempUM2=userMovies.get(cell1);
                if (!(tempUM2==null))
                {
                    //System.out.println("aek1");

                    //tempUM2=userMovies.get(cell1); 
                    tempMovies++;
                    if (Global_Vars.WEIGHT_TYPE==1)
                    {

                        tempWeight=(double)(tempUM1.Time_Stamp-users[i].MinTimeStamp)/(double)(users[i].MaxTimeStamp-users[i].MinTimeStamp);
                        tempUM1.Weight=tempWeight; 
                        
                        //-System.out.print(i+" "+k+" "+tempWeight+"<->");
                        
                        tempWeight=(double)(tempUM2.Time_Stamp-users[j].MinTimeStamp)/(double)(users[j].MaxTimeStamp-users[j].MinTimeStamp);
                        tempUM2.Weight=tempWeight;
                        
                        //-System.out.println(j+" "+k+" "+tempWeight+"<->");
                        //System.out.println(userMovies.get(cell0).Time_Stamp+" "+users[i].getMinTimeStamp()+" "+users[i].getMaxTimeStamp());
                        //System.out.println(userMovies.get(cell1).Time_Stamp+" "+users[j].getMinTimeStamp()+" "+users[j].getMaxTimeStamp());
                    }
                    else
                    {
                        /*userMovies.get(cell0).setWeight(1);   
                        userMovies.get(cell1).setWeight(1);   */
                        tempUM1.Weight=1;   
                        tempUM2.Weight=1;
                    }
                    
                    numeratorSimij += (tempUM1.Rating-averageUI)*(tempUM2.Rating-averageUJ)*tempUM1.Weight*tempUM2.Weight;
                    
                    denominatorPartA += (tempUM1.Rating-averageUI)*(tempUM1.Rating-averageUI);
                    denominatorPartB += (tempUM2.Rating-averageUJ)*(tempUM2.Rating-averageUJ);

                }//if
                  
            }//for k
            
            commonRatingSet.clear();
            
            denominatorSimij= denominatorPartA * denominatorPartB;
            Similarity=(double)(numeratorSimij/Math.sqrt(denominatorSimij));
            
            //System.out.println("Similarity:"+Similarity);
            //find min/max similarity values
            if (MinSimValue>Similarity) MinSimValue=Similarity;
            else
            if (maxSimValue<Similarity) maxSimValue=Similarity;
                
            
            //At least "commonMovies" common ratings
            if (tempMovies>commonMovies) {
            
                if (Similarity>=simBase)    //Only Positive Similarities are Considered
                {
                        //System.out.println(Similarity+"Den");
                        userSim[i].add(new UserSimilarity(i,j,Similarity,1));
                        userSim[j].add(new UserSimilarity(j,i,Similarity,1));

                } 
            }    
            
        }//for j//for j

}//for i
//System.out.println("max:"+absMaxTimeStamp+" min:"+absMinTimeStamp);//System.out.println("max:"+absMaxTimeStamp+" min:"+absMinTimeStamp);
} //END OF METHOD Positive_Similarity_Parallel



/**
 * 
 * Inverted_Similarity: 
 * 
 * @param totalUsers
 * @param totalMovies
 * @param userSim
 * @param similaritySign 
 */

public static void Inverted_Similarity (
int totalUsers, 
int totalMovies,
List<UserSimilarity>[] userSim,
User[] users,
HashMap<CellCoor,UserMovie>  userMovies,
HashSet<Integer>[] usersRatingSet,
double simBase,
int commonMovies)
    
{
        
int    i, j;
double tempWeight;
int    tempMovies;
double averageUI, averageUJ;                    //Hold average rating of user i and j, respectively
double numeratorSimij, denominatorSimij;        //Numerator and Denominator of Similarity (Pearson) function.
double denominatorPartA, denominatorPartB;      //Denominator consists of two parts        
double Similarity;
double maxSimValue=Integer.MIN_VALUE, MinSimValue=Integer.MAX_VALUE;
HashSet<Integer> commonRatingSet = new HashSet<>();   //Set containg for a specific user the Movies that has rated
CellCoor cell0 = new CellCoor();
CellCoor cell1 = new CellCoor();
UserMovie tempUM1;
UserMovie tempUM2;

//System.out.println("Similarity"+simBase);
for (i=0;i<=totalUsers;i++) 
    userSim[i]=new ArrayList<>();

for (i=0;i<=totalUsers-1;i++)
{    
            
    averageUI=users[i].UserInvertedAverageRating();            
    if ((users[i].MaxTimeStamp-users[i].MinTimeStamp)>Global_Vars.MIN_TIMESPACE)
       for (j=i+1;j<=totalUsers;j++)
       {
           
            numeratorSimij=0.0;                    //Initializing variables used in computing similarity
            denominatorPartA=0.0;denominatorPartB=0.0;

            averageUJ=users[j].UserInvertedAverageRating();
            tempMovies=0;
                
            //for (k=0;k<=totalMovies;k++)
            //System.out.print("\nUser:"+i+" "+j+" Items:");
            commonRatingSet=mergeSet(usersRatingSet[i],usersRatingSet[j]);

            for (int k: commonRatingSet)            
            //for (int k: userRatingSet)
            {
                cell0.user=i;cell0.movie=k;
                tempUM1=userMovies.get(cell0);                
                cell1.user=j;cell1.movie=k;

                
                //if (!(userMovies.get(cell0)==null) && !(userMovies.get(cell1)==null))
                if (!(userMovies.get(cell1)==null))
                {
                    
                    tempUM2=userMovies.get(cell1);                    
                    tempMovies++;
                    
                    if (Global_Vars.WEIGHT_TYPE==1)
                    {
                        tempWeight=(double)(tempUM1.Time_Stamp-users[i].MinTimeStamp)/(double)(users[i].MaxTimeStamp-users[i].MinTimeStamp);
                        tempUM1.setWeight(tempWeight);   
                        //System.out.print(j+" "+k+" "+tempWeight+"<->");
                        tempWeight=(double)(tempUM2.Time_Stamp-users[j].MinTimeStamp)/(double)(users[j].MaxTimeStamp-users[j].MinTimeStamp);
                        tempUM2.setWeight(tempWeight);   
                        //System.out.print(j+" "+k+" "+tempWeight+"<->");
                        //System.out.println(tempUM1.Time_Stamp+" "+users[i].getMinTimeStamp()+" "+users[i].getMaxTimeStamp());
                        //System.out.println(tempUM2.Time_Stamp+" "+users[j].getMinTimeStamp()+" "+users[j].getMaxTimeStamp());                        
                    }
                    else
                    {
                        tempUM1.setWeight(1);   
                        tempUM2.setWeight(1);   
                    }
                    
                    
                    numeratorSimij += (userMovies.get(cell0).invRating-averageUI)*(userMovies.get(cell1).invRating-averageUJ)*userMovies.get(cell0).getWeight()*userMovies.get(cell1).getWeight();
                    denominatorPartA += (userMovies.get(cell0).invRating-averageUI)*(userMovies.get(cell0).invRating-averageUI);
                    denominatorPartB += (userMovies.get(cell1).invRating-averageUJ)*(userMovies.get(cell1).invRating-averageUJ);

                }
                  
            }//for k
                
            denominatorSimij= denominatorPartA * denominatorPartB;
            Similarity=(double)(numeratorSimij/Math.sqrt(denominatorSimij));
                
            //At least "commonMovies" common ratings
            if (tempMovies>commonMovies)
            
                if (Similarity>=simBase)    //Only Positive Similarities are Considered
                {
                        //System.out.println(Similarity+"Eff");
                        userSim[i].add(new UserSimilarity(i,j,Similarity));
                        userSim[j].add(new UserSimilarity(j,i,Similarity));

                } 
                
        }//for i

}

} //END OF METHOD Inverted_Similarity

/**
 * 
 * Inverted_Similarity: 
 * 
 * @param totalUsers
 * @param totalMovies
 * @param userSim
 * @param similaritySign 
 */

public static void Inverted_Similarity_Parallel (
        
int low,
int upper,        
int totalUsers, 
List<UserSimilarity>[] userSim,
User[] users,
HashMap<CellCoor,UserMovie>  userMovies,
HashSet<Integer>[] usersRatingSet,
double simBase,
int commonMovies)

{
        
int    i, j;
double tempWeight;
int    tempMovies;
double averageUI, averageUJ;                    //Hold average rating of user i and j, respectively
double numeratorSimij, denominatorSimij;        //Numerator and Denominator of Similarity (Pearson) function.
double denominatorPartA, denominatorPartB;      //Denominator consists of two parts        
double Similarity;
double maxSimValue=Integer.MIN_VALUE, MinSimValue=Integer.MAX_VALUE;
HashSet<Integer> commonRatingSet = new HashSet<>();   //Set containg for a specific user the Movies that has rated
CellCoor cell0 = new CellCoor();
CellCoor cell1 = new CellCoor();
UserMovie tempUM1;
UserMovie tempUM2;

//System.out.println("Similarity"+simBase);
//for (i=0;i<=totalUsers;i++)                   //NOT NEEDED at Parallel Version
//    userSim[i]=new ArrayList<>();

for (i=low;i<=upper;i++)
{    
            
    averageUI=users[i].UserInvertedAverageRating();            
    if ((users[i].getMaxTimeStamp()-users[i].getMinTimeStamp())>Global_Vars.MIN_TIMESPACE)
       for (j=i+1;j<=totalUsers;j++)
       {
           
            numeratorSimij=0.0;                    //Initializing variables used in computing similarity
            denominatorPartA=0.0;denominatorPartB=0.0;

            averageUJ=users[j].UserInvertedAverageRating();
            tempMovies=0;
                
            //for (k=0;k<=totalMovies;k++)
            //System.out.print("\nUser:"+i+" "+j+" Items:");
            commonRatingSet=mergeSet(usersRatingSet[i],usersRatingSet[j]);

            for (int k: commonRatingSet)            
            //for (int k: userRatingSet)
            {
                cell0.user=i;cell0.movie=k;
                tempUM1=userMovies.get(cell0);                
                cell1.user=j;cell1.movie=k;

                
                //if (!(userMovies.get(cell0)==null) && !(userMovies.get(cell1)==null))
                if (!(userMovies.get(cell1)==null))
                {
                    
                    tempUM2=userMovies.get(cell1);                    
                    tempMovies++;
                    
                    if (Global_Vars.WEIGHT_TYPE==1)
                    {
                        tempWeight=(double)(tempUM1.Time_Stamp-users[i].getMinTimeStamp())/(double)(users[i].getMaxTimeStamp()-users[i].getMinTimeStamp());
                        tempUM1.setWeight(tempWeight);   
                        //System.out.print(j+" "+k+" "+tempWeight+"<->");
                        tempWeight=(double)(tempUM2.Time_Stamp-users[j].getMinTimeStamp())/(double)(users[j].getMaxTimeStamp()-users[j].getMinTimeStamp());
                        tempUM2.setWeight(tempWeight);   
                        //System.out.print(j+" "+k+" "+tempWeight+"<->");
                        //System.out.println(tempUM1.Time_Stamp+" "+users[i].getMinTimeStamp()+" "+users[i].getMaxTimeStamp());
                        //System.out.println(tempUM2.Time_Stamp+" "+users[j].getMinTimeStamp()+" "+users[j].getMaxTimeStamp());                        
                    }
                    else
                    {
                        tempUM1.setWeight(1);   
                        tempUM2.setWeight(1);   
                    }
                    
                    
                    numeratorSimij += (userMovies.get(cell0).invRating-averageUI)*(userMovies.get(cell1).invRating-averageUJ)*userMovies.get(cell0).getWeight()*userMovies.get(cell1).getWeight();
                    denominatorPartA += (userMovies.get(cell0).invRating-averageUI)*(userMovies.get(cell0).invRating-averageUI);
                    denominatorPartB += (userMovies.get(cell1).invRating-averageUJ)*(userMovies.get(cell1).invRating-averageUJ);

                }
                  
            }//for k
                
            denominatorSimij= denominatorPartA * denominatorPartB;
            Similarity=(double)(numeratorSimij/Math.sqrt(denominatorSimij));
                
            //At least "commonMovies" common ratings
            if (tempMovies>commonMovies)
            
                if (Similarity>=simBase)    //Only Positive Similarities are Considered
                {
                        //System.out.println(Similarity+"Eff");
                        userSim[i].add(new UserSimilarity(i,j,Similarity));
                        userSim[j].add(new UserSimilarity(j,i,Similarity));

                } 
                
        }//for i

}

} //END OF METHOD Inverted_Similarity_Parallel

/**
 * 
 * Compute_Similarity: Method to compute similarities among all neighbors. Accepts as imput the
 * following variables
 * 
 * @param totalUsers
 * @param totalMovies
 * @param userSim
 * @param similaritySign 
 */

public static void Compute_Similarity (
int totalUsers, 
int totalMovies,
List<UserSimilarity>[] userSim,
User[] users,
HashMap<CellCoor,UserMovie>  userMovies,
HashSet<Integer>[] usersRatingSet,
int similaritySign,
double simBase,
int commonMovies)
    
{
        
int    i, j;
double tempWeight, negWeight;
int    tempMovies, temp_no3movies;
double averageUI, averageUJ;                    //Hold average rating of user i and j, respectively
double numeratorSimij, negNumeratorSimij, denominatorSimij;        //Numerator and Denominator of Similarity (Pearson) function.
double denominatorPartA, denominatorPartB;      //Denominator consists of two parts        
double Similarity, negSimilarity;
double NO3_numeratorSimij, NO3_denominatorSimij;
double NO3_denominatorPartA, NO3_denominatorPartB;
double NO3_similarity;
double maxSimValue=Integer.MIN_VALUE, MinSimValue=Integer.MAX_VALUE;
HashSet<Integer> commonRatingSet = new HashSet<>();   //Set containg for a specific user the Movies that has rated
CellCoor cell0 = new CellCoor();
CellCoor cell1 = new CellCoor();
UserMovie tempUM1;
UserMovie tempUM2;

//System.out.println("Similarity"+simBase);

for (i=0;i<=totalUsers;i++) 
    userSim[i]=new ArrayList<>();

for (i=0;i<=totalUsers-1;i++)
{    
            
    averageUI=users[i].UserAverageRate();     
    //System.out.println(i+" "+averageUI);
    //System.out.println(i+" "+users[i].MaxTimeStamp+" "+users[i].MinTimeStamp+" "+Global_Vars.MIN_TIMESPACE);
    //System.out.println(i+" "+users[i].getMaxTimeStamp()+" "+users[i].getMinTimeStamp()+" "+Global_Vars.MIN_TIMESPACE);       

    if ((users[i].getMaxTimeStamp()-users[i].getMinTimeStamp())>Phd_Hub_Hashtable.MIN_TIMESPACE)
       for (j=i+1;j<=totalUsers;j++)
       {

            numeratorSimij=0.0;negNumeratorSimij=0.0;denominatorSimij=0.0;                    //Initializing variables used in computing similarity
            denominatorPartA=0.0;denominatorPartB=0.0;

            NO3_numeratorSimij=0;NO3_denominatorSimij=0;
            NO3_denominatorPartA=0; NO3_denominatorPartB=0;

            averageUJ=users[j].UserAverageRate();
            tempMovies=0;temp_no3movies=0;
                
            //for (k=0;k<=totalMovies;k++)
            //System.out.print("\nUser:"+i+" "+j+" Items:");
            commonRatingSet=mergeSet(usersRatingSet[i],usersRatingSet[j]);
            
            for (int k: commonRatingSet)
            //for (int k: userRatingSet)            
            {
                cell0.user=i;cell0.movie=k;
                tempUM1=userMovies.get(cell0);
                cell1.user=j;cell1.movie=k;

                //if (!(userMovies.get(cell0)==null) && !(userMovies.get(cell1)==null))
                if (userMovies.containsKey(cell1))
                {
                    //System.out.println("OK");
                    tempUM2=userMovies.get(cell1);
                    tempMovies++;
                    
                    if (Global_Vars.NEG_WEIGHT_TYPE==0) 
                        negWeight=1;
                    else
                        negWeight=Phd_Utils.Neg_Weight(tempUM1.getRating(), tempUM2.getRating());
                    
                    if (Global_Vars.WEIGHT_TYPE==1)
                    {

                        /*tempWeight=(double)(userMovies.get(cell0).Time_Stamp-users[i].MinTimeStamp)/(double)(users[i].MaxTimeStamp-users[i].MinTimeStamp);
                        userMovies.get(cell0).Weight=tempWeight;   */

                        tempWeight=(double)(tempUM1.Time_Stamp-users[i].getMinTimeStamp())/(double)(users[i].getMaxTimeStamp()-users[i].getMinTimeStamp());
                        tempUM1.setWeight(tempWeight);   
                        
                        //-System.out.print(i+" "+k+" "+tempWeight+"<->");
                        /*tempWeight=(double)(userMovies.get(cell1).Time_Stamp-users[j].MinTimeStamp)/(double)(users[j].MaxTimeStamp-users[j].MinTimeStamp);
                        userMovies.get(cell1).Weight=tempWeight;*/

                        tempWeight=(double)(tempUM2.Time_Stamp-users[j].getMinTimeStamp())/(double)(users[j].getMaxTimeStamp()-users[j].getMinTimeStamp());
                        tempUM2.setWeight(tempWeight);   
                    }
                    else
                    {
                        tempUM1.Weight=1;   
                        tempUM2.Weight=1;   
                    }
                    
                    
                    numeratorSimij += (tempUM1.getRating()-averageUI)*(tempUM2.getRating()-averageUJ)*tempUM1.getWeight()*tempUM2.getWeight();
                    negNumeratorSimij += (tempUM1.getRating()-averageUI)*(tempUM2.getRating()-averageUJ)*tempUM1.getWeight()*tempUM2.getWeight()*negWeight;

                    denominatorPartA += (tempUM1.getRating()-averageUI)*(tempUM1.getRating()-averageUI);
                    denominatorPartB += (tempUM2.getRating()-averageUJ)*(tempUM2.getRating()-averageUJ);

                    if ((tempUM1.getRating()!=3) && (tempUM2.getRating()!=3) && (similaritySign==2))
                    {
                        temp_no3movies++;            
                        NO3_numeratorSimij   += (tempUM1.getRating()-averageUI)*(tempUM2.getRating()-averageUJ)*tempUM1.getWeight()*tempUM2.getWeight()*negWeight;
                        NO3_denominatorPartA += (tempUM1.getRating()-averageUI)*(tempUM1.getRating()-averageUI);
                        NO3_denominatorPartB += (tempUM2.getRating()-averageUJ)*(tempUM2.getRating()-averageUJ);
                    }
                }
                  
            }//for k

            commonRatingSet.clear();
                
            denominatorSimij= denominatorPartA * denominatorPartB;
            Similarity=(double)(numeratorSimij/Math.sqrt(denominatorSimij));
            negSimilarity=(double)(negNumeratorSimij/Math.sqrt(denominatorSimij));
                
            NO3_denominatorSimij= NO3_denominatorPartA * NO3_denominatorPartB;
            NO3_similarity=(double)(NO3_numeratorSimij/Math.sqrt(NO3_denominatorSimij));
                
            //find min/max similarity values
            if (MinSimValue>Similarity) MinSimValue=Similarity;
            else
            if (maxSimValue<Similarity) maxSimValue=Similarity;
                
            
            //At least "commonMovies" common ratings
            if (tempMovies>commonMovies)
            {
                if (Similarity>=simBase && similaritySign==1)    //Only Positive Similarities are Considered
                {
                    
                        userSim[i].add(new UserSimilarity(i,j,Similarity));
                        userSim[j].add(new UserSimilarity(j,i,Similarity));

                } 
                else    
                   //Reverse Similarity
                if (negSimilarity<=simBase && similaritySign==0)
                {
                    
                    userSim[i].add(new UserSimilarity(i,j,negSimilarity));
                    userSim[j].add(new UserSimilarity(j,i,negSimilarity));

                } 
                
            }
            
            if (temp_no3movies>commonMovies)
                if (NO3_similarity<=simBase && similaritySign==2)
                    {
                        userSim[i].add(new UserSimilarity(i,j,NO3_similarity));
                        userSim[j].add(new UserSimilarity(j,i,NO3_similarity));                        
                    }
                        
            
        
        }//for j

} //for i
//System.out.println("max:"+absMaxTimeStamp+" min:"+absMinTimeStamp);
} //END of Method Compute_Similarity

public static void Compute_Similarity_Parallel (
int low,
int upper,        
int totalUsers, 
List<UserSimilarity>[] userSim,
User[] users,
HashMap<CellCoor,UserMovie>  userMovies,
HashSet<Integer>[] usersRatingSet,
int similaritySign,
double simBase,
int commonMovies)
    
{
        
int    i, j;
double tempWeight, negWeight;
int    tempMovies, temp_no3movies;
double averageUI, averageUJ;                    //Hold average rating of user i and j, respectively
double numeratorSimij, negNumeratorSimij, denominatorSimij;        //Numerator and Denominator of Similarity (Pearson) function.
double denominatorPartA, denominatorPartB;      //Denominator consists of two parts        
double Similarity, negSimilarity;
double NO3_numeratorSimij, NO3_denominatorSimij;
double NO3_denominatorPartA, NO3_denominatorPartB;
double NO3_similarity;
double maxSimValue=Integer.MIN_VALUE, MinSimValue=Integer.MAX_VALUE;
HashSet<Integer> commonRatingSet = new HashSet<>();   //Set containg for a specific user the Movies that has rated
CellCoor cell0 = new CellCoor();
CellCoor cell1 = new CellCoor();
UserMovie tempUM1;
UserMovie tempUM2;

//System.out.println("Similarity"+simBase);

//for (i=0;i<=totalUsers;i++) 
//    userSim[i]=new ArrayList<>(); //Collections.synchronozedList(new ArrayList<>());

for (i=low;i<=upper;i++)
{    
            
    averageUI=users[i].UserAverageRate();     
    //System.out.println(i+" "+averageUI);
    //System.out.println(i+" "+users[i].MaxTimeStamp+" "+users[i].MinTimeStamp+" "+Global_Vars.MIN_TIMESPACE);
    //System.out.println(i+" "+users[i].getMaxTimeStamp()+" "+users[i].getMinTimeStamp()+" "+Global_Vars.MIN_TIMESPACE);       

    if ((users[i].getMaxTimeStamp()-users[i].getMinTimeStamp())>Global_Vars.MIN_TIMESPACE)
       for (j=i+1;j<=totalUsers;j++)
       {

            numeratorSimij=0.0;negNumeratorSimij=0.0;denominatorSimij=0.0;                    //Initializing variables used in computing similarity
            denominatorPartA=0.0;denominatorPartB=0.0;

            NO3_numeratorSimij=0;NO3_denominatorSimij=0;
            NO3_denominatorPartA=0; NO3_denominatorPartB=0;

            averageUJ=users[j].UserAverageRate();
            tempMovies=0;temp_no3movies=0;
                
            //for (k=0;k<=totalMovies;k++)
            //System.out.print("\nUser:"+i+" "+j+" Items:");
            commonRatingSet=mergeSet(usersRatingSet[i],usersRatingSet[j]);
            
            for (int k: commonRatingSet)
            //for (int k: userRatingSet)            
            {
                cell0.user=i;cell0.movie=k;
                tempUM1=userMovies.get(cell0);
                cell1.user=j;cell1.movie=k;

                //if (!(userMovies.get(cell0)==null) && !(userMovies.get(cell1)==null))
                if (userMovies.containsKey(cell1))
                {
                    //System.out.println("OK");
                    tempUM2=userMovies.get(cell1);
                    tempMovies++;
                    
                    if (Global_Vars.NEG_WEIGHT_TYPE==0) 
                        negWeight=1;
                    else
                        negWeight=Phd_Utils.Neg_Weight(tempUM1.getRating(), tempUM2.getRating());
                    
                    if (Global_Vars.WEIGHT_TYPE==1)
                    {

                        /*tempWeight=(double)(userMovies.get(cell0).Time_Stamp-users[i].MinTimeStamp)/(double)(users[i].MaxTimeStamp-users[i].MinTimeStamp);
                        userMovies.get(cell0).Weight=tempWeight;   */

                        tempWeight=(double)(tempUM1.Time_Stamp-users[i].getMinTimeStamp())/(double)(users[i].getMaxTimeStamp()-users[i].getMinTimeStamp());
                        tempUM1.setWeight(tempWeight);   
                        
                        //-System.out.print(i+" "+k+" "+tempWeight+"<->");
                        /*tempWeight=(double)(userMovies.get(cell1).Time_Stamp-users[j].MinTimeStamp)/(double)(users[j].MaxTimeStamp-users[j].MinTimeStamp);
                        userMovies.get(cell1).Weight=tempWeight;*/

                        tempWeight=(double)(tempUM2.Time_Stamp-users[j].getMinTimeStamp())/(double)(users[j].getMaxTimeStamp()-users[j].getMinTimeStamp());
                        tempUM2.setWeight(tempWeight);   
                    }
                    else
                    {
                        tempUM1.Weight=1;   
                        tempUM2.Weight=1;   
                    }
                    
                    
                    numeratorSimij += (tempUM1.getRating()-averageUI)*(tempUM2.getRating()-averageUJ)*tempUM1.getWeight()*tempUM2.getWeight();
                    negNumeratorSimij += (tempUM1.getRating()-averageUI)*(tempUM2.getRating()-averageUJ)*tempUM1.getWeight()*tempUM2.getWeight()*negWeight;

                    denominatorPartA += (tempUM1.getRating()-averageUI)*(tempUM1.getRating()-averageUI);
                    denominatorPartB += (tempUM2.getRating()-averageUJ)*(tempUM2.getRating()-averageUJ);

                    if ((tempUM1.getRating()!=3) && (tempUM2.getRating()!=3) && (similaritySign==2))
                    {
                        temp_no3movies++;            
                        NO3_numeratorSimij   += (tempUM1.getRating()-averageUI)*(tempUM2.getRating()-averageUJ)*tempUM1.getWeight()*tempUM2.getWeight()*negWeight;
                        NO3_denominatorPartA += (tempUM1.getRating()-averageUI)*(tempUM1.getRating()-averageUI);
                        NO3_denominatorPartB += (tempUM2.getRating()-averageUJ)*(tempUM2.getRating()-averageUJ);
                    }
                }
                  
            }//for k

            commonRatingSet.clear();
                
            denominatorSimij= denominatorPartA * denominatorPartB;
            Similarity=(double)(numeratorSimij/Math.sqrt(denominatorSimij));
            negSimilarity=(double)(negNumeratorSimij/Math.sqrt(denominatorSimij));
                
            NO3_denominatorSimij= NO3_denominatorPartA * NO3_denominatorPartB;
            NO3_similarity=(double)(NO3_numeratorSimij/Math.sqrt(NO3_denominatorSimij));
                
            //find min/max similarity values
            if (MinSimValue>Similarity) MinSimValue=Similarity;
            else
            if (maxSimValue<Similarity) maxSimValue=Similarity;
                
            
            //At least "commonMovies" common ratings
            if (tempMovies>commonMovies)
            {
                if (Similarity>=simBase && similaritySign==1)    //Only Positive Similarities are Considered
                {
                    
                        userSim[i].add(new UserSimilarity(i,j,Similarity));
                        userSim[j].add(new UserSimilarity(j,i,Similarity));

                } 
                else    
                   //Reverse Similarity
                if (negSimilarity<=simBase && similaritySign==0)
                {
                    
                    userSim[i].add(new UserSimilarity(i,j,negSimilarity));
                    userSim[j].add(new UserSimilarity(j,i,negSimilarity));

                } 
                
            }
            
            if (temp_no3movies>commonMovies)
                if (NO3_similarity<=simBase && similaritySign==2)
                    {
                        userSim[i].add(new UserSimilarity(i,j,NO3_similarity));
                        userSim[j].add(new UserSimilarity(j,i,NO3_similarity));                        
                    }
                        
            
        
        }//for j

} //for i
//System.out.println("max:"+absMaxTimeStamp+" min:"+absMinTimeStamp);
} //END of Method Compute_Similarity Paraller

} //END of class
