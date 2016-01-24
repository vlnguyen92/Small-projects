/****************************************************************************
*																			*
*	File:		job.cpp														*
*																			*
*	Author:		Linh Nguyen													*
*																			*
*	Date:		April 12, 2014												*
*																			*
*	Purpose:	This file implements the << operator for Job class			*
*																			*
****************************************************************************/


//	Header files
#include <iostream>
#include "job.h"

/****************************************************************************
*																			*
*	Function:	operator<<													*
*																			*
*	Purpose:	This function will insert a Job into the output stream		*
*																			*
****************************************************************************/

ostream& operator<<(ostream& out, const Job& job)
{
	job.output(out);
	return out;
}
