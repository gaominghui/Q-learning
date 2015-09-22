package com.ming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

public class QLearning5
{
	private static final int absorbing_state=242;
    private static final int Q_SIZE = 243;
    private static final int A_SIZE = 3;
    private static final double GAMMA = 0.9;
    private static final double Alpha=0.1;
    private static final int NUM_INITIALS = 243;
    private static final int ITERATIONS = 80;
    private static final int INITIAL_STATES[] = new int[243] ;
    static{
    	for(int i=0;i<243;i++){
    		INITIAL_STATES[i]=i;
    	}
    }
    private static final int R[][] = new int[243][3];
    	
	private static List<List<String>>list_list=new LinkedList<List<String>>();
	private static List<List<String>>request_list=new LinkedList<List<String>>();
	private static List<List<String>>confirm_list=new LinkedList<List<String>>();
	private static List<List<String>>con_req_list=new LinkedList<List<String>>();
    		
   // private static HashMap<Integer,HashMap<Integer,List<Integer>>>trans=new HashMap<Integer,HashMap<Integer,List<Integer>>>();
    private static double q[][] = new double[Q_SIZE][A_SIZE];
    private static int currentState = 0;
    
    private static void initialize()
    {
    	
    	readReward.readReward("./src/states",R,list_list);
        for(int i = 0; i < Q_SIZE; i++)
        {
            for(int j = 0; j < A_SIZE; j++)
            {
                q[i][j] = 0;
            } // j
        } // i
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
    		return "confirm";
    	}else return "confirm_request";
    	
    }
    private static void getResult(){
    	for(int i=0;i<Q_SIZE;i++){
    		String res=getResultHelper(q[i]);
    		List<String>list=list_list.get(i);
    		//list.add(res);
    		if(res.equals("request")){
    			request_list.add(list);
    		}else if(res.equals("confirm")){
    			confirm_list.add(list);
    		}else{
    			con_req_list.add(list);
    		}
    		
    	}
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
/*
        System.out.println("Q Matrix values:");
        for(int i = 0; i < Q_SIZE; i++)
        {
            for(int j = 0; j < A_SIZE; j++)
            {
                System.out.print(q[i][j] + ",\t");
            } // j
            System.out.print("\n");
        } // i
        System.out.print("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\n");
        */
        getResult();
        for(int i=0;i<con_req_list.size();i++){
        	List<String>temp=con_req_list.get(i);
        	for(int j=0;j<temp.size();j++){
        		System.out.print(temp.get(j)+"\t");
        	}
        	System.out.println();
        }
        //System.out.println(request_list.size());
        //System.out.println(confirm_list.size());
       // System.out.println(con_req_list.size());
        
        return;
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