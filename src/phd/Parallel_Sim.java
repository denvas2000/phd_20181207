
/*
 * To change this license header, choose License Headers in Project
Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phd;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Parallel_Sim implements Runnable{
 int option;
 int low;
 int upper;
 int totalUsers;
 List<UserSimilarity>[]  userSim;
 User[] users;
 HashMap<CellCoor,UserMovie> userMovies;
 HashSet<Integer>[] usersRatingSet;
 int similaritySign;
 double simBase;
 int commonMovies;

Parallel_Sim(
int option,
int low,
int upper,
int totalUsers,
List<UserSimilarity>[] userSim,
User[] users,
HashMap<CellCoor,UserMovie> userMovies,
HashSet<Integer>[] usersRatingSet,
int similaritySign,
double simBase,
int commonMovies) {

this.option = option;
this.low = low;
this.upper=upper;
this.totalUsers=totalUsers;
this.userSim=userSim;
this.users=users;
this.userMovies=userMovies;
this.usersRatingSet=usersRatingSet;
this.similaritySign=similaritySign;
this.simBase=simBase;
this.commonMovies=commonMovies;
} //Constructor Parallel_Sim

 @Override
 public void run() {
System.out.println(low + " "+upper+" "+simBase+" "+commonMovies+ "option "
+ option);
switch(option) {
    case 1: Similarities.Positive_Similarity_Parallel(low,upper,
totalUsers, userSim, users, userMovies, usersRatingSet, simBase,
commonMovies);
            break;
    case 2: Similarities.Compute_Similarity_Parallel(low,upper,
totalUsers, userSim, users, userMovies, usersRatingSet,
similaritySign, simBase, commonMovies);
            break;
    case 3: Similarities.Inverted_Similarity_Parallel(low,upper,
totalUsers, userSim, users, userMovies, usersRatingSet, simBase,
commonMovies);          //Set new timer
            break;
}//switch
} //run

} //Class Parallel_Sim
