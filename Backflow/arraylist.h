#ifndef ARRAYLIST_H
#define ARRAYLIST_H

//	Header files

#include <iostream>
#include <cassert>

using namespace std;

/****************************************************************************
 *																			*
 *	The ArrayList class definition											*
 *																			*
 ****************************************************************************/

template <class T>
class ArrayList
{
  //	Public member functions

public:

  //	Constructors

  ArrayList(int sz = 0, const T& value = T());
  ArrayList(const ArrayList<T>& lst) {makeCopy(lst);}


  //	Destructor

  ~ArrayList() {delete [] element;}

  //	Inspectors

  T getElement(int pos) const
    {assert(pos >= 0 && pos < mSize); return element[pos];}
  T& getElement(int pos)
    {assert(pos >= 0 && pos < mSize); return element[pos];}
  int size() const {return mSize;}
  bool isEmpty() const {return mSize == 0;}

  //	Mutators

  void setElement(int pos, const T& value) 
    {assert(pos >= 0 && pos < mSize); element[pos] = value;}
  void insert(int pos, const T& value);
  void remove(int pos);
  void pushFront(const T& value) {insert(0, value);}
  void pushBack(const T& value) {insert(mSize, value);}
  T popFront() {T value = getElement(0); remove(0); return value;}
  T popBack() {T value = getElement(mSize - 1); remove(mSize - 1); return value;}
  void makeEmpty()
    {delete [] element; capacity = 0; mSize = 0; element = NULL;}

  //	Facilitators

  void input(istream& in);
  void output(ostream& out) const;

  //	Operators

  ArrayList<T>& operator=(const ArrayList<T>& lst);
  T operator[](int pos) const
    {assert(pos >= 0 && pos < mSize); return element[pos];}
  T& operator[](int pos) 
    {assert(pos >= 0 && pos < mSize); return element[pos];}

  //	Other functions

  void swap(ArrayList<T>& lst);
  int search(const T& value) const;
  void sort() {sort(0, mSize - 1);}
  bool isValid() const;

  //	Protected member functions

protected:
  void setCapacity(int newCap);
  void makeCopy(const ArrayList<T>& lst);

  //	Private member functions

private:
  void sort(int left, int right);

  //	Data members

protected:
  int capacity;	// Allocated space
  int mSize;		// Number of items in list
  T* element;		// Pointer to first item
};

//	ArrayList operators

template <class T>
istream& operator>>(istream& in, ArrayList<T>& lst);
template <class T>
ostream& operator<<(ostream& out, const ArrayList<T>& lst);

/****************************************************************************
 *																			*
 *	Function:	ArrayList(int = 0, T& = T())								*
 *																			*
 *	Purpose:	This function will construct a list of the specified size	*
 *																			*
 ****************************************************************************/

template <class T>
ArrayList<T>::ArrayList(int sz, const T& value)
{
  assert(sz >= 0);

  capacity = sz;
  mSize = sz;

  if (mSize == 0)
    element = NULL;
  else
    element = new T[capacity];

  for (int i = 0; i < mSize; i++)
    element[i] = value;
  return;
}

/****************************************************************************
 *																			*
 *	Function:	insert														*
 *																			*
 *	Purpose:	This function will insert an element into the list at the	*
 *				specified position											*
 *																			*
 ****************************************************************************/

template <class T>
void ArrayList<T>::insert(int pos, const T& value)
{
  assert(pos >= 0 && pos <= mSize);

  if (mSize == capacity)
    if (capacity == 0)
      setCapacity(1);
    else
      setCapacity(2 * capacity);

  for (int i = mSize - 1; i >= pos; i--)
    element[i + 1] = element[i];

  element[pos] = value;
  mSize++;
  return;
}

/****************************************************************************
 *																			*
 *	Function:	remove														*
 *																			*
 *	Purpose:	This function will delete the element in the specified 		*
 *				position													*
 *																			*
 ****************************************************************************/

template <class T>
void ArrayList<T>::remove(int pos)
{
  assert(pos >= 0 && pos < mSize);

  for (int i = pos + 1; i < mSize; i++)
    element[i - 1] = element[i];

  mSize--;

  if (mSize <= capacity/4)
    setCapacity(capacity/2);

  return;
}

/****************************************************************************
 *																			*
 *	Function:	input														*
 *																			*
 *	Purpose:	This function will extract a list from the input stream		*
 *																			*
 ****************************************************************************/

template <class T>
void ArrayList<T>::input(istream& in)
{
  makeEmpty();			// Clear out old values
  char c;
  T value;

  in >> c;				// Read the first character
  assert(c == '{');		// Verify it is open brace

  in >> c;				// Read next character
  if (c == '}')			// List is empty
    return;				// Nothing else to read

  else					// List is not empty
    {
      in.putback(c);		// Put char back in input stream
      while(c != '}')		// Read until '}' is found
        {
          in >> value;	// Read the element
          pushBack(value);// Append the element to the list
          in >> c;		// Read the comma or close brace
        }
    }
  return;
}

/****************************************************************************
 *																			*
 *	Function:	output														*
 *																			*
 *	Purpose:	This function will insert a list into the output stream		*
 *																			*
 ****************************************************************************/

template <class T>
void ArrayList<T>::output(ostream& out) const
{
  out << "{";							// Write open brace
  if (mSize > 0)
    {
      out << element[0];
      for (int i = 1; i < mSize; i++)
        out << ", " << element[i];	// Write element and comma
    }
  out << "}";							// Write close brace
  return;
}

/****************************************************************************
 *																			*
 *	Function:	operator=													*
 *																			*
 *	Purpose:	This function will assign one list to another				*
 *																			*
 ****************************************************************************/

template <class T>
ArrayList<T>& ArrayList<T>::operator=(const ArrayList<T>& lst)
{
  if (this != &lst)						// Lists are distinct
    {
      delete [] element;					// Clear out old elements
      makeCopy(lst);
    }
  return *this;
}

/****************************************************************************
 *																			*
 *	Function:	swap														*
 *																			*
 *	Purpose:	This function will swap this list with the specified list	*
 *																			*
 ****************************************************************************/

template <class T>
void ArrayList<T>::swap(ArrayList<T>& lst)
{

  //	Perform a swap of the three data members, not the allocated memory

  int tempMaxSize = lst.capacity;
  int tempSize = lst.mSize;
  T* tempElement = lst.element;

  lst.capacity = capacity;
  lst.mSize = mSize;
  lst.element = element;

  capacity = tempMaxSize;
  mSize = tempSize;
  element = tempElement;
  return;
}

/****************************************************************************
 *																			*
 *	Function:	search(T)													*
 *																			*
 *	Purpose:	This function will use a sequential search to search the 	*
 *				unsorted list for the specified element and report its 		*
 *				location													*
 *																			*
 *	Note:		If the element was not found, the function returns 0		*
 *																			*
 ****************************************************************************/

template <class T>
int ArrayList<T>::search(const T& value) const 
{
  int pos;
  for (pos = 0; pos < mSize; pos++)
    if (element[pos] == value)
      return pos;
  return -1;
}

/****************************************************************************
 *																			*
 *	Function:	sort(int, int)												*
 *																			*
 *	Purpose:	This function will use the quicksort algorithm to sort 		*
 *				the list recursively										*
 *																			*
 ****************************************************************************/

template <class T>
void ArrayList<T>::sort(int left, int right)
{
  if (left < right)
    {
      T temp;		// Used to swap list elements

      //	Pivot

      if (element[right] < element[left])
        {
          temp = element[right];
          element[right] = element[left];
          element[left] = temp;
        }

      //	Partition the list

      T pivot = element[left];
      int i = left;
      int j = right + 1;
      do
        {
          do ++i; while (element[i] < pivot);
          do --j; while (pivot < element[j]);
          if (i < j)
            {
              temp = element[i];
              element[i] = element[j];
              element[j] = temp;
            }
        } while (i < j);
          temp = element[j];
          element[j] = element[left];
          element[left] = temp;

          //	Sort the sublists recursively

          sort(left, j - 1);
          sort(j + 1, right);
    }
  return;
}

/****************************************************************************
 *																			*
 *	Function:	isValid														*
 *																			*
 *	Purpose:	To validate that the arraylist has a valid structure		*
 *																			*
 ****************************************************************************/

template <class T>
bool ArrayList<T>::isValid() const
{
  if (capacity < 0)
    {
      cerr << "Error: ArrayList<T>: capacity < 0" << endl;
      return false;
    }

  if (mSize < 0 || mSize > capacity)
    {
      cerr << "Error: ArrayList<T>: mSize < 0 || mSize > capacity" << endl;
      return false;
    }

  if (capacity == 0 && element != NULL)
    {
      cerr << "Error: ArrayList<T>: capacity == 0 && element != NULL" << endl;
      return false;
    }

  if (capacity > 0 && element == NULL)
    {
      cerr << "Error: ArrayList<T>: capacity > 0 && element == NULL" << endl;
      return false;
    }

  return true;
}

/****************************************************************************
 *																			*
 *	Function:	setCapacity													*
 *																			*
 *	Purpose:	This function will reset the maximum size of the list to 	*
 *				the specified size											*
 *																			*
 ****************************************************************************/

template <class T>
void ArrayList<T>::setCapacity(int newCap)
{
  //	Verify that new capacity is valid

  assert(newCap >= 0);

  capacity = newCap;
  T* new_array;

  //	Allocate memory and copy elements

  if (capacity == 0)
    {
      mSize = 0;
      new_array = NULL;
    }
  else
    {
      //	Allocate memory

      new_array = new T[capacity];

      //	Get new size

      mSize = min(capacity, mSize);

      //	Copy elements

      for (int i = 0; i < mSize; i++)
        new_array[i] = element[i];
    }

  //	Delete old memory

  delete [] element;
  element = new_array;
  return;
}

/****************************************************************************
 *																			*
 *	Function:	makeCopy													*
 *																			*
 *	Purpose:	This function will set this list equal to a copy of the 	*
 *				specified list												*
 *																			*
 ****************************************************************************/

template <class T>
void ArrayList<T>::makeCopy(const ArrayList<T>& lst)
{
  capacity = lst.capacity;
  mSize = lst.mSize;

  if (capacity == 0)
    element = NULL;
  else
    element = new T[capacity];

  for (int i = 0; i < mSize; i++)
    element[i] = lst.element[i];

  return;
}

/****************************************************************************
 *																			*
 *	Function:	operator>>													*
 *																			*
 *	Purpose:	This function will extract a list from the input stream		*
 *																			*
 ****************************************************************************/

template <class T>
istream& operator>>(istream& in, ArrayList<T>& lst)
{
  lst.input(in);
  return in;
}

/****************************************************************************
 *																			*
 *	Function:	operator<<													*
 *																			*
 *	Purpose:	This function will insert a list into the output stream		*
 *																			*
 ****************************************************************************/

template <class T>
ostream& operator<<(ostream& out, const ArrayList<T>& lst)
{
  lst.output(out);
  return out;
}

#endif
