/*
 * =====================================================================================
 *
 *       Filename:  digit.c
 *
 *    Description:  
 *
 *        Version:  1.0
 *        Created:  03/17/2016 12:42:32 PM
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  Runjie Zhang (rz3vg), Runjie@virginia.edu
 *        Company:  CS/CpE@UVa
 *
 * =====================================================================================
 */

#include <stdio.h>

int isCorrect(long num) {
    int histo[10] = {0};
    if((num % 10)) return 0;
    else {
        while(num)
          {
            unsigned digit = num%10;
            num/= 10;
            histo[digit] = 1;
          }
    }
    int i;
    for(i = 0; i < 10; i++)
        if(!histo[i]) return 0;
    return 1;
}

int main(void)
{
  long i, j, n;

  long test = 1234567800;
  for(i = 1000000080; i <= 9999999990; i+= 90) {
      for(n = i, j = 10; j > 1; n/=10, j--) if(n%j) break;
      if(j == 1) {
          if(isCorrect(i)) 
            printf("%ld \n",i);
      }
  }
}
