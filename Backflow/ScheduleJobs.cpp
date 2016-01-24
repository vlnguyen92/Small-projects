#include <iostream>
#include "job.h"

using namespace std;

//	Function prototypes

bool jobWaiting(bool waiting[], int size);
int getFreeProc(ArrayList<Job*> curr_job);
int getNextJob(bool waiting[], ArrayList<Job*> prefList, ArrayList<Job*> done);
void startJob(int free_proc, int next_job, ArrayList<Job*>& curr_job, ArrayList<Job*> prefList, bool waiting[], 
	float finish_time[], float curr_time, ArrayList<Job*> proc_sched_job[], ArrayList<float> proc_sched_time[]);
void retireJobs(ArrayList<Job*>& curr_job, float finish_time[], float curr_time, ArrayList<Job*>& done);
float minimumTime(float finish_time[], int size);

/****************************************************************************
*																			*
*	Function:	scheduleJobs												*
*																			*
****************************************************************************/

void scheduleJobs(ArrayList<Job*> prefList, int numProcs)
{
	bool* waiting = new bool[prefList.size()];	// True if job is waiting to be scheduled
	ArrayList<Job*> curr_job(numProcs);
	float* finish_time = new float[numProcs];
	float curr_time = 0.0;
	ArrayList<Job*>* proc_sched_job = new ArrayList<Job*>[numProcs];
	ArrayList<float>* proc_sched_time = new ArrayList<float>[numProcs];
	ArrayList<Job*> done;
	
	for (int i = 0; i < prefList.size(); i++)
		waiting[i] = true;
	
	for (int i = 0; i < numProcs; i++)
		curr_job[i] = NULL;
	
	while (jobWaiting(waiting, prefList.size()))
	{
		retireJobs(curr_job, finish_time, curr_time, done);
		int free_proc = getFreeProc(curr_job);
		if (free_proc >= 0)
		{
			int next_job = getNextJob(waiting, prefList, done);
			if (next_job >= 0)
				startJob(free_proc, next_job, curr_job, prefList, waiting, finish_time, curr_time,
				proc_sched_job, proc_sched_time);
			else
				curr_time = minimumTime(finish_time, numProcs);
		}
		else
			curr_time = minimumTime(finish_time, numProcs);
	}
	
	retireJobs(curr_job, finish_time, curr_time, done);
	
//	Remove Start and End jobs

	proc_sched_job[0].popFront();
	proc_sched_job[0].popBack();
	proc_sched_time[0].popFront();
	proc_sched_time[0].popBack();
	
//	Output the schedule for each processor

	int last = proc_sched_time[0].size() - 1;
	float total_time = proc_sched_time[0][last] + proc_sched_job[0][last]->getDuration();

	float total_proc_time = 0.0;
	float total_idle_time = 0.0;
	for (int i = 0; i < numProcs; i++)
	{
		float proc_time = 0.0;
		for (int j = 0; j < proc_sched_job[i].size(); j++)
			proc_time += proc_sched_job[i][j]->getDuration();
		cout << endl << "Processor " << i + 1 << ": Busy " << proc_time << ", Idle " << total_time - proc_time << endl;
		total_proc_time += proc_time;
		total_idle_time += total_time - proc_time;
		for (int j = 0; j < proc_sched_job[i].size(); j++)
		{
			cout << '\t' << "Start job " << proc_sched_job[i][j]->getName();
			cout << " at time " << proc_sched_time[i][j];
			cout << ", end at time " << proc_sched_time[i][j] + proc_sched_job[i][j]->getDuration() << endl;
		}
	}
	
	cout << endl << "Time to complete project = " << total_time << endl;
	cout << "Total processing time = " << total_proc_time << endl;
	cout << "Total idle time = " << total_idle_time << endl;
	
	return;
}

/****************************************************************************
*																			*
*	Function:	jobWaiting													*
*																			*
****************************************************************************/

bool jobWaiting(bool waiting[], int size)
{
	for (int i = 0; i < size; i++)
		if (waiting[i])
		{
		//	cout << "Job " << i << " is waiting" << endl;
			return true;
		}
	return false;
}
	
/****************************************************************************
*																			*
*	Function:	getFreeProc													*
*																			*
****************************************************************************/

int getFreeProc(ArrayList<Job*> curr_job)
{
	for (int i = 0; i < curr_job.size(); i++)
		if (curr_job[i] == NULL)
		{
		//	cout << "Processor " << i << " is free" << endl;
			return i;
		}
	return -1;
}

/****************************************************************************
*																			*
*	Function:	getNextJob													*
*																			*
****************************************************************************/

int getNextJob(bool waiting[], ArrayList<Job*> prefList, ArrayList<Job*> done)
{
	for (int i = 0; i < prefList.size(); i++)
	{
		if (waiting[i])
		{
			ArrayList<Job*> predList = prefList[i]->getPredList();
		//	cout << "For job " << i << " the pred list is ";
		//	for (int k = 0; k < predList.size(); k++)
		//		cout << predList[k]->getName() << ' ';
		//	cout << endl << "    done = ";
		//	for (int k = 0; k < done.size(); k++)
		//		cout << done[k]->getName() << ' ';
		//	cout << endl;
			bool ready = true;
			for (int j = 0; j < predList.size(); j++)
				if (done.search(predList[j]) < 0)
					ready = false;
			if (ready)
			{
		//		cout << "Job " << i << " (" << prefList[i]->getName() << ") is the next job" << endl;
				return i;
			}
		}
	}
	return -1;
}

/****************************************************************************
*																			*
*	Function:	startJob													*
*																			*
****************************************************************************/

void startJob(int free_proc, int next_job, ArrayList<Job*>& curr_job, ArrayList<Job*> prefList, bool waiting[], 
float finish_time[], float curr_time, ArrayList<Job*> proc_sched_job[], ArrayList<float> proc_sched_time[])
{
//	cout << "Starting job " << next_job << " (" << prefList[next_job]->getName() << ") on processor " << free_proc << endl;
	curr_job[free_proc] = prefList[next_job];
	proc_sched_job[free_proc].pushBack(curr_job[free_proc]);
	proc_sched_time[free_proc].pushBack(curr_time);
	finish_time[free_proc] = curr_time + prefList[next_job]->getDuration();
	waiting[next_job] = false;
	return;
}

/****************************************************************************
*																			*
*	Function:	retireJobs													*
*																			*
****************************************************************************/

void retireJobs(ArrayList<Job*>& curr_job, float finish_time[], float curr_time, ArrayList<Job*>& done)
{
	for (int i = 0; i < curr_job.size(); i++)
		if (finish_time[i] == curr_time)
		{
		//	cout << "Retiring job " << curr_job[i]->getName() << " on processor " << i << " at time " << curr_time << endl;
			done.pushBack(curr_job[i]);
			curr_job[i] = NULL;
			finish_time[i] = 0.0;
		}
	return;
}

/****************************************************************************
*																			*
*	Function:	minimumTime													*
*																			*
****************************************************************************/

float minimumTime(float finish_time[], int size)
{
	float min_time = 0.0;
	int i = 0;
	while (i < size && finish_time[i] == 0.0)
		i++;
	min_time = finish_time[i];
	for (int j = i + 1; j < size; j++)
		if (finish_time[j] > 0.0 && finish_time[j] < min_time)
			min_time = finish_time[j];
	return min_time;
}
