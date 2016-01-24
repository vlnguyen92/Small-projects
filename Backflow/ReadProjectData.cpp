#include <iostream>
#include <fstream>
#include "arraylist.h"
#include "job.h"

using namespace std;

//	Function prototypes

ArrayList<Job*> readProjectData(ifstream& fin);

/****************************************************************************
*																			*
*	Function:	readProjectData												*
*																			*
****************************************************************************/

ArrayList<Job*> readProjectData(ifstream& fin)
{
	ArrayList<string> nameList;
	fin >> nameList;

	ArrayList<Job*> project(nameList.size());

	for (int i = 0; i < nameList.size(); i++)
		project[i] = new Job(nameList[i]);

	int job_no;
	float dur;
	ArrayList<int> pred;

	while (fin >> job_no >> dur)
	{
		fin >> pred;
		project[job_no]->setDuration(dur);
//		project[job_no]->setCriticalTime(dur);

		for (int i = 0; i < pred.size(); i++)
			project[job_no]->addPred(project[pred[i]]);
	}

	return project;
}


