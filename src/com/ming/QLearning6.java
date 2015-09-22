package com.ming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

public class QLearning6
{
	private static final int absorbing_state=1;
    private static final int Q_SIZE = 14;
    private static final int A_SIZE = 7;
    private static final double GAMMA = 0.9;
    private static final double Alpha=0.1;
    private static final int NUM_INITIALS = 14;
    private static final int ITERATIONS = 100;
    private static final int INITIAL_STATES[] = new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13};
    
    private static final int R[][] = new int[][] {{2,-1,-1,-1,-1,-1,-1}, 
                                                  {-1,-1,2,-1,-1,-1,-1}, 
                                                  {1,2,-1,-1,-1,-1,-1}, 
                                                  {-1,-1,2,-1,-1,-1,-1}, 
                                                  {-1,-1,2,-1,-1,-1,-1}, 
                                                  {-1,-1,-1,-1,2,-1,-1},
                                                  {-1,-1,1,2,-1,-1,-1},
                                                  {1,1,-1,-1,-1,2,-1},
                                                  {-1,-1,-1,-1,2,-1,-1},
                                                  {-1,-1,1,2,-1,-1,-1},
                                                  {-1,-1,-1,-1,2,-1,-1},
                                                  {-1,-1,1,2,-1,-1,-1},
                                                  {-1,-1,-1,-1,2,-1,-1},
                                                  {-1,-1,-1,-1,-1,-1,2},
                                                  };
   // private static HashMap<Integer,HashMap<Integer,List<Integer>>>trans=new HashMap<Integer,HashMap<Integer,List<Integer>>>();
    private static double q[][] = new double[Q_SIZE][A_SIZE];
    private static int currentState = 0;
    
    private static void initialize()
    {
    	
    	
        for(int i = 0; i < Q_SIZE; i++)
        {
            for(int j = 0; j < A_SIZE; j++)
            {
                q[i][j] = 0;
            } // j
        } // i
        return;
    }
    private static void train()
    {
        initialize();
        

        // Perform training, starting at all initial states.
        for(int j = 0; j < ITERATIONS; j++)
        {
            for(int i = 0; i < Q_SIZE; i++)
            {
           
                episode(INITIAL_STATES[i]);
               
            } // i
        } // j

        System.out.println("Q Matrix values:");
        
    	double max;
    	int ret;
        for(int i = 0; i < Q_SIZE; i++)
        {	 max=Double.MIN_VALUE;
        	 ret=-1;
            for(int j = 0; j < A_SIZE; j++)
            {
            	if(q[i][j]>max){
        			max=q[i][j];
        			ret=j;
        		}
              // System.out.print(q[i][j] + ",\t");
            } // j
            if(ret==0){
        		System.out.print( "request");
        	}else if(ret==1){
        		System.out.print( "re_request");
        	}else if(ret==2){
        		System.out.print ("confirm");
        	}else if(ret==3)
        		System.out.print ("re_confirm");
        	else  if(ret==4)
        		System.out.print("done");
        	else if(ret==5)
        		System.out.print("dis_request");
        	else System.out.print("ok");
            System.out.print("\n");
        } // i
        //System.out.print("\n");

        return;
    }
    private static String getResultHelper(double[]arr){
    	double max=Double.MIN_VALUE;
    	int ret=-1;
    	for(int i=0;i<3;i++){
    		if(arr[i]>max){
    			max=arr[i];
    			ret=i;
    		}
    	}
    	if(ret==0){
    		return "request";
    	}else if(ret==1){
    		return "re_request";
    	}else if(ret==2){
    		return "confirm";
    	}else if(ret==3){
    		return "re_confirm";
    	}else 
    		return "done";
    		
    	
    	
    }
    private static void getResult(){
    	for(int i=0;i<Q_SIZE;i++){
    		String res=getResultHelper(q[i]);
    		
    		
    	}
    }
    private static void episode(final int initialState)
    {
        currentState = initialState;

        // Travel from state to state until goal state is reached.
        do
        {
            chooseAnAction();
        }while(currentState != absorbing_state);
       // System.out.println("asdfasdfasdf");
        // When currentState = 5, Run through the set once more for convergence.
        for(int i = 0; i < Q_SIZE; i++)
        {
            chooseAnAction();
        }
        return;
    }
    
    private static void chooseAnAction()
    {
        int possibleAction = 0;

        // Randomly choose a possible action connected to the current state.
        
        possibleAction = getRandomAction(A_SIZE);
        if(R[currentState][possibleAction] >= 0){
            q[currentState][possibleAction] = reward(possibleAction);
            //int ran=new Random().nextInt(Q_SIZE);
            currentState = new Random().nextInt(Q_SIZE);
           // System.out.println(currentState);
        }
      
        return;
    }
    
    private static int getRandomAction(final int upperBound)
    {
        int action = 0;
        boolean choiceIsValid = false;

        // Randomly choose a possible action connected to the current state.
        while(choiceIsValid == false)
        {
            // Get a random value between 0(inclusive) and 6(exclusive).
            action = new Random().nextInt(upperBound);
            //System.out.println("xxxxxx:"+action);
            if(R[currentState][action] > -1){
                choiceIsValid = true;
            }
        }
        
        return action;
    }
    
   
    
    private static double maximum(final int State, final boolean ReturnIndexOnly)
    {
        // If ReturnIndexOnly = True, the Q matrix index is returned.
        // If ReturnIndexOnly = False, the Q matrix value is returned.
        int winner = 0;
        boolean foundNewWinner = false;
        boolean done = false;

        while(!done)
        {
            foundNewWinner = false;
            for(int i = 0; i < A_SIZE; i++)
            {
                if(i != winner){             // Avoid self-comparison.
                    if(q[State][i] > q[State][winner]){
                        winner = i;
                        foundNewWinner = true;
                    }
                }
            }

            if(foundNewWinner == false){
                done = true;
            }
        }

        if(ReturnIndexOnly == true){
            return winner;
        }else{
            return q[State][winner];
        }
    }
    
    private static double reward(final int Action)
    {
        return q[currentState][Action]+Alpha*(R[currentState][Action] + (GAMMA * maximum(Action, false))-q[currentState][Action]);
    }

    public static void main(String[] args)
    {
    	
        train();
        System.out.println("train end");
        //test();
        return;
  
    	
    	
    }

}