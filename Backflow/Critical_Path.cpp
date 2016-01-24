/****************************************************************************
*																			*
*	Program:	Critical_Path.cpp											*
*																			*
*	Author:		Linh Nguyen													*
*																			*
*	Date:		Apr 12, 2014												*
*																			*
*	Purpose:	This file computes the critical path and critical			*
*				time and invokes the ScheduleJobs funtion					*
*																			*
****************************************************************************/

//	Header files
#include <iostream>
#include <fstream>
#include "job.h"
#include "arraylist.h"
#include "arrayqueue.h"

using namespace std;

//	Function prototypes
float findMax(const ArrayList<Job*>& jobList);
ArrayList<Job*> readProjectData(ifstream& fin);
void scheduleJobs(ArrayList<Job*> prefList, int numProcs);

/****************************************************************************
*																			*
*	Function:	main														*
*																			*
****************************************************************************/
int main()
{	

//	Open the data file
	ifstream fin("data/Project_0.txt");
	
//	Make sure it opens
	if(!fin)
	{
		cout << "Cannot open file" << endl;
		return -1;
	}
	
	
//	Read in the job list
	ArrayList<Job*> pList = readProjectData(fin);
	
	ArrayQueue<Job*> queue; 



//	And successors to each job
	int size = pList.size();

	for(int i = 0; i < size; i++)
	{
		cout << pList[i]->getSuccList() << endl;
	}

	for(int i = 0; i < size; i++)
	{
		ArrayList<Job*> pred = pList[i]->getPredList();
		for(int j = 0; j < pred.size(); j++)
			pred[j]->addSucc(pList[i]);
	}

	for(int i = 0; i < size; i++)
	{
		cout << pList[i]->getSuccList() << endl;
	}
	
//	Begin with the End job
	queue.enqueue(pList[size-1]);
	
//	Update the job's critical times
	while(!queue.isEmpty())
	{	
		Job* node = queue.dequeue();
		ArrayList<Job*> preList = node->getPredList();
		for(int i = 0; i < preList.size(); ++i)
			queue.enqueue(preList[i]);
		node->setCriticalTime(node->getDuration() + findMax(node->getSuccList()));
	}
	
	ArrayList<Job*> pListCopy = pList;
	ArrayList<Job*> prefList;
	ArrayList<string> nameList, path;
	
//	Construct the preference list	
	for(int i = 0; i < size; i++)	
	{
		float max = findMax(pListCopy);
		for(int j = 0; j < pListCopy.size(); ++j)
		{
		//	Find the Job with maximum critical time
			if (pListCopy[j]->getCriticalTime() == max) 
			{
			//	Add it to preference list
				prefList.pushBack(pListCopy[j]);
				nameList.pushBack(pListCopy[j]->getName());
				pListCopy.remove(j); 
			}	
		}
	}
	
//	Start with the START node
	Job* node = pList[0];
	ArrayList<Job*> succList = node->getSuccList();
	path.pushBack(node->getName());
	
//	Find the critical path
	while(node->getName()!="End")
	{
	//	Find the maximum of the current node's sucessors
		//float max = findMax(succList);
		int size = succList.size();
	
		//if (size == 0) return 0;
		
		float max = succList[0]->getCriticalTime();
	
		for(int i = 1; i < size; ++i)
		{
			float num = succList[i]->getCriticalTime();
			if (num > max)
			max = num;
		}

		for(int j = 0; j < succList.size(); ++j)
		{
		//	Update if necessary
			if (succList[j]->getCriticalTime() == max) 
			{
				path.pushBack(succList[j]->getName());
				node = succList[j];
				succList = succList[j]->getSuccList();
			}	
		}
	}

//	Output the results		
	cout << "Critical Time = " << prefList[0]->getCriticalTime() << endl;
	cout << "Critical Path = " << path << endl;
	cout << "Preference List = " << nameList << endl;
	int n;
	cout << "How many processors? " << endl;
	cin >> n;
	scheduleJobs(prefList,n);
	return 0;
}

/****************************************************************************
*																			*
*	Function:	findMax														*
*																			*
*	Purpose:	This function will find the Job with maximum				*
*			critial time in a list											*
*																			*
****************************************************************************/

float findMax(const ArrayList<Job*>& jobList)
{
	int size = jobList.size();
	
	if (size == 0) return 0;
	
	float max = jobList[0]->getCriticalTime();
	
	for(int i = 1; i < size; ++i)
	{
		float num = jobList[i]->getCriticalTime();
		if (num > max)
			max = num;
	}
	return max;
}

























