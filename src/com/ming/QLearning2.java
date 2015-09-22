package com.ming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

public class QLearning2
{
	private static final int absorbing_state=2;
    private static final int Q_SIZE = 7;
    private static final int A_SIZE = 3;
    private static final double GAMMA = 0.9;
    private static final int NUM_INITIALS = 7;
    private static final int ITERATIONS = 10;
    private static final double Alpha=0.1;
    private static final int INITIAL_STATES[] = new int[] {0,1,2,3,4,5,6};
    
    private static final int R[][] = new int[][] {{1,-1,-1}, 
                                                  {-1,1,-1}, 
                                                  {10,10,10}, 
                                                  {0,0,10}, 
                                                  {1,-1,-1}, 
                                                  {-1,1,-1},
                                                  {1,1,10}};
    private static HashMap<Integer,HashMap<Integer,List<Integer>>>trans=new HashMap<Integer,HashMap<Integer,List<Integer>>>();
    private static double q[][] = new double[Q_SIZE][A_SIZE];
    private static int currentState = 0;
    
    private static void initialize()
    {
    	for(int i=0;i<R.length;i++){
    		
    		for(int j=0;j<R[i].length;j++){
    			if(R[i][j]==-1){
    				HashMap<Integer,List<Integer>>temp=new HashMap<Integer,List<Integer>>();
    				temp.put(j, null);
    				trans.put(i, temp);
    			}
    		}
    	}
    	HashMap<Integer,List<Integer>>temp=new HashMap<Integer,List<Integer>>();
    	List<Integer>list=new LinkedList<Integer>(Arrays.asList(0,1,3));
    	temp.put(0, list);
    	trans.put(0, temp);
    	
    	list=new LinkedList<Integer>(Arrays.asList(1,2,3,4,5,6));
    	temp=new HashMap<Integer,List<Integer>>();
    	temp.put(1, list);
    	trans.put(1, temp);
    	
    
    	
    	
       	list=new LinkedList<Integer>(Arrays.asList(2));
    	temp=new HashMap<Integer,List<Integer>>();
    	temp.put(0, list);
    	temp.put(1, list);
    	temp.put(2, list);
    	trans.put(2, temp);

       	list=new LinkedList<Integer>(Arrays.asList(1,3));
    	temp=new HashMap<Integer,List<Integer>>();
    	temp.put(0, list);
       	list=new LinkedList<Integer>(Arrays.asList(0,3,4,6));
    	temp.put(1, list);
       	list=new LinkedList<Integer>(Arrays.asList(0,1,2,3,4,5,6));
       	temp.put(2, list);
    	trans.put(3, temp);

    	
       	list=new LinkedList<Integer>(Arrays.asList(5,6));
    	temp=new HashMap<Integer,List<Integer>>();
    	temp.put(0, list);
    	trans.put(4, temp);

    	
       	list=new LinkedList<Integer>(Arrays.asList(2,4,5,6));
    	temp=new HashMap<Integer,List<Integer>>();
    	temp.put(1, list);
    	trans.put(5, temp);
    	
    	

       	list=new LinkedList<Integer>(Arrays.asList(5,6));
    	temp=new HashMap<Integer,List<Integer>>();
    	temp.put(0, list);
       	list=new LinkedList<Integer>(Arrays.asList(4,5,6));
    	temp.put(1, list);
    	temp.put(2, list);
    	trans.put(6, temp);

    	
    	
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
        for(int i = 0; i < Q_SIZE; i++)
        {
            for(int j = 0; j < A_SIZE; j++)
            {
                System.out.print(q[i][j] + "\t");
            } // j
            System.out.print("\n\n");
        } // i
        System.out.print("\n");

        return;
    }
    /*
    private static void test()
    {
        // Perform tests, starting at all initial states.
        System.out.println("Shortest routes from initial states:");
        for(int i = 0; i < Q_SIZE; i++)
        {
            currentState = INITIAL_STATES[i];
            int newState = 0;
            do
            {
                newState = maximum(currentState, true);
                System.out.print(currentState + ", ");
                currentState = newState;
            }while(currentState !=absorbing_state);
            System.out.print("5\n");
        }

        return;
    }*/
    
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
            HashMap<Integer,List<Integer>>temp=(HashMap)trans.get(currentState);
            List<Integer>list=(List<Integer>)temp.get(possibleAction);
            if(temp==null)System.out.println("null hashmap");
            if(list==null){System.out.println("null");}
            int len=list.size();
            int ran=new Random().nextInt(len);
            currentState = list.get(ran); 
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
        //return (int)(R[currentState][Action] + (GAMMA * maximum(Action, false)));
    }
    
    public static void print(){
    	Iterator iterator=trans.entrySet().iterator();
    	while(iterator.hasNext()){
    		Entry entry=(Entry)iterator.next();
    		System.out.println("outter:"+entry.getKey());
    		HashMap<Integer,List<Integer>>map=(HashMap)entry.getValue();
    		if(map==null){System.out.println("null....");break;}
    		Iterator ite=map.entrySet().iterator();
    		while(ite.hasNext()){
    			Entry entry2=(Entry)ite.next();
    			System.out.println("inner:"+entry2.getKey());
    			List<Integer>list2=(List<Integer>)entry2.getValue();
    			for(int i:list2){
    				System.out.print(i+" ");
    			}
    			System.out.println();
    		}
    		
    	}
    }
    public static void main(String[] args)
    {
    	
        train();
        System.out.println("train end");
        //test();
        return;
        
    	
    	
    }

}