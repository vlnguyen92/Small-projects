#ifndef ARRAYQUEUE_H
#define ARRAYQUEUE_H

#include <iostream>
#include "circarraylist.h"

using namespace std;

template <class T>
class ArrayQueue
{
//	Public member functions

	public:
	
	//	Inspectors
		
		T head() const {return list[0];}
		int size() const {return list.size();}
		bool isEmpty() const {return list.isEmpty();}
		
	//	Mutators
	
		void enqueue(const T& value) {list.pushBack(value);}
		T dequeue() {return list.popFront();}
		void makeEmpty() {list.makeEmpty();}
		
	//	Facilitators
	
		void input(istream& in) {list.input(in);}
		void output(ostream& out) const {list.output(out);}
		
//	Private data members

	private:
		
		CircArrayList<T> list;
};

template <class T>
istream& operator>>(istream& in, ArrayQueue<T>& q);
template <class T>
ostream& operator<<(ostream& out, const ArrayQueue<T>& q);

template <class T>
istream& operator>>(istream& in, ArrayQueue<T>& q)
{
	q.input(in);
	return in;
}

template <class T>
ostream& operator<<(ostream& out, ArrayQueue<T>& q)
{
	q.output(out);
	return out;
}

#endif
