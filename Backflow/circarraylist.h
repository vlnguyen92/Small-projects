#ifndef CIRCARRAYLIST_H
#define CIRCARRAYLIST_H

//	Header files

#include <iostream>
#include <cassert>

using namespace std;

/****************************************************************************
*																			*
*	The CircArrayList class definition										*
*																			*
****************************************************************************/

template<class T>
class CircArrayList
{
//	Public member functions

	public:

	//	Constructors

		CircArrayList(int sz = 0, const T& value = T());
		CircArrayList(const CircArrayList<T>& lst) {makeCopy(lst);}
		
	//	Destructor
	
		~CircArrayList() {delete [] element;}

	//	Inspectors

		T getElement(int pos) const;
		T& getElement(int pos);
		int size() const {return mSize;}
		bool isEmpty() const {return mSize == 0;}

	//	Mutators

		void setElement(int pos, const T& value) {getElement(pos) = value;}
		void insert(int pos, const T& value);
		void remove(int pos);
		void pushFront(const T& value);
		void pushBack(const T& value);
		T popFront();
		T popBack();
		void makeEmpty();

	//	Facilitators
	
		void input(istream& in);
		void output(ostream& out) const;
	
	//	Operators
	
		CircArrayList<T>& operator=(const CircArrayList<T>& lst);
		T operator[](int pos) const {return getElement(pos);}
		T& operator[](int pos) {return getElement(pos);}

	//	Other functions
	
		void swap(CircArrayList<T>& lst);
		int seqSearch(const T& value) const;
		int binSearch(const T& value);
		void selectionSort();
		void mergeSort();
		bool isValid() const;
	
//	Private member functions
	
	private:
		void setCapacity(int sz);
		void makeCopy(const CircArrayList<T>&);
		void shiftLeft(int start, int stop);
		void shiftRight(int start, int stop);
		void copyElements(T* src, int start, int stop, T* dst);
		int binSearch(int left, int right, const T& value) const;
		int adjIndex(int i) const;
		void mergeSort(int left, int right);
		void merge(int left, int middle, int right);

//	Data members

	protected:
		int capacity;	// Number of positions allocated
		int mSize;		// Number of elements in the list
		int head;		// Position of first element
		int tail;		// Position 1 past last element
		T* element;		// List of elements
};

//	CircArrayList operators

template <class T>
istream& operator>>(istream& in, CircArrayList<T>&);
template <class T>
ostream& operator<<(ostream& out, const CircArrayList<T>&);

/****************************************************************************
*																			*
*	Function:	CircArrayList(int = 0, T = T())								*
*																			*
*	Purpose:	To construct a doubly linked list containing a specified	*
*				number of nodes containing default values					*
*																			*
****************************************************************************/

template<class T>
CircArrayList<T>::CircArrayList(int sz, const T& value)
{
	assert(sz >= 0);

//	Initialize the data members

	capacity = sz;
	mSize = sz;
	head = 0;
	tail = 0;
	
//	Allocate memory for the array

	if (capacity == 0)
		element = NULL;
	else
		element = new T[capacity];

//	Initialize the array
	
	for (int i = 0; i < mSize; i++)
		element[i] = value;
	
	return;
}

/****************************************************************************
*																			*
*	Function:	T getElement												*
*																			*
*	Purpose:	To return the element in the specified position				*
*																			*
****************************************************************************/

template<class T>
T CircArrayList<T>::getElement(int pos) const
{
//	Verify that pos is valid

	assert(pos >= 0 && pos < mSize);

//	Return value

	return element[adjIndex(pos + head)];
}

/****************************************************************************
*																			*
*	Function:	T& getElement												*
*																			*
*	Purpose:	To return a reference to the element in the specified		*
*				position													*
*																			*
****************************************************************************/

template<class T>
T& CircArrayList<T>::getElement(int pos)
{
//	Verify that pos is valid

	assert(pos >= 0 && pos < mSize);
	
//	Return value

	return element[adjIndex(pos + head)];
}

/****************************************************************************
*																			*
*	Function:	insert														*
*																			*
*	Purpose:	To insert a new element into the specified position of the	*
*				list														*
*																			*
****************************************************************************/

template<class T>
void CircArrayList<T>::insert(int pos, const T& value)
{
	assert(pos >= 0 && pos <= mSize);

	if (pos == 0)
		pushFront(value);
	else if (pos == mSize)
		pushBack(value);
	else
	{
	//	See if list if full
	
		if (mSize == capacity)
		{
			if (capacity == 0)
				setCapacity(1);
			else
				setCapacity(2 * capacity);
		}
		
	//	See which end is closer
	
		int realPos;
	
		if (pos < mSize/2)							// Head is closer
		{
			realPos = adjIndex(pos - 1 + head);		// Find actual position
			shiftLeft(head, realPos);
			head = adjIndex(--head + capacity);
		}
		else										// Tail is closer
		{
			realPos = adjIndex(pos + head);			// Find actual position
			shiftRight(realPos, tail - 1);
			tail = adjIndex(++tail);		
		}
		element[realPos] = value;
		mSize++;
	}
	
	return;
}

/****************************************************************************
*																			*
*	Function:	remove														*
*																			*
*	Purpose:	To delete from the list the element in the specified		*
*				position													*
*																			*
****************************************************************************/

template<class T>
void CircArrayList<T>::remove(int pos)
{
	assert(pos >= 0 && pos < mSize);				// List cannot be empty

	if (pos == 0)
		popFront();
	else if (pos == mSize - 1)
		popBack();
	else
	{
	//	See which end is closer
	
		int realPos = adjIndex(pos + head);		// Find actual position
		
		if (pos < mSize/2)							// Head is closer
		{
			shiftRight(head, realPos - 1);
			head = adjIndex(++head);
		}
		else										// Tail is closer
		{
			shiftLeft(realPos + 1, tail - 1);
			tail = adjIndex(--tail + capacity);
		}
		
		mSize--;
		
		if (mSize <= capacity/4)
			setCapacity(capacity/2);
	}
	
	return;
}

/****************************************************************************
*																			*
*	Function:	pushFront													*
*																			*
*	Purpose:	To append the specified element onto the end of the list	*
*																			*
****************************************************************************/

template<class T>
void CircArrayList<T>::pushFront(const T& value)
{
	if (mSize == capacity)
	{
		if (capacity == 0)
			setCapacity(1);
		else
			setCapacity(2*capacity);
	}
	
	head = adjIndex(--head + capacity);
	element[head] = value;
	mSize++;
	
	return;
}

/****************************************************************************
*																			*
*	Function:	pushBack													*
*																			*
*	Purpose:	To append the specified element onto the end of the list	*
*																			*
****************************************************************************/

template<class T>
void CircArrayList<T>::pushBack(const T& value)
{
	if (mSize == capacity)
	{
		if (capacity == 0)
			setCapacity(1);
		else
			setCapacity(2*capacity);
	}
	
	element[tail] = value;
	tail = adjIndex(++tail);
	mSize++;
	
	return;
}

/****************************************************************************
*																			*
*	Function:	popFront													*
*																			*
*	Purpose:	To append the specified element onto the end of the list	*
*																			*
****************************************************************************/

template<class T>
T CircArrayList<T>::popFront()
{
	assert(mSize > 0);
	
	T value = element[head];
	head = adjIndex(++head);
	mSize--;

	if (mSize <= capacity/4)
		setCapacity(capacity/2);
		
	return value;
}

/****************************************************************************
*																			*
*	Function:	popBack														*
*																			*
*	Purpose:	To append the specified element onto the end of the list	*
*																			*
****************************************************************************/

template<class T>
T CircArrayList<T>::popBack()
{
	assert(mSize > 0);
	
	T value = element[tail];
	tail = adjIndex(--tail + capacity);
	mSize--;

	if (mSize <= capacity/4)
		setCapacity(capacity/2);
		
	return value;
}

/****************************************************************************
*																			*
*	Function:	makeEmpty													*
*																			*
*	Purpose:	To remove all elements from the list, returning the list	*
*				to the empty state											*
*																			*
****************************************************************************/

template<class T>
void CircArrayList<T>::makeEmpty()
{
	if (capacity > 0)
	{
		capacity = 0;
		mSize = 0;
		head = 0;
		tail = 0;
		delete [] element;
		element = NULL;
	}
	return;
}

/****************************************************************************
*																			*
*	Function:	input														*
*																			*
*	Purpose:	To extract a linked list object from the input stream		*
*																			*
****************************************************************************/

template <class T>
void CircArrayList<T>::input(istream& in)
{
	makeEmpty();
	char c;
	T value;
	
	in >> c;					// Read the first character
	assert(c == '{');			// First char is not '{'

	in >> c;					// Read next character
	if (c == '}')				// List is empty
		return;					// Nothing else to read
		
	else						// List is not empty
	{
		in.putback(c);			// Put char back in input stream
		while(c != '}')			// Read until '}' is found
		{
			in >> value;		// Read the element
		 	pushBack(value);	// Append the element to the list
		 	in >> c;			// Read the comma or close brace
		}
	}
	return;
}

/****************************************************************************
*																			*
*	Function:	output														*
*																			*
*	Purpose:	To insert a doubly linked list into the output stream		*
*																			*
****************************************************************************/

template<class T>
void CircArrayList<T>::output(ostream& out) const
{
	out << "{";
	if (mSize > 0)
	{
		if (head < tail)
		{
			out << element[head];
			for (int i = head + 1; i < tail; i++)
				out << ", " << element[i];
		}
		else
		{
			out << element[head];
			for (int i = head + 1; i < capacity; i++)
				out << ", " << element[i];
			for (int i = 0; i < tail; i++)
				out << ", " << element[i];
		}
	}
	out << "}";
	return;
}

/****************************************************************************
*																			*
*	Function:	operator=													*
*																			*
*	Purpose:	To assign one doubly linked list to another doubly linked	*
*				list.  This is the assignment operator.						*
*																			*
****************************************************************************/

template<class T>
CircArrayList<T>& CircArrayList<T>::operator=(const CircArrayList<T>& lst)
{
	if (this != &lst)
	{
		makeEmpty();
		makeCopy(lst);
	}
	return *this;
}

/****************************************************************************
*																			*
*	Function:	swap														*
*																			*
*	Purpose:	To swap two circularly linked lists							*
*																			*
****************************************************************************/

template <class T>
void CircArrayList<T>::swap(CircArrayList<T>& lst)
{
	int tempCapacity = capacity;
	capacity = lst.capacity;
	lst.capacity = tempCapacity;
	
	int tempSize = mSize;
	mSize = lst.mSize;
	lst.mSize = tempSize;
	
	int tempHead = head;
	head = lst.head;
	lst.head = tempHead;
	
	int tempTail = tail;
	tail = lst.tail;
	lst.tail = tempTail;
	
	T* tempElement = element;
	element = lst.element;
	lst.element = tempElement;
	
	return;
}

/****************************************************************************
*																			*
*	Function:	seqSearch(T)												*
*																			*
*	Purpose:	This function will use a sequential search to find the      *
*               specified element and report its location					*
*																			*
*	Note:		If the element was not found, the function returns -1		*
*																			*
****************************************************************************/

template <class T>
int CircArrayList<T>::seqSearch(const T& value) const
{
	int realPos = head;
	
	if (mSize == 0)
		return 0;
		
	else
	{
		if (head <= tail)
			while (realPos <= tail && element[realPos] != value)
				realPos++;
		else
		{
			while (realPos < capacity && element[realPos] != value)
				realPos++;
			if (realPos == capacity)
			{
				realPos = 0;
				while (realPos <= tail && element[realPos] != value)
					realPos++;
			}
		}
	}
	
	int pos = adjIndex(realPos - head + capacity);
	
	if (pos >= 0 && pos < mSize)
		return pos;
	else
		return -1; 
}

/****************************************************************************
*																			*
*	Function:	binSearch(int, int, T)										*
*																			*
*	Purpose:	This function will use a binary search to find the          *
*               specified element and report its location					*
*																			*
*	Note:		If the element was not found, the function returns -1		*
*																			*
****************************************************************************/

template <class T>
int CircArrayList<T>::binSearch(const T& value)
{
	if (head > 0)
		setCapacity(capacity);
	
	int left = 0;
	int right = mSize - 1;
	int middle = (mSize - 1)/2;
	while (left <= right)
	{
		if (value < element[middle])
			right = middle - 1;
		else if (value > element[middle])
			left = middle + 1;
		else
			return middle;
		middle = (left + right)/2;
	}
	return -1;
}

/****************************************************************************
*																			*
*	Function:	selectionSort												*
*																			*
*	Purpose:	This function will sort the members of the list into 		*
*				ascending order												*
*																			*
****************************************************************************/

template <class T>
void CircArrayList<T>::selectionSort()
{
	if (head > 0)
		setCapacity(capacity);	// Put elements in positions 0 through n - 1

//	Perform a selection sort
	
	for (int i = 0; i < mSize - 1; i++)
	{
		int minPos = i;
		for (int j = i + 1; j < mSize; j++)
			if (element[j] < element[minPos])
				minPos = j;
		
		T temp = element[i];
		element[i] = element[minPos];
		element[minPos] = temp;
	}
	
	return;
}

/****************************************************************************
*																			*
*	Function:	mergeSort													*
*																			*
*	Purpose:	This function will sort the members of the list into 		*
*				ascending order												*
*																			*
****************************************************************************/

template <class T>
void CircArrayList<T>::mergeSort()
{
	if (head > 0)
	 	setCapacity(capacity);	// Put elements in positions 0 through n - 1

//	Perform a merge sort
	
	mergeSort(0, tail - 1);
	
	return;
}

template <class T>
void CircArrayList<T>::mergeSort(int left, int right)
{
	if (left < right)
	{
		int middle = (left + right)/2;
		mergeSort(left, middle);
		mergeSort(middle + 1, right);
		merge(left, middle, right);
	}
	return;
}

template <class T>
void CircArrayList<T>::merge(int left, int middle, int right)
{
	CircArrayList<T> list(right - left + 1);
	
	int i = left;
	int j = middle + 1;
	int k = 0;
	while (i <= middle && j <= right)
	{
		if (element[i] < element[j])
			list[k++] = element[i++];
		else
			list[k++] = element[j++];
	}
	while (i <= middle)
		list[k++] = element[i++];
	while (j <= right)
		list[k++] = element[j++];
		
	for (int i = 0; i < list.size(); i++)
		element[left + i] = list[i];
	return;
}

/****************************************************************************
*																			*
*	Function:	isValid														*
*																			*
*	Purpose:	To validate the structure of a circularly linked list		*
*																			*
****************************************************************************/

template <class T>
bool CircArrayList<T>::isValid() const
{
	if (capacity < 0)
	{
		cerr << "Error: (CircArrayList) capacity < 0" << endl;
		return false;
	}
	
	if (mSize < 0 || mSize > capacity)
	{
		cerr << "Error: (CircArrayList) mSize < 0 || mSize > capacity" << endl;
		return false;
	}
	
	if (capacity == 0)
	{
		if (head != 0 || tail != 0)
		{
			cerr << "Error: (CircArrayList) capacity == 0 && (head != 0 || tail != 0)" << endl;
			return false;
		}
	
		if (element != NULL)
		{
			cerr << "Error: (CircArrayList) capacity == 0 && element != NULL" << endl;
			return false;
		}
	}
	else
	{
		if (head < 0 || head >= capacity)
		{
			cerr << "Error: (CircArrayList) capacity > 0 && (head < 0 || head >= capacity)" << endl;
			return false;
		}
	
		if (tail < 0 || tail >= capacity)
		{
			cerr << "Error: (CircArrayList) capacity > 0 && (tail < 0 || tail >= capacity)" << endl;
			return false;
		}	
		
		if (mSize > 0)
			if (adjIndex(tail - head + capacity - 1) + 1 != mSize)
			{
				cerr << "Error: (CircArrayList) capacity > 0 && mSize > 0 && adjIndex(tail - head + capacity - 1) + 1 != mSize" << endl;
				return false;
			}
	}
	
	return true;
}

/****************************************************************************
*																			*
*	Function:	setCapacity													*
*																			*
*	Purpose:	To reset the size of the array								*
*																			*
****************************************************************************/

template <class T>
void CircArrayList<T>::setCapacity(int newCap)
{
	if (newCap == 0)
		makeEmpty();
	else if (capacity == 0)
	{
		capacity = newCap;
		mSize = 0;
		head = 0;
		tail = 0;
		element = new T[capacity];
	}
	else
	{
		T* new_list = new T[newCap];
		
		int numToCopy = min(mSize, newCap);
		if (numToCopy > 0)
		{
			int last = adjIndex(head + numToCopy - 1);
			copyElements(element, head, last, new_list);
		}

		capacity = newCap;
		head = 0;
		tail = adjIndex(numToCopy);
		mSize = numToCopy;
		delete [] element;
		element = new_list;
	}
	return;
}

/****************************************************************************
*																			*
*	Function:	makeCopy													*
*																			*
*	Purpose:	To make a copy of the nodes of a list						*
*																			*
****************************************************************************/

template <class T>
void CircArrayList<T>::makeCopy(const CircArrayList<T>& lst)
{
	if (lst.capacity == 0)
		makeEmpty();
	else
	{
		capacity = lst.capacity;
		mSize = lst.mSize;
		head = 0;
		tail = adjIndex(mSize);
		
		if (capacity == 0)
			element = NULL;
		else
			element = new T[capacity];
		
		if (mSize > 0)
			copyElements(lst.element, lst.head, lst.tail - 1, element);
	}
	return;
}

/****************************************************************************
*																			*
*	Function:	shiftLeft													*
*																			*
*	Purpose:	To shift the elements in the specified range one position	*
*				to the left													*
*																			*
****************************************************************************/

template <class T>
void CircArrayList<T>::shiftLeft(int start, int stop)
{
	start = adjIndex(--start + capacity);
	
	if (start < stop)
		for (int i = start; i < stop; i++)
			element[i] = element[i + 1];
	else
	{
		for (int i = start; i < capacity - 1; i++)
			element[i] = element[i + 1];
		element[capacity - 1] = element[0];
		for (int i = 0; i < stop; i++)
			element[i] = element[i + 1];
	}
	return;
}

/****************************************************************************
*																			*
*	Function:	shiftRight													*
*																			*
*	Purpose:	To shift the elements in the specified range one position	*
*				to the right												*
*																			*
****************************************************************************/

template <class T>
void CircArrayList<T>::shiftRight(int start, int stop)
{
	stop = adjIndex(++stop);
	
	if (start < stop)
		for (int i = stop; i > start; i--)
			element[i] = element[i - 1];
	else
	{
		for (int i = stop; i > 0; i--)
			element[i] = element[i - 1];
		element[0] = element[capacity - 1];
		for (int i = capacity - 1; i > start; i--)
			element[i] = element[i - 1];
	}
	return;
}

/****************************************************************************
*																			*
*	Function:	copyElements												*
*																			*
*	Purpose:	To copy the specified elements from the source array to		*
*				the destination array										*
*																			*
*	Note 1:		The elements will be copied into positions 0 through n - 1	*
*																			*
*	Note 2:		This function will always copy at least one element			*
*																			*
****************************************************************************/

template <class T>
void CircArrayList<T>::copyElements(T* src, int start, int stop, T* dst)
{
	if (start <= stop)
		for (int i = start, j = 0; i <= stop; i++, j++)
			dst[j] = src[i];
	else
	{
		for (int i = start, j = 0; i < capacity; i++, j++)
			dst[j] = src[i];
		for (int i = 0, j = capacity - start; i <= stop; i++, j++)
			dst[j] = src[i];
	}
	return;
}

/****************************************************************************
*																			*
*	Function:	adjIndex													*
*																			*
*	Purpose:	To return an equivalent index in the range 0 to 			*
*				capacity - 1												*
*																			*
****************************************************************************/

template <class T>
int CircArrayList<T>::adjIndex(int i) const
{
	if (i >= capacity)
		return i - capacity;
	else if (i < 0)
		return i + capacity;
	else return i;
}

/****************************************************************************
*																			*
*	Function:	operator>>													*
*																			*
*	Purpose:	To extract a doubly linked list from the input stream		*
*																			*
****************************************************************************/

template<class T>
istream& operator>>(istream& in, CircArrayList<T>& lst)
{
	lst.input(in);
	return in;
}

/****************************************************************************
*																			*
*	Function:	operator<<													*
*																			*
*	Purpose:	To insert a doubly linked list into the output stream		*
*																			*
****************************************************************************/

template<class T>
ostream& operator<<(ostream& out, const CircArrayList<T>& lst)
{
	lst.output(out);
	return out;
}

#endif
