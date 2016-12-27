package com.wbliu.SSM002.test.SortArithmentic;

/*选择排序:
 * 包括简单选择排序和堆排序*/
public class SelectionSort {

	
	public static void main(String[] args) {
			
		  int[] B = simpleSelectSort(InsertionSort.testA);
		
			InsertionSort.printArray(B);
			
	}
	
	/*简单选择排序
	 * 
	 * 
    1.【初始升序】：交换0次，时间复杂度为o(n); 【初始降序】：交换n-1次，时间复杂度为o(n^2)。
           【特点】：交换移动数据次数少，比较次数多。
	 * 
	 * */

	public static int [] simpleSelectSort(int [] A){
		
		 int min = 0;
		 
		 for(int i = 0;i < A.length-1 ;i++){
			 
			 min = i;//赋值
			 
			 for(int j = i+1 ; j <A.length;j++){
				 
				 /*交换数组下标，使min是最小数组元素的下标*/
				 if(A[min] > A[j]){
					 min = j;
				 }
				 
			 }//end for
			 

			 //只有下标不同时，才交换数据
			 if( i !=min){
				 swap(A,i,min);
			 }
			 
		 }//end for
		
		return A;
	}//end function
	

	/*交换数组中两个元素的值*/
	public  static void swap(int [] A,int i,int min){
		int temp = A[i];
		A[i] = A[min];
		A[min] =temp;
	}//end function
	

	

/*    【堆】 1.堆是完全二叉树 2.大顶堆：每个结点的值都大于或等于其左右孩子结点的值，称为大顶堆。3.小顶堆：每个结点的值都大于或等于其左右孩子结点的值，称为小顶堆。
    【完全二叉树性质5】：如果i>1,则双亲是结点[i/2]。也就是说下标i与2i和2i+1是双亲子女关系。 当排序对象为数组时，下标从0开始，所以下标 i 与下标 2i+1和2i+2是双亲子女关系。

*
* 回头再实现
*/
	public static int  [] heapSort(int[] A){
		
		
		
		return A;
	}//end function
	
	
}
