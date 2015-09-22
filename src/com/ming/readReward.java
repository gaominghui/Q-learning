package com.ming;
import java.io.*;
import java.util.regex.*;
import java.util.*;
public class readReward {
	
	public static void readReward(String file,int[][]ret,List<List<String>>list_list){
		try {
			File f=new File(file);
			FileInputStream fis=new FileInputStream(f);
			InputStreamReader isr=new InputStreamReader(fis);
			BufferedReader br=new BufferedReader(isr);
			String line;
			Pattern pattern=Pattern.compile(",");
			int count=0;
			int[]arr;
			List<String>list;
			while((line=br.readLine())!=null){
				if(line.equals(""))continue;
				line.trim();
				arr=new int[3];
				line=line.substring(0,line.length()-1);
				String[]splits=pattern.split(line);
				list=new ArrayList<String>();
				list.add(splits[0]);
				list_list.add(list);
				for(int i=1;i<splits.length;i++){
					arr[i-1]=Integer.parseInt(splits[i]);
				}
				ret[count]=arr;
			
				count++;
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[][]ret=new int[243][3];
		List<List<String>>list_list=new LinkedList<List<String>>();
		readReward("./src/states",ret,list_list);
		if(ret==null)System.out.println("ret is null");
		for(int i=0;i<243;i++){
			for(int j=0;j<3;j++){
				System.out.print(ret[i][j]+" ");
			}
			System.out.println();
		}
		for(List<String>list:list_list){
			for(String temp:list){
				System.out.println(temp);
			}
		}
	}

}
