package com.wbliu.SSM002.test.SortArithmentic;

/*插入排序-直接插入排序
 * 2016年12月27日 星期二
 * */
public class InsertionSort {

	
	/*主函数*/
	public static void main(String[] args) {
        int [] testA = {10,3,2,1,5,6,4};
        
          /*直接排序*/
/*      	int [] testB = insertionSort1(testA);*/	
      	 
        
        /*希尔排序*/
         int [] testB = shellSort(testA);	
      	
         
      	
      	/*打印出排序后的数组*/
      	printArray(testB);
	}

	
	
	
	
	
	/*打印数组*/
	public static void  printArray(int[] B){
	    for(int i : B){
	    	System.out.print(i+" ");
	    }
		

	}//end function
	
	
/*直接插入排序
 * 1、遍历数组；
 * 2、获得的一个数值i为参考数值，往前寻找最小位置的坐标；
 * 3、将i赋值给最小数值；
 * 4、循环以上步骤，直到遍历所有数组；
 * */
	public static int [] insertionSort1(int [] A){
		
	     int i ,j,temp;
	     
	     /*1、正序遍历，获得当前值*/
	     for(i = 0; i<A.length; i++){//for 1
	    	   temp = A[i];
	    	 
	    	   
	    	 //2、以当前值为终点，倒序遍历，并作判断处理，找到当前值在倒序数组中的最小位置
	    	 for(j=i; j>0 && A[j-1] > temp ; j--){//for 1.1
	    	     A[j] = A[j-1];//交换位置上的数值
	    	 }//end for 1.1
	    	 
	    	 //3、将当前值赋值到最小位置上
	    	 A[j] = temp;
	    	 
	     }//end for1
		
		return A;
	}
	
	
	/*希尔排序*/
	
	public static int [] shellSort(int [] A){
		
		int temp,j,i;
		
		int n= A.length;
		for(int D = n/2; D>1 ;D = D/2 ){

			for(j = D ; j<n ; j++){
				
				temp = A[j];
				 for(i = j ; i >=D && A[i - D] >temp ; i-=D){
					 
					 A[i] = A[i-D];
					 
					 
				 }//end for
				
				A[i] = temp;
				
			}//end for
			
			
			
			
			
		}//end for
		
		
		
		
		
		
		return A;
	}//end function
	
	
	
	
}
