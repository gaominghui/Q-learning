package com.ming;

import java.util.Random;

public class QLearning3
{
    private static final int Q_SIZE = 16;
    private static final double GAMMA = 0.8;
    private static final int ITERATIONS = 10;
    private static final int NUM_INITIALS = 16;
    private static final int GOAL_STATE = 15;
    private static final int INITIAL_STATES[] = new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};

    private static final int R[][] = new int[][] 
            {{-1, 0, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}, 
             {0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}, 
             {-1, -1, -1, 0, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1}, 
             {-1, -1, 0, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1}, 
             {0, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1}, 
             {-1, -1, -1, -1, -1, -1, 0, -1, -1, 0, -1, -1, -1, -1, -1, -1}, 
             {-1, -1, 0, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}, 
             {-1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1}, 
             {-1, -1, -1, -1, 0, -1, -1, -1, -1, 0, -1, -1, 0, -1, -1, -1}, 
             {-1, -1, -1, -1, -1, 0, -1, -1, 0, -1, 0, -1, -1, 0, -1, -1}, 
             {-1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, 0, -1}, 
             {-1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, 100}, 
             {-1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1}, 
             {-1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, 0, -1}, 
             {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, 0, -1, -1}, 
             {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, 100}};

    private static int q[][] = new int[Q_SIZE][Q_SIZE];
    private static int currentState = 0;
    
    private static void train()
    {
        initialize();

        // Perform training, starting at all initial states.
        for(int j = 0; j < ITERATIONS; j++)
        {
            for(int i = 0; i < NUM_INITIALS; i++)
            {
                episode(INITIAL_STATES[i]);
            }
        }

        // Print out Q Matrix
//        System.out.println("Q Matrix values:");
//        for(int i = 0; i < Q_SIZE; i++)
//        {
//            for(int j = 0; j < Q_SIZE; j++)
//            {
//                System.out.print(q[i][j] + ",\t");
//            }
//            System.out.print("\n");
//        }
//        System.out.print("\n");
        return;
    }
    
    private static void test()
    {
        int newState = 0;

        // Perform tests, starting at all initial states.
        System.out.println("Shortest routes from initial states:");
        for(int i = 0; i < NUM_INITIALS; i++)
        {
            currentState = INITIAL_STATES[i];
            newState = 0;
            do
            {
                newState = maximum(currentState, true);
                System.out.print(currentState + ", ");
                currentState = newState;
            }while(currentState < GOAL_STATE); //Loop Until currentState = GOAL_STATE
            System.out.print(GOAL_STATE + "\n");
        }
        return;
    }
    
    private static void episode(final int initialState)
    {
        currentState = initialState;

        // Travel from state to state until goal state is reached.
        do
        {
            chooseAnAction();
        }while(currentState == GOAL_STATE); // Loop Until currentState = GOAL_STATE

        // When currentState = GOAL_STATE, Run through the set once more to
        // for convergence.
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
        possibleAction = getRandomAction(Q_SIZE);

        if(R[currentState][possibleAction] >= 0){
            q[currentState][possibleAction] = reward(possibleAction);
            currentState = possibleAction;
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
            // Get a random value between 0(inclusive) and UpperBound(exclusive).
            action = new Random().nextInt(upperBound);
            if(R[currentState][action] > -1){
                choiceIsValid = true;
            }
        }
        return action;
    }
    
    private static void initialize()
    {
        for(int i = 0; i < Q_SIZE; i++)
        {
            for(int j = 0; j < Q_SIZE; j++)
            {
                q[i][j] = 0;
            }
        }
        return;
    }
    
    private static int maximum(final int state, final boolean returnIndexOnly)
    {
        // if(ReturnIndexOnly = true, the Q matrix index is returned.
        // if(ReturnIndexOnly = false, the Q matrix value is returned.
        int winner = 0;
        boolean foundNewWinner = false;
        boolean done = false;

        while(!done)
        {
            foundNewWinner = false;
            for(int i = 0; i < Q_SIZE; i++)
            {
                if(i != winner){             // Avoid self-comparison.
                    if(q[state][i] > q[state][winner]){
                        winner = i;
                        foundNewWinner = true;
                    }
                }
            }

            if(foundNewWinner == false){
                done = true;
            }
        }

        if(returnIndexOnly == true){
            return winner;
        }else{
            return q[state][winner];
        }
    }
    
    private static int reward(final int action)
    {
        return (int)(R[currentState][action] + (GAMMA * maximum(action, false)));
    }

    public static void main(String[] args)
    {
        train();
        test();
        return;
    }

}