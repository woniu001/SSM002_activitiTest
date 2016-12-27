package com.wbliu.SSM002.test.SortArithmentic;
/*交换排序
 * 
 * 包括冒泡排序和快速排序
 * 
 * */
public class ChangeSort {

	public static void main(String[] args) {
		 /*测试冒泡排序*/
		//InsertionSort.printArray(bubbleSort(InsertionSort.testA));
		
		/*测试快速排序*/
		int[] A =InsertionSort.testA;
		InsertionSort.printArray(quickSort(A, 0, A.length-1));

	}
	
	
	/*冒泡排序
	 * 
	 * 1、确定一个临界值；
	 * 2、对临界值后面的部分进行比较，数值大的往后排序
	 * 
	 * */
	public static int[] bubbleSort(int [] A){
		boolean flag = true;
		int n = A.length;

		for(int i = 0 ;i < n-1 & flag; i++){
			 flag =  false;
			
			 /*从后面往前面比较*/
			for(int j=n-2 ; j>=i ; j--){
				if(A[j] > A[j+1]){
					/*交换位置*/
					SelectionSort.swap(A,j,j+1);
			        flag =true;
				}
			}//end for
		}//end for
		
		return A;
	}//end function
	
	
	
	/*快速排序*/
	
	public static int [] quickSort(int [] A,int low,int high){
		
		if(low < high){
			/*获得基准数据*/
			int middle =getMiddle(A,low,high);  
			quickSort(A,low,middle-1);	
			quickSort(A,middle+1,high);	
		}

		return A;
	}//end function

	
	/*获得基准数据*/
	private static int getMiddle(int[] A, int low, int high) {
		// TODO Auto-generated method stub

		 int temp = A[low];
		 
		while(low < high){
			
			    while(low<high && A[high] >= temp){
			    	high--;
			    	
			    }//end while
			
			    A[low] = A[high];
			    
			    
			    while(low<high && A[low] <=temp){
			    	low++;
			    }//end while

			    A[low] =A[high];
			    
			    
		}//end while
		
		A[low] = temp;
		
		
		return low;
	}
	
}
