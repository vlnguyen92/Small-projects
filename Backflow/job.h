/****************************************************************************
*																			*
*	File:		job.h														*
*																			*
*	Author:		Linh Nguyen													*
*																			*
*	Date:		April 12, 2014												*
*																			*
*	Purpose:	This file contains the definition and 						*
*				and implementation of the Job class							*
*																			*
****************************************************************************/

#ifndef JOB_H
#define JOB_H


//	Header files
#include <iostream>
#include <string>
#include <cassert>
#include "arraylist.h"

using namespace std;

/****************************************************************************
*																			*
*	The Job class definition												*
*																			*
****************************************************************************/

class Job
{
	private:
		string name;
		float duration;
		float crit_time;
		ArrayList<Job*> predList;
		ArrayList<Job*> succList;


	public:
	//	Constructors
		Job() : name("XXX"), duration(0.0) {}
		Job(const string n) : name(n), duration(0.0) {}
		Job(const string n, const float d) : name(n), duration(d) {}

	//	Inspectors
		string getName() const {return name;}
		float getDuration() const {return duration;}
		float getCriticalTime() const {return crit_time;}
		ArrayList<Job*> getPredList() const {return predList;}
		ArrayList<Job*> getSuccList() const {return succList;}

	//	Mutators
		void setName(string n) {name = n;}
		void setDuration(float d) 
			{assert(d >= 0.0); duration = d;}
		void setCriticalTime(float ct) 
			{assert(ct >= 0.0); crit_time = ct;}
		void addToCriticalTime(float t) 
			{assert(t >= 0.0); crit_time+=t;}
		void addPred(Job* job) {predList.pushBack(job);}
		void addSucc(Job* job) {succList.pushBack(job);}
		
	//	Facilitators
		void output(ostream& out) const
			{out << "Name: " << name << endl 
			     << "Duration: " << duration << endl
			     << "Critical Time: " << crit_time << endl 
			     << "Predecessor List: " << predList << endl
			     << "Successor List: " << succList << endl;}
};

//	Operators
ostream& operator<<(ostream& out, const Job& job);

#endif
